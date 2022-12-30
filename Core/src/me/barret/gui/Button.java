package me.barret.gui;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import me.barret.utils.UtilItem;

public class Button {
	
	private int slot;
	private ItemStack item;
	private String name;
	private String[] lore;
	
	public Button(int slot, ItemStack item, String name, List<String> lore) {
		this.slot = slot;
		this.name = name;
		
		
		this.lore = lore.toArray(new String[0]);
		
		this.item = UtilItem.setDisplayName(item, name);
		this.item = UtilItem.setLore(item, lore);

	}
	public Button(int slot, ItemStack item, String name) {
		this.slot = slot;
		this.name = name;
		this.item = UtilItem.setDisplayName(item, name);

	}
	public int getSlot() {
		return slot;
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public String getName() {
		return name;
	}
	
	public void setItem(ItemStack item) {
		this.item = item;
		
	}
	

}
