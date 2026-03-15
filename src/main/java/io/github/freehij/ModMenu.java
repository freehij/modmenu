package io.github.freehij;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ModMenu {
    public static final String version = "a1.0.0";
    static final Map<String, Config> modIdToConfigMap = new HashMap<>();
    public static ModMenu.Config config;
    static String autoSave = "auto-save(FOR_DEV_PURPOSES_ONLY)";
    public static String disableButtonFix = "disable-button-fix(FOR_FABRIC_MOD_MENU)";
    static final Map<String, Runnable> modIdToCustomConfigMap = new HashMap<>();

    public static void initConfig() {
        config = ModMenu.Config.fromModId("modmenu");
        if (config.getValue(autoSave) == null) {
            config.setValue(autoSave, "true");
        }
        if (config.getValue(disableButtonFix) == null) {
            config.setValue(disableButtonFix, "false");
        }
    }

    public static void addCustomConfig(String modId, Runnable config) {
        modIdToCustomConfigMap.put(modId, config);
    }

    public static Runnable getCustomConfig(String modId) {
        return modIdToCustomConfigMap.get(modId);
    }

    public static boolean definedConfig(String modId) {
        return modIdToConfigMap.containsKey(modId) || modIdToCustomConfigMap.containsKey(modId);
    }

    public static class Config {
        final String modId;
        final HashMap<String, String> values = new HashMap<>();

        protected Config(String modId) {
            this.modId = modId;
            Path configPath = Paths.get("config", modId + ".properties");
            try {
                if (Files.exists(configPath)) {
                    try (BufferedReader reader = Files.newBufferedReader(configPath)) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            int eqIndex = line.indexOf('=');
                            if (eqIndex > 0) {
                                values.put(line.substring(0, eqIndex), line.substring(eqIndex + 1));
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Unable to load configuration file");
            }
            modIdToConfigMap.put(modId, this);
            this.saveConfig();
        }

        public static Config fromModId(String modId) {
            return modIdToConfigMap.getOrDefault(modId, new Config(modId));
        }

        public void setValue(String key, String value) {
            values.put(key, value);
            String autoSaveValue = config.getValue(autoSave);
            if (autoSaveValue == null || autoSaveValue.equals("true")) {
                this.saveConfig();
            }
        }

        public String getValue(String key) {
            return values.get(key);
        }

        public void saveConfig() {
            Path configPath = Paths.get("config", modId + ".properties");
            try {
                Files.createDirectories(configPath.getParent());
                try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
                    for (Map.Entry<String, String> entry : values.entrySet()) {
                        writer.write(entry.getKey() + "=" + entry.getValue());
                        writer.newLine();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Unable to save config for " + modId);
            }
        }
    }
}
