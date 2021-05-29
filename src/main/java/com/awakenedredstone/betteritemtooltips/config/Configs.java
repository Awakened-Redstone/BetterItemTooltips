package com.awakenedredstone.betteritemtooltips.config;

import com.awakenedredstone.betteritemtooltips.util.TooltipMode;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import fi.dy.masa.malilib.util.StringUtils;

import java.io.File;

public class Configs implements IConfigHandler {
    private static final String modId = "betteritemtooltips";
    private static final String path = modId + "." + "gui.option.description.";
    private static final String CONFIG_FILE_NAME = "betteritemtooltips.json";
    private static final KeybindSettings GUI_RELAXED = KeybindSettings.create(KeybindSettings.Context.GUI, KeyAction.PRESS, true, false, false, false);

    public static class Settings {
        public static final ConfigInteger MAX_LINES_SHOWN = new ConfigInteger("maxLinesShown", 10, 1, 100, StringUtils.translate(path + "maxLinesShown"));
        public static final ConfigInteger MAX_WIDTH = new ConfigInteger("maxWidth", 300, 1, 1024, StringUtils.translate(path + "maxWidth"));
        public static final ConfigInteger NBT_MIN_WIDTH = new ConfigInteger("nbtMinWidth", 300, 1, 1024, StringUtils.translate(path + "nbtMinWidth"));
        public static final ConfigInteger HORIZONTAL_SCROLL_AMOUNT = new ConfigInteger("horizontalScrollAmount", 10, 1, 100, StringUtils.translate(path + "horizontalScrollAmount"));
        public static final ConfigInteger VERTICAL_SCROLL_AMOUNT = new ConfigInteger("verticalScrollAmount", 10, 1, 100, StringUtils.translate(path + "verticalScrollAmount"));
        public static final ConfigBoolean ALWAYS_SHOW = new ConfigBoolean("alwaysShow", false, StringUtils.translate(path + "alwaysShow"));
        public static final ConfigString COPY_PATTERN = new ConfigString("copyPattern", "{item.id}{item.nbt}", StringUtils.translate(path + "copyPattern"));
        public static final ConfigOptionList TOOLTIP_MODE = new ConfigOptionList("tooltipMode", TooltipMode.TEXT_SCROLL, StringUtils.translate(path + "tooltipMode"));
        public static final ConfigBoolean PERSISTENT_TOOLTIP = new ConfigBoolean("persistentTooltip", false, StringUtils.translate(path + "persistentTooltip"));
        public static final ConfigBoolean REMOVE_DEBUG_LINE = new ConfigBoolean("removeDebugLine", true, StringUtils.translate(path + "removeDebugLine"));


        public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
                MAX_LINES_SHOWN,
                MAX_WIDTH,
                NBT_MIN_WIDTH,
                HORIZONTAL_SCROLL_AMOUNT,
                VERTICAL_SCROLL_AMOUNT,
                ALWAYS_SHOW,
                COPY_PATTERN,
                TOOLTIP_MODE,
                PERSISTENT_TOOLTIP,
                REMOVE_DEBUG_LINE
        );
    }

    public static class Hotkeys {
        public static final ConfigHotkey KEY_MAIN_TOGGLE = new ConfigHotkey("mainToggle", "LEFT_CONTROL,N", KeybindSettings.GUI, StringUtils.translate(path + "mainToggle"));
        public static final ConfigHotkey SHOW = new ConfigHotkey("showNBT", "LEFT_CONTROL", GUI_RELAXED, StringUtils.translate(path + "showNBT"));
        public static final ConfigHotkey HORIZONTAL_SCROLL = new ConfigHotkey("horizontalScroll", "LEFT_SHIFT", GUI_RELAXED, StringUtils.translate(path + "horizontalScroll"));
        public static final ConfigHotkey RESET_SCROLL = new ConfigHotkey("resetScroll", "LEFT_CONTROL,LEFT_ALT", GUI_RELAXED, StringUtils.translate(path + "resetScroll"));
        public static final ConfigHotkey COPY = new ConfigHotkey("copy", "DOWN", GUI_RELAXED, StringUtils.translate(path + "copy"));
        public static final ConfigHotkey SHOW_ONLY_NBT = new ConfigHotkey("showOnlyNBT", "A", GUI_RELAXED, StringUtils.translate(path + "showOnlyNBT"));
        public static final ConfigHotkey KEY_OPEN_CONFIG_GUI = new ConfigHotkey("openConfigGui", "N,C", StringUtils.translate(path + "openConfigGui"));
        public static final ConfigHotkey MANIPULATE_NBT = new ConfigHotkey("manipulateNbt", "RIGHT_SHIFT", StringUtils.translate(path + "manipulateNbt"));


        public static final ImmutableList<ConfigHotkey> HOTKEYS = ImmutableList.of(
                KEY_MAIN_TOGGLE,
                KEY_OPEN_CONFIG_GUI,
                SHOW,
                HORIZONTAL_SCROLL,
                RESET_SCROLL,
                COPY,
                SHOW_ONLY_NBT,
                MANIPULATE_NBT
        );
    }

    public static void loadFromFile() {
        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject()) {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Settings", Settings.OPTIONS);
                ConfigUtils.readConfigBase(root, "Hotkeys", Hotkeys.HOTKEYS);
            }
        }
    }

    public static void saveToFile() {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Settings", Settings.OPTIONS);
            ConfigUtils.writeConfigBase(root, "Hotkeys", Hotkeys.HOTKEYS);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }

    @Override
    public void load() {
        loadFromFile();
    }

    @Override
    public void save() {
        saveToFile();
    }
}
