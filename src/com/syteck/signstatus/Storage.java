package com.syteck.signstatus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Storage {

	private static String PREFIX = "[ss]";
	public static String getPrefix() { return PREFIX; }

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd HHaa");
	public static SimpleDateFormat getDateFormat() { return DATE_FORMAT; }

	private static File storageFile;
	public static File getStorageFile() { return storageFile; }

	private static YamlConfiguration storageYaml;
	public static YamlConfiguration getStorageYaml() { return storageYaml; }

	private static HashMap<String, User> users = new HashMap<String, User>();
	public static HashMap<String, User> getUsers() { return users; }
	public static User addUser(String id) { User user = new User(id); users.put(id, user); return user; }
	public static User getUser(String id) { return users.get(id); }
	public static void removeUser(String id) { users.remove(id); }

	public static SSign getSignWithoutUser(Location location) {

		for(Entry<String, User> user: users.entrySet()) {

			for(SSign sign: user.getValue().getSignList()) {

				if(sign.getLocation().equals(location)) return sign;

			}	
		}

		return null;
	}

	public static void load() {

		YamlConfiguration yaml = getStorageYaml();

		for(String str: yaml.getKeys(true)) {

			String id = yaml.getString(str+".id");
			List<String> list = yaml.getStringList(str+".list");

			User user = addUser(id);

			for(String encoded: list) {

				String[] decoded = encoded.split(", ");
				int x = Integer.parseInt(decoded[0]);
				int y = Integer.parseInt(decoded[1]);
				int z = Integer.parseInt(decoded[2]);
				String world = decoded[3];

				Location location = new Location(Bukkit.getWorld(world), x, y, z);
				user.addSign(location);
			}
		}
	}

	public static void saveUser(String id) {

		YamlConfiguration yaml = getStorageYaml();
		User user = getUser(id);

		ArrayList<String> list = new ArrayList<String>();

		for(SSign sign: user.getSignList()) {

			list.add(sign.getLocation().getBlockX()+", "+sign.getLocation().getBlockY()+", "+sign.getLocation().getBlockZ()+", "+sign.getLocation().getWorld().getName());

		}

		yaml.set(id+".id", id);
		yaml.set(id+".list", list);

		try {

			yaml.save(getStorageFile());

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	public static void saveAll() {

		for(Entry<String, User> entry: getUsers().entrySet()) {

			saveUser(entry.getKey());

		}
	}

	public static void reload(FileConfiguration config) {

		saveAll();

		if(config.contains("prefix")) { PREFIX = config.getString("prefix"); }
		if(config.contains("date-format")) { DATE_FORMAT = new SimpleDateFormat(config.getString("date-format")); }

	}

	public static void setup(File dataFolder, String fileName, FileConfiguration config) {

		storageFile = new File(dataFolder, fileName);
		storageYaml = YamlConfiguration.loadConfiguration(storageFile);

		if(!storageFile.exists()) {

			SignStatus.log("Failed to find storage.yml, creating new one.");

			try {

				storageFile.createNewFile();

			} catch (IOException e) {

				e.printStackTrace();

			}
		}

		if(config.contains("prefix")) { PREFIX = config.getString("prefix"); }
		if(config.contains("date-format")) { DATE_FORMAT = new SimpleDateFormat(config.getString("date-format")); }
	}

}
