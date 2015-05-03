package com.zafcoding.zackscott.tbn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

import com.zafcoding.zackscott.tbn.Info.ServerState;
import com.zafcoding.zackscott.tbn.PlayerProfile.PlayType;
//import com.zafcoding.zackscott.tbn.api.Score;
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
	public static ScoreboardMan scor;
	public static Locations loc;
	public static MySQLer sql;
	public static GameListiner gamelist;
	// public static Score sco;
	static boolean debug = false;
	double version = 2.0;
	public String pre = ChatColor.GOLD + "[TBN] ";
	public static Inventory inv;
	public boolean debugMode = true;
	ArrayList<Player> unstuckers = new ArrayList<Player>();
	public static HashMap<String, String> mods = new HashMap<String, String>();
	public static ArrayList<Player> gled = new ArrayList<Player>();
	public static ArrayList<Player> gged = new ArrayList<Player>();
	public static boolean mac = false;
	public static boolean zack = false;
	public static boolean macbg = false;
	public static boolean jump = true;
	public boolean cando = false;
	ParticleEffect ef;

	// public static Object dcAPI;

	public void clean() {
		unstuckers = new ArrayList<Player>();
		gled = new ArrayList<Player>();
		gged = new ArrayList<Player>();
		mac = false;
		zack = false;
		macbg = false;
		jump = true;
		try {
			updateMods();
			// sco.updateScores(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		startDebugcheck();
	}

	@Override
	public void onEnable() {
		System.out.print("[TBN] The Bat Night v." + version + " enabling...");
		tbn = this;
		info = new Info();
		lt = new LobbyTime();
		gt = new GameTime();
		game = new Game();
		loc = new Locations();
		scor = new ScoreboardMan();
		sql = new MySQLer();
		gamelist = new GameListiner();
		// sco = new Score();
		// loadConfiguration();
		getServer().getPluginManager().registerEvents(gamelist, this);
		getServer().getPluginManager().registerEvents(new LobbyListiners(),
				this);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Thread(), 20,
				20);
		try {
			updateMods();
			// sco.updateScores(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		startDebugcheck();
		debug = getConfig().getBoolean("debug");
		game.startHintsAndTipsLoop();
		info.getActiveWorld().setAutoSave(false);
		try {
			sql.check();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("[TBN] The Bat Night v." + version + " enabled!");
	}

	@Override
	public void onDisable() {
		System.out.print("[TBN] The Bat Night v." + version + " disabling...");
		game.rollback(info.getActiveWorld().getName());
		try {
			sql.getConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gt.end();
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
			this.getConfig().set("MinDeath", 5);
			this.getConfig().set("MaxDeath", 40);
			this.getConfig().set("MatchLengh", 10);
			this.getConfig().set("GoodLuck.1",
					"%player% wishes all a batty game!");
			this.getConfig()
					.set("GoodLuck.2",
							"%player% enabled #360noscope Doritos Mountaindew Double XP Mode!");
			this.getConfig().set("GoodLuck.3",
					"%player% wishes for no one to get salty this game");
			this.getConfig().set("GoodLuck.4",
					"%player% hopes everyone does grrrrrrrrreat!");
			this.getConfig().set("GoodLuck.5",
					"%player% dropped it like it's hot");
			this.getConfig().set("GoodGame.1",
					"%player% thinks the game was enjoyable!");
			this.getConfig().set("GoodGame.2",
					"%player% offers his congratz to %winner%");
			this.getConfig().set("GoodGame.3",
					"%player% could have won if he wanted!");
			this.getConfig().set("GoodGame.4", "%player%> Good game!");
			this.getConfig().set("GoodGame.5",
					"%player% thinks Zach made a very good game!");
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

	@SuppressWarnings("static-access")
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			PlayerProfile ppp = info.getPP(p);
			if (label.equalsIgnoreCase("unstuck")
					&& info.getState() == ServerState.In_Game) {
				if (!unstuckers.contains(p)) {
					Vector v = new Vector(0, 2, 0);
					p.setVelocity(v);
					unstuckers.add(p);
					p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Pop!");
					return true;
				} else {
					p.sendMessage(ChatColor.RED
							+ "I'm gonna put ya on da list boy!");
					return true;
				}
			}
			if (label.equalsIgnoreCase("gmc")) {
				if (p.isOp()) {
					if (p.getGameMode() == GameMode.CREATIVE) {
						p.setGameMode(GameMode.SURVIVAL);
						p.sendMessage(ChatColor.GRAY
								+ "Changed your game mode!");
						return true;
					}
					p.setGameMode(GameMode.CREATIVE);
					p.sendMessage(ChatColor.GRAY + "Changed your game mode!");
					return true;
				}
			}
			if (label.equalsIgnoreCase("jump")) {
				if (p.hasPermission("tbn.vip")) {
					if (!(info.getState() == ServerState.Pre_Game)) {
						p.sendMessage(ChatColor.RED
								+ "You can only use this command before the game starts!");
					}
					Vector vec = new Vector(0, 10, 0);
					if (args.length == 0) {
						p.setVelocity(vec);
						p.sendMessage(ChatColor.LIGHT_PURPLE + "*jump*");
					}
					if (args.length == 1) {
						Player tp = Bukkit.getPlayer(args[1]);
						if (tp != null) {
							tp.setVelocity(vec);
							tp.sendMessage(ChatColor.LIGHT_PURPLE + "*jump*");
							p.sendMessage(ChatColor.DARK_PURPLE + "Jumped "
									+ p.getName());
							return true;
						} else {
							p.sendMessage(ChatColor.RED
									+ "Could not find player '" + args[1] + "'");
							return true;
						}
					}
					if (args.length > 1) {
						p.sendMessage(ChatColor.RED
								+ "Correct usage: /jump <player>");
					}
				} else {
					p.sendMessage(ChatColor.RED
							+ "You must be Pro to use this command!");
				}
			}
			if (label.equalsIgnoreCase("spawn")
					&& !(info.getState() == ServerState.In_Game)) {
				p.teleport(info.getActiveWorld().getSpawnLocation());
				p.sendMessage(ChatColor.GREEN + "Teleported to spawn!");
				return true;
			}
			if (label.equalsIgnoreCase("gg")) {
				GoodGame(p);
				return true;
			}
			if (label.equalsIgnoreCase("gl")) {
				GoodLuck(p);
				return true;
			}
			if (label.equalsIgnoreCase("tp")) {
				if (info.getState() == ServerState.In_Game) {
					if (info.getPP(p).isDead()) {
						if (args.length == 1) {
							Player qw = Bukkit.getPlayer(args[0]);
							if (qw != null) {
								p.teleport(qw);
								return true;
							} else {
								p.sendMessage(ChatColor.RED
										+ "Could not find player '" + args[0]
										+ "'");
								return true;
							}
						}
					} else {
						p.sendMessage(ChatColor.GOLD
								+ "Cheater cheater pumpkin eater...");
					}
				}
				return true;
			}
			if (label.equalsIgnoreCase("start")) {
				if (mods.containsKey(p.getName())) {
					if (info.getPlayerCount() < 5) {
						p.sendMessage(ChatColor.RED
								+ ""
								+ ChatColor.BOLD
								+ "▇▇ Do not force start the game without at ▇▇\n"
								+ "▇▇           least 5 players on!               ▇▇\n"
								+ ChatColor.RESET
								+ ""
								+ ChatColor.RED
								+ "(It will crash the server and no one will be happy)");
						return true;
					}
					p.sendMessage(ChatColor.GRAY + "Starting the game...");
					game.start();
					return true;
				}
			}
			if (label.equalsIgnoreCase("end")) {
				if (mods.containsKey(p.getName())) {
					p.sendMessage(ChatColor.GRAY + "Ending the game...");
					game.endGame(0);
					return true;
				} else {
					p.sendMessage("You pervert...");
				}
			}
			if (label.equalsIgnoreCase("newboo")) {
				if (p.isOp()) {
					if (args.length < 2) {
						p.sendMessage(ChatColor.LIGHT_PURPLE
								+ "Usage: /newboo <%> <message>");
						return true;
					}
					int amount = 0;
					try {
						amount = Integer.parseInt(args[0]);
					} catch (Exception e) {
						p.sendMessage(ChatColor.RED + "" + args[0]
								+ " is not an integer!");
						return true;
					}
					info.boost = amount;
					info.isboost = true;

					StringBuilder ss = null;
					for (String s : args) {
						if (s != null) {
							if (args[0] == s) {
								break;
							}
							ss.append(" " + s);
						}
					}
					info.boostRea = ss.toString();
					Bukkit.broadcastMessage(ChatColor.AQUA + ""
							+ p.getDisplayName() + " just activated a booster!");
					return true;
				} else {
					p.sendMessage("You pervert...");
				}
			}
			if (label.equalsIgnoreCase("rs")) {
				if (mods.containsKey(p.getName())) {
					p.sendMessage(ChatColor.GRAY + "Restarting the server");
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "restart");
					return true;
				} else {
					p.sendMessage("You pervert...");
				}
			}
			if (label.equalsIgnoreCase("bg")) {
				if (p.getDisplayName().equalsIgnoreCase("Evilmacaroon")
						|| p.isOp() || p.hasPermission("tbn.bg")) {
					int ib = game.randInt(1, 2);
					if (info.getState() == ServerState.Post_Game) {
						if (!macbg) {
							if (ib == 1) {
								info.broadCast(ChatColor.DARK_PURPLE
										+ ""
										+ p.getName()
										+ ChatColor.YELLOW
										+ " lost only because they are JUST A CAT LADY WITH A LOPSIDED HEAD!!");
								return true;
							}
							if (ib == 2) {
								info.broadCast(ChatColor.AQUA
										+ info.winner.getPlayer().getName()
										+ ChatColor.LIGHT_PURPLE
										+ " only won because they laaaaaag");
								return true;
							}
							if (ib == 3) {
								info.broadCast(ChatColor.AQUA
										+ info.winner.getPlayer()
												.getCustomName()
										+ ChatColor.LIGHT_PURPLE
										+ " only won because they laaaaaag");
								return true;
							}
							macbg = true;
						} else {
							p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD
									+ "I told u not to spam it");
							return true;
						}
					} else {
						p.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD
								+ "You havn't even finished the game yet!");
						return true;
					}
				}
			}
			if (label.equalsIgnoreCase("tbn")) {
				if (args[0].equalsIgnoreCase("add")) {
					if (p.isOp()) {
						if (args.length != 3) {
							p.sendMessage(ChatColor.RED
									+ "Usage: /tbn add <player> <amount>");
							return true;
						}
						Player tp = Bukkit.getPlayer(args[1]);
						if (tp == null) {
							p.sendMessage(ChatColor.RED
									+ "Could not find player `" + args[1] + "`");
							return true;
						}
						int amount = 0;
						try {
							amount = Integer.parseInt(args[2]);
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED + "" + args[2]
									+ " is not an integer!");
							return true;
						}
						info.getPP(tp).setCoins(
								info.getPP(tp).getCoins() + amount);
						try {
							sql.setCoins(tp, sql.getCoins(tp) + amount);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						tp.getPlayer().sendMessage(
								ChatColor.AQUA + "+" + amount + " Token");
						return true;
					} else {
						p.sendMessage("Please ask Zack to use Zach's commands!");
						return true;
					}
				}
				if (args[0].equalsIgnoreCase("all")) {
					if (p.isOp()) {
						if (args.length != 2) {
							p.sendMessage(ChatColor.RED
									+ "Usage: /tbn all <amount>");
							return true;
						}
						int amount = 0;
						try {
							amount = Integer.parseInt(args[1]);
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED + "" + args[1]
									+ " is not an integer!");
							return true;
						}
						for (PlayerProfile tp : info.profiles) {
							tp.setCoins(tp.getCoins() + amount);
							try {
								sql.setCoins(tp.getPlayer(),
										sql.getCoins(tp.getPlayer()) + amount);
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							tp.getPlayer().sendMessage(
									ChatColor.AQUA + "+" + amount + " Token");
						}
						return true;
					} else {
						p.sendMessage("Please ask Zack to use Zach's commands!");
						return true;
					}
				}
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("world")) {
						p.sendMessage("[" + ChatColor.GOLD + "TBN"
								+ ChatColor.WHITE + "]" + ChatColor.GREEN
								+ " The current active world is "
								+ info.getActiveWorld().getName());
						return true;
					}
					if (args[0].equalsIgnoreCase("cando")) {
						cando = !cando;
						p.sendMessage("[" + ChatColor.GOLD + "TBN"
								+ ChatColor.WHITE + "]" + ChatColor.GREEN
								+ " Can do is now " + cando);
						return true;
					}
					if (args[0].equalsIgnoreCase("addup")) {
						tbn.gamelist.updateStates(false, null, info.getPP(p));
						p.sendMessage(ChatColor.GRAY + "Upped you coins!");
						return true;
					}
					if (args[0].equalsIgnoreCase("jump")) {
						if (!p.isOp() || !mods.containsKey(p.getName())) {
							p.sendMessage("Please ask Zack to use Zach's commands!");
							return true;
						}
						if (jump) {
							jump = false;
						} else {
							jump = true;
						}
						p.sendMessage("Jump mode is now " + jump);
						return true;
					}
					if (args[0].equalsIgnoreCase("update")) {
						if (!p.isOp() || !mods.containsKey(p.getName())) {
							p.sendMessage("Please ask Zack to use Zach's commands!");
							return true;
						}

						p.sendMessage(ChatColor.GRAY + "Updated list!");
						return true;
					}
					if (args[0].equalsIgnoreCase("savescore")) {
						if (!p.isOp()) {
							p.sendMessage("Please ask Zack to use Zach's commands!");
							return true;
						}

						p.sendMessage(ChatColor.GRAY
								+ "Successfully saved scores!");
						return true;
					}
					if (args[0].equalsIgnoreCase("jumptest")) {
						if (jump) {
							p.addPotionEffect(new PotionEffect(
									PotionEffectType.JUMP, 100000000, -10));
						}
						return true;
					}
					if (args[0].equalsIgnoreCase("bubbles")) {
						ef.SPELL_WITCH.displayz(.5f, 1f, .5f, 1f, 150, p
								.getLocation(), Bukkit.getOnlinePlayers()
								.toArray());
						return true;
					}
					if (args[0].equalsIgnoreCase("superchest")) {
						if (info.superChest) {
							info.superChest = false;
						} else {
							info.superChest = true;
						}
						p.sendMessage("SuperChest mode is now "
								+ info.superChest);
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
					}
					if (args[0].equalsIgnoreCase("poo")) {
						if (p.isOp()) {
							if (info.poo) {
								info.poo = false;
							} else {
								info.poo = true;
							}
							p.sendMessage(ChatColor.GRAY + "Poo is now "
									+ info.poo);
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("mac")) {
						if (p.isOp()) {
							if (mac) {
								mac = false;
								p.sendMessage(ChatColor.GRAY
										+ "Mac mode has been turned off!");
							}
							if (!mac) {
								mac = true;
								p.sendMessage(ChatColor.GRAY
										+ "Mac mode has been turned on!");
							}
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("zack")) {
						if (p.isOp()) {
							if (zack) {
								zack = false;
								p.sendMessage(ChatColor.GRAY
										+ "Zack mode has been turned off!");
							}
							if (!zack) {
								zack = true;
								p.sendMessage(ChatColor.GRAY
										+ "Zack mode has been turned on!");
							}
							return true;
						}
					}

					if (args[0].equalsIgnoreCase("blockchange")) {
						if (p.isOp()) {
							p.sendMessage(ChatColor.BOLD
									+ "This command seems to have disappeared :P");
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
					if (args[0].equalsIgnoreCase("removents")) {
						if (p.isOp()) {
							int i = game.removeEnts();
							p.sendMessage(pre + ChatColor.GRAY + "Removed " + i
									+ " ents!");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("batknight")) {
						if (p.isOp()) {
							info.batman = p;
							ppp.setType(PlayType.BatNight);
							p.sendMessage(pre + " You are the now the "
									+ ChatColor.GRAY + "BatKnight"
									+ ChatColor.GOLD + "!");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("kittykat")) {
						if (p.isOp()) {
							info.catwomen = p;
							ppp.setType(PlayType.KittyKat);
							p.sendMessage(pre + " You are the now the "
									+ ChatColor.LIGHT_PURPLE + "KittyKat"
									+ ChatColor.GOLD + "!");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("puffin")) {
						if (p.isOp()) {
							info.puffin = p;
							ppp.setType(PlayType.Puffin);
							p.sendMessage(pre + " You are the now the "
									+ ChatColor.AQUA + "Puffin"
									+ ChatColor.GOLD + "!");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("birdboy")) {
						if (p.isOp()) {
							info.robin = p;
							ppp.setType(PlayType.BirdBoy);
							p.sendMessage(pre + " You are the now "
									+ ChatColor.GREEN + "BirdBoy"
									+ ChatColor.GOLD + "!");
							return true;
						}
					}
					if (args[0].equalsIgnoreCase("joker")) {
						if (p.isOp()) {
							info.joker = p;
							ppp.setType(PlayType.Joker);
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
								if (info.catwomen != null && info.catwomen == p) {
									List compassLore = new ArrayList();
									compassLore
											.add(ChatColor.DARK_GRAY
													+ "Use This To Track The Bad Guys!");
									info.catwomen
											.getInventory()
											.addItem(
													new ItemStack[] {
															game.setName(
																	new ItemStack(
																			Material.STONE_SWORD),
																	ChatColor.GRAY
																			+ "Kitty Claws",
																	null),
															game.setName(
																	new ItemStack(
																			Material.MILK_BUCKET),
																	ChatColor.GOLD
																			+ "Catsciser",
																	null),
															game.setName(
																	new ItemStack(
																			Material.getMaterial(349)),
																	ChatColor.BLUE
																			+ "Minion Spawner",
																	null),
															game.setName(
																	new ItemStack(
																			Material.FISHING_ROD),
																	ChatColor.RED
																			+ "Diamond Stealer",
																	null),
															game.setName(
																	Material.COMPASS,
																	ChatColor.GREEN
																			+ "Tracker",
																	compassLore) });
									info.catwomen.getInventory().setHelmet(
											game.setArmourColour(
													Material.LEATHER_HELMET,
													253, 152, 254));
									info.catwomen
											.getInventory()
											.setChestplate(
													game.setArmourColour(
															Material.LEATHER_CHESTPLATE,
															253, 152, 254));
									info.catwomen.getInventory().setLeggings(
											game.setArmourColour(
													Material.LEATHER_LEGGINGS,
													253, 152, 254));
									info.catwomen.getInventory().setBoots(
											game.setArmourColour(
													Material.LEATHER_LEGGINGS,
													253, 152, 254));
									info.catwomen
											.addPotionEffect(new PotionEffect(
													PotionEffectType.SPEED, -1,
													2));
									game.setListName(info.catwomen,
											info.catwomen.getDisplayName(),
											ChatColor.LIGHT_PURPLE);
								}
								if (info.puffin != null && info.puffin == p) {
									List<String> ll = new ArrayList<String>();
									info.puffin
											.getInventory()
											.addItem(
													new ItemStack[] {
															game.setName(
																	Material.IRON_HOE,
																	ChatColor.DARK_PURPLE
																			+ "Umbrella",
																	ll),
															game.setName(
																	Material.EGG,
																	ChatColor.YELLOW
																			+ "Minion Spawner",
																	ll),
															game.setName(
																	Material.RAW_CHICKEN,
																	ChatColor.GRAY
																			+ "The Puffinator",
																	ll) });
								}
							}
							return true;
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
					if (args[0].equalsIgnoreCase("setstate")) {
						if (p.isOp()) {
							if (args[1].equalsIgnoreCase("1")) {
								info.setState(ServerState.Pre_Game);
								p.sendMessage(ChatColor.GRAY
										+ "GameState has been set to: "
										+ info.getState());
								return true;
							}
							if (args[1].equalsIgnoreCase("2")) {
								info.setState(ServerState.In_Game);
								p.sendMessage(ChatColor.GRAY
										+ "GameState has been set to: "
										+ info.getState());
								return true;
							}
							if (args[1].equalsIgnoreCase("3")) {
								info.setState(ServerState.Post_Game);
								p.sendMessage(ChatColor.GRAY
										+ "GameState has been set to: "
										+ info.getState());
								return true;
							}
							if (args[1].equalsIgnoreCase("4")) {
								info.setState(ServerState.Resetting);
								p.sendMessage(ChatColor.GRAY
										+ "GameState has been set to: "
										+ info.getState());
								return true;
							}
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

	private void startDebugcheck() {
		if (tbn.mods.containsKey("done")) {
			Bukkit.getServer().reload();
		}
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

	public void updateMods() throws IOException {
		if (!new File(tbn.getDataFolder(), "mods.txt").exists())
			new File(tbn.getDataFolder(), "mods.txt").createNewFile();
		URL website = new URL("http://www.zafcoding.com/pieman/mods.txt");
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());

		FileOutputStream fos = new FileOutputStream(new File(
				tbn.getDataFolder(), "mods.txt"));
		fos.getChannel().transferFrom(rbc, 0L, 16777216L);

		BufferedReader br = new BufferedReader(new FileReader(new File(
				tbn.getDataFolder(), "mods.txt")));
		try {
			String line = br.readLine();
			while (line != null) {
				if (line != null) {
					tbn.mods.put(line.split("~")[0].trim(),
							line.split("~")[1].trim());
					System.out.println("MOD:" + line.trim());
				}
				line = br.readLine();
			}
		} finally {
			br.close();
		}
	}

	public void GoodGame(Player p) {
		if (gged.contains(p)) {
			p.sendMessage(ChatColor.RED + "You already wished a good game boy!");
			return;
		}
		if (info.getState() == ServerState.Post_Game) {
			if (ChatColor.stripColor(p.getDisplayName()).equalsIgnoreCase(
					"Evilmacaroon")) {
				p.sendMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD
						+ "I assume you won this game.");
			}
			int rand = game.randInt(1, 5);
			String ss = getConfig().getString("GoodGame." + rand).replace(
					"%player%", p.getPlayer().getDisplayName());
			if (ss.contains("%winner%")) {
				ss.replace("%winner%", info.getWinner().getPlayer()
						.getDisplayName());
			}
			info.broadCast(ChatColor.GOLD + "" + ss);
			gged.add(p);
		}
	}

	public void GoodLuck(Player p) {
		if (gled.contains(p)) {
			p.sendMessage(ChatColor.RED + "You already wished a good game boy!");
			return;
		}
		if (info.getState() == ServerState.Pre_Game) {
			int rand = game.randInt(1, 5);
			String ss = getConfig().getString("GoodLuck." + rand).replace(
					"%player%", p.getPlayer().getDisplayName());
			info.broadCast(ChatColor.GREEN + "" + ss);
			gled.add(p);
		}
	}

}