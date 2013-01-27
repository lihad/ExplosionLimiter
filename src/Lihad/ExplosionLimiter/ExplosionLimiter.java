package Lihad.ExplosionLimiter;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;


public class ExplosionLimiter extends JavaPlugin implements Listener {
	protected static String PLUGIN_NAME = "ExplosionLimiter";
	protected static String header = "[" + PLUGIN_NAME + "] ";
	private static Logger log = Logger.getLogger("Minecraft");
	private static FileConfiguration config;
	public static Integer level;
	public static List<Integer> whitelist = new LinkedList<Integer>();
	public static boolean whitelistOn = false;
	public static boolean levelOn = false;


	@Override
	public void onEnable() {
		config = this.getConfig();
		whitelistOn = config.getBoolean("activate-whitelist");
		if(whitelistOn){
			whitelist = config.getIntegerList("block-whitelist");
		}
		levelOn = config.getBoolean("activate-level");
		if(levelOn){
			level = config.getInt("level");
		}
		this.getServer().getPluginManager().registerEvents(this, this);
	}
	@Override
	public void onDisable() {
		this.saveConfig();
	}
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event){
		for(int i = 0; i<event.blockList().size(); i++){
			if(whitelistOn && whitelist.contains(event.blockList().get(i).getTypeId())){
				event.setCancelled(true);
				return;
			}else if(event.blockList().get(i).getLocation().getBlockY() > level){
				event.setCancelled(true);
				return;
			}
		}	
	}
	public static void info(String message){ 
		log.info(header + ChatColor.WHITE + message);
	}
	public static void severe(String message){
		log.severe(header + ChatColor.RED + message);
	}
	public static void warning(String message){
		log.warning(header + ChatColor.YELLOW + message);
	}
	public static void log(java.util.logging.Level level, String message){
		log.log(level, header + message);
	}
}
