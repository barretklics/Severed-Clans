package me.barret.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;


public class guiManager {

	private static List<Gui> guiList = new ArrayList<>();

	public static void addGui(Gui g) {
		guiList.add(g);
	}
	
	public static List<Gui> getGuis() {
		return guiList;
	}
	
	
	
	
	public static Gui getGui(String inv_name) {
		for (Gui g : guiList) {
			if (g.getName().equalsIgnoreCase(inv_name)) {
				return g;
			}
		}
		return null;
	}
	
	
	public static Button getButton(ItemStack i) {
		for (Gui g : guiList) {
			for (Button b: g.getButtons()) {
				if (b.getItem().equals(i)) {
					return b;
				}
			}
		}
		return null;
	}
	

}

