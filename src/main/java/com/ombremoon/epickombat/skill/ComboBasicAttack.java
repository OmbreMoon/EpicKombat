package com.ombremoon.epickombat.skill;

import com.ombremoon.epickombat.main.Constants;
import com.ombremoon.epickombat.util.KombatUtil;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class ComboBasicAttack extends Skill {

    @SuppressWarnings("unchecked")
    public static Skill.Builder<ComboBasicAttack> createComboBasicAttackBuilder() {
        return (new Skill.Builder()).setCategory(KombatCategories.BASIC).setActivateType(ActivateType.ONE_SHOT).setResource(Resource.NONE);
    }

    public ComboBasicAttack(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
    }

    @Override
    public void executeOnServer(ServerPlayerPatch executor, FriendlyByteBuf args) {
        super.executeOnServer(executor, args);
//        Input input = Input.read(args);
        Constants.LOG.info("Skill: {}", 3);
    }

    @Override
    public void executeOnClient(LocalPlayerPatch executor, FriendlyByteBuf args) {
        var kombat = KombatUtil.getKombat(executor.getOriginal());
//        if (executor.getOriginal().tickCount % 5 == 0)
//            kombat.getInputs().reset(true);
    }

    @Override
    public void onReset(SkillContainer container) {
        super.onReset(container);
    }

    @Override
    public FriendlyByteBuf gatherArguments(LocalPlayerPatch executor, ControllEngine controllEngine) {
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());

//        var kombat = KombatUtil.getKombat(executor.getOriginal());
//        Input input = kombat.getInputs().getCurrentInput();
//        if (input != null)
//            input.write(buf);

        return buf;
    }
}
