package com.margele1337.jellomusic;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc., and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());

        JelloMusic.LOG.info(Config.greeting);
        JelloMusic.LOG.info("I am Jello-music at version " + Tags.VERSION);
        JelloMusic.LOG.info("default music volume:" + Config.volume);

        List<Track> playlist = new ArrayList<>();
        playlist.add(new Track("晴天", 269));
        playlist.add(new Track("Lemon", 257));

        for (Track track : playlist) {
            JelloMusic.LOG.info(track.title + " - " + track.seconds + "s");
        }
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {}

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {}

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}
}
