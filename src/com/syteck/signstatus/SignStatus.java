package com.syteck.signstatus;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.syteck.signstatus.events.Events;

public class SignStatus extends JavaPlugin {

	public static Logger logger;
	public static void log(String str) { logger.log(Level.INFO, str); }

	private static final double VERSION = 1.4;
	public static double getVersion() { return VERSION; }

	private static SignStatus i;
	public static SignStatus getInstance() { return i; }

	@Override
	public void onDisable() {

		Storage.saveAll();

	}

	@Override
	public void onEnable() {

		i = this;

		logger = this.getLogger();

		Storage.setup("storage.yml");
		Storage.load();

		Bukkit.getPluginManager().registerEvents(new Events(), this);

	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(command.getName().equalsIgnoreCase("signstatus")) {

			if(!sender.hasPermission("signstatus.reload")) {

				sender.sendMessage(ChatColor.RED+"You do not have permission to do this.");

			} else {

				if(sender instanceof Player) {

					sender.sendMessage(ChatColor.GREEN+"You saved the signs and reloaded the config.");

				} else {

					log("You saved the signs and reloaded the config.");

				}

				Storage.reload();
			}
		}

		return true;
	}
}
