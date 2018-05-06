package org.dragonet.proxy;

import com.nukkitx.server.entity.Attribute;
import org.dragonet.common.data.entity.PEEntityAttribute;

public final class NukkitProtocolUtils {

    // Utility class
    private NukkitProtocolUtils() {
    }

    public static Attribute getBedrockAttribute(PEEntityAttribute entityAttribute) {
        return new Attribute(entityAttribute.name, entityAttribute.currentValue, entityAttribute.min, entityAttribute.max, entityAttribute.defaultValue);
    }

}
