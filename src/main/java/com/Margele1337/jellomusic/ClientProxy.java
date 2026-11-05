package com.margele1337.jellomusic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ClientProxy extends CommonProxy {

    private boolean playedJoinSound = false;
    private ISound currentSound = null;

    private void playTrack(Minecraft mc, Track track) {
        currentSound = new SongSound(track.soundName());
        mc.getSoundHandler()
            .playSound(currentSound);
        JelloMusic.LOG.info("Now playing: " + track.title() + " - " + track.seconds() + "s");
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer != null && !playedJoinSound) {
            playedJoinSound = true;
            playTrack(mc, MusicPlayer.getFirst());
        } else if (currentSound != null && !mc.getSoundHandler()
            .isSoundPlaying(currentSound)) {
                Track next = MusicPlayer.next();
                if (next == null) {
                    currentSound = null;
                    JelloMusic.LOG.info("No more tracks in playlist.");
                } else {
                    playTrack(mc, next);
                }
            } else if (mc.thePlayer == null) {
                if (currentSound != null) {
                    mc.getSoundHandler()
                        .stopSound(currentSound);
                    currentSound = null;
                }
                playedJoinSound = false;
                MusicPlayer.reset();
            }
    }
}
