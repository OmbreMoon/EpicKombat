package com.ombremoon.epickombat.skill;

import com.ombremoon.epickombat.main.Constants;
import com.ombremoon.epickombat.networking.NetworkManager;
import com.ombremoon.epickombat.util.KombatUtil;
import com.ombremoon.epickombat.world.capability.Fighter;
import com.ombremoon.epickombat.world.capability.KombatKapability;
import com.ombremoon.epickombat.world.capability.input.Combo;
import com.ombremoon.epickombat.world.capability.input.Input;
import com.ombremoon.epickombat.world.capability.input.InputCache;
import com.ombremoon.epickombat.world.capability.input.InputReader;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.BasicAttackEvent;
import yesman.epicfight.world.entity.eventlistener.ComboCounterHandleEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;
import yesman.epicfight.world.entity.eventlistener.SkillConsumeEvent;

import java.util.List;
import java.util.UUID;

public class ComboBasicAttack extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("bad13d1e-7afa-4004-b447-17b0497f6354");

    @SuppressWarnings("unchecked")
    public static Skill.Builder<ComboBasicAttack> createComboBasicAttackBuilder() {
        return (new Skill.Builder()).setCategory(KombatCategories.BASIC).setActivateType(ActivateType.ONE_SHOT).setResource(Resource.NONE);
    }

    public ComboBasicAttack(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    public static void setComboCounterWithEvent(ComboCounterHandleEvent.Causal reason, ServerPlayerPatch playerpatch, SkillContainer container, StaticAnimation causalAnimation, int value) {
        int prevValue = container.getDataManager().getDataValue(EpicKombatDataKeys.COMBO_COUNTER.get());
        ComboCounterHandleEvent comboResetEvent = new ComboCounterHandleEvent(reason, playerpatch, causalAnimation, prevValue, value);
        container.getExecuter().getEventListener().triggerEvents(PlayerEventListener.EventType.COMBO_COUNTER_HANDLE_EVENT, comboResetEvent);
        container.getDataManager().setDataSync(EpicKombatDataKeys.COMBO_COUNTER.get(), comboResetEvent.getNextValue(), playerpatch.getOriginal());
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.ANIMATION_END_EVENT, EVENT_UUID, event -> {
            container.getDataManager().setData(EpicKombatDataKeys.BASIC_ATTACK_ACTIVATE.get(), false);
            if (event.isEnd() && container.getExecuter().isLogicalClient()) {
                KombatKapability kombat = KombatUtil.getKombat(event.getPlayerPatch().getOriginal());
                InputReader inputs = kombat.getInputs();
                InputCache cache = inputs.getCache();
                int comboCounter = container.getDataManager().getDataValue(EpicKombatDataKeys.COMBO_COUNTER.get());
                if (comboCounter < cache.size()) {
                    LocalPlayerPatch playerPatch = (LocalPlayerPatch) event.getPlayerPatch();
                    if (playerPatch.getSkill(KombatSlots.BASIC).sendExecuteRequest(playerPatch, ClientEngine.getInstance().controllEngine).isExecutable())
                        playerPatch.getOriginal().resetAttackStrengthTicker();
                } else {
                    inputs.reset(true);
                    cache.clearCache();
                }
            }
        });
    }

    @Override
    public boolean isExecutableState(PlayerPatch<?> executor) {
        EntityState state = executor.getEntityState();
        Player player = executor.getOriginal();

        return !(player.isSpectator() || executor.isInAir() || !state.canBasicAttack());
    }

    @Override
    public void executeOnServer(ServerPlayerPatch executor, FriendlyByteBuf args) {
        SkillConsumeEvent event = new SkillConsumeEvent(executor, this, this.resource);
        executor.getEventListener().triggerEvents(PlayerEventListener.EventType.SKILL_CONSUME_EVENT, event);

        if (!event.isCanceled())
            event.getResourceType().consumer.consume(this, executor, event.getAmount());

        if (executor.getEventListener().triggerEvents(PlayerEventListener.EventType.BASIC_ATTACK_EVENT, new BasicAttackEvent(executor)))
            return;

        Fighter fighter = KombatUtil.getKombat(executor.getOriginal()).getFighter();
        StaticAnimation attackMotion = null;
        ServerPlayer player = executor.getOriginal();
        SkillContainer skillContainer = executor.getSkill(this);
        SkillDataManager dataManager = skillContainer.getDataManager();
        Input firstInput = dataManager.getDataValue(EpicKombatDataKeys.FIRST_INPUT.get());
        List<Input> cache = InputCache.deserialize(args);
        int comboCounter = dataManager.getDataValue(EpicKombatDataKeys.COMBO_COUNTER.get());

        if (player.isPassenger())
            return;

        if (hasValidFirstInput(executor)) {
            List<Combo> combos = fighter.getBasicCombo(firstInput);
            if (combos.isEmpty()) NetworkManager.clearCache(player);

            Combo combo = combos.get(comboCounter);

            if (combo.inputs() != null) {
                int size = combo.inputs().size();
                if (comboCounter > size) {
                    NetworkManager.clearCache(player);
                } else {
                    Input comboInput = combo.inputs().get(comboCounter - 1);
                    Input playerInput = cache.get(comboCounter);
                    if (comboInput.equals(playerInput)) {
                        attackMotion = combo.animation().get();
                    } else {
                        NetworkManager.clearCache(player);
                    }
                    Constants.LOG.debug("{}", combo);
                }
            } else {
                attackMotion = combo.animation().get();
            }
        }

        comboCounter++;
        setComboCounterWithEvent(ComboCounterHandleEvent.Causal.ANOTHER_ACTION_ANIMATION, executor, skillContainer, attackMotion, comboCounter);

        if (attackMotion != null) {
            executor.playAnimationSynchronized(attackMotion, 0);
            dataManager.setData(EpicKombatDataKeys.BASIC_ATTACK_ACTIVATE.get(), true);
        } else {
            setComboCounterWithEvent(ComboCounterHandleEvent.Causal.ANOTHER_ACTION_ANIMATION, executor, skillContainer, null, 0);
            NetworkManager.clearCache(player);
        }

        executor.updateEntityState();
    }

    @Override
    public FriendlyByteBuf gatherArguments(LocalPlayerPatch executor, ControllEngine controllEngine) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        var kombat = KombatUtil.getKombat(executor.getOriginal());
        var cache = kombat.getInputs().getCache();
        cache.serialize(buf);
        if (!hasValidFirstInput(executor)) {
            SkillContainer container = executor.getSkill(this);
            container.getDataManager().setDataSync(EpicKombatDataKeys.FIRST_INPUT.get(), cache.getFirst(), executor.getOriginal());
        }

        return buf;
    }

    protected boolean hasValidFirstInput(PlayerPatch<?> executor) {
        SkillContainer container = executor.getSkill(this);
        return container.getDataManager().getDataValue(EpicKombatDataKeys.FIRST_INPUT.get()) != Input.EMPTY;
    }

    @Override
    public void updateContainer(SkillContainer container) {
        if (!container.getExecuter().isLogicalClient() && container.getExecuter().getTickSinceLastAction() > 16 && container.getDataManager().getDataValue(EpicKombatDataKeys.COMBO_COUNTER.get()) > 0) {
            setComboCounterWithEvent(ComboCounterHandleEvent.Causal.TIME_EXPIRED, (ServerPlayerPatch)container.getExecuter(), container, null, 0);
        }
    }
}
