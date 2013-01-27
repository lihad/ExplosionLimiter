package Lihad.ExplosionLimiter;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
		if(whitelistOn)whitelist = config.getIntegerList("block-whitelist");
		levelOn = config.getBoolean("activate-level");
		if(levelOn)level = config.getInt("level");

		this.getServer().getPluginManager().registerEvents(this, this);
	}
	@Override
	public void onDisable() {
		save();
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("el") && sender.isOp()) {
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("level")){
					if(args.length == 2){
						try{
							level = Integer.parseInt(args[1]);
							sender.sendMessage("'level' set to "+level);
						}catch(Exception e){sender.sendMessage("/el <level> <num>");}
					}else{
						sender.sendMessage("/el <level> <num>");
					}
				}else if(args[0].equalsIgnoreCase("level-on")){
					if(args.length == 2){
						try{
							levelOn = Boolean.parseBoolean(args[1]);
							sender.sendMessage("'levelOn' set to "+levelOn);
						}catch(Exception e){sender.sendMessage("/el <level-on> <boolean>");}
					}else{
						sender.sendMessage("/el <level-on> <boolean>");
					}
				}else if(args[0].equalsIgnoreCase("whitelist-on")){
					if(args.length == 2){
						try{
							whitelistOn = Boolean.parseBoolean(args[1]);
							sender.sendMessage("'whitelist-on' set to "+whitelistOn);
						}catch(Exception e){sender.sendMessage("/el <whitelist-on> <boolean>");}
					}else{sender.sendMessage("/el <whitelist-on> <boolean>");}
				}else if(args[0].equalsIgnoreCase("whitelist-add")){
					if(args.length == 2){
						try{
							int add = Integer.parseInt(args[1]);
							if(!whitelist.contains(add)){
								whitelist.add(add);
								sender.sendMessage("'whitelist' added "+add);
							}else{
								sender.sendMessage("ID already on whitelist.");
							}
						}catch(Exception e){sender.sendMessage("/el <whitelist-add> <num>");}
					}else{
						sender.sendMessage("/el <whitelist-add> <num>");
					}
				}else if(args[0].equalsIgnoreCase("whitelist-remove")){
					if(args.length == 2){
						try{
							int remove = Integer.parseInt(args[1]);
							if(whitelist.contains(remove)){
								whitelist.remove(remove);
								sender.sendMessage("'whitelist' removed "+remove);
							}else{
								sender.sendMessage("ID not on whitelist.");
							}
						}catch(Exception e){sender.sendMessage("/el <whitelist-remove> <num>");}
					}else{
						sender.sendMessage("/el <whitelist-remove> <num>");
					}
				}else if(args[0].equalsIgnoreCase("whitelist-list")){
					if(args.length == 1){
						String nums = "";
						for(int i = 0; i<whitelist.size();i++){
							nums.concat(whitelist.get(i).toString());
						}
						sender.sendMessage("'whitelist' = "+nums);
					}else{
						sender.sendMessage("/el <whitelist-list>");
					}
				}else if(args[0].equalsIgnoreCase("whitelist-clear")){
					if(args.length == 1){
						whitelist.clear();
						sender.sendMessage("'whitelist' cleared");

					}else{
						sender.sendMessage("/el <whitelist-clear>");
					}
				}
				save();
				return true;
			}else{
				sender.sendMessage("/el <level || level-on || whitelist-on || whitelist-add>");
				sender.sendMessage("/el <whitelist-remove || whitelist-list || whitelist-clear>");
			}
		}
		return false;
	}
	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event){
		for(int i = 0; i<event.blockList().size(); i++){
			if(whitelistOn && whitelist.contains(event.blockList().get(i).getTypeId()) ||
					event.blockList().get(i).getLocation().getBlockY() > level){
				event.setCancelled(true);
			}
		}	
	}
	public void save(){
		config.set("activate-whitelist", whitelistOn);
		config.set("block-whitelist", whitelist);
		config.set("activate-level", levelOn);
		config.set("level", level);

		this.saveConfig();
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
