package me.barret.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;

public class ButtonClickEvent extends Event{
	private static final HandlerList handlers = new HandlerList();
	
	
	 public static HandlerList getHandlerList() {
	        return handlers;
	 }

	 public HandlerList getHandlers() {
	        return handlers;
	}

	 
	 private Player p;
	 private Gui gui;
	 private Button b;
	 private ClickType clicktype;
	 private int slot;
	 
	public ButtonClickEvent(Player p, Gui g, Button b, ClickType clickType, int slot) {
		this.p = p;
		this.gui = g;
		this.b = b;
		this.clicktype = clickType;
		this.slot = slot;
		
	}
	 
	public Player getPlayer() {
		return p;
	}
	public Gui getGui() {
		return gui;
	}
	
	public Button getButton() {
		return b;
	}
	
	public ClickType getClick() {
		return clicktype;
	}
	
	public int getSlot() {
		return slot;
	}
	
	 
	 
}
