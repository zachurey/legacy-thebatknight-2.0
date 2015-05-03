package com.zafcoding.zackscott.tbn.game;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.zafcoding.zackscott.tbn.Info;
import com.zafcoding.zackscott.tbn.Info.ServerState;
import com.zafcoding.zackscott.tbn.PlayerProfile.PlayType;
import com.zafcoding.zackscott.tbn.TBN;

public class GameTime {

	TBN tbn = TBN.tbn;
	Info info = TBN.info;
	Game game = TBN.game;
	int aw = 600;
	int ot = 0;
	boolean now = false;
	boolean sh = false;
	Timer timer = new Timer();
	boolean other = false;

	public void GameHeartBeat() {
		// TODO: test this
		if (other) {
			return;
		}
		timeTest();
		if (info.poo) {
			if (info.batman == null && info.robin == null) {
				game.endGame(1);
			}
			if (info.puffin == null && info.joker == null
					&& info.badguys.isEmpty()) {
				game.endGame(0);
			}
			tbn.debugMsg("The current game info.getGameTime() is: "
					+ info.getGameTime());
			info.setGameTime(info.getGameTime() - 1);
			broadCastShort(info.getGameTime());
		}
	}

	public void GameHeartBeater() {
		// TODO: test this
		timeTest();
			if (info.batman == null && info.robin == null) {
				game.endGame(1);
			}
			if (info.puffin == null && info.joker == null
					&& info.badguys.isEmpty()) {
				game.endGame(0);
			}
			tbn.debugMsg("1 The current game info.getGameTime() is: "
					+ info.getGameTime());
			info.setGameTime(info.getGameTime() - 1);
			broadCastShort(info.getGameTime());
	}

	private void broadCastShort(int gameTime) {
		if (info.getGameTime() == 120 || info.getGameTime() == 180
				|| info.getGameTime() == 240 || info.getGameTime() == 300
				|| info.getGameTime() == 360 || info.getGameTime() == 420
				|| info.getGameTime() == 480 || info.getGameTime() == 540
				|| info.getGameTime() == 600) {
			info.broadCast(ChatColor.RED + "" + (gameTime / 60)
					+ ChatColor.GOLD + " minutes till the villians win!");
		}
		if (gameTime == 60) {
			info.broadCast(ChatColor.RED + "1" + ChatColor.GOLD
					+ " minute till the villians win!");
		}
		if (gameTime <= 10) {
			info.broadCast(ChatColor.RED + "" + gameTime + ChatColor.GOLD
					+ " seconds till the villians win!");
		}
		if (gameTime <= 0) {
			game.endGame(1);
		}
	}

	public void timeTest() {
		if (now) {
			if (aw == info.getGameTime()) {
				if (sh) {
					if (info.getGameTime() == ot) {
						System.out.println("She broke!");
						if(other){
							return;
						}
						other = true;
						timer.schedule(new SayHello(), 0, 1000);
						now = false;
						sh = false;
						return;
					} else {
						sh = false;
						ot = 0;
						return;
					}
				} else {
					sh = true;
					ot = info.getGameTime();
					return;
				}
			} else {
				now = false;
				sh = false;
				return;
			}
		} else {
			aw = info.getGameTime();
			now = true;
			sh = false;
			return;
		}
	}
	
	public void end(){
		timer.cancel();
	}
}

class SayHello extends TimerTask {
	public void run() {
		TBN tbn = TBN.tbn;
		tbn.gt.GameHeartBeater();
	}
}