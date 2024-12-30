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

//TODO: MAKE SURE TO VALIDATE FIRST INPUT ON FIRST CLICK/TICK AND THEN START WINDOWS
//TODO: ADD WINDOW COUNT TO KEEP TRACK OF WHAT HOW MANY INPUTS HAVE BEEN REGISTERED

public class InputReader {
    private static final int INACTIVE_WINDOW = 2;
    private static final List<KeyMapping> COMBAT_MAPPINGS = Lists.newArrayList();
    private final Minecraft minecraft;
    private final LocalPlayerPatch playerPatch;
    private final ControllEngine engine;
//    public final InputCache cache;
    private final List<Input> attackInputs = new ObjectArrayList<>();
    private final Set<Input> directionalInputs = new LinkedHashSet<>();
    private boolean tickWindows;
    public int tickSinceLastInput = 0;
    private boolean activeWindow;
    private boolean initString;
    private Input currentInput;
    private Input prevInput;
    private Input firstInput;
    private int inputId = 0;

    public InputReader(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.playerPatch = EpicFightCapabilities.getEntityPatch(minecraft.player, LocalPlayerPatch.class);
        this.engine = ClientEngine.getInstance().controllEngine;
        this.currentInput = new Input();
        this.prevInput = new Input();
        this.firstInput = new Input();
//        this.cache = new InputCache();
    }

    public void tick() {
        if (playerPatch.getSkill(KombatSlots.FIGHTER) == null || !playerPatch.isBattleMode())
            return;

        int timing = ConfigHandler.INPUT_TIMING.get().getDuration();
        if (this.tickWindows) {
            this.tickSinceLastInput++;

            if (this.tickSinceLastInput >= 3)
                this.activeWindow = false;

            this.updateInputs(false);
            boolean flag = this.initString && this.tickSinceLastInput == 1;
            if (!this.activeWindow || flag) {
                if (this.firstInput.isEmpty())
                    this.firstInput = this.createString();

                if (this.tickSinceLastInput == 3 || flag) {
                    this.updateString();

                    int size = this.currentInput.size();
                    if (!this.firstInput.isMovement()) {
                        //Basic Attack Logic
                    } else {
                        if (this.inputId > 2)
                            this.handleMovementInputs();
                    }

                    if (this.currentInput.isEmpty() || this.currentInput.equals(this.prevInput)) {
                        this.reset(true);
                        return;
                    }

                    if (flag)
                        this.reset();
                }
            }

            if (this.tickSinceLastInput >= 4) {
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
//        Constants.LOG.debug("{}", this.tickSinceLastInput);
//        Constants.LOG.debug("{}", this.currentInput);
        Constants.LOG.debug("{}", this.inputId);
    }

    private void handleMovementInputs() {
        var kombat = KombatUtil.getKombat(minecraft.player);
        var combos = KombatUtil.getFighter(minecraft.player).getSpecialCombo(this.firstInput);

        Constants.LOG.debug("{}", this.currentInput);
        if (this.currentInput.getInput().equalsIgnoreCase("cccc")) {
            playerPatch.playAnimationClientPreemptive(kombat.getFighterInfo().getTaunt(playerPatch.getOriginal()), 0.0F);
            this.reset(true);
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
        if (this.currentInput.canAppend(input)) {
            this.currentInput = this.currentInput.append(input);
            if (!input.isEmpty()) {
                playerPatch.getOriginal().sendSystemMessage(Component.literal(input.getInput()));
                this.inputId++;
            }
        } else {
            this.currentInput.clear();
        }
        this.initString = false;
    }

    private void updateInputs(boolean startString) {
        Options options = this.minecraft.options;
        pressKey(KeyBinds.FRONT_PUNCH_BINDING, Input.FP, startString, startString);
        pressKey(KeyBinds.BACK_PUNCH_BINDING, Input.BP, startString, startString);
        pressKey(KeyBinds.FRONT_KICK_BINDING, Input.FK, startString, startString);
        pressKey(KeyBinds.BACK_KICK_BINDING, Input.BK, startString, startString);
        pressKey(options.keyUp, Input.W, false, startString);
        pressKey(options.keyLeft, Input.A, false, startString);
        pressKey(options.keyDown, Input.S, false, startString);
        pressKey(options.keyRight, Input.D, false, startString);
        pressKey(options.keyJump, Input.JUMP, false, startString);
        pressKey(options.keyShift, Input.CROUCH, false, startString);
    }

    private boolean isHeldInput(List<Input> inputs) {
        return inputs.size() == 2 && inputs.get(0) == inputs.get(1);
    }

    public void pressKey(KeyMapping key, Input input) {
        this.pressKey(key, input, true, false);
    }

    public void pressKey(KeyMapping key, Input input, boolean startWindow) {
        this.pressKey(key, input, true, startWindow);
    }

    private void pressKey(KeyMapping key, Input input, boolean performSkill, boolean startWindow) {
        boolean flag = KombatUtil.hasFighterWeapon(playerPatch.getOriginal());
        while (keyPressed(key)) {
            Constants.LOG.debug("{}: {}", input.getInput(), this.tickSinceLastInput);

            int timing = ConfigHandler.INPUT_TIMING.get().getDuration();
            if (this.tickSinceLastInput >= 3)
                this.reset(true);

            if (startWindow && flag) {
                this.tickWindows = true;
                this.activeWindow = true;
                this.initString = true;
            }

            if (!flag) break;

            if (performSkill && !input.isMovement()) {
                if (playerPatch.getSkill(KombatSlots.BASIC).sendExecuteRequest(playerPatch, engine).isExecutable())
                    playerPatch.getOriginal().resetAttackStrengthTicker();

                engine.lockHotkeys();
                this.addAttackInput(input);
            } else if (!input.isMovement()) {
                this.addAttackInput(input);
            } else {
                this.addDirectionalInput(input);
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
            //Reset Cache
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
