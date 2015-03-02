package com.zafcoding.zackscott.tbn.orginial;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class GameLoop
  implements Runnable
{
  public static TheBatKnight plugin;

  public GameLoop(TheBatKnight instance)
  {
    plugin = instance;
  }

  public void run()
  {
    if (TheBatKnight.gameState.getState().equalsIgnoreCase(
      GameState.IN_LOBBY)) {
      TheBatKnight.hasStarted = false;
      for (Player P : Bukkit.getOnlinePlayers()) {
        P.setLevel(TheBatKnight.timeInSeconds);
      }
    }
    if (!TheBatKnight.pauseGame)
    {
      if (TheBatKnight.gameState.getState().equalsIgnoreCase(
        GameState.IN_LOBBY)) {
        if ((TheBatKnight.timeInSeconds >= 60) && 
          (TheBatKnight.timeInSeconds % 60 == 0)) {
          plugin.broadcastTimeUntilStart(true);
        }
        if ((TheBatKnight.timeInSeconds <= 30) && 
          (TheBatKnight.timeInSeconds % 15 == 0) && 
          (TheBatKnight.timeInSeconds > 0)) {
          plugin.broadcastTimeUntilStart(true);
        }
        if ((TheBatKnight.timeInSeconds <= 10) && 
          (TheBatKnight.timeInSeconds > 0)) {
          plugin.broadcastTimeUntilStart(false);
          playNote();
        }
        if (TheBatKnight.timeInSeconds == 0) {
          if (TheBatKnight.canStart) {
            plugin.startGame();
            for (Player p : Bukkit.getOnlinePlayers())
              p.setExp(0.0F);
          } else {
            Bukkit.broadcastMessage(ChatColor.GOLD + 
              "Not Enough Players! Reseting Timer!");
            playReseting();
            TheBatKnight.timeInSeconds = 121;
          }
        }
        if (TheBatKnight.timeInSeconds > 0)
          TheBatKnight.timeInSeconds -= 1;
      }
    }
  }

  public static void playNote()
  {
    for (Player player : Bukkit.getOnlinePlayers())
      for (int i = 0; i < 3; i++) {
        player.playSound(player.getLocation(), Sound.NOTE_PLING, 10.0F, 0.0F);
        player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 
          10.0F, 0.0F);
        player.playSound(player.getLocation(), Sound.NOTE_BASS, 10.0F, 0.0F);
        player.playSound(player.getLocation(), Sound.NOTE_BASS_DRUM, 
          10.0F, 0.0F);
      }
  }

  public static void playNote(Player player) {
    for (int i = 0; i < 3; i++) {
      player.playSound(player.getLocation(), Sound.NOTE_PLING, 10.0F, 0.0F);
      player.playSound(player.getLocation(), Sound.NOTE_BASS_GUITAR, 10.0F, 
        2.0F);
      player.playSound(player.getLocation(), Sound.NOTE_BASS, 10.0F, 2.0F);
    }
  }

  private void playReseting() {
    for (final Player player : Bukkit.getOnlinePlayers()) {
      final Location l = new Location(player.getWorld(), player
        .getLocation().getBlockX(), player.getLocation()
        .getBlockY() + 3, player.getLocation().getBlockZ());
      new Thread(new Runnable() {
        public void run() {
          try {
            for (int i = 0; i < 4; i++) {
              player.playEffect(l, Effect.CLICK1, 1);
              Thread.sleep(450L);
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }).start();
    }
  }
}