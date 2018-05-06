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

import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.data.game.world.sound.CustomSound;

import org.dragonet.proxy.DragonProxy;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;

import org.dragonet.common.maths.BlockPosition;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.PlaySoundPacket;

public class PCPlaySoundPacketTranslator implements IPCPacketTranslator<ServerPlaySoundPacket> {

	public PEPacket[] translate(UpstreamSession session, ServerPlaySoundPacket originalPacket) {
		try {
			String soundName;
			
			if (BuiltinSound.class.isAssignableFrom(originalPacket.getSound().getClass())) {
				if(DragonProxy.getInstance().getSoundTranslator().isIgnored((BuiltinSound) originalPacket.getSound())) {
					return null;
				}
				if (DragonProxy.getInstance().getSoundTranslator().isTranslatable((BuiltinSound) originalPacket.getSound())) {
					soundName = DragonProxy.getInstance().getSoundTranslator().translate((BuiltinSound) originalPacket.getSound());
				} else {
					BuiltinSound sound = (BuiltinSound) originalPacket.getSound();
					soundName = sound.name();
				}
			} else {
				soundName = ((CustomSound) originalPacket.getSound()).getName();
			}
			if (soundName == null) {
				return null;
			}
			PlaySoundPacket pk = new PlaySoundPacket();
			pk.blockPosition = new BlockPosition((int) originalPacket.getX(), (int) originalPacket.getY(), (int) originalPacket.getZ());
			pk.name = soundName;
			pk.volume = originalPacket.getVolume();
			pk.pitch = originalPacket.getPitch();
			return new PEPacket[] { pk };
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
