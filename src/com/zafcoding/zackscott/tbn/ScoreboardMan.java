package com.zafcoding.zackscott.tbn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import com.zafcoding.zackscott.tbn.Info.ServerState;

public class ScoreboardMan {

	TBN tbn = TBN.tbn;
	Info info = tbn.info;
	
	public void updateScoreBoard(){
		if(info.getState() != ServerState.In_Game){
			return;
		}
		ScoreboardManager manage = Bukkit.getScoreboardManager();
		Scoreboard board = manage.getNewScoreboard();
		Objective obj = board.registerNewObjective("TBK", "dummy");
		Score score = obj.getScore(ChatColor.GREEN + "Time:");
		score.setScore(info.getGameTime());
	}
	
}
