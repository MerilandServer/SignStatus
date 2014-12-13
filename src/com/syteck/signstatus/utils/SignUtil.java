package com.syteck.signstatus.utils;

import java.util.Date;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.syteck.signstatus.Storage;

public class SignUtil {

	public static String process(String str, String name) {

		String date = Storage.getDateFormat().format(new Date());

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
