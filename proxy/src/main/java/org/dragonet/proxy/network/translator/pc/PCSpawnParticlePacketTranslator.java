package org.dragonet.proxy.network.translator.pc;

import java.util.ArrayList;
import java.util.Random;

import org.dragonet.common.data.blocks.GlobalBlockPalette;
import org.dragonet.common.data.itemsblocks.ItemEntry;
import org.dragonet.common.maths.Vector3F;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.LevelEventPacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.proxy.network.translator.ItemBlockTranslator;
import org.dragonet.proxy.network.translator.ParticleTranslator;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.Particle;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;

public class PCSpawnParticlePacketTranslator implements IPCPacketTranslator<ServerSpawnParticlePacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerSpawnParticlePacket originalPacket) {
        ArrayList<PEPacket> packets = new ArrayList<PEPacket>();
        if (originalPacket.getParticle() == Particle.BLOCK_CRACK || originalPacket.getParticle() == Particle.BLOCK_DUST) {
            LevelEventPacket pk = new LevelEventPacket();
            pk.eventId = LevelEventPacket.EVENT_PARTICLE_DESTROY;
            pk.position = new Vector3F(originalPacket.getX(), originalPacket.getY(), originalPacket.getZ());
            Position pos = new Position((int) originalPacket.getX(), (int) originalPacket.getY(), (int) originalPacket.getZ());
            ItemEntry entry = ItemBlockTranslator.translateToPE(session.getChunkCache().getBlock(pos).getId(),
                    session.getChunkCache().getBlock(pos).getData());
            if (session.getChunkCache().getBlock(pos) != null)
                pk.data = GlobalBlockPalette.getOrCreateRuntimeId(entry.getId(), entry.getPEDamage());
            else
                pk.data = GlobalBlockPalette.getOrCreateRuntimeId(1);
            packets.add(pk);
        } else {
            int num = ParticleTranslator.getInstance().translate(originalPacket.getParticle());
            if (num != -1) {
                Random random = new Random(System.currentTimeMillis());
                for (int i = 0; i < originalPacket.getAmount(); i++) {
                    packets.add(getParticle(originalPacket.getX() + (random.nextFloat() * 2 - 1) * originalPacket.getOffsetX(),
                            originalPacket.getY() + (random.nextFloat() * 2 - 1) * originalPacket.getOffsetY(),
                            originalPacket.getZ() + (random.nextFloat() * 2 - 1) * originalPacket.getOffsetZ(), num, 0));
                }
            }
        }
        if (!packets.isEmpty()) {
            return packets.toArray(new PEPacket[packets.size()]);
        } else {
            return null;
        }
    }

    public LevelEventPacket getParticle(float x, float y, float z, int type, int data) {
        LevelEventPacket pk = new LevelEventPacket();
        pk.eventId = (short) (LevelEventPacket.EVENT_ADD_PARTICLE_MASK | type);
        pk.position = new Vector3F(x, y, z);
        pk.data = data;
        return pk;
    }

}
