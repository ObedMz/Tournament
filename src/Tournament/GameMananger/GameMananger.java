package Tournament.GameMananger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;



public class GameMananger {
	public static Game state;
	public static int maxplayers = 0;
	public static ArrayList<String> jugadores = new ArrayList<String>();
	public static ArrayList<String> jugadorespvp = new ArrayList<String>();
	public static ArrayList<String> espectadores = new ArrayList<String>();
	public static ArrayList<String> preparando = new ArrayList<String>();
	public static Map<String, ItemStack[]> inventarios = new HashMap<String, ItemStack[]>();
	public static Map<String, ItemStack[]> armadura = new HashMap<String, ItemStack[]>();
	public static HashMap<String, List<PotionEffect>> potion = new HashMap<String, List<PotionEffect>>();
	public static int TaskID;
	public static int Tiempo = 30;

	
	public static Game getGame() {
		return state;
		
	}
	
	public static void setGame(Game game) {
		state = game;
	}
	
	//inventarios
	public static void saveInventory(Player p) {
		ItemStack[] inv = p.getInventory().getContents();
		inventarios.put(p.getName(), inv);
	}
	public static void setInventory(Player p) {
		ItemStack[] inv = inventarios.get(p.getName());
		p.getInventory().setContents(inv);
		p.updateInventory();
	}
	
	public static void saveArmorContent(Player p) {
		ItemStack[] arm = p.getInventory().getArmorContents();
		armadura.put(p.getName(), arm);
	}
	public static void setArmorContent(Player p) {
		ItemStack[] arm = armadura.get(p.getName());
		p.getInventory().setArmorContents(arm);
	}
	
	//Jugadores
	
	public static boolean isInGame(Player Player) {
		if(jugadores.contains(Player.getName())) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public static void addGame(Player Player) {
		jugadores.add(Player.getName());
	}
	
	public static void removeGame(Player Player) {
		jugadores.remove(Player.getName());
	}
	
	public static int GetPlayersize() {
		return jugadores.size();
	}
	
	public static Player getRandomPlayer() {
		Random rand = new Random();
		Player otro = null;
		Boolean validar = false;
		while(!validar) {
			Player jugador = Bukkit.getPlayer(jugadores.get(rand.nextInt(jugadores.size())));
			if(!(isInPvP(jugador)) && !(isEspectate(jugador))) {
				otro = jugador;
				validar = true;
			}
		}
		
		return otro;
	}
	
	public static void removePotionEffect(Player p) {
		List<PotionEffect> effect = new ArrayList<PotionEffect>();
		effect = (List<PotionEffect>) p.getActivePotionEffects();
		for(int x=0; x<effect.size();x++){
			p.removePotionEffect(effect.get(x).getType());
		}
	}
	
	public static void savePotionEffect(Player p) {
		List<PotionEffect> playereffects = new ArrayList<PotionEffect>();
		playereffects = (List<PotionEffect>) p.getActivePotionEffects();
		potion.put(p.getName(), playereffects);
		
	}
	public static void setPotionEffects(Player p) {
		List<PotionEffect> playereffects = new ArrayList<PotionEffect>();
		playereffects = potion.get(p.getName());
		p.addPotionEffects(playereffects);
	}
	
	//pvp
	
	public static boolean isInPvP(Player Player) {
		if(jugadorespvp.contains(Player.getName())) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public static void addToPvP(Player Player) {
		jugadorespvp.add(Player.getName());
		Player.setHealth(20);
		Player.setFoodLevel(20);
		Player.getInventory().clear();
	}
	
	public static void removeToPvP(Player Player) {
		jugadorespvp.remove(Player.getName());
	}	
	
	
	
	public static boolean isEspectate(Player Player) {
		if(espectadores.contains(Player.getName())) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public static void addToEspectate(Player Player) {
		espectadores.add(Player.getName());
		SetItemsEspect(Player);
	}
	
	public static void removeToEspectate(Player Player) {
		espectadores.remove(Player.getName());
	}
	
	public static int GetsizeEspect() {
		
		return espectadores.size();
	}

	/// Items:
	
	public static void SetItemsEspect(Player p) {
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		p.updateInventory();
	}
	
	public static void SendMessage(Player p, String message,String command, String hover) {
		TextComponent tc = new TextComponent();
		tc.setText(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', message).replaceAll("%command%", command));
		tc.setClickEvent(new ClickEvent(Action.RUN_COMMAND, command));
		tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', hover)).create()));
		p.spigot().sendMessage(tc);
	}
	

	
	public static void Announcer(String message) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
		
		
	}

	
	public static void AnnouncerForGame(String message) {
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(isInGame(p)) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
			}
		}
		
		
	}
	
	
	
	public static Player getotherplayer(Player uno) {
		Random rand = new Random();
		Player otro = null;
		Boolean validar = false;
		while(!validar) {
			Player jugador = Bukkit.getPlayer(jugadores.get(rand.nextInt(jugadores.size())));
			if(!(isInPvP(jugador)) && !(isEspectate(jugador)) && jugador != uno ) {
				otro = jugador;
				validar = true;
			}
		}
		
		return otro;
	}



	
	
}
