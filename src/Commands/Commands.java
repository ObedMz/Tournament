package Tournament.Commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.common.collect.Lists;


import Tournament.ConfigMananger.ConfigMananger;
import Tournament.GameMananger.Game;
import Tournament.GameMananger.GameMananger;
import Tournament.Inventory.Inventory;
import Tournament.Main.Main;
import Tournament.PlayerListener.Listener;

public class Commands implements CommandExecutor {
	private ConfigMananger config;
	private ConfigMananger message;
	public static int TaskID;
	public static int Tiempo=40;
	public static boolean run = true;
	private Listener listener;
	private Inventory inv;
	private String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].replace("v", "").replace("R", "").replace("_", ".");
	@SuppressWarnings("unused")
	private Main plugin = Main.getPlugin(Main.class);
	public Commands(Main plugin, ConfigMananger config, ConfigMananger message, Listener listener, Inventory inv) {
		this.plugin = plugin;
		this.config = config;
		this.message = message;
		this.listener = listener;
		this.inv = inv;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
		config = new ConfigMananger();
		message = new ConfigMananger();
		if(s instanceof Player) {
			Player p = (Player) s;
			FileConfiguration mensaje = message.getMessage();
			String nopermisos = mensaje.getString("Message.No-Permisos");
			String unknow = mensaje.getString("Message.Unknow-Command");
			if(args.length <=0) {
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.More-Arguments")));
				return false;
			}

			//ADMIN COMMANDS
			if(args[0].equalsIgnoreCase("set")) {
				if(p.hasPermission("tournament.create")) {
					if(args.length <= 1) {
						s.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.More-Arguments")));
						return false;
					}
					
					else if(args[1].equalsIgnoreCase("spawn")) {
						String world = p.getWorld().getName();
						String x = Double.toString(p.getLocation().getBlockX());
						String y = Double.toString(p.getLocation().getBlockY());
						String z = Double.toString(p.getLocation().getBlockZ());
						String pitch = Float.toString(p.getLocation().getPitch());
						String yaw = Float.toString(p.getLocation().getYaw());
						config.getConfig().set("Config.spawn-Location.world", world);
						config.getConfig().set("Config.spawn-Location.x", x);
						config.getConfig().set("Config.spawn-Location.y", y);
						config.getConfig().set("Config.spawn-Location.z", z);
						config.getConfig().set("Config.spawn-Location.pitch", pitch);
						config.getConfig().set("Config.spawn-Location.yaw", yaw);
						config.saveConfig();
						config.reloadConfig();
						Main.get().reloadConfig();
						s.sendMessage(ChatColor.GREEN + "Spawn seteado correctamente.");
					}else if(args[1].equalsIgnoreCase("back")) {
						String world = p.getWorld().getName();
						String x = Double.toString(p.getLocation().getBlockX());
						String y = Double.toString(p.getLocation().getBlockY());
						String z = Double.toString(p.getLocation().getBlockZ());
						String pitch = Float.toString(p.getLocation().getPitch());
						String yaw = Float.toString(p.getLocation().getYaw());
						config.getConfig().set("Config.leave-Location.world", world);
						config.getConfig().set("Config.leave-Location.x", x);
						config.getConfig().set("Config.leave-Location.y", y);
						config.getConfig().set("Config.leave-Location.z", z);
						config.getConfig().set("Config.leave-Location.pitch", pitch);
						config.getConfig().set("Config.leave-Location.yaw", yaw);
						config.saveConfig();
						config.reloadConfig();
						Main.get().reloadConfig();
						s.sendMessage(ChatColor.GREEN + "Posición de regreso seteada correctamente.");
					}else if(args[1].equalsIgnoreCase("pos1")) {
						String world = p.getWorld().getName();
						String x = Double.toString(p.getLocation().getBlockX());
						String y = Double.toString(p.getLocation().getBlockY());
						String z = Double.toString(p.getLocation().getBlockZ());
						String pitch = Float.toString(p.getLocation().getPitch());
						String yaw = Float.toString(p.getLocation().getYaw());
						config.getConfig().set("Config.match-Location.pos1.world", world);
						config.getConfig().set("Config.match-Location.pos1.x", x);
						config.getConfig().set("Config.match-Location.pos1.y", y);
						config.getConfig().set("Config.match-Location.pos1.z", z);
						config.getConfig().set("Config.match-Location.pos1.pitch", pitch);
						config.getConfig().set("Config.match-Location.pos1.yaw", yaw);
						config.saveConfig();
						config.reloadConfig();
						Main.get().reloadConfig();
						s.sendMessage(ChatColor.GREEN + "Posición 1 seteada correctamente.");
					}else if(args[1].equalsIgnoreCase("pos2")) {
						String world = p.getWorld().getName();
						String x = Double.toString(p.getLocation().getBlockX());
						String y = Double.toString(p.getLocation().getBlockY());
						String z = Double.toString(p.getLocation().getBlockZ());
						String pitch = Float.toString(p.getLocation().getPitch());
						String yaw = Float.toString(p.getLocation().getYaw());
						config.getConfig().set("Config.match-Location.pos2.world", world);
						config.getConfig().set("Config.match-Location.pos2.x", x);
						config.getConfig().set("Config.match-Location.pos2.y", y);
						config.getConfig().set("Config.match-Location.pos2.z", z);
						config.getConfig().set("Config.match-Location.pos2.pitch", pitch);
						config.getConfig().set("Config.match-Location.pos2.yaw", yaw);
						config.saveConfig();
						config.reloadConfig();
						Main.get().reloadConfig();
						s.sendMessage(ChatColor.GREEN + "Posición 2 seteada correctamente.");	
					}else if(args[1].equalsIgnoreCase("kit")) {
						List <String> lista =  config.getConfig().getStringList("Config.Inventory.kit");
						lista.clear();
						config.getConfig().set("Config.Inventory.kit", lista);
						config.saveConfig();
						for(int x=0; x<36; x++) {
							if(p.getInventory().getItem(x) != null) {
								ItemStack item = p.getInventory().getItem(x);
								//potions
								if(item.getType().equals(Material.POTION)||item.getType().equals(Material.getMaterial("SPLASH_POTION")))  {
									
									String material = item.getType().toString();
									p.sendMessage(Integer.toString(item.getDurability())); //short
									if(version.contains("1.8")) {
										String ID = Integer.toString(item.getTypeId());
										String sh = Integer.toString( item.getDurability());
										String stack = Integer.toString(item.getAmount());
										String cadena = x+":"+stack+","+ID+":"+sh;
										lista.add(cadena);
									} else {
										String[] efparts = item.getItemMeta().toString().split(",");
										String tipo = efparts[1].replaceAll("potion-type=minecraft:", "").replaceAll("\\}", "").replaceFirst(" ", "").toUpperCase().replaceAll("SWIFTNESS", "SPEED").replaceAll("LEAPING", "JUMP").replaceAll("HARMING", "INSTANT_DAMAGE").replaceAll("REGENERATION", "REGEN").replaceAll("HEALING", "INSTANT_HEAL");
										Boolean extend;
										Boolean isSplash;
										int level;
										
											if(tipo.startsWith("LONG")) {
												level = 2;
											} 
											else {
												level = 1;
											}
											if(tipo.startsWith("STRONG")) {
													extend = true;
											} else {
												extend = false;
											}
											if(material.contains("SPLASH")) {
												isSplash = true;
											} else {
												isSplash = false;
											}
											String type = tipo.replaceAll("STRONG_", "").replaceAll("LONG_", "");
											String stack = Integer.toString(item.getAmount());
											String cadena = x+":"+stack+","+"Potion"+ ":" + type+":"+level + ":"+ isSplash.toString() + ":"+ extend.toString();
											lista.add(cadena);
									}
								}else {
									
									String id = Integer.toString(item.getTypeId());
									String[] pdata = item.getData().toString().split("\\(");
									String data = pdata[1].replaceAll("\\)", "");
									String stack = Integer.toString(item.getAmount());
									String cadena = x + ":" + stack  + ","+ id + ":" + data;
									Map <Enchantment, Integer> ench = new HashMap<Enchantment, Integer>();
									ench.putAll(item.getEnchantments());
									 Set<Enchantment> keys = ench.keySet();
			
								     for (Enchantment k : keys) {
								         String enchid = Integer.toString(k.getId());
								         String levelid = Integer.toString(ench.get(k));
								         String encantamiento = "," + enchid + ":" + levelid;
								         cadena = cadena + encantamiento;
										
								     }
								     
								     lista.add(cadena);

									ench.clear();
								}
								

							}
							
						}
						config.getConfig().set("Config.Inventory.kit", lista);
						//// Armadura
						List <String> armorlist = Lists.newArrayList();
						ItemStack[] items = p.getInventory().getArmorContents();
						for(ItemStack i : items) {
							if(i != null) {
								String id = Integer.toString(i.getTypeId());
								String[] pdata = i.getData().toString().split("\\(");
								String data = pdata[1].replaceAll("\\)", "");
								String armorcadena =  id + ":" + data;
								Map <Enchantment, Integer> ench = new HashMap<Enchantment, Integer>();
								ench.putAll(i.getEnchantments());
								Set<Enchantment> keys = ench.keySet();

							     for (Enchantment k : keys) {
							         String enchid = Integer.toString(k.getId());
							         String levelid = Integer.toString(ench.get(k));
							         String encantamiento = "," + enchid + ":" + levelid;
							         armorcadena = armorcadena + encantamiento;
									
							     }
							     armorlist.add(armorcadena);
								 ench.clear();
							} else {
								armorlist.add("null");
							}
						}
						config.getConfig().set("Config.Inventory.Armor", armorlist);

						
						config.saveConfig();
						config.reloadConfig();
						Main.get().reloadConfig();
						s.sendMessage(ChatColor.GREEN + "Inventario guardado correctamente.");
					} else {
						s.sendMessage(ChatColor.translateAlternateColorCodes('&', unknow));
					}

				}
			} else if(args[0].equalsIgnoreCase("inv")) {
				if(!(args.length <=1)) {
					Player opponent = Bukkit.getPlayer(args[1]);
					if(opponent != null) {
						inv.ShowInventory(p, opponent);
					}
				}
			}
			else if(args[0].equalsIgnoreCase("reload")) {
				if(s.hasPermission("tournament.reload")) {
					if(GameMananger.getGame() != Game.STOP) {
						s.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Tournament-Started")));
					} else {
						message.reloadMessage();
						config.reloadConfig();
						Main.get().reloadConfig();
						s.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Tournament-Reload")));
					}
				}else {s.sendMessage(ChatColor.translateAlternateColorCodes('&', nopermisos));}
			} 
			//end /
			else if(args[0].equalsIgnoreCase("start")) {
				if(s.hasPermission("tournament.start")) {
					if(args.length<=1) {
						s.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Tournament-SetPlayers")));
					}else{
						if(config.getConfig().getString("Config.spawn-Location.world") == null) {
							s.sendMessage(ChatColor.DARK_RED + "You have to set the spawn location.");
							return false;
						}
						if(config.getConfig().getString("Config.leave-Location.world") == null) {
							s.sendMessage(ChatColor.DARK_RED + "You have to set the back location.");
							return false;
						}
						
						if(config.getConfig().getString("Config.match-Location.pos1.world") == null) {
							s.sendMessage(ChatColor.DARK_RED + "You have to set the pos1 location.");
							return false;
						}
						if(config.getConfig().getString("Config.match-Location.pos2.world") == null) {
							s.sendMessage(ChatColor.DARK_RED + "You have to set the pos2 location.");
							return false;
						}
						if(GameMananger.getGame() == Game.STOP) {
							GameMananger.maxplayers = Integer.parseInt(args[1]);
							GameMananger.setGame(Game.DISPONIBLE);
							Contador();
							
						} else {
							s.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Tournament-Started")));
						}

						
					}
				}else {s.sendMessage(ChatColor.translateAlternateColorCodes('&', nopermisos));}
			}else if(args[0].equalsIgnoreCase("forcestart")) {
				if(s.hasPermission("tournament.forcestart")) {
					if(GameMananger.getGame().equals(Game.ESPERANDO) || GameMananger.getGame().equals(Game.DISPONIBLE)) {
						Listener.Tiempo = 1;
						GameMananger.Announcer(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Tournament-ForceStart")));
					}else if(GameMananger.getGame().equals(Game.JUGANDO)) {
						s.sendMessage(mensaje.getString("Message.Tournament-Started"));
					}
					else {
						s.sendMessage(mensaje.getString("Message.Not-Arena"));
					}
					/// forzar con countdown
				}else {s.sendMessage(ChatColor.translateAlternateColorCodes('&', nopermisos));}
			}else if(args[0].equalsIgnoreCase("end")){
				if(s.hasPermission("tournament.end")) {
						GameMananger.setGame(Game.STOP);
						run = false;
						for(Player all : Bukkit.getOnlinePlayers()) {
							if(GameMananger.isInGame(all)) {
								GameMananger.removeGame(all);
								GameMananger.removeToEspectate(all);
								GameMananger.removeToPvP(all);
								all.getInventory().clear();
								GameMananger.setArmorContent(all);
								GameMananger.setInventory(all);
								all.teleport(listener.BackLocation());
							}
						}
						GameMananger.Announcer(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Tournament-ForceEnd")));
				}else {s.sendMessage(ChatColor.translateAlternateColorCodes('&', nopermisos));}
				//// JUGADORES 
			}else if(args[0].equalsIgnoreCase("join")){
				if(s.hasPermission("tournament.join")) {
					if(GameMananger.getGame()== Game.DISPONIBLE ||GameMananger.getGame()== Game.ESPERANDO) {
						if(GameMananger.isInGame(p)) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Player-Already")));
						}
						else {
							if(GameMananger.GetPlayersize() >= GameMananger.maxplayers) {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Arena-Full")));
							} else {
								
								GameMananger.addGame(p);
								GameMananger.saveArmorContent(p);
								GameMananger.saveInventory(p);
								GameMananger.savePotionEffect(p);
								GameMananger.removePotionEffect(p);
								GameMananger.AnnouncerForGame(mensaje.getString("Message.Player-Join").replaceAll("%max%", Integer.toString(GameMananger.maxplayers)).replaceAll("%player%", p.getName()).replaceAll("%now%", Integer.toString(GameMananger.GetPlayersize())));
								p.getInventory().clear();
								GameMananger.SetItemsEspect(p);
								p.teleport(listener.SpawnLocation());
							}

						}
					}else if(GameMananger.getGame()== Game.JUGANDO )	{
						if(GameMananger.isInGame(p)) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Player-Already")));
						} else {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Not-Arena")));
						}
					}else {p.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Not-Arena")));}
				}else {s.sendMessage(ChatColor.translateAlternateColorCodes('&', nopermisos));}
			}else if(args[0].equalsIgnoreCase("leave")){
				if(s.hasPermission("tournament.leave")) {
					if(GameMananger.isInGame(p)) {
						if(GameMananger.isInPvP(p)) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Cant-Leave")));
						}
							p.getInventory().clear();
							GameMananger.removeGame(p);
							GameMananger.removeToEspectate(p);
							GameMananger.setArmorContent(p);
							GameMananger.setInventory(p);
							GameMananger.removePotionEffect(p);
							GameMananger.setPotionEffects(p);
							p.updateInventory();
							p.teleport(listener.BackLocation());
							GameMananger.AnnouncerForGame(mensaje.getString("Message.Player-Leave").replaceAll("%max%", Integer.toString(GameMananger.maxplayers)).replaceAll("%player%", p.getName()).replaceAll("%now%", Integer.toString(GameMananger.GetPlayersize())));
					}else {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', mensaje.getString("Message.Cant-Leave")));
					}
				}else {s.sendMessage(ChatColor.translateAlternateColorCodes('&', nopermisos));}
			} else {
				s.sendMessage(ChatColor.translateAlternateColorCodes('&', unknow));
			}

		} else {
			s.sendMessage(ChatColor.RED+ "No puedes hacer esto desde consola.");
		}
		return false;
	}
	
	
	
	// CountDown
	
	
	public void Contador() {
		message = new ConfigMananger();
		FileConfiguration mensaje = message.getMessage();
		BukkitScheduler sh= Bukkit.getServer().getScheduler();
		TaskID = sh.scheduleSyncRepeatingTask(Main.get(), new Runnable() {

			@Override
			public void run() {
				if(run) {
					if(Tiempo == 0) {
						if(GameMananger.GetPlayersize() > 4) {
							GameMananger.setGame(Game.JUGANDO);
							// seleccionar dos de la lista
							Player uno = GameMananger.getRandomPlayer();
							Player dos = GameMananger.getotherplayer(uno);
							uno.teleport(listener.MatchLocationUno());
							dos.teleport(listener.MatchLocationDos());
							listener.toStart(uno);
							listener.toStart(dos);
							GameMananger.AnnouncerForGame(mensaje.getString("Message.Next-Round").replaceAll("%player%", uno.getName()).replaceAll("%oponent%", dos.getName()));
						}else {
							GameMananger.Announcer(mensaje.getString("Message.Not-Players"));
							GameMananger.setGame(Game.STOP);
							for(Player p : Bukkit.getOnlinePlayers()) {
								if(GameMananger.isInGame(p)) {
									GameMananger.removeGame(p);
									p.teleport(listener.BackLocation());
									p.getInventory().clear();
									GameMananger.setArmorContent(p);
									GameMananger.setInventory(p);
									GameMananger.setPotionEffects(p);
									p.updateInventory();
								}
							}
						}
						Tiempo = 40;
						sh.cancelTask(TaskID);
						
					}else {
						if(Tiempo == 40||Tiempo == 30 || Tiempo == 20 || Tiempo == 10 || Tiempo<=5){
							for(Player p : Bukkit.getOnlinePlayers()) {
								String ms = mensaje.getString("Message.Arena-Count").replaceAll("%time%", Integer.toString(Tiempo));
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', ms));
							}
							
						}

						Tiempo--;
					}
				} else {
					sh.cancelTask(TaskID);
				}
				
				
				
				
			}
			
		}, 0L, 20);
		
		
		
	}
	

	

}
