package com.zafcoding.zackscott.tbn.orginial;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

public class Extras
{
  public static List<String> badWords = new ArrayList();

  public Extras() {
    badWords.clear();
    badWords.add("fuck");
    badWords.add("cunt");
    badWords.add("cunts");
    badWords.add("cunnt");
    badWords.add("cunnts");
    badWords.add("shitty");
    badWords.add("shitt");
    badWords.add("cunt5");
    badWords.add("cunnt5");
    badWords.add("shit");
    badWords.add("nigger");
    badWords.add("nigga");
    badWords.add("n1gger");
    badWords.add("n1gg3r");
    badWords.add("nigg3r");
    badWords.add("gay");
    badWords.add("gays");
    badWords.add("gayboy");
    badWords.add("gayboi");
    badWords.add("faggot");
    badWords.add("fag");
    badWords.add("scottland");
    badWords.add("scotland");
    badWords.add("faggots");
    badWords.add("fags");
    badWords.add("retard");
    badWords.add("slut");
    badWords.add("bitch");
    badWords.add("bich");
    badWords.add("whore");
    badWords.add("kkk");
    badWords.add("ficken");
    badWords.add("scheisse");
    badWords.add("hündin");
    badWords.add("hure");
    badWords.add("hundin");
    badWords.add("jebote");
    badWords.add("govno");
    badWords.add("kucka");
    badWords.add("kurac");
    badWords.add("kurva");
    badWords.add("lul");
    badWords.add("kanker");
    badWords.add("tering");
    badWords.add("godver");
    badWords.add("tyfus");
    badWords.add("godverdomme");
    badWords.add("pleures");
    badWords.add("teef");
    badWords.add("mongool");
    badWords.add("graftering");
    badWords.add("kut");
    badWords.add("klote");
    badWords.add("debiel");
    badWords.add("bastard");
    badWords.add("fucking");
    badWords.add("prick");
    badWords.add("ass");
    badWords.add("wankers");
    badWords.add("penis");
    badWords.add("pussy");
    badWords.add("testicle");
    badWords.add("boner");
    badWords.add("fucker");
    badWords.add("fuckers");
    badWords.add("fuckin");
    badWords.add("bitches");
    badWords.add("assholes");
    badWords.add("asshole");
    badWords.add("dick");
    badWords.add("dickwad");
    badWords.add("wank");
    badWords.add("wankstain");
    badWords.add("vagina");
    badWords.add("damn");
  }

  public static void clearAllInventories() {
    for (Player player : Bukkit.getOnlinePlayers()) {
      player.getInventory().clear();
      ItemStack[] is = { new ItemStack(Material.AIR), 
        new ItemStack(Material.AIR), new ItemStack(Material.AIR), 
        new ItemStack(Material.AIR) };
      player.getInventory().setArmorContents(is);
    }
  }

  public static void updateAllInventories()
  {
    for (Player player : Bukkit.getOnlinePlayers())
      player.updateInventory();
  }

  public static boolean rand(Random r) {
    return ((r.nextBoolean()) && (r.nextInt(50) > r.nextInt(80))) || (
      (r
      .nextBoolean()) && (
      r.nextInt(500) < 200) && (r
      .nextInt(5) == 1));
  }

  public static void broadcastWinner(Player gameWinner, int amountOfDiamonds) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      player.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + 
        "Game Over" + ChatColor.WHITE + "------");
      player.sendMessage(ChatColor.GOLD + "Game Statistics:");

      sendMessage(
        player, 
        "Round Winner: " + ChatColor.GOLD + gameWinner.getName() + 
        " With " + ChatColor.AQUA + 
        String.valueOf(amountOfDiamonds) + ChatColor.GOLD + 
        " Diamonds!");
      sendMessage(player, "BadGuy Deaths: " + ChatColor.GOLD + " " + 
        String.valueOf(TheBatKnight.badGuyDeaths));
    }
  }

  private static void sendMessage(Player player, String message) {
    player.sendMessage(ChatColor.DARK_AQUA + message);
  }

  public static void tauntPlayer(String name) {
    int random = new Random().nextInt(12) + 1;
    switch (random) {
    case 1:
      Bukkit.broadcastMessage(name + 
        ChatColor.RED + 
        " is so fat when she stands on the rainbow she makes skittles.");
      break;
    case 2:
      Bukkit.broadcastMessage(name + ChatColor.RED + 
        "'s so fat, when she stepped on the moon it broke ");
      break;
    case 3:
      Bukkit.broadcastMessage(name + 
        ChatColor.RED + 
        "'s so ugly when you walk into the bank they turn off the cameras. ");
      break;
    case 4:
      Bukkit.broadcastMessage(name + ChatColor.RED + 
        "'s so ugly you have to sneak up on your mirror. ");
      break;
    case 5:
      Bukkit.broadcastMessage(name + ChatColor.RED + 
        "'s so ugly farmers use your picture as a scarecrow. ");
      break;
    case 6:
      Bukkit.broadcastMessage(name + 
        ChatColor.RED + 
        "'s so ugly every time you go out you get chased by the dog catcher. ");
      break;
    case 7:
      Bukkit.broadcastMessage(name + ChatColor.RED + 
        "'s so dumb she locked herself on a motorcycle.");
      break;
    case 8:
      Bukkit.broadcastMessage(name + 
        ChatColor.RED + 
        ", when you were born the doctor took one look at you and slapped your parents.");
      break;
    case 9:
      Bukkit.broadcastMessage(name + 
        ChatColor.RED + 
        "'s ugly.");
      break;
    case 10:
      Bukkit.broadcastMessage(name + 
        ChatColor.RED + 
        "'s so ugly, when you went to the zoo they refused to let you out.");
      break;
    case 11:
      Bukkit.broadcastMessage(name + ChatColor.RED + 
        "'s so ugly, you make blind kids cry.");
      break;
    case 12:
      Bukkit.broadcastMessage(name + ChatColor.RED + 
        "'s so ugly, your reflection gives you the finger.");
    }
  }

  public static String antiSwear(String message)
  {
    String finalMessage = "";

    for (String s : message.split(" ")) {
      if (badWords.contains(s.toLowerCase()))
        finalMessage = finalMessage + randomWord(s.toLowerCase()) + " ";
      else
        finalMessage = finalMessage + s + " ";
    }
    return finalMessage;
  }

  private static String randomWord(String string) {
    int rand = new Random().nextInt(20) + 1;
    if ((string.contains("land")) || (string.contains("land?")) || 
      (string.contains("land.")) || (string.contains("land!")))
      return "...nevermind, I'm an idiot.";
    switch (rand) {
    case 1:
    default:
      return "ORANGES";
    case 2:
      return "POOPY";
    case 3:
      return "BANNANA";
    case 4:
      return "FUDGE NUGGET";
    case 5:
      return "ONIONY MESS";
    case 6:
      return "FREK";
    case 7:
      return "#BLAMEZACK";
    case 8:
      return "ZACK BROKE IT";
    case 9:
      return "HYPERPOLYGLOT";
    case 10:
      return "PNEUMONOULTRAMICROSCOPICSILYCOVOLCANACANIOSIS";
    case 11:
      return "ONLYBURNSWHENIPEE";
    case 12:
      return "JUST THE TIP";
    case 13:
      return "MICKY FICKY";
    case 14:
      return "FUDGE";
    case 15:
      return "BRAINS";
    case 16:
      return "CASTLE";
    case 17:
      return "POUR MOI";
    case 18:
      return "INDEED";
    case 19:
      return "LAMPOST";
    case 20:
    }return "lowercase";
  }

  @Deprecated
  public static void bounceBlock(TheBatKnight plugin, Block b)
  {
    try {
      if (b == null)
        return;
      Material type = b.getType();
      byte data = b.getData();
      Location l = b.getLocation();
      b.setType(Material.AIR);
      FallingBlock fb = b.getWorld().spawnFallingBlock(l, type, data);

      float x = -0.6F + (float)(Math.random() * 2.2D);
      float y = -2.0F + (float)(Math.random() * 5.0D);
      float z = -0.3F + (float)(Math.random() * 1.6D);

      fb.setVelocity(new Vector(x, y, z));
    } catch (Exception localException) {
    }
  }

  public static void removeArmour(Player player) {
    ItemStack[] is = { new ItemStack(Material.AIR), 
      new ItemStack(Material.AIR), new ItemStack(Material.AIR), 
      new ItemStack(Material.AIR) };
    player.getInventory().setArmorContents(is);
  }

  public static void hidePlayer(Player player) {
    for (Player p : Bukkit.getOnlinePlayers())
      p.hidePlayer(player);
  }

  public static void showPlayer(Player player) {
    for (Player p : Bukkit.getOnlinePlayers())
      p.showPlayer(player);
  }

  public static void showPlayers() {
    for (Player p : Bukkit.getOnlinePlayers())
      showPlayer(p);
  }
}