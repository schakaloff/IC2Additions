package com.ic2additions.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Reference {
    public static final String MODID = "ic2additions";
    public static final String NAME = "IC2 Additions";
    public static final String VERSION = "1.0.0";

    public static final String CLIENT_PROXY_CLASS = "com.ic2additions.proxy.ClientProxy";
    public static final String COMMON_PROXY_CLASS = "com.ic2additions.proxy.CommonProxy";
    public static final String DEPENDENCIES = "required-after:ic2@[2.8.222-ex112,)";

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    private Reference(){}

}
