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
            Track first = MusicPlayer.getFirst();
            currentSound = new SongSound(first.soundName());
            mc.getSoundHandler()
                .playSound(currentSound);
            JelloMusic.LOG.info("Now playing: " + first.title() + " - " + first.seconds() + "s");
        } else if (mc.thePlayer == null) {
            if (currentSound != null) {
                mc.getSoundHandler()
                    .stopSound(currentSound);
                currentSound = null;
            }
            playedJoinSound = false;
        }
    }
}
