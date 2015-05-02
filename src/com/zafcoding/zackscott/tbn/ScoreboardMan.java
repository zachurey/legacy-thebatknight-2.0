package com.zafcoding.zackscott.tbn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.zafcoding.zackscott.tbn.Info.ServerState;

public class ScoreboardMan {

	TBN tbn = TBN.tbn;
	Info info = tbn.info;

	public void updateScoreBoard() {
		for (Player pp : Bukkit.getOnlinePlayers()) {
			if (info.getPP(pp).sb == null) {
				ScoreboardManager manage = Bukkit.getScoreboardManager();
				Scoreboard board = manage.getNewScoreboard();
				info.getPP(pp).sb = board;
			}
			Scoreboard board = info.getPP(pp).sb;
			if (board.getObjective("test") == null) {
				Objective obj = board.registerNewObjective("test", "dummy");
				obj.setDisplaySlot(DisplaySlot.SIDEBAR);
				obj.setDisplayName(ChatColor.GOLD + "TBK");
			}
			Objective obj = board.getObjective("test");
			if (info.getState() == ServerState.In_Game) {
				Score score = obj.getScore(ChatColor.GREEN + "Time:");
				score.setScore(info.getGameTime());
			}
			if (pp.getScoreboard() == board) {
				if (board.getObjective("test")
						.getScore(ChatColor.GREEN + "Ⓣ Tokens:").getScore() != info
						.getPP(pp).getCoins()) {
						Score coin = obj
								.getScore(ChatColor.GREEN + "Ⓣ Tokens:");
						coin.setScore(info.getPP(pp).getCoins());
				}
			}
			if (pp.getScoreboard() != board) {
				pp.setScoreboard(board);
			}
		}
	}

}
