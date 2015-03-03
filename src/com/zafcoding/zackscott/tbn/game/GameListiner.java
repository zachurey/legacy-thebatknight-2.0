package com.zafcoding.zackscott.tbn.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.zafcoding.zackscott.tbn.Info;
import com.zafcoding.zackscott.tbn.Info.ServerState;
import com.zafcoding.zackscott.tbn.PlayerProfile;
import com.zafcoding.zackscott.tbn.PlayerProfile.PlayType;
import com.zafcoding.zackscott.tbn.TBN;

public class GameListiner implements Listener {

	TBN tbn = TBN.tbn;
	Info info = TBN.info;
	Game game = TBN.game;

	@EventHandler
	public void PlayerFall(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL) {
			if (e.getEntity() instanceof Player) {
				PlayerProfile pp = info.getPP((Player) e.getEntity());
				if (pp.getType() == PlayType.BatNight
						|| pp.getType() == PlayType.BirdBoy) {
					e.setCancelled(true);
				}
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		try {
			tbn.debugMsg("Chest place called!");
			if ((info.joker == event.getPlayer())
					&& event.getBlock().getType() != null
					&& (event.getBlock().getType() == Material.CHEST)) {
				if (!event.getBlock().getLocation().add(1.0D, 0.0D, 0.0D)
						.getBlock().getType().equals(Material.CHEST)) {
					tbn.debugMsg("1 Chest place called!");
					if (!event.getBlock().getLocation().add(0.0D, 0.0D, 1.0D)
							.getBlock().getType().equals(Material.CHEST)) {
						tbn.debugMsg("2 Chest place called!");
						if (!event.getBlock().getLocation()
								.subtract(1.0D, 0.0D, 0.0D).getBlock()
								.getType().equals(Material.CHEST)) {
							tbn.debugMsg("3 Chest place called!");
							if (!event.getBlock().getLocation()
									.subtract(0.0D, 0.0D, 1.0D).getBlock()
									.getType().equals(Material.CHEST)) {
								tbn.debugMsg("4 Chest place called!");
								event.setCancelled(false);
								tbn.debugMsg(event.getBlock().getLocation()
										.toString()
										+ "");
								if (info.joker == null) {
									event.getPlayer().sendMessage(
											ChatColor.RED
													+ "Trap Chest Placed...");
								}
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
				tbn.debugMsg("It's a chest!");
				tbn.debugMsg(((Chest) event.getView().getTopInventory()
						.getHolder()).getBlock().getLocation().toString()
						+ "");
				if (info.fakechests.contains(((Chest) event.getView()
						.getTopInventory().getHolder()).getBlock()
						.getLocation())) {
					tbn.debugMsg("Right location!");
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

	@EventHandler
	public void PlayerDie(PlayerDeathEvent e) {
		if (!(info.getState() == ServerState.In_Game)) {
			e.setDeathMessage("");
			e.getEntity().setHealth(20);
			return;
		}
		if (info.ingame.contains(e.getEntity())) {
			info.outplayer(e.getEntity());
			e.setDeathMessage(ChatColor.RED + ""
					+ e.getEntity().getDisplayName() + " has died! "
					+ ChatColor.GOLD + "" + info.ingame.size() + ChatColor.RED
					+ " remain!");
			e.getEntity().teleport(
					tbn.getPlayerSpawn(info.getActiveWorld().getName(), 0),
					TeleportCause.PLUGIN);
		} else {
		}
	}

	@EventHandler
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
					&& e.getClickedBlock().getType() == Material.CHEST) {
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

	@EventHandler
	public void PlayerHit(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL) {
			if (e.getEntity() instanceof Player) {
				if (info.getPP((Player) e.getEntity()).getType() == PlayType.BatNight
						|| info.getPP((Player) e.getEntity()).getType() == PlayType.BirdBoy) {
					e.setCancelled(true);
				}
			}
		}
	}
}
