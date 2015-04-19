package com.zafcoding.zackscott.tbn.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.zafcoding.zackscott.tbn.Info;
import com.zafcoding.zackscott.tbn.Info.ServerState;
import com.zafcoding.zackscott.tbn.ParticleEffect;
import com.zafcoding.zackscott.tbn.PlayerProfile;
import com.zafcoding.zackscott.tbn.PlayerProfile.PlayType;
import com.zafcoding.zackscott.tbn.TBN;

public class GameListiner implements Listener {

	TBN tbn = TBN.tbn;
	Info info = TBN.info;
	Game game = TBN.game;
	ParticleEffect ef;

	@EventHandler
	public void PlayerFall(EntityDamageEvent e) {
		if (e.getEntity().getType() == EntityType.PLAYER) {
			if (info.getGameTime() >= ((tbn.getConfig().getInt("MatchLengh") * 60) - 3)) {
				e.setCancelled(true);
			}
			if (!(info.getState() == ServerState.In_Game)) {
				e.setCancelled(true);
			}
			if (e.getCause() == DamageCause.FALL) {
				if (e.getEntity() instanceof Player) {
					PlayerProfile pp = info.getPP((Player) e.getEntity());
					if (pp == null) {
						return;
					}
					if (pp.getType() == PlayType.BatNight
							|| pp.getType() == PlayType.BirdBoy
							|| pp.getType() == PlayType.KittyKat) {
						e.setCancelled(true);
					}
				}
			}
		} else {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		try {
			if ((info.joker == event.getPlayer())
					&& event.getBlock().getType() != null
					&& (event.getBlock().getType() == Material.CHEST)) {
				if (!event.getBlock().getLocation().add(1.0D, 0.0D, 0.0D)
						.getBlock().getType().equals(Material.CHEST)) {
					if (!event.getBlock().getLocation().add(0.0D, 0.0D, 1.0D)
							.getBlock().getType().equals(Material.CHEST)) {
						if (!event.getBlock().getLocation()
								.subtract(1.0D, 0.0D, 0.0D).getBlock()
								.getType().equals(Material.CHEST)) {
							if (!event.getBlock().getLocation()
									.subtract(0.0D, 0.0D, 1.0D).getBlock()
									.getType().equals(Material.CHEST)) {
								event.setCancelled(false);
								info.joker.sendMessage(ChatColor.RED
										+ "Trap Chest Placed...");
								event.getBlockPlaced().setType(Material.CHEST);
								info.fakechests.add(event.getBlock()
										.getLocation());
							} else {
								event.setCancelled(true);
							}
						} else
							event.setCancelled(true);
					} else
						event.setCancelled(true);
				} else
					event.setCancelled(true);
			}
			if (event.getBlock().getType() == Material.TNT) {
				event.getBlock()
						.getWorld()
						.spawnEntity(event.getBlock().getLocation(),
								EntityType.PRIMED_TNT);
				event.setCancelled(false);
				event.getBlock().setType(Material.AIR);
			}
		} catch (Exception localException) {
		}
	}

	@EventHandler
	public void onContainerOpen(InventoryOpenEvent event) {
		try {
			if (info.isSpect((Player) event.getPlayer())) {
				event.setCancelled(true);
				event.getPlayer().closeInventory();
				return;
			}

			if (event.getInventory().getType() == InventoryType.CHEST) {
				if (info.getPP((Player) event.getPlayer()).getType() == PlayType.BatNight
						|| info.getPP((Player) event.getPlayer()).getType() == PlayType.BirdBoy) {
					event.setCancelled(true);
				}
				if (info.fakechests.contains(((Chest) event.getView()
						.getTopInventory().getHolder()).getBlock()
						.getLocation())) {
					Chest c = (Chest) event.getView().getTopInventory()
							.getHolder();
					event.setCancelled(true);
					c.getInventory().clear();
					c.getBlock().setType(Material.AIR);
					c.getBlock().getWorld()
							.createExplosion(c.getLocation(), 2.0F);
					c.getBlock()
							.getWorld()
							.playEffect(c.getBlock().getLocation(),
									Effect.ENDER_SIGNAL, 1);
					if (info.joker != null)
						info.joker.sendMessage(ChatColor.GOLD
								+ "POP GOES THE VILLAIN!");
					((Player) event.getPlayer()).sendMessage(ChatColor.RED
							+ "IT'S A TRAP!");
					((Player) event.getPlayer()).damage(4.0);
					((Player) event.getPlayer())
							.addPotionEffect(new PotionEffect(
									PotionEffectType.BLINDNESS, 5, 1));
					((Player) event.getPlayer())
							.addPotionEffect(new PotionEffect(
									PotionEffectType.CONFUSION, 6, 1));
					((Player) event.getPlayer())
							.addPotionEffect(new PotionEffect(
									PotionEffectType.SLOW, 10, 1));
					info.removeArmour((Player) event.getPlayer());
				}
			}
		} catch (Exception localException) {
		}
	}

	@SuppressWarnings("static-access")
	@EventHandler
	public void PlayerDie(PlayerDeathEvent e) {
		if (!(info.getState() == ServerState.In_Game)) {
			e.setDeathMessage("");
			e.getEntity().setHealth(20);
			return;
		}
		if (e.getEntity().getKiller() instanceof Player) {
			PlayerProfile pp = info.getPP(e.getEntity().getKiller());
			pp.setKills(pp.getKills() + 1);
			info.coin.put(pp.getPlayer().getUniqueId().toString(),
					info.coin.get(pp.getPlayer().getUniqueId().toString() + 1));
			// pp.getPlayer().sendMessage(ChatColor.AQUA + "+1 Bat Bullion");
		}
		info.outplayer(e.getEntity());
		int amount = game.randInt(tbn.getConfig().getInt("MinDeath"), tbn
				.getConfig().getInt("MaxDeath"));
		List diamondLore = new ArrayList();
		diamondLore.add(ChatColor.DARK_AQUA + "Collect The Most "
				+ ChatColor.AQUA + "Diamonds " + ChatColor.DARK_AQUA
				+ "To Win!");
		ItemStack ads = game.setName(new ItemStack(Material.DIAMOND, amount),
				ChatColor.AQUA + "Diamond", diamondLore);
		e.getEntity().getWorld()
				.dropItemNaturally(e.getEntity().getLocation(), ads);
		e.setDeathMessage(ChatColor.RED + ""
				+ ChatColor.stripColor(e.getEntity().getDisplayName())
				+ " has died! " + ChatColor.GOLD + "" + info.getPlayerCount()
				+ ChatColor.RED + " remain!");
		e.getEntity().teleport(
				tbn.getPlayerSpawn(info.getActiveWorld().getName(), 0),
				TeleportCause.PLUGIN);
		displayPart(e.getEntity());
		info.checkEnd();

	}

	@EventHandler(ignoreCancelled = true)
	public void PlayerInteract(PlayerInteractEvent e) {
		PlayerProfile pe = info.getPP(e.getPlayer());
		if (pe != null && pe.isDead()) {
			if (pe.getPlayer().getItemInHand() != null
					&& pe.getPlayer().getItemInHand().getType() == Material.COMPASS) {
				List ss = new ArrayList<>();
				for (Player p : info.getPlayers()) {
					tbn.inv.addItem(game.setName(new ItemStack(Material.SKULL),
							p.getDisplayName() + "", ss));
				}
				pe.getPlayer().openInventory(tbn.inv);
				return;
			}
		} else {
			if (e.getClickedBlock() != null
					&& e.getClickedBlock().getType() == Material.ENCHANTMENT_TABLE) {
				e.setCancelled(true);
				pe.getPlayer().sendMessage(ChatColor.RED + "Just don't");
			}
			if (e.getClickedBlock() != null
					&& e.getClickedBlock().getType() == Material.CHEST
					&& !info.fakechests.contains(e.getClickedBlock()
							.getLocation())
					&& !(info.getPP(e.getPlayer()).getType() == PlayType.BatNight || info
							.getPP(e.getPlayer()).getType() == PlayType.BirdBoy)) {
				Chest ch = (Chest) e.getClickedBlock().getState();
				ItemStack[] ss = ch.getInventory().getContents();
				for (ItemStack ii : ss) {
					if (ii != null) {
						e.getPlayer()
								.getWorld()
								.dropItemNaturally(
										e.getClickedBlock().getLocation(), ii);
						ch.getInventory().remove(ii);
					}
				}
				e.getClickedBlock().setType(Material.AIR);
			}
			try {
				if (e.getItem().getType() != null
						&& e.getItem().getType() == Material.RAW_FISH) {
					e.setCancelled(true);
					Location loc = e.getClickedBlock().getLocation();
					loc.setY(e.getClickedBlock().getLocation().getY() + 2);
					final Entity ee = e.getClickedBlock().getWorld()
							.spawnEntity(loc, EntityType.OCELOT);
					Ocelot oc = (Ocelot) ee;
					oc.setAdult();
					oc.setBreed(false);
					if (tbn.zack) {
						int rand = game.randInt(1, 2);
						if (rand == 1) {
							oc.setCustomName("Otto");
						}
						if (rand == 2) {
							oc.setCustomName("Egon");
						}
					}
					Bukkit.getServer().getScheduler()
							.scheduleSyncDelayedTask(tbn, new Runnable() {
								public void run() {
									ee.remove();
								}
							}, 260L);
				}
			} catch (Exception ee) {
			}
			try {
				if (e.getItem().getType() != null
						&& e.getItem().getType() == Material.EGG) {
					Location loc = e.getClickedBlock().getLocation();
					loc.setY(e.getClickedBlock().getLocation().getY() + 2);
					final Entity ee = e.getClickedBlock().getWorld()
							.spawnEntity(loc, EntityType.CHICKEN);
					Chicken chi = (Chicken) ee;
					chi.setAdult();
					chi.setBreed(false);
					if (tbn.mac) {
						int iq = game.randInt(6, 25);
						chi.setCustomName("Murphy " + iq);
					}
					Bukkit.getServer().getScheduler()
							.scheduleSyncDelayedTask(tbn, new Runnable() {
								public void run() {
									ee.remove();
								}
							}, 260L);
				}
			} catch (NullPointerException ee) {
			}
			try {
				if ((e.getItem().getType() == Material.MILK_BUCKET)
						|| (e.getItem().getType() == Material.RAW_FISH)) {
					e.setUseItemInHand(Event.Result.DENY);
					e.setCancelled(true);
				}
			} catch (NullPointerException eee) {
			}
		}
	}

	@EventHandler
	public void a1308a(PlayerInteractEvent event) {
		try {
			event.setCancelled(false);
			if ((event.getItem().getType() == Material.IRON_HOE)
					&& ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event
							.getAction() == Action.RIGHT_CLICK_BLOCK))) {
				event.setCancelled(false);
				if (info.h == 1) {
					event.getPlayer().sendMessage(
							ChatColor.DARK_AQUA + "" + ChatColor.BOLD
									+ "Cooling down...");
					return;
				}
				info.h = 1;
				final Player player = event.getPlayer();
				ItemStack item = new ItemStack(Material.NETHER_STAR);
				final Item i = player.getWorld().dropItem(player.getLocation(),
						item);
				info.puffin.sendMessage(ChatColor.DARK_AQUA + "Shoot!");
				i.setPickupDelay(50000);
				tbn.getServer().getScheduler()
						.scheduleSyncDelayedTask(tbn, new Runnable() {
							public void run() {
								i.getLocation().getWorld()
										.createExplosion(i.getLocation(), 3.8f);
								i.remove();
								info.puffin.sendMessage(ChatColor.DARK_AQUA
										+ "" + ChatColor.BOLD + "Boom!");
								info.h = 0;
							}
						}, 40L);
			}
			if ((event.getItem().getType() == Material.IRON_HOE)
					&& ((event.getAction() == Action.LEFT_CLICK_AIR) || (event
							.getAction() == Action.LEFT_CLICK_BLOCK))) {
				event.setCancelled(false);
				if (info.h == 1) {
					event.getPlayer().sendMessage(
							ChatColor.DARK_AQUA + "" + ChatColor.BOLD
									+ "Cooling down...");
					return;
				}
				info.h = 1;
				final Player player = event.getPlayer();
				ItemStack item = new ItemStack(Material.NETHER_STAR);
				final Item i = player.getWorld().dropItem(player.getLocation(),
						item);
				i.setVelocity(player.getLocation().getDirection()
						.multiply(3.0F));
				info.puffin.sendMessage(ChatColor.DARK_AQUA + "Shoot!");
				i.setPickupDelay(50000);
				tbn.getServer().getScheduler()
						.scheduleSyncDelayedTask(tbn, new Runnable() {
							public void run() {
								i.getLocation().getWorld()
										.createExplosion(i.getLocation(), 3.8f);
								i.remove();
								info.puffin.sendMessage(ChatColor.DARK_AQUA
										+ "" + ChatColor.BOLD + "Boom!");
								info.h = 0;
							}
						}, 40L);
			}
		} catch (NullPointerException e) {

		}
	}

	@EventHandler
	public void PlayerClick(InventoryClickEvent e) {
		if (info.getPP((Player) e.getWhoClicked()).isDead()
				&& e.getCursor() != null
				&& e.getCursor().getType() == Material.SKULL) {
			Player pp = Bukkit.getPlayer(ChatColor.stripColor(e.getCursor()
					.getItemMeta().getDisplayName()));
			e.getWhoClicked().closeInventory();
			e.getWhoClicked().teleport(pp);
			((Player) e.getWhoClicked()).sendMessage(ChatColor.GREEN
					+ "You have been teleported to " + pp.getDisplayName());
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void handleExplosion(EntityExplodeEvent e) {
		if (e.getEntity().getType() != EntityType.PLAYER) {
			e.setCancelled(true);
		}

		double x = 0;
		double y = 0;
		double z = 0;
		Location eLoc;
		if (e.getEntity() == null) {
			eLoc = e.getLocation();
		} else {
			eLoc = e.getEntity().getLocation();
		}
		World w = eLoc.getWorld();
		for (int i = 0; i < e.blockList().size(); i++) {
			Block b = e.blockList().get(i);
			Location bLoc = b.getLocation();
			x = bLoc.getX() - eLoc.getX();
			y = bLoc.getY() - eLoc.getY() + 0.5;
			z = bLoc.getZ() - eLoc.getZ();
			FallingBlock fb = w.spawnFallingBlock(bLoc, b.getType(),
					(byte) b.getData());
			fb.setDropItem(false);
			fb.setVelocity(new Vector(x, y, z));
		}
	}

	// TODO:
	@EventHandler
	public void ProjectHit(ProjectileHitEvent event) {
		if (event.getEntityType() == EntityType.SNOWBALL) {
			if (event.getEntity().getShooter() instanceof Player) {
				Player pp = (Player) event.getEntity().getShooter();
				PlayerProfile ppp = info.getPP(pp);
				if (ppp.getType() == PlayType.Puffin && info.puffin == pp) {
					event.getEntity()
							.getLocation()
							.getWorld()
							.createExplosion(event.getEntity().getLocation(),
									4f);
					info.puffin.sendMessage(ChatColor.DARK_AQUA + "Boom!");
				}
			}
		}
	}

	@EventHandler
	public void PlayerDdammam(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			if (e.getDamager() instanceof Player) {
				PlayerProfile pp = info.getPP((Player) e.getEntity());
				PlayerProfile ep = info.getPP((Player) e.getDamager());
				if (pp.getType() == PlayType.BatNight
						&& pp.getType() == PlayType.BirdBoy) {
					e.setCancelled(true);
				}
				if (pp.getType() == PlayType.BirdBoy
						&& pp.getType() == PlayType.BatNight) {
					e.setCancelled(true);
				}
				if (info.batman == pp && info.robin == ep) {
					ep.getPlayer()
							.sendMessage(
									ChatColor.RED
											+ "Why would you want to hurt TheBatKnight!?");
					e.setCancelled(true);
				}
				if (info.batman == ep && info.robin == pp) {
					ep.getPlayer().sendMessage(
							ChatColor.RED
									+ "Why would you want to hurt Robin!?");
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void PlayerThrow123(ProjectileLaunchEvent e) {
		if (e.getEntityType() == EntityType.EGG) {
			if (e.getEntity().getShooter() instanceof Player) {
				if (info.getPP((Player) e.getEntity().getShooter()).getType() == PlayType.Puffin
						|| info.puffin == (Player) e.getEntity().getShooter()) {
					Player p = (Player) e.getEntity().getShooter();
					e.setCancelled(true);
					p.getInventory().addItem(
							game.setName(Material.EGG, ChatColor.YELLOW
									+ "Minion Spawner", null));
					p.updateInventory();
				}
			}
		}
	}

	@EventHandler
	public void b893(PlayerBucketEmptyEvent e) {
		if (e.getBucket() == Material.MILK_BUCKET) {
			if (info.getPP(e.getPlayer()).getType() == PlayType.KittyKat) {
				e.setCancelled(true);
			}
		}
	}

	public void displayPart(Player p) {
		if (p.hasPermission("tbk.pro")) {
			ef.SPELL_WITCH.displayz(.5f, 1f, .5f, 1f, 150, p.getLocation(),
					(Player[]) Bukkit.getOnlinePlayers().toArray());
		}
	}

}
