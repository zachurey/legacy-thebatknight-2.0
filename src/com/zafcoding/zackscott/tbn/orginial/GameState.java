package com.zafcoding.zackscott.tbn.orginial;

import org.bukkit.ChatColor;

public class GameState
{
  public static final String IN_LOBBY = starter() + ChatColor.YELLOW +
    "Game In LOBBY";
 
  public static final String IN_GAME = starter() + ChatColor.GREEN +
    "Game In GAME";
 
  public static final String POST_GAME = starter() + ChatColor.RED +
    "Game RESTARTING";
 
  public static final String GAME_RESETING = starter() + ChatColor.DARK_RED +
    "Game RESETTING";
  private String gameState;
 
  private static String starter()
  {
    return ChatColor.DARK_GRAY + "[" + ChatColor.GOLD + "TheBatKnight" +
      ChatColor.DARK_GRAY + "] ";
  }
 
  public void setGameState(String state) {
    this.gameState = state;
  }
 
  public String getState() {
    return this.gameState;
  }
}
