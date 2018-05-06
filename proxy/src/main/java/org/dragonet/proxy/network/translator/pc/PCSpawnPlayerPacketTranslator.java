package org.dragonet.proxy.network.translator.pc;

import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.dragonet.protocol.PEPacket;

public class PCSpawnPlayerPacketTranslator implements IPCPacketTranslator<ServerSpawnPlayerPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerSpawnPlayerPacket originalPacket) {
        try {
            session.getEntityCache().newPlayer(originalPacket).spawn(session);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
