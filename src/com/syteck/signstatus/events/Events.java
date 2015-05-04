package com.syteck.signstatus.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.syteck.signstatus.SSign;
import com.syteck.signstatus.Status;
import com.syteck.signstatus.Storage;
import com.syteck.signstatus.User;
import com.syteck.signstatus.utils.SignUtil;

public class Events implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onSignChangeEvent(SignChangeEvent event) {

		Player player = event.getPlayer();

		if(event.getLine(0).equalsIgnoreCase(Storage.getPrefix())) {

			if(!player.hasPermission("signstatus.create")) {

				player.sendMessage(ChatColor.RED+"You do not have permission to do this.");
				event.setCancelled(true);

			} else {

				if(event.getLine(1).equalsIgnoreCase("")) {

					player.sendMessage(ChatColor.RED+"You have to specify a player.");
					event.setCancelled(true);

				} else {

					OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(event.getLine(1));
					String id = offlinePlayer.getUniqueId().toString();

					if(offlinePlayer != null && offlinePlayer.hasPlayedBefore()) {

						User user;

						if(Storage.getUser(id) == null) { 

							user = Storage.addUser(id); 

						} else {

							user = Storage.getUser(id);

						}

						user.addSign(event.getBlock().getLocation());
						Storage.saveUser(id);

						String name = offlinePlayer.getName();

						if(offlinePlayer.isOnline()) {

							event.setLine(0, SignUtil.process(Storage.getOnlineLine(1), name));
							event.setLine(1, SignUtil.process(Storage.getOnlineLine(2), name));
							event.setLine(2, SignUtil.process(Storage.getOnlineLine(3), name));
							event.setLine(3, SignUtil.process(Storage.getOnlineLine(4), name));

						} else {

							event.setLine(0, SignUtil.process(Storage.getOfflineLine(1), name));
							event.setLine(1, SignUtil.process(Storage.getOfflineLine(2), name));
							event.setLine(2, SignUtil.process(Storage.getOfflineLine(3), name));
							event.setLine(3, SignUtil.process(Storage.getOfflineLine(4), name));

						}

						event.getBlock().getState().update();

					} else {

						player.sendMessage(ChatColor.RED+"This player is not valid.");

					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event) {

		Player player = event.getPlayer();
		Location location = event.getBlock().getLocation();

		if(Storage.getSignWithoutUser(location) != null) {

			SSign sign = Storage.getSignWithoutUser(location);
			Storage.getUser(sign.getId()).removeSign(location);

			player.sendMessage(ChatColor.RED+"Sign was removed from database.");
		}
	}

	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event) {

		String id = event.getPlayer().getUniqueId().toString();
		
		if(Storage.getUser(id) != null) {

			User user = Storage.getUser(id);

			user.setStatus(Status.ONLINE);
		}
	}

	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event) {

		String id = event.getPlayer().getUniqueId().toString();

		if(Storage.getUser(id) != null) {

			User user = Storage.getUser(id);

			user.setStatus(Status.OFFLINE);
		}
	}

}
