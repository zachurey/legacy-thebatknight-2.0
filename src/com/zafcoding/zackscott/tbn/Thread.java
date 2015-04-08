package com.zafcoding.zackscott.tbn;

import me.libraryaddict.disguise.*;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.zafcoding.zackscott.tbn.Info.ServerState;
import com.zafcoding.zackscott.tbn.PlayerProfile.PlayType;
import com.zafcoding.zackscott.tbn.game.GameTime;
import com.zafcoding.zackscott.tbn.lobby.LobbyTime;

public class Thread implements Runnable {

	LobbyTime lt = TBN.lt;
	Info info = TBN.info;
	GameTime gt = TBN.gt;
	TBN tbn = TBN.tbn;

	@Override
	public void run() {
		if (info.getState() == ServerState.In_Game) {
			gt.GameHeartBeat();
		}
		if (info.getState() == ServerState.Pre_Game) {
			lt.LobbyHeartBeat();
		}
		if (info.didsuperChest == false) {
			int rand = (int) (Math.random() * 100 + 1);
			if (rand <= 5) {
				info.didsuperChest = true;
				info.superChest = true;
			} else {
				info.didsuperChest = true;
				info.superChest = false;
				tbn.debugMsg("So close! The int was " + rand);
			}
		}
		for (Player pp : info.players) {
			pp.setFoodLevel(20);
			pp.setSaturation(20f);
			if (info.getPP(pp).getDis()
					&& info.getPP(pp).getType() == PlayType.KittyKat) {
				if (pp.getItemInHand() == null) {
					DisguiseAPI.undisguiseToAll(pp);
					pp.sendMessage(ChatColor.RED + "" + ChatColor.BOLD
							+ "You have been undisguised!");
					info.getPP(pp).setDis(false);
				}
				if (pp.getItemInHand() != null
						&& !(pp.getItemInHand().getType() == Material.MILK_BUCKET)) {
					DisguiseAPI.undisguiseToAll(pp);
					pp.sendMessage(ChatColor.RED + "" + ChatColor.BOLD
							+ "You have been undisguised!");
					info.getPP(pp).setDis(false);
				}
			}
			if (info.getPP(pp).getType() == PlayType.Puffin) {
				if (pp.getItemInHand() != null
						&& pp.getItemInHand().getType() == Material.RAW_CHICKEN) {
					if (info.getPP(pp).getRap() == false) {
						DisguiseAPI.disguiseToAll(pp, new MobDisguise(
								DisguiseType.CHICKEN));
						pp.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD
								+ "You are now disguised as a chicken!");
						info.getPP(pp).setRap(true);
					}
				}
			}
			if (info.getPP(pp).getRap()
					&& info.getPP(pp).getType() == PlayType.Puffin) {
				if (pp.getItemInHand() == null) {
					DisguiseAPI.undisguiseToAll(pp);
					pp.sendMessage(ChatColor.RED + "" + ChatColor.BOLD
							+ "You have been undisguised!");
					info.getPP(pp).setRap(false);
				}
				if (pp.getItemInHand() != null
						&& !(pp.getItemInHand().getType() == Material.RAW_CHICKEN)) {
					DisguiseAPI.undisguiseToAll(pp);
					pp.sendMessage(ChatColor.RED + "" + ChatColor.BOLD
							+ "You have been undisguised!");
					info.getPP(pp).setRap(false);
				}
			}
			if (info.getPP(pp).getType() == PlayType.KittyKat) {
				if (pp.getItemInHand() != null
						&& pp.getItemInHand().getType() == Material.MILK_BUCKET) {
					if (info.getPP(pp).getDis() == false) {
						DisguiseAPI.disguiseToAll(pp, new MobDisguise(
								DisguiseType.OCELOT));
						pp.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD
								+ "You are now disguised as a cat! (Ocelot)");
						info.getPP(pp).setDis(true);
					}
				}
			}
			if (pp.getItemInHand().getType() == Material.FEATHER
					&& !(pp.getGameMode() == GameMode.CREATIVE)) {
				pp.setAllowFlight(true);
				pp.setFlying(true);
			}
			if (!(pp.getItemInHand().getType() == Material.FEATHER)
					&& !(pp.getGameMode() == GameMode.CREATIVE)
					&& !(pp.getGameMode() == GameMode.SPECTATOR)) {
				pp.setAllowFlight(false);
				pp.setFlying(false);
			}
		}

	}

}
