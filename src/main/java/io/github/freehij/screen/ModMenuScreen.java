package io.github.freehij.screen;

import io.github.freehij.ModMenu;
import io.github.freehij.loader.Loader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;

import java.io.File;
import java.nio.file.Paths;

public class ModMenuScreen extends Screen {
    final Minecraft mc = Minecraft.getInstance();
    static ModList list;
    public static Button configButton;

    public ModMenuScreen() {
        super(Component.literal("Mod Menu"));
    }

    @Override
    protected void init() {
        list = new ModList(mc, 320, this.height - 86);
        list.setPosition(this.width / 2 - 150, 33);
        for (Loader.ModInfo modInfo : Loader.getMods()) {
            list.addEntry(new ModList.ModEntry(modInfo));
        }
        this.addRenderableWidget(list);
        configButton = Button.builder(Component.literal("Config"), button -> openConfig())
                .bounds(this.width / 2 - 150, this.height - 37, 155, 20)
                .build();
        configButton.active = false;
        this.addRenderableWidget(configButton);
        this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> mc.setScreen(null))
                .bounds(this.width / 2 + 15, this.height - 37, 155, 20)
                .build());
        this.addRenderableWidget(Button.builder(Component.literal("D"),
                        button -> Util.getPlatform().openPath(Paths.get("mods")))
                .bounds(this.width / 2 - 160 - 22, this.height - 37, 20, 20)
                .build());
    }

    public static void openConfig() {
        String modId = list.getSelected().modInfo.id();
        Runnable customConfig = ModMenu.getCustomConfig(modId);
        if (customConfig != null) {
            customConfig.run();
        } else {
            Util.getPlatform().openFile(new File(String.valueOf(
                    Paths.get("config", modId + ".properties"))));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawString(this.font, "Mod Menu " + ModMenu.version, 2, this.height - 10, Integer.MAX_VALUE);
    }

    protected static class ModList extends ObjectSelectionList<ModList.ModEntry> {
        public ModList(Minecraft minecraft, int width, int height) {
            super(minecraft, width, height, 0, 14);
        }

        @Override
        public void setSelected(ModEntry selected) {
            super.setSelected(selected);
            configButton.active = ModMenu.definedConfig(selected.modInfo.id());
        }

        @Override
        public int addEntry(ModList.ModEntry entry) {
            return super.addEntry(entry);
        }

        protected static class ModEntry extends ObjectSelectionList.Entry<ModEntry> {
            public final Loader.ModInfo modInfo;
            final String text;

            public ModEntry(Loader.ModInfo modInfo) {
                this.modInfo = modInfo;
                this.text = modInfo.name() + " " + modInfo.version() + " §8by " + modInfo.creator();
            }

            @Override
            public Component getNarration() {
                return Component.literal(this.text);
            }

            @Override
            public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
                graphics.drawString(Minecraft.getInstance().font, this.text, this.getX() + 3, this.getY() + 3, 0xffffffff);
            }
        }
    }
}
