package com.syteck.signstatus;

import java.util.ArrayList;

import org.bukkit.Location;

import com.syteck.signstatus.utils.SignUtil;

public class User {

	private String id;

	private ArrayList<SSign> signs = new ArrayList<SSign>();

	public User(String id) {

		this.id = id;

	}

	public String getId() { return this.id; }
	public ArrayList<SSign> getSignList() { return this.signs; }

	public void addSign(Location location) { 

		SSign sign = new SSign(this.getId(), location);
		this.getSignList().add(sign);

	}

	public SSign getSign(Location location) {

		for(SSign sign: this.getSignList()) {

			if(sign.getLocation().equals(location)) return sign;	

		}

		return null;
	}

	public void removeSign(Location location) {

		SSign sign = getSign(location);
		int i = this.getSignList().indexOf(sign);
		this.getSignList().remove(i);

		Storage.saveUser(this.getId());
	}

	public void setStatus(Status status) {

		for(SSign sign: signs) {

			if(!SignUtil.isValid(sign.getLocation().getBlock())) { 

				this.removeSign(sign.getLocation());
				SignStatus.log("Sign from user "+id+" was not valid and removed.");

			} else {

				sign.setStatus(status);

			}
		}	
	}

	public void clean() {

		for(SSign sign: this.getSignList()) {

			removeSign(sign.getLocation());

		}

		signs = null;
	}
}
