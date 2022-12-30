package me.barret.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Gui {

	
	
	
	
	
	private String name;
	private Inventory inventory;
	private List<Button> buttons;
	
	public Gui(Player p, String name, int size, Button[] buttons) {
		
		this.name = name;
		this.inventory = Bukkit.createInventory(p, size, name);
		this.buttons = new ArrayList<>(Arrays.asList(buttons));
		
		guiManager.addGui(this);
		populate();
	}
	
	
	public void populate() {
		for (Button b : buttons) {
			this.inventory.setItem(b.getSlot(), b.getItem());
		}
	}
	
	public void addButton(Button b) {
		
		this.buttons.add(b);
		
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}
	
	
	public String getName() {
		return this.name;
	}
	
	public List<Button> getButtons(){
		return this.buttons;
	}
	
	
}
