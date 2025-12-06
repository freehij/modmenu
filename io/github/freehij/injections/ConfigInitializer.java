package io.github.freehij.injections;

import io.github.freehij.ModMenu;
import io.github.freehij.loader.annotation.EditClass;
import io.github.freehij.loader.annotation.Inject;
import io.github.freehij.loader.util.InjectionHelper;

@EditClass("net/minecraft/client/main/Main")
public class ConfigInitializer {
    @Inject(method = "main", descriptor = "([Ljava/lang/String;)V")
    public static void initConfigs(InjectionHelper helper) {
        ModMenu.initConfig();
    }
}
