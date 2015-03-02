package com.zafcoding.zackscott.tbn.game;

import com.zafcoding.zackscott.tbn.Info;
import com.zafcoding.zackscott.tbn.TBN;
import com.zafcoding.zackscott.tbn.orginial.Map;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class Locations {

	static TBN tbn = TBN.tbn;
	static Info info = TBN.info;
	static Game game = TBN.game;

	public static void teleportPlayers(int mapId, String mapName) {
		Location batmanLocation = null;
		Location robinLocation = null;
		Location catwomanLocation = null;
		int random1 = new Random().nextInt(10) + 1;
		if (mapId == 0) {
			if (random1 <= 3) {
				batmanLocation = new Location(info.batman.getWorld(), tbn
						.getConfig().getInt("Spawns.Batman.x"), tbn.getConfig()
						.getInt("Spawns.Batman.y"), tbn.getConfig().getInt(
						"Spawns.Batman.z"));
				robinLocation = new Location(info.robin.getWorld(), tbn
						.getConfig().getInt("Spawns.Robin.x"), tbn.getConfig()
						.getInt("Spawns.Robin.y"), tbn.getConfig().getInt(
						"Spawns.Robin.z"));
				catwomanLocation = new Location(info.robin.getWorld(), 510.0D,
						147.0D, -36.0D);
			} else if ((random1 >= 3) && (random1 <= 6)) {
				batmanLocation = new Location(info.batman.getWorld(), 507.0D,
						147.0D, -36.0D);
				robinLocation = new Location(info.robin.getWorld(), 510.0D,
						147.0D, -36.0D);
				catwomanLocation = new Location(info.robin.getWorld(), 510.0D,
						147.0D, -36.0D);
			} else {
				batmanLocation = new Location(info.batman.getWorld(), 599.0D,
						100.0D, -149.0D);
				robinLocation = new Location(info.robin.getWorld(), 599.0D,
						100.0D, -151.0D);
				catwomanLocation = new Location(info.robin.getWorld(), 510.0D,
						147.0D, -36.0D);
			}
		} else if (mapId == 1) {
			if (random1 <= 3) {
				batmanLocation = new Location(info.batman.getWorld(), 2398.0D,
						221.0D, -821.0D);
				robinLocation = new Location(info.robin.getWorld(), 2398.0D,
						221.0D, -823.0D);
				catwomanLocation = new Location(info.robin.getWorld(), 2380.0D,
						186.0D, -801.0D);
			} else if ((random1 >= 3) && (random1 <= 6)) {
				batmanLocation = new Location(info.batman.getWorld(), 2317.0D,
						186.0D, -828.0D);
				robinLocation = new Location(info.robin.getWorld(), 2317.0D,
						221.0D, -812.0D);
				catwomanLocation = new Location(info.robin.getWorld(), 2380.0D,
						186.0D, -801.0D);
			} else {
				batmanLocation = new Location(info.batman.getWorld(), 2364.0D,
						211.0D, -794.0D);
				robinLocation = new Location(info.robin.getWorld(), 2364.0D,
						211.0D, -846.0D);
				catwomanLocation = new Location(info.robin.getWorld(), 2380.0D,
						186.0D, -801.0D);
			}
		} else if (mapId == 2) {
			if (random1 <= 3) {
				batmanLocation = new Location(info.batman.getWorld(), 2683.0D,
						81.0D, 67.0D);
				robinLocation = new Location(info.robin.getWorld(), 2693.0D,
						81.0D, 71.0D);
				catwomanLocation = new Location(info.robin.getWorld(), 2848.0D,
						77.0D, -11.0D);
			} else if ((random1 >= 3) && (random1 <= 6)) {
				batmanLocation = new Location(info.batman.getWorld(), 2701.0D,
						60.0D, 256.0D);
				robinLocation = new Location(info.robin.getWorld(), 2702.0D,
						54.0D, 267.0D);
				catwomanLocation = new Location(info.robin.getWorld(), 3025.0D,
						64.0D, 362.0D);
			} else {
				batmanLocation = new Location(info.batman.getWorld(), 3124.0D,
						99.0D, 344.0D);
				robinLocation = new Location(info.robin.getWorld(), 3115.0D,
						99.0D, 344.0D);
				catwomanLocation = new Location(info.robin.getWorld(), 2907.0D,
						48.0D, 451.0D);
			}
		} else if (mapId == 3) {
			if (random1 <= 3) {
				batmanLocation = new Location(info.batman.getWorld(), 2716.0D,
						144.0D, -2220.0D);
				robinLocation = new Location(info.robin.getWorld(), 2712.0D,
						144.0D, -2220.0D);
				catwomanLocation = new Location(info.robin.getWorld(), 2704.5D,
						81.0D, -2044.5D);
			} else if ((random1 >= 3) && (random1 <= 6)) {
				batmanLocation = new Location(info.batman.getWorld(), 2729.0D,
						151.0D, -1804.0D);
				robinLocation = new Location(info.robin.getWorld(), 2728.0D,
						151.0D, -1795.0D);
				catwomanLocation = new Location(info.robin.getWorld(), 2824.5D,
						173.0D, -1712.5D);
			} else {
				batmanLocation = new Location(info.batman.getWorld(), 2685.0D,
						77.0D, -1952.0D);
				robinLocation = new Location(info.robin.getWorld(), 2679.0D,
						77.0D, -1952.0D);
				catwomanLocation = new Location(info.robin.getWorld(), 2792.5D,
						144.0D, -1813.5D);
			}
		} else if (mapId == 4) {
			if (new Random().nextBoolean()) {
				batmanLocation = new Location(info.batman.getWorld(), -31.5D,
						97.0D, 189.5D);
				robinLocation = new Location(info.robin.getWorld(), -31.5D,
						97.0D, 196.5D);
				catwomanLocation = new Location(info.robin.getWorld(), 160.5D,
						74.0D, 183.5D);
			} else {
				batmanLocation = new Location(info.batman.getWorld(), 106.5D,
						73.0D, 344.5D);
				robinLocation = new Location(info.robin.getWorld(), 108.5D,
						73.0D, 344.5D);
				catwomanLocation = new Location(info.robin.getWorld(), 58.5D,
						58.0D, 307.5D);
			}
		} else if (mapId == 5) {
			if (new Random().nextBoolean()) {
				batmanLocation = new Location(Bukkit.getWorld("MallMap"),
						951.5D, 46.0D, 846.5D);
				robinLocation = new Location(Bukkit.getWorld("MallMap"),
						-950.5D, 46.0D, 874.5D);
				catwomanLocation = new Location(Bukkit.getWorld("MallMap"),
						969.5D, 43.0D, 643.5D);
			} else {
				batmanLocation = new Location(Bukkit.getWorld("MallMap"),
						875.5D, 42.0D, 410.5D);
				robinLocation = new Location(Bukkit.getWorld("MallMap"),
						877.5D, 42.0D, 410.5D);
				catwomanLocation = new Location(Bukkit.getWorld("MallMap"),
						1098.5D, 47.0D, 728.5D);
			}
		} else {
			Bukkit.broadcastMessage(ChatColor.DARK_RED
					+ "Restarting Server! REPORT THIS BUG TO Zack or Zach!"
					+ ChatColor.YELLOW + "Unknown Map Id | " + mapId + " | "
					+ mapName);
			game.stop("unknown");
		}

		if (!batmanLocation.getChunk().isLoaded())
			batmanLocation.getChunk().load();
		if (!robinLocation.getChunk().isLoaded())
			robinLocation.getChunk().load();
		info.batman.teleport(batmanLocation);
		info.robin.teleport(robinLocation);

		for (int i = 1; i <= info.badGuys.length; i++)
			if (info.badGuys[(i - 1)] != null) {
				int random = new Random().nextInt(20) + 1;
				try {
					if (mapId == 0) {
						if (info.badGuys[(i - 1)] != null) {
							Location l = null;
							if (random <= 3) {
								String unSplit = tbn.getConfig().getString(
										"Spawns.BadGuys." + String.valueOf(i));
								String[] xyz = unSplit.split(",");
								int x = Integer.valueOf(xyz[0]).intValue();
								int y = Integer.valueOf(xyz[1]).intValue() + 2;
								int z = Integer.valueOf(xyz[2]).intValue();

								l = new Location(info.batman.getWorld(), x, y,
										z);
							} else if (random <= 6) {
								l = new Location(info.batman.getWorld(),
										446.0D, 72.0D, -244.0D);
							} else if (random <= 8) {
								l = new Location(info.batman.getWorld(),
										432.0D, 72.0D, 68.0D);
							} else if (random <= 10) {
								l = new Location(info.batman.getWorld(),
										689.0D, 72.0D, -250.0D);
							} else if (random <= 12) {
								l = new Location(info.batman.getWorld(),
										649.0D, 72.0D, -242.0D);
							} else if (random <= 14) {
								l = new Location(info.batman.getWorld(),
										536.0D, 72.0D, -191.0D);
							} else if (random <= 16) {
								l = new Location(info.batman.getWorld(),
										520.0D, 77.0D, -239.0D);
							} else if (random <= 18) {
								l = new Location(info.batman.getWorld(),
										471.0D, 72.0D, -173.0D);
							} else {
								l = new Location(info.batman.getWorld(),
										448.0D, 72.0D, -79.0D);
							}
							l.getChunk().load();
							for (int j = 0; j < 2; j++)
								info.badGuys[(i - 1)].teleport(l);
						}
					} else if (mapId == 1) {
						if (info.badGuys[(i - 1)] != null) {
							Location l = null;
							if (random <= 3)
								l = new Location(info.batman.getWorld(),
										2438.5D, 196.5D, -886.5D);
							else if (random <= 6)
								l = new Location(info.batman.getWorld(),
										2468.5D, 187.5D, -822.5D);
							else if (random <= 8)
								l = new Location(info.batman.getWorld(),
										2525.5D, 194.5D, -867.5D);
							else if (random <= 10)
								l = new Location(info.batman.getWorld(),
										2433.5D, 187.5D, -812.5D);
							else if (random <= 12)
								l = new Location(info.batman.getWorld(),
										2439.5D, 195.5D, -897.5D);
							else if (random <= 14)
								l = new Location(info.batman.getWorld(),
										2562.5D, 195.5D, -846.5D);
							else if (random <= 16)
								l = new Location(info.batman.getWorld(),
										2488.5D, 195.5D, -866.5D);
							else if (random <= 18)
								l = new Location(info.batman.getWorld(),
										2554.5D, 187.5D, -831.5D);
							else {
								l = new Location(info.batman.getWorld(),
										2524.5D, 187.5D, -866.5D);
							}
							l.getChunk().load();
							info.badGuys[(i - 1)].teleport(l);
						}
					} else if (mapId == 2) {
						if (info.badGuys[(i - 1)] != null) {
							Location l = null;
							if (random <= 3)
								l = new Location(info.batman.getWorld(),
										3057.5D, 33.5D, 194.5D);
							else if (random <= 6)
								l = new Location(info.batman.getWorld(),
										2980.5D, 33.5D, 325.5D);
							else if (random <= 8)
								l = new Location(info.batman.getWorld(),
										3098.5D, 31.5D, 199.5D);
							else if (random <= 10)
								l = new Location(info.batman.getWorld(),
										3146.5D, 33.5D, 42.5D);
							else if (random <= 12)
								l = new Location(info.batman.getWorld(),
										2734.5D, 35.5D, 441.5D);
							else if (random <= 14)
								l = new Location(info.batman.getWorld(),
										2975.5D, 33.5D, 166.5D);
							else if (random <= 16)
								l = new Location(info.batman.getWorld(),
										2765.5D, 33.5D, 429.5D);
							else if (random <= 18)
								l = new Location(info.batman.getWorld(),
										2926.5D, 33.5D, -13.5D);
							else {
								l = new Location(info.batman.getWorld(),
										2914.5D, 22.5D, 186.5D);
							}
							l.getChunk().load();
							info.badGuys[(i - 1)].teleport(l);
						}
					} else if (mapId == 3) {
						if (info.badGuys[(i - 1)] != null) {
							Location l = null;
							if (random <= 3)
								l = new Location(info.batman.getWorld(),
										2730.5D, 143.5D, -1799.5D);
							else if (random <= 6)
								l = new Location(info.batman.getWorld(),
										2699.5D, 44.5D, -1691.5D);
							else if (random <= 8)
								l = new Location(info.batman.getWorld(),
										2864.5D, 44.5D, -2103.5D);
							else if (random <= 10)
								l = new Location(info.batman.getWorld(),
										2883.5D, 44.5D, -1964.5D);
							else if (random <= 12)
								l = new Location(info.batman.getWorld(),
										2722.5D, 47.5D, -2172.5D);
							else if (random <= 14)
								l = new Location(info.batman.getWorld(),
										2612.5D, 44.5D, -1683.5D);
							else if (random <= 16)
								l = new Location(info.batman.getWorld(),
										2553.5D, 47.5D, -1975.5D);
							else if (random <= 18)
								l = new Location(info.batman.getWorld(),
										2879.5D, 44.5D, -1645.5D);
							else {
								l = new Location(info.batman.getWorld(),
										2564.5D, 39.5D, -2100.5D);
							}
							l.getChunk().load();
							info.badGuys[(i - 1)].teleport(l);
						}
					} else if (mapId == 4) {
						if (info.badGuys[(i - 1)] != null) {
							Location l = null;
							if (random <= 3)
								l = new Location(info.batman.getWorld(),
										175.5D, 36.5D, 276.5D);
							else if (random <= 6)
								l = new Location(info.batman.getWorld(),
										-69.5D, 38.5D, 311.5D);
							else if (random <= 8)
								l = new Location(info.batman.getWorld(), 76.5D,
										36.5D, 383.5D);
							else if (random <= 10)
								l = new Location(info.batman.getWorld(), 98.5D,
										36.5D, 227.5D);
							else if (random <= 12)
								l = new Location(info.batman.getWorld(), -0.4D,
										55.5D, 167.5D);
							else if (random <= 14)
								l = new Location(info.batman.getWorld(),
										127.5D, 50.5D, 227.5D);
							else if (random <= 16)
								l = new Location(info.batman.getWorld(),
										-28.5D, 36.5D, 219.5D);
							else if (random <= 18)
								l = new Location(info.batman.getWorld(),
										182.5D, 48.5D, 343.5D);
							else {
								l = new Location(info.batman.getWorld(), 33.5D,
										36.5D, 188.5D);
							}
							l.getChunk().load();
							info.badGuys[(i - 1)].teleport(l);
						}
					} else if ((mapId == 5) && (info.badGuys[(i - 1)] != null)) {
						Location l = null;
						int randInt = new Random().nextInt(6);
						if (randInt == 0)
							l = new Location(info.batman.getWorld(), 978.5D,
									34.5D, 535.5D);
						else if (randInt == 1)
							l = new Location(info.batman.getWorld(), 1020.5D,
									40.5D, 645.5D);
						else if (randInt == 2)
							l = new Location(info.batman.getWorld(), 1104.5D,
									35.5D, 646.5D);
						else if (randInt == 3)
							l = new Location(info.batman.getWorld(), 941.5D,
									34.5D, 582.5D);
						else if (randInt == 4)
							l = new Location(info.batman.getWorld(), 965.5D,
									44.5D, 717.5D);
						else if (randInt == 5) {
							l = new Location(info.batman.getWorld(), 968.5D,
									42.0D, 793.0D);
						}
						l.getChunk().load();
						info.badGuys[(i - 1)].teleport(l);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	}

	/*
	 * public static void teleportAllToWaitingCell(FileConfiguration config) {
	 * Location waitingCellLocation = new Location(
	 * Bukkit.getOnlinePlayers()[0].getWorld(), 2569.0D, 29.0D, -271.0D); for
	 * (Player player : Bukkit.getOnlinePlayers())
	 * player.teleport(waitingCellLocation); }
	 */

	/*
	 * public static void teleportToWaitingCell(Player player) { Location
	 * waitingCellLocation = new Location(player.getWorld(), 2569.0D, 29.0D,
	 * -271.0D); player.teleport(waitingCellLocation); }
	 */

	public static void populateChests(Player player, boolean superChestMode) {
		if (superChestMode) {
			Bukkit.broadcastMessage(ChatColor.GOLD + "SUPER CHEST GAME!");
			Bukkit.broadcastMessage(ChatColor.YELLOW
					+ "All Chests Have Been Spawned!");
		}
		int chestsSpawned = 0;

		for (int i = 1; i <= tbn.getConfig().getInt("Chests.amount"); i++) {
			try {
				String unSplit = tbn.getConfig().getString(
						"Chests." + String.valueOf(i));
				if (unSplit != null) {
					String[] xyz = unSplit.split(",");
					if (xyz != null) {
						int x = 0;
						int y = 0;
						int z = 0;
						try {
							x = Integer.valueOf(xyz[0]).intValue();
							y = Integer.valueOf(xyz[1]).intValue();
							z = Integer.valueOf(xyz[2]).intValue();
						} catch (Exception e) {
							System.out.println("ERR_:" + i);
							x = 0;
							y = 0;
							z = 0;
						}
						if ((x != 0) && (y != 0) && (z != 0)
								&& (player != null)) {
							Location chestLoc = new Location(
									Bukkit.getWorld("SamCity"), x, y, z);
							if (chestLoc != null) {
								int randomChests = Map.mapId == 2 ? 750
										: Map.mapId == 0 ? 350 : 500;
								if ((new Random().nextInt(1000) < randomChests)
										|| (superChestMode)) {
									if (chestLoc.getBlock().getType() != Material.CHEST)
										chestLoc.getBlock().setType(
												Material.CHEST);
									if (chestLoc.getBlock().getType() == Material.CHEST) {
										chestsSpawned++;
										if (chestLoc.getBlock() != null) {
											if ((chestLoc.getBlock().getState() instanceof Chest)) {
												Chest chest = (Chest) chestLoc
														.getBlock().getState();
												if ((chest != null)
														&& (chest
																.getInventory() != null)) {
													Random rand = new Random();
													int random = rand
															.nextInt(20) + 1;
													List diamondLore = new ArrayList();
													diamondLore
															.add(ChatColor.DARK_AQUA
																	+ "Collect The Most "
																	+ ChatColor.AQUA
																	+ "Diamonds "
																	+ ChatColor.DARK_AQUA
																	+ "To Win!");
													chest.getInventory()
															.addItem(
																	new ItemStack[] { game
																			.setName(
																					new ItemStack(
																							Material.DIAMOND,
																							random),
																					ChatColor.AQUA
																							+ "Diamond",
																					diamondLore) });
													int arrowRand = rand
															.nextBoolean() ? rand
															.nextInt(45) + 6
															: 12;
													ItemStack ironSword = rand
															.nextInt(1000) < 50 ? new ItemStack(
															Material.IRON_SWORD)
															: new ItemStack(
																	Material.AIR);
													if ((ironSword.getType() == Material.IRON_SWORD)
															&& (rand.nextInt(1000) < 200)) {
														ironSword
																.addEnchantment(
																		Enchantment.DAMAGE_ALL,
																		rand.nextInt(2) + 1);
													}
													for (int i1 = 0; i1 < random; i1++)
														chest.getInventory()
																.addItem(
																		new ItemStack[] { game
																				.setName(
																						new ItemStack(
																								Material.DIAMOND),
																						ChatColor.AQUA
																								+ "Diamond",
																						diamondLore) });
													if (rand.nextInt(100) <= 12) {
														chest.getInventory()
																.addItem(
																		new ItemStack[] { new ItemStack(
																				Material.ARROW,
																				arrowRand) });
														if (rand.nextInt(100) <= 40)
															chest.getInventory()
																	.addItem(
																			new ItemStack[] { new ItemStack(
																					Material.BOW) });
													}
													chest.getInventory()
															.addItem(
																	new ItemStack[] { ironSword });

													ItemStack potion1 = new ItemStack(
															Material.POTION);
													Potion potion1T = new Potion(
															PotionType.REGEN);
													if (rand.nextBoolean())
														potion1T.setSplash(true);
													potion1T.apply(potion1);

													ItemStack potion2 = new ItemStack(
															Material.POTION);
													Potion potion2T = new Potion(
															PotionType.STRENGTH);
													if (rand.nextBoolean())
														potion2T.setSplash(true);
													potion2T.apply(potion2);

													ItemStack potion3 = new ItemStack(
															Material.POTION);
													Potion potion3T = new Potion(
															PotionType.SPEED);
													if (rand.nextBoolean())
														potion3T.setSplash(true);
													potion3T.apply(potion3);

													ItemStack potion4 = new ItemStack(
															Material.POTION);
													Potion potion4T = new Potion(
															PotionType.INSTANT_HEAL);
													if (rand.nextBoolean())
														potion4T.setSplash(true);
													potion4T.apply(potion4);

													if (rand.nextInt(9) == 1) {
														int rando = rand
																.nextInt(100) + 1;
														if (rando <= 25)
															chest.getInventory()
																	.addItem(
																			new ItemStack[] { potion1 });
														else if (rando <= 50)
															chest.getInventory()
																	.addItem(
																			new ItemStack[] { potion2 });
														else if (rand
																.nextInt(100) <= 75)
															chest.getInventory()
																	.addItem(
																			new ItemStack[] { potion3 });
														else {
															chest.getInventory()
																	.addItem(
																			new ItemStack[] { potion4 });
														}
													}
													if (rand.nextInt(1100) <= 67) {
														int randI = rand
																.nextInt(100);
														if (rand.nextBoolean()) {
															if (randI <= 25)
																chest.getInventory()
																		.addItem(
																				new ItemStack[] { new ItemStack(
																						Material.CHAINMAIL_BOOTS) });
															else if (randI <= 50)
																chest.getInventory()
																		.addItem(
																				new ItemStack[] { new ItemStack(
																						Material.CHAINMAIL_HELMET) });
															else if (randI <= 75)
																chest.getInventory()
																		.addItem(
																				new ItemStack[] { new ItemStack(
																						Material.CHAINMAIL_CHESTPLATE) });
															else if (randI <= 100) {
																chest.getInventory()
																		.addItem(
																				new ItemStack[] { new ItemStack(
																						Material.CHAINMAIL_LEGGINGS) });
															}
														} else if (randI <= 25)
															chest.getInventory()
																	.addItem(
																			new ItemStack[] { new ItemStack(
																					Material.IRON_BOOTS) });
														else if (randI <= 50)
															chest.getInventory()
																	.addItem(
																			new ItemStack[] { new ItemStack(
																					Material.IRON_HELMET) });
														else if (randI <= 75)
															chest.getInventory()
																	.addItem(
																			new ItemStack[] { new ItemStack(
																					Material.IRON_CHESTPLATE) });
														else if (randI <= 100) {
															chest.getInventory()
																	.addItem(
																			new ItemStack[] { new ItemStack(
																					Material.IRON_LEGGINGS) });
														}

													}

													chest.update();
													chest.update(true);
												}
											}
										}
									} else if ((chestLoc != null)
											&& ((chestLoc.getBlock().getState() instanceof Chest))) {
										((Chest) chestLoc.getBlock().getState())
												.getInventory().clear();
										chestLoc.getBlock().setType(
												Material.AIR);
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println("ERR_" + i);
			}
		}
		System.out.println("Chests Spawned: " + chestsSpawned);
	}
}