package com.zafcoding.zackscott.tbn;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

import com.zafcoding.zackscott.tbn.game.Game;

public class PlayerProfile {

	TBN tbn = TBN.tbn;
	Info info = TBN.info;
	Game game = TBN.game;
	Player p;
	int kills;
	int diamonds;
	boolean spectate = false;
	boolean dead = false;
	public boolean did = false;
	private boolean de = false;
	private boolean ra = false;
	PlayType ty = PlayType.Villan;
	public ArrayList<Location> loc = new ArrayList<Location>();
	public boolean shopuse = false;
	public ShopUse su = ShopUse.inactive;
	int coins = 0;
	public Scoreboard sb = null;

	public PlayerProfile(Player player, PlayType pt) {
		p = player;
		kills = 0;
		diamonds = 0;
		ty = pt;
	}

	public void confirmLocs() {
		int i = tbn.getConfig().getInt("Chest.amount");
		for (Location loca : loc) {
			tbn.getConfig().set(
					"Chest." + i,
					loca.getBlockX() + "," + loca.getBlockY() + ","
							+ loca.getBlockZ());
			p.sendMessage(tbn.pre + " Adding chest " + i + " out of "
					+ loc.size());
		}
		tbn.saveAll();
		p.sendMessage(tbn.pre + "Success!");
		clearLocations();
	}

	public void clearLocations() {
		loc.clear();
	}

	public boolean getDis() {
		return de;
	}

	public void setDis(boolean bool) {
		de = bool;
	}

	public boolean getRap() {
		return ra;
	}

	public void setRap(boolean bool) {
		ra = bool;
	}

	public void removeLocation(Location l) {
		for (Location ll : loc) {
			if (ll == l) {
				loc.remove(l);
			}
		}
	}

	public void addLocation(Location l) {
		loc.add(l);
	}

	public ArrayList getLocations() {
		return loc;
	}

	public void setDeath(boolean is) {
		dead = is;
	}

	public boolean isDead() {
		return dead;
	}

	public void setType(PlayType pt) {
		ty = pt;
	}

	public PlayType getType() {
		return ty;
	}

	public void setSpec(boolean spec) {
		p.getInventory().clear();
		Info.removeArmour(p);
		List itemlore = new ArrayList<>();
		itemlore.add(ChatColor.DARK_AQUA + "Help you find players!");
		p.getInventory().addItem(
				game.setName(new ItemStack(Material.COMPASS), ChatColor.GOLD
						+ "Player Finder", itemlore));
		spectate = spec;
	}

	public boolean getSpec() {
		return spectate;
	}

	public Player getPlayer() {
		return p;
	}

	public int getKills() {
		return kills;
	}

	public void setKills(int num) {
		kills = num;
	}

	public int getDiamonds() {
		return diamonds;
	}

	public void setDiamonds(int num) {
		diamonds = num;
	}

	public enum PlayType {
		Joker, Villan, BatNight, BirdBoy, KittyKat, Puffin
	}

	public enum ShopUse {
		inactive, batblocker, invis
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int i) {
		TBN.debugMsg("Setting coins for " + p.getDisplayName() + " to " + i);
		coins = i;
	}

}
