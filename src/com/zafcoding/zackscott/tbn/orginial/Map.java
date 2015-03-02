package com.zafcoding.zackscott.tbn.orginial;

import java.io.PrintStream;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Map
{
  public static int mapId = 0;
  public static int skipvotes = 0;

  public static boolean canSkip = true;

  public static String mapName = null;

  public Map() {
    canSkip = true;
    skipvotes = 0;
    mapId = 0;
    //int map = new Random().nextInt(5) + 1;
    mapName = null;
      mapId = 1;
      mapName = "Samopollis";
    System.out.println(mapId + " | " + mapName);
  }

  public int addSkip(Player player) {
    Bukkit.broadcastMessage(player.getDisplayName() + ChatColor.GOLD + 
      " voted to skip " + mapName);
    return ++skipvotes;
  }

  //TODO: Make it where it skips, dont let them fricking do it now though!
  public void skip() {
    String mapBefore = mapName;
    int random = new Random().nextInt(4);
    while (random == mapId)
      random = new Random().nextInt(5);
    switch (random) {
    case 0:
      mapId = 3;
      mapName = "Samopollis";
      break;
    case 1:
      mapName = "Ruce Baynes Manor";
      mapId = 1;
      break;
    case 2:
      mapId = 3;
      mapName = "Samopollis";
    case 3:
      mapId = 4;
      mapName = "Jester's Carnival";
    case 4:
      mapId = 5;
      mapName = "Three Pines Mall";
    }
    canSkip = false;
    nl();
    Bukkit.broadcastMessage(ChatColor.GOLD + "Skipped " + ChatColor.RED + 
      mapBefore + "!");
    Bukkit.broadcastMessage(ChatColor.GOLD + "New Map: " + ChatColor.RED + 
      ChatColor.RED + mapName + "!");
    nl();
  }

  private void nl() {
    Bukkit.broadcastMessage("");
  }

  public String getMapName() {
    return mapName;
  }

  public int getMapId() {
    return mapId;
  }
}