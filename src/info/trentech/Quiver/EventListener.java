package info.trentech.Quiver;

import info.trentech.Quiver.CustomItems.Items;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Score;

public class EventListener implements Listener {
	
	private Quiver plugin;
	public EventListener(Quiver plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerItemBreakEvent(PlayerItemBreakEvent event){
		Player player = event.getPlayer();
		ItemStack arm = event.getBrokenItem();
		if(arm.getItemMeta().hasDisplayName()){
			ItemMeta armMeta = arm.getItemMeta();
			if(armMeta.getDisplayName().equalsIgnoreCase("Quiver")){
				List<String> lore = armMeta.getLore();
				int numArrows = Integer.parseInt(ChatColor.stripColor(lore.get(0).replace("Total: ", "")));
				ItemStack arrows = new ItemStack(Material.ARROW, numArrows);
				player.getWorld().dropItem(player.getLocation(), arrows);
				player.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteractBowEvent(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(player.getItemInHand().getType() == Material.BOW && player.getGameMode() != GameMode.CREATIVE){
				Inventory inv = player.getInventory();
				ItemStack arm = player.getInventory().getChestplate();
				if(arm != null){
					if(arm.getItemMeta().hasDisplayName()){
						ItemMeta armMeta = arm.getItemMeta();
						if(armMeta.getDisplayName().equalsIgnoreCase("Quiver")){					
							List<String> lore = armMeta.getLore();
							if(Integer.parseInt(ChatColor.stripColor(lore.get(0).replace("Total: ", ""))) > 0){
								if(!inv.contains(Material.ARROW)){
									if(inv.firstEmpty() > -1){
										int numArrows = Integer.parseInt(ChatColor.stripColor(lore.get(0).replace("Total: ", ""))) - 1;
										lore.set(0, ChatColor.GREEN + "Total: " + Integer.toString(numArrows));
										armMeta.setLore(lore);
										arm.setItemMeta(armMeta);
										player.getInventory().setChestplate(arm);
										inv.addItem(new ItemStack(Material.ARROW, 1));									
										@SuppressWarnings("deprecation")
										Score score = plugin.objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Total:"));
										score.setScore(numArrows);
										player.setScoreboard(plugin.board);
									}
								}
							}
						}
					}
				}			
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerInteractArrowEvent(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(player.getItemInHand().getType() == Material.ARROW){
				ItemStack arm = player.getInventory().getChestplate();
				if(arm != null){
					if(arm.getItemMeta().hasDisplayName()){
						ItemMeta armMeta = arm.getItemMeta();
						if(armMeta.getDisplayName().equalsIgnoreCase("Quiver")){
							ItemStack arrows = player.getInventory().getItemInHand();
							int amount = arrows.getAmount();
							List<String> lore = armMeta.getLore();
							int quiverArrows = Integer.parseInt(ChatColor.stripColor(lore.get(0).replace("Total: ", "")));
							if(arm.getType() == Material.LEATHER_CHESTPLATE){
								int total = plugin.getConfig().getInt("Leather_Quiver.Total-Arrows");
								if((quiverArrows + amount) > total){
									amount = total - quiverArrows;
								}
							}else if(arm.getType() == Material.IRON_CHESTPLATE){
								int total = plugin.getConfig().getInt("Iron_Quiver.Total-Arrows");
								if((quiverArrows + amount) > total){
									amount = total - quiverArrows;
								}
							}else if(arm.getType() == Material.GOLD_CHESTPLATE){
								int total = plugin.getConfig().getInt("Gold_Quiver.Total-Arrows");
								if((quiverArrows + amount) > total){
									amount = total - quiverArrows;
								}
							}else if(arm.getType() == Material.DIAMOND_CHESTPLATE){
								int total = plugin.getConfig().getInt("Diamond_Quiver.Total-Arrows");
								if((quiverArrows + amount) > total){
									amount = total - quiverArrows;
								}
							}
							lore.set(0, ChatColor.GREEN + "Total: " + Integer.toString(quiverArrows + amount));
							armMeta.setLore(lore);
							arm.setItemMeta(armMeta);
							player.getInventory().setChestplate(arm);						
							Score score = plugin.objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Total:"));
							score.setScore(quiverArrows + amount);
							player.setScoreboard(plugin.board);
							player.updateInventory();
							if((player.getInventory().getItemInHand().getAmount() - amount) == 0){
								player.getInventory().setItemInHand(new ItemStack(Material.AIR));
							}else{
								player.getInventory().getItemInHand().setAmount(player.getItemInHand().getAmount() - amount);
							}
						}
					}
				}			
			}		
		}else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(player.getItemInHand().getType() == Material.LEATHER_CHESTPLATE 
					|| player.getItemInHand().getType() == Material.IRON_CHESTPLATE 
					|| player.getItemInHand().getType() == Material.GOLD_CHESTPLATE 
					|| player.getItemInHand().getType() == Material.DIAMOND_CHESTPLATE ){
				ItemStack arm = player.getInventory().getItemInHand();
				if(arm != null){
					if(arm.getItemMeta().hasDisplayName()){
						ItemMeta armMeta = arm.getItemMeta();
						if(armMeta.getDisplayName().equalsIgnoreCase("Quiver")){
							List<String> lore = armMeta.getLore();
							int quiverArrows = Integer.parseInt(ChatColor.stripColor(lore.get(0).replace("Total: ", "")));
							if(quiverArrows > 0){							
								if(quiverArrows > 64){
									if(player.getInventory().firstEmpty() > -1){
										player.getInventory().addItem(new ItemStack(Material.ARROW, 64));
										quiverArrows = quiverArrows - 64;
									}
								}else{
									if(player.getInventory().firstEmpty() > -1){
										player.getInventory().addItem(new ItemStack(Material.ARROW, quiverArrows));
										quiverArrows = 0;
									}
								}
								lore.set(0, ChatColor.GREEN + "Total: " + Integer.toString(quiverArrows));
								armMeta.setLore(lore);
								arm.setItemMeta(armMeta);
								Score score = plugin.objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Total:"));
								score.setScore(quiverArrows);
								player.setScoreboard(plugin.board);
							}
						}
					}
				}
			}
		}	
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerRenameQuiver(InventoryClickEvent event) {
		if (event.getInventory().getType() == InventoryType.ANVIL) {
			if(event.getSlot() == 2){
				for(Items item : CustomItems.Items.values()){
					ItemStack itemStack = CustomItems.getCustomItem(item);
					if(event.getCurrentItem().getType() == itemStack.getType() && event.getCurrentItem().hasItemMeta()){
						if(event.getCurrentItem().getItemMeta().hasDisplayName()){
							if(!event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("Quiver")){
								event.setCancelled(true);
								((Player) event.getWhoClicked()).sendMessage(ChatColor.DARK_RED + "You cannot rename quivers!");
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerPickupArrowEvent(PlayerPickupItemEvent event){
		Player player = event.getPlayer();
		ItemStack item = event.getItem().getItemStack();
		if(item.getType() == Material.ARROW){
			ItemStack arm = player.getInventory().getChestplate();
			if(arm != null){
				if(arm.getItemMeta().hasDisplayName()){
					ItemMeta armMeta = arm.getItemMeta();
					if(armMeta.getDisplayName().equalsIgnoreCase("Quiver")){
						int amount = item.getAmount();
						int amtToQuiver = amount;
						List<String> lore = armMeta.getLore();
						int quiverArrows = Integer.parseInt(ChatColor.stripColor(lore.get(0).replace("Total: ", "")));
						if(arm.getType() == Material.LEATHER_CHESTPLATE){
							int total = plugin.getConfig().getInt("Leather_Quiver.Total-Arrows");
							if((quiverArrows + amount) > total){
								amtToQuiver = total - quiverArrows;
							}
						}else if(arm.getType() == Material.IRON_CHESTPLATE){
							int total = plugin.getConfig().getInt("Iron_Quiver.Total-Arrows");
							if((quiverArrows + amount) > total){
								amtToQuiver = total - quiverArrows;
								
							}
						}else if(arm.getType() == Material.GOLD_CHESTPLATE){
							int total = plugin.getConfig().getInt("Gold_Quiver.Total-Arrows");
							if((quiverArrows + amount) > total){
								amtToQuiver = total - quiverArrows;
							}
						}else if(arm.getType() == Material.DIAMOND_CHESTPLATE){
							int total = plugin.getConfig().getInt("Diamond_Quiver.Total-Arrows");
							if((quiverArrows + amount) > total){
								amtToQuiver = total - quiverArrows;
							}
						}
						int numArrows = Integer.parseInt(ChatColor.stripColor(lore.get(0).replace("Total: ", ""))) + amtToQuiver;
						lore.set(0, ChatColor.GREEN + "Total: " + Integer.toString(numArrows));
						armMeta.setLore(lore);
						arm.setItemMeta(armMeta);
						player.getInventory().setChestplate(arm);
						event.setCancelled(true);
						event.getItem().remove();
						if((item.getAmount() - amtToQuiver) != 0){
							if(player.getInventory().firstEmpty() > -1 || (player.getInventory().contains(Material.ARROW))){
								player.getInventory().addItem(new ItemStack(Material.ARROW, item.getAmount() - amtToQuiver ));
							}else{
								player.getWorld().dropItem(player.getLocation(), new ItemStack(Material.ARROW, item.getAmount() - amtToQuiver));
							}						
						}				
						@SuppressWarnings("deprecation")
						Score score = plugin.objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Total:"));
						score.setScore(numArrows);
						player.setScoreboard(plugin.board);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerItemHeldEvent(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		if(player.getInventory().getItem(event.getNewSlot()) != null){
			if(player.getInventory().getItem(event.getNewSlot()).getType() == Material.BOW){
				ItemStack arm = player.getInventory().getChestplate();
				if(arm != null){
					if(arm.getItemMeta().hasDisplayName()){
						ItemMeta armMeta = arm.getItemMeta();
						if(armMeta.getDisplayName().equalsIgnoreCase("Quiver")){
							List<String> lore = armMeta.getLore();
							int quiverArrows = Integer.parseInt(ChatColor.stripColor(lore.get(0).replace("Total: ", "")));
							@SuppressWarnings("deprecation")
							Score score = plugin.objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Total:"));
							score.setScore(quiverArrows);
							player.setScoreboard(plugin.board);					
						}
					}
				}
			}else{
				player.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
			}
		}else{
			player.setScoreboard(Bukkit.getServer().getScoreboardManager().getNewScoreboard());
		}
	}
	
}
