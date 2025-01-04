package com.ombremoon.epickombat.world.capability.input;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.ombremoon.epickombat.client.KeyBinds;
import com.ombremoon.epickombat.config.ConfigHandler;
import com.ombremoon.epickombat.main.Constants;
import com.ombremoon.epickombat.skill.KombatSlots;
import com.ombremoon.epickombat.util.KombatUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.InputEvent;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class InputReader {
    private static final List<KeyMapping> COMBAT_MAPPINGS = Lists.newArrayList();
    private final Minecraft minecraft;
    private final LocalPlayerPatch playerPatch;
    private final ControllEngine engine;
    public final InputCache cache;
    private final List<Input> attackInputs = new ObjectArrayList<>();
    private final Set<Input> directionalInputs = new LinkedHashSet<>();
    private boolean tickWindows;
    public int tickSinceLastInput = 0;
    private boolean activeWindow;
    private boolean initString;
    private Input currentInput;
    private Input prevInput;
    private Input firstInput;
    private int inputTimer;
    private int inputId = 0;

    public InputReader(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.playerPatch = EpicFightCapabilities.getEntityPatch(minecraft.player, LocalPlayerPatch.class);
        this.engine = ClientEngine.getInstance().controllEngine;
        this.cache = new InputCache(playerPatch);
        this.currentInput = new Input();
        this.prevInput = new Input();
        this.firstInput = new Input();
    }

    public void tick() {
//        Constants.LOG.debug("{}", tickSinceLastInput);
        Constants.LOG.debug("{}", cache.size());
//        cache.clearCache();
        if (playerPatch.getSkill(KombatSlots.FIGHTER) == null || !playerPatch.isBattleMode())
            return;

        int timing = ConfigHandler.INPUT_TIMING.get().getDuration();
        if (this.tickWindows && inputTimer <= 0) {
            this.tickSinceLastInput++;

            if (this.tickSinceLastInput >= timing)
                this.activeWindow = false;

            boolean flag = this.initString/* && this.tickSinceLastInput == 1*/;
            if (!this.activeWindow || flag) {
                if (this.firstInput.isEmpty())
                    this.firstInput = this.createString();

                if (this.firstInput.equals(Input.W) || this.firstInput.equals(Input.S) || this.firstInput.equals(Input.JUMP)) {
                    this.reset(true);
                    return;
                }

                if (this.tickSinceLastInput == timing || flag) {
                    this.updateString();

                    if (!this.firstInput.isMovement()) {
                        if (this.inputId < 2) {
                            if (playerPatch.getSkill(KombatSlots.BASIC).sendExecuteRequest(playerPatch, engine).isExecutable())
                                playerPatch.getOriginal().resetAttackStrengthTicker();

                            engine.lockHotkeys();
                        }
                    } else {
                        if (this.inputId > 2)
                            this.handleMovementInputs();
                    }

                    if (this.currentInput.isEmpty() || this.currentInput.equals(this.prevInput)) {
                        this.reset(true);
                        return;
                    }


                    if (flag) {
                        this.prevInput = this.currentInput;
                        this.activeWindow = true;
                        this.reset();
                        return;
                    }
                }
            } else {
                this.updateInputs(false);
            }

            if (this.tickSinceLastInput >= timing + 1) {
                if (!this.currentInput.isEmpty()) {
                    this.activeWindow = true;
                    this.prevInput = this.currentInput;
                }
                this.reset(!this.isWindowActive());
            }
        } else {
            this.updateInputs(true);
        }

        this.validateInputs();
        if (this.inputTimer > 0)
            this.inputTimer--;
    }

    private void handleMovementInputs() {
        var kombat = KombatUtil.getKombat(minecraft.player);
        var combos = KombatUtil.getFighter(minecraft.player).getSpecialCombo(this.firstInput);

        Constants.LOG.debug("{}", this.currentInput);
        if (this.currentInput.getInput().equalsIgnoreCase("cccc")) {
            playerPatch.playAnimationClientPreemptive(kombat.getFighter().getTaunt(playerPatch.getOriginal()), 0.0F);
            this.reset(true);
            this.cache.clearCache();
        }

        boolean foundMatch = false;
        for (SkillCombo combo : combos) {
            Input comboInput = this.firstInput.append(combo.inputs());
            if (this.currentInput.equals(comboInput)) {
                SkillContainer special = playerPatch.getSkill(KombatSlots.SPECIAL);
                kombat.changeSpecialSkill(combo.skill());
                if (special.sendExecuteRequest(playerPatch, engine).isExecutable())
                    engine.lockHotkeys();

                foundMatch = true;
                this.currentInput.clear();
                this.cache.clearCache();
                break;
            }
        }

        if (!foundMatch && !this.currentInput.isEmpty()) {
            for (SkillCombo combo : combos) {
                Input comboInput = this.firstInput.append(combo.inputs());
                if (this.currentInput.isPartialMatch(comboInput)) {
                    foundMatch = true;
                    break;
                }
            }

            if (this.currentInput.getInput().equalsIgnoreCase("ccc"))
                foundMatch = true;
        }

        if (!foundMatch)
            this.reset(true);
    }

    private Input createString() {
        Input input = new Input();
        for (Input dirInput : this.directionalInputs) {
            input = input.append(dirInput);
        }
        for (Input combatInput : this.attackInputs) {
            input = input.append(combatInput);
        }

        String s = Input.sortString(input.getInput());
        return new Input(s, input.isMovement(), input.size());
    }

    private void updateString() {
        Input input = this.createString();
        if (this.currentInput.canAppend(input) && !input.isEmpty()) {
            this.currentInput = this.currentInput.append(input);
            this.cache.cacheInput(input);
            playerPatch.getOriginal().sendSystemMessage(Component.literal(input.getInput()));
        } else {
            this.currentInput.clear();
        }
        this.inputId++;
        this.initString = false;
    }

    private void updateInputs(boolean startString) {
        Options options = this.minecraft.options;
        pressKey(KeyBinds.FRONT_PUNCH_BINDING, Input.FP, startString);
        pressKey(KeyBinds.BACK_PUNCH_BINDING, Input.BP, startString);
        pressKey(KeyBinds.FRONT_KICK_BINDING, Input.FK, startString);
        pressKey(KeyBinds.BACK_KICK_BINDING, Input.BK, startString);
        pressKey(options.keyUp, Input.W, startString);
        pressKey(options.keyLeft, Input.A, startString);
        pressKey(options.keyDown, Input.S, startString);
        pressKey(options.keyRight, Input.D, startString);
        pressKey(options.keyJump, Input.JUMP, startString);
        pressKey(options.keyShift, Input.CROUCH, startString);
    }

    private boolean isHeldInput(List<Input> inputs) {
        return inputs.size() == 2 && inputs.get(0) == inputs.get(1);
    }

    public void pressKey(KeyMapping key, Input input) {
        this.pressKey(key, input, false);
    }

    private void pressKey(KeyMapping key, Input input, boolean startWindow) {
        boolean flag = KombatUtil.hasFighterWeapon(playerPatch.getOriginal());

        while (keyPressed(key)) {
            Constants.LOG.debug("{} {}", this.tickSinceLastInput, input);
            int timing = ConfigHandler.INPUT_TIMING.get().getDuration();
            if (this.tickSinceLastInput >= timing) {
                this.reset(true);
                Constants.LOG.debug("LARVA");
            }

            if (startWindow && flag) {
                this.tickWindows = true;
                this.activeWindow = true;
                this.initString = true;
            }

            if (!flag) break;

            if (input.isMovement()) {
                this.addDirectionalInput(input);
            } else {
                this.addAttackInput(input);
            }
        }
    }

    private static boolean keyPressed(KeyMapping key) {
        boolean consumes = key.consumeClick();

        if (consumes) {
            int mouseButton = InputConstants.Type.MOUSE == key.getKey().getType() ? key.getKey().getValue() : -1;
            InputEvent.InteractionKeyMappingTriggered inputEvent = ForgeHooksClient.onClickInput(mouseButton, key, InteractionHand.MAIN_HAND);

            if (inputEvent.isCanceled()) {
                return false;
            }
        }

        return consumes;
    }

    private void addAttackInput(Input input) {
        if (this.attackInputs.size() >= 2) return;
        this.attackInputs.add(input);
    }

    private void addDirectionalInput(Input input) {
        List<Input> inputs = Lists.newArrayList(this.directionalInputs);
        if (inputs.size() == 1 && input.isOpposite(inputs.get(0)))
            this.directionalInputs.clear();

        if (this.directionalInputs.size() < 2)
            this.directionalInputs.add(input);
    }

    private void validateInputs() {
        if (!this.tickWindows && (!this.firstInput.isEmpty() || !this.currentInput.isEmpty() || !this.directionalInputs.isEmpty() || !this.attackInputs.isEmpty())) {
            this.clearInputs();
            this.clearStrings();
        }
    }

    private void reset() {
        if (!this.attackInputs.isEmpty()) {
            for (KeyMapping key : COMBAT_MAPPINGS) {
                key.release();
            }
        }

        this.clearInputs();
        this.tickSinceLastInput = 0;
    }

    public void reset(boolean hardReset) {
        this.reset();
        if (hardReset) {
            this.tickWindows = false;
            this.clearStrings();
            this.inputId = 0;
            this.inputTimer = 3;
        }
    }

    private void clearInputs() {
        this.directionalInputs.clear();
        this.attackInputs.clear();
    }

    private void clearStrings() {
        this.currentInput.clear();
        this.prevInput.clear();
        this.firstInput.clear();
    }

    public boolean isWindowActive() {
        return this.activeWindow;
    }

    public InputCache getCache() {
        return this.cache;
    }

    public Input getCurrentInput() {
        return this.currentInput;
    }

    public Input getFirstInput() {
        return this.firstInput;
    }

    static {
        COMBAT_MAPPINGS.add(KeyBinds.FRONT_PUNCH_BINDING);
        COMBAT_MAPPINGS.add(KeyBinds.BACK_PUNCH_BINDING);
        COMBAT_MAPPINGS.add(KeyBinds.FRONT_KICK_BINDING);
        COMBAT_MAPPINGS.add(KeyBinds.BACK_KICK_BINDING);
    }
}
