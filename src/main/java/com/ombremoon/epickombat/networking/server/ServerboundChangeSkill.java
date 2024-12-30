package com.ombremoon.epickombat.networking.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillSlot;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

import java.util.function.Supplier;

public class ServerboundChangeSkill {
    private final SkillSlot skillSlot;
    private final String skillName;
    private final State state;

    public ServerboundChangeSkill(SkillSlot slot, String name, State state) {
        this.skillSlot = slot;
        this.skillName = name;
        this.state = state;
    }

    public ServerboundChangeSkill(final FriendlyByteBuf buf) {
        this.skillSlot = SkillSlot.ENUM_MANAGER.getOrThrow(buf.readInt());
        this.skillName = buf.readUtf();
        this.state = State.values()[buf.readInt()];
    }

    public void encode(final FriendlyByteBuf buf) {
        buf.writeInt(this.skillSlot.universalOrdinal());
        buf.writeUtf(this.skillName);
        buf.writeInt(this.state.ordinal());
    }

    public static void handle(ServerboundChangeSkill packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            PlayerPatch<?> playerpatch = EpicFightCapabilities.getEntityPatch(ctx.get().getSender(), PlayerPatch.class);
            if (playerpatch != null) {
                if (!packet.skillName.equals("")) {
                    Skill skill = SkillManager.getSkill(packet.skillName);
                    playerpatch.getSkill(packet.skillSlot).setSkill(skill);
                    if (packet.skillSlot.category().learnable()) {
                        playerpatch.getSkillCapability().addLearnedSkill(skill);
                    }
                }

                playerpatch.getSkill(packet.skillSlot).setDisabled(packet.state.setter);
            }

        });
        ctx.get().setPacketHandled(true);
    }

    public enum State {
        ENABLE(false),
        DISABLE(true);

        boolean setter;

        State(boolean setter) {
            this.setter = setter;
        }
    }
}
