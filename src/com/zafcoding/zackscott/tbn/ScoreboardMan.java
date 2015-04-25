package com.zafcoding.zackscott.tbn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardMan {

	TBN tbn = TBN.tbn;
	Info info = tbn.info;
	
	public void updateScoreBoard(){
		
		ScoreboardManager manage = Bukkit.getScoreboardManager();
		Scoreboard board = manage.getNewScoreboard();
		
		Objective obj = board.registerNewObjective("test", "dummy");
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.GOLD + "TBK");
		Score score = obj.getScore(ChatColor.GREEN + "Time:");
		score.setScore(info.getGameTime());
		for(Player pp : Bukkit.getOnlinePlayers()){
			pp.setScoreboard(board);
		}
	}
	
}
