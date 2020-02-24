package Tournament.PlayerListener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.collect.Lists;

import Tournament.ConfigMananger.ConfigMananger;
import Tournament.GameMananger.Game;
import Tournament.GameMananger.GameMananger;
import Tournament.GameMananger.Sounds;
import Tournament.Main.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
@SuppressWarnings("unused")
public class Listener implements org.bukkit.event.Listener {
	private ConfigMananger config;

	private ConfigMananger message;
	public static int TaskID;
	public static int TaskTo;
	public static int TaskPo;
	public static int Tiempo=30;
	public static int time=5;
	private Tournament.Inventory.Inventory tinventory;
	private Main plugin;
	
	public Listener(Main plugin, ConfigMananger config, ConfigMananger message, Tournament.Inventory.Inventory tinventory) {
		this.config = config;
		this.message = message;
		this.plugin = plugin;
		this.tinventory = tinventory;
	}
	
	@EventHandler
	  public void onPreCommand(PlayerCommandPreprocessEvent event) {
		if(GameMananger.getGame() == Game.JUGANDO ||
		   GameMananger.getGame() == Game.ESPERANDO||
		   GameMananger.getGame() == Game.DISPONIBLE) {
			String msg = event.getMessage().toLowerCase();
			 if (msg.startsWith("/spawn")){
			      event.setCancelled(true);
			      
			    }
		}
		
	
	}
	
	@EventHandler
	public void onblockPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(GameMananger.isInGame(p)) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onblockbreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(GameMananger.isInGame(p)) {
			e.setCancelled(true);
		}
	}
	
	
	@EventHandler
	@SuppressWarnings("deprecation")
	public void onItemConsume(PlayerItemConsumeEvent e){

		if(e.getItem().getType() == Material.getMaterial(373)) {
			
			BukkitScheduler sh = Bukkit.getServer().getScheduler();
			
			TaskPo = sh.scheduleSyncRepeatingTask(Main.get(), new Runnable() {

				@Override
				public void run() {
						e.getPlayer().getInventory().remove(Material.getMaterial(374));
						e.getPlayer().updateInventory();
						sh.cancelTask(TaskPo);
				}
				
			}, 0L, 10);
		}

	}
	
	

	
	
	@EventHandler
	public void onplayerdrop(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		if(GameMananger.isInGame(p)) {
			e.setCancelled(true);
		}
	}
	


	
	@EventHandler
	public void PlayerDeath(PlayerDeathEvent e) {
		message = new ConfigMananger();
		FileConfiguration mensaje = message.getMessage();
		config = new ConfigMananger();
		if(e.getEntity() instanceof Player && e.getEntity().getKiller() instanceof Player){
			Player p = (Player) e.getEntity();
			Player d = (Player) e.getEntity().getKiller();
			
			if(GameMananger.isInPvP(p) && GameMananger.isInPvP(d)) {
				e.getDrops().clear();
				Sounds.EXPLODE.play(p);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Player-Lose")));
				GameMananger.AnnouncerForGame(message.getMessage().getString("Message.Round-Winner").replaceAll("%winer%", d.getName()).replaceAll("%loser%", p.getName()));
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), config.getConfig().getString("Config.Round-Reward").replaceAll("%player%", d.getName()));
				p.getInventory().clear();
				d.getInventory().clear();
				GameMananger.removeToPvP(p);
				GameMananger.removeToPvP(d);
				GameMananger.addToEspectate(p);
				GameMananger.SetItemsEspect(d);
				// tp to the spectate arena
				p.teleport(SpawnLocation());
				d.teleport(SpawnLocation());
				p.setHealth(20);
				d.setHealth(20);
				p.setFoodLevel(20);
				d.setFoodLevel(20);
				GameMananger.SendMessage(d, message.getMessage().getString("Message.Player-Inventory.Text").replaceAll("%player%", p.getName()), "/tournament inv "+ p.getName(), message.getMessage().getString("Message.Player-Inventory.Text-Hover"));
				GameMananger.SendMessage(p, message.getMessage().getString("Message.Player-Inventory.Text").replaceAll("%player%", d.getName()), "/tournament inv "+ d.getName(), message.getMessage().getString("Message.Player-Inventory.Text-Hover"));
				/// get other players to match
				
				if(GameMananger.GetPlayersize() - GameMananger.GetsizeEspect() > 1) {
					// seleccionar otros
					Player uno = GameMananger.getRandomPlayer();
					Player dos = GameMananger.getotherplayer(uno);
					uno.teleport(MatchLocationUno());
					dos.teleport(MatchLocationDos());
					toStart(uno);
					toStart(dos);
					GameMananger.AnnouncerForGame(mensaje.getString("Message.Next-Round").replaceAll("%player%", uno.getName()).replaceAll("%oponent%", dos.getName()));
					RunForPvP();
				}else {
					GameMananger.Announcer(mensaje.getString("Message.Arena-Winner").replaceAll("%winner%", d.getName()));
				//premio aqui.
				 Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), config.getConfig().getString("Config.Tournament-Reward").replaceAll("%player%", d.getName()));
					GameMananger.setGame(Game.STOP);
					for(Player all : Bukkit.getOnlinePlayers()) {
						Sounds.FIREWORK_LARGE_BLAST.play(all);
						if(GameMananger.isInGame(all)) {
							GameMananger.removeGame(all);
							all.teleport(BackLocation());
							all.getInventory().clear();
							GameMananger.setArmorContent(all);
							GameMananger.setInventory(all);
							GameMananger.setPotionEffects(all);
							all.updateInventory();
						}
					}
				
				}
				
			}	
		}
		
	}
	


	

	
	@EventHandler
	public void kickplayer(PlayerKickEvent e) {
		message = new ConfigMananger();
		FileConfiguration mensaje = message.getMessage();
		Player p = e.getPlayer();
		if(GameMananger.isInGame(p)) {
			GameMananger.removeGame(p);
			if(GameMananger.isInPvP(p)) {
				// Detect dmager
				GameMananger.removeToPvP(p);
				
				Player d = Bukkit.getPlayer(GameMananger.jugadorespvp.get(0));
				if(d !=null) {
					d.teleport(SpawnLocation());
					d.getInventory().clear();
					Sounds.FIREWORK_LARGE_BLAST.play(d);
					GameMananger.removeToPvP(d);
					GameMananger.SetItemsEspect(d);
					GameMananger.AnnouncerForGame(message.getMessage().getString("Message.Round-Winner").replaceAll("%winner%", d.getName()).replaceAll("%loser%", p.getName()));
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), config.getConfig().getString("Config.Round-Reward").replaceAll("%player%", d.getName()));
					// select other
					if(GameMananger.GetPlayersize()- GameMananger.GetsizeEspect() > 1) {
						// seleccionar otros
						Player uno = GameMananger.getRandomPlayer();
						Player dos = GameMananger.getotherplayer(uno);
						uno.teleport(MatchLocationUno());
						dos.teleport(MatchLocationDos());
						toStart(uno);
						toStart(dos);
						GameMananger.AnnouncerForGame(message.getMessage().getString("Message.Next-Round").replaceAll("%player%", uno.getName()).replaceAll("%oponent%", dos.getName()));
						RunForPvP();
					}else {
						GameMananger.Announcer(message.getMessage().getString("Message.Arena-Winner").replaceAll("%winner%", d.getName()));
					//premio aqui.
					 Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), config.getConfig().getString("Config.Tournament-Reward").replaceAll("%player%", d.getName()));
						GameMananger.setGame(Game.STOP);
						for(Player all : Bukkit.getOnlinePlayers()) {
							if(GameMananger.isInGame(all)) {
								Sounds.FIREWORK_BLAST.play(all);
								GameMananger.removeGame(all);
								all.teleport(BackLocation());
								all.getInventory().clear();
								GameMananger.setArmorContent(all);
								GameMananger.setInventory(all);
								GameMananger.setPotionEffects(all);
								all.updateInventory();
							}
						}
					
					}
					
				}
				
			}
			if(GameMananger.isEspectate(p)) {
				GameMananger.removeToEspectate(p);
			}
			
		}
	}
	
	
	@EventHandler
	public void quitplayer(PlayerQuitEvent e) {
		message = new ConfigMananger();
		FileConfiguration mensaje = message.getMessage();
		Player p = e.getPlayer();
		if(GameMananger.isInGame(p)) {
			GameMananger.removeGame(p);
			if(GameMananger.isInPvP(p)) {
				// Detect dmager
				GameMananger.removeToPvP(p);
				
				Player d = Bukkit.getPlayer(GameMananger.jugadorespvp.get(0));
				if(d !=null) {
					GameMananger.removeToPvP(d);
					d.teleport(SpawnLocation());
					d.getInventory().clear();
					Sounds.FIREWORK_LARGE_BLAST.play(d);
					GameMananger.SetItemsEspect(d);
					GameMananger.AnnouncerForGame(message.getMessage().getString("Message.Round-Winner").replaceAll("%winner%", d.getName()).replaceAll("%loser%", p.getName()));
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), config.getConfig().getString("Config.Round-Reward").replaceAll("%player%", d.getName()));
					// select other
					if(GameMananger.GetPlayersize()- GameMananger.GetsizeEspect() > 1) {
						// seleccionar otros
						Player uno = GameMananger.getRandomPlayer();
						Player dos = GameMananger.getotherplayer(uno);
						uno.teleport(MatchLocationUno());
						dos.teleport(MatchLocationDos());
						toStart(uno);
						toStart(dos);
						GameMananger.AnnouncerForGame(mensaje.getString("Message.Next-Round").replaceAll("%player%", uno.getName()).replaceAll("%oponent%", dos.getName()));
						RunForPvP();
					}else {
						GameMananger.Announcer(mensaje.getString("Message.Arena-Winner").replaceAll("%winner%", d.getName()));
					//premio aqui.
					 Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), config.getConfig().getString("Config.Tournament-Reward").replaceAll("%player%", d.getName()));
						GameMananger.setGame(Game.STOP);
						for(Player all : Bukkit.getOnlinePlayers()) {
							if(GameMananger.isInGame(all)) {
								Sounds.FIREWORK_LARGE_BLAST.play(all);
								GameMananger.removeGame(all);
								all.teleport(BackLocation());
								all.getInventory().clear();
								GameMananger.setArmorContent(all);
								GameMananger.setInventory(all);
								GameMananger.setPotionEffects(all);
								all.updateInventory();
							}
						}
					
					}
					
				}
				
			}
			if(GameMananger.isEspectate(p)) {
				GameMananger.removeToEspectate(p);
			}
			
		}
	}
	
	
	@EventHandler
	public void onPlayerPvP(EntityDamageByEntityEvent e) {

		if(e.getEntity() instanceof Player && e.getDamager() instanceof Player){
			Player p = (Player) e.getEntity();
			Player d = (Player) e.getDamager();
			if(GameMananger.isInPvP(p) && GameMananger.isInPvP(d)) {
				message = new ConfigMananger();
				FileConfiguration mensaje = message.getMessage();
				config = new ConfigMananger();
				if(p.getHealth() - e.getFinalDamage() > 0) {
					return;
				}
				e.setCancelled(true);
				Sounds.EXPLODE.play(p);
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Player-Lose")));
				GameMananger.AnnouncerForGame(message.getMessage().getString("Message.Round-Winner").replaceAll("%winer%", d.getName()).replaceAll("%loser%", p.getName()));
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), config.getConfig().getString("Config.Round-Reward").replaceAll("%player%", d.getName()));
				p.getInventory().clear();
				d.getInventory().clear();
				GameMananger.removeToPvP(p);
				GameMananger.removeToPvP(d);
				GameMananger.addToEspectate(p);
				GameMananger.SetItemsEspect(d);
				// tp to the spectate arena
				p.teleport(SpawnLocation());
				d.teleport(SpawnLocation());
				p.setFireTicks(0);
				d.setFireTicks(0);
				p.setHealth(20);
				d.setHealth(20);
				p.setFoodLevel(20);
				d.setFoodLevel(20);
				GameMananger.SendMessage(d, message.getMessage().getString("Message.Player-Inventory.Text").replaceAll("%player%", p.getName()), "/tournament inv "+ p.getName(), message.getMessage().getString("Message.Player-Inventory.Text-Hover"));
				GameMananger.SendMessage(p, message.getMessage().getString("Message.Player-Inventory.Text").replaceAll("%player%", d.getName()), "/tournament inv "+ d.getName(), message.getMessage().getString("Message.Player-Inventory.Text-Hover"));
				/// get other players to match
				
				if(GameMananger.GetPlayersize() - GameMananger.GetsizeEspect() > 1) {
					// seleccionar otros
					Player uno = GameMananger.getRandomPlayer();
					Player dos = GameMananger.getotherplayer(uno);
					uno.teleport(MatchLocationUno());
					dos.teleport(MatchLocationDos());
					toStart(uno);
					toStart(dos);
					GameMananger.AnnouncerForGame(mensaje.getString("Message.Next-Round").replaceAll("%player%", uno.getName()).replaceAll("%oponent%", dos.getName()));
					RunForPvP();
				}else {
					GameMananger.Announcer(mensaje.getString("Message.Arena-Winner").replaceAll("%winner%", d.getName()));
				//premio aqui.
				 Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), config.getConfig().getString("Config.Tournament-Reward").replaceAll("%player%", d.getName()));
					GameMananger.setGame(Game.STOP);
					for(Player all : Bukkit.getOnlinePlayers()) {
						if(GameMananger.isInGame(all)) {
							Sounds.FIREWORK_LARGE_BLAST.play(all);
							GameMananger.removeGame(all);
							all.teleport(BackLocation());
							all.getInventory().clear();
							GameMananger.setArmorContent(all);
							GameMananger.setInventory(all);
							GameMananger.setPotionEffects(all);
							all.updateInventory();
						}
					}
				
				}
				
			}else if(GameMananger.isInGame(p) && GameMananger.isInGame(d)) {
				e.setCancelled(true);
			}
			
		}
		
		
		
		
	}
	

	

	
	
	
	
	
	//Locations
	public Location SpawnLocation() {
		config = new ConfigMananger();
		config.reloadConfig();
		Main.get().reloadConfig();
		World world = Bukkit.getWorld(config.getConfig().getString("Config.spawn-Location.world"));
		double x = Double.parseDouble(config.getConfig().getString("Config.spawn-Location.x"));
		double y = Double.parseDouble(config.getConfig().getString("Config.spawn-Location.y"));
		double z = Double.parseDouble(config.getConfig().getString("Config.spawn-Location.z"));
		float pitch = Float.parseFloat(config.getConfig().getString("Config.spawn-Location.pitch"));
		float yaw = Float.parseFloat(config.getConfig().getString("Config.spawn-Location.yaw"));
		Location loc = new Location(world,x,y,z,yaw,pitch);
		return loc;
	}
	
	//Locations
	public Location BackLocation() {
		config = new ConfigMananger();
		config.reloadConfig();
		Main.get().reloadConfig();
		World world = Bukkit.getWorld(config.getConfig().getString("Config.leave-Location.world"));
		double x = Double.parseDouble(config.getConfig().getString("Config.leave-Location.x"));
		double y = Double.parseDouble(config.getConfig().getString("Config.leave-Location.y"));
		double z = Double.parseDouble(config.getConfig().getString("Config.leave-Location.z"));
		float pitch = Float.parseFloat(config.getConfig().getString("Config.leave-Location.pitch"));
		float yaw = Float.parseFloat(config.getConfig().getString("Config.leave-Location.yaw"));
		Location loc = new Location(world,x,y,z,yaw,pitch);
		return loc;
	}
	//Locations
	public Location MatchLocationUno() {
		config = new ConfigMananger();
		config.reloadConfig();
		Main.get().reloadConfig();
		World world = Bukkit.getWorld(config.getConfig().getString("Config.match-Location.pos1.world"));
		double x = Double.parseDouble(config.getConfig().getString("Config.match-Location.pos1.x"));
		double y = Double.parseDouble(config.getConfig().getString("Config.match-Location.pos1.y"));
		double z = Double.parseDouble(config.getConfig().getString("Config.match-Location.pos1.z"));
		float pitch = Float.parseFloat(config.getConfig().getString("Config.match-Location.pos1.pitch"));
		float yaw = Float.parseFloat(config.getConfig().getString("Config.match-Location.pos1.yaw"));
		Location loc = new Location(world,x,y,z,yaw,pitch);
		return loc;
	}
	
	public Location MatchLocationDos() {
		config = new ConfigMananger();
		config.reloadConfig();
		Main.get().reloadConfig();
		World world = Bukkit.getWorld(config.getConfig().getString("Config.match-Location.pos2.world"));
		double x = Double.parseDouble(config.getConfig().getString("Config.match-Location.pos2.x"));
		double y = Double.parseDouble(config.getConfig().getString("Config.match-Location.pos2.y"));
		double z = Double.parseDouble(config.getConfig().getString("Config.match-Location.pos2.z"));
		float pitch = Float.parseFloat(config.getConfig().getString("Config.match-Location.pos2.pitch"));
		float yaw = Float.parseFloat(config.getConfig().getString("Config.match-Location.pos2.yaw"));
		Location loc = new Location(world,x,y,z,yaw,pitch);
		return loc;
	}
	
	
	
	public void toStart(Player p) {
		GameMananger.addToPvP(p);
		p.setFireTicks(0);
		setArmor(p);
		setPvPItems(p);
		message = new ConfigMananger();
		FileConfiguration mensaje = message.getMessage();
		GameMananger.preparando.add(p.getName());	
		for(Player all : Bukkit.getOnlinePlayers()) {
			if(GameMananger.isInPvP(all)) {
				all.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 255, 255));
			}
		}	
		
	}
	
	@SuppressWarnings("deprecation")
	public void setPvPItems(Player p) {
		config = new ConfigMananger();
		config.reloadConfig();
		Main.get().reloadConfig();
		List <String> lista =  config.getConfig().getStringList("Config.Inventory.kit");
		for(int x=0; x<lista.size(); x++) {
			String cadena = lista.get(x);
			String[] parts = cadena.split(",");
			int pos = Integer.parseInt(parts[0].split(":")[0]);
			int amount = Integer.parseInt(parts[0].split(":")[1]);
			String arg = parts[1].split(":")[0];
			if(arg.startsWith("Potion")) {
				String tipo = parts[1].split(":")[1];
				int level = Integer.parseInt(parts[1].split(":")[2]);
				Boolean splash = false;
				Boolean extend = false;
				if(parts[1].split(":")[3].equalsIgnoreCase("true")) {
					splash = true;
				}
				if(parts[1].split(":")[4].equalsIgnoreCase("true")) {
					extend = true;
				}
				Potion poti = new Potion(PotionType.valueOf(tipo), level, splash, extend);
				poti.setSplash(splash);
				
				ItemStack item = poti.toItemStack(amount);
				p.getInventory().setItem(pos, item);
				
			}else {
				int itemID = Integer.parseInt(parts[1].split(":")[0]);
				int Data = Integer.parseInt(parts[1].split(":")[1]);
				ItemStack item = new ItemStack(Material.getMaterial(itemID), amount, (short) Data);
				
				for(int i = 2; i<parts.length; i++) {
					if(parts.length >2){
						Enchantment ench = Enchantment.getById(Integer.parseInt(parts[i].split(":")[0]));
						item.addEnchantment(ench,Integer.parseInt(parts[i].split(":")[1]));
					}
				}
				
				p.getInventory().setItem(pos, item);
			}

		}
		p.updateInventory();
	}
	

	
	@SuppressWarnings("deprecation")
	public void setArmor(Player p) {
		config = new ConfigMananger();
		List <String> lista = config.getConfig().getStringList("Config.Inventory.Armor");
		List<ItemStack> itemstacks = Lists.newArrayList();
		ItemStack item;
		for(int x=0;x<4;x++) {

			if(lista.get(x).equalsIgnoreCase("null")) {
				item = null;
			}else {
				String[] path = lista.get(x).split(",");
				int id = Integer.parseInt(path[0].split(":")[0]);
				int data = Integer.parseInt(path[0].split(":")[1]);
				item = new ItemStack(Material.getMaterial(id),1, (short) data);
				for(int i = 1; i<path.length; i++) {
					if(path.length >1){
						Enchantment ench = Enchantment.getById(Integer.parseInt(path[i].split(":")[0]));
						item.addUnsafeEnchantment(ench,Integer.parseInt(path[i].split(":")[1]));
					}
				}
				
			}
			itemstacks.add(item);
		}
		p.getInventory().setBoots(itemstacks.get(0));
		p.getInventory().setLeggings(itemstacks.get(1));
		p.getInventory().setChestplate(itemstacks.get(2));
		p.getInventory().setHelmet(itemstacks.get(3));
		p.updateInventory();

		
		
	}
	
	
	
	public void RunForPvP() {
		message = new ConfigMananger();
		FileConfiguration mensaje = message.getMessage();
		BukkitScheduler sh = Bukkit.getServer().getScheduler();
		TaskTo = sh.scheduleSyncRepeatingTask(Main.get(), new Runnable() {

			@Override
			public void run() {
				if(time == 0) {
					for(Player all : Bukkit.getOnlinePlayers()) {
						if(GameMananger.isInPvP(all)) {
							all.removePotionEffect(PotionEffectType.SLOW);
							GameMananger.preparando.remove(all.getName());
							all.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Start-Fight")));
							Sounds.valueOf("LEVEL_UP").play(all);
							
						}

					}
					time = 5;
					sh.cancelTask(TaskTo);
				}else {
					for(Player all : Bukkit.getOnlinePlayers()) {
						if(GameMananger.isInPvP(all)) {
						all.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.CountDown").replaceAll("%time%", Integer.toString(time))));
						Sounds.valueOf("NOTE_PLING").play(all);
						}
		
					}

					time--;
				}
				
			}
			
		}, 0L, 20);
	}
	
	
	
	
	
}
