package info.trentech.Quiver;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItems {
	
	private static Quiver plugin;
	public CustomItems(Quiver plugin) {
		CustomItems.plugin = plugin;
	}
	
	public static enum Items{
		Leather_Quiver,
		Iron_Quiver,
		Gold_Quiver,
		Diamond_Quiver,
	}
	
	public static ItemStack getCustomItem(Items item){
		ItemStack itemStack = null;
		switch (item){
		case Leather_Quiver:
			itemStack = buildItem("Leather_Quiver", Material.LEATHER_CHESTPLATE);
			break;
		case Iron_Quiver:
			itemStack = buildItem("Iron_Quiver", Material.IRON_CHESTPLATE);
			break;
		case Gold_Quiver:
			itemStack = buildItem("Gold_Quiver", Material.GOLD_CHESTPLATE);
			break;
		case Diamond_Quiver:
			itemStack = buildItem("Diamond_Quiver", Material.DIAMOND_CHESTPLATE);
			break;
		}
		return itemStack;
	}
	
	public static ItemStack buildItem(String name, Material material){
		ItemStack itemStack = new ItemStack(material, 1);
		List<String> lore = new ArrayList<String>();
		ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName("Quiver");
		lore.add(0, ChatColor.GREEN + "Total: 0");
		itemMeta.setLore(lore);
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	public static void createRecipes(){		
		for(Items item : CustomItems.Items.values()){
			ItemStack itemStack = CustomItems.getCustomItem(item);
			ShapedRecipe recipe = new ShapedRecipe(itemStack);
			recipe.shape("ABC", "DEF", "GHI");
			char[] shapes = {'A','B','C','D','E','F','G','H','I'};
			List<String> list = plugin.getConfig().getStringList(item.toString() + ".Recipe");
			int slotIndex = 0;
			for(String row : list){
				String[] ingredients = row.split(",");
				for(String ingredient : ingredients){
					if(!ingredient.equalsIgnoreCase("EMPTY")){	
						recipe.setIngredient(shapes[slotIndex], Material.getMaterial(ingredient));
					}
					slotIndex++;
				}
			}
			Bukkit.addRecipe(recipe);
		}
	}

}
