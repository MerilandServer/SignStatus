package com.syteck.signstatus.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class SignUtil {

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
