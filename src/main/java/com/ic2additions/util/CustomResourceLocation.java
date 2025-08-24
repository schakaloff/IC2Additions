package com.ic2additions.util;

import net.minecraft.util.ResourceLocation;

public class CustomResourceLocation extends ResourceLocation {

    public CustomResourceLocation(String domain, String path) {
        super(domain, path);
    }

    public CustomResourceLocation(ResourceLocation location, String appendix) {
        super(location.getResourceDomain(), location.getResourcePath() + appendix);
    }
}
