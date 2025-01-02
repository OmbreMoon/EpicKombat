package com.ombremoon.epickombat.world.capability;

import com.ombremoon.epickombat.gameasset.EpicKombatSkills;
import com.ombremoon.epickombat.networking.NetworkManager;
import com.ombremoon.epickombat.networking.server.ServerboundChangeSkill;
import com.ombremoon.epickombat.skill.KombatSlots;
import com.ombremoon.epickombat.world.capability.input.InputReader;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class KombatKapability {
    protected Player player;
    protected PlayerPatch<?> playerPatch;
    protected boolean initialized = false;
    protected Fighter fighter;
    private InputReader inputReader;

    public KombatKapability() {
    }

    public void tick() {
        if (this.isClientSide()) {
            this.clientTick();
        } else {
            this.serverTick();
        }
    }

    public void clientTick() {
        this.inputReader.tick();
    }

    public void serverTick() {
    }

    public void init(Player player) {
        this.player = player;
    }

    public void joinWorld(EntityJoinLevelEvent event) {
        this.playerPatch = EpicFightCapabilities.getEntityPatch(this.player, PlayerPatch.class);
        if (isClientSide())
            this.inputReader = new InputReader(Minecraft.getInstance());

        if (playerPatch.getSkill(KombatSlots.FIGHTER) != null)
            playerPatch.getSkill(KombatSlots.BASIC).setSkill(EpicKombatSkills.COMBO_BASIC_ATTACK);

        this.initialized = true;
    }

    public void die(LivingDeathEvent event) {

    }

    public void changeSpecialSkill(Skill specialSkill) {
        SkillContainer specialSkillContainer = playerPatch.getSkill(KombatSlots.SPECIAL);
        String skillName = "";
        ServerboundChangeSkill.State state = ServerboundChangeSkill.State.ENABLE;
        if (specialSkill != null) {
            if (specialSkillContainer.getSkill() != specialSkill) {
                specialSkillContainer.setSkill(specialSkill);
            }

            skillName = specialSkill.toString();
        } else {
            state = ServerboundChangeSkill.State.DISABLE;
        }

        specialSkillContainer.setDisabled(specialSkill == null);
        NetworkManager.changeSkill(KombatSlots.SPECIAL, skillName, state);
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public Fighter getFighter() {
        return this.fighter;
    }

    public void setFighter(Fighter fighter) {
        this.fighter = fighter;
    }

    public InputReader getInputs() {
        return this.inputReader;
    }

    public boolean isClientSide() {
        return this.player.level().isClientSide;
    }
}
