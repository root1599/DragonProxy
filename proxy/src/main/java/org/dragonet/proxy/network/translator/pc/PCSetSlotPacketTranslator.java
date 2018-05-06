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

import org.dragonet.proxy.network.InventoryTranslatorRegister;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.cache.CachedWindow;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import org.dragonet.protocol.PEPacket;

public class PCSetSlotPacketTranslator implements IPCPacketTranslator<ServerSetSlotPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerSetSlotPacket originalPacket) {
        if (!session.getWindowCache().hasWindow(originalPacket.getWindowId())) {
            // Cache this
            session.getWindowCache().newCachedPacket(originalPacket.getWindowId(), originalPacket);
            return null;
        }
        CachedWindow win = session.getWindowCache().get(originalPacket.getWindowId());
        if (win.pcType == null && originalPacket.getWindowId() != 0) {
            return null;
        }
        if (originalPacket.getWindowId() == 0) {
            if (originalPacket.getSlot() >= win.slots.length) {
                return null;
            }
            win.slots[originalPacket.getSlot()] = originalPacket.getItem();
            return InventoryTranslatorRegister.sendPlayerInventory(session); // Too lazy lol
        }
//        if (packet.getItem() != null)
//            System.out.println("Caching window " + packet.getWindowId() + " item " + packet.getItem().getId());
        if (win.isOpen)
            InventoryTranslatorRegister.updateSlot(session, originalPacket);
        else
            session.getWindowCache().newCachedPacket(originalPacket.getWindowId(), originalPacket);
        return null;
    }
}
