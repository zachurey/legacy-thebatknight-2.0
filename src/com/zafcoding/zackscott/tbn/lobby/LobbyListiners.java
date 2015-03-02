package com.zafcoding.zackscott.tbn.lobby;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import com.zafcoding.zackscott.tbn.Info;
import com.zafcoding.zackscott.tbn.Info.ServerState;
import com.zafcoding.zackscott.tbn.PlayerProfile;
import com.zafcoding.zackscott.tbn.PlayerProfile.PlayType;
import com.zafcoding.zackscott.tbn.TBN;
import com.zafcoding.zackscott.tbn.game.Game;

public class LobbyListiners implements Listener {

	Info info = TBN.info;
	TBN tbn = TBN.tbn;
	Game game = TBN.game;

	@EventHandler
	public void PlayerPre(AsyncPlayerPreLoginEvent e) {
		tbn.debugMsg("The PreLoginEvent has been called!");
		if (info.getState() == ServerState.In_Game
				|| info.getState() == ServerState.Post_Game
				|| info.getState() == ServerState.Resetting) {
			tbn.debugMsg("The PreLoginEvent has been called!");
			e.disallow(
					AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
					ChatColor.RED
							+ "You can not join the game while the game state is: "
							+ info.getState());
			tbn.debugMsg("The player " + e.getName()
					+ " has been kicked due to the wrong state!");
			// pp.kickPlayer(ChatColor.RED +
			// "You can not join the game while the game state is: " +
			// info.getState());
			return;
		}
		if (info.getPlayerCount() >= 30) {
			tbn.debugMsg("Too many players! " + e.getName()
					+ " has been disallowed!");
			e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
					ChatColor.RED + "The game is currently full!");
			return;
		}
	}

	@EventHandler
	public void PlayerJoin(PlayerJoinEvent e) {
		tbn.debugMsg("Join Event Called!");
		e.getPlayer().setGameMode(GameMode.SURVIVAL);
		e.getPlayer().setMaxHealth(20);
		e.getPlayer().setLevel(0);
		e.getPlayer().getInventory().setHelmet(new ItemStack(Material.AIR));
		e.getPlayer().getInventory().setChestplate(new ItemStack(Material.AIR));
		e.getPlayer().getInventory().setLeggings(new ItemStack(Material.AIR));
		e.getPlayer().getInventory().setBoots(new ItemStack(Material.AIR));
		info.addPlayer(e.getPlayer());
		info.getPP(e.getPlayer()).setType(PlayType.Villan);
		e.setJoinMessage(ChatColor.GOLD + "" + e.getPlayer().getDisplayName()
				+ "" + ChatColor.WHITE + " has joined the game!"
				+ ChatColor.YELLOW + " (" + info.getPlayerCount() + "/"
				+ tbn.getMaxPlayer() + ")");
		// e.getPlayer().teleport(tbn.getPlayerSpawn(0), TeleportCause.PLUGIN);
		e.getPlayer().teleport(info.getActiveWorld().getSpawnLocation());
	}

	@EventHandler
	public void onPlayerLeavae(PlayerQuitEvent e) {
		if (info.getPP(e.getPlayer()) != null) {
			Player p = e.getPlayer();
			info.removePlayer(p);
			info.setCount(info.getPlayerCount() - 1);
		}
	}

	@EventHandler
	public void PickupItem(PlayerPickupItemEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		if (info.getState() == ServerState.Pre_Game
				|| info.getState() == ServerState.Post_Game) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void DropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if (p.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		e.setCancelled(true);
	}

	@EventHandler
	public void BlockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if (tbn.debugMode && info.getPP(e.getPlayer()).did
				&& e.getBlock().getType() == Material.CHEST) {
			PlayerProfile aa = info.getPP(e.getPlayer());
			aa.removeLocation(e.getBlock().getLocation());
			e.getBlock().setType(Material.AIR);
			e.getPlayer().sendMessage(
					tbn.pre + "Successfully removed the chest!");
			return;
		}
		if (p.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		if (info.getState() == ServerState.Pre_Game
				|| info.getState() == ServerState.Post_Game) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void BlockerBreak(EntityDamageByEntityEvent e) {
		Entity en = e.getEntity();
		if (en instanceof Player) {
			if (((HumanEntity) en).getGameMode() == GameMode.CREATIVE) {
				return;
			}
			if (!(info.getState() == ServerState.In_Game || info.getState() == ServerState.Post_Game)
					|| info.pvp == false) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void CraftItem(CraftItemEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void BlockBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		tbn.debugMsg("Block Break Event! (e.getBlock=" + e.getBlock().getType());
		if (p.getGameMode() == GameMode.CREATIVE) {
			return;
		}
		if ((e.getBlock().getType() == Material.GLASS
				|| e.getBlock().getType() == Material.STAINED_GLASS_PANE || e
				.getBlock().getType() == Material.STAINED_GLASS)
				&& info.getState() == ServerState.In_Game) {
			return;
		}
		e.setCancelled(true);

	}

	@EventHandler
	public void ServerPing(ServerListPingEvent e) {
		e.setMaxPlayers(tbn.getMaxPlayer());
		if (info.getState() == ServerState.Pre_Game) {
			e.setMotd(ChatColor.GOLD + "" + ChatColor.BOLD + "TheBatNight"
					+ "\n" + ChatColor.GREEN
					+ "The server is currently in the lobby!");
		}
		if (info.getState() == ServerState.In_Game) {
			e.setMotd(ChatColor.GOLD + "" + ChatColor.BOLD + "TheBatNight"
					+ "\n" + ChatColor.AQUA
					+ "The server is currently in game!");
		}
		if (info.getState() == ServerState.Post_Game) {
			e.setMotd(ChatColor.GOLD + "" + ChatColor.BOLD + "TheBatNight"
					+ "\n" + ChatColor.GOLD
					+ "The game is over! Play again in a minute!");
		}
		if (info.getState() == ServerState.Resetting) {
			e.setMotd(ChatColor.GOLD + "" + ChatColor.BOLD + "TheBatNight"
					+ "\n" + ChatColor.RED + "The game is resetting!");
		}
	}

	@EventHandler
	public void onMobTarget(EntityTargetLivingEntityEvent e) {
		if (e.getTarget() instanceof Player) {
			Player p = (Player) e.getTarget();
			if (info.getPlayers().contains(p)) {
				e.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void PlayerChat(AsyncPlayerChatEvent e) {
		if (info.isSpect(e.getPlayer())) {
			info.deadChat(e.getMessage(), e.getPlayer());
			e.setCancelled(true);
		}
		if (info.getState() == ServerState.In_Game
				&& !info.isSpect(e.getPlayer())) {
			if (info.batman == e.getPlayer()) {
				e.setFormat(ChatColor.GRAY + "[BatKnight] " + ChatColor.WHITE
						+ e.getMessage());
			}
			if (info.robin == e.getPlayer()) {
				e.setFormat(ChatColor.GREEN + "[BirdBoy] " + ChatColor.WHITE
						+ e.getMessage());
			}
			if (info.joker == e.getPlayer()) {
				e.setFormat(ChatColor.LIGHT_PURPLE + "[Jester] "
						+ ChatColor.WHITE + e.getMessage());
			}
		}
		if (e.getPlayer().getDisplayName().equalsIgnoreCase("legostarwarszach")) {
			e.setFormat(ChatColor.WHITE
					+ "["
					+ ChatColor.AQUA
					+ "Dev"
					+ ChatColor.WHITE
					+ "]<"
					+ e.getPlayer().getDisplayName()
					+ "> "
					+ ChatColor.translateAlternateColorCodes('&',
							e.getMessage()));
			return;
		}
		if (e.getPlayer().getDisplayName().equalsIgnoreCase("zackscott")) {
			e.setFormat(ChatColor.WHITE
					+ "["
					+ ChatColor.BOLD
					+ ChatColor.AQUA
					+ "BlameMe"
					+ ChatColor.RESET
					+ ChatColor.WHITE
					+ "]<"
					+ e.getPlayer().getDisplayName()
					+ "> "
					+ ChatColor.translateAlternateColorCodes('&',
							e.getMessage()));
			return;
		}
		if (e.getPlayer().hasPermission("tt.admin")) {
			e.setFormat(ChatColor.WHITE
					+ "["
					+ ChatColor.RED
					+ "Admin"
					+ ChatColor.WHITE
					+ "]<"
					+ e.getPlayer().getDisplayName()
					+ "> "
					+ ChatColor.translateAlternateColorCodes('&',
							e.getMessage()));
			return;
		}
		if (e.getPlayer().hasPermission("tt.mod")) {
			e.setFormat(ChatColor.WHITE
					+ "["
					+ ChatColor.GOLD
					+ "Mod"
					+ ChatColor.WHITE
					+ "]<"
					+ e.getPlayer().getDisplayName()
					+ "> "
					+ ChatColor.translateAlternateColorCodes('&',
							e.getMessage()));
			return;
		}
		if (e.getPlayer().hasPermission("tt.pro")) {
			e.setFormat(ChatColor.WHITE
					+ "["
					+ ChatColor.GREEN
					+ "PRO"
					+ ChatColor.WHITE
					+ "]<"
					+ e.getPlayer().getDisplayName()
					+ "> "
					+ ChatColor.translateAlternateColorCodes('&',
							e.getMessage()));
			return;
		}
		e.setFormat(ChatColor.GRAY + "<" + e.getPlayer().getDisplayName()
				+ "> " + e.getMessage());
	}

	@EventHandler
	public void WeatherChange(WeatherChangeEvent e) {
		e.setCancelled(true);
	}

}
