package io.github.freehij.injections;

import io.github.freehij.ModMenu;
import io.github.freehij.loader.annotation.EditClass;
import io.github.freehij.loader.annotation.Inject;
import io.github.freehij.loader.util.InjectionHelper;
import io.github.freehij.loader.util.Reflector;
import io.github.freehij.screen.ModMenuScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;

@EditClass("net/minecraft/client/gui/screens/TitleScreen")
public class TitleScreenInjection {
    @Inject(method = "init")
    public static void init(InjectionHelper helper) {
        TitleScreen ts = (TitleScreen) helper.getSelf();
        Reflector tsR = helper.getReflector();
        tsR.invokeRaw("addRenderableWidget",
                new Class<?>[] { GuiEventListener.class },
                Button.builder(Component.literal("Mods"),
                        button -> Minecraft.getInstance().gui.setScreen(new ModMenuScreen()))
                        .bounds(ts.width / 2 - 32, shouldFixMenu() ? 14 : 2, 64, 20)
                        .build());
    }

    static boolean shouldFixMenu() {
        if (ModMenu.config.getValue(ModMenu.disableButtonFix).equalsIgnoreCase("true")) return false;
        try {
            Class.forName("com.terraformersmc.modmenu.ModMenu");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }
}
