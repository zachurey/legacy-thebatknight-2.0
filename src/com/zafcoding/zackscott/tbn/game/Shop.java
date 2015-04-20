package com.zafcoding.zackscott.tbn.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.zafcoding.zackscott.tbn.TBN;

public class Shop implements Listener {

	TBN tbn = TBN.tbn;

	public void openInventoryBad(Player pl) {
		Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RED
				+ "Shop: " + ChatColor.GRAY + "BadGuy");
		List<String> batblock = new ArrayList<String>();
		List<String> invis = new ArrayList<String>();
		batblock.add(ChatColor.RED + "Block All Hero Compass'!");
		batblock.add(ChatColor.LIGHT_PURPLE + "(30 seconds)");
		invis.add(ChatColor.RED + "Get an invisablity potion!");
		batblock.add(ChatColor.LIGHT_PURPLE + "(40 seconds)");
		inv.addItem(new ItemStack[] { setName(Material.COMPASS,
				ChatColor.DARK_RED + "BatBlocker", batblock) });
	}

	public void openInventoryGood(Player pl) {

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

	@EventHandler
	public void onPlayerClick(InventoryClickEvent e) {
		if (ChatColor.stripColor(e.getInventory().getName()).contains("Shop")) {
			if (ChatColor.stripColor(e.getInventory().getName()).contains(
					"BadGuy")) {
				if (tbn.info.getPP((Player) e.getWhoClicked()).shopuse) {
					e.getWhoClicked().closeInventory();
					((Player) e.getWhoClicked()).sendMessage(ChatColor.RED
							+ "You already have something active!");
				}
			}
		}
	}

}
