package com.ombremoon.epickombat.world.capability.input;

import com.mojang.blaze3d.platform.InputConstants;
import com.ombremoon.epickombat.client.KeyBinds;
import com.ombremoon.epickombat.config.ConfigHandler;
import com.ombremoon.epickombat.main.Constants;
import com.ombremoon.epickombat.skill.KombatSlots;
import com.ombremoon.epickombat.util.KombatUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.InputEvent;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

import java.util.Map;

public class InputReader {
    private static final Map<KeyMapping, Input> COMBAT_MAPPINGS = new Object2ObjectOpenHashMap<>();
    private final Minecraft minecraft;
    private final LocalPlayer player;
    private final LocalPlayerPatch playerPatch;
    private final ControllEngine engine;
    private final InputCache cache;
    private boolean tickWindow;
    private int ticksSinceLastInput;
    private boolean addToCache;

    public InputReader(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.player = minecraft.player;
        this.playerPatch = EpicFightCapabilities.getEntityPatch(player, LocalPlayerPatch.class);
        this.engine = ClientEngine.getInstance().controllEngine;
        this.cache = new InputCache(playerPatch);
    }

    public void tick() {
        Constants.LOG.debug("{}", this.cache);

        //BASIC ATTACK & SPECIAL LOGIC

        boolean flag = true;
        int timing = ConfigHandler.INPUT_TIMING.get().getDuration();
        for (var entry : COMBAT_MAPPINGS.entrySet()) {
            while (keyPressed(entry.getKey(), true)) {
                if (this.playerPatch.isBattleMode()) {
                    SkillSlot slot = (!this.player.onGround() && !this.player.isInWater()) ? KombatSlots.AERIAL : KombatSlots.BASIC;

                    this.cache.cacheInput(entry.getValue());
                    if (this.cache.getFirst().isMovement()) {
                        this.handleMovementInputs();
                        flag = false;
                    } else {
                        if (this.playerPatch.getSkill(slot).sendExecuteRequest(this.playerPatch, this.engine).isExecutable()) {
                            this.player.resetAttackStrengthTicker();
                        }
                    }

                    engine.lockHotkeys();
                }
            }
        }

        if (!this.cache.isEmpty() && this.cache.getInput().isMovement() && this.cache.size() > 2 && flag)
            this.handleMovementInputs();

        //KEYBIND HANDLING

        while (keyPressed(this.minecraft.options.keyLeft, false)) {
            this.cacheInput(Input.A, false);
        }

        while (keyPressed(this.minecraft.options.keyRight, false)) {
            this.cacheInput(Input.D, false);
        }

        while (keyPressed(this.minecraft.options.keyShift, false)) {
            this.cacheInput(Input.CROUCH, false);
        }

        while (keyPressed(this.minecraft.options.keyUp, false)) {
            this.cacheInput(Input.W, true);
        }

        while (keyPressed(this.minecraft.options.keyDown, false)) {
            this.cacheInput(Input.S, true);
        }

        //WINDOW LOGIC

        if (this.tickWindow) {
            if (this.ticksSinceLastInput == timing && !this.addToCache) {
                this.ticksSinceLastInput = 0;
                this.addToCache = true;
            } else if (this.ticksSinceLastInput > timing) {
                this.cache.clearCache();
                this.tickWindow = false;
                this.addToCache = true;
                this.ticksSinceLastInput = 0;
            } else {
                this.ticksSinceLastInput++;
            }
        } else {
            this.ticksSinceLastInput = 0;
            this.addToCache = true;
            if (!this.cache.isEmpty() && this.cache.getFirst().isMovement())
                this.cache.clearCache();
        }
    }

    public InputCache getCache() {
        return this.cache;
    }

    private void cacheInput(Input input, boolean mustHaveActiveWindow) {
        int timing = ConfigHandler.INPUT_TIMING.get().getDuration();
        if (mustHaveActiveWindow && !this.tickWindow)
            return;

        if (this.cache.isEmpty() || (this.ticksSinceLastInput < timing && this.addToCache)) {
            this.cache.cacheInput(input);
            this.addToCache = false;
            this.tickWindow = true;
            this.ticksSinceLastInput = 0;
        } else if (this.ticksSinceLastInput < timing) {
            this.cache.appendLast(input);
        }
    }

    private void handleMovementInputs() {
        Input firstInput = this.cache.getFirst();
        Input input = this.cache.getInput();
        var kombat = KombatUtil.getKombat(minecraft.player);
        var combos = KombatUtil.getFighter(minecraft.player).getSpecialCombo(firstInput);

        if (this.cache.getInput().getInput().contains("ccc"))
            return;

        if (this.cache.getInput().getInput().contains("cccc")) {
            playerPatch.playAnimationClientPreemptive(kombat.getFighter().getTaunt(playerPatch.getOriginal()), 0.0F);
            this.cache.clearCache();
            return;
        }

        for (SkillCombo combo : combos) {
            Input comboInput = firstInput.append(combo.inputs());
            if (comboInput.equals(input)) {
                SkillContainer special = playerPatch.getSkill(KombatSlots.SPECIAL);
                kombat.changeSpecialSkill(combo.skill());
                if (special.sendExecuteRequest(playerPatch, engine).isExecutable())
                    engine.lockHotkeys();

                this.cache.clearCache();
                return;
            }
        }

        for (SkillCombo combo : combos) {
            Input comboInput = firstInput.append(combo.inputs());
            if (input.isPartialMatch(comboInput)) {
                return;
            }
        }

        this.cache.clearCache();
        this.tickWindow = false;
        this.ticksSinceLastInput = 0;
        this.addToCache = true;
    }

    private static boolean keyPressed(KeyMapping key, boolean eventCheck) {
        boolean consumes = key.consumeClick();

        if (consumes && eventCheck) {
            int mouseButton = InputConstants.Type.MOUSE == key.getKey().getType() ? key.getKey().getValue() : -1;
            InputEvent.InteractionKeyMappingTriggered inputEvent = ForgeHooksClient.onClickInput(mouseButton, key, InteractionHand.MAIN_HAND);

            if (inputEvent.isCanceled()) {
                return false;
            }
        }

        return consumes;
    }

    static {
        COMBAT_MAPPINGS.put(KeyBinds.FRONT_PUNCH_BINDING, Input.FP);
        COMBAT_MAPPINGS.put(KeyBinds.BACK_PUNCH_BINDING, Input.BP);
        COMBAT_MAPPINGS.put(KeyBinds.FRONT_KICK_BINDING, Input.FK);
        COMBAT_MAPPINGS.put(KeyBinds.BACK_KICK_BINDING, Input.BK);
    }
}
