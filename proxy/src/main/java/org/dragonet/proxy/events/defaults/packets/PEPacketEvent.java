package org.dragonet.proxy.events.defaults.packets;

import com.nukkitx.server.network.minecraft.MinecraftPacket;
import com.nukkitx.server.network.raknet.NetworkPacket;
import org.dragonet.proxy.events.Event;

public abstract class PEPacketEvent extends Event {

    private NetworkPacket packet;

    public PEPacketEvent(NetworkPacket packet) {
        this.packet = packet;
    }

    public NetworkPacket getPacket() {
        return packet;
    }

    public void setPacket(NetworkPacket packet) {
        this.packet = packet;
    }

}
