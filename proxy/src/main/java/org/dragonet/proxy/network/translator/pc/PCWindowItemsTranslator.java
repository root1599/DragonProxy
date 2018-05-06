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
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import org.dragonet.protocol.PEPacket;

public class PCWindowItemsTranslator implements IPCPacketTranslator<ServerWindowItemsPacket> {

    public PEPacket[] translate(UpstreamSession session, ServerWindowItemsPacket originalPacket) {
        if (!session.getWindowCache().hasWindow(originalPacket.getWindowId())) {
            // Cache this
            session.getWindowCache().newCachedPacket(originalPacket.getWindowId(), originalPacket);
            return null;
        }
        CachedWindow win = session.getWindowCache().get(originalPacket.getWindowId());
        if (originalPacket.getWindowId() == 0) {
            if (originalPacket.getItems().length < 40) {
                // Almost impossible to happen either.
                return null;
            }
            // Update items in window cache
            win.slots = originalPacket.getItems();
            return InventoryTranslatorRegister.sendPlayerInventory(session);
        }
        InventoryTranslatorRegister.updateContent(session, originalPacket);
        return null;
    }

}
