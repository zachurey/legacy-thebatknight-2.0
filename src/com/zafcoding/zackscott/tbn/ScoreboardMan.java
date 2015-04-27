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

	ScoreboardManager manage = Bukkit.getScoreboardManager();
	Scoreboard board = manage.getNewScoreboard();
	Objective obj = board.registerNewObjective("test", "dummy");

	public void updateScoreBoard() {
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.GOLD + "TBK");
		if (info.getState() == ServerState.In_Game) {
			Score score = obj.getScore(ChatColor.GREEN + "Time:");
			score.setScore(info.getGameTime());
		}
		for (Player pp : Bukkit.getOnlinePlayers()) {
			if (info.getState() == ServerState.Pre_Game) {
				Score coin = obj.getScore(ChatColor.GREEN + "Tokens:");
				coin.setScore(info.getPP(pp).getCoins());
			}
			pp.setScoreboard(board);
		}
	}

}
