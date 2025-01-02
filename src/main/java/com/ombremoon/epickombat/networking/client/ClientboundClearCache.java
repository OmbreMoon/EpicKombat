package com.ombremoon.epickombat.networking.client;

import com.ombremoon.epickombat.util.KombatUtil;
import com.ombremoon.epickombat.world.capability.KombatKapability;
import com.ombremoon.epickombat.world.capability.input.InputCache;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundClearCache {

    public ClientboundClearCache() {
    }

    public ClientboundClearCache(final FriendlyByteBuf buf) {
    }

    public void encode(final FriendlyByteBuf buf) {
    }

    public static void handle(ClientboundClearCache packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            final var context = ctx.get();
            final var handler = context.getNetworkManager().getPacketListener();
            if (handler instanceof ClientGamePacketListener) {
                Player player = Minecraft.getInstance().player;
                KombatKapability kombat = KombatUtil.getKombat(player);
                InputCache cache = kombat.getInputs().cache;
                cache.clearCache();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
