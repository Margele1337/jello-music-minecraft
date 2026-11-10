package com.margele1337.jellomusic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class MusicCommand implements ICommand {

    private final ClientProxy proxy;

    public MusicCommand(ClientProxy proxy) {
        this.proxy = proxy;
    }

    private void say(ICommandSender sender, String text) {
        sender.addChatMessage(new ChatComponentText(text));
    }

    private void showSearchResults(ICommandSender sender, List<Track> found) {
        if (found.isEmpty()) {
            say(sender, "No results found");
            return;
        }
        for (int i = 0; i < Math.min(5, found.size()); i++) {
            Track track = found.get(i);
            say(sender, (i + 1) + ". " + track.title() + " (" + track.seconds() + "s)");
        }
    }

    @Override
    public String getCommandName() {
        return "music";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/music <next|stop|search|url>";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Minecraft mc = Minecraft.getMinecraft();

        if (args.length == 0) {
            say(sender, getCommandUsage(sender));
            return;
        }

        if (args[0].equals("next")) {
            if (MusicPlayer.isEmpty()) {
                say(sender, "歌单为空");
                return;
            }
            proxy.stopCurrentSound(mc);
            proxy.playNextTrack(mc);
        } else if (args[0].equals("stop")) {
            proxy.stopCurrentSound(mc);
            say(sender, "音乐已停止");
        } else if (args[0].equals("search")) {
            if (args.length < 2) {
                say(sender, "Usage: /music search <keyword>");
                return;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                if (i > 1) {
                    sb.append(' ');
                }
                sb.append(args[i]);
            }
            String keyword = sb.toString();
            say(sender, "Searching: " + keyword + " ...");
            new Thread(() -> {
                List<Track> found = proxy.getMusicApi()
                    .search(keyword);
                proxy.runOnMainThread(() -> showSearchResults(sender, found));
            }, "jello-music-search").start();
        } else if (args[0].equals("url")) {
            if (args.length < 2) {
                say(sender, "Usage: /music url <hash>");
                return;
            }
            String hash = args[1];
            say(sender, "Getting play URL...");
            new Thread(() -> {
                String url = proxy.getMusicApi()
                    .getPlayUrl(hash);
                proxy.runOnMainThread(
                    () -> say(
                        sender,
                        url != null ? "Play URL: " + url : "Could not get play URL (see log for reason)"));
            }, "jello-music-url").start();
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        if (args.length == 1) {
            List<String> options = new ArrayList<>();
            options.add("next");
            options.add("stop");
            options.add("search");
            options.add("url");
            return options;
        }
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return this.getCommandName()
            .compareTo(((ICommand) o).getCommandName());
    }
}
