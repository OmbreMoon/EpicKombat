package com.ombremoon.epickombat.networking;

import com.ombremoon.epickombat.main.CommonClass;
import com.ombremoon.epickombat.networking.client.ClientboundClearCache;
import com.ombremoon.epickombat.networking.server.ServerboundChangeSkill;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import yesman.epicfight.skill.SkillSlot;

public class NetworkManager {
    private static final String VER = "1";
    public static final SimpleChannel PACKET_CHANNEL = NetworkRegistry.newSimpleChannel(CommonClass.customLocation("main"), () -> VER, VER::equals, VER::equals);

    public static void changeSkill(SkillSlot skillSlot, String skillName, ServerboundChangeSkill.State state) {
        sendToServer(new ServerboundChangeSkill(skillSlot, skillName, state));
    }

    public static void clearCache(ServerPlayer serverPlayer) {
        sendToPlayer(new ClientboundClearCache(), serverPlayer);
    }

    public static void registerPackets() {
        var id = 0;
        PACKET_CHANNEL.registerMessage(id++, ServerboundChangeSkill.class, ServerboundChangeSkill::encode, ServerboundChangeSkill::new, ServerboundChangeSkill::handle);
        PACKET_CHANNEL.registerMessage(id++, ClientboundClearCache.class, ClientboundClearCache::encode, ClientboundClearCache::new, ClientboundClearCache::handle);
    }

    protected static <MSG> void sendToServer(MSG message) {
        PACKET_CHANNEL.sendToServer(message);
    }

    protected static <MSG> void sendToPlayer(MSG message, ServerPlayer serverPlayer) {
        PACKET_CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
    }

    protected static  <MSG> void sendToClients(MSG message) {
        PACKET_CHANNEL.send(PacketDistributor.ALL.noArg(), message);
    }
}
