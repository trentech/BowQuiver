package info.trentech.Quiver;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {
	
	private Quiver plugin;
	public CommandHandler(Quiver plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("quiver") || label.equalsIgnoreCase("qv")) {
			if(args.length >= 1){
				if(args[0].equalsIgnoreCase("reload")){
					if(sender.hasPermission("Quiver.reload")){
						plugin.reloadConfig();
						plugin.saveConfig();
						CustomItems.createRecipes();
						sender.sendMessage(ChatColor.DARK_GREEN + "Quiver Reloaded!");
					}else{
						sender.sendMessage(ChatColor.DARK_RED + "You do not have permission!");
					}
				}
			}else{
				sender.sendMessage(ChatColor.YELLOW + "/Quiver -or- /qv");
				sender.sendMessage(ChatColor.YELLOW + "/qv reload");
			}
		}
		return true;
	}


}
