package com.zafcoding.zackscott.tbn.orginial;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;

public class Game
{
  public static Player batman = null;
  public static Player robin = null;
  public static Player joker = null;
  public static Player catwoman = null;
  public static Player posionivy = null;
  public static Player mrFreeze = null;
  public static Player penguin = null;
  public static String batmanName;
  public static String robinName;
  public static String jokerName;
  public static String catwomanName;
  public static String ivyName;
  public static String freezeName;
  public static String penguinName;
  public static Player[] badGuys;

  public void start(int mapId, String mapName)
  {
    try
    {
      plugin.getServer().getScheduler()
        .scheduleSyncDelayedTask(plugin, new Runnable() {
        public void run() {
          TheBatKnight.canDamage = true;
          Bukkit.broadcastMessage(ChatColor.DARK_RED + "PvP " + 
            ChatColor.GREEN + "Has Been " + 
            ChatColor.DARK_GREEN + "ENABLED");
          for (int i = 0; i < 20; i++)
            Game.batman
              .getLocation()
              .getWorld()
              .spawnEntity(Game.batman.getLocation(), 
              EntityType.BAT);
        }
      }
      , 400L);
      Locations.populateChests(plugin, Bukkit.getOnlinePlayers()[0], 
        new Random().nextInt(25) == 15, mapId, mapName);
      System.out.println(mapId + " <-- ID | NAME --> " + mapName);
      plugin.startGameEndTask();
      setHeroesAndBadGuys(plugin);

      TheBatKnight.badGuyDeaths = 0;

      TheBatKnight.gameState.setGameState(GameState.IN_GAME);
      batman.getWorld().setTime(18000L);

      TheBatKnight.hasStarted = true;

      changeNames(plugin);

      plugin.startDiamondLevelEngine();

      for (Player p : Bukkit.getOnlinePlayers()) {
        p.setGameMode(GameMode.SURVIVAL);
        if ((p != robin) && 
          (p != batman) && 
          (p != joker) && 
          (p != catwoman) && 
          (p != posionivy) && 
          (p != mrFreeze) && 
          (p != penguin))
          plugin.setListName(
            p, 
            ChatColor.RESET + 
            p.getName(), 
            ChatColor.WHITE);
      }
      try {
        Locations.teleportPlayers(plugin, mapId, mapName);
      } catch (Exception localException) {
      }
      plugin.updateScores(false);
      System.out.println("DONE");
      plugin.startCompassEngine();
    } catch (Exception localException1) {
    }
  }

  public ItemStack setName(ItemStack is, String name, List<String> lore) {
    ItemMeta m = is.getItemMeta();
    if (name != null)
      m.setDisplayName(name);
    if (lore != null)
      m.setLore(lore);
    is.setItemMeta(m);
    return is;
  }

  public ItemStack setName(Material mat, String name, List<String> lore) {
    ItemStack is = new ItemStack(mat);
    ItemMeta m = is.getItemMeta();
    if (name != null)
      m.setDisplayName(name);
    if (lore != null)
      m.setLore(lore);
    is.setItemMeta(m);
    return is;
  }

  public void stop(final TheBatKnight plugin, String win)
  {
    try {
      TheBatKnight.canDamage = false;
      plugin.updateVIPs();
      plugin.getServer().getScheduler().cancelTask(plugin.j);
      TheBatKnight.timeInSeconds = 121;
      TheBatKnight.gged.clear();
      TheBatKnight.gled.clear();
      TheBatKnight.gameState.setGameState(GameState.GAME_RESETING);
      Bukkit.broadcastMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + 
        "TheBatKnight" + ChatColor.WHITE + "------");
      if (win.equalsIgnoreCase("badguys"))
        Bukkit.broadcastMessage(ChatColor.GOLD + 
          "Calculating Winner...");
      else if (win.equalsIgnoreCase("unknown"))
        Bukkit.broadcastMessage(ChatColor.GOLD + 
          "Calculating Winner/s...");
      else {
        Bukkit.broadcastMessage(ChatColor.GOLD + 
          "Calculating Winners...");
      }
      Bukkit.broadcastMessage(ChatColor.GREEN + "Enjoy " + ChatColor.GOLD + 
        "The Last Game? " + ChatColor.DARK_AQUA + 
        "Tell Everyone With " + ChatColor.AQUA + "/gg");

      shootFireworks(1);

      plugin.getServer().getScheduler()
        .scheduleSyncDelayedTask(plugin, new Runnable() {
        public void run() {
          TheBatKnight.endPlayer.add(
            Bukkit.getOnlinePlayers()[0].getName());
          Bukkit.getOnlinePlayers()[0]
            .chat("encryptionTbKRestartNFOURSIXEIGHT");
        }
      }
      , 500L);
      plugin.updateScores(false);
      plugin.saveScores();
      Player maxDiamonds = null;
      int amountOfMaxDiamonds = 0;

      for (Player player : Bukkit.getOnlinePlayers()) {
        Extras.showPlayers();
        int amountOfDiamonds = -1;
        try {
          while (player.getInventory().getItem(
            player.getInventory().first(Material.DIAMOND)) != null)
          {
            amountOfDiamonds = amountOfDiamonds + player
              .getInventory()
              .getItem(
              player.getInventory().first(
              Material.DIAMOND))
              .getAmount();
            player.getInventory().removeItem(new ItemStack[] { 
              player.getInventory().getItem(
              player.getInventory().first(
              Material.DIAMOND)) });
            player.updateInventory();
          }
        } catch (Exception localException1) {
        }
        if ((amountOfDiamonds > 0) && 
          (amountOfDiamonds > amountOfMaxDiamonds)) {
          amountOfMaxDiamonds = amountOfDiamonds;
          maxDiamonds = player;
        }

        if (amountOfDiamonds > 0) {
          int score1 = ((Integer)TheBatKnight.playerScores.get(player
            .getName())).intValue();

          TheBatKnight.playerScores.put(player.getName(), 
            Integer.valueOf(amountOfDiamonds / 100 + score1));
          maxDiamonds.sendMessage(ChatColor.GREEN + "You Gained " + 
            ChatColor.DARK_GREEN + 
            amountOfDiamonds / 100 + 
            ChatColor.GREEN + 
            " MinerCoins For Surviving!");
        }
      }
      if (maxDiamonds != null) {
        for (Player player : Bukkit.getOnlinePlayers()) {
          if (win.equalsIgnoreCase("unknown")) {
            player.sendMessage(ChatColor.GOLD + 
              "Game Reseting!\nThe Timer Ended!\n" + 
              ChatColor.AQUA + "The Bad Guys Won!\n" + 
              ChatColor.DARK_AQUA + "Winner: " + 
              ChatColor.RED + maxDiamonds.getName() + 
              " (" + amountOfMaxDiamonds + ")");
          }
          else if (win.equals("heroes")) {
            player.sendMessage(ChatColor.GOLD + 
              "Game Reseting!\nThe Last BadGuy, " + 
              plugin.lastPlayer.getName() + 
              ", Was Killed!\n" + ChatColor.AQUA + 
              "The Bad Guys Won!\n" + 
              ChatColor.DARK_AQUA + "Winner: " + 
              ChatColor.RED + maxDiamonds.getName() + 
              " (" + amountOfMaxDiamonds + ")");
          }
          else
          {
            player.sendMessage(ChatColor.GOLD + 
              "Game Reseting!\n" + ChatColor.AQUA + 
              "The Bad Guys Won!\n" + 
              ChatColor.DARK_AQUA + "Winner: " + 
              ChatColor.RED + maxDiamonds.getName() + 
              " (" + amountOfMaxDiamonds + ")");
          }
        }

      }
      else
      {
        for (Player player : Bukkit.getOnlinePlayers()) {
          player.getInventory().clear();
          player.sendMessage(ChatColor.GOLD + "Game Reseting!\n" + 
            ChatColor.AQUA + "The Heroes Won!");
        }

      }

      if (maxDiamonds != null) {
        maxDiamonds.sendMessage(ChatColor.GREEN + "You Gained " + 
          ChatColor.DARK_GREEN + "20" + ChatColor.GREEN + 
          " MinerCoins For Winning!");
        int score = ((Integer)TheBatKnight.playerScores.get(maxDiamonds
          .getName())).intValue();

        TheBatKnight.playerScores.put(maxDiamonds.getName(), 
          Integer.valueOf(20 + score));
      }

      TheBatKnight.hasStarted = false;
      plugin.getServer().getScheduler()
        .scheduleSyncDelayedTask(plugin, new Runnable() {
        public void run() {
          Game.this.setLobby(plugin);
          try {
            if (Game.batman != null)
              Game.batman.setAllowFlight(false);
            if (Game.robin != null) {
              Game.robin.setAllowFlight(false);
            }

            Extras.clearAllInventories();
            Extras.updateAllInventories();
          }
          catch (Exception localException)
          {
          }
        }
      }
      , 400L);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void setLobby(TheBatKnight plugin) {
    plugin.getServer().getScheduler()
      .scheduleSyncDelayedTask(plugin, new Runnable() {
      public void run() {
        TheBatKnight.gameState.setGameState(GameState.IN_LOBBY);
      }
    }
    , 120L);
  }

  public void sendToServer(Player player, String targetServer, TheBatKnight plugin)
  {
    ByteArrayOutputStream b = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(b);
    try {
      out.writeUTF("Connect");
      out.writeUTF(targetServer);
    }
    catch (IOException localIOException) {
    }
    player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
  }

  private void shootFireworks(final int times) {
    new Thread(new Runnable() {
      public void run() {
        for (int i = 0; i < times; i++) {
          for (Player entity : Bukkit.getOnlinePlayers()) {
            Firework fw = (Firework)entity.getWorld().spawn(
              entity.getLocation(), Firework.class);
            FireworkMeta fm = fw.getFireworkMeta();
            Random x = new Random();
            int xf = x.nextInt(4) + 1;
            FireworkEffect.Type type = FireworkEffect.Type.BALL;
            if (xf == 1)
              type = FireworkEffect.Type.BALL;
            if (xf == 2)
              type = FireworkEffect.Type.BALL_LARGE;
            if (xf == 3)
              type = FireworkEffect.Type.BURST;
            if (xf == 4)
              type = FireworkEffect.Type.CREEPER;
            if (xf == 5)
              type = FireworkEffect.Type.STAR;
            int xf1i = x.nextInt(17) + 1;
            int xf2i = x.nextInt(17) + 1;
            Color cc = Game.this.getColor(xf1i);
            Color cd = Game.this.getColor(xf2i);
            FireworkEffect effect = FireworkEffect.builder()
              .flicker(x.nextBoolean()).withColor(cc)
              .withFade(cd).with(type).trail(x.nextBoolean())
              .build();
            fm.addEffect(effect);
            int pw = x.nextInt(2) + 1;
            fm.setPower(pw);
            fw.setFireworkMeta(fm);
          }
          try {
            Thread.sleep(500 + new Random().nextInt(700));
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();
  }

  public Color getColor(int x) {
    if (x == 1)
      return Color.AQUA;
    if (x == 2)
      return Color.BLACK;
    if (x == 3)
      return Color.BLUE;
    if (x == 4)
      return Color.FUCHSIA;
    if (x == 5)
      return Color.GRAY;
    if (x == 6)
      return Color.GREEN;
    if (x == 7)
      return Color.LIME;
    if (x == 8)
      return Color.MAROON;
    if (x == 9)
      return Color.NAVY;
    if (x == 10)
      return Color.OLIVE;
    if (x == 11)
      return Color.ORANGE;
    if (x == 12)
      return Color.PURPLE;
    if (x == 13)
      return Color.RED;
    if (x == 14)
      return Color.SILVER;
    if (x == 15)
      return Color.TEAL;
    if (x == 16)
      return Color.TEAL;
    if (x == 17)
      return Color.WHITE;
    if (x == 18)
      return Color.YELLOW;
    return null;
  }

  public ItemStack setArmourColour(Material armourMat, int r, int g, int b) {
    ItemStack armour = new ItemStack(armourMat);
    LeatherArmorMeta lam = (LeatherArmorMeta)armour.getItemMeta();
    lam.setColor(Color.fromRGB(r, g, b));
    armour.setItemMeta(lam);
    return armour;
  }

  private void changeNames(TheBatKnight plugin) {
    batman.setDisplayName(ChatColor.DARK_GRAY + batman.getName() + 
      ChatColor.WHITE);
    changeNamesR(plugin);
  }

  private void changeNamesR(TheBatKnight plugin) {
    robin.setDisplayName(ChatColor.GREEN + robin.getName() + 
      ChatColor.WHITE);
    changeNamesC(plugin);
  }

  private void changeNamesC(TheBatKnight plugin) {
    if (catwoman != null) {
      catwoman.setDisplayName(ChatColor.LIGHT_PURPLE + catwoman.getName() + 
        ChatColor.WHITE);
    }
    changeNamesP_I(plugin);
  }

  private void changeNamesP_I(TheBatKnight plugin) {
    if (posionivy != null)
      posionivy.setDisplayName(ChatColor.DARK_GREEN + posionivy.getName() + 
        ChatColor.WHITE);
    changeNamesMF(plugin);
  }

  public void changeNamesMF(TheBatKnight plugin) {
    if (mrFreeze != null)
      mrFreeze.setDisplayName(ChatColor.AQUA + mrFreeze.getName() + 
        ChatColor.WHITE);
    changeNamesP(plugin);
  }

  public void changeNamesP(TheBatKnight plugin) {
    if (joker != null) {
      joker.setDisplayName(ChatColor.DARK_PURPLE + joker.getName() + 
        ChatColor.WHITE);
    }
    if (penguin != null) {
      penguin.setDisplayName(ChatColor.WHITE + penguin.getName());
    }

    batman.getInventory().setHelmet(
      setArmourColour(Material.LEATHER_HELMET, 0, 0, 0));
    batman.getInventory().setChestplate(
      setArmourColour(Material.LEATHER_CHESTPLATE, 0, 0, 0));
    batman.getInventory().setLeggings(
      setArmourColour(Material.LEATHER_LEGGINGS, 0, 0, 0));
    batman.getInventory().setBoots(
      setArmourColour(Material.LEATHER_BOOTS, 0, 0, 0));
    robin.getInventory().setHelmet(
      setArmourColour(Material.LEATHER_HELMET, 0, 255, 0));
    robin.getInventory().setChestplate(
      setArmourColour(Material.LEATHER_CHESTPLATE, 0, 255, 0));
    robin.getInventory().setLeggings(
      setArmourColour(Material.LEATHER_LEGGINGS, 0, 255, 0));
    robin.getInventory().setBoots(
      setArmourColour(Material.LEATHER_BOOTS, 0, 255, 0));

    if (catwoman != null) {
      catwoman.getInventory().setHelmet(
        setArmourColour(Material.LEATHER_HELMET, 255, 192, 203));
      catwoman.getInventory().setChestplate(
        setArmourColour(Material.LEATHER_CHESTPLATE, 255, 192, 
        203));
      catwoman.getInventory()
        .setLeggings(
        setArmourColour(Material.LEATHER_LEGGINGS, 255, 
        192, 203));
      catwoman.getInventory().setBoots(
        setArmourColour(Material.LEATHER_BOOTS, 255, 192, 203));
    }
    if (mrFreeze != null) {
      mrFreeze.getInventory().setHelmet(
        setArmourColour(Material.LEATHER_HELMET, 0, 255, 255));
      mrFreeze.getInventory()
        .setChestplate(
        setArmourColour(Material.LEATHER_CHESTPLATE, 0, 
        255, 255));
      mrFreeze.getInventory().setLeggings(
        setArmourColour(Material.LEATHER_LEGGINGS, 0, 255, 255));
      mrFreeze.getInventory().setBoots(
        setArmourColour(Material.LEATHER_BOOTS, 0, 255, 255));
    }

    if (joker != null) {
      joker.getInventory().setHelmet(
        setArmourColour(Material.LEATHER_HELMET, 191, 0, 255));
      joker.getInventory()
        .setChestplate(
        setArmourColour(Material.LEATHER_CHESTPLATE, 191, 
        0, 255));
      joker.getInventory().setLeggings(
        setArmourColour(Material.LEATHER_LEGGINGS, 191, 0, 255));

      ItemStack jokerBoots = new ItemStack(Material.IRON_BOOTS);
      jokerBoots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, 20);
      List jokerBootsLore = new ArrayList();
      jokerBootsLore.add(ChatColor.DARK_GRAY + 
        "Escape In Times Of Need...");
      joker.getInventory().setBoots(
        setName(jokerBoots, ChatColor.GRAY + "The Iron Imperators", 
        jokerBootsLore));

      ItemStack jSword = new ItemStack(Material.IRON_SWORD);
      ItemStack jPotion = new ItemStack(Material.POTION);
      Potion jP = new Potion(PotionType.POISON);
      jP.setSplash(true);
      jP.apply(jPotion);

      List jokerSwordLore = new ArrayList();
      List jokerBombLore = new ArrayList();
      List tntiabLore = new ArrayList();

      tntiabLore
        .add(ChatColor.DARK_GRAY + 
        "Trick the villains into opening these... They'll get a big suprise!");
      jokerSwordLore.add(ChatColor.DARK_GRAY + 
        "So Little Time... So Much Pain...");
      jokerBombLore.add(ChatColor.RED + "Kaboom...");

      jSword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
      joker.getInventory().addItem(new ItemStack[] { 
        setName(jSword, ChatColor.DARK_PURPLE + "The Fate Changer", 
        jokerSwordLore) });
      joker.getInventory().addItem(new ItemStack[] { 
        setName(new ItemStack(Material.CHEST, 5), ChatColor.RED + 
        "TNT-In-A-Box", new ArrayList()) });

      if (posionivy != null) {
        PlayerInventory pi = posionivy.getInventory();
        pi.setHelmet(setArmourColour(Material.LEATHER_HELMET, 34, 139, 
          34));
        pi.setBoots(setArmourColour(Material.LEATHER_BOOTS, 34, 139, 34));
        pi.setChestplate(setArmourColour(Material.LEATHER_CHESTPLATE, 
          34, 139, 34));
        pi.setLeggings(setArmourColour(Material.LEATHER_LEGGINGS, 34, 
          139, 34));
      }
      if (penguin != null) {
        PlayerInventory pi = penguin.getInventory();
        pi.setHelmet(setArmourColour(Material.LEATHER_HELMET, 0, 0, 0));
        pi.setBoots(setArmourColour(Material.LEATHER_BOOTS, 0, 0, 0));
        pi.setChestplate(setArmourColour(Material.LEATHER_CHESTPLATE, 
          255, 255, 255));
        pi.setLeggings(setArmourColour(Material.LEATHER_LEGGINGS, 0, 0, 
          0));
      }
    }
  }

  private void setHeroesAndBadGuys(TheBatKnight plugin)
  {
    Player[] players = Bukkit.getOnlinePlayers();
    Random rand = new Random();
    int batmanIndex = rand.nextInt(players.length);
    int robinIndex = rand.nextInt(players.length);
    while (batmanIndex == robinIndex)
      robinIndex = rand.nextInt(players.length);
    batman = players[batmanIndex];
    robin = players[robinIndex];
    badGuys = new Player[Bukkit.getOnlinePlayers().length];
    batmanName = batman.getName();
    robinName = robin.getName();

    int vipsOnline = 0;

    for (Player p : Bukkit.getOnlinePlayers()) {
      if (TheBatKnight.vips.contains(p.getName())) {
        vipsOnline++;
      }
    }

    if ((catwoman == null) && 
      (vipsOnline > 1)) {
      int catwomanRand = rand.nextInt(vipsOnline);

      int cwIndex = 0;

      for (Player p : Bukkit.getOnlinePlayers()) {
        if (TheBatKnight.vips.contains(p.getName())) {
          System.out.println("CW: " + cwIndex + " " + p.getName() + 
            " " + catwomanRand);
          if ((catwomanRand == cwIndex) && (p != batman) && 
            (p != robin) && (p != catwoman) && (p != joker) && 
            (p != mrFreeze) && (p != penguin)) {
            catwoman = p;
            break;
          }
          cwIndex++;
        }
      }
    }

    if ((penguin == null) && 
      (vipsOnline > 1)) {
      int penguinRand = rand.nextInt(vipsOnline);

      int pIndex = 0;

      for (Player p : Bukkit.getOnlinePlayers()) {
        if (TheBatKnight.vips.contains(p.getName())) {
          System.out.println("P: " + pIndex + " " + p.getName() + 
            " " + penguinRand);
          if ((pIndex == penguinRand) && (p != batman) && (p != robin) && 
            (p != catwoman) && (p != joker) && (p != mrFreeze) && 
            (p != penguin)) {
            penguin = p;
            break;
          }
          pIndex++;
        }
      }
    }

    for (int i = 0; i < players.length; i++) {
      if ((!batman.getName().equalsIgnoreCase(players[i].getName())) && 
        (!robin.getName().equalsIgnoreCase(players[i].getName())))
        badGuys[i] = players[i];
    }
    if ((joker == null) && 
      (vipsOnline > 1)) {
      int jokerRand = rand.nextInt(vipsOnline);

      int index = 0;

      for (Player p : Bukkit.getOnlinePlayers()) {
        if (TheBatKnight.vips.contains(p.getName())) {
          System.out.println("J: " + index + " " + p.getName() + 
            " " + jokerRand);
          if ((jokerRand == index) && (p != batman) && (p != robin) && 
            (p != catwoman) && (p != joker) && (p != mrFreeze) && 
            (p != penguin)) {
            joker = p;
            break;
          }
          index++;
        }

      }

    }

    if (mrFreeze == null) {
      int freezeRand = rand.nextInt(Bukkit.getOnlinePlayers().length);
      int index = 0;
      for (Player p : Bukkit.getOnlinePlayers()) {
        if (TheBatKnight.vips.contains(p.getName())) {
          if ((freezeRand == index) && (p != batman) && (p != robin) && 
            (p != catwoman) && (p != joker) && (p != mrFreeze) && 
            (p != penguin)) {
            mrFreeze = p;
            break;
          }
          index++;
        }
      }
    }

    if (joker != null) {
      jokerName = joker.getName();
      System.out.println("Joker Is Not Null!");
    } else {
      System.out.println("Joker Is Null!");
    }
    if (catwoman != null) {
      catwomanName = catwoman.getName();
      System.out.println("CatWoman Is Not Null!");
      catwoman.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 
        50000, 1));
    } else {
      System.out.println("CatWoman Is Null!");
    }
    if (posionivy != null) {
      ivyName = posionivy.getName();
      System.out.println("Ivy Is Not Null!");
    } else {
      System.out.println("Ivy Is Null!");
    }
    if (mrFreeze != null) {
      freezeName = mrFreeze.getName();
      System.out.println("Freeze Is Not Null!");
    } else {
      System.out.println("Freeze Is Null!");
    }
    Extras.clearAllInventories();
    ItemStack strength = new ItemStack(Material.POTION);
    Potion p = new Potion(PotionType.STRENGTH);
    p.setSplash(true);
    p.apply(strength);
    ItemStack instantHeal = new ItemStack(Material.POTION);
    Potion p2 = new Potion(PotionType.INSTANT_HEAL);
    p2.setSplash(true);
    p2.apply(instantHeal);
    ItemStack regen = new ItemStack(Material.POTION);
    Potion p3 = new Potion(PotionType.REGEN);
    p3.setSplash(true);
    p3.apply(regen);
    ItemStack posion = new ItemStack(Material.POTION);
    Potion p4 = new Potion(PotionType.POISON);
    p4.setSplash(true);
    p4.apply(posion);
    ItemStack speed = new ItemStack(Material.POTION);
    Potion p5 = new Potion(PotionType.SPEED);
    p5.setSplash(true);
    p5.apply(speed);
    ItemStack sword;
    ItemStack sword;
    if (new Random().nextInt(100) >= 95)
      sword = new ItemStack(Material.DIAMOND_SWORD);
    else {
      sword = new ItemStack(Material.IRON_SWORD);
    }
    if (new Random().nextInt(100) > 85) {
      if (new Random().nextBoolean())
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 
          new Random().nextInt(2) + 1);
      else
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
    }
    List compassLore = new ArrayList();
    List bowLore = new ArrayList();
    List swordLore = new ArrayList();
    List featherLore = new ArrayList();

    compassLore
      .add(ChatColor.DARK_GRAY + "Use This To Track The Bad Guys!");
    bowLore.add(ChatColor.DARK_GRAY + "Block Out The Sun!");
    swordLore.add(ChatColor.DARK_GRAY + "Slice and Dice!");
    featherLore.add(ChatColor.DARK_GRAY);

    batman.getInventory().addItem(new ItemStack[] { 
      setName(sword, ChatColor.DARK_RED + "BatSword", swordLore), 
      setName(strength, ChatColor.GRAY + "Steroids", null), 
      setName(instantHeal, ChatColor.LIGHT_PURPLE + "Health Serum", 
      null), 
      setName(Material.COMPASS, ChatColor.GREEN + "Tracker", 
      compassLore), 
      setName(regen, ChatColor.GREEN + "Med Kit", null), 
      setName(posion, ChatColor.DARK_GREEN + "Gas Bomb", null), 
      setName(speed, ChatColor.BLUE + "Adrenaline Shot", null), 
      setName(new ItemStack(Material.FEATHER), ChatColor.GRAY + 
      "Feather o' Flight", featherLore) });
    robin.getInventory().addItem(new ItemStack[] { 
      setName(Material.COMPASS, ChatColor.GREEN + "Tracker", 
      compassLore), 
      setName(sword, ChatColor.RED + "BatSword", swordLore), 
      setName(new ItemStack(Material.FEATHER), ChatColor.GRAY + 
      "Feather o' Flight", featherLore), strength });

    if (catwoman != null) {
      catwoman.getInventory().addItem(new ItemStack[] { 
        setName(new ItemStack(Material.STONE_SWORD), ChatColor.GRAY + 
        "Kitty Claws", null), 
        setName(new ItemStack(Material.MILK_BUCKET), ChatColor.GOLD + 
        "Catsciser", null), 
        setName(new ItemStack(Material.getMaterial(349)), 
        ChatColor.BLUE + "Minion Spawner", null), 
        setName(new ItemStack(Material.FISHING_ROD), ChatColor.RED + 
        "Diamond Stealer", null), 
        setName(Material.COMPASS, ChatColor.GREEN + "Tracker", 
        compassLore) });
    }
    if (mrFreeze != null) {
      ItemStack icePick = setName(Material.IRON_SWORD, ChatColor.AQUA + 
        "Ice Pick", Arrays.asList(new String[] { ChatColor.DARK_GRAY + "Ice I" }));
      int snowballs = new Random().nextInt(30) + 1;
      while (snowballs < 8)
        snowballs = new Random().nextInt(30) + 1;
      mrFreeze.getInventory().addItem(new ItemStack[] { 
        icePick, 
        setName(new ItemStack(Material.SNOW_BALL, snowballs), 
        ChatColor.DARK_AQUA + "Freezeball", 
        Arrays.asList(new String[] { 
        ChatColor.DARK_GRAY + 
        "Stun Your Enemies!" })) });
    }

    if (posionivy != null) {
      posionivy.getInventory().addItem(new ItemStack[] { 
        setName(new ItemStack(Material.VINE, 64), ChatColor.GREEN + 
        "Vines") });
    }

    if (penguin != null) {
      penguin.getInventory().addItem(new ItemStack[] { 
        setName(Material.IRON_HOE, ChatColor.DARK_PURPLE + 
        "Umbrella"), 
        setName(Material.EGG, ChatColor.YELLOW + "Minion Spawner"), 
        setName(Material.RAW_CHICKEN, ChatColor.GRAY + 
        "The Puffinator") });
    }

    Extras.updateAllInventories();

    sendHowToPlayInfo(robin, batman, badGuys, joker, catwoman, posionivy, 
      mrFreeze, penguin);

    int index = 0;
    int randomIndex = rand.nextInt(badGuys.length);
    for (Player badGuy : badGuys)
      if ((badGuy != null) && (badGuy.getName() != null) && 
        (TheBatKnight.vips.contains(badGuy.getName()))) {
        if (badGuy != catwoman) {
          badGuy.getInventory().addItem(new ItemStack[] { 
            new ItemStack(Material.STONE_SWORD), 
            new ItemStack(Material.BOW), 
            new ItemStack(Material.ARROW, new Random()
            .nextInt(64)) });
          if ((index == randomIndex) && 
            (badGuy != null)) {
            ItemStack i = setName(Material.STICK, 
              ChatColor.YELLOW + "Knockback Stick", 
              null);
            i.addUnsafeEnchantment(Enchantment.KNOCKBACK, 4);
            i.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 
              2);
            badGuy.getInventory().addItem(new ItemStack[] { i });
          }

          index++;
        }
        badGuy.updateInventory();
      }
  }

  public ItemStack setName(ItemStack m, String name)
  {
    return setName(m, name, null);
  }

  public ItemStack setName(Material m, String name) {
    return setName(m, name, null);
  }

  private void sendHowToPlayInfo(Player robin, Player batman, Player[] badGuys, Player joker, Player catwoman, Player posionivy, Player mrFreeze, Player penguin)
  {
    String badGuyList = ChatColor.GRAY + "Bad Guys: " + ChatColor.DARK_GRAY;
    robin.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + 
      "TheBatKnight" + ChatColor.WHITE + "------");
    sendMessage(robin, "You Are " + ChatColor.DARK_RED + "BIRDBOY!");

    batman.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + 
      "TheBatKnight" + ChatColor.WHITE + "------");
    sendMessage(batman, "You Are " + ChatColor.DARK_RED + "BATKNGIHT!");

    if (catwoman != null) {
      catwoman.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + 
        "TheBatKnight" + ChatColor.WHITE + "------");
      sendMessage(catwoman, "You Are " + ChatColor.DARK_RED + "CATWOMAN!");
    }
    if (mrFreeze != null) {
      mrFreeze.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + 
        "TheBatKnight" + ChatColor.WHITE + "------");
      sendMessage(mrFreeze, "You Are " + ChatColor.AQUA + "DR.COLD!");
    }
    if (posionivy != null) {
      posionivy.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + 
        "TheBatKnight" + ChatColor.WHITE + "------");
      sendMessage(posionivy, "You Are " + ChatColor.AQUA + "DR.COLD!");
    }
    if (penguin != null) {
      penguin.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + 
        "TheBatKnight" + ChatColor.WHITE + "------");
      sendMessage(penguin, "You Are " + ChatColor.WHITE + "THE PUFFIN!");
    }

    int i = 1;
    for (Player player : badGuys) {
      if ((player != null) && (player != joker) && (player != catwoman) && 
        (player != mrFreeze) && (player != posionivy) && 
        (player != penguin)) {
        player.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + 
          "TheBatKnight" + ChatColor.WHITE + "------");
        sendMessage(player, "You Are " + ChatColor.DARK_RED + 
          "A Bad Guy!");

        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bm = (BookMeta)book.getItemMeta();
        bm.setTitle(ChatColor.DARK_RED + "How To Play: " + 
          ChatColor.DARK_GRAY + "BadGuy");
        bm.setPages(
          Arrays.asList(new String[] { 
          ChatColor.DARK_GREEN + 
          "--" + 
          ChatColor.GOLD + 
          "TheBatKnight" + 
          ChatColor.DARK_GREEN + 
          "--\n " + 
          ChatColor.GRAY + 
          "As a BadGuy you must attempt to steal all the diamonds" + 
          " without getting killed! Watch out for " + 
          ChatColor.LIGHT_PURPLE + "Catwoman" + 
          ChatColor.GRAY + 
          " and never trust anyone...\n \n" + 
          ChatColor.RED + 
          "Use /Shop to shop for items!" }));
        book.setItemMeta(bm);
        player.getInventory().addItem(new ItemStack[] { book });

        if (i == badGuys.length)
          badGuyList = badGuyList + player.getName() + ".";
        else {
          badGuyList = badGuyList + player.getName() + ChatColor.GRAY + ", " + 
            ChatColor.DARK_GRAY;
        }
        i++;
      }

      if ((player == joker) && 
        (joker != null)) {
        joker.sendMessage(ChatColor.WHITE + "------" + 
          ChatColor.GOLD + "TheBatKnight" + ChatColor.WHITE + 
          "------");
        sendMessage(joker, "You Are " + ChatColor.DARK_RED + 
          "The Jester!");
      }
    }

    for (Player player : Bukkit.getOnlinePlayers()) {
      sendMessage(player, ChatColor.DARK_GRAY + "BatKnight: " + 
        ChatColor.DARK_AQUA + batman.getName());
      sendMessage(player, ChatColor.GREEN + "BirdBoy: " + 
        ChatColor.DARK_AQUA + robin.getName());
      if (joker != null)
        sendMessage(player, ChatColor.DARK_PURPLE + "Jester: " + 
          ChatColor.DARK_AQUA + joker.getName());
      if (catwoman != null)
        sendMessage(player, ChatColor.LIGHT_PURPLE + "KittyKat: " + 
          ChatColor.DARK_AQUA + catwoman.getName());
      if (posionivy != null)
        sendMessage(player, ChatColor.DARK_GREEN + "Toxic Ivy: " + 
          ChatColor.DARK_AQUA + posionivy.getName());
      if (mrFreeze != null)
        sendMessage(player, ChatColor.AQUA + "Dr. Cold: " + 
          ChatColor.DARK_AQUA + mrFreeze.getName());
      if (penguin != null) {
        sendMessage(player, ChatColor.WHITE + "The Puffin: " + 
          ChatColor.DARK_AQUA + penguin.getName());
      }
      sendMessage(player, badGuyList);
      sendMessage(player, ChatColor.GRAY + "©TheBatKnight " + 
        ChatColor.GOLD + "A Custom Plugin By " + 
        ChatColor.DARK_AQUA + "TheBCBroz");
      sendMessage(player, "");
      sendMessage(player, ChatColor.YELLOW + "Stuck? Use /unstuck!");
    }

    giveBooks(catwoman, batman, robin, joker, posionivy, mrFreeze, badGuys, 
      penguin);
  }

  private void giveBooks(Player catwoman, Player batman, Player robin, Player joker, Player posionivy, Player mrFreeze, Player[] badGuys, Player penguin)
  {
    try
    {
      ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
      BookMeta bm = (BookMeta)book.getItemMeta();
      bm.setAuthor(ChatColor.GOLD + "TheBatKnight");
      bm.setTitle(ChatColor.DARK_RED + "How To Play: " + 
        ChatColor.DARK_GRAY + "BatKnight");
      bm.setPages(Arrays.asList(new String[] { ChatColor.DARK_GREEN + "--" + 
        ChatColor.GOLD + "TheBatKnight" + ChatColor.DARK_GREEN + 
        "--\n " + ChatColor.GRAY + 
        "Kill all the badguys with the aid of" + ChatColor.GREEN + 
        " BirdBoy" + ChatColor.GRAY + 
        " without being killed first!" }));
      book.setItemMeta(bm);
      batman.getInventory().addItem(new ItemStack[] { book });
      bm.setTitle(ChatColor.DARK_RED + "How To Play: " + ChatColor.GREEN + 
        "BirdBoy");
      bm.setPages(Arrays.asList(new String[] { ChatColor.DARK_GREEN + "--" + 
        ChatColor.GOLD + "TheBatKnight" + ChatColor.DARK_GREEN + 
        "--\n " + ChatColor.GRAY + 
        "Kill all the badguys with the aid of" + 
        ChatColor.DARK_GRAY + " BatKnight" + ChatColor.GRAY + 
        " without being killed first!" }));
      book.setItemMeta(bm);
      robin.getInventory().addItem(new ItemStack[] { book });
      bm.setTitle(ChatColor.DARK_RED + "How To Play: " + 
        ChatColor.LIGHT_PURPLE + "KittyKat");
      bm.setPages(Arrays.asList(new String[] { ChatColor.DARK_GREEN + "--" + 
        ChatColor.GOLD + "TheBatKnight" + ChatColor.DARK_GREEN + 
        "--\n " + ChatColor.GRAY + "As " + ChatColor.LIGHT_PURPLE + 
        "KittyKat" + ChatColor.GRAY + 
        " you go solo. You can team up with the " + 
        ChatColor.DARK_GRAY + "BadGuys" + ChatColor.GRAY + 
        " or roam the streets with the heroes." + 
        " Whatever you choose, always watch your back..." }));
      book.setItemMeta(bm);
      catwoman.getInventory().addItem(new ItemStack[] { book });
      bm.setTitle(ChatColor.DARK_RED + "How To Play: " + 
        ChatColor.DARK_PURPLE + "Jester");
      bm.setPages(
        Arrays.asList(new String[] { 
        ChatColor.DARK_GREEN + 
        "--" + 
        ChatColor.GOLD + 
        "TheBatKnight" + 
        ChatColor.DARK_GREEN + 
        "--\n " + 
        ChatColor.GRAY + 
        "As " + 
        ChatColor.DARK_PURPLE + 
        "The Jester" + 
        ChatColor.GRAY + 
        ", you should lead the " + 
        ChatColor.DARK_GRAY + 
        "BadGuys" + 
        ChatColor.GRAY + 
        " to victory. Use the Fate Changer and your Iron Imperators " + 
        "to save you from tricky situations..." }));
      book.setItemMeta(bm);
      joker.getInventory().addItem(new ItemStack[] { book });
      bm.setTitle(ChatColor.DARK_RED + "How To Play: " + 
        ChatColor.DARK_GREEN + "Toxic Ivy");
      bm.setPages(Arrays.asList(new String[] { ChatColor.DARK_GREEN + "--" + 
        ChatColor.GOLD + "TheBatKnight" + ChatColor.DARK_GREEN + 
        "--\n " + ChatColor.GRAY + "As " + ChatColor.DARK_GREEN + 
        "Toxic Ivy" + ChatColor.GRAY + 
        ", you can use your surroundings to your advantage" + 
        ". Climb buildings and posion playe" + 
        "rs with your toxic traits..." }));
      book.setItemMeta(bm);
      posionivy.getInventory().addItem(new ItemStack[] { book });

      bm.setTitle(ChatColor.DARK_RED + "How To Play: " + ChatColor.AQUA + 
        "Dr. Freeze");
      bm.setPages(Arrays.asList(new String[] { ChatColor.DARK_GREEN + "--" + 
        ChatColor.GOLD + "TheBatKnight" + ChatColor.DARK_GREEN + 
        "--\n " + ChatColor.GRAY + "As " + ChatColor.AQUA + 
        "Dr. Freeze" + ChatColor.GRAY + 
        ", you can use the element of " + 
        "distraction to your advantage. " + 
        "Freeze someone on the spot and" + 
        " slaughter them before they can " + 
        "defrost to elimenate your opponents!" }));
      book.setItemMeta(bm);
      mrFreeze.getInventory().addItem(new ItemStack[] { book });
      bm.setTitle(ChatColor.DARK_RED + "How To Play: " + ChatColor.WHITE + 
        "The Puffin");
      bm.setPages(
        Arrays.asList(new String[] { 
        ChatColor.DARK_GREEN + 
        "--" + 
        ChatColor.GOLD + 
        "TheBatKnight" + 
        ChatColor.DARK_GREEN + 
        "--\n " + 
        ChatColor.GRAY + 
        "As " + 
        ChatColor.WHITE + 
        "The Puffin" + 
        ChatColor.GRAY + 
        ", You are the The Puffin! Don't forget you're a villain! " + 
        "Try to steal as many diamonds as you can before time runs out, and remember: Don't trust just anyone! " + 
        "Turn into a chicken to decieve your enemies, and shoot powerful poison at anyone who stands before you!" }));
      book.setItemMeta(bm);
      penguin.getInventory().addItem(new ItemStack[] { book });
    } catch (Exception localException) {
    }
  }

  private void sendMessage(Player player, String message) {
    player.sendMessage(ChatColor.GOLD + message);
  }
}