package org.dragonet.proxy.network.translator.pc;

import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;
import org.dragonet.proxy.network.UpstreamSession;
import org.dragonet.proxy.network.translator.IPCPacketTranslator;
import org.dragonet.protocol.PEPacket;
import org.dragonet.protocol.packets.SetTitlePacket;

public class PCSetTitlePacketTranslator implements IPCPacketTranslator<ServerTitlePacket> {

    @Override
    public PEPacket[] translate(UpstreamSession session, ServerTitlePacket originalPacket) {
        SetTitlePacket titlePacket = new SetTitlePacket();

        switch (originalPacket.getAction()) {
            case ACTION_BAR:
                titlePacket.action = SetTitlePacket.SET_ACTIONBAR;
                titlePacket.text = originalPacket.getActionBar().getFullText();
                break;
            case TITLE:
                titlePacket.action = SetTitlePacket.SET_TITLE;
                titlePacket.text = originalPacket.getTitle().getText();
                break;
            case SUBTITLE:
                titlePacket.action = SetTitlePacket.SET_SUBTITLE;
                titlePacket.text = originalPacket.getSubtitle().getText();
                break;
            case RESET:
            case CLEAR:
                titlePacket.action = SetTitlePacket.RESET;
                titlePacket.text = "";
                break;
            default:
                return null;
        }

        titlePacket.fadeIn = originalPacket.getFadeIn();
        titlePacket.fadeOut = originalPacket.getFadeOut();
        titlePacket.stay = originalPacket.getStay();

        return new PEPacket[]{titlePacket};
    }

}
