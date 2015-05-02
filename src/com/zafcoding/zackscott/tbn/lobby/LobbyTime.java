package com.zafcoding.zackscott.tbn.lobby;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.zafcoding.zackscott.tbn.Info;
import com.zafcoding.zackscott.tbn.Info.ServerState;
import com.zafcoding.zackscott.tbn.TBN;
import com.zafcoding.zackscott.tbn.game.Game;

public class LobbyTime {

	TBN tbn = TBN.tbn;
	Info info = tbn.info;
	Game game = null;

	@SuppressWarnings("static-access")
	public void LobbyHeartBeat() {
		if (info.getActiveWorld() == null) {
			String worldName = "SamCity";
			info.setActiveWorld(Bukkit.getWorld("SamCity"));
			File playerFilesDir = new File(worldName + "/players");
			if (playerFilesDir.isDirectory()) {
				String[] playerDats = playerFilesDir.list();
				for (int i = 0; i < playerDats.length; i++) {
					File datFile = new File(playerFilesDir, playerDats[i]);
					datFile.delete();
				}
			}
		}
		if (game == null) {
			game = tbn.getG();
		}
		if (info.getState() == ServerState.Pre_Game) {
			for (Player pp : info.getPlayers()) {
				pp.setHealth(20);
				pp.setCanPickupItems(false);
				pp.setFoodLevel(20);
				pp.setFireTicks(0);
				pp.setLevel(info.getTime());
			}
			dotime();
		}
	}

	private void dotime() {
		if (info.getTime() == 120 || info.getTime() == 90
				|| info.getTime() == 60 || info.getTime() == 30
				|| info.getTime() == 15 || info.getTime() == 10
				|| info.getTime() == 9 || info.getTime() == 8
				|| info.getTime() == 7 || info.getTime() == 6
				|| info.getTime() == 5 || info.getTime() == 4
				|| info.getTime() == 3 || info.getTime() == 2
				|| info.getTime() == 1) {
			broadcastTime(info.getTime());
		}
		if (info.getTime() == 0) {
			if (info.getPlayerCount() >= tbn.getMinPlayer()) {
				if (info.getState() == ServerState.In_Game) {
					return;
				}
				info.setState(ServerState.In_Game);
				game.start();
				return;
			} else {
				info.broadCast("");
				info.broadCast("");
				info.broadCast(ChatColor.AQUA
						+ "No enough players! That game needs at least "
						+ tbn.getMinPlayer() + " players to start!");
				info.broadCast(ChatColor.AQUA + "Restarting da clock!");
				info.broadCast("");
				info.setTime(120);
				for (final Player player : Bukkit.getOnlinePlayers()) {
					final Location l = new Location(player.getWorld(), player
							.getLocation().getBlockX(), player.getLocation()
							.getBlockY() + 3, player.getLocation().getBlockZ());
					new Thread(new Runnable() {
						public void run() {
							try {
								for (int i = 0; i < 4; i++) {
									player.playEffect(l, Effect.CLICK1, 1);
									Thread.sleep(450L);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
				return;
			}
		}
		info.setTime(info.getTime() - 1);
	}

	private void broadcastTime(int t) {
		if (t == 120) {
			info.broadCast(ChatColor.RED + "2 minutes " + ChatColor.GOLD
					+ "till the game starts!");
			info.broadCast(ChatColor.GOLD + "Map: "
					+ info.getActiveWorld().getName());
			info.broadCast(ChatColor.GOLD + "" + ChatColor.RED
					+ info.getPlayerCount() + ChatColor.GOLD + "/"
					+ tbn.getMaxPlayer());
			info.broadCast(ChatColor.GOLD + "You need " + tbn.getMinPlayer()
					+ " players to start the game.");
			return;
		}
		if (t == 90) {
			info.broadCast(ChatColor.RED + "1 minute 30 seconds "
					+ ChatColor.GOLD + "till the game starts!");
			info.broadCast(ChatColor.GOLD + "Map: "
					+ info.getActiveWorld().getName());
			info.broadCast(ChatColor.GOLD + "" + ChatColor.RED
					+ info.getPlayerCount() + ChatColor.GOLD + "/"
					+ tbn.getMaxPlayer());
			info.broadCast(ChatColor.GOLD + "You need " + tbn.getMinPlayer()
					+ " players to start the game.");
			return;
		}
		if (t == 60) {
			info.broadCast(ChatColor.RED + "1 minute " + ChatColor.GOLD
					+ " till the game starts!");
			info.broadCast(ChatColor.GOLD + "Map: "
					+ info.getActiveWorld().getName());
			info.broadCast(ChatColor.GOLD + "" + ChatColor.RED
					+ info.getPlayerCount() + ChatColor.GOLD + "/"
					+ tbn.getMaxPlayer());
			info.broadCast(ChatColor.GOLD + "You need " + tbn.getMinPlayer()
					+ " players to start the game.");
			return;
		}
		if (t <= 10) {
			info.broadCast(ChatColor.RED + "" + t + ChatColor.GOLD
					+ " seconds till the game starts!");
			for (Player player : Bukkit.getOnlinePlayers()) {
				player.playSound(player.getLocation(), Sound.NOTE_PLING, 10.0F,
						0.0F);
				player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR,
						10.0F, 0.0F);
				player.playSound(player.getLocation(), Sound.NOTE_BASS, 10.0F,
						0.0F);
				player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM,
						10.0F, 0.0F);
				return;
			}
		}
		info.broadCast(ChatColor.RED + "" + t + ChatColor.GOLD
				+ " seconds till the game starts!");
	}

}
