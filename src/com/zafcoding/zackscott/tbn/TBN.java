package com.zafcoding.zackscott.tbn;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import com.zafcoding.zackscott.tbn.Info.ServerState;
import com.zafcoding.zackscott.tbn.game.Game;
import com.zafcoding.zackscott.tbn.game.GameListiner;
import com.zafcoding.zackscott.tbn.game.GameTime;
import com.zafcoding.zackscott.tbn.game.Locations;
import com.zafcoding.zackscott.tbn.lobby.LobbyListiners;
import com.zafcoding.zackscott.tbn.lobby.LobbyTime;

public class TBN extends JavaPlugin {

	public static TBN tbn;
	public static Info info;
	public static LobbyTime lt;
	public static GameTime gt;
	public static Game game;
	public static Locations loc;
	static boolean debug = true;
	double version = 1.6;
	public String pre = ChatColor.GOLD + "[TBN]";
	public static Inventory inv;
	public boolean debugMode = true;
	ArrayList<Player> unstuckers = new ArrayList<Player>();

	@Override
	public void onEnable() {
		System.out.print("[TBN] The Bat Night v." + version + " enabling...");
		tbn = this;
		info = new Info();
		lt = new LobbyTime();
		gt = new GameTime();
		game = new Game();
		loc = new Locations();
		loadConfiguration();
		getServer().getPluginManager().registerEvents(new GameListiner(), this);
		getServer().getPluginManager().registerEvents(new LobbyListiners(),
				this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Thread(), 20,
				20);
		// String[] ss = getConfig().getString("Worlds").split(",");
		// int randy = info.getRandom(1, ss.length);
		// info.setActiveWorld(Bukkit.getWorld((String) ss[randy - 1]));
		debug = getConfig().getBoolean("debug");
		System.out.print("[TBN] The Bat Night v." + version + " enabled!");
	}

	@Override
	public void onDisable() {
		System.out.print("[TBN] The Bat Night v." + version + " disabling...");
		System.out.print("[TBN] The Bat Night v." + version + " disabled!");
	}

	private void loadConfiguration() {
		if (this.getConfig().get("Config.exsits") == null) {
			String[] list = { "world", "DarkTemple" };
			this.getConfig().set("Config.exsits", true);
			this.getConfig().set("MinPlayers", 8);
			this.getConfig().set("MaxPlayers", 25);
			this.getConfig().set("MinDiamonds", 10);
			this.getConfig().set("MaxDiamonds", 100);
			this.getConfig().set("MatchLengh", 10);
			this.getConfig().set("GracePeriod", 10);
			this.getConfig().set("Chests.amount", 3);
			this.getConfig().set("Chests.SamCity.1", "-1194,18,-550");
			this.getConfig().set("Chests.SamCity.2", "-1186,18,-549");
			this.getConfig().set("Chests.SamCity.3", "-1188,18,-553");
			this.getConfig().set("debug", true);
			this.getConfig().options().copyDefaults(true);
			this.saveConfig();
			this.reloadConfig();
		} else {
			this.reloadConfig();
		}
	}

	public static void debugMsg(String message) {
		if (debug) {
			System.out.println("[DEBUG] " + message);
		}
	}

	public static Game getG() {
		debugMsg("Returned " + game);
		return game;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (label.equalsIgnoreCase("unstuck")
					&& info.getState() == ServerState.In_Game) {
				if (!unstuckers.contains(p)) {
					Vector v = new Vector(0, 2, 0);
					p.setVelocity(v);
					unstuckers.add(p);
				} else {
					p.sendMessage(ChatColor.RED
							+ "I'm gonna put ya on da list boy!");
				}
			}
			if (label.equalsIgnoreCase("spawn")
					&& !(info.getState() == ServerState.In_Game)) {
				p.teleport(info.getActiveWorld().getSpawnLocation());
				p.sendMessage(ChatColor.GREEN + "Teleported to spawn!");
				return true;
			}
			if (label.equalsIgnoreCase("tbn")) {
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("world")) {
						p.sendMessage("[" + ChatColor.GOLD + "TBN"
								+ ChatColor.WHITE + "]" + ChatColor.GREEN
								+ " The current active world is "
								+ info.getActiveWorld().getName());
						return true;
					}
					if (args[0].equalsIgnoreCase("debug")) {
						if (!p.isOp()) {
							// Get it? Yeah me neither...
							p.sendMessage("You're killing me boy!");
							return true;
						}
						if (debugMode) {
							debugMode = false;
							debug = false;
							p.sendMessage(pre + " Debug mode is now "
									+ ChatColor.RED + " disabled"
									+ ChatColor.GOLD + "!");
							return true;
						}
						debugMode = true;
						debug = true;
						p.sendMessage(pre + " Debug mode is now "
								+ ChatColor.GREEN + " enabled" + ChatColor.GOLD
								+ "!");
						return true;
					}

					if (args[0].equalsIgnoreCase("chest")) {
						if (p.isOp()) {
							int i = loc.populateChests(false);
							p.sendMessage(pre + ChatColor.GRAY + "Spawned " + i
									+ " chests!");
							return true;
						}
					}if (args[0].equalsIgnoreCase("force")) {
						if (p.isOp()) {
							p.sendMessage(ChatColor.GRAY + "Starting the game...");
							game.start();
							return true;
						}
					}if (args[0].equalsIgnoreCase("finish")) {
						if (p.isOp()) {
							p.sendMessage(ChatColor.GRAY + "Ending the game...");
							game.endGame(0);
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("removechest")) {
						if (p.isOp()) {
							int i = game.removeChest();
							p.sendMessage(pre + ChatColor.GRAY + "Removed " + i
									+ " chests!");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("batknight")) {
						if (p.isOp()) {
							info.batman = p;
							p.sendMessage(pre + " You are the now the "
									+ ChatColor.GRAY + "BatKnight"
									+ ChatColor.GOLD + "!");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("birdboy")) {
						if (p.isOp()) {
							info.robin = p;
							p.sendMessage(pre + " You are the now "
									+ ChatColor.GREEN + "BirdBoy"
									+ ChatColor.GOLD + "!");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("joker")) {
						if (p.isOp()) {
							info.joker = p;
							p.sendMessage(pre + " You are the now "
									+ ChatColor.LIGHT_PURPLE + "Jester"
									+ ChatColor.GOLD + "!");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("arm")) {
						if (p.isOp()) {
							if (debugMode) {
								if (info.batman != null && info.batman == p) {
									ItemStack strength = new ItemStack(
											Material.POTION);
									Potion pp = new Potion(PotionType.STRENGTH);
									pp.setSplash(true);
									pp.apply(strength);
									ItemStack instantHeal = new ItemStack(
											Material.POTION);
									Potion p2 = new Potion(
											PotionType.INSTANT_HEAL);
									p2.setSplash(true);
									p2.apply(instantHeal);
									ItemStack regen = new ItemStack(
											Material.POTION);
									Potion p3 = new Potion(PotionType.REGEN);
									p3.setSplash(true);
									p3.apply(regen);
									ItemStack posion = new ItemStack(
											Material.POTION);
									Potion p4 = new Potion(PotionType.POISON);
									p4.setSplash(true);
									p4.apply(posion);
									ItemStack speed = new ItemStack(
											Material.POTION);
									Potion p5 = new Potion(PotionType.SPEED);
									p5.setSplash(true);
									p5.apply(speed);
									ItemStack sword;
									sword = new ItemStack(
											Material.DIAMOND_SWORD);
									sword.addEnchantment(
											Enchantment.DAMAGE_ALL, 1);

									List compassLore = new ArrayList();
									List bowLore = new ArrayList();
									List swordLore = new ArrayList();
									List featherLore = new ArrayList();

									compassLore
											.add(ChatColor.DARK_GRAY
													+ "Use This To Track The Bad Guys!");
									bowLore.add(ChatColor.DARK_GRAY
											+ "Take em down boyz!");
									swordLore.add(ChatColor.DARK_GRAY
											+ "Slice and Dice!");
									featherLore
											.add(ChatColor.DARK_GRAY
													+ "Fly like one of your French Girls!");

									info.batman
											.getInventory()
											.addItem(
													new ItemStack[] {
															game.setName(
																	sword,
																	ChatColor.DARK_RED
																			+ "BatSword",
																	swordLore),
															game.setName(
																	strength,
																	ChatColor.GRAY
																			+ "Steroids",
																	null),
															game.setName(
																	instantHeal,
																	ChatColor.LIGHT_PURPLE
																			+ "Health Serum",
																	null),
															game.setName(
																	new ItemStack(
																			Material.COMPASS),
																	ChatColor.GREEN
																			+ "Tracker",
																	compassLore),
															game.setName(
																	regen,
																	ChatColor.GREEN
																			+ "Med Kit",
																	null),
															game.setName(
																	posion,
																	ChatColor.DARK_GREEN
																			+ "Gas Bomb",
																	null),
															game.setName(
																	speed,
																	ChatColor.BLUE
																			+ "Adrenaline Shot",
																	null),
															game.setName(
																	new ItemStack(
																			Material.FEATHER),
																	ChatColor.GRAY
																			+ "Feather o' Flight",
																	featherLore) });
									info.batman.getInventory().setHelmet(
											game.setArmourColour(
													Material.LEATHER_HELMET, 0,
													0, 0));
									info.batman
											.getInventory()
											.setChestplate(
													game.setArmourColour(
															Material.LEATHER_CHESTPLATE,
															0, 0, 0));
									info.batman.getInventory().setLeggings(
											game.setArmourColour(
													Material.LEATHER_LEGGINGS,
													0, 0, 0));
									info.batman.getInventory().setBoots(
											game.setArmourColour(
													Material.LEATHER_LEGGINGS,
													0, 0, 0));
									game.setListName(info.batman,
											info.batman.getDisplayName(),
											ChatColor.GRAY);
									return true;
								}
								if (info.robin != null && info.robin == p) {
									ItemStack strength = new ItemStack(
											Material.POTION);
									Potion pp = new Potion(PotionType.STRENGTH);
									pp.setSplash(true);
									pp.apply(strength);
									ItemStack instantHeal = new ItemStack(
											Material.POTION);
									Potion p2 = new Potion(
											PotionType.INSTANT_HEAL);
									p2.setSplash(true);
									p2.apply(instantHeal);
									ItemStack regen = new ItemStack(
											Material.POTION);
									Potion p3 = new Potion(PotionType.REGEN);
									p3.setSplash(true);
									p3.apply(regen);
									ItemStack posion = new ItemStack(
											Material.POTION);
									Potion p4 = new Potion(PotionType.POISON);
									p4.setSplash(true);
									p4.apply(posion);
									ItemStack speed = new ItemStack(
											Material.POTION);
									Potion p5 = new Potion(PotionType.SPEED);
									p5.setSplash(true);
									p5.apply(speed);
									ItemStack sword;
									sword = new ItemStack(
											Material.DIAMOND_SWORD);
									sword.addEnchantment(
											Enchantment.DAMAGE_ALL, 1);

									List compassLore = new ArrayList();
									List bowLore = new ArrayList();
									List swordLore = new ArrayList();
									List featherLore = new ArrayList();

									compassLore
											.add(ChatColor.DARK_GRAY
													+ "Use This To Track The Bad Guys!");
									bowLore.add(ChatColor.DARK_GRAY
											+ "Take em down boyz!");
									swordLore.add(ChatColor.DARK_GRAY
											+ "Slice and Dice!");
									featherLore
											.add(ChatColor.DARK_GRAY
													+ "Fly like one of your French Girls!");

									info.robin
											.getInventory()
											.addItem(
													new ItemStack[] {
															game.setName(
																	new ItemStack(
																			Material.COMPASS),
																	ChatColor.GREEN
																			+ "Tracker",
																	compassLore),
															game.setName(
																	sword,
																	ChatColor.RED
																			+ "BatSword",
																	swordLore),
															game.setName(
																	new ItemStack(
																			Material.FEATHER),
																	ChatColor.GRAY
																			+ "Feather o' Flight",
																	featherLore),
															strength });
									info.robin.getInventory().setHelmet(
											game.setArmourColour(
													Material.LEATHER_HELMET,
													64, 193, 55));
									info.robin
											.getInventory()
											.setChestplate(
													game.setArmourColour(
															Material.LEATHER_CHESTPLATE,
															64, 193, 55));
									info.robin.getInventory().setLeggings(
											game.setArmourColour(
													Material.LEATHER_LEGGINGS,
													64, 193, 55));
									info.robin.getInventory().setBoots(
											game.setArmourColour(
													Material.LEATHER_LEGGINGS,
													64, 193, 55));
									game.setListName(info.robin,
											info.robin.getDisplayName(),
											ChatColor.GREEN);
								}
								if (info.joker != null && info.joker == p) {
									info.joker.getInventory().setHelmet(
											game.setArmourColour(
													Material.LEATHER_HELMET,
													191, 0, 255));
									info.joker
											.getInventory()
											.setChestplate(
													game.setArmourColour(
															Material.LEATHER_CHESTPLATE,
															191, 0, 255));
									info.joker.getInventory().setLeggings(
											game.setArmourColour(
													Material.LEATHER_LEGGINGS,
													191, 0, 255));

									ItemStack jokerBoots = new ItemStack(
											Material.IRON_BOOTS);
									jokerBoots.addUnsafeEnchantment(
											Enchantment.PROTECTION_FALL, 20);
									List jokerBootsLore = new ArrayList();
									jokerBootsLore.add(ChatColor.DARK_GRAY
											+ "Escape In Times Of Need...");
									info.joker
											.getInventory()
											.setBoots(
													game.setName(
															jokerBoots,
															ChatColor.GRAY
																	+ "The Iron Imperators",
															jokerBootsLore));

									ItemStack jSword = new ItemStack(
											Material.IRON_SWORD);
									ItemStack jPotion = new ItemStack(
											Material.POTION);
									Potion jP = new Potion(PotionType.POISON);
									jP.setSplash(true);
									jP.apply(jPotion);

									List jokerSwordLore = new ArrayList();
									List jokerBombLore = new ArrayList();
									List tntiabLore = new ArrayList();

									tntiabLore
											.add(ChatColor.DARK_GRAY
													+ "Trick the villains into opening these... They'll get a big suprise!");
									jokerSwordLore
											.add(ChatColor.DARK_GRAY
													+ "So Little Time... So Much Pain...");
									jokerBombLore.add(ChatColor.RED
											+ "Kaboom...");

									jSword.addEnchantment(
											Enchantment.DAMAGE_ALL, 2);
									info.joker
											.getInventory()
											.addItem(
													new ItemStack[] { game
															.setName(
																	jSword,
																	ChatColor.DARK_PURPLE
																			+ "The Fate Changer",
																	jokerSwordLore) });
									info.joker.getInventory().addItem(
											new ItemStack[] { game.setName(
													new ItemStack(
															Material.CHEST, 5),
													ChatColor.RED
															+ "TNT-In-A-Box",
													new ArrayList()) });
									game.setListName(info.joker,
											info.joker.getDisplayName(),
											ChatColor.LIGHT_PURPLE);
								}
							}
						}
					}
				}
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("setcount")) {
						if (p.isOp()) {
							info.playerc = Integer.parseInt(args[1]);
							p.sendMessage(pre
									+ " The player count has been set to "
									+ info.playerc);
							return true;
						} else {
							p.sendMessage("You don't got the permissions!");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("settime")) {
						if (p.isOp()) {
							info.lobbytime = Integer.parseInt(args[1]);
							p.sendMessage(pre
									+ " The lobby time has been set to "
									+ info.lobbytime);
							return true;
						} else {
							p.sendMessage("You don't got the permissions!");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("addchest")) {
						if (p.isOp()) {
							int amount = getConfig().getInt("Chests.amount");
							getConfig().set(
									"Chests." + args[1] + "." + (amount + 1),
									p.getLocation().getBlockX() + ","
											+ p.getLocation().getBlockY() + ","
											+ p.getLocation().getBlockZ());
							p.sendMessage(pre + " (" + args[1]
									+ ") Added a new chest to "
									+ p.getLocation().getBlockX() + ","
									+ p.getLocation().getBlockY() + ","
									+ p.getLocation().getBlockZ()
									+ " (Your current location)");
							getConfig().set("Chests.amount", amount + 1);
							saveAll();
							return true;
						} else {
							p.sendMessage("Nope.");
							return true;
						}
					}

				} else {
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("world")) {
							System.out
									.println("[TBN] The current active world is "
											+ info.getActiveWorld().getName());
							return true;
						}
					}
				}
			}
		}
		if (label.equalsIgnoreCase("setspawn")) {
			if (sender instanceof Player) {
				Player pp = (Player) sender;
				// TODO: Change to tradional method of mod finding
				if (pp.isOp()) {
					if (args.length == 2) {
						String world = args[0];
						int spawnnm = Integer.parseInt(args[1]);
						setPlayerSpawn(spawnnm, pp.getLocation(), world);
						pp.sendMessage(ChatColor.LIGHT_PURPLE
								+ "The spawn number " + spawnnm
								+ " in the world " + world
								+ "has been set to your current location!");
						return true;
					} else {
						pp.sendMessage(ChatColor.RED
								+ "Correct usage: /setplayerspaw <world> <which>");
						return true;
					}
				}
			} else {
				System.out
						.print("Why the hell do you want to set the spawn location from the console?!");
				return true;
			}
		}
		return false;
	}

	public void setChestSpawn(int i, Location loca) {
		getConfig().set(
				"Location." + loca.getWorld().getName() + ".chest." + i + ".x",
				loca.getBlockX());
		getConfig().set(
				"Location." + loca.getWorld().getName() + ".chest." + i + ".y",
				loca.getBlockY());
		getConfig().set(
				"Location." + loca.getWorld().getName() + ".chest." + i + ".z",
				loca.getBlockZ());
		saveAll();
	}

	public Location getPlayerSpawn(String world, int i) {
		if (i == 0) {
			String[] ss = getConfig().getString(
					"Spawn." + world + "." + "lobby").split(",");
			return new Location(Bukkit.getWorld(world),
					Integer.parseInt(ss[0]), Integer.parseInt(ss[1]),
					Integer.parseInt(ss[2]));
		}
		if (i == -1) {
			String[] ss = getConfig().getString(
					"Spawn." + world + "." + "batman").split(",");
			return new Location(Bukkit.getWorld(world),
					Integer.parseInt(ss[0]), Integer.parseInt(ss[1]),
					Integer.parseInt(ss[2]));
		}
		if (i == -2) {
			String[] ss = getConfig().getString(
					"Spawn." + world + "." + "robin").split(",");
			return new Location(Bukkit.getWorld(world),
					Integer.parseInt(ss[0]), Integer.parseInt(ss[1]),
					Integer.parseInt(ss[2]));
		}
		String[] ss = getConfig().getString("Spawn." + world + "." + i).split(
				",");
		return new Location(Bukkit.getWorld(world), Integer.parseInt(ss[0]),
				Integer.parseInt(ss[1]), Integer.parseInt(ss[2]));
	}

	public void setPlayerSpawn(int i, Location loca, String world) {
		if (i == 0) {
			getConfig().set(
					"Spawn." + world + "." + "lobby",
					"" + loca.getBlockX() + "," + loca.getBlockY() + ","
							+ loca.getBlockZ());
			saveAll();
			return;
		}
		if (i == -1) {
			getConfig().set(
					"Spawn." + world + "." + "batman",
					"" + loca.getBlockX() + "," + loca.getBlockY() + ","
							+ loca.getBlockZ());
			saveAll();
			return;
		}
		if (i == -2) {
			getConfig().set(
					"Spawn." + world + "." + "robin",
					"" + loca.getBlockX() + "," + loca.getBlockY() + ","
							+ loca.getBlockZ());
			saveAll();
			return;
		}
		getConfig().set(
				"Spawn." + world + "." + i,
				"" + loca.getBlockX() + "," + loca.getBlockY() + ","
						+ loca.getBlockZ());
		saveAll();
	}

	public int getMaxPlayer() {
		return getConfig().getInt("MaxPlayers");
	}

	public int getMinPlayer() {
		return getConfig().getInt("MinPlayers");
	}

	public int getTime() {
		return getConfig().getInt("LobbyTime");
	}

	public void saveAll() {
		saveConfig();
		reloadConfig();
	}

}