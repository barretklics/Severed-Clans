package me.barret.cooldown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.barret.utils.UtilMessage;
import me.barret.utils.UtilPlayer;

public class cooldownManager {

	
	private static HashMap<UUID, List<Cooldown>> cooldowns = new HashMap<UUID, List<Cooldown>>();	
	
	
	
	
	//Removes/deletes cooldowns when the time is passed
	public static void iterateCooldowns() {
		
		for (UUID key : cooldowns.keySet()) {
		//	System.out.println("set: " + key.toString());
			
			List<Cooldown> toRemove = new ArrayList<>();
			
			
			for (Cooldown c : cooldowns.get(key)) {
				//System.out.println(c.getName() + ": " + c.getRemainingTime());
				if (c.isPassed()) {
					toRemove.add(c);
				}
			}
			
			for (Cooldown c : toRemove) {
				Player p = UtilPlayer.getPlayerFromUUID(key);
				if (p != null ) {
					if (c.notifyUsable()) {
						UtilMessage.recharge(p, c.getName());
						//System.out.println("REMOVED COOLDOWN: " + c.getName());
					}
				}
				
			}
			
			
			cooldowns.get(key).removeAll(toRemove);
			
			
		}
	}
	
	
	/**
	 * 
	 * @param p
	 * @param cooldownName
	 * @param duration
	 * @param informWhenComplete
	 * @return true if added cooldown, false if existing
	 */
	public static boolean addCooldown(Player p, String cooldownName, long duration, boolean informWhenComplete) {
		Cooldown c = new Cooldown(cooldownName, duration, informWhenComplete);
		List<Cooldown> cd_templist = new ArrayList<>();
		cd_templist.add(c);
		
		if (cooldowns.size() == 0) {
			cooldowns.put(p.getUniqueId(), cd_templist);
			UtilMessage.use(p, cooldownName);
			//System.out.println("HERE 1");
			
			return true;	
		}
		
		
		if (!(cooldowns.containsKey(p.getUniqueId()))) {
			cooldowns.put(p.getUniqueId(), cd_templist);
			UtilMessage.use(p, cooldownName);
			//System.out.println("HERE 2");
			return true;
		}
		
		
		
		List<Cooldown> iterList = cooldowns.get(p.getUniqueId());
		
		if (iterList.size() == 0) {
			cooldowns.put(p.getUniqueId(), cd_templist);
			UtilMessage.use(p, cooldownName);
		//	System.out.println("HERE 3");
			return true;
		}
		
		
		//If player has a cooldown with the same tag, fail to add cooldown and return false
		for (Cooldown c_temp : iterList) {
			if (c_temp.getName().equalsIgnoreCase(cooldownName)) {
				
				if (c_temp.showFail()) UtilMessage.cooldown(p, c_temp.getName(), c_temp.getRemainingTime()); //display fail message if appropriate
				
				//System.out.println("HERE 4");
				return false;
			}
			
		}
		
		
		//Begin adding new cooldown
		cooldowns.get(p.getUniqueId()).add(c);
		UtilMessage.use(p, cooldownName);
		//System.out.println("HERE 5");
		return true;	
	}
	
	
}
