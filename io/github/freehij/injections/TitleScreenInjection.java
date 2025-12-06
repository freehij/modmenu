package io.github.freehij.injections;

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
                        button -> Minecraft.getInstance().setScreen(new ModMenuScreen()))
                        .bounds(ts.width / 2 - 32, 2, 64, 20)
                        .build());
    }
}
