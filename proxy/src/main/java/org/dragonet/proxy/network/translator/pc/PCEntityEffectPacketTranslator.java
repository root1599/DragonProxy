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

import com.github.steveice10.mc.protocol.data.MagicValues;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEffectPacket;
import org.dragonet.common.data.PocketPotionEffect;
import org.dragonet.proxy.network.CacheKey;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedEntity;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.MobEffectPacket;

public class PCEntityEffectPacketTranslator implements IPCPacketTranslator<ServerEntityEffectPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerEntityEffectPacket originalPacket) {

        CachedEntity entity = session.getEntityCache().getByRemoteEID(originalPacket.getEntityId());
        if (entity == null) {
            if (originalPacket.getEntityId() == (int) session.getDataCache().get(CacheKey.PLAYER_EID)) {
                entity = session.getEntityCache().getClientEntity();
            } else {
                return null;
            }
        }

        int effectId = MagicValues.value(Integer.class, originalPacket.getEffect());

        PocketPotionEffect effect = PocketPotionEffect.getByID(effectId);
        if (effect == null) {
            System.out.println("Unknown effect ID: " + effectId);
            return null;
        }

        MobEffectPacket eff = new MobEffectPacket();
        eff.rtid = entity.proxyEid;
        eff.effectId = effect.getEffect();
        if (entity.effects.contains(effectId)) {
            eff.eventId = MobEffectPacket.EVENT_MODIFY;
        } else {
            eff.eventId = MobEffectPacket.EVENT_ADD;
            entity.effects.add(effectId);
        }
        eff.amplifier = originalPacket.getAmplifier();
        eff.duration = originalPacket.getDuration();
        eff.particles = originalPacket.getShowParticles();
        return new PEPacket[]{eff};
    }
}
