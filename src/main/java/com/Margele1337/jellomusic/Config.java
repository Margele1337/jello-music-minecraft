package com.margele1337.jellomusic;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

    public static String greeting = "Hello World";
    public static int volume = 100;

    public static void synchronizeConfiguration(File configFile) {
        Configuration configuration = new Configuration(configFile);

        greeting = configuration.getString("greeting", Configuration.CATEGORY_GENERAL, greeting, "How shall I greet?");
        volume = configuration
            .getInt("volume", Configuration.CATEGORY_GENERAL, volume, 0, 100, "Playback volume (0-100)");
        if (configuration.hasChanged()) {
            configuration.save();
        }
    }
}
