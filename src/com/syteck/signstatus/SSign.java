package com.syteck.signstatus;

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

			sign.setLine(0, SignUtil.process(Storage.getOnlineLine(1), name));
			sign.setLine(1, SignUtil.process(Storage.getOnlineLine(2), name));
			sign.setLine(2, SignUtil.process(Storage.getOnlineLine(3), name));
			sign.setLine(3, SignUtil.process(Storage.getOnlineLine(4), name));

		} else if(status.equals(Status.OFFLINE)) {

			sign.setLine(0, SignUtil.process(Storage.getOfflineLine(1), name));
			sign.setLine(1, SignUtil.process(Storage.getOfflineLine(2), name));
			sign.setLine(2, SignUtil.process(Storage.getOfflineLine(3), name));
			sign.setLine(3, SignUtil.process(Storage.getOfflineLine(4), name));

		} else if(status.equals(Status.UNKNOWN)) {

			sign.setLine(0, ChatColor.RED+name);
			sign.setLine(1, ChatColor.RED+"error");
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
