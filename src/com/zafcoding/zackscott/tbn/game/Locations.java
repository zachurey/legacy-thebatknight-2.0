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
		batmanLocation = new Location(info.batman.getWorld(), tbn.getConfig()
				.getInt("Spawns." + info.getActiveWorld().getName()
						+ ".Batman.x"), tbn.getConfig().getInt(
				"Spawns." + info.getActiveWorld().getName() + ".Batman.y"), tbn
				.getConfig().getInt(
						"Spawns." + info.getActiveWorld().getName()
								+ ".Batman.z"));
		robinLocation = new Location(info.robin.getWorld(), tbn.getConfig()
				.getInt("Spawns." + info.getActiveWorld().getName()
						+ ".Robin.x"), tbn.getConfig().getInt(
				"Spawns." + info.getActiveWorld().getName() + ".Robin.y"), tbn
				.getConfig().getInt(
						"Spawns." + info.getActiveWorld().getName()
								+ ".Robin.z"));
		catwomanLocation = new Location(info.robin.getWorld(), 510.0D, 147.0D,
				-36.0D);

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
					if (info.badGuys[(i - 1)] != null) {
						Location l = null;
						String unSplit = tbn.getConfig().getString(
								"Spawns." + info.getActiveWorld().getName()
										+ ".BadGuys." + String.valueOf(i));
						String[] xyz = unSplit.split(",");
						int x = Integer.valueOf(xyz[0]).intValue();
						int y = Integer.valueOf(xyz[1]).intValue() + 2;
						int z = Integer.valueOf(xyz[2]).intValue();

						l = new Location(info.batman.getWorld(), x, y, z);
						l.getChunk().load();
						for (int j = 0; j < 2; j++)
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

	public static int populateChests(boolean superChestMode) {
		if (superChestMode) {
			info.broadCast(ChatColor.GOLD + "SUPER CHEST GAME!");
			info.broadCast(ChatColor.YELLOW + "All Chests Have Been Spawned!");
		}
		int chestsSpawned = 0;

		for (int i = 1; i <= tbn.getConfig().getInt("Chests.amount"); i++) {
			try {
				String unSplit = tbn.getConfig().getString(
						"Chests." + info.getActiveWorld().getName() + "."
								+ String.valueOf(i));
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
						if ((x != 0) && (y != 0) && (z != 0)) {
							Location chestLoc = new Location(
									info.getActiveWorld(), x, y, z);
							if (chestLoc != null) {
								int randomChests = Map.mapId == 2 ? 750
										: Map.mapId == 0 ? 350 : 500;
								if ((new Random().nextInt(1000) < randomChests)
										|| (superChestMode)) {
									if (chestLoc.getBlock().getType() != Material.CHEST) {
										chestLoc.getBlock().setType(
												Material.CHEST);
										if (chestLoc.getBlock().getType() == Material.CHEST) {
											chestsSpawned++;
											info.chests.add(chestLoc);
											if (chestLoc.getBlock() != null) {
												if ((chestLoc.getBlock()
														.getState() instanceof Chest)) {
													Chest chest = (Chest) chestLoc
															.getBlock()
															.getState();
													if ((chest != null)
															&& (chest
																	.getInventory() != null)) {
														Random rand = new Random();
														int random = rand
																.nextInt(tbn.getConfig().getInt("MaxDiamonds")) + tbn.getConfig().getInt("MinDiamonds");
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
																.nextInt(20) + 6
																: 12;
														ItemStack ironSword = rand
																.nextInt(1000) < 25 ? new ItemStack(
																Material.IRON_SWORD,
																1)
																: new ItemStack(
																		Material.AIR);
														if ((ironSword
																.getType() == Material.IRON_SWORD)
																&& (rand.nextInt(1000) < 200)) {
															ironSword
																	.addEnchantment(
																			Enchantment.DAMAGE_ALL,
																			rand.nextInt(4) + 1);
															if (ironSword
																	.getType() == Material.IRON_SWORD) {
																if (contains(
																		chest.getInventory(),
																		Material.IRON_SWORD)) {
																	tbn.debugMsg("Adding it!");
																	chest.getInventory()
																			.addItem(
																					new ItemStack[] { ironSword });
																}
															}
														}
														if (rand.nextInt(100) <= 12) {
															if (contains(
																	chest.getInventory(),
																	Material.ARROW)) {
																if ((Math
																		.random() * 2 + 1) == 1) {
																	ItemStack op = new ItemStack(
																			Material.ARROW,
																			arrowRand);
																	chest.getInventory()
																			.addItem(
																					op);
																}
															}
														}
														if (rand.nextInt(100) <= 10) {
															if (contains(
																	chest.getInventory(),
																	Material.BOW)) {
																if ((Math
																		.random() * 30 + 1) >= 20) {
																	ItemStack it = new ItemStack(
																			Material.BOW,
																			1);
																	tbn.debugMsg("Adding it! ("
																			+ it.getAmount()
																			+ ")");
																	chest.getInventory()
																			.addItem(
																					it);
																}
															}
														}

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
															if (rando <= 25) {
																if (contains(
																		chest.getInventory(),
																		potion1.getType())) {
																	chest.getInventory()
																			.addItem(
																					new ItemStack[] { potion1 });
																} else if (rando <= 50) {
																	if (contains(
																			chest.getInventory(),
																			potion2.getType())) {
																		chest.getInventory()
																				.addItem(
																						new ItemStack[] { potion2 });
																	}
																} else if (rand
																		.nextInt(100) <= 75) {
																	if (contains(
																			chest.getInventory(),
																			potion3.getType())) {
																		chest.getInventory()
																				.addItem(
																						new ItemStack[] { potion3 });
																	}
																} else {
																	if (contains(
																			chest.getInventory(),
																			potion4.getType())) {
																		chest.getInventory()
																				.addItem(
																						new ItemStack[] { potion4 });
																	}
																}
															}
															if (rand.nextInt(1100) <= 67) {
																int randI = rand
																		.nextInt(100);
																if (rand.nextBoolean()) {
																	if (randI <= 25) {
																		if (contains(
																				chest.getInventory(),
																				Material.CHAINMAIL_BOOTS)) {
																			chest.getInventory()
																					.addItem(
																							new ItemStack[] { new ItemStack(
																									Material.CHAINMAIL_BOOTS) });
																		}
																	} else if (randI <= 50) {
																		if (contains(
																				chest.getInventory(),
																				Material.CHAINMAIL_HELMET)) {
																			chest.getInventory()
																					.addItem(
																							new ItemStack[] { new ItemStack(
																									Material.CHAINMAIL_HELMET) });
																		}
																	} else if (randI <= 75) {
																		if (contains(
																				chest.getInventory(),
																				Material.CHAINMAIL_CHESTPLATE)) {
																			chest.getInventory()
																					.addItem(
																							new ItemStack[] { new ItemStack(
																									Material.CHAINMAIL_CHESTPLATE) });
																		}
																	} else if (randI <= 100) {
																		if (contains(
																				chest.getInventory(),
																				Material.CHAINMAIL_LEGGINGS)) {
																			chest.getInventory()
																					.addItem(
																							new ItemStack[] { new ItemStack(
																									Material.CHAINMAIL_LEGGINGS) });
																		}
																	}
																} else if (randI <= 25) {
																	if (contains(
																			chest.getInventory(),
																			Material.IRON_BOOTS)) {
																		chest.getInventory()
																				.addItem(
																						new ItemStack[] { new ItemStack(
																								Material.IRON_BOOTS) });
																	}
																} else if (randI <= 50) {
																	if (contains(
																			chest.getInventory(),
																			Material.IRON_HELMET)) {
																		chest.getInventory()
																				.addItem(
																						new ItemStack[] { new ItemStack(
																								Material.IRON_HELMET) });
																	}
																} else if (randI <= 75) {
																	if (contains(
																			chest.getInventory(),
																			Material.IRON_CHESTPLATE)) {
																		chest.getInventory()
																				.addItem(
																						new ItemStack[] { new ItemStack(
																								Material.IRON_CHESTPLATE) });
																	}
																} else if (randI <= 100) {
																	if (contains(
																			chest.getInventory(),
																			Material.IRON_LEGGINGS)) {
																		chest.getInventory()
																				.addItem(
																						new ItemStack[] { new ItemStack(
																								Material.IRON_LEGGINGS) });
																	}
																}

															}

															chest.update();
															chest.update(true);
														}
													}
												}
											} else if ((chestLoc != null)
													&& ((chestLoc.getBlock()
															.getState() instanceof Chest))) {
												((Chest) chestLoc.getBlock()
														.getState())
														.getInventory().clear();
												chestLoc.getBlock().setType(
														Material.AIR);
											}
										}
									} else {
										tbn.debugMsg("Skiped 1");
									}
								}
							}
						}
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage() + " ERR_" + i);
			}
		}
		System.out.println("Chests Spawned: " + chestsSpawned);
		return chestsSpawned;
	}

	private static boolean contains(Inventory inv, Material is) {
		for (ItemStack it : inv.getContents()) {
			if (it != null) {
				if (it.getType() == is) {
					tbn.debugMsg(is + " is not a thingy!");
					return false;
				}
			} else {
			}
		}
		tbn.debugMsg(is + " is a thingy!");
		return true;
	}

}