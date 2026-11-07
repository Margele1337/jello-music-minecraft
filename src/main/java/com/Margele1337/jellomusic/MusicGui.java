package com.margele1337.jellomusic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class MusicGui extends GuiScreen {

    private final ClientProxy proxy;

    public MusicGui(ClientProxy proxy) {
        this.proxy = proxy;
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 - 50, 200, 20, "下一首"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 2 - 25, 200, 20, "停止"));
        buttonList.add(new GuiButton(2, width / 2 - 100, height / 2, 200, 20, "关闭面板"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Jello Music", width / 2, height / 2 - 70, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        Minecraft mc = Minecraft.getMinecraft();

        if (button.id == 0) {
            proxy.playNextTrack(mc);
        } else if (button.id == 1) {
            proxy.stopCurrentSound(mc);
        } else if (button.id == 2) {
            mc.displayGuiScreen(null); // null = 关闭面板
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
