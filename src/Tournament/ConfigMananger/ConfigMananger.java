package Tournament.ConfigMananger;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import Tournament.Main.Main;

public class ConfigMananger {
	private Main plugin = Main.getPlugin(Main.class);
	public FileConfiguration config = null;
	private File configFile = null;
	public FileConfiguration message = null;
	private File messageFile = null;	

	
	//config
	public FileConfiguration getMessage() {
		if(message == null) {
			reloadMessage();
		}
		return message;
	}
	
	public void reloadMessage(){
		if(message == null) {
			messageFile = new File(plugin.getDataFolder(), "message.yml");
		}
		
		message = YamlConfiguration.loadConfiguration(messageFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(plugin.getResource("message.yml"), "UTF8");
			if(defConfigStream !=null) {
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				message.setDefaults(defConfig);
			}
			
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	public void saveMessage() {
		try {
			message.save(messageFile);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void registerMessage() {
		messageFile = new File(plugin.getDataFolder(), "message.yml");
		if(!messageFile.exists()) {
			this.getMessage().options().copyDefaults(true);
			saveMessage();
		}
	}
	
	//
	public FileConfiguration getConfig() {
		if(config == null) {
			reloadConfig();
		}
		return config;
	}
	
	public void reloadConfig(){
		if(config == null) {
			configFile = new File(plugin.getDataFolder(), "config.yml");
		}
		
		config = YamlConfiguration.loadConfiguration(configFile);
		Reader defConfigStream;
		try{
			defConfigStream = new InputStreamReader(plugin.getResource("config.yml"), "UTF8");
			if(defConfigStream !=null) {
				YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
				config.setDefaults(defConfig);
			}
			
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	public void saveConfig() {
		try {
			config.save(configFile);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void registerConfig() {
		configFile = new File(plugin.getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			this.getConfig().options().copyDefaults(true);
			saveConfig();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
