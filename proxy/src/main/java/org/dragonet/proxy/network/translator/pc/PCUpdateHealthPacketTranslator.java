/*
 * GNU LESSER GENERAL PUBLIC LICENSE
 *                       Version 3, 29 June 2007
 *
 * Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 *
 * You can view LICENCE file for details.
 *
 * @author The Dragonet Team
 */
package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.nukkitx.server.network.minecraft.MinecraftPacket;
import com.nukkitx.server.network.minecraft.packet.RespawnPacket;
import com.nukkitx.server.network.minecraft.packet.SetHealthPacket;
import com.nukkitx.server.network.minecraft.packet.UpdateAttributesPacket;
import org.dragonet.common.data.entity.PEEntityAttribute;
import org.dragonet.proxy.NukkitProtocolUtils;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;

import java.util.ArrayList;
import java.util.List;

public class PCUpdateHealthPacketTranslator implements IPCPacketTranslator<ServerPlayerHealthPacket> {

    public List<MinecraftPacket> translate(UpstreamSession session, ServerPlayerHealthPacket originalPacket) {
        List<MinecraftPacket> resultPackets = new ArrayList<>();

        // 1: Health packet
        int newHealth = (int) Math.ceil(originalPacket.getHealth()); // Always round up
        SetHealthPacket healthPacket = new SetHealthPacket();
        healthPacket.setHealth(newHealth);
        resultPackets.add(healthPacket);

        // 2: Update attributes packet
        CachedEntity peSelfPlayer = session.getEntityCache().getClientEntity();
        peSelfPlayer.attributes.put(PEEntityAttribute.HEALTH,
            NukkitProtocolUtils.getBedrockAttribute(PEEntityAttribute.findAttribute(PEEntityAttribute.HEALTH).setValue(newHealth)));
        if (peSelfPlayer.foodPacketCount == 0) {
            peSelfPlayer.attributes.put(PEEntityAttribute.FOOD,
                NukkitProtocolUtils.getBedrockAttribute(PEEntityAttribute.findAttribute(PEEntityAttribute.FOOD).setValue(originalPacket.getFood())));
        }
        UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
        updateAttributesPacket.setRuntimeEntityId(peSelfPlayer.proxyEid);
        updateAttributesPacket.setAttributes(new ArrayList<>(peSelfPlayer.attributes.values()));
        resultPackets.add(updateAttributesPacket);

        // 3: Respawn packet
        if (newHealth <= 0) {
            resultPackets.add(new RespawnPacket());
        }

        return resultPackets;
    }
}
