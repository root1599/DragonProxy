package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.data.game.world.block.ExplodedBlockRecord;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.ExplodePacket;


import java.util.ArrayList;
import org.dragonet.common.maths.BlockPosition;

public class PCExplosionTranslator implements IPCPacketTranslator<ServerExplosionPacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerExplosionPacket originalPacket) {

        ExplodePacket pk = new ExplodePacket();

        pk.position = new Vector3F(originalPacket.getX(), originalPacket.getY(), originalPacket.getZ());
        pk.radius = originalPacket.getRadius();
        pk.destroyedBlocks = new ArrayList<>(originalPacket.getExploded().size());

        for (ExplodedBlockRecord record : originalPacket.getExploded())
            pk.destroyedBlocks.add(new BlockPosition(record.getX(), record.getY(), record.getZ()));

        return new PEPacket[]{pk};
    }

}
