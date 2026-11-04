package com.margele1337.jellomusic;

import net.minecraft.client.Minecraft;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ClientProxy extends CommonProxy {

    private boolean playedJoinSound = false;

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
            mc.thePlayer.playSound("random.levelup", 1.0F, 1.0F);
            JelloMusic.LOG.info("Player joined world!");
        } else if (mc.thePlayer == null) {
            playedJoinSound = false;
        }
    }
}
