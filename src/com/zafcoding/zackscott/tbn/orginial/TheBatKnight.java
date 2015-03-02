package com.zafcoding.zackscott.tbn.orginial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Fish;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class TheBatKnight extends JavaPlugin
  implements Listener
{
  private static final Logger LOGGER = Logger.getLogger("Minecraft");

  public static int minPlayers = 0;
  public static int timeInSeconds = 120;

  public static boolean canStart = false;
  public static boolean hasStarted = false;
  public static boolean pauseGame = false;
  public static boolean chests = false;
  public static boolean batBlocked = false;
  public static boolean vipOnly = false;
  public static boolean canDamage = false;
  public static boolean canLeap = true;
  public static boolean penguinSpecialActive = true;

  public boolean reset = false;

  public static int badGuyDeaths = 0;
  public static int batmanKills = 0;
  public static int robinKills = 0;

  public static List<String> vips = new ArrayList();
  public static List<String> spectating = new ArrayList();
  public static List<String> gled = new ArrayList();
  public static List<String> gged = new ArrayList();
  public static List<String> skiped = new ArrayList();
  public static List<String> unstuck = new ArrayList();
  public static List<String> endPlayer = new ArrayList();
  public static List<Location> trapChests = new ArrayList();
  public static List<Location> vines = new ArrayList();
  public static List<String> bannedPlayers = new ArrayList();

  public List<String> killOnRespawn = new ArrayList();

  public static HashMap<String, Integer> tauntsSent = new HashMap();
  public static HashMap<String, String> mods = new HashMap();
  public static HashMap<String, Integer> playerScores = new HashMap();
  public static Game game;
  public static Map maps;
  public Fish hookDest = null;
  public Player lastPlayer = null;
  public static Inventory shopInv;
  public Thread loop;
  public Thread endLoop;
  public static GameState gameState;
  public static TheBatKnight main;
  public ArrayList<Material> disallowedBlocks = new ArrayList();
  public int i;
  public int j;
  int message = -1;

  int chestsInt = 1351;

  public static void main(String[] args)
  {
  }

  public TheBatKnight()
  {
    LOGGER.info("[TheBatKnight_DEBUG] Constructor Called");
  }

  public void onEnable() {
    getServer().getMessenger().registerOutgoingPluginChannel(this, 
      "BungeeCord");
    minPlayers = 8;
    this.disallowedBlocks.clear();
    this.disallowedBlocks.add(Material.TNT);
    this.disallowedBlocks.add(Material.PISTON_BASE);
    this.disallowedBlocks.add(Material.PISTON_EXTENSION);
    this.disallowedBlocks.add(Material.PISTON_MOVING_PIECE);
    this.disallowedBlocks.add(Material.PISTON_STICKY_BASE);
    maps = new Map();
    main = this;
    shopInv = Bukkit.createInventory(null, 9, "TheBatKnight Shop!");
    new Extras();

    String worldName = "world";
    File playerFilesDir = new File(worldName + "/players");
    if (playerFilesDir.isDirectory()) {
      String[] playerDats = playerFilesDir.list();
      for (int i = 0; i < playerDats.length; i++) {
        File datFile = new File(playerFilesDir, playerDats[i]);
        datFile.delete();
      }
    }

    this.reset = false;
    spectating.clear();
    saveConfig();
    gameState = new GameState();
    gameState.setGameState(GameState.IN_LOBBY);
    this.loop = new Thread(new GameLoop(this));
    this.endLoop = new Thread(new EndGame(this));
    PluginDescriptionFile pdfFile = getDescription();
    saveConfig();
    setupConfig();
    game = new Game();
    try
    {
      updateVIPs();
      updateMods();
    } catch (Exception e) {
      e.printStackTrace();
    }

    LOGGER.info("[TheBatKnight] Registering Events...");
    getServer().getPluginManager().registerEvents(this, this);
    LOGGER.info("[TheBatKnight] Events Registered!");
    LOGGER.info("[TheBatKnight] Starting Game Loops...");
    startStartGameLoop();
    startSaveConfigLoop(getServer().getScheduler());
    LOGGER.info("[TheBatKnight] Game Loops Started!");
    LOGGER.info("[TheBatKnight] Vips: ");
    Iterator irt = vips.iterator();
    while (irt.hasNext())
      LOGGER.info((String)irt.next());
    System.out.println("--BANNED PLAYERS--");

    startHintsAndTipsLoop();
    try {
      updateScores(true);
    } catch (IOException e) {
      e.printStackTrace();
    }
    getServer().getScheduler().scheduleSyncRepeatingTask(this, 
      new Runnable() {
      public void run() {
        if (!TheBatKnight.hasStarted)
          try {
            TheBatKnight.this.updateScores(false);
            TheBatKnight.this.updateBannedPlayers();
            for (Player p : Bukkit.getOnlinePlayers())
              if (!TheBatKnight.playerScores.containsKey(p.getName()))
                TheBatKnight.playerScores.put(p.getName(), Integer.valueOf(100));
          } catch (IOException e) {
            e.printStackTrace();
          }
      }
    }
    , 60L, 100L);

    LOGGER.info("[" + pdfFile.getName() + " V." + pdfFile.getVersion() + 
      "] Has Been Enabled!");
  }

  public void onDisable() {
    PluginDescriptionFile pdfFile = getDescription();
    saveConfig();
    LOGGER.info("[" + pdfFile.getName() + " V." + pdfFile.getVersion() + 
      "] Has Been Disabled!");
    getServer().getScheduler().cancelAllTasks();
    Extras.badWords.clear();

    if (this.loop != null)
      this.loop.interrupt();
    if (this.endLoop != null)
      this.endLoop.interrupt();
    System.out.println(getDataFolder().getAbsolutePath());
  }

  @EventHandler
  public void throwHook(ProjectileLaunchEvent event)
  {
    if ((event.getEntity() instanceof Fish)) {
      Fish hook = (Fish)event.getEntity();
      if ((hook.getShooter() != null) && 
        ((hook.getShooter() instanceof Player))) {
        this.hookDest = hook;
      }
    }
    if ((event.getEntityType() == EntityType.EGG) && 
      ((event.getEntity().getShooter() instanceof Player))) {
      event.setCancelled(true);
      Player player = (Player)event.getEntity().getShooter();
      player.launchProjectile(Egg.class);
    }
  }

  @EventHandler
  public void onPlayerFish(PlayerFishEvent event)
  {
    event.setExpToDrop(0);
    if ((this.hookDest != null) && 
      (event.getState() == PlayerFishEvent.State.IN_GROUND) && 
      (event.getPlayer().getWorld()
      .getBlockAt(this.hookDest.getLocation()).getType() == Material.CHEST)) {
      Block b = event.getPlayer().getWorld()
        .getBlockAt(this.hookDest.getLocation());
      Chest c = (Chest)b.getState();
      event.getPlayer().getInventory()
        .addItem(c.getInventory().getContents());
      c.getInventory().clear();
      b.setType(Material.AIR);
    }
  }

  @EventHandler
  public void onBlockBreak(BlockBreakEvent event)
  {
    if (!hasStarted)
      event.setCancelled(true);
    else if ((Map.mapId == 4) && 
      (event.getBlock().getType() == Material.IRON_FENCE))
      event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    getServer().getScheduler().scheduleSyncDelayedTask(this, 
      new Runnable() {
      public void run() {
        String worldName = "world";
        File playerFilesDir = new File(worldName + "/players");
        if (playerFilesDir.isDirectory()) {
          String[] playerDats = playerFilesDir.list();
          for (int i = 0; i < playerDats.length; i++) {
            File datFile = new File(playerFilesDir, 
              playerDats[i]);
            datFile.delete();
          }
        }
      }
    }
    , 20L);

    if (event.getPlayer().getServer().getOnlinePlayers().length - 1 >= minPlayers)
      canStart = true;
    else {
      canStart = false;
    }
    Extras.showPlayer(event.getPlayer());
    spectating.remove(event.getPlayer().getName());

    if (hasStarted) {
      if (event.getPlayer() == Game.robin)
        Game.robin = null;
      if (event.getPlayer() == Game.batman) {
        Game.batman = null;
      }
      if ((Bukkit.getOnlinePlayers().length == spectating.size() - 1 + 2) && 
        (Game.batman != null) && (Game.robin != null)) {
        game.stop(this, "heroes");
        return;
      }

      if ((Game.batman == null) && (Game.robin == null)) {
        game.stop(this, "badguys");
        return;
      }

      if ((Bukkit.getOnlinePlayers().length == 2) && 
        (Game.batman != null) && (Game.robin == null))
        game.stop(this, "heroes");
      if ((Bukkit.getOnlinePlayers().length == 2) && (Game.robin != null) && 
        (Game.batman == null))
        game.stop(this, "heroes");
      if ((Bukkit.getOnlinePlayers().length - spectating.size() == 3) && 
        (Game.batman != null) && 
        (Game.robin != null) && 
        (Game.catwoman != null))
        game.stop(this, "heroes");
      if ((Bukkit.getOnlinePlayers().length - spectating.size() == 2) && 
        (Game.robin != null) && 
        (Game.catwoman != null))
        game.stop(this, "heroes");
      if ((Bukkit.getOnlinePlayers().length - spectating.size() == 2) && 
        (Game.catwoman != null) && 
        (Game.batman != null))
        game.stop(this, "heroes");
    }
    else {
      event.setQuitMessage("");
    }
  }

  @EventHandler(ignoreCancelled=true)
  public void onWeatherChange(WeatherChangeEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onEntityDamageEvent(EntityDamageEvent event) {
    Entity e = event.getEntity();
    if ((e instanceof Player)) {
      if (!hasStarted) {
        event.setCancelled(true);
      }
      if ((((Player)e == Game.batman) || ((Player)e == Game.robin) || 
        ((Player)e == Game.catwoman)) && 
        (event.getCause() == EntityDamageEvent.DamageCause.FALL)) {
        event.setCancelled(true);
      }
    }

    if (spectating.contains(((Player)e).getName()))
      event.setCancelled(true);
  }

  public Random randSeed(Random r)
  {
    int i = r.nextInt(101) + 1;
    int j = r.nextInt(101) + 1;
    int min = Math.min(i, j);
    int max = Math.max(i, j);
    while (min < max) {
      min++;
      r.setSeed(r.nextLong());
    }
    return r;
  }

  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event)
  {
    Random r = randSeed(new Random());
    if (canDamage)
    {
      int freeze;
      if (((event.getDamager() instanceof Player)) && 
        ((event.getEntity() instanceof Player))) {
        Player damager = (Player)event.getDamager();
        final Player player = (Player)event.getEntity();

        if (((damager == Game.batman) && (player == Game.robin)) || (
          (damager == Game.robin) && (player == Game.batman) && 
          (damager != Game.catwoman) && (player != Game.catwoman))) {
          event.setCancelled(true);
          return;
        }

        if (spectating.contains(damager.getName())) {
          event.setCancelled(true);
          event.setDamage(0);
          return;
        }
        if (damager == Game.catwoman) {
          player.getInventory().removeItem(new ItemStack[] { 
            new ItemStack(Material.DIAMOND) });
          Game.catwoman.getInventory().addItem(new ItemStack[] { 
            new ItemStack(Material.DIAMOND) });
          Game.catwoman.updateInventory();
          player.updateInventory();
          event.setCancelled(false);
          return;
        }
        if (damager == Game.posionivy) {
          r = randSeed(r);
          if (Extras.rand(r)) {
            int posionTime = r.nextInt() % 7;
            while (posionTime <= 0)
              posionTime = r.nextInt() % 7;
            player.addPotionEffect(new PotionEffect(
              PotionEffectType.POISON, posionTime, r
              .nextBoolean() ? 1 : 2));
          }
        }
        if (damager == Game.mrFreeze) {
          r = randSeed(r);
          if (Extras.rand(r)) {
            freeze = r.nextInt() % 7;
            while (freeze <= 0)
              freeze = r.nextInt() % 7;
            player.addPotionEffect(new PotionEffect(
              PotionEffectType.SLOW, 200, 2));
            ((Player)((Snowball)event.getDamager()).getShooter())
              .sendMessage(ChatColor.DARK_AQUA + "You " + 
              ChatColor.AQUA + "FROZE " + 
              ChatColor.DARK_AQUA + 
              player.getName() + "!");
            Game.mrFreeze.sendMessage(ChatColor.DARK_AQUA + "You " + 
              ChatColor.AQUA + "FROZE " + 
              ChatColor.DARK_AQUA + player.getName() + "!");
            if (player.isFlying())
              player.setAllowFlight(false);
          }
        }
        new Thread(new Runnable() {
          public void run() {
            for (int i = 0; i < 3; i++)
              player.getWorld().playEffect(
                player.getLocation().add(0.0D, 
                1.0D + Math.random(), 0.0D), 
                Effect.SMOKE, 1);
          }
        }).start();
        player.getWorld().playSound(player.getLocation(), Sound.HURT_FLESH, 
          2.0F, 1.0F);
      }

      if (((event.getDamager() instanceof Snowball)) && 
        ((event.getEntity() instanceof Player))) {
        Player player = (Player)event.getEntity();
        player.addPotionEffect(new PotionEffect(
          PotionEffectType.BLINDNESS, 60, 1));

        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 
          100, 2));
        player.sendMessage(ChatColor.DARK_AQUA + "You Have Been " + 
          ChatColor.AQUA + "FROZEN!");
        ((Player)((Snowball)event.getDamager()).getShooter())
          .sendMessage(ChatColor.DARK_AQUA + "You " + 
          ChatColor.AQUA + "FROZE " + 
          ChatColor.DARK_AQUA + player.getName() + "!");
      }
      if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
        if (((Player)((Projectile)event.getDamager()).getShooter() == Game.catwoman) && 
          (!((Projectile)event.getDamager() instanceof Arrow)) && 
          ((event.getEntity() instanceof Player))) {
          Player player = (Player)event.getEntity();
          player.getInventory().remove(
            new ItemStack(Material.DIAMOND, 10));
          Game.catwoman.getInventory().addItem(new ItemStack[] { 
            new ItemStack(Material.DIAMOND, 10) });
          event.setDamage(5.0);
          Game.catwoman.updateInventory();
          player.updateInventory();
          event.setCancelled(false);
          return;
        }
        if (((Projectile)event.getDamager() instanceof Arrow))
        {
          if (((Player)((Projectile)event.getDamager())
            .getShooter()).getInventory().contains(
            Material.SLIME_BALL)) {
            ((Player)event.getEntity())
              .addPotionEffect(new PotionEffect(
              PotionEffectType.POISON, 60, 1));
            int slimeballs = 0;
            ItemStack[] arrayOfItemStack;
            int k = (arrayOfItemStack = ((Player)((Projectile)event
              .getDamager()).getShooter()).getInventory()
              .getContents()).length; for (freeze = 0; freeze < k; freeze++)
            {
              ItemStack is = arrayOfItemStack[freeze];
              if (is.getType() == Material.SLIME_BALL) {
                slimeballs += is.getAmount();
                break;
              }
            }
            ((Player)((Projectile)event.getDamager())
              .getShooter()).getInventory().remove(
              Material.SLIME_BALL);
            ((Player)((Projectile)event.getDamager())
              .getShooter()).getInventory().addItem(new ItemStack[] { 
              new ItemStack(Material.SLIME_BALL, 
              slimeballs - 1) });
            ((Player)((Projectile)event.getDamager())
              .getShooter()).updateInventory();
          }
        }
      }
    }
    else {
      event.setCancelled(true);
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) { Player player = event.getPlayer();
    if ((player == Game.catwoman) && 
      (event.getFrom().getBlockY() < event.getTo().getBlockY()))
    {
      if ((player.getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType()
        .equals(Material.AIR)) && 
        (!player.isFlying()) && (!player.isSneaking()) && (canLeap)) {
        player.setVelocity(player
          .getLocation()
          .getDirection()
          .add(player
          .getLocation()
          .getDirection()
          .multiply(2)
          .setY(player.getVelocity().getY() + 0.25D)));
        canLeap = false;
        getServer().getScheduler().scheduleSyncDelayedTask(
          this, new Runnable() {
          public void run() {
            TheBatKnight.canLeap = true;
          }
        }
        , 60L);
      }
    }
  }

  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event)
  {
    if (!hasStarted)
      Locations.teleportToWaitingCell(event.getPlayer(), this);
    event.getPlayer().setDisplayName(event.getPlayer().getName());
  }

  @EventHandler
  public void onPlayerJoin(final PlayerJoinEvent event) {
    if (playerScores.containsKey(event.getPlayer().getName())) {
      event.getPlayer().setLevel(
        ((Integer)playerScores.get(event.getPlayer().getName())).intValue());
    } else {
      playerScores.put(event.getPlayer().getName(), Integer.valueOf(100));
      event.getPlayer().setLevel(
        ((Integer)playerScores.get(event.getPlayer().getName())).intValue());
    }

    if (event.getPlayer().getName().toLowerCase().contains("zackscott")) {
      Bukkit.broadcastMessage(ChatColor.AQUA + "WELCOME " + 
        ChatColor.DARK_AQUA + "Zack Scott!!!" + ChatColor.GREEN + 
        "\nBE SURE TO ASK HIM LOTS OF QUESTIONS!" + 
        ChatColor.DARK_GREEN + "\n#BlameZack");
    }
    if (!new File("world/players").exists())
      new File("world/players").mkdir();
    event.getPlayer().getInventory().clear();
    event.getPlayer().damage(event.getPlayer().getHealth());
    if (!hasStarted) {
      final Player player = event.getPlayer();
      if (player.getServer().getOnlinePlayers().length >= minPlayers) {
        canStart = true;
      } else {
        getServer().getScheduler().scheduleSyncDelayedTask(this, 
          new Runnable() {
          public void run() {
            player.sendMessage(ChatColor.GOLD + 
              "Wish Your Fellow Players " + 
              ChatColor.GOLD + "Good Luck! " + 
              ChatColor.DARK_AQUA + 
              "Tell Everyone With " + 
              ChatColor.AQUA + "/gl");
          }
        }
        , 10L);
        broadcastTimeUntilStart(true);
      }
      player.setHealth(20);
      player.setFoodLevel(20);

      Collection potionEffects = player
        .getActivePotionEffects();
      while (potionEffects.iterator().hasNext()) {
        player.removePotionEffect(((PotionEffect)potionEffects.iterator().next())
          .getType());
      }
      Locations.teleportToWaitingCell(player, this);

      if ((Bukkit.getOnlinePlayers().length >= Bukkit.getMaxPlayers()) && 
        (timeInSeconds >= 35)) {
        for (Player p : Bukkit.getOnlinePlayers()) {
          p.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + 
            "TheBatKnight" + ChatColor.WHITE + "------");
          p.sendMessage(ChatColor.DARK_GREEN + "The Server Is " + 
            ChatColor.DARK_RED + "FULL!");
          p.sendMessage(ChatColor.DARK_AQUA + "Game Starting In " + 
            ChatColor.GOLD + "30 " + ChatColor.DARK_AQUA + 
            "Seconds!");
        }
        timeInSeconds = 30;
      }
      for (String s : spectating)
        if (Bukkit.getPlayer(s) != null) {
          Player player1 = Bukkit.getPlayer(s);
          event.getPlayer().hidePlayer(player1);
        }
    }
    else {
      Extras.hidePlayer(event.getPlayer());
      if (!vips.contains(event.getPlayer().getName()))
        event.getPlayer().kickPlayer(
          ChatColor.GOLD + "Game Has Already Started!");
      if (gameState.getState() == GameState.IN_GAME) {
        spectating.add(event.getPlayer().getName());
        event.getPlayer().addPotionEffect(
          new PotionEffect(PotionEffectType.INVISIBILITY, 50000, 
          1));
        event.getPlayer().setCanPickupItems(false);
        Locations.teleportToWaitingCell(event.getPlayer(), this);
        getServer().getScheduler().scheduleSyncDelayedTask(this, 
          new Runnable() {
          public void run() {
            TheBatKnight.this.setListName(event.getPlayer(), ChatColor.BLACK + 
              event.getPlayer().getName(), 
              ChatColor.BLACK);
            event.getPlayer().setAllowFlight(true);
            Extras.hidePlayer(event.getPlayer());
          }
        }
        , 60L);
      }
    }

    event.getPlayer().getInventory().clear();
    event.getPlayer().setGameMode(GameMode.ADVENTURE);

    Player player = event.getPlayer();

    String name = player.getPlayerListName();

    if (vips.contains(player.getName())) {
      setListName(player, ChatColor.DARK_AQUA + player.getDisplayName(), 
        ChatColor.DARK_AQUA);
      name = "[" + ChatColor.DARK_AQUA + "VIP" + ChatColor.WHITE + "] " + 
        ChatColor.YELLOW + player.getDisplayName();
    }

    Iterator<String> sss = getConfig().getConfigurationSection("Player Names")
      .getKeys(false).iterator();

    while (sss.hasNext()) {
      String s = (String)sss.next();
      if (player.getName().equalsIgnoreCase(s)) {
        setListName(player, 
          ChatColor.DARK_AQUA + player.getDisplayName(), 
          ChatColor.DARK_AQUA);
        name = "[" + ChatColor.DARK_AQUA + 
          getConfig().getString(new StringBuilder("Player Names.").append(s).toString()) + 
          ChatColor.WHITE + "] " + ChatColor.YELLOW + 
          player.getDisplayName();
        break;
      }
    }
    if (!hasStarted)
      event.setJoinMessage(name + ChatColor.YELLOW + 
        " Joined The Server!");
    else
      event.setJoinMessage("");
  }

  @EventHandler
  public void onPlayerChat(AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    int score = ((Integer)playerScores.get(player.getName())).intValue();
    if (!endPlayer.contains(player.getName())) {
      event.setCancelled(true);

      event.setMessage(Extras.antiSwear(event.getMessage()));
      if ((mods.containsKey(player.getName())) && 
        (!spectating.contains(player.getName()))) {
        if (!player.isOp())
          Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "(" + 
            ChatColor.GRAY + score + ChatColor.DARK_GRAY + 
            ")" + ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + 
            (String)mods.get(player.getName()) + ChatColor.RESET + 
            "] " + ChatColor.YELLOW + player.getName() + 
            ChatColor.RESET + ": " + event.getMessage());
        else
          Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "(" + 
            ChatColor.GRAY + score + ChatColor.DARK_GRAY + 
            ")" + ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + 
            ChatColor.BOLD + 
            (String)mods.get(player.getName()) + ChatColor.RESET + 
            "] " + ChatColor.YELLOW + player.getName() + 
            ChatColor.RESET + ": " + event.getMessage());
        return;
      }
      if ((vips.contains(player.getName())) && 
        (!spectating.contains(player.getName()))) {
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "(" + 
          ChatColor.GRAY + score + ChatColor.DARK_GRAY + ")" + 
          ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "VIP" + 
          ChatColor.WHITE + "] " + ChatColor.YELLOW + 
          player.getDisplayName() + ChatColor.WHITE + ": " + 
          event.getMessage());
        return;
      }

      if (spectating.contains(player.getName()))
      {
        if (!gameState.getState().equalsIgnoreCase(
          GameState.GAME_RESETING)) {
          event.setCancelled(true);

          for (Player p : Bukkit.getOnlinePlayers())
            if ((spectating.contains(p.getName())) || 
              (mods.containsKey(player.getName())))
              p.sendMessage(ChatColor.DARK_GRAY + "(" + 
                ChatColor.GRAY + score + ChatColor.DARK_GRAY + 
                ")" + ChatColor.WHITE + "[" + ChatColor.RED + 
                "DEAD" + ChatColor.WHITE + "] " + 
                ChatColor.YELLOW + player.getDisplayName() + 
                ChatColor.WHITE + ": " + event.getMessage());
          return;
        }
      }
      event.setCancelled(true);
      Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "(" + ChatColor.GRAY + 
        score + ChatColor.DARK_GRAY + ")" + ChatColor.YELLOW + 
        player.getDisplayName() + ChatColor.WHITE + ": " + 
        event.getMessage());
    }
  }

  public void setListName(Player player, String name, ChatColor C) {
    String s = name;
    if (name.toCharArray().length >= 14) {
      s = C + s;
      for (int i = 0; i <= 13; i++)
        s = C + s + name.toCharArray()[i];
    }
    player.setPlayerListName(s);
  }

  @EventHandler
  public void onPlayerChangeItem(PlayerItemHeldEvent event) {
    try {
      if (event.getPlayer() == Game.batman) {
        if (event.getPlayer().getInventory()
          .getItem(event.getNewSlot()).getType() == Material.FEATHER)
          Game.batman.setAllowFlight(true);
        else {
          Game.batman.setAllowFlight(false);
        }
      }
      if (event.getPlayer() == Game.robin) {
        if (event.getPlayer().getInventory()
          .getItem(event.getNewSlot()).getType() == Material.FEATHER)
          Game.robin.setAllowFlight(true);
        else
          Game.robin.setAllowFlight(false);
      }
      if (event.getPlayer() == Game.catwoman) {
        if (event.getPlayer().getInventory()
          .getItem(event.getNewSlot()).getType() == Material.MILK_BUCKET)
          getServer().dispatchCommand(getServer().getConsoleSender(), 
            "d " + event.getPlayer().getName() + " ocelot");
        else {
          getServer().dispatchCommand(getServer().getConsoleSender(), 
            "ud " + event.getPlayer().getName());
        }
      }
      if (event.getPlayer() == Game.penguin)
        if (event.getPlayer().getInventory()
          .getItem(event.getNewSlot()).getType() == Material.RAW_CHICKEN)
          getServer().dispatchCommand(getServer().getConsoleSender(), 
            "d " + event.getPlayer().getName() + " chicken");
        else
          getServer().dispatchCommand(getServer().getConsoleSender(), 
            "ud " + event.getPlayer().getName());
    }
    catch (Exception localException)
    {
    }
  }

  @EventHandler
  public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
    if (gameState.getState() == GameState.GAME_RESETING) {
      event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.RED + "Game Is " + 
        ChatColor.DARK_RED + "RESETING\n" + ChatColor.YELLOW + 
        "Try Again In A Few Seconds!");
      event.setKickMessage(ChatColor.RED + "Game Is " + 
        ChatColor.DARK_RED + "RESETING\n" + ChatColor.YELLOW + 
        "Try Again In A Few Seconds!");
      return;
    }

    if ((hasStarted) && (!vips.contains(event.getName()))) {
      event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, ChatColor.GOLD + 
        "Game Has Already Started!");
      event.setKickMessage(ChatColor.GOLD + "Game Has Already Started!");
    }
    if ((vips.contains(event.getName())) && 
      (Bukkit.getOnlinePlayers().length >= Bukkit.getMaxPlayers()) && 
      (gameState.getState() != GameState.IN_GAME)) {
      int rand = new Random()
        .nextInt(Bukkit.getOnlinePlayers().length - 1);

      while (vips.contains(Bukkit.getServer().getOnlinePlayers()[rand]
        .getName())) {
        rand = new Random()
          .nextInt(Bukkit.getOnlinePlayers().length - 1);
      }
      Bukkit.getServer().getOnlinePlayers()[rand]
        .kickPlayer(ChatColor.GOLD + 
        "Kicked So A Player With A Higer Rank Could Join!\n" + 
        ChatColor.AQUA + "Get VIP Access At " + 
        ChatColor.DARK_AQUA + "www.TheBatKnight.com" + 
        ChatColor.AQUA + "!");
      event.allow();
      return;
    }
    if ((vips.contains(event.getName())) && 
      (gameState.getState() != GameState.IN_GAME)) {
      event.allow();
    }

    if ((!vips.contains(event.getName())) && (vipOnly)) {
      event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, ChatColor.AQUA + 
        "This Server Is In VIP Mode!" + ChatColor.DARK_RED + 
        "RESETING\n" + ChatColor.YELLOW);
      event.setKickMessage(ChatColor.RED + "Game Is " + 
        ChatColor.DARK_RED + "RESETING\n" + ChatColor.YELLOW + 
        "Get VIP At: " + ChatColor.GRAY + 
        "\nwww.TheBatKnight.com");
    }
    if (bannedPlayers.contains(event.getName())) {
      event.disallow(
        AsyncPlayerPreLoginEvent.Result.KICK_BANNED, 
        ChatColor.DARK_RED + 
        "You did something you weren't supposed to and were caught by a mod. \n" + 
        ChatColor.RED + 
        "If you feel this ban is incorrect, please go to" + 
        ChatColor.DARK_AQUA + " forums.thebatknight.com" + 
        ChatColor.RED + 
        " and post your side of the story.\n");
      event.setKickMessage(ChatColor.DARK_RED + 
        "You did something you weren't supposed to and were caught by a mod. \n" + 
        ChatColor.RED + 
        "If you feel this ban is incorrect, please go to" + 
        ChatColor.DARK_AQUA + " forums.thebatknight.com" + 
        ChatColor.RED + " and post your side of the story.\n");
    }
  }

  @EventHandler
  public void playerThrowEggEvent(PlayerEggThrowEvent event) {
    event.setHatching(true);
    event.setHatchingType(EntityType.CHICKEN);
  }

  @EventHandler
  public void onPlayerEnchant(EnchantItemEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onServerListPing(ServerListPingEvent event)
  {
    event.setMotd(gameState.getState());
  }

  @EventHandler
  public void onPreCommand(PlayerCommandPreprocessEvent event) {
    String command = event.getMessage().replace("/", "");
    Player player = event.getPlayer();
    if (!player.isOp()) {
      if (command.equalsIgnoreCase("help")) {
        player.sendMessage(ChatColor.WHITE + "------" + ChatColor.GOLD + 
          "TheBatKnight" + ChatColor.WHITE + "------");
        player.sendMessage(ChatColor.GOLD + "Welcome To " + 
          ChatColor.DARK_AQUA + "TheBatKnight" + ChatColor.GOLD + 
          "!");
        player.sendMessage(ChatColor.GOLD + "A Custom Plugin Made By " + 
          ChatColor.DARK_AQUA + "TheBCBroz" + ChatColor.GOLD + 
          "!");
        event.setCancelled(true);
      }
      if ((command.equalsIgnoreCase("pl")) || 
        (command.equalsIgnoreCase("plugins"))) {
        player.sendMessage(ChatColor.RED + 
          "You could do that, but you're going to have to pursuade me first...");
        event.setCancelled(true);
      }
      if (command.toLowerCase().contains("kill")) {
        player.sendMessage(ChatColor.RED + 
          "Don't Be So Inconsiderate To Other Players!");
        event.setCancelled(true);
      }
      if (command.toLowerCase().startsWith("me")) {
        player.sendMessage(ChatColor.RED + 
          "You could do that, but you're going to have to pursuade me first...");
        event.setCancelled(true);
      }
    }
    if ((mods.containsKey(player.getName())) && 
      (command.toLowerCase().startsWith("ban"))) {
      event.setCancelled(true);
      player.sendMessage("Usage: /batban [player] [reason]");
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    try {
      if (((event.getPlayer() == Game.batman) || (event.getPlayer() == Game.robin)) && 
        (!spectating.contains(event.getPlayer().getName())) && 
        (event.getClickedBlock().getType() == Material.CHEST)) {
        event.setCancelled(true);
      }
      if (spectating.contains(event.getPlayer().getName())) {
        if (vips.contains(event.getPlayer().getName())) {
          if ((event.getAction() == Action.LEFT_CLICK_AIR) || 
            (event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            int rand = new Random().nextInt(
              Bukkit.getOnlinePlayers().length - 1);

            while ((spectating
              .contains(Bukkit.getOnlinePlayers()[rand]
              .getName())) || 
              (Bukkit.getOnlinePlayers()[rand] == event
              .getPlayer())) {
              rand = new Random().nextInt(
                Bukkit.getOnlinePlayers().length - 1);
            }
            event.getPlayer().teleport(
              Bukkit.getOnlinePlayers()[rand]);
            event.getPlayer().sendMessage(
              ChatColor.GOLD + 
              "Teleported To: " + 
              ChatColor.DARK_AQUA + 
              Bukkit.getOnlinePlayers()[rand]
              .getDisplayName() + 
              ChatColor.GOLD + "!");
          } else {
            event.setCancelled(true);
          }
        }
        else event.setCancelled(true);

      }

      if (event.getAction() == Action.PHYSICAL) {
        if (spectating.contains(event.getPlayer().getName()))
          event.setCancelled(true);
      }
      else if ((event.getAction() == Action.LEFT_CLICK_BLOCK) && 
        (event.getClickedBlock().getType() == Material.CHEST) && 
        (event.getPlayer() != Game.batman) && 
        (event.getPlayer() != Game.robin)) {
        event.getClickedBlock().setType(Material.AIR);
      }

      if (event.getItem().getType() == Material.getMaterial(349)) {
        final Entity e = event
          .getClickedBlock()
          .getWorld()
          .spawnEntity(event.getClickedBlock().getLocation(), 
          EntityType.OCELOT);
        getServer().getScheduler().scheduleSyncDelayedTask(this, 
          new Runnable() {
          public void run() {
            e.remove();
          }
        }
        , 200L);
      }

      if ((event.getItem().getType() == Material.MILK_BUCKET) || 
        (event.getItem().getType() == Material.getMaterial(349))) {
        event.setCancelled(true);
        event.setUseItemInHand(Event.Result.DENY);
      }

      if ((event.getItem().getType() == Material.IRON_HOE) && (
        (event.getAction() == Action.LEFT_CLICK_AIR) || 
        (event.getAction() == Action.LEFT_CLICK_BLOCK))) {
        event.setCancelled(false);
        if (penguinSpecialActive) {
          final Player player = event.getPlayer();
          ItemStack item = new ItemStack(Material.NETHER_STAR);
          final Item i = player.getWorld().dropItem(
            player.getLocation(), item);
          i.setVelocity(player.getLocation().getDirection()
            .multiply(3.0F));
          i.setPickupDelay(50000);
          penguinSpecialActive = false;
          getServer().getScheduler().scheduleSyncDelayedTask(
            this, new Runnable() {
            public void run() {
              player.getWorld().createExplosion(
                i.getLocation(), 3.8F);
              i.remove();
              TheBatKnight.penguinSpecialActive = true;
            }
          }
          , 40L);
        }
      }
    }
    catch (Exception localException) {
    }
  }

  @EventHandler
  public void onItemDrop(PlayerDropItemEvent event) {
    if (event.getItemDrop().getItemStack().getType() == Material.DIAMOND) {
      event.setCancelled(true);
      event.getPlayer()
        .sendMessage(
        ChatColor.RED + 
        "Why Would You Want To Throw Away Your Diamonds?!?!?!");
    }
    if (event.getItemDrop().getItemStack().getType() == Material.FEATHER) {
      event.setCancelled(true);
      event.getPlayer()
        .sendMessage(
        ChatColor.RED + 
        "Why Would You Want To Throw Away Your Feather?!?!?!");
    }
    if (event.getItemDrop().getItemStack().getType() == Material.GHAST_TEAR) {
      event.setCancelled(true);
      event.getPlayer()
        .sendMessage(
        ChatColor.RED + 
        "Why Would You Want To Throw Away Your BatBlocker?!?!?!");
    }
  }

  @EventHandler
  public void onBlockBurn(BlockBurnEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onTagRecieve(PlayerReceiveNameTagEvent event) {
    if (event.getNamedPlayer() == Game.batman) {
      event.setTag(ChatColor.DARK_GRAY + event.getNamedPlayer().getName());
      setListName(Game.batman, ChatColor.DARK_GRAY + 
        event.getNamedPlayer().getName(), ChatColor.DARK_GRAY);
    } else if (event.getNamedPlayer() == Game.robin) {
      event.setTag(ChatColor.GREEN + event.getNamedPlayer().getName());
      setListName(Game.robin, ChatColor.GREEN + 
        event.getNamedPlayer().getName(), ChatColor.GREEN);
    } else if (event.getNamedPlayer() == Game.joker) {
      event.setTag(ChatColor.DARK_PURPLE + 
        event.getNamedPlayer().getName());
      setListName(Game.joker, ChatColor.DARK_PURPLE + 
        event.getNamedPlayer().getName(), ChatColor.DARK_PURPLE);
    } else if ((event.getNamedPlayer() == Game.catwoman) && (hasStarted)) {
      event.setTag(ChatColor.LIGHT_PURPLE + 
        event.getNamedPlayer().getName());
      setListName(Game.catwoman, ChatColor.LIGHT_PURPLE + 
        event.getNamedPlayer().getName(), ChatColor.LIGHT_PURPLE);
    } else if ((event.getNamedPlayer() == Game.posionivy) && (hasStarted)) {
      event.setTag(ChatColor.DARK_GREEN + 
        event.getNamedPlayer().getName());
      setListName(Game.posionivy, ChatColor.DARK_GREEN + 
        event.getNamedPlayer().getName(), ChatColor.DARK_GREEN);
    } else if ((event.getNamedPlayer() == Game.mrFreeze) && (hasStarted)) {
      event.setTag(ChatColor.AQUA + event.getNamedPlayer().getName());
      setListName(Game.mrFreeze, ChatColor.AQUA + 
        event.getNamedPlayer().getName(), ChatColor.AQUA);
    } else if ((event.getNamedPlayer() == Game.penguin) && (hasStarted)) {
      event.setTag(ChatColor.WHITE + event.getNamedPlayer().getName());
      setListName(Game.penguin, ChatColor.WHITE + 
        event.getNamedPlayer().getName(), ChatColor.WHITE);
    } else if (spectating.contains(event.getNamedPlayer().getName())) {
      event.setTag(ChatColor.BLACK + event.getNamedPlayer().getName());
    }
    else if ((vips.contains(event.getNamedPlayer().getName())) && (!hasStarted)) {
      event.setTag(ChatColor.AQUA + event.getNamedPlayer().getName());
    } else {
      event.setTag(ChatColor.YELLOW + 
        event.getNamedPlayer().getName());
      setListName(event.getNamedPlayer(), ChatColor.YELLOW + 
        event.getNamedPlayer().getName(), ChatColor.YELLOW);
    }
  }

  @EventHandler
  public void onPlayerDeath(PlayerDeathEvent event)
  {
    event.setDroppedExp(0);
    event.setKeepLevel(true);
    System.out.print("Death Loop ");
    if (hasStarted) {
      System.out.println("Started.");
      Player player = event.getEntity();
      if ((player.getKiller() != null) && (
        (player.getKiller() == Game.batman) || (player.getKiller() == Game.robin))) {
        int kills = ((Integer)playerScores.get(player.getKiller()
          .getName())).intValue();

        playerScores.put(player.getKiller().getName(), 
          Integer.valueOf(kills + 1));
        player.getKiller().sendMessage(
          ChatColor.GREEN + "You Gained " + ChatColor.DARK_GREEN + 
          "1 " + ChatColor.GREEN + 
          "MinerCoins For Killing " + ChatColor.GRAY + 
          "BatKnight" + "!");
      }

      int kills = ((Integer)playerScores.get(player.getName())).intValue();
      playerScores.put(player.getName(), 
        Integer.valueOf(kills - 5));

      player.sendMessage(ChatColor.RED + "You Lost " + ChatColor.DARK_RED + 
        "5 " + ChatColor.RED + "MinerCoins For Getting Killed!");
      player.setCanPickupItems(false);
      List diamondLore = new ArrayList();
      diamondLore.add(ChatColor.DARK_AQUA + "Collect The Most " + 
        ChatColor.AQUA + "Diamonds " + ChatColor.DARK_AQUA + 
        "To Win!");
      player.getWorld().dropItem(
        player.getLocation(), 
        game.setName(
        new ItemStack(Material.DIAMOND, new Random()
        .nextInt(20)), ChatColor.AQUA + "Diamond", 
        diamondLore));

      player.sendMessage(ChatColor.RED + 
        "You can now only talk to other dead players until the next game starts!");
      if (!vips.contains(player.getName()))
        player.setAllowFlight(false);
      if ((player != Game.batman) && (player != Game.robin)) {
        this.lastPlayer = player;
      }
      if (player == Game.batman) {
        int deathDiaB = new Random().nextInt(50) + 1;
        while (deathDiaB < 15)
          deathDiaB = new Random().nextInt(50) + 1;
        Game.batman = null;
        player.getWorld().dropItem(
          event.getEntity().getLocation(), 
          game.setName(
          new ItemStack(Material.DIAMOND, deathDiaB), 
          ChatColor.AQUA + "Diamond", diamondLore));
        if (player.getKiller() != null)
        {
          int kills1 = ((Integer)playerScores.get(player
            .getKiller().getName())).intValue();

          playerScores.put(player.getKiller()
            .getName(), Integer.valueOf(kills1 + 3));
          player.getKiller().sendMessage(
            ChatColor.GREEN + "You Gained " + 
            ChatColor.DARK_GREEN + "5 " + 
            ChatColor.GREEN + 
            "MinerCoins For Killing " + 
            ChatColor.GRAY + "BatKnight" + "!");
        }

      }

      if (player == Game.robin) {
        int deathDiaR = new Random().nextInt(40) + 1;
        while (deathDiaR < 12)
          deathDiaR = new Random().nextInt(50) + 1;
        Game.robin = null;
        player.getWorld().dropItem(
          event.getEntity().getLocation(), 
          game.setName(
          new ItemStack(Material.DIAMOND, deathDiaR), 
          ChatColor.AQUA + "Diamond", diamondLore));
        if (player.getKiller() != null)
        {
          int kills1 = ((Integer)playerScores.get(player
            .getKiller().getName())).intValue();

          playerScores.put(player.getKiller()
            .getName(), Integer.valueOf(kills1 + 3));
          player.getKiller().sendMessage(
            ChatColor.GREEN + "You Gained " + 
            ChatColor.DARK_GREEN + "5 " + 
            ChatColor.GREEN + 
            "MinerCoins For Killing " + 
            ChatColor.GREEN + "BirdBoy" + "!");
        }
      }

      if (player == Game.catwoman)
        Game.catwoman = null;
      if (player == Game.joker)
        Game.joker = null;
      if (player == Game.posionivy)
        Game.posionivy = null;
      if (player == Game.mrFreeze)
        Game.mrFreeze = null;
      event.getEntity().getInventory().clear();
      event.getDrops().clear();
      if (vips.contains(player.getName())) {
        Locations.teleportToWaitingCell(player, this);
        player.addPotionEffect(new PotionEffect(
          PotionEffectType.INVISIBILITY, 50000, 1));
        spectating.add(player.getName());
        player.setAllowFlight(true);
      } else {
        spectating.add(player.getName());
        Locations.teleportToWaitingCell(player, this);
      }
      Extras.hidePlayer(player);

      if ((hasStarted) && (player != Game.batman) && (player != Game.robin))
        if ((Game.batman != null) && (Game.robin != null)) {
          event.setDeathMessage(player.getDisplayName() + 
            ChatColor.GOLD + 
            " Was Killed! " + 
            ChatColor.DARK_AQUA + 
            String.valueOf(Bukkit.getOnlinePlayers().length - 
            spectating.size() - 2) + ChatColor.GOLD + 
            " Remain!");
        } else if ((Game.batman != null) || (Game.robin != null)) {
          event.setDeathMessage(player.getDisplayName() + 
            ChatColor.GOLD + 
            " Was Killed! " + 
            ChatColor.DARK_AQUA + 
            String.valueOf(Bukkit.getOnlinePlayers().length - 
            spectating.size() - 1) + ChatColor.GOLD + 
            " Remain!");
        } else {
          game.stop(this, "badguys");
          return;
        }
      if ((Bukkit.getOnlinePlayers().length == spectating.size() + 2) && 
        (Game.batman != null) && (Game.robin != null)) {
        game.stop(this, "heroes");
        return;
      }
      if ((Bukkit.getOnlinePlayers().length == spectating.size() + 1) && 
        (Game.batman == null) && (Game.robin != null)) {
        game.stop(this, "heroes");
        return;
      }
      if ((Bukkit.getOnlinePlayers().length == spectating.size() + 1) && 
        (Game.batman != null) && (Game.robin == null)) {
        game.stop(this, "heroes");
        return;
      }

      if ((Game.batman == null) && (Game.robin == null)) {
        game.stop(this, "badguys");
        return;
      }

      if (Bukkit.getOnlinePlayers().length - spectating.size() - 1 == 0) {
        game.stop(this, "heroes");
        return;
      }

      setListName(player, ChatColor.BLACK + player.getName(), 
        ChatColor.BLACK);
      System.out.println("Ended");
    } else {
      System.out.println("Failure To Register Death");
      Locations.teleportToWaitingCell(event.getEntity(), this);
    }
  }

  @EventHandler
  public void onHungerChange(FoodLevelChangeEvent event) {
    if ((event.getEntity() instanceof Player))
      ((Player)event.getEntity()).setSaturation(20.0F);
    event.setCancelled(true);
  }

  @EventHandler(priority=EventPriority.NORMAL)
  public void onBlockExplode(EntityExplodeEvent e)
  {
    if (e.isCancelled())
      return;
    if (e.blockList().isEmpty())
      return;
    e.setYield(2.0F);
    double x = 0.0D;
    double y = 0.0D;
    double z = 0.0D;
    Location eLoc = e.getLocation();
    World w = eLoc.getWorld();
    for (int i = 0; i < e.blockList().size(); i++) {
      b = (Block)e.blockList().get(i);
      Location bLoc = b.getLocation();
      if (!this.disallowedBlocks.contains(b.getType()))
      {
        x = bLoc.getX() - eLoc.getX();
        y = bLoc.getY() - eLoc.getY() + 0.5D;
        z = bLoc.getZ() - eLoc.getZ();
        FallingBlock fb = w.spawnFallingBlock(bLoc, b.getType(), 
          b.getData());
        fb.setDropItem(false);
        fb.setVelocity(new Vector(x, y, z));
      }
    }

    Block b = e.getLocation().getWorld()
      .spawnEntity(e.getLocation(), EntityType.BAT)
      .getNearbyEntities(8.0D, 8.0D, 8.0D).iterator();

    while (b.hasNext())
    {
      Entity e1 = (Entity)b.next();
      if ((e1 != null) && 
        ((e1 instanceof Player))) {
        ((Player)e1).addPotionEffect(new PotionEffect(
          PotionEffectType.POISON, 60, 1));
        e1.setVelocity(e1.getVelocity().setY(
          e1.getVelocity().getY() + 1.0D));
      }
    }
  }

  @EventHandler
  public void onContainerOpen(InventoryOpenEvent event) {
    try {
      if (spectating.contains(event.getPlayer().getName())) {
        event.setCancelled(true);
        event.getPlayer().closeInventory();
        return;
      }

      if (event.getInventory().getType() == InventoryType.CHEST)
      {
        if (trapChests.contains(
          ((Chest)event.getView()
          .getTopInventory().getHolder()).getBlock()
          .getLocation())) {
          Chest c = (Chest)event.getView().getTopInventory().getHolder();
          event.setCancelled(true);
          c.getInventory().clear();
          c.getBlock().setType(Material.AIR);
          c.getBlock().getWorld().createExplosion(c.getLocation(), 2.0F);
          c.getBlock()
            .getWorld()
            .playEffect(c.getBlock().getLocation(), 
            Effect.ENDER_SIGNAL, 1);
          if (Game.joker != null)
            Game.joker.sendMessage(ChatColor.GOLD + 
              "POP GOES THE VILLAIN!");
          ((Player)event.getPlayer()).sendMessage(ChatColor.RED + 
            "IT'S A TRAP!");
          ((Player)event.getPlayer()).damage(event.getPlayer()
            .getHealth() / new Random().nextInt(2) + 2);
          ((Player)event.getPlayer()).addPotionEffect(new PotionEffect(
            PotionEffectType.BLINDNESS, 5, 1));
          ((Player)event.getPlayer()).addPotionEffect(new PotionEffect(
            PotionEffectType.CONFUSION, 6, 1));
          ((Player)event.getPlayer()).addPotionEffect(new PotionEffect(
            PotionEffectType.SLOW, 10, 1));
          Extras.removeArmour((Player)event.getPlayer());
        }
      }
    } catch (Exception localException) {
    }
  }

  @EventHandler
  public void onPlayerBedEnter(PlayerBedEnterEvent event) { event.setCancelled(true); }

  private void setupConfig()
  {
    FileConfiguration config = getConfig();
    try {
      LOGGER.info("[TheBatKnight] Setting Up Config...");
      if ((!new File(getDataFolder(), "RESET.FILE").exists()) || 
        (!new File(getDataFolder(), "config.yml").exists())) {
        LOGGER.info("[TheBatKnight] Reset File Not Found! Reseting Default Config Values!");

        config.options().copyDefaults(true);

        saveConfig();

        if (!new File(getDataFolder(), "chests.txt").exists()) {
          new File(getDataFolder(), "chests.txt").createNewFile();
        }
        LOGGER.info("[TheBatKnight] Configuration Saved!");
      }

      LOGGER.info("[TheBatKnight] Configuration Loaded!");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  void startStartGameLoop() {
    getServer().getScheduler().scheduleSyncRepeatingTask(this, 
      new GameLoop(this), 20L, 20L);
  }

  private void startSaveConfigLoop(BukkitScheduler scheduler) {
    scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
      public void run() {
        TheBatKnight.this.saveConfig();
      }
    }
    , 1200L, 1200L);
  }

  public void broadcastTimeUntilStart(boolean extras) {
    for (Player player : Bukkit.getOnlinePlayers()) {
      player.sendMessage("");
      if (timeInSeconds % 60 == 0) {
        if (timeInSeconds == 60)
          player.sendMessage(ChatColor.RED + 
            String.valueOf(timeInSeconds / 60) + 
            ChatColor.GOLD + " Minute Until The Game Starts!");
        else {
          player.sendMessage(ChatColor.RED + 
            String.valueOf(timeInSeconds / 60) + 
            ChatColor.GOLD + 
            " Minutes Until The Game Starts!");
        }
      }
      else if (timeInSeconds == 1)
      {
        player.sendMessage(ChatColor.RED + 
          String.valueOf(timeInSeconds) + ChatColor.GOLD + 
          " Second Until The Game Starts!");
      }
      else player.sendMessage(ChatColor.RED + 
          String.valueOf(timeInSeconds) + ChatColor.GOLD + 
          " Seconds Until The Game Starts!");

      if (extras) {
        player.sendMessage(ChatColor.GOLD + "Map: " + ChatColor.RED + 
          maps.getMapName());
        player.sendMessage(ChatColor.GOLD + 
          String.valueOf(getServer().getOnlinePlayers().length) + 
          ChatColor.WHITE + "/" + ChatColor.GOLD + 
          String.valueOf(getServer().getMaxPlayers()));
        String message = ChatColor.GOLD + "Minimum Players Needed: ";
        String minimumPlayersNeeded = 
          ChatColor.DARK_RED + "8";
        message = message + minimumPlayersNeeded;
        player.sendMessage(message);
        if (Map.canSkip)
          player.sendMessage(ChatColor.GOLD + "Skip Votes: " + 
            ChatColor.DARK_AQUA + Map.skipvotes + "/" + (
            Bukkit.getOnlinePlayers().length / 2 + 2));
      }
    }
  }

  protected void broadcastTimeUntilEnd(int timeLeft, String time) {
    for (Player player : Bukkit.getOnlinePlayers())
      player.sendMessage(ChatColor.RED + String.valueOf(timeLeft) + 
        ChatColor.GOLD + " " + time + " Until The Game Ends!");
  }

  public void startGame()
  {
    if (canStart) {
      game.start(this, Map.mapId, Map.mapName);
      hasStarted = true;
    }
  }

  public void startGameEndTask()
  {
    if (Map.mapId == 0)
      this.i = 600;
    else if (Map.mapId == 1)
      this.i = 360;
    else {
      this.i = 600;
    }
    this.j = getServer().getScheduler().scheduleSyncRepeatingTask(this, 
      new Runnable() {
      public void run() {
        if (TheBatKnight.this.i == 0)
          TheBatKnight.game.stop(GameLoop.plugin, "unknown");
        if ((TheBatKnight.gameState.getState().equals(GameState.IN_GAME)) && 
          (TheBatKnight.this.i != 0))
          if ((TheBatKnight.this.i % 60 == 0) && (TheBatKnight.this.i > 50))
            TheBatKnight.this.broadcastTimeUntilEnd(TheBatKnight.this.i / 60, "Minutes");
          else if (TheBatKnight.this.i <= 10)
            TheBatKnight.this.broadcastTimeUntilEnd(TheBatKnight.this.i, "Seconds");
        TheBatKnight.this.i -= 1;
      }
    }
    , 20L, 20L);
  }

  @EventHandler
  public void onItemPickup(PlayerPickupItemEvent event)
  {
    if (((event.getPlayer() == Game.batman) || (event.getPlayer() == Game.robin)) && 
      (event.getItem().getItemStack().getType() == Material.DIAMOND))
      event.setCancelled(true);
  }

  @EventHandler
  public void onContainerClose(InventoryCloseEvent event)
  {
    try
    {
      if (event.getView().getType() == InventoryType.CHEST) {
        Chest c = (Chest)event.getView().getTopInventory().getHolder();
        Inventory inv = c.getInventory();

        boolean hasEmptySlots = true;
        for (ItemStack stack : inv.getContents()) {
          if (stack != null) {
            hasEmptySlots = false;
            break;
          }
        }

        if (hasEmptySlots)
          c.getBlock().setType(Material.AIR);
      }
    }
    catch (Exception localException) {
    }
  }

  public void dosomeDiamondLvl() {
    getServer().getScheduler().scheduleSyncRepeatingTask(this, 
      new Runnable() {
      public void run() {
        if (TheBatKnight.hasStarted)
          for (Player p : Bukkit.getOnlinePlayers())
            p.setLevel(TheBatKnight.this.getDiamonds(p));
      }
    }
    , 120L, 40L);
  }

  public int getDiamonds(Player player) {
    int total = 0;
    for (ItemStack is : player.getInventory()) {
      if ((is != null) && 
        (is.getType() == Material.DIAMOND)) {
        total += is.getAmount();
      }
    }
    return total;
  }

  public void startCompassEngine() {
    getServer().getScheduler().scheduleSyncRepeatingTask(this, 
      new Runnable() {
      public void run() {
        if (TheBatKnight.hasStarted)
          if (!TheBatKnight.batBlocked)
            try {
              if (Game.batman != null) {
                List entities = Game.batman
                  .getNearbyEntities(500.0D, 500.0D, 
                  500.0D);
                for (Entity e : entities)
                  if ((e instanceof Player)) {
                    Player p = (Player)e;
                    if ((p != Game.robin) && 
                      (p != Game.catwoman))
                    {
                      if (!TheBatKnight.spectating.contains(p
                        .getName()))
                        Game.batman.setCompassTarget(p
                          .getLocation());
                    }
                  }
              }
              if (Game.robin != null) {
                List entities = Game.robin
                  .getNearbyEntities(500.0D, 500.0D, 
                  500.0D);
                for (Entity e : entities)
                  if ((e instanceof Player)) {
                    Player p = (Player)e;
                    if ((p != Game.batman) && 
                      (p != Game.catwoman))
                    {
                      if (!TheBatKnight.spectating.contains(p
                        .getName()))
                        Game.robin.setCompassTarget(p
                          .getLocation());
                    }
                  }
              }
              if (Game.catwoman == null) return;
              List entities = Game.catwoman
                .getNearbyEntities(500.0D, 500.0D, 
                500.0D);
              for (Entity e : entities)
                if ((e instanceof Player)) {
                  Player p = (Player)e;
                  if ((p != Game.robin) && 
                    (p != Game.batman))
                  {
                    if (!TheBatKnight.spectating.contains(p
                      .getName()))
                      Game.catwoman.setCompassTarget(p
                        .getLocation());
                  }
                }
            }
            catch (Exception e) {
              System.out.println("ERR_" + e.getMessage());
            }
          else
            try {
              if (Game.batman != null)
                Game.batman
                  .setCompassTarget(new Location(
                  Game.batman.getWorld(), 
                  Game.batman
                  .getLocation()
                  .getX(), 
                  Game.batman
                  .getLocation()
                  .getY(), 
                  Game.batman
                  .getLocation()
                  .getZ()));
              if (Game.robin != null)
                Game.robin
                  .setCompassTarget(new Location(
                  Game.robin.getWorld(), 
                  Game.robin
                  .getLocation()
                  .getX(), 
                  Game.robin
                  .getLocation()
                  .getY(), 
                  Game.robin
                  .getLocation()
                  .getZ()));
              if (Game.catwoman != null)
                Game.catwoman
                  .setCompassTarget(new Location(
                  Game.catwoman
                  .getWorld(), 
                  Game.catwoman
                  .getLocation()
                  .getX(), 
                  Game.catwoman
                  .getLocation()
                  .getY(), 
                  Game.catwoman
                  .getLocation()
                  .getZ()));
            }
            catch (Exception localException1)
            {
            }
      }
    }
    , 0L, 20L);
  }

  public void startHintsAndTipsLoop()
  {
    getServer().getScheduler().scheduleSyncRepeatingTask(this, 
      new Runnable() {
      public void run() {
        TheBatKnight.this.message = new Random().nextInt(13);
        switch (TheBatKnight.this.message) { case 0:
        case 2:
        default:
          Bukkit.broadcastMessage(ChatColor.GOLD + 
            "BatKnight's name shows up as " + 
            ChatColor.DARK_GRAY + 
            "Grey" + 
            ChatColor.GOLD + 
            " in the list of players names. BirdBoy is " + 
            ChatColor.GREEN + "Green" + 
            ChatColor.GOLD + " and Jester is" + 
            ChatColor.DARK_PURPLE + " Purple" + 
            ChatColor.GOLD + ".");
          break;
        case 1:
          Bukkit.broadcastMessage(ChatColor.GOLD + 
            "Wanna be able to see what's going on after being killed? Get a VIP membership at " + 
            ChatColor.DARK_AQUA + 
            "www.TheBatKnight.com");
          break;
        case 3:
          Bukkit.broadcastMessage(ChatColor.GOLD + 
            "Are you a villian who wants an edge?  VIP's start each game with a sword, bow, and arrows! Get your VIP membership at" + 
            ChatColor.DARK_AQUA + 
            " www.TheBatKnight.com");
          break;
        case 4:
          Bukkit.broadcastMessage(ChatColor.GOLD + 
            "The villian left alive at the end with the most diamonds will win. How many have you gotten?");
          break;
        case 5:
          Bukkit.broadcastMessage(ChatColor.GOLD + 
            "Are you a dead VIP? Then Left Click to teleport to a random player that's still alive and watch the action up close!");
          break;
        case 6:
          Bukkit.broadcastMessage(ChatColor.GOLD + 
            "Have any " + ChatColor.RED + "bugs " + 
            ChatColor.GOLD + "or " + ChatColor.GREEN + 
            "ideas" + ChatColor.GOLD + 
            "? Contact the developer on " + 
            ChatColor.AQUA + "Twitter.com " + 
            ChatColor.DARK_AQUA + "@TheBCBroz!");
          break;
        case 7:
          Bukkit.broadcastMessage(ChatColor.GOLD + 
            "The" + 
            ChatColor.DARK_PURPLE + 
            " Jester " + 
            ChatColor.GOLD + 
            "is the most powerful villian in The city. Want to be the " + 
            ChatColor.DARK_PURPLE + 
            "Jester" + 
            ChatColor.GOLD + 
            "? Only VIPs have the chance! Sign up at " + 
            ChatColor.DARK_AQUA + 
            "www.TheBatKnight.com");
          break;
        case 8:
          Bukkit.broadcastMessage(ChatColor.GOLD + 
            "Hold your " + ChatColor.WHITE + 
            "Feather " + ChatColor.GOLD + "as " + 
            ChatColor.DARK_GRAY + "BatKnight" + 
            ChatColor.GOLD + " and " + 
            ChatColor.GREEN + "BirdBoy" + 
            ChatColor.GOLD + " to fly!");
          break;
        case 9:
          Bukkit.broadcastMessage(ChatColor.GOLD + 
            "MinerCore Is Temporarialy Down For Maintainance! You Can Now Connect Using w(1,2,3).TheBatKnight.com!");
          break;
        case 10:
          Bukkit.broadcastMessage(ChatColor.DARK_AQUA + 
            "Get " + ChatColor.GOLD + "VIP " + 
            ChatColor.DARK_AQUA + 
            "To Taunt Your Enemies!");
          break;
        case 11:
          Bukkit.broadcastMessage(ChatColor.GOLD + 
            "Have more to say than will fit in this chat? Come to the forum!" + 
            ChatColor.DARK_AQUA + 
            " http://forum.TheBatKnight.com");
          break;
        case 12:
          Bukkit.broadcastMessage(ChatColor.GOLD + "Buy" + 
            ChatColor.DARK_GRAY + " BatKnight Shirts" + 
            ChatColor.GOLD + " Over At " + 
            ChatColor.AQUA + "shop.MinerCore.com");
        }
      }
    }
    , 900L, 2340L);
  }

  @EventHandler
  public void onPlayerCraft(CraftItemEvent event) {
    event.setCancelled(true);
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event)
  {
    try
    {
      if ((chests) && (event.getBlock().getType() == Material.CHEST)) {
        System.out.println("[Chest DEBUG] " + this.chestsInt++ + ": " + 
          event.getBlock().getX() + "," + 
          event.getBlock().getY() + "," + 
          event.getBlock().getZ());
      }

      if ((Game.joker == event.getPlayer()) && 
        (event.getBlock().getType() == Material.CHEST))
      {
        if (!event.getBlock().getLocation().add(1.0D, 0.0D, 0.0D).getBlock()
          .getType().equals(Material.CHEST))
        {
          if (!event.getBlock().getLocation().add(0.0D, 0.0D, 1.0D).getBlock()
            .getType().equals(Material.CHEST))
          {
            if (!event.getBlock().getLocation().subtract(1.0D, 0.0D, 0.0D)
              .getBlock().getType().equals(Material.CHEST))
            {
              if (!event.getBlock().getLocation()
                .subtract(0.0D, 0.0D, 1.0D).getBlock().getType()
                .equals(Material.CHEST)) {
                event.setCancelled(false);
                Game.joker.sendMessage(ChatColor.RED + 
                  "Trap Chest Placed...");
                event.getBlockPlaced().setType(Material.CHEST);
                trapChests.add(event.getBlock().getLocation());
              }
              else {
                event.setCancelled(true);
              }
            } else event.setCancelled(true); 
          }
          else
            event.setCancelled(true);
        }
        else event.setCancelled(true);
      }
      if ((Game.posionivy == event.getPlayer()) && 
        (event.getBlock().getType() == Material.VINE)) {
        event.setCancelled(false);
        vines.add(event.getBlock().getLocation());
      }
      if (event.getBlock().getType() == Material.TNT) {
        event.getBlock()
          .getWorld()
          .spawnEntity(event.getBlock().getLocation(), 
          EntityType.PRIMED_TNT);
        event.setCancelled(false);
        event.getBlock().setType(Material.AIR);
      }
    }
    catch (Exception localException) {
    }
  }

  @EventHandler
  public void onPotionSplash(PotionSplashEvent event) {
    for (LivingEntity e : event.getAffectedEntities())
      if (((e instanceof Player)) && 
        (spectating.contains((Player)e)))
        event.setIntensity(e, 0.0D);
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event)
  {
    if (((event.getWhoClicked() instanceof Player)) && 
      (event.getCurrentItem() != null) && 
      (event.getInventory().getSize() < 12)) {
      Player player = (Player)event.getWhoClicked();
      if (event.getCurrentItem().getType() == Material.GHAST_TEAR) {
        player.closeInventory();

        if (player.getInventory().contains(Material.DIAMOND, 
          200)) {
          if (!batBlocked) {
            Bukkit.broadcastMessage(ChatColor.GOLD + 
              player.getName() + 
              ChatColor.RED + 
              " Blocked The Heros' Trackers For 30 Seconds!");
            batBlocked = true;
            player.getInventory().remove(
              Material.GHAST_TEAR);
            player.updateInventory();
            event.setCancelled(true);
            player.playEffect(player.getLocation(), 
              Effect.ENDER_SIGNAL, 1);
            player.playEffect(player.getLocation(), 
              Effect.ENDER_SIGNAL, 1);
            player.playEffect(player.getLocation(), 
              Effect.SMOKE, 5);
            player.playEffect(player.getLocation(), 
              Effect.SMOKE, 5);
            player.getInventory().removeItem(new ItemStack[] { 
              new ItemStack(Material.DIAMOND, 200) });
            getServer().getScheduler()
              .scheduleSyncDelayedTask(this, 
              new Runnable() {
              public void run() {
                TheBatKnight.batBlocked = false;
                Bukkit.broadcastMessage(ChatColor.GOLD + 
                  "The Heros' Trackers Have Been Unblocked!");
              }
            }
            , 600L);
          } else {
            player.sendMessage(ChatColor.RED + 
              "There's Already A BatBlocker Active!");
          }
        }
        else player.sendMessage(ChatColor.RED + "You Need " + 
            ChatColor.DARK_AQUA + "200" + 
            ChatColor.RED + " Diamonds To Get A " + 
            ChatColor.BOLD + ChatColor.ITALIC + 
            ChatColor.DARK_GREEN + "BatBlocker!");

        event.setCancelled(true);
        player.updateInventory();
      }
      if (event.getCurrentItem().getType() == Material.SLIME_BALL) {
        player.closeInventory();

        if (player.getInventory().contains(Material.DIAMOND, 
          400)) {
          player.getInventory().addItem(new ItemStack[] { 
            game.setName(new ItemStack(
            Material.SLIME_BALL, 1), 
            ChatColor.BOLD + 
            ChatColor.GREEN + 
            "Posion-Dipped" + 
            ChatColor.DARK_GRAY + 
            " Arrows", Arrays.asList(new String[] { 
            "Posion your enemies", 
            "from afar!" })) });
          player.getInventory().removeItem(new ItemStack[] { 
            new ItemStack(Material.DIAMOND, 400) });
        } else {
          player.sendMessage(ChatColor.RED + "You Need " + 
            ChatColor.DARK_AQUA + "400" + 
            ChatColor.RED + " Diamonds To Get Some " + 
            ChatColor.BOLD + ChatColor.ITALIC + 
            ChatColor.DARK_GREEN + 
            ChatColor.BOLD + ChatColor.GREEN + 
            "Posion-Dipped" + ChatColor.DARK_GRAY + 
            " Arrows!");
        }
        event.setCancelled(true);
        player.updateInventory();
      }
    }
  }

  public boolean onCommand(final CommandSender sender, Command command, String label, String[] args)
  {
    if (label.equalsIgnoreCase("populatechests")) {
      if (((Player)sender).isOp()) {
        Locations.populateChests(this, (Player)sender, false, 
          maps.getMapId(), maps.getMapName());
        return true;
      }
      ((Player)sender)
        .sendMessage("Unknown command. Type \"help\" for help.");
      return false;
    }

    if ((label.equalsIgnoreCase("batban")) && (
      (mods.containsKey(sender.getName())) || 
      ((sender instanceof ConsoleCommandSender)))) {
      if (args.length > 2) {
        String playerName = args[0];
        String reason = "";
        for (int i = 1; i < args.length; i++)
          reason = reason + args[i] + " ";
        String formatted = "\n" + 
          playerName + 
          ":" + (
          (sender instanceof ConsoleCommandSender) ? "Console" : 
          sender.getName()) + ":" + reason;

        FTPClient ftp = new FTPClient();
        ftp.setHost("thebatknight.com");
        ftp.setPassword("Batknight2013!");
        ftp.setUser("bench3");
        ftp.setRemoteFile("ban.txt");
        if (ftp.connect())
        {
          if (ftp.downloadFile(new File(getDataFolder(), 
            "banlist.txt").getPath()))
            System.out.println(ftp.getLastSuccessMessage());
          else
            System.out.println(ftp.getLastErrorMessage());
        }
        else System.out.println(ftp.getLastErrorMessage());

        try
        {
          PrintWriter out = new PrintWriter(new BufferedWriter(
            new FileWriter(new File(getDataFolder(), 
            "banlist.txt"), true)));
          out.println(formatted);
          out.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
        if (Bukkit.getPlayer(playerName) != null) {
          Bukkit.getPlayer(playerName).kickPlayer(
            ChatColor.RED + "You Have Been " + 
            ChatColor.DARK_RED + 
            ChatColor.BOLD + 
            ChatColor.UNDERLINE + "BANNED\n" + 
            ChatColor.YELLOW + "Reason: " + 
            ChatColor.DARK_AQUA + reason);
        }

        FTPClient ftp = new FTPClient();
        ftp.setHost("thebatknight.com");
        ftp.setPassword("Batknight2013!");
        ftp.setUser("bench3");
        ftp.setRemoteFile("ban.txt");
        if (ftp.connect())
        {
          if (ftp.uploadFile(new File(getDataFolder(), 
            "banlist.txt").getPath()))
            System.out.println(ftp.getLastSuccessMessage());
          else
            System.out.println(ftp.getLastErrorMessage());
        }
        else System.out.println(ftp.getLastErrorMessage()); 
      }
      else
      {
        sender.sendMessage(ChatColor.RED + 
          "/batban [player] [reason]");
      }
    }

    if (label.equalsIgnoreCase("force")) {
      Player player = (Player)sender;
      if (player.isOp()) {
        if (args.length == 1) {
          if (args[0].equalsIgnoreCase("start")) {
            if (!hasStarted) {
              game.start(this, maps.getMapId(), maps.getMapName());
              player.sendMessage(ChatColor.GOLD + "Game Started!");
              return true;
            }
            player.sendMessage(ChatColor.RED + 
              "The Game Has Already Started!");
            return false;
          }
          if (args[0].equalsIgnoreCase("stop")) {
            if (hasStarted) {
              game.stop(this, "unknown");
              player.sendMessage(ChatColor.GOLD + "Game Stopped!");
              return true;
            }
            player.sendMessage(ChatColor.RED + 
              "The Game Has Already Ended!");
            return false;
          }

          player.sendMessage("Unknown command. Type \"help\" for help.");
          return false;
        }

        player.sendMessage("Unknown command. Type \"help\" for help.");
        return false;
      }

      player.sendMessage("Unknown command. Type \"help\" for help.");
      return false;
    }

    if ((label.equalsIgnoreCase("forceskip")) && 
      ((sender instanceof Player))) {
      Player player = (Player)sender;
      if (player.isOp())
        maps.skip();
      else {
        player.sendMessage("Unknown command. Type \"help\" for help.");
      }
    }

    if (label.equalsIgnoreCase("server")) {
      getServer().dispatchCommand(sender, "help");
      return true;
    }
    if ((label.equalsIgnoreCase("gg")) || (label.equalsIgnoreCase("goodgame"))) {
      if ((sender instanceof Player)) {
        Player player = (Player)sender;
        if (!gged.contains(player.getName()))
        {
          if (gameState.getState().equalsIgnoreCase(
            GameState.GAME_RESETING)) {
            gged.add(player.getName());
            int random = new Random().nextInt(12) + 1;
            switch (random) {
            case 1:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " Profoundly Proclaims That One Had A Good Game.");
              break;
            case 2:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " Would Like To Announce That The Previous Game Was Enjoyable.");
              break;
            case 3:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " Congratulates All Who Participated In The Previous Game For Playing Well.");
              break;
            case 4:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " Very Much Enjoyed The Spiffingness Of The Last Game.");
              break;
            case 5:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " Is Ecstatic That The Last Game Was Good.");
              break;
            case 6:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " hated this game. You guys are terrible.");
              break;
            case 7:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.AQUA + 
                " would like to thank Bench3 for this amazing plugin!");
            case 8:
              Bukkit.broadcastMessage(ChatColor.DARK_GREEN + 
                "After playing an amazing game of TheBatKnight, " + 
                ChatColor.RESET + player.getDisplayName() + 
                ChatColor.DARK_GREEN + 
                " is going to subscribe to " + 
                ChatColor.AQUA + "TheBCBroz" + 
                ChatColor.DARK_GREEN + " on " + 
                ChatColor.RED + "You" + ChatColor.RED + 
                "Tube" + ChatColor.DARK_AQUA + "!");
              break;
            case 9:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " would like to give everyone in the last game a pat on the back!");
              break;
            case 10:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " would like to give everyone in the last game a pat on the butt!");
              break;
            case 11:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " gives all the other players a gold star!");
              break;
            case 12:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " is rejoicing at the amazingness of the previous game!");
            default:
              break;
            } } else { player.sendMessage("Unknown command. Type \"help\" for help.");
            return false; }
        }
        else
          player.sendMessage(ChatColor.RED + 
            "We Understand! You Had A Good Game!");
      }
      else {
        return false;
      }
      return true;
    }

    if ((label.equalsIgnoreCase("gl")) || (label.equalsIgnoreCase("goodluck"))) {
      if ((sender instanceof Player)) {
        Player player = (Player)sender;
        if (!gled.contains(player.getName()))
        {
          if (gameState.getState().equalsIgnoreCase(
            GameState.IN_LOBBY)) {
            gled.add(player.getName());
            int random = new Random().nextInt(5) + 1;
            switch (random) {
            case 1:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " Wishes All Good Luck!");
              break;
            case 2:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " Exclaims At The Top Of Their Lungs... Good Luck!");
              break;
            case 3:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " Secretly Likes Smelling Their Own Farts");
              break;
            case 4:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.GREEN + 
                " Wishes All To Have Good Luck; And To All, A Good Night.");
              break;
            case 5:
              Bukkit.broadcastMessage(player.getDisplayName() + 
                ChatColor.AQUA + 
                " Is In Love With Sam Seide! " + 
                ChatColor.LIGHT_PURPLE + "<3");
            default:
              break;
            } } else { player.sendMessage("Unknown command. Type \"help\" for help.");
            return false; }
        }
        else
          player.sendMessage(ChatColor.RED + 
            "We Understand! You Want Good Luck!");
      }
      else {
        return false;
      }
      return true;
    }
    if ((label.equalsIgnoreCase("taunt")) || (label.equalsIgnoreCase("t"))) {
      if ((sender instanceof Player)) {
        Player player = (Player)sender;
        if (args.length == 1)
          if (vips.contains(player.getName()))
          {
            if (gameState.getState().equalsIgnoreCase(
              GameState.IN_GAME)) {
              Player playerToTaunt = Bukkit.getPlayer(args[0]);
              if (playerToTaunt != null) {
                if (vips.contains(playerToTaunt.getName())) {
                  player.sendMessage(ChatColor.RED + 
                    "Don't Taunt Your Fellow " + 
                    ChatColor.AQUA + "VIPs!");
                  return false;
                }
                boolean sendTaunt;
                boolean sendTaunt;
                if (tauntsSent.containsKey(player.getName())) {
                  int taunts = ((Integer)tauntsSent.get(player
                    .getName())).intValue();
                  boolean sendTaunt;
                  if (taunts >= 10)
                    sendTaunt = false;
                  else
                    sendTaunt = true;
                } else {
                  tauntsSent.put(player.getName(), Integer.valueOf(1));
                  sendTaunt = true;
                }
                if (sendTaunt) {
                  Extras.tauntPlayer(playerToTaunt
                    .getDisplayName());
                  tauntsSent
                    .put(player.getName(), 
                    Integer.valueOf(((Integer)tauntsSent
                    .get(player.getName())).intValue() + 1));
                } else {
                  player.sendMessage(ChatColor.RED + 
                    "That's probably enough for now, I think they are sufficiently taunted.");
                }
              } else {
                player.sendMessage(ChatColor.RED + "Player " + 
                  args[0] + " Is Not Online!");
                return false;
              }
            } else {
              player.sendMessage("Unknown command. Type \"help\" for help.");
              return false;
            }
          }
          else player.sendMessage(ChatColor.DARK_AQUA + "Get " + 
              ChatColor.GOLD + "VIP " + ChatColor.DARK_AQUA + 
              "To Taunt Your Enemies!");
      }
      else
      {
        return false;
      }
      return true;
    }
    if (label.equalsIgnoreCase("batknight")) {
      if ((sender instanceof Player)) {
        Player player = (Player)sender;
        player.sendMessage("I'm BATKNIGHT!");
        return true;
      }
      sender.sendMessage("Unknown command. Type \"help\" for help.");
      return false;
    }

    if (label.equalsIgnoreCase("chests")) {
      if (sender.isOp()) {
        chests = !chests;
        sender.sendMessage("Chest Debug Mode: " + chests);
        return true;
      }
      return false;
    }
    if (label.equalsIgnoreCase("pausegame")) {
      if (sender.isOp()) {
        pauseGame = !pauseGame;
        sender.sendMessage("Game Paused: " + pauseGame);
        return true;
      }
      return false;
    }
    if (label.equalsIgnoreCase("chestsNum")) {
      if (sender.isOp()) {
        if (args.length == 1)
          try {
            Integer.valueOf(args[0]);
          } catch (Exception e) {
            sender.sendMessage(args[0] + "!= Integer");
            return false;
          }
        this.chestsInt = Integer.valueOf(args[0]).intValue();
        return true;
      }
      return false;
    }

    if ((label.equalsIgnoreCase("kitty")) && 
      ((sender instanceof Player))) {
      Player player = (Player)sender;
      if ((player.getName().equalsIgnoreCase("bench3")) || 
        (player.getName().equalsIgnoreCase("zackscott")) || 
        (player.getName().equalsIgnoreCase("samseide"))) {
        if (!hasStarted) {
          if (args.length == 0) {
            Game.catwoman = player;
            player.sendMessage(ChatColor.LIGHT_PURPLE + 
              "You're KittyKat!");
          }
          else if (Bukkit.getPlayer(args[0]) != null) {
            Game.catwoman = Bukkit.getPlayer(args[0]);
            player.sendMessage(ChatColor.LIGHT_PURPLE + 
              args[0] + " Is KittyKat!");
            Bukkit.getPlayer(args[0]).sendMessage(
              ChatColor.LIGHT_PURPLE + 
              "You're KittyKat!");
          } else {
            player.sendMessage("PLAYER NOT ONLINE");
          }
        }
        else
          player.sendMessage("NO!");
      }
      else {
        player.sendMessage("NO!");
      }
    }

    if ((label.equalsIgnoreCase("puffin")) && 
      ((sender instanceof Player))) {
      Player player = (Player)sender;
      if ((player.getName().equalsIgnoreCase("bench3")) || 
        (player.getName().equalsIgnoreCase("zackscott")) || 
        (player.getName().equalsIgnoreCase("samseide"))) {
        if (!hasStarted) {
          if (args.length == 0) {
            Game.penguin = player;
            player.sendMessage(ChatColor.WHITE + 
              "You're PUFFIN!!");
          }
          else if (Bukkit.getPlayer(args[0]) != null) {
            Game.catwoman = Bukkit.getPlayer(args[0]);
            player.sendMessage(ChatColor.WHITE + args[0] + 
              " Is Puffin!");
            Bukkit.getPlayer(args[0]).sendMessage(
              ChatColor.WHITE + "You're Puffin!");
          } else {
            player.sendMessage("PLAYER NOT ONLINE");
          }
        }
        else
          player.sendMessage("NO!");
      }
      else {
        player.sendMessage("NO!");
      }
    }

    if ((label.equalsIgnoreCase("jester")) && 
      ((sender instanceof Player))) {
      Player player = (Player)sender;
      if ((player.getName().equalsIgnoreCase("bench3")) || 
        (player.getName().equalsIgnoreCase("zackscott")) || 
        (player.getName().equalsIgnoreCase("samseide"))) {
        if (!hasStarted) {
          if (args.length == 0) {
            Game.joker = player;
            player.sendMessage(ChatColor.DARK_PURPLE + 
              "You're Jester!");
          }
          else if (Bukkit.getPlayer(args[0]) != null) {
            Game.joker = Bukkit.getPlayer(args[0]);
            player.sendMessage(ChatColor.DARK_PURPLE + 
              args[0] + " Is Jester!");
            Bukkit.getPlayer(args[0]).sendMessage(
              ChatColor.DARK_PURPLE + 
              "You're Jester!");
          } else {
            player.sendMessage("PLAYER NOT ONLINE");
          }
        }
        else
          player.sendMessage("NO!");
      }
      else {
        player.sendMessage("NO!");
      }
    }

    if ((label.equalsIgnoreCase("cold")) && 
      ((sender instanceof Player))) {
      Player player = (Player)sender;
      if ((player.getName().equalsIgnoreCase("bench3")) || 
        (player.getName().equalsIgnoreCase("zackscott")) || 
        (player.getName().equalsIgnoreCase("samseide"))) {
        if (!hasStarted) {
          if (args.length == 0) {
            Game.mrFreeze = player;
            player.sendMessage(ChatColor.AQUA + 
              "You're Dr. Cold!");
          }
          else if (Bukkit.getPlayer(args[0]) != null) {
            Game.mrFreeze = Bukkit.getPlayer(args[0]);
            player.sendMessage(ChatColor.AQUA + args[0] + 
              " Is Dr. Cold!");
            Bukkit.getPlayer(args[0]).sendMessage(
              ChatColor.AQUA + "You're Dr. Cold!");
          } else {
            player.sendMessage("PLAYER NOT ONLINE");
          }
        }
        else
          player.sendMessage("NO!");
      }
      else {
        player.sendMessage("NO!");
      }
    }

    if (((label.equalsIgnoreCase("ivy")) || (label.equalsIgnoreCase("toxic")) || 
      (label.equalsIgnoreCase("toxicivy"))) && 
      ((sender instanceof Player))) {
      Player player = (Player)sender;
      if ((player.getName().equalsIgnoreCase("bench3")) || 
        (player.getName().equalsIgnoreCase("zackscott")) || 
        (player.getName().equalsIgnoreCase("samseide"))) {
        if (!hasStarted) {
          if (args.length == 0) {
            Game.posionivy = player;
            player.sendMessage(ChatColor.DARK_GREEN + 
              "You're Toxic Ivy!");
          }
          else if (Bukkit.getPlayer(args[0]) != null) {
            Game.posionivy = Bukkit.getPlayer(args[0]);
            player.sendMessage(ChatColor.DARK_GREEN + 
              args[0] + " Is Toxic Ivy!");
            Bukkit.getPlayer(args[0]).sendMessage(
              ChatColor.DARK_GREEN + 
              "You're Toxic Ivy!");
          } else {
            player.sendMessage("PLAYER NOT ONLINE");
          }
        }
        else
          player.sendMessage("NO!");
      }
      else {
        player.sendMessage("NO!");
      }

    }

    if (label.equalsIgnoreCase("skip")) {
      int skipsNeeded = Bukkit.getOnlinePlayers().length / 2 + 2;
      if ((sender instanceof Player)) {
        Player player = (Player)sender;
        if (!skiped.contains(player.getName())) {
          if (!hasStarted) {
            if (Map.canSkip) {
              if (maps.addSkip(player) >= skipsNeeded)
                maps.skip();
              skiped.add(player.getName());
            } else {
              player.sendMessage(ChatColor.RED + 
                "The Map Has Already Been Skipped!");
            }
          }
          else player.sendMessage(ChatColor.RED + 
              "The Game Has Already Started!");
        }
        else
          player.sendMessage(ChatColor.RED + 
            "You Have Already Skipped This Round!");
      }
      else {
        sender.sendMessage("You Must Be A Player To Skip!");
      }
    }
    if ((label.equalsIgnoreCase("unstuck")) && 
      (!unstuck.contains(sender.getName()))) {
      ((Player)sender).teleport(((Player)sender).getLocation().add(
        0.0D, 2.0D, 0.0D));
      ((Player)sender).getWorld()
        .getChunkAt(((Player)sender).getLocation())
        .load(false);
      ((Player)sender).getWorld()
        .getChunkAt(((Player)sender).getLocation()).load(true);
      unstuck.add(sender.getName());
      getServer().getScheduler().scheduleSyncDelayedTask(this, 
        new Runnable() {
        public void run() {
          TheBatKnight.unstuck.remove(sender.getName());
        }
      }
      , 30L);
    }

    if (label.equalsIgnoreCase("shop")) {
      if ((sender instanceof Player)) {
        Player player = (Player)sender;
        if ((player.isOp()) || ((hasStarted) && (player != Game.batman) && 
          (player != Game.robin))) {
          player.openInventory(shopInv);
          shopInv.clear();
          shopInv.addItem(new ItemStack[] { game.setName(new ItemStack(
            Material.GHAST_TEAR), ChatColor.BOLD + 
            ChatColor.ITALIC + ChatColor.DARK_GREEN + 
            "BatBlocker" + ChatColor.AQUA + " (" + 
            ChatColor.DARK_AQUA + "200 Diamonds" + 
            ChatColor.AQUA + ")", Arrays.asList(new String[] { 
            "Jam your enemies trackers", 
            "with this new piece of tech!" + ChatColor.ITALIC, 
            ChatColor.GRAY + "(Lasts for 30 seconds)" })) });
          shopInv.addItem(new ItemStack[] { game.setName(new ItemStack(
            Material.SLIME_BALL), ChatColor.BOLD + 
            ChatColor.GREEN + "Posion-Dipped" + 
            ChatColor.DARK_GRAY + " Arrows" + ChatColor.AQUA + 
            " (" + ChatColor.DARK_AQUA + "400 Diamonds" + 
            ChatColor.AQUA + ")", Arrays.asList(new String[] { 
            "Posion your enemies", "from afar!" + 
            ChatColor.ITALIC + ChatColor.GRAY + 
            "(Lasts One Game)" })) });
        } else {
          player.sendMessage(ChatColor.RED + 
            "To use the shop you must be in a game and be a villan!");
        }
      } else {
        sender.sendMessage("You must be a player to use this command!");
      }
    }
    if ((label.equalsIgnoreCase("break")) && 
      (hasStarted) && 
      ((sender instanceof Player))) {
      Player player = (Player)sender;
      if ((player.getItemInHand() != null) && 
        (player.getItemInHand().getType() != Material.AIR))
        player.getInventory().setItemInHand(
          new ItemStack(Material.AIR));
      World w = player.getWorld();
      for (int i = 0; i < 5; i++) {
        w.playEffect(player.getLocation(), Effect.SMOKE, 1);
        GameLoop.playNote(player);
      }
      player.sendMessage(ChatColor.RED + 
        "Well that was a waste...");
    }

    if ((label.equalsIgnoreCase("chilled")) && 
      (hasStarted) && 
      ((sender instanceof Player))) {
      Player player = (Player)sender;
      player.sendBlockChange(player.getLocation().add(1.0D, 0.0D, 0.0D), 
        Material.GLOWSTONE, (byte)0);
      player.sendBlockChange(
        player.getLocation().subtract(1.0D, 0.0D, 0.0D), 
        Material.GLOWSTONE, (byte)0);
      player.sendBlockChange(player.getLocation().add(0.0D, 0.0D, 1.0D), 
        Material.GLOWSTONE, (byte)0);
      player.sendBlockChange(
        player.getLocation().subtract(0.0D, 0.0D, 1.0D), 
        Material.GLOWSTONE, (byte)0);
      player.sendBlockChange(player.getLocation().add(1.0D, 1.0D, 0.0D), 
        Material.GLOWSTONE, (byte)0);
      player.sendBlockChange(
        player.getLocation().subtract(1.0D, 1.0D, 0.0D), 
        Material.GLOWSTONE, (byte)0);
      player.sendBlockChange(player.getLocation().add(0.0D, 1.0D, 1.0D), 
        Material.GLOWSTONE, (byte)0);
      player.sendBlockChange(
        player.getLocation().subtract(0.0D, 1.0D, 1.0D), 
        Material.GLOWSTONE, (byte)0);
      player.sendBlockChange(player.getLocation().add(0.0D, 2.0D, 0.0D), 
        Material.GLOWSTONE, (byte)0);
      player.sendMessage(ChatColor.GOLD + "Because LOGIC!");
    }

    if ((label.equalsIgnoreCase("setspawn")) && 
      ((sender instanceof Player))) {
      Player p = (Player)sender;
      if (p.isOp()) {
        ((Player)sender).getWorld().setSpawnLocation(
          p.getLocation().getBlockX(), 
          p.getLocation().getBlockY() + 2, 
          p.getLocation().getBlockZ());
      }
    }
    if ((label.equalsIgnoreCase("server")) || 
      (label.equalsIgnoreCase("serverinfo")) || 
      (label.equalsIgnoreCase("info"))) {
      String ip = getServer().getIp();
      String server = "-1";
      if (ip.trim().equalsIgnoreCase("198.154.105.242"))
        server = "W1";
      else if (ip.trim().equalsIgnoreCase("198.154.105.241"))
        server = "W2";
      else if (ip.trim().equalsIgnoreCase("216.172.106.238"))
        server = "W3";
      else if (ip.trim().equalsIgnoreCase("216.172.106.193"))
        server = "build";
      else {
        server = "-1";
      }
      int minsLeft = 0;
      int secsLeft = 0;

      if (hasStarted) {
        minsLeft = this.i > 60 ? this.i / 60 : 0;
        secsLeft = this.i % 60;
      } else {
        minsLeft = timeInSeconds > 60 ? timeInSeconds / 60 : 0;
        secsLeft = timeInSeconds % 60;
      }

      sender.sendMessage("---" + ChatColor.BOLD + 
        ChatColor.DARK_AQUA + "TheBatKnight" + ChatColor.RESET + 
        "---");
      sender.sendMessage(ChatColor.GOLD + "Server: " + ChatColor.YELLOW + 
        server);
      sender.sendMessage(ChatColor.GOLD + "Game State: " + 
        gameState.getState());
      sender.sendMessage(ChatColor.GOLD + "Time Remaining: " + 
        ChatColor.YELLOW + (minsLeft != 0 ? minsLeft + "m" : "") + 
        " " + (secsLeft != 0 ? secsLeft + "s" : ""));
      if (hasStarted) {
        if (Game.batman != null)
          sender.sendMessage(ChatColor.DARK_GRAY + "BatKnight: " + 
            Game.batman.getPlayerListName());
        if (Game.robin != null)
          sender.sendMessage(ChatColor.GREEN + "BirdBoy: " + 
            Game.robin.getPlayerListName());
        if (Game.penguin != null)
          sender.sendMessage(ChatColor.WHITE + "Puffin: " + 
            Game.penguin.getPlayerListName());
        if (Game.catwoman != null)
          sender.sendMessage(ChatColor.LIGHT_PURPLE + "KittyKat: " + 
            Game.catwoman.getPlayerListName());
        if (Game.posionivy != null)
          sender.sendMessage(ChatColor.DARK_GREEN + "ToxicIvy: " + 
            Game.posionivy.getPlayerListName());
        if (Game.mrFreeze != null)
          sender.sendMessage(ChatColor.AQUA + "DrCold: " + 
            Game.mrFreeze.getPlayerListName());
        if (Game.joker != null)
          sender.sendMessage(ChatColor.DARK_PURPLE + "Jester: " + 
            Game.joker.getPlayerListName());
      }
    }
    return false;
  }

  @EventHandler
  public void onBlockIgnite(BlockIgniteEvent event) {
    event.setCancelled(true);
  }

  public void updateVIPs() throws IOException {
    if (!new File(getDataFolder(), "vips.txt").exists())
      new File(getDataFolder(), "vips.txt").createNewFile();
    vips.clear();
    URL website = new URL("http://www.thebatknight.com/vips.txt");
    ReadableByteChannel rbc = Channels.newChannel(website.openStream());

    FileOutputStream fos = new FileOutputStream(new File(getDataFolder(), 
      "vips.txt"));
    fos.getChannel().transferFrom(rbc, 0L, 16777216L);

    BufferedReader br = new BufferedReader(new FileReader(new File(getDataFolder(), 
      "vips.txt")));
    try {
      String line = br.readLine();

      while (line != null) {
        line = br.readLine();
        vips.add(line);
      }
    } finally {
      br.close();
    }
  }

  public void updateBannedPlayers() throws IOException {
    if (!new File(getDataFolder(), "ban.txt").exists())
      new File(getDataFolder(), "ban.txt").createNewFile();
    URL website = new URL("http://www.thebatknight.com/ban.txt");
    ReadableByteChannel rbc = Channels.newChannel(website.openStream());

    FileOutputStream fos = new FileOutputStream(new File(getDataFolder(), 
      "ban.txt"));
    fos.getChannel().transferFrom(rbc, 0L, 16777216L);

    bannedPlayers.clear();
    BufferedReader br = new BufferedReader(new FileReader(new File(getDataFolder(), 
      "ban.txt")));
    try {
      String line = br.readLine();

      while (line != null) {
        if (line != null) {
          bannedPlayers.add(line.split(":")[0]);
        }
        line = br.readLine();
      }
    } finally {
      br.close();
    }
  }

  public void updateMods() throws IOException {
    if (!new File(getDataFolder(), "mods.txt").exists())
      new File(getDataFolder(), "mods.txt").createNewFile();
    URL website = new URL("http://www.thebatknight.com/mods.txt");
    ReadableByteChannel rbc = Channels.newChannel(website.openStream());

    FileOutputStream fos = new FileOutputStream(new File(getDataFolder(), 
      "mods.txt"));
    fos.getChannel().transferFrom(rbc, 0L, 16777216L);

    BufferedReader br = new BufferedReader(new FileReader(new File(getDataFolder(), 
      "mods.txt")));
    try {
      String line = br.readLine();
      while (line != null) {
        if (line != null) {
          mods.put(line.split("~")[0], line.split("~")[1]);

          System.out.println("MOD:" + line);
        }
        line = br.readLine();
      }
    } finally {
      br.close();
    }
  }

  public void sendToServer(Player player, String targetServer) {
    ByteArrayOutputStream b = new ByteArrayOutputStream();
    DataOutputStream out = new DataOutputStream(b);
    try {
      out.writeUTF("Connect");
      out.writeUTF(targetServer);
    }
    catch (IOException localIOException) {
    }
    player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
  }

  public void updateScores(boolean wipe) throws IOException {
    FTPClient ftp = new FTPClient();
    ftp.setHost("thebatknight.com");
    ftp.setPassword("Batknight2013!");
    ftp.setUser("bench3");
    ftp.setRemoteFile("MinerCoins/scores.txt");
    if (ftp.connect())
    {
      if (ftp.downloadFile(new File(getDataFolder(), "scores.txt")
        .getPath()))
        System.out.println(ftp.getLastSuccessMessage());
      else
        System.out.println(ftp.getLastErrorMessage());
    }
    else System.out.println(ftp.getLastErrorMessage());

    BufferedReader br = new BufferedReader(new FileReader(new File(getDataFolder(), 
      "scores.txt")));
    try {
      if (wipe)
        playerScores.clear();
      String line = br.readLine();
      while (line != null) {
        if (!playerScores.containsKey(line.split(",")[0]))
          playerScores.put(line.split(",")[0], 
            Integer.valueOf(line.split(",")[1]));
        line = br.readLine();
      }
    } finally {
      br.close();
    }
  }

  public void saveScores() throws IOException {
    for (Player p : Bukkit.getOnlinePlayers()) {
      if (playerScores.containsKey(p.getName())) {
        removeLineFromFile(
          new File(getDataFolder(), "scores.txt").getPath(), 
          p.getName());
      }
    }
    for (Player p : Bukkit.getOnlinePlayers()) {
      if (playerScores.containsKey(p.getName())) {
        PrintWriter out = new PrintWriter(new BufferedWriter(
          new FileWriter(new File(getDataFolder(), "scores.txt"), 
          true)));
        out.println(p.getName() + 
          "," + (
          ((Integer)playerScores.get(p.getName())).intValue() < 0 ? 0 : 
          ((Integer)playerScores.get(p.getName())).intValue()));
        out.close();
      }
    }
    FTPClient ftp = new FTPClient();
    ftp.setHost("thebatknight.com");
    ftp.setPassword("Batknight2013!");
    ftp.setUser("bench3");
    ftp.setRemoteFile("MinerCoins/scores.txt");
    if (ftp.connect())
    {
      if (ftp.uploadFile(new File(getDataFolder(), "scores.txt")
        .getPath()))
        System.out.println(ftp.getLastSuccessMessage());
      else
        System.out.println(ftp.getLastErrorMessage());
    }
    else System.out.println(ftp.getLastErrorMessage()); 
  }

  public void removeLineFromFile(String file, String playerName)
  {
    try { File inFile = new File(file);

      if (!inFile.isFile()) {
        System.out.println("Parameter is not an existing file");
        return;
      }

      File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

      BufferedReader br = new BufferedReader(new FileReader(file));
      PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

      String line = null;

      while ((line = br.readLine()) != null)
      {
        if (!line.trim().contains(playerName + ","))
        {
          pw.println(line);
          pw.flush();
        }
      }
      pw.close();
      br.close();

      if (!inFile.delete()) {
        System.out.println("Could not delete file");
        return;
      }

      if (!tempFile.renameTo(inFile))
        System.out.println("Could not rename file");
    } catch (FileNotFoundException ex)
    {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  private class EndGame
    implements Runnable
  {
    TheBatKnight plugin;

    public EndGame(TheBatKnight pl)
    {
      this.plugin = pl;
    }

    public void run() {
      while (true)
        if (TheBatKnight.gameState.getState() == GameState.IN_GAME) {
          if (!TheBatKnight.this.reset)
          {
            if (TheBatKnight.this.i == 0) {
              TheBatKnight.game.stop(this.plugin, "unknown");
              TheBatKnight.this.i = -1;
            } else {
              TheBatKnight.this.i -= 1;
              if ((TheBatKnight.this.i % 60 == 0) && (TheBatKnight.this.i != 0)) {
                TheBatKnight.this.broadcastTimeUntilEnd(TheBatKnight.this.i, "Minutes");
              }
              if (TheBatKnight.this.i <= 10) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                  player.sendMessage(ChatColor.RED + 
                    String.valueOf(TheBatKnight.this.i) + 
                    ChatColor.GOLD + 
                    " Seconds Until The Game Ends!");
                }
                GameLoop.playNote();
              }
            }
            try {
              Thread.sleep(1000L);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
        else try {
            Thread.sleep(1000L);
          } catch (Exception e) {
            e.printStackTrace();
          }
    }
  }
}