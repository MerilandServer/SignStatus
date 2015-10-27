package com.syteck.signstatus.utils;

import com.earth2me.essentials.IEssentials;
import com.earth2me.essentials.User;
import java.util.Date;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.syteck.signstatus.Storage;
import org.bukkit.Bukkit;

public class SignUtil {

	public static String process(String str, String name) {
		String date = Storage.getDateFormat().format(new Date());

                IEssentials es = (IEssentials) Bukkit.getPluginManager().getPlugin("Essentials");
                User u2 = es.getOfflineUser(name);
                if (u2 != null) { //Si existe, obtener su fecha desde Essentials
                    Date fechaEssentials = new Date();
                    fechaEssentials.setTime(u2.getLastLogout());
                    date = Storage.getDateFormat().format(fechaEssentials);
                }
		return str.replaceAll("%date%", date).replaceAll("%name%", name);
	}

	public static boolean isValid(Block block) {

		if(block != null && block.getType() != Material.AIR) {

			if(block.getType().equals(Material.SIGN)) return true; 
			if(block.getType().equals(Material.SIGN_POST)) return true;
			if(block.getType().equals(Material.WALL_SIGN)) return true;

		}

		return false;
	}

	public static boolean canBreak(Block block) {

		if(block != null && block.getType() != Material.AIR) return true;

		return false;
	}

	public static void breakSign(Block block) {

		if(canBreak(block)) {

			block.setType(Material.AIR);	

		}
	}

}
