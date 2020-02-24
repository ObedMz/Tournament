package Tournament.Inventory;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Lists;

import Tournament.ConfigMananger.ConfigMananger;


public class Inventory implements Listener {
	private ConfigMananger config;
	public Inventory(ConfigMananger config) {
		this.config = config;
	}
	
	@SuppressWarnings("deprecation")
	public void ShowInventory(Player ganador, Player perdedor) {
		config = new ConfigMananger();
		String title = config.getConfig().getString("Config.Inventory.Opponent.Title").replaceAll("%player%", perdedor.getName());
		org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 45,ChatColor.translateAlternateColorCodes('&',title));
		inv.setContents(perdedor.getInventory().getContents());
		ItemStack vida;
		String pathMaterial = config.getConfig().getString("Config.Inventory.Opponent.health.Material");
		if(pathMaterial.contains(":")) {
			String[] parts = config.getConfig().getString("Config.Inventory.Opponent.health.Material").split(":");
			int id = Integer.parseInt(parts[0]);
			int data = Integer.parseInt(parts[1]);
			vida = new ItemStack(Material.getMaterial(id), 1, (short) data);
		}else {
			vida = new ItemStack(Material.getMaterial(Integer.parseInt(pathMaterial)));
		}
		ItemMeta metav = vida.getItemMeta();
		metav.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("Config.Inventory.Opponent.health.Name")));
		List<String> lore = Lists.newArrayList();
		int corazones= (int) perdedor.getHealth();
		vida.setAmount(corazones);
		lore.addAll(config.getConfig().getStringList(ChatColor.translateAlternateColorCodes('&',"Config.Inventory.Opponent.health.lore")));
		for(int x=0; x<lore.size();x++) {
			lore.get(x).replaceAll("%food%", Integer.toString(corazones));
		}
		
		metav.setLore(lore);
		vida.setItemMeta(metav);
		//// comida
		ItemStack food;
		String foodpath = config.getConfig().getString("Config.Inventory.Opponent.food.Material");
		if(foodpath.contains(":")) {
			String[] partes = config.getConfig().getString("Config.Inventory.Opponent.food.Material").split(":");
			int idf = Integer.parseInt(partes[0]);
			int dataf = Integer.parseInt(partes[1]);
			food = new ItemStack(Material.getMaterial(idf), 1, (short) dataf);
		}else {
			food = new ItemStack(Material.getMaterial(Integer.parseInt(foodpath)));
		}
		ItemMeta metaf = food.getItemMeta();
		metaf.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getConfig().getString("Config.Inventory.Opponent.food.Name")));
		List<String> loref = Lists.newArrayList();
		int foodlevel = (int) perdedor.getFoodLevel();
		vida.setAmount(foodlevel);
		loref.addAll(config.getConfig().getStringList(ChatColor.translateAlternateColorCodes('&',"Config.Inventory.Opponent.food.lore")));
		for(int x=0; x<loref.size();x++) {
			loref.get(x).replaceAll("%food%", Integer.toString(foodlevel));
		}
		
		metaf.setLore(loref);
		food.setItemMeta(metaf);	
		food.setAmount(foodlevel);
		
		
		
		
		
		inv.setItem(43, food);
		inv.setItem(44, vida);
        ganador.openInventory(inv);
	
	}
	
	

	

	@EventHandler
	public void inventoryClick(InventoryClickEvent e) {
		config = new ConfigMananger();
		FileConfiguration mconfig = config.getConfig();
		String title = mconfig.getString("Config.Inventory.Opponent.Title").replaceAll("%player%", "");
		String nombre = ChatColor.translateAlternateColorCodes('&', title);
		String nombreM = ChatColor.stripColor(nombre);
		if(ChatColor.stripColor(e.getInventory().getName()).startsWith(nombreM)) {
				e.setCancelled(true);
				return;
			}
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
