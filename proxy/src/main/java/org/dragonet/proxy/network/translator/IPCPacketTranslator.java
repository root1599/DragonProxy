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
package org.dragonet.proxy.network.translator;

import com.github.steveice10.packetlib.packet.Packet;
import com.nukkitx.server.network.minecraft.MinecraftPacket;
import org.dragonet.proxy.network.UpstreamSession;

import java.util.List;

public interface IPCPacketTranslator<P extends Packet> {

    /**
     * Translate a packet from PC version to Bedrock version.
     *
     * @param session the upstream session (client-side)
     * @param originalPacket  the packet sent from the server
     * @return the bedrock packets (client-side)
     */
    List<MinecraftPacket> translate(UpstreamSession session, P originalPacket);
}
