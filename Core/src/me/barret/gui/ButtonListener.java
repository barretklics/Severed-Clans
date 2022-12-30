package me.barret.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ButtonListener implements Listener {
	
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Gui g = guiManager.getGui(e.getView().getTitle());
		Button b = guiManager.getButton(e.getCurrentItem());
		Player p = (Player) e.getWhoClicked();
		
		if (g == null) return;
		Bukkit.getPluginManager().callEvent(new ButtonClickEvent(p, g, b, e.getClick(), e.getSlot()));
		e.setCancelled(true);
	}
	

}
