package com.margele1337.jellomusic;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.margele1337.jellomusic.api.KuGouApi;
import com.margele1337.jellomusic.api.MusicApi;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

public class ClientProxy extends CommonProxy {

    private boolean playedJoinSound = false;
    private ISound currentSound = null;
    private KeyBinding nextKey = new KeyBinding("key.jellomusic.next", Keyboard.KEY_M, "key.categories.jellomusic");
    private KeyBinding stopKey = new KeyBinding("key.jellomusic.stop", Keyboard.KEY_N, "key.categories.jellomusic");
    private KeyBinding panelKey = new KeyBinding("key.jellomusic.panel", Keyboard.KEY_G, "key.categories.jellomusic");
    private MusicApi musicApi;
    private final Queue<Runnable> mainThreadTasks = new ConcurrentLinkedQueue<>();
    private final RemotePlayer remotePlayer = new RemotePlayer();

    private void playTrack(Minecraft mc, Track track) {
        currentSound = new SongSound(track.soundName());
        mc.getSoundHandler()
            .playSound(currentSound);
        JelloMusic.LOG.info("Now playing: " + track.title() + " - " + track.seconds() + "s");
    }

    public MusicApi getMusicApi() {
        return musicApi;
    }

    public void playNextTrack(Minecraft mc) {
        stopCurrentSound(mc);
        Track next = MusicPlayer.next();
        if (next == null) {
            currentSound = null;
            JelloMusic.LOG.info("No more tracks in playlist.");
        } else {
            playTrack(mc, next);
        }
    }

    public void stopCurrentSound(Minecraft mc) {
        remotePlayer.stop();
        if (currentSound != null) {
            mc.getSoundHandler()
                .stopSound(currentSound);
            currentSound = null;
        }
    }

    public void playRemote(String url) {
        remotePlayer.play(url, Config.volume);
    }

    public void runOnMainThread(Runnable task) {
        mainThreadTasks.add(task);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
        ClientRegistry.registerKeyBinding(nextKey);
        ClientRegistry.registerKeyBinding(stopKey);
        ClientRegistry.registerKeyBinding(panelKey);
        musicApi = new KuGouApi(Config.kugouApiUrl, Config.kugouCookie);

    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Runnable task;
        while ((task = mainThreadTasks.poll()) != null) {
            task.run();
        }
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer != null && !playedJoinSound && !MusicPlayer.isEmpty()) {
            playedJoinSound = true;
            playTrack(mc, MusicPlayer.getFirst());
        } else if (mc.thePlayer == null) {
            stopCurrentSound(mc);
            playedJoinSound = false;
            MusicPlayer.reset();
        }
        if (nextKey.isPressed() && mc.thePlayer != null) {
            playNextTrack(mc);
        }
        if (stopKey.isPressed() && mc.thePlayer != null) {
            stopCurrentSound(mc);
            JelloMusic.LOG.info("Stopped track.");
        }
        // 自动切换下一首
        if (currentSound != null && !mc.getSoundHandler()
            .isSoundPlaying(currentSound)) {
            playNextTrack(mc);
        }
        if (panelKey.isPressed() && mc.thePlayer != null) {
            mc.displayGuiScreen(new MusicGui(this));
        }
    }
}
