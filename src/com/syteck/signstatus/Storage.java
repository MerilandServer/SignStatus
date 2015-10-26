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
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.syteck.signstatus.utils.ConfigUtil;
import com.syteck.signstatus.utils.UUIDUtil;

public class Storage {

	private static String[] PREFIX = new String[] {"[ss]", "[statussigns]"};
	public static String[] getPrefixes() { return PREFIX; }

	private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM HH:mm");
	public static SimpleDateFormat getDateFormat() { return DATE_FORMAT; }

	private static File storageFile;
	public static File getStorageFile() { return storageFile; }

	private static YamlConfiguration storageYaml;
	public static YamlConfiguration getStorageYaml() { return storageYaml; }

	private static File configFile;
	public static File getConfigFile() { return configFile; }

	private static final HashMap<Integer, String> online = new HashMap<>();
	private static final HashMap<Integer, String> offline = new HashMap<>();
	public static String getOnlineLine(int i) { return online.get(i).replaceAll("&", "§"); }
	public static String getOfflineLine(int i) { return offline.get(i).replaceAll("&", "§"); }

	public static void addLine(Status status, int i, String message) {

		if(status.equals(Status.ONLINE)) {

			online.put(i, message);

		} else if(status.equals(Status.OFFLINE)) {

			offline.put(i, message);

		} else {

			SignStatus.log("Error customizing lines!");

		}	
	}

	private static final HashMap<String, User> users = new HashMap<>();
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

		for(String str: yaml.getKeys(false)) {

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

		if(!storageFile.exists()) {

			SignStatus.log("Failed to find storage.yml, creating new one.");

			try {

				storageFile.createNewFile();

			} catch (IOException e) {

				e.printStackTrace();

			}
		}

		YamlConfiguration yaml = getStorageYaml();
		User user = getUser(id);

		ArrayList<String> list = new ArrayList<>();

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

		if(!storageFile.exists()) {

			SignStatus.log("Failed to find storage.yml, creating new one.");

			try {

				storageFile.createNewFile();

			} catch (IOException e) {

				e.printStackTrace();

			}
		}

		for(Entry<String, User> entry: getUsers().entrySet()) {

			saveUser(entry.getKey());

		}
	}

	public static void updateAll() {

		for(Entry<String, User> entry: getUsers().entrySet()) {

			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(UUIDUtil.fromString(entry.getKey()));
			User user = entry.getValue();

			if(offlinePlayer.isOnline()) {

				user.setStatus(Status.ONLINE);

			} else {

				user.setStatus(Status.OFFLINE);

			}
		}
	}

	public static void reload() {

		SignStatus main = SignStatus.getInstance();

		saveAll();	
		main.reloadConfig();
		updateConfig();

		updateAll();
	}

	public static void setup(String fileName) {

		File dataFolder = SignStatus.getInstance().getDataFolder();

		configFile = new File(dataFolder, "config.yml");
		updateConfig();

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
	}

	public static void updateConfig() {

		File configFile = getConfigFile();
		SignStatus main = SignStatus.getInstance();
		FileConfiguration config = main.getConfig();

		if(!configFile.exists()) {

			SignStatus.log("Failed to find config.yml, creating new one.");
			main.saveDefaultConfig();

			config = main.getConfig();
		}

		if(!ConfigUtil.contains("version", config) || config.getDouble("version") != SignStatus.getVersion()) {

			SignStatus.log("Invalid config detected, creating new one.");

			configFile.delete();
			main.saveDefaultConfig();

			config = main.getConfig();
		}

		if(ConfigUtil.contains("date-format", config)) { DATE_FORMAT = new SimpleDateFormat(config.getString("date-format")); }

		if(ConfigUtil.contains("online-sign", config)) {

			String os = "online-sign";

			for(int i = 1; i<=4; i++) {

				addLine(Status.ONLINE, i, config.getString(os+"."+i));

			}

		} else {

			addLine(Status.ONLINE, 1, "&a%name%");
			addLine(Status.ONLINE, 2, "&aEn Línea");
			addLine(Status.ONLINE, 3, "");
			addLine(Status.ONLINE, 4, "");

		}

		if(ConfigUtil.contains("offline-sign", config)) {

			String os = "offline-sign";

			for(int i = 1; i<=4; i++) {

				addLine(Status.OFFLINE, i, config.getString(os+"."+i));

			}

		} else {

			addLine(Status.OFFLINE, 1, "&c%name%");
			addLine(Status.OFFLINE, 2, "&cDesconectado");
			addLine(Status.OFFLINE, 3, "&cdesde");
			addLine(Status.OFFLINE, 4, "&c%date%");

		}
	}
}
