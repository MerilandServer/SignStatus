package com.syteck.signstatus.utils;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtil {

	public static boolean contains(String path, FileConfiguration config) { return config.get(path) != null; }

}
