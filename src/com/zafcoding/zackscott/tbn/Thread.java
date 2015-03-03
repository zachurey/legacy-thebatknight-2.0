package com.zafcoding.zackscott.tbn;

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
			if (rand <= 2) {
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
			if (info.getState() == ServerState.In_Game || tbn.debugMode
					|| pp.getGameMode() == GameMode.CREATIVE) {
				if (info.isSpect(pp)) {
					pp.setAllowFlight(true);
					pp.setFlying(true);
					return;
				}
				if (pp.getItemInHand().getType() == Material.FEATHER) {
					pp.setAllowFlight(true);
					pp.setFlying(true);
					return;
				}
				pp.setAllowFlight(false);
				pp.setFlying(false);
			} else {
				if (!(pp.getGameMode() == GameMode.CREATIVE)
						&& info.getPP(pp).getType() == PlayType.Villan) {
					pp.setFlying(false);
				}
			}
		}

	}

}
