package com.zafcoding.zackscott.tbn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.zafcoding.zackscott.tbn.PlayerProfile.PlayType;
import com.zafcoding.zackscott.tbn.orginial.TheBatKnight;

public class Info {

	TBN tbn = TBN.tbn;
	ArrayList<Player> players = new ArrayList<Player>();
	int lobbytime = 120;
	int gametime = 600;
	int playerc = 0;
	public static int spawnwitch = 1;
	public static boolean superChest = false;
	public static boolean didsuperChest = false;
	public static boolean herofreeze = false;
	public static int mapId;
	public static String mapName;
	public static Player batman = null;
	public static Player robin = null;
	public static Player joker = null;
	public static Player puffin = null;
	public static Player[] badGuys = null;
	World active = null;
	public boolean pvp = false;
	ServerState state = ServerState.Pre_Game;
	ArrayList<PlayerProfile> profiles = new ArrayList<PlayerProfile>();
	ArrayList<Player> spects = new ArrayList<Player>();
	public static ArrayList<Player> ingame = new ArrayList<Player>();
	public static ArrayList<Location> fakechests = new ArrayList<Location>();
	public static ArrayList<Location> chests = new ArrayList<Location>();
	public static HashMap<Location, Material> broke = new HashMap<Location, Material>();
	PlayerProfile winner = null;

	public boolean isSpect(Player p) {
		return spects.contains(p);
	}

	public World getActiveWorld() {
		if (active == null) {
			setActiveWorld(Bukkit.getWorld("SamCity"));
		}
		return active;
	}

	public void setActiveWorld(World world) {
		active = world;
	}

	public void setWinner(PlayerProfile win) {
		winner = win;
	}

	public PlayerProfile getWinner() {
		return winner;
	}

	public void setGameTime(int n) {
		gametime = n;
	}

	public int getGameTime() {
		return gametime;
	}

	public void addPlayerPro(PlayerProfile player) {
		profiles.add(player);
	}

	public void removePlayerPro(Player player) {
		for (Object pp : profiles.toArray()) {
			if (pp instanceof PlayerProfile) {
				if (((PlayerProfile) pp).getPlayer().equals(player)) {
					profiles.remove(pp);
				}
			}
		}
	}

	public PlayerProfile getPP(Player pl) {
		for (Object pp : profiles.toArray()) {
			if (pp instanceof PlayerProfile) {
				if (((PlayerProfile) pp).getPlayer().equals(pl)) {
					return (PlayerProfile) pp;
				}
			}
		}
		return null;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player p) {
		if (getState() == ServerState.Pre_Game) {
			if (getPlayerCount() >= 20) {
				p.kickPlayer(ChatColor.RED + "The server is full!");
				return;
			}
			playerc++;
			tbn.debugMsg("Increased player count! (" + playerc + ")");
			players.add(p);
			tbn.debugMsg("Player added to the ArrayList!");
			ingame.add(p);
			tbn.debugMsg("Player added to the In_Game ArrayList!");
			p.getInventory().clear();
			tbn.debugMsg("Player's Inventory Cleared!");
			PlayerProfile pp = new PlayerProfile(p, PlayType.Villan);
			addPlayerPro(pp);
			tbn.debugMsg("Created and added new PlayerProfile!");
			p.sendMessage(ChatColor.YELLOW + "You have joined the game!");
			return;
		} else {
			p.kickPlayer(ChatColor.RED
					+ "You can not join the game while the game state is: "
					+ getState());
		}

	}

	public void outplayer(Player p) {
		if (players.contains(p)) {
			PlayerProfile pp = getPP(p);
			pp.setSpec(true);
			pp.setDeath(true);
			playerc--;
			ingame.remove(p);
			p.setGameMode(GameMode.SPECTATOR);
			spects.add(p);
		}
	}

	public static void hidePlayer(Player player) {
		for (Player p : Bukkit.getOnlinePlayers())
			p.hidePlayer(player);
	}

	public static void showPlayer(Player player) {
		for (Player p : Bukkit.getOnlinePlayers())
			p.showPlayer(player);
	}

	public static void showPlayers() {
		for (Player p : Bukkit.getOnlinePlayers())
			showPlayer(p);
	}

	public void removePlayer(Player p) {
		if (players.contains(p)) {
			players.remove(p);
		}
	}

	public int getTime() {
		return lobbytime;
	}

	public void setTime(Integer time) {
		lobbytime = time;
	}

	public enum ServerState {
		Pre_Game, In_Game, Post_Game, Resetting
	}

	public ServerState getState() {
		return state;
	}

	public void setState(ServerState server) {
		state = server;
	}

	public int getPlayerCount() {
		return playerc;
	}

	public void setCount(Integer in) {
		playerc = in;
	}

	public void broadCast(String message) {
		for (Player play : Bukkit.getOnlinePlayers()) {
			if (players.contains(play)) {
				play.sendMessage(message);
			}
		}
	}

	public int getRandom(int small, int large) {
		Random rand = new Random();
		return rand.nextInt(large) + small;
	}

	public static void createDisplay(Material material, Inventory inv,
			int Slot, int ammount, String name, String lore) {
		ItemStack item = new ItemStack(material, ammount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		ArrayList<String> Lore = new ArrayList<String>();
		Lore.add(lore);
		meta.setLore(Lore);
		item.setItemMeta(meta);

		inv.setItem(Slot, item);

	}

	public static void clearAllInventories() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.getInventory().clear();
			ItemStack[] is = { new ItemStack(Material.AIR),
					new ItemStack(Material.AIR), new ItemStack(Material.AIR),
					new ItemStack(Material.AIR) };
			player.getInventory().setArmorContents(is);
		}
	}

	public void deadChat(String message, Player player) {
		for (Player pl : players) {
			if (pl.hasPermission("tbk.mod")
					|| (pl.hasPermission("tbk.admin") || isSpect(pl))) {
				pl.sendMessage(ChatColor.RED + "[DEAD] " + ChatColor.YELLOW
						+ "" + player.getDisplayName() + ChatColor.WHITE + " "
						+ message);
			}
		}
	}

	public static void removeArmour(Player player) {
		ItemStack[] is = { new ItemStack(Material.AIR),
				new ItemStack(Material.AIR), new ItemStack(Material.AIR),
				new ItemStack(Material.AIR) };
		player.getInventory().setArmorContents(is);
	}

	public static void updateAllInventories() {
		for (Player player : Bukkit.getOnlinePlayers())
			player.updateInventory();
	}

	@Deprecated
	public static void bounceBlock(TheBatKnight plugin, Block b) {
		try {
			if (b == null)
				return;
			Material type = b.getType();
			byte data = b.getData();
			Location l = b.getLocation();
			b.setType(Material.AIR);
			FallingBlock fb = b.getWorld().spawnFallingBlock(l, type, data);

			float x = -0.6F + (float) (Math.random() * 2.2D);
			float y = -2.0F + (float) (Math.random() * 5.0D);
			float z = -0.3F + (float) (Math.random() * 1.6D);

			fb.setVelocity(new Vector(x, y, z));
		} catch (Exception localException) {
		}
	}

}
