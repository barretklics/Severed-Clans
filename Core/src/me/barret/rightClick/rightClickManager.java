package me.barret.rightClick;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.barret.energy.EnergyBar;

public class rightClickManager {

	private static final long rightClickCooldown = 260l; //260 ms
	
	
	//private static HashMap<UUID, List<EnergyBar>> Bars = new HashMap<UUID, List<EnergyBar>>();
	private static HashMap<Player, Long> map = new HashMap<Player, Long>();
	
	
	public static void playerUseRightClick(Player p) {
		map.put(p, System.currentTimeMillis());
	}
	
	
	public static boolean isHoldingRightClick(Player p) {
		if (map.size() == 0) return false;
		if (!(map.containsKey(p))) return false;
		long timeSinceLastRightClick = System.currentTimeMillis() - map.get(p);
		
		return timeSinceLastRightClick <= rightClickCooldown;
	}
	
	
}
