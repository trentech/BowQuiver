package info.trentech.Quiver;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class Quiver extends JavaPlugin{

	private EventListener eventlistener;
	private CommandHandler cmdExecutor;
	public Objective objective;
	public Scoreboard board;
	
	@Override
	public void onEnable(){
		
		new CustomItems(this);
		this.eventlistener = new EventListener(this);
		getServer().getPluginManager().registerEvents(this.eventlistener, this);
		
		this.cmdExecutor = new CommandHandler(this);
		getCommand("quiver").setExecutor(cmdExecutor);
		
		getConfig().options().copyDefaults(true);
    	saveConfig();
    	
        CustomItems.createRecipes();
        
        board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        objective = board.registerNewObjective("quiver", "dummy");
        objective.setDisplayName(ChatColor.DARK_AQUA + "Quiver");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
}
