package io.flairhead.testplugin;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unchecked")
public class TestPlugin extends JavaPlugin implements Listener {
	//I could have used multiple classes, but it seems like overkill for something this simple
	//If you want an example of my grasp of polymorphism, head on over to my other sample plugin, ItemFilter
	//https://github.com/FlairHead/PluginFilter
	ArrayList<Player> playersInWorlds = new ArrayList<>();
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(this, this);

		Collection<Player> players = (Collection<Player>) this.getServer().getOnlinePlayers(); 
		for (Player p : players) {
			if (this.getConfig().getList("worlds").contains(p.getWorld().getName())) {
				this.handleNewPlayer(p);
			}
		}
	}
	
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event){
		Player p = event.getPlayer();
		if (this.getConfig().getList("worlds").contains(p.getWorld().getName())) {
			this.handleNewPlayer(p);
		} else if (!this.getConfig().getList("worlds").contains(p.getWorld().getName()) && playersInWorlds.contains(p)) {
			playersInWorlds.remove(p);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		System.out.println("awodiaowdijifgbsdfg");
		Player p = event.getPlayer();
		if (this.getConfig().getList("worlds").contains(p.getWorld().getName())) {
			this.handleNewPlayer(p);
		} else if (!this.getConfig().getList("worlds").contains(p.getWorld().getName()) && playersInWorlds.contains(p)) {
			playersInWorlds.remove(p);
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		//I apologize for this next conditional
		System.out.println("Whup");
		if (playersInWorlds.contains(event.getWhoClicked()) && 
				this.getConfig().getList("items").contains(event.getCurrentItem().getType().toString()) && !event.getWhoClicked().hasPermission("testplugin.force.override")) {
			event.setCancelled(true);
			System.out.println("Titty sprinkles");
		}
	}
	
	public void handleNewPlayer(Player p) {
		if (!playersInWorlds.contains(p)) {
			playersInWorlds.add(p);
			System.out.println("Adding player");
		}
		
		System.out.println("Handling new player");
		
		int currentInvSlot = 0;
		ArrayList<String> items = (ArrayList<String>) this.getConfig().getList("items");
		
		for (String s : items) {
			Material type = Material.getMaterial(s);
			ItemStack item = null;
			if (s == null) {
				System.out.println("[TestPlugin] '" + s + "' is not a valid type.");
				item = new ItemStack(Material.AIR);
			} else {
				item = new ItemStack(type);
			}
			
			p.getInventory().setItem(currentInvSlot, item);
			currentInvSlot++;
		}
	}
}
