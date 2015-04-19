package com.zafcoding.zackscott.tbn.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.zafcoding.zackscott.tbn.Info;
import com.zafcoding.zackscott.tbn.Info.ServerState;
import com.zafcoding.zackscott.tbn.PlayerProfile;
import com.zafcoding.zackscott.tbn.PlayerProfile.PlayType;
import com.zafcoding.zackscott.tbn.TBN;

public class Game {

	static TBN tbn = TBN.tbn;
	static Info info = TBN.info;
	static Locations loc = TBN.loc;
	static PlayerProfile mostdi1a = null;
	public static int i = 0;
	int oldrand = 2;

	public static void clean() {
		mostdi1a = null;
		i = 0;
	}

	public void start() {
		info.setState(ServerState.In_Game);
		info.setGameTime(tbn.getConfig().getInt("MatchLengh") * 60);
		setHeroesAndBadGuys(tbn);
		Locations.populateChests(Info.superChest);
		Info.herofreeze = true;
		info.getActiveWorld().setTime(14000);
		for (Player pl : info.getPlayers()) {
			pl.setCanPickupItems(true);
			if (!TBN.jump) {
				pl.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,
						100000000, -10));
			}
			pl.sendMessage(ChatColor.AQUA + "The game has started!");
			if (Info.batman != null) {
				pl.sendMessage(ChatColor.GRAY + "Your BatNight is "
						+ Info.batman.getName());
			}
			if (Info.robin != null) {
				pl.sendMessage(ChatColor.DARK_GREEN + "Your BirdBoy is "
						+ Info.robin.getName());
			}
			if (Info.joker != null) {
				pl.sendMessage(ChatColor.DARK_PURPLE + "Your Jester is "
						+ Info.joker.getName());
			}
			if (Info.puffin != null) {
				pl.sendMessage(ChatColor.AQUA + "Your Puffin is "
						+ Info.puffin.getName());
			}
			if (Info.catwomen != null) {
				pl.sendMessage(ChatColor.LIGHT_PURPLE + "Your KittyKat is "
						+ Info.catwomen.getName());
			}
		}
		dosomeDiamondLvl();
		flymanager();
		startCompassEngine();
		startGraceStop();
		Info.poo = true;
	}

	int time = 10;

	private void startGraceStop() {
		info.pvp = false;
		info.broadCast(ChatColor.RED
				+ "You have been given a grace period for 10 seconds!");
		Bukkit.getServer().getScheduler()
				.scheduleSyncRepeatingTask(tbn, new Runnable() {

					@Override
					public void run() {
						if (time <= 10 && time > 1) {
							time--;
						}
						if (time <= 1) {
							info.pvp = true;
							info.broadCast(ChatColor.GREEN + ""
									+ ChatColor.BOLD
									+ "You may kill each other now");
						}
					}
				}, 20, 20);

	}

	public static void endGame(final int bo) {
		info.setState(ServerState.Post_Game);
		PlayerProfile mostdia = null;
		if (bo == 1) {
			int total = 0;
			for (Player pa : info.getPlayers()) {
				PlayerProfile pq = info.getPP(pa);
				pa.teleport(pa.getWorld().getSpawnLocation());
				pa.getInventory().clear();
				pa.setGameMode(GameMode.ADVENTURE);
				if (pq.getType() == PlayType.BatNight) {
					pa.setDisplayName(ChatColor.GRAY + pa.getName());
				}
				if (pq.getType() == PlayType.BirdBoy) {
					pa.setDisplayName(ChatColor.GREEN + pa.getName());
				}
				if (pq.getType() == PlayType.Joker) {
					pa.setDisplayName(ChatColor.DARK_PURPLE + pa.getName());
				}
				if (pq.getType() == PlayType.KittyKat) {
					pa.setDisplayName(ChatColor.LIGHT_PURPLE + pa.getName());
				}
				if (pq.getType() == PlayType.Puffin) {
					pa.setDisplayName(ChatColor.AQUA + pa.getName());
				}
				if ((mostdia == null || pq.getDiamonds() > mostdia
						.getDiamonds())
						&& !pq.isDead()
						&& !(pq.getType() == PlayType.BatNight || pq.getType() == PlayType.BirdBoy)) {
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
					+ ChatColor.DARK_AQUA + mostdia.getPlayer().getName()
					+ ChatColor.YELLOW + " (" + ChatColor.DARK_AQUA
					+ mostdia.getDiamonds() + ChatColor.YELLOW + ")");
			info.broadCast(ChatColor.YELLOW
					+ "The bad guys collected a total of " + ChatColor.AQUA
					+ total + "" + ChatColor.YELLOW + " diamonds!");
			shootFireworks(mostdia.getPlayer(), 30);
		}
		if (bo == 0) {
			PlayerProfile mostkill = null;
			int totalkill = 0;
			for (Player pa : info.getPlayers()) {
				PlayerProfile pq = info.getPP(pa);
				pa.teleport(pa.getWorld().getSpawnLocation());
				pa.getInventory().clear();
				pa.setGameMode(GameMode.ADVENTURE);
				if (pq.getType() == PlayType.BatNight) {
					pa.setDisplayName(ChatColor.GRAY + pa.getName());
				}
				if (pq.getType() == PlayType.BirdBoy) {
					pa.setDisplayName(ChatColor.GREEN + pa.getName());
				}
				if (pq.getType() == PlayType.Joker) {
					pa.setDisplayName(ChatColor.DARK_PURPLE + pa.getName());
				}
				if (pq.getType() == PlayType.KittyKat) {
					pa.setDisplayName(ChatColor.LIGHT_PURPLE + pa.getName());
				}
				if (pq.getType() == PlayType.Puffin) {
					pa.setDisplayName(ChatColor.AQUA + pa.getName());
				}
				if (pq.getType() == PlayType.BatNight
						|| pq.getType() == PlayType.BirdBoy
						|| pq.getType() == PlayType.KittyKat) {
					if (mostkill == null || pq.getKills() > mostkill.getKills()) {
						mostkill = pq;
					}
					totalkill = totalkill + pq.getKills();
				}
				if (mostkill == null) {
					mostkill = info.getPP(pa);
				}
			}
			try {
				info.setState(ServerState.Post_Game);
				info.broadCast(ChatColor.WHITE + "----------" + ChatColor.GOLD
						+ "TheBatKnight" + ChatColor.WHITE + "----------");
				info.broadCast(ChatColor.GREEN + "Enjoy " + ChatColor.BLUE
						+ " the last game?" + ChatColor.AQUA
						+ " Tell everyone with /gg!");
				info.broadCast(ChatColor.AQUA + "The good guys won!");
				info.broadCast(ChatColor.AQUA + "The best hero is "
						+ ChatColor.DARK_AQUA + mostkill.getPlayer().getName()
						+ ChatColor.AQUA + " (" + ChatColor.DARK_AQUA
						+ mostkill.getKills() + ChatColor.AQUA + ")");
				info.broadCast(ChatColor.AQUA + "The heros killed "
						+ ChatColor.DARK_AQUA + totalkill + "" + ChatColor.AQUA
						+ " players!");
				shootFireworks(mostkill.getPlayer(), 30);
			} catch (Exception e) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			}
		}

		/*
		 * try { tbn.sco.saveScores(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		TBN.debugMsg("Removed chests: " + removeChest());
		info.broadCast(ChatColor.RED + "" + ChatColor.BOLD
				+ "Server reseting in 10 seconds!");
		i = Bukkit.getScheduler().scheduleSyncDelayedTask(tbn, new Runnable() {

			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					p.chat("/hub");
				}
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
			}
		}, 200L);
	}

	public static int removeEnts() {
		int removed = 0;
		for (Entity ent : info.getActiveWorld().getEntities()) {
			if (!(ent instanceof Player)) {
				ent.remove();
				removed++;
			}
		}
		return removed;
	}

	public void kickAll(int bo) {
		/*
		 * ArrayList<Player> po = new ArrayList<Player>(info.getPlayers()); for
		 * (Player ppp : po) { if (bo == 1) { ppp.kickPlayer(ChatColor.GOLD + ""
		 * + ChatColor.BOLD + "TheBatKnight" + ChatColor.RESET +
		 * "\nThanks for playing! \nRejoin to play another game!\n" +
		 * ChatColor.GREEN + "Winner: " + ChatColor.DARK_AQUA +
		 * info.getWinner().getPlayer().getName() + " (" +
		 * info.getWinner().getDiamonds() + ")"); } if (bo == 0) {
		 * ppp.kickPlayer(ChatColor.GOLD + "" + ChatColor.BOLD + "TheBatKnight"
		 * + ChatColor.RESET +
		 * "\nThanks for playing! \nRejoin to play another game!\n" +
		 * ChatColor.GREEN + "Winners: " + ChatColor.DARK_AQUA + "Heros!"); }
		 * try { Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
		 * "reload"); } catch (Exception e) { tbn.debugMsg("Err_ " +
		 * e.getMessage()); Bukkit.getServer().reload(); } }
		 */

	}

	public static int removeChest() {
		int chestdone = 0;
		int totala = Info.chests.size();
		for (Object loca : Info.chests.toArray()) {
			Location loc = (Location) loca;
			if (loc.getBlock().getType() == Material.CHEST) {
				loc.getBlock().setType(Material.AIR);
				chestdone++;
				TBN.debugMsg("Removed chest " + chestdone + "/" + totala);
				Info.chests.remove(loc);
			}
		}
		clearEnts();
		return chestdone;
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

	public void dosomeDiamondLvl() {
		tbn.getServer().getScheduler()
				.scheduleSyncRepeatingTask(tbn, new Runnable() {
					public void run() {
						if (info.getState() == ServerState.In_Game)
							for (Player p : Bukkit.getOnlinePlayers()) {
								p.setLevel(getDiamonds(p));
								info.getPP(p).setDiamonds(getDiamonds(p));
							}
					}
				}, 20L, 20L);
	}

	public void flymanager() {
		tbn.getServer().getScheduler()
				.scheduleSyncRepeatingTask(tbn, new Runnable() {
					public void run() {
						for (Player pp : info.getPlayers()) {
							if (info.getState() == ServerState.In_Game)
								if (info.getState() == ServerState.In_Game) {
									if (info.isSpect(pp)) {
										pp.setAllowFlight(true);
										pp.setFlying(true);
										return;
									}
									if (pp.getItemInHand().getType() == Material.FEATHER) {
										if (Info.batman == pp.getPlayer()
												|| Info.robin == pp.getPlayer()) {
											pp.setAllowFlight(true);
											pp.setFlying(true);
											return;
										}
									}
									if (!(pp.getGameMode() == GameMode.CREATIVE)) {
										pp.setAllowFlight(false);
										pp.setFlying(false);
									}
								} else {
									if (!(pp.getGameMode() == GameMode.CREATIVE)
											&& info.getPP(pp).getType() == PlayType.Villan) {
										pp.setAllowFlight(false);
										pp.setFlying(false);
									}
								}
						}
					}
				}, 10L, 10L);
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
			"deprecation" })
	private void setHeroesAndBadGuys(TBN tbn2) {
		ArrayList<Player> pl = info.getPlayers();
		Random rand = new Random();
		int batmanIndex = rand.nextInt(pl.size());
		int robinIndex = rand.nextInt(pl.size());
		int jockerIndex = rand.nextInt(pl.size());
		int puffinIndex = rand.nextInt(pl.size());
		int catIndex = rand.nextInt(pl.size());

		while (batmanIndex == robinIndex) {
			robinIndex = rand.nextInt(pl.size());
		}
		while ((batmanIndex == jockerIndex || robinIndex == jockerIndex)) {
			jockerIndex = rand.nextInt(pl.size());
		}
		while ((batmanIndex == catIndex || robinIndex == catIndex)
				|| jockerIndex == catIndex) {
			catIndex = rand.nextInt(pl.size());
		}
		while ((batmanIndex == puffinIndex || robinIndex == puffinIndex)
				|| jockerIndex == puffinIndex || catIndex == puffinIndex) {
			puffinIndex = rand.nextInt(pl.size());
		}

		tbn.debugMsg("Setting batman...");
		if (info.batman == null) {
			info.batman = pl.get(batmanIndex);
			info.getPP(info.batman).setType(PlayType.BatNight);
		}

		tbn.debugMsg("Setting robin...");
		if (info.robin == null) {
			info.robin = pl.get(robinIndex);
			info.getPP(info.robin).setType(PlayType.BirdBoy);
		}

		tbn.debugMsg("Setting joker...");
		if (info.joker == null) {
			info.joker = pl.get(jockerIndex);
			info.getPP(info.joker).setType(PlayType.Joker);
		}
		tbn.debugMsg("Setting puffin...");
		if (info.puffin == null) {
			info.puffin = pl.get(puffinIndex);
			info.getPP(info.puffin).setType(PlayType.Puffin);
		}
		tbn.debugMsg("Setting catlady...");
		if (info.catwomen == null) {
			info.catwomen = pl.get(catIndex);
			info.getPP(info.catwomen).setType(PlayType.KittyKat);
		}

		tbn.debugMsg("Adding badguys...");
		for (Player pp : info.getPlayers()) {
			PlayerProfile ppp = info.getPP(pp);
			if (ppp.getType() != PlayType.BatNight
					&& ppp.getType() != PlayType.BirdBoy
					&& ppp.getType() != PlayType.KittyKat
					&& ppp.getType() != PlayType.Joker
					&& ppp.getType() != PlayType.Puffin) {
				info.badguys.add(pp);
			}
		}

		tbn.debugMsg("Doing some inv stuffr...");
		info.clearAllInventories();
		ItemStack strength = new ItemStack(Material.POTION);
		Potion p = new Potion(PotionType.STRENGTH);
		p.apply(strength);
		ItemStack instantHeal = new ItemStack(Material.POTION);
		Potion p2 = new Potion(PotionType.INSTANT_HEAL);
		p2.apply(instantHeal);
		ItemStack regen = new ItemStack(Material.POTION);
		Potion p3 = new Potion(PotionType.REGEN);
		p3.apply(regen);
		ItemStack posion = new ItemStack(Material.POTION);
		Potion p4 = new Potion(PotionType.POISON);
		p4.apply(posion);
		ItemStack speed = new ItemStack(Material.POTION);
		Potion p5 = new Potion(PotionType.SPEED);
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

		if (info.batman != null) {
			info.batman.getInventory()
					.addItem(
							new ItemStack[] {
									setName(sword, ChatColor.DARK_RED
											+ "BatSword", swordLore),
									setName(strength, ChatColor.GRAY
											+ "Steroids", null),
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
											ChatColor.GRAY
													+ "Feather o' Flight",
											featherLore) });
			info.batman.getInventory().setHelmet(
					setArmourColour(Material.LEATHER_HELMET, 0, 0, 0));
			info.batman.getInventory().setChestplate(
					setArmourColour(Material.LEATHER_CHESTPLATE, 0, 0, 0));
			info.batman.getInventory().setLeggings(
					setArmourColour(Material.LEATHER_LEGGINGS, 0, 0, 0));
			info.batman.getInventory().setBoots(
					setArmourColour(Material.LEATHER_BOOTS, 0, 0, 0));
			setListName(info.batman, info.batman.getDisplayName(),
					ChatColor.GRAY);
			info.batman.setDisplayName(ChatColor.GRAY + "[BatKnight]");
			info.batman.teleport(tbn.getPlayerSpawn(info.getActiveWorld()
					.getName(), -1));
		}
		if (info.robin != null) {
			info.robin.getInventory().addItem(
					new ItemStack[] {
							setName(new ItemStack(Material.COMPASS),
									ChatColor.GREEN + "Tracker", compassLore),
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
					setArmourColour(Material.LEATHER_BOOTS, 64, 193, 55));
			setListName(info.robin, info.robin.getDisplayName(),
					ChatColor.GREEN);
			info.robin.setDisplayName(ChatColor.GREEN + "[BirdBoy]");
			info.robin.teleport(tbn.getPlayerSpawn(info.getActiveWorld()
					.getName(), -2));
		}
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
			int spawn = randInt(1, 20);
			info.joker.teleport(tbn.getPlayerSpawn(info.getActiveWorld()
					.getName(), spawn));
			setListName(info.joker, info.joker.getDisplayName(),
					ChatColor.DARK_PURPLE);
			info.joker.setDisplayName(ChatColor.DARK_PURPLE + "[Jester]");
			if (info.spawnwitch >= 20) {
				info.spawnwitch = 1;
			}
			info.joker.teleport(tbn.getPlayerSpawn(info.getActiveWorld()
					.getName(), info.spawnwitch));
			info.spawnwitch++;
		}
		if (info.puffin != null) {
			List<String> ll = new ArrayList<String>();
			info.puffin.getInventory().addItem(
					new ItemStack[] {
							setName(Material.STONE_HOE, ChatColor.DARK_PURPLE
									+ "Umbrella", ll),
							setName(Material.EGG, ChatColor.YELLOW
									+ "Minion Spawner", ll),
							setName(Material.RAW_CHICKEN, ChatColor.GRAY
									+ "The Puffinator", ll) });
			if (info.spawnwitch >= 20) {
				info.spawnwitch = 1;
			}
			info.puffin.teleport(tbn.getPlayerSpawn(info.getActiveWorld()
					.getName(), info.spawnwitch));
			info.spawnwitch++;
		}
		if (info.catwomen != null) {
			info.catwomen.getInventory().addItem(
					new ItemStack[] {
							setName(new ItemStack(Material.STONE_SWORD),
									ChatColor.GRAY + "Kitty Claws", null),
							setName(new ItemStack(Material.MILK_BUCKET),
									ChatColor.GOLD + "Catsciser", null),
							setName(new ItemStack(Material.getMaterial(349)),
									ChatColor.BLUE + "Minion Spawner", null),
							setName(new ItemStack(Material.FISHING_ROD),
									ChatColor.RED + "Diamond Stealer", null),
							setName(Material.COMPASS, ChatColor.GREEN
									+ "Tracker", compassLore) });
			info.catwomen.getInventory().setHelmet(
					setArmourColour(Material.LEATHER_HELMET, 253, 152, 254));
			info.catwomen.getInventory()
					.setChestplate(
							setArmourColour(Material.LEATHER_CHESTPLATE, 253,
									152, 254));
			info.catwomen.getInventory().setLeggings(
					setArmourColour(Material.LEATHER_LEGGINGS, 253, 152, 254));
			info.catwomen.getInventory().setBoots(
					setArmourColour(Material.LEATHER_LEGGINGS, 253, 152, 254));
			info.catwomen.addPotionEffect(new PotionEffect(
					PotionEffectType.SPEED, -1, 3));
			setListName(info.catwomen, info.catwomen.getDisplayName(),
					ChatColor.LIGHT_PURPLE);
			if (info.spawnwitch >= 20) {
				info.spawnwitch = 1;
			}
			info.catwomen.teleport(tbn.getPlayerSpawn(info.getActiveWorld()
					.getName(), info.spawnwitch));
			info.spawnwitch++;
		}

		int index = 0;
		for (Player badGuy : info.badguys) {
			if ((badGuy != null) && (badGuy.getName() != null)
					&& info.getPP(badGuy).getType() == PlayType.Villan) {
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
		info.updateAllInventories();
		sendHowToPlayInfo();
	}

	public ItemStack setName(Material ma, String name, List<String> lore) {
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
	private void sendHowToPlayInfo() {
		if (info.robin != null) {
			info.robin.sendMessage(ChatColor.GOLD + "You Are "
					+ ChatColor.GREEN + "BIRDBOY!");
			info.robin.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");
			info.robin
					.sendMessage(ChatColor.GREEN
							+ "As BirdBoy, you must assit BatKnight in killing all the villians in the city!");
			info.robin.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");
		}
		if (info.batman != null) {
			info.batman.sendMessage(ChatColor.GOLD + "You Are "
					+ ChatColor.GRAY + "BATKNGIHT!");
			info.batman.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");
			info.batman
					.sendMessage(ChatColor.GREEN
							+ "As BatKnight, you, with the help of BirdBoy, must kill all the villians in the city!");
			info.batman.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");
		}

		if (info.catwomen != null) {
			info.catwomen.sendMessage(ChatColor.GOLD + "You are "
					+ ChatColor.LIGHT_PURPLE + "KITTYKAT!");
			info.catwomen.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");
			info.catwomen
					.sendMessage(ChatColor.GREEN
							+ "You are KittyKat, you can choose your side! You can either aid the heros and kill the villians, or try and outwit fellow villians to capture the most diamonds!");
			info.catwomen.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");
		}
		if (info.puffin != null) {
			info.puffin.sendMessage(ChatColor.GOLD + "You are "
					+ ChatColor.AQUA + "PUFFIN!");
			info.puffin.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");
			info.puffin
					.sendMessage(ChatColor.GREEN
							+ "As BatKnight, you, with the help of BirdBoy, must kill all the villians in the city!");
			info.puffin.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");
		}
		if (info.joker != null) {
			info.joker.sendMessage(ChatColor.GOLD + "You are "
					+ ChatColor.DARK_PURPLE + "JESTER!");
			info.joker.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");
			info.joker
					.sendMessage(ChatColor.GREEN
							+ "As Jester, you must try and get the most diamonds, and plant decoy ones to throw off your fellow villians!");
			info.joker.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");
		}

		for (Player player : info.badguys) {
			player.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD
					+ "TheBatKnight" + ChatColor.WHITE + "------");
			sendMessage(player, "You Are " + ChatColor.GRAY + "A Bad Guy!");
			info.batman.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");
			info.batman
					.sendMessage(ChatColor.GREEN
							+ "As a bad guy, you must run around the city trying to get the most diamonds! Remember, you can not get the most diamonds if you are killed by a hero or fellow villians!");
			info.batman.sendMessage(ChatColor.BOLD + "▇▇▇▇▇▇▇▇▇▇▇▇▇▇");

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
								if (info.catwomen != null) {
									List entities = info.catwomen
											.getNearbyEntities(500.0D, 500.0D,
													500.0D);
									for (Object e : entities)
										if ((e instanceof Player)) {
											Player p = (Player) e;
											if ((p != info.catwomen)) {
												if (!info.isSpect(p))
													info.catwomen.setCompassTarget(p
															.getLocation());
											}
										}
								}
							} catch (Exception e) {
								System.out.println("ERR_" + e.getMessage());
							}
						else
							try {
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
	public static void setListName(Player player, String name, ChatColor C) {
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

	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	// Unloading maps, to rollback maps. Will delete all player builds until
	// last server save
	public static void unloadMap(String mapname) {
		if (Bukkit.getServer().unloadWorld(
				Bukkit.getServer().getWorld(mapname), false)) {
			System.out.println("Successfully unloaded " + mapname);
		} else {
			System.out.println("COULD NOT UNLOAD " + mapname);
		}
	}

	// Loading maps (MUST BE CALLED AFTER UNLOAD MAPS TO FINISH THE ROLLBACK
	// PROCESS)
	public static void loadMap(String mapname) {
		Bukkit.getServer().createWorld(new WorldCreator(mapname));
	}

	// Maprollback method, because were too lazy to type 2 lines
	public static void rollback(String mapname) {
		unloadMap(mapname);
		loadMap(mapname);
	}

	public void startHintsAndTipsLoop() {
		tbn.getServer().getScheduler()
				.scheduleSyncRepeatingTask(tbn, new Runnable() {
					String intro = ChatColor.YELLOW + "TheBatKnight"
							+ ChatColor.WHITE + "> ";

					public void run() {
						int rand = new Random().nextInt(13);
						while (rand == oldrand) {
							rand = new Random().nextInt(13);
						}
						switch (rand) {
						case 0:
						case 2:
						default:
							Bukkit.broadcastMessage(intro
									+ ChatColor.GOLD
									+ "BatKnight's name shows up as "
									+ ChatColor.DARK_GRAY
									+ "Grey"
									+ ChatColor.GOLD
									+ " in the list of players names. BirdBoy is "
									+ ChatColor.GREEN + "Green"
									+ ChatColor.GOLD + " and Jester is"
									+ ChatColor.DARK_PURPLE + " Purple"
									+ ChatColor.GOLD + ".");
							break;
						case 1:
							Bukkit.broadcastMessage(intro + ChatColor.GOLD
									+ "When you die, you can only talk to "
									+ ChatColor.RED + "dead " + ChatColor.GOLD
									+ "players!");
							break;
						case 3:
							Bukkit.broadcastMessage(intro
									+ ChatColor.GOLD
									+ "Watch out for trap chests placed by the "
									+ ChatColor.DARK_PURPLE + "Jester"
									+ ChatColor.GOLD + "!");
							break;
						case 4:
							Bukkit.broadcastMessage(intro
									+ ChatColor.GOLD
									+ "The villian left alive at the end with the most "
									+ ChatColor.AQUA + "diamonds"
									+ ChatColor.GOLD
									+ " will win. How many have you gotten?");
							break;
						case 5:
							Bukkit.broadcastMessage(intro + ChatColor.GOLD
									+ "When " + ChatColor.WHITE + "Pro"
									+ ChatColor.GOLD
									+ " players die, they explode into "
									+ ChatColor.LIGHT_PURPLE + "glitter!");
							break;
						case 6:
							Bukkit.broadcastMessage(intro + ChatColor.GOLD
									+ "Seen someone breaking the "
									+ ChatColor.GREEN + "rules"
									+ ChatColor.GOLD + "? Report them at "
									+ ChatColor.RED
									+ "www.scottlandstudios.com");
							break;
						case 7:
							Bukkit.broadcastMessage(intro
									+ ChatColor.GOLD
									+ ""
									+ ChatColor.LIGHT_PURPLE
									+ " KittyKat "
									+ ChatColor.GOLD
									+ "is the most cat-loving villian in the city!");
							break;
						case 8:
							Bukkit.broadcastMessage(intro + ChatColor.GOLD
									+ "Hold your " + ChatColor.WHITE
									+ "Feather " + ChatColor.GOLD + "as "
									+ ChatColor.DARK_GRAY + "BatKnight"
									+ ChatColor.GOLD + " and "
									+ ChatColor.GREEN + "BirdBoy"
									+ ChatColor.GOLD + " to fly!");
							break;
						case 10:
							Bukkit.broadcastMessage(intro + ChatColor.GOLD
									+ "To get the lastest development "
									+ ChatColor.GREEN + "news" + ChatColor.GOLD
									+ ", " + ChatColor.LIGHT_PURPLE + "hints,"
									+ ChatColor.GOLD + " and "
									+ ChatColor.YELLOW + "more"
									+ ChatColor.GOLD
									+ ", follow the Developer on "
									+ ChatColor.AQUA + "Twitter "
									+ ChatColor.RED + "@Legostarwarszac");
							break;
						}
					}
				}, 1200L, 1200L);
	}

	private static void shootFireworks(final Player entity, final int times) {
		new Thread(new Runnable() {
			public void run() {
				for (int i = 0; i < times; i++) {
					Firework fw = (Firework) entity.getWorld().spawn(
							entity.getLocation(), Firework.class);
					FireworkMeta fm = fw.getFireworkMeta();
					Random x = new Random();
					int xf = x.nextInt(4) + 1;
					FireworkEffect.Type type = FireworkEffect.Type.BALL;
					if (xf == 1)
						type = FireworkEffect.Type.BALL;
					if (xf == 2)
						type = FireworkEffect.Type.BALL_LARGE;
					if (xf == 3)
						type = FireworkEffect.Type.BURST;
					if (xf == 4)
						type = FireworkEffect.Type.CREEPER;
					if (xf == 5)
						type = FireworkEffect.Type.STAR;
					int xf1i = x.nextInt(17) + 1;
					int xf2i = x.nextInt(17) + 1;
					Color cc = getColor(xf1i);
					Color cd = getColor(xf2i);
					FireworkEffect effect = FireworkEffect.builder()
							.flicker(x.nextBoolean()).withColor(cc)
							.withFade(cd).with(type).trail(x.nextBoolean())
							.build();
					fm.addEffect(effect);
					int pw = x.nextInt(2) + 1;
					fm.setPower(pw);
					fw.setFireworkMeta(fm);
					try {
						Thread.sleep(500 + new Random().nextInt(700));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	public static Color getColor(int x) {
		if (x == 1)
			return Color.AQUA;
		if (x == 2)
			return Color.BLACK;
		if (x == 3)
			return Color.BLUE;
		if (x == 4)
			return Color.FUCHSIA;
		if (x == 5)
			return Color.GRAY;
		if (x == 6)
			return Color.GREEN;
		if (x == 7)
			return Color.LIME;
		if (x == 8)
			return Color.MAROON;
		if (x == 9)
			return Color.NAVY;
		if (x == 10)
			return Color.OLIVE;
		if (x == 11)
			return Color.ORANGE;
		if (x == 12)
			return Color.PURPLE;
		if (x == 13)
			return Color.RED;
		if (x == 14)
			return Color.SILVER;
		if (x == 15)
			return Color.TEAL;
		if (x == 16)
			return Color.TEAL;
		if (x == 17)
			return Color.WHITE;
		if (x == 18)
			return Color.YELLOW;
		return null;
	}
}