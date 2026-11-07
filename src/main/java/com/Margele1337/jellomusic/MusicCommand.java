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

    @Override
    public String getCommandName() {
        return "music";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/music <next|stop>";
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
        } else {
            say(sender, getCommandUsage(sender));
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
