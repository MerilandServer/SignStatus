package com.syteck.signstatus;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import com.syteck.signstatus.utils.SignUtil;
import com.syteck.signstatus.utils.UUIDUtil;

public class SSign {

	private String id;
	private Location location;

	public SSign(String id, Location location) {

		this.id = id;
		this.location = location;

	}

	public String getId() { return this.id; }
	public Location getLocation() { return this.location; }

	public Sign getSign() { return (Sign) this.location.getBlock().getState(); }

	public void setStatus(Status status) {

		Sign sign = this.getSign();

		String name = Bukkit.getOfflinePlayer(UUIDUtil.fromString(this.getId())).getName();

		if(status.equals(Status.ONLINE)) {

			sign.setLine(0, ChatColor.GREEN+name);
			sign.setLine(1, ChatColor.GREEN+"online");
			sign.setLine(2, "");
			sign.setLine(3, "");

		} else if(status.equals(Status.OFFLINE)) {

			String date = Storage.getDateFormat().format(new Date());

			sign.setLine(0, ChatColor.RED+name);
			sign.setLine(1, ChatColor.RED+"offline");
			sign.setLine(2, ChatColor.RED+"since");
			sign.setLine(3, ChatColor.RED+date);

		} else if(status.equals(Status.UNKNOWN)) {

			sign.setLine(0, ChatColor.RED+name);
			sign.setLine(1, ChatColor.RED+"unknown");
			sign.setLine(2, "");
			sign.setLine(3, "");

		}

		sign.update();
	}

	public void clean() {

		Block block = this.getLocation().getBlock();

		SignUtil.breakSign(block);
	}
}
