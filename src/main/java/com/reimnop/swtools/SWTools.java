package com.reimnop.swtools;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SWTools implements ModInitializer {
    public static final String MOD_ID = "swtools";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static com.reimnop.swtools.SWTConfig CONFIG;

    @Override
    public void onInitialize() {
        LOGGER.info("Hello SWTools!");

        CONFIG = com.reimnop.swtools.SWTConfig.createAndLoad();
    }
}
