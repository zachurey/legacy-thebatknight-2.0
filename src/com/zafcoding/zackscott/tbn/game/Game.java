package com.zafcoding.zackscott.tbn.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.zafcoding.zackscott.tbn.Info;
import com.zafcoding.zackscott.tbn.PlayerProfile;
import com.zafcoding.zackscott.tbn.Info.ServerState;
import com.zafcoding.zackscott.tbn.PlayerProfile.PlayType;
import com.zafcoding.zackscott.tbn.TBN;

public class Game {

	static TBN tbn = TBN.tbn;
	static Info info = TBN.info;
	static Locations loc = TBN.loc;
	final PlayerProfile mostdi1a = null;

	public void start() {
		info.setState(ServerState.In_Game);
		info.setGameTime(tbn.getConfig().getInt("MatchLengh") * 60);
		setHeroesAndBadGuys(tbn);
		loc.populateChests(info.superChest);
		sendHowToPlayInfo(info.robin, info.batman, info.badGuys, info.joker);
		info.herofreeze = true;
		for (Player pl : info.getPlayers()) {
			pl.setCanPickupItems(true);
			pl.sendMessage(ChatColor.AQUA + "The game has started!");
			if (info.batman != null) {
				pl.sendMessage(ChatColor.GRAY + "Your BatNight is "
						+ info.batman.getDisplayName());
			}
			if (info.robin != null) {
				pl.sendMessage(ChatColor.DARK_GREEN + "Your BirdBoy is "
						+ info.robin.getDisplayName());
			}
			if (info.joker != null) {
				pl.sendMessage(ChatColor.DARK_PURPLE + "Your Jester is "
						+ info.joker.getDisplayName());
			}
			if (info.puffin != null) {
				pl.sendMessage(ChatColor.LIGHT_PURPLE + "Your Puffin is "
						+ info.puffin.getDisplayName());
			}
		}
		dosomeDiamondLvl();
	}

	@SuppressWarnings("deprecation")
	public static void endGame(final int bo) {
		info.setState(ServerState.Post_Game);
		PlayerProfile mostdia = null;
		if (bo == 1) {
			int total = 0;
			for (Player pa : info.getPlayers()) {
				PlayerProfile pq = info.getPP(pa);
				pa.teleport(tbn.getPlayerSpawn(info.getActiveWorld().getName(),
						0));
				pa.getInventory().clear();
				pa.setGameMode(GameMode.ADVENTURE);
				if (mostdia == null || pq.getDiamonds() > mostdia.getDiamonds()) {
					mostdia = pq;
				}
				total = total + pq.getDiamonds();
			}
			info.setWinner(mostdia);
			info.broadCast(ChatColor.WHITE + "----------" + ChatColor.GOLD
					+ "TheBatKnight" + ChatColor.WHITE + "----------");
			info.broadCast(ChatColor.GREEN + "Enjoy " + ChatColor.BLUE
					+ " the last game?" + ChatColor.AQUA
					+ " Tell everyone with /gg!");
			info.broadCast(ChatColor.YELLOW + "The bad guys won!");
			info.broadCast(ChatColor.YELLOW + "Bad guy winner is "
					+ ChatColor.DARK_AQUA
					+ mostdia.getPlayer().getDisplayName() + ChatColor.YELLOW
					+ " (" + ChatColor.DARK_AQUA + mostdia.getDiamonds()
					+ ChatColor.YELLOW + ")");
			info.broadCast(ChatColor.YELLOW
					+ "The bad guys collected a total of " + ChatColor.AQUA
					+ total + "" + ChatColor.YELLOW + " diamonds!");
		}
		if (bo == 0) {
			PlayerProfile mostkill = null;
			int totalkill = 0;
			for (Player pa : info.getPlayers()) {
				PlayerProfile pp = info.getPP(pa);
				pa.teleport(tbn.getPlayerSpawn(info.getActiveWorld().getName(),
						0));
				pa.getInventory().clear();
				pa.setGameMode(GameMode.ADVENTURE);
				if (pp.getType() == PlayType.BatNight
						|| pp.getType() == PlayType.BirdBoy) {
					if (mostkill == null || pp.getKills() > mostkill.getKills()) {
						mostkill = pp;
					}
					totalkill = totalkill + pp.getKills();
				}
				if (mostkill == null) {
					mostkill = info.getPP(pa);
				}
			}
			info.setState(ServerState.Post_Game);
			info.broadCast(ChatColor.WHITE + "----------" + ChatColor.GOLD
					+ "TheBatKnight" + ChatColor.WHITE + "----------");
			info.broadCast(ChatColor.GREEN + "Enjoy " + ChatColor.BLUE
					+ " the last game?" + ChatColor.AQUA
					+ " Tell everyone with /gg!");
			info.broadCast(ChatColor.AQUA + "The good guys won!");
			info.broadCast(ChatColor.AQUA + "The best hero is "
					+ ChatColor.DARK_AQUA
					+ mostkill.getPlayer().getDisplayName() + ChatColor.AQUA
					+ " (" + ChatColor.DARK_AQUA + mostkill.getDiamonds()
					+ ChatColor.AQUA + ")");
			info.broadCast(ChatColor.AQUA + "The heros killed "
					+ ChatColor.DARK_AQUA + totalkill + "" + ChatColor.AQUA
					+ " players!");
		}
		tbn.debugMsg("Removed chests: " + removeChest());
		tbn.debugMsg("Changed blocks: " + removeChest());
		info.broadCast(ChatColor.RED + "" + ChatColor.BOLD
				+ "Server reseting in 10 seconds!");
		Bukkit.getScheduler().scheduleAsyncDelayedTask(tbn, new Runnable() {

			@Override
			public void run() {
				for (Player ppp : info.getPlayers()) {
					if (bo == 1) {
						ppp.kickPlayer(ChatColor.GOLD
								+ ""
								+ ChatColor.BOLD
								+ "TheBatKnight"
								+ ChatColor.RESET
								+ "\nThanks for playing! \nRejoin to play another game!\n"
								+ ChatColor.GREEN + "Winner: "
								+ ChatColor.DARK_AQUA
								+ info.getWinner().getPlayer().getDisplayName()
								+ " (" + info.getWinner().getDiamonds() + ")");
					}
					if (bo == 0) {
						ppp.kickPlayer(ChatColor.GOLD
								+ ""
								+ ChatColor.BOLD
								+ "TheBatKnight"
								+ ChatColor.RESET
								+ "\nThanks for playing! \nRejoin to play another game!\n"
								+ ChatColor.GREEN + "Winners: "
								+ ChatColor.DARK_AQUA + "Heros!");
					}
					try {
						Bukkit.getServer().dispatchCommand(
								Bukkit.getConsoleSender(), "reload");
					} catch (Exception e) {
						tbn.debugMsg("Err_ " + e.getMessage());
						Bukkit.getServer().reload();
					}
				}
			}
		}, 200L);
	}

	public static int removeChest() {
		int chestdone = 0;
		int totala = info.chests.size();
		for (Object loca : info.chests.toArray()) {
			Location loc = (Location) loca;
			if (loc.getBlock().getType() == Material.CHEST) {
				loc.getBlock().setType(Material.AIR);
				chestdone++;
				tbn.debugMsg("Removed chest " + chestdone + "/" + totala);
				info.chests.remove(loc);
			}
		}
		clearEnts();
		return chestdone;
	}

	public static int removeBlock() {
		int blockchange = 0;
		int totala = info.broke.size();
		HashMap<Location, Material> newbroke = new HashMap<Location, Material>(info.broke);
		for (Object loca : newbroke.keySet()) {
			Location loc = (Location) loca;
			Material mat = info.broke.get(loca);
			loc.getBlock().setType(mat);
			info.broke.remove(loc);
			newbroke.remove(loc);
		}
		clearEnts();
		return blockchange;
	}

	@SuppressWarnings("static-access")
	public static void clearEnts() {
		int done = 0;
		for (Entity ent : info.getActiveWorld().getEntities()) {
			if (ent.getType() == EntityType.DROPPED_ITEM) {
				done++;
				ent.remove();
			}
		}
		tbn.debugMsg("Removed " + done + " entites!");
	}

	public ItemStack setName(ItemStack is, String name, List<String> lore) {
		ItemMeta m = is.getItemMeta();
		if (name != null)
			m.setDisplayName(name);
		if (lore != null)
			m.setLore(lore);
		is.setItemMeta(m);
		return is;
	}

	public void stop(String reason) {
		if (reason == "unknown") {

		}
	}

	public void dosomeDiamondLvl() {
		tbn.getServer().getScheduler()
				.scheduleSyncRepeatingTask(tbn, new Runnable() {
					public void run() {
						if (info.getState() == ServerState.In_Game)
							for (Player p : Bukkit.getOnlinePlayers())
								p.setLevel(getDiamonds(p));
					}
				}, 20L, 20L);
	}

	public int getDiamonds(Player player) {
		int total = 0;
		for (ItemStack is : player.getInventory()) {
			if ((is != null) && (is.getType() == Material.DIAMOND)) {
				total += is.getAmount();
			}
		}
		return total;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "static-access", "unused",
			"deprecation", "null" })
	private void setHeroesAndBadGuys(TBN tbn2) {
		Player[] players = null;
		players = new Player[info.getPlayers().size()];
		int io = 0;
		for (Player pl : info.getPlayers()) {
			players[io] = pl;
			io++;
		}
		Random rand = new Random();
		int batmanIndex = rand.nextInt(players.length);
		int robinIndex = rand.nextInt(players.length);
		int jockerIndex = rand.nextInt(players.length);
		int puffinIndex = rand.nextInt(players.length);

		while (batmanIndex == robinIndex)
			robinIndex = rand.nextInt(players.length);
		while ((batmanIndex == jockerIndex || robinIndex == jockerIndex))
			jockerIndex = rand.nextInt(players.length);
		while ((batmanIndex == puffinIndex || robinIndex == puffinIndex)
				|| jockerIndex == puffinIndex)
			puffinIndex = rand.nextInt(players.length);

		tbn.debugMsg("Setting batman...");
		if (info.batman == null) {
			info.batman = players[batmanIndex];
			info.getPP(info.batman).setType(PlayType.BatNight);
		}
		tbn.debugMsg("Setting robin...");

		if (info.robin == null) {
			info.robin = players[robinIndex];
			info.getPP(info.robin).setType(PlayType.BirdBoy);
		}

		tbn.debugMsg("Setting joker...");
		if (info.joker == null) {
			info.joker = players[jockerIndex];
			info.getPP(info.joker).setType(PlayType.Joker);
		}
		tbn.debugMsg("Setting puffin...");
		if (info.puffin == null) {
			info.joker = players[jockerIndex];
			info.getPP(info.joker).setType(PlayType.Joker);
		}
		info.badGuys = new Player[info.getPlayers().size()];

		/*
		 * if ((catwoman == null) && (vipsOnline > 1)) { int catwomanRand =
		 * rand.nextInt(vipsOnline);
		 * 
		 * int cwIndex = 0;
		 * 
		 * for (Player p : Bukkit.getOnlinePlayers()) { if
		 * (TheBatKnight.vips.contains(p.getName())) { System.out.println("CW: "
		 * + cwIndex + " " + p.getName() + " " + catwomanRand); if
		 * ((catwomanRand == cwIndex) && (p != batman) && (p != robin) && (p !=
		 * catwoman) && (p != joker) && (p != mrFreeze) && (p != penguin)) {
		 * catwoman = p; break; } cwIndex++; } } }
		 * 
		 * if ((penguin == null) && (vipsOnline > 1)) { int penguinRand =
		 * rand.nextInt(vipsOnline);
		 * 
		 * int pIndex = 0;
		 * 
		 * for (Player p : Bukkit.getOnlinePlayers()) { if
		 * (TheBatKnight.vips.contains(p.getName())) { System.out.println("P: "
		 * + pIndex + " " + p.getName() + " " + penguinRand); if ((pIndex ==
		 * penguinRand) && (p != batman) && (p != robin) && (p != catwoman) &&
		 * (p != joker) && (p != mrFreeze) && (p != penguin)) { penguin = p;
		 * break; } pIndex++; } } }
		 */

		tbn.debugMsg("Adding badguys...");
		for (int i = 0; i < players.length; i++) {
			if ((!info.batman.getName().equalsIgnoreCase(players[i].getName()))
					&& (!info.robin.getName().equalsIgnoreCase(
							players[i].getName())))
				info.badGuys[i] = players[i];
		}
		/*
		 * if ((info.joker == null)) { int jokerRand =
		 * rand.nextInt(info.getPlayerCount()); int index = 0; for (Player p :
		 * Bukkit.getOnlinePlayers()) { tbn.debugMsg("J: " + index + " " +
		 * p.getName() + " " + jokerRand); if ((jokerRand == index) && (p !=
		 * info.batman) && (p != info.robin)) { info.joker = p; break; }
		 * index++;
		 * 
		 * }
		 * 
		 * }
		 */

		/*
		 * if (mrFreeze == null) { int freezeRand =
		 * rand.nextInt(Bukkit.getOnlinePlayers().length); int index = 0; for
		 * (Player p : Bukkit.getOnlinePlayers()) { if
		 * (TheBatKnight.vips.contains(p.getName())) { if ((freezeRand == index)
		 * && (p != batman) && (p != robin) && (p != catwoman) && (p != joker)
		 * && (p != mrFreeze) && (p != penguin)) { mrFreeze = p; break; }
		 * index++; } } }
		 */

		if (info.joker != null) {
			tbn.debugMsg("Joker Is Not Null!");
		} else {
			tbn.debugMsg("Joker Is Null!");
		}
		/*
		 * if (catwoman != null) { catwomanName = catwoman.getName();
		 * tbn.debugMsg("CatWoman Is Not Null!"); catwoman.addPotionEffect(new
		 * PotionEffect(PotionEffectType.SPEED, 50000, 1)); } else {
		 * tbn.debugMsg("CatWoman Is Null!"); } if (posionivy != null) { ivyName
		 * = posionivy.getName(); tbn.debugMsg("Ivy Is Not Null!"); } else {
		 * tbn.debugMsg("Ivy Is Null!"); } if (mrFreeze != null) { freezeName =
		 * mrFreeze.getName(); tbn.debugMsg("Freeze Is Not Null!"); } else {
		 * tbn.debugMsg("Freeze Is Null!"); }
		 */
		tbn.debugMsg("Doing some inv stuffr...");
		info.clearAllInventories();
		ItemStack strength = new ItemStack(Material.POTION);
		Potion p = new Potion(PotionType.STRENGTH);
		p.setSplash(true);
		p.apply(strength);
		ItemStack instantHeal = new ItemStack(Material.POTION);
		Potion p2 = new Potion(PotionType.INSTANT_HEAL);
		p2.setSplash(true);
		p2.apply(instantHeal);
		ItemStack regen = new ItemStack(Material.POTION);
		Potion p3 = new Potion(PotionType.REGEN);
		p3.setSplash(true);
		p3.apply(regen);
		ItemStack posion = new ItemStack(Material.POTION);
		Potion p4 = new Potion(PotionType.POISON);
		p4.setSplash(true);
		p4.apply(posion);
		ItemStack speed = new ItemStack(Material.POTION);
		Potion p5 = new Potion(PotionType.SPEED);
		p5.setSplash(true);
		p5.apply(speed);
		ItemStack sword;
		sword = new ItemStack(Material.DIAMOND_SWORD);
		sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);

		List compassLore = new ArrayList();
		List bowLore = new ArrayList();
		List swordLore = new ArrayList();
		List featherLore = new ArrayList();

		compassLore
				.add(ChatColor.DARK_GRAY + "Use This To Track The Bad Guys!");
		bowLore.add(ChatColor.DARK_GRAY + "Take em down boyz!");
		swordLore.add(ChatColor.DARK_GRAY + "Slice and Dice!");
		featherLore.add(ChatColor.DARK_GRAY
				+ "Fly like one of your French Girls!");

		info.batman.getInventory()
				.addItem(
						new ItemStack[] {
								setName(sword, ChatColor.DARK_RED + "BatSword",
										swordLore),
								setName(strength, ChatColor.GRAY + "Steroids",
										null),
								setName(instantHeal, ChatColor.LIGHT_PURPLE
										+ "Health Serum", null),
								setName(new ItemStack(Material.COMPASS),
										ChatColor.GREEN + "Tracker",
										compassLore),
								setName(regen, ChatColor.GREEN + "Med Kit",
										null),
								setName(posion, ChatColor.DARK_GREEN
										+ "Gas Bomb", null),
								setName(speed, ChatColor.BLUE
										+ "Adrenaline Shot", null),
								setName(new ItemStack(Material.FEATHER),
										ChatColor.GRAY + "Feather o' Flight",
										featherLore) });
		info.batman.getInventory().setHelmet(
				setArmourColour(Material.LEATHER_HELMET, 0, 0, 0));
		info.batman.getInventory().setChestplate(
				setArmourColour(Material.LEATHER_CHESTPLATE, 0, 0, 0));
		info.batman.getInventory().setLeggings(
				setArmourColour(Material.LEATHER_LEGGINGS, 0, 0, 0));
		info.batman.getInventory().setBoots(
				setArmourColour(Material.LEATHER_LEGGINGS, 0, 0, 0));
		setListName(info.batman, info.batman.getDisplayName(), ChatColor.GRAY);
		info.batman.teleport(tbn.getPlayerSpawn(
				info.getActiveWorld().getName(), -1));
		info.robin.getInventory()
				.addItem(
						new ItemStack[] {
								setName(new ItemStack(Material.COMPASS),
										ChatColor.GREEN + "Tracker",
										compassLore),
								setName(sword, ChatColor.RED + "BatSword",
										swordLore),
								setName(new ItemStack(Material.FEATHER),
										ChatColor.GRAY + "Feather o' Flight",
										featherLore), strength });
		info.robin.getInventory().setHelmet(
				setArmourColour(Material.LEATHER_HELMET, 64, 193, 55));
		info.robin.getInventory().setChestplate(
				setArmourColour(Material.LEATHER_CHESTPLATE, 64, 193, 55));
		info.robin.getInventory().setLeggings(
				setArmourColour(Material.LEATHER_LEGGINGS, 64, 193, 55));
		info.robin.getInventory().setBoots(
				setArmourColour(Material.LEATHER_LEGGINGS, 64, 193, 55));
		setListName(info.robin, info.robin.getDisplayName(), ChatColor.GREEN);
		info.batman.teleport(tbn.getPlayerSpawn(
				info.getActiveWorld().getName(), -2));
		if (info.joker != null) {
			info.joker.getInventory().setHelmet(
					setArmourColour(Material.LEATHER_HELMET, 191, 0, 255));
			info.joker.getInventory().setChestplate(
					setArmourColour(Material.LEATHER_CHESTPLATE, 191, 0, 255));
			info.joker.getInventory().setLeggings(
					setArmourColour(Material.LEATHER_LEGGINGS, 191, 0, 255));

			ItemStack jokerBoots = new ItemStack(Material.IRON_BOOTS);
			jokerBoots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 20);
			List jokerBootsLore = new ArrayList();
			jokerBootsLore.add(ChatColor.DARK_GRAY
					+ "Escape In Times Of Need...");
			info.joker.getInventory().setBoots(
					setName(jokerBoots, ChatColor.GRAY + "The Iron Imperators",
							jokerBootsLore));

			ItemStack jSword = new ItemStack(Material.IRON_SWORD);
			ItemStack jPotion = new ItemStack(Material.POTION);
			Potion jP = new Potion(PotionType.POISON);
			jP.setSplash(true);
			jP.apply(jPotion);

			List jokerSwordLore = new ArrayList();
			List jokerBombLore = new ArrayList();
			List tntiabLore = new ArrayList();

			tntiabLore
					.add(ChatColor.DARK_GRAY
							+ "Trick the villains into opening these... They'll get a big suprise!");
			jokerSwordLore.add(ChatColor.DARK_GRAY
					+ "So Little Time... So Much Pain...");
			jokerBombLore.add(ChatColor.RED + "Kaboom...");

			jSword.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			info.joker.getInventory().addItem(
					new ItemStack[] { setName(jSword, ChatColor.DARK_PURPLE
							+ "The Fate Changer", jokerSwordLore) });
			info.joker.getInventory().addItem(
					new ItemStack[] { setName(new ItemStack(Material.CHEST, 5),
							ChatColor.RED + "TNT-In-A-Box", new ArrayList()) });
			int spawn = (int) (Math.random() * 20 + 1);
			info.joker.teleport(tbn.getPlayerSpawn(info.getActiveWorld()
					.getName(), spawn));
		}
		if (info.puffin != null) {
			List<String> ll = new ArrayList<String>();
			info.puffin.getInventory().addItem(
					new ItemStack[] {
							setName(Material.IRON_HOE, ChatColor.DARK_PURPLE
									+ "Umbrella", ll),
							setName(Material.EGG, ChatColor.YELLOW
									+ "Minion Spawner", ll),
							setName(Material.RAW_CHICKEN, ChatColor.GRAY
									+ "The Puffinator", ll) });
		}

		/*
		 * if (catwoman != null) { catwoman.getInventory().addItem(new
		 * ItemStack[] { setName(new ItemStack(Material.STONE_SWORD),
		 * ChatColor.GRAY + "Kitty Claws", null), setName(new
		 * ItemStack(Material.MILK_BUCKET), ChatColor.GOLD + "Catsciser", null),
		 * setName(new ItemStack(Material.getMaterial(349)), ChatColor.BLUE +
		 * "Minion Spawner", null), setName(new ItemStack(Material.FISHING_ROD),
		 * ChatColor.RED + "Diamond Stealer", null), setName(Material.COMPASS,
		 * ChatColor.GREEN + "Tracker", compassLore) }); } if (mrFreeze != null)
		 * { ItemStack icePick = setName(Material.IRON_SWORD, ChatColor.AQUA +
		 * "Ice Pick", Arrays.asList(new String[] { ChatColor.DARK_GRAY +
		 * "Ice I" })); int snowballs = new Random().nextInt(30) + 1; while
		 * (snowballs < 8) snowballs = new Random().nextInt(30) + 1;
		 * mrFreeze.getInventory().addItem(new ItemStack[] { icePick,
		 * setName(new ItemStack(Material.SNOW_BALL, snowballs),
		 * ChatColor.DARK_AQUA + "Freezeball", Arrays.asList(new String[] {
		 * ChatColor.DARK_GRAY + "Stun Your Enemies!" })) }); }
		 * 
		 * if (posionivy != null) { posionivy.getInventory().addItem(new
		 * ItemStack[] { setName(new ItemStack(Material.VINE, 64),
		 * ChatColor.GREEN + "Vines") }); }
		 * 
		 * if (penguin != null) { penguin.getInventory().addItem(new ItemStack[]
		 * { setName(Material.IRON_HOE, ChatColor.DARK_PURPLE + "Umbrella"),
		 * setName(Material.EGG, ChatColor.YELLOW + "Minion Spawner"),
		 * setName(Material.RAW_CHICKEN, ChatColor.GRAY + "The Puffinator") });
		 * }
		 */

		info.updateAllInventories();

		int index = 0;
		int randomIndex = rand.nextInt(info.badGuys.length);
		for (Player badGuy : info.badGuys)
			if ((badGuy != null) && (badGuy.getName() != null)) {
				badGuy.getInventory().addItem(
						new ItemStack[] {
								new ItemStack(Material.STONE_SWORD),
								new ItemStack(Material.BOW),
								new ItemStack(Material.ARROW, new Random()
										.nextInt(64)) });
				setListName(badGuy, badGuy.getDisplayName(), ChatColor.YELLOW);
				/*
				 * if ((index == randomIndex) && (badGuy != null)) { ItemStack i
				 * = setName(new ItemStack(Material.STICK), ChatColor.YELLOW +
				 * "Knockback Stick", null);
				 * i.addUnsafeEnchantment(Enchantment.KNOCKBACK, 4);
				 * i.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 2);
				 * badGuy.getInventory().addItem(new ItemStack[] { i }); } End
				 * of comment
				 */
				index++;
				badGuy.updateInventory();
				if (info.spawnwitch >= 20) {
					info.spawnwitch = 1;
				}
				badGuy.teleport(tbn.getPlayerSpawn(info.getActiveWorld()
						.getName(), info.spawnwitch));
				info.spawnwitch++;
			}
	}

	private ItemStack setName(Material ma, String name, List<String> lore) {
		ItemStack is = new ItemStack(ma);
		ItemMeta m = is.getItemMeta();
		if (name != null)
			m.setDisplayName(name);
		if (lore != null)
			m.setLore(lore);
		is.setItemMeta(m);
		return is;
	}

	@SuppressWarnings({ "unused", "static-access" })
	private void sendHowToPlayInfo(Player robin, Player batman,
			Player[] badGuys, Player joker) {
		String badGuyList = ChatColor.GRAY + "Bad Guys: " + ChatColor.DARK_GRAY;
		robin.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD
				+ "TheBatKnight" + ChatColor.WHITE + "------");
		sendMessage(robin, "You Are " + ChatColor.DARK_RED + "BIRDBOY!");

		batman.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD
				+ "TheBatKnight" + ChatColor.WHITE + "------");
		sendMessage(batman, "You Are " + ChatColor.DARK_RED + "BATKNGIHT!");

		/*
		 * if (catwoman != null) { catwoman.sendMessage(ChatColor.WHITE +
		 * "------" + ChatColor.GOLD + "TheBatKnight" + ChatColor.WHITE +
		 * "------"); sendMessage(catwoman, "You Are " + ChatColor.DARK_RED +
		 * "CATWOMAN!"); } if (mrFreeze != null) {
		 * mrFreeze.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD +
		 * "TheBatKnight" + ChatColor.WHITE + "------"); sendMessage(mrFreeze,
		 * "You Are " + ChatColor.AQUA + "DR.COLD!"); } if (posionivy != null) {
		 * posionivy.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD +
		 * "TheBatKnight" + ChatColor.WHITE + "------"); sendMessage(posionivy,
		 * "You Are " + ChatColor.AQUA + "DR.COLD!"); } if (penguin != null) {
		 * penguin.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD +
		 * "TheBatKnight" + ChatColor.WHITE + "------"); sendMessage(penguin,
		 * "You Are " + ChatColor.WHITE + "THE PUFFIN!"); }
		 */

		int i = 1;
		for (Player player : badGuys) {
			if ((player != null) && (player != joker)) {
				player.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD
						+ "TheBatKnight" + ChatColor.WHITE + "------");
				sendMessage(player, "You Are " + ChatColor.DARK_RED
						+ "A Bad Guy!");

				ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
				BookMeta bm = (BookMeta) book.getItemMeta();
				bm.setTitle(ChatColor.DARK_RED + "How To Play: "
						+ ChatColor.DARK_GRAY + "BadGuy");
				bm.setPages(Arrays
						.asList(new String[] { ChatColor.DARK_GREEN
								+ "--"
								+ ChatColor.GOLD
								+ "TheBatKnight"
								+ ChatColor.DARK_GREEN
								+ "--\n "
								+ ChatColor.GRAY
								+ "As a BadGuy you must attempt to steal all the diamonds"
								+ " without getting killed! Never trust anyone...\n \n" /*
																						 * +
																						 * ChatColor
																						 * .
																						 * RED
																						 * +
																						 * "Use /Shop to shop for items!"
																						 */}));
				book.setItemMeta(bm);
				player.getInventory().addItem(new ItemStack[] { book });

				if (i == badGuys.length)
					badGuyList = badGuyList + player.getName() + ".";
				else {
					badGuyList = badGuyList + player.getName() + ChatColor.GRAY
							+ ", " + ChatColor.DARK_GRAY;
				}
				i++;
			}

			if ((player == joker) && (joker != null)) {
				joker.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD
						+ "TheBatKnight" + ChatColor.WHITE + "------");
				sendMessage(joker, "You Are " + ChatColor.DARK_RED
						+ "The Jester!");
			}
		}

		for (Player player : info.getPlayers()) {
			sendMessage(player, "");
			sendMessage(player, ChatColor.YELLOW + "Stuck? Use /unstuck!");
		}

		giveBooks(info.batman, info.robin, info.joker, info.badGuys);
	}

	private void giveBooks(Player batman, Player robin, Player joker,
			Player[] badGuys) {
		try {
			ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
			BookMeta bm = (BookMeta) book.getItemMeta();
			bm.setAuthor(ChatColor.GOLD + "TheBatKnight");
			bm.setTitle(ChatColor.DARK_RED + "How To Play: "
					+ ChatColor.DARK_GRAY + "BatKnight");
			bm.setPages(Arrays.asList(new String[] { ChatColor.DARK_GREEN
					+ "--" + ChatColor.GOLD + "TheBatKnight"
					+ ChatColor.DARK_GREEN + "--\n " + ChatColor.GRAY
					+ "Kill all the badguys with the aid of" + ChatColor.GREEN
					+ " BirdBoy" + ChatColor.GRAY
					+ " without being killed first!" }));
			book.setItemMeta(bm);
			batman.getInventory().addItem(new ItemStack[] { book });
			bm.setTitle(ChatColor.DARK_RED + "How To Play: " + ChatColor.GREEN
					+ "BirdBoy");
			bm.setPages(Arrays.asList(new String[] { ChatColor.DARK_GREEN
					+ "--" + ChatColor.GOLD + "TheBatKnight"
					+ ChatColor.DARK_GREEN + "--\n " + ChatColor.GRAY
					+ "Kill all the badguys with the aid of"
					+ ChatColor.DARK_GRAY + " BatKnight" + ChatColor.GRAY
					+ " without being killed first!" }));
			book.setItemMeta(bm);
			robin.getInventory().addItem(new ItemStack[] { book });
			bm.setTitle(ChatColor.DARK_RED + "How To Play: "
					+ ChatColor.LIGHT_PURPLE + "KittyKat");
			bm.setPages(Arrays.asList(new String[] { ChatColor.DARK_GREEN
					+ "--" + ChatColor.GOLD + "TheBatKnight"
					+ ChatColor.DARK_GREEN + "--\n " + ChatColor.GRAY + "As "
					+ ChatColor.LIGHT_PURPLE + "KittyKat" + ChatColor.GRAY
					+ " you go solo. You can team up with the "
					+ ChatColor.DARK_GRAY + "BadGuys" + ChatColor.GRAY
					+ " or roam the streets with the heroes."
					+ " Whatever you choose, always watch your back..." }));
			book.setItemMeta(bm);
			/*
			 * catwoman.getInventory().addItem(new ItemStack[] { book });
			 * bm.setTitle(ChatColor.DARK_RED + "How To Play: " +
			 * ChatColor.DARK_PURPLE + "Jester"); bm.setPages( Arrays.asList(new
			 * String[] { ChatColor.DARK_GREEN + "--" + ChatColor.GOLD +
			 * "TheBatKnight" + ChatColor.DARK_GREEN + "--\n " + ChatColor.GRAY
			 * + "As " + ChatColor.DARK_PURPLE + "The Jester" + ChatColor.GRAY +
			 * ", you should lead the " + ChatColor.DARK_GRAY + "BadGuys" +
			 * ChatColor.GRAY +
			 * " to victory. Use the Fate Changer and your Iron Imperators " +
			 * "to save you from tricky situations..." }));
			 * book.setItemMeta(bm); joker.getInventory().addItem(new
			 * ItemStack[] { book }); bm.setTitle(ChatColor.DARK_RED +
			 * "How To Play: " + ChatColor.DARK_GREEN + "Toxic Ivy");
			 * bm.setPages(Arrays.asList(new String[] { ChatColor.DARK_GREEN +
			 * "--" + ChatColor.GOLD + "TheBatKnight" + ChatColor.DARK_GREEN +
			 * "--\n " + ChatColor.GRAY + "As " + ChatColor.DARK_GREEN +
			 * "Toxic Ivy" + ChatColor.GRAY +
			 * ", you can use your surroundings to your advantage" +
			 * ". Climb buildings and posion playe" +
			 * "rs with your toxic traits..." })); book.setItemMeta(bm);
			 * posionivy.getInventory().addItem(new ItemStack[] { book });
			 * 
			 * bm.setTitle(ChatColor.DARK_RED + "How To Play: " + ChatColor.AQUA
			 * + "Dr. Freeze"); bm.setPages(Arrays.asList(new String[] {
			 * ChatColor.DARK_GREEN + "--" + ChatColor.GOLD + "TheBatKnight" +
			 * ChatColor.DARK_GREEN + "--\n " + ChatColor.GRAY + "As " +
			 * ChatColor.AQUA + "Dr. Freeze" + ChatColor.GRAY +
			 * ", you can use the element of " +
			 * "distraction to your advantage. " +
			 * "Freeze someone on the spot and" +
			 * " slaughter them before they can " +
			 * "defrost to elimenate your opponents!" })); book.setItemMeta(bm);
			 * mrFreeze.getInventory().addItem(new ItemStack[] { book });
			 * bm.setTitle(ChatColor.DARK_RED + "How To Play: " +
			 * ChatColor.WHITE + "The Puffin"); bm.setPages( Arrays.asList(new
			 * String[] { ChatColor.DARK_GREEN + "--" + ChatColor.GOLD +
			 * "TheBatKnight" + ChatColor.DARK_GREEN + "--\n " + ChatColor.GRAY
			 * + "As " + ChatColor.WHITE + "The Puffin" + ChatColor.GRAY +
			 * ", You are the The Puffin! Don't forget you're a villain! " +
			 * "Try to steal as many diamonds as you can before time runs out, and remember: Don't trust just anyone! "
			 * +
			 * "Turn into a chicken to decieve your enemies, and shoot powerful poison at anyone who stands before you!"
			 * })); book.setItemMeta(bm); penguin.getInventory().addItem(new
			 * ItemStack[] { book });
			 */
		} catch (Exception localException) {
		}
	}

	private void sendMessage(Player player, String message) {
		player.sendMessage(ChatColor.GOLD + message);
	}

	public void startCompassEngine() {
		tbn.getServer().getScheduler()
				.scheduleSyncRepeatingTask(tbn, new Runnable() {
					@SuppressWarnings({ "static-access", "rawtypes" })
					public void run() {
						if (info.getState() == ServerState.In_Game)
							try {
								if (info.batman != null) {
									List entities = info.batman
											.getNearbyEntities(500.0D, 500.0D,
													500.0D);
									for (Object e : (entities)) {
										if (e instanceof Player) {
											Player p = (Player) e;
											if ((p != info.robin)) {
												if (!info.isSpect(p)) {
													info.batman.setCompassTarget(p
															.getLocation());
												}
											}
										}
									}
								}
								if (info.robin != null) {
									List entities = info.robin
											.getNearbyEntities(500.0D, 500.0D,
													500.0D);
									for (Object e : entities)
										if ((e instanceof Player)) {
											Player p = (Player) e;
											if ((p != info.batman)) {
												if (!info.isSpect(p))
													info.robin.setCompassTarget(p
															.getLocation());
											}
										}
								}
							} catch (Exception e) {
								System.out.println("ERR_" + e.getMessage());
							}
						else
							try {
								tbn.debugMsg("I had to do it...");
								if (info.batman != null)
									info.batman.setCompassTarget(new Location(
											info.batman.getWorld(), info.batman
													.getLocation().getX(),
											info.batman.getLocation().getY(),
											info.batman.getLocation().getZ()));
								if (info.robin != null)
									info.robin.setCompassTarget(new Location(
											info.robin.getWorld(), info.robin
													.getLocation().getX(),
											info.robin.getLocation().getY(),
											info.robin.getLocation().getZ()));
							} catch (Exception localException1) {
							}
					}
				}, 0L, 20L);
	}

	@SuppressWarnings("static-access")
	public void setListName(Player player, String name, ChatColor C) {
		/*
		 * String s = name; String ss = ""; if (name.toCharArray().length >= 14)
		 * { s = C + s; for (int i = 0; i <= 13; i++) s = C + s +
		 * name.toCharArray()[i]; }
		 */
		tbn.debugMsg("The listName has been updated!");
		player.setPlayerListName(C + name);
	}

	public ItemStack setArmourColour(Material armourMat, int r, int g, int b) {
		ItemStack armour = new ItemStack(armourMat);
		LeatherArmorMeta lam = (LeatherArmorMeta) armour.getItemMeta();
		lam.setColor(Color.fromRGB(r, g, b));
		armour.setItemMeta(lam);
		return armour;
	}
}