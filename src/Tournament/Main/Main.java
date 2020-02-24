package Tournament.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import Tournament.Commands.Commands;
import Tournament.ConfigMananger.ConfigMananger;
import Tournament.GameMananger.Game;
import Tournament.GameMananger.GameMananger;
import Tournament.Inventory.Inventory;
import Tournament.PlayerListener.Listener;


public class Main extends JavaPlugin {
	private Main plugin;
	private static Main instance;
	private ConfigMananger config;
	private ConfigMananger message;
	private Listener listener;
	private Inventory inventory;
	public static boolean stop = false;
	
	 public void onLoad() {
	    	instance = this;
	    	
	 }
	
	
	public void onEnable() {
		try {
			loadConfig();
			RegisterCommands();
			GameMananger.setGame(Game.STOP);
			Bukkit.getPluginManager().registerEvents(new Listener(plugin, config, message, inventory), this);
			Bukkit.getPluginManager().registerEvents(new Inventory(config), this);
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Tournament - Enable [" + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].replace("v", "").replace("R", "").replace("_", ".") + "]");
			
		}catch(Exception ex) {
			Bukkit.shutdown();
			ex.printStackTrace();
		}
		
		
	}
	
	
	public void RegisterCommands(){
		getCommand("Tournament").setExecutor(new Commands(plugin, config,message, listener, inventory));
	}
	
	public void loadConfig(){
		config = new ConfigMananger();
		message = new ConfigMananger();
		config.registerConfig();
		message.registerMessage();
		listener = new Listener(plugin, config, message, inventory);
		inventory = new Inventory(config);
	}
	
	
	public void onDisable() {
		
		stop = true;
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Tournament - Disable");
		
	}
	
	
	public static Main get() {
		return instance;
	}
	
	
	
	
	
	
	
}
