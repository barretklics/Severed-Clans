package me.barret.energy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;



public class energyManager {
	private static HashMap<UUID, List<EnergyBar>> Bars = new HashMap<UUID, List<EnergyBar>>();
	
	
	
	
	
	//Removes/deletes cooldowns when the time is passed
	public static void iterateEnergyBars() {
		
		for (UUID key : Bars.keySet()) {
			
			for (EnergyBar e : Bars.get(key))
			{
				if (e.getEnergy() >= e.getMaxEnergy()) e.setEnergy(e.getMaxEnergy()); //cap energy at max energy
				
				if (e.getEnergy() < e.getMaxEnergy()) {//increment energy if less than max
					e.setEnergy(e.getEnergy() + e.getIncrementEnergy());
					if (e.getEnergy() > e.getMaxEnergy()) e.setEnergy(e.getMaxEnergy());
				}

				
			}
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
	public static EnergyBar getEnergyBar(Player p, String cooldownName, int maxEnergy, int incrementEnergy) {
		

		EnergyBar e = new EnergyBar(cooldownName, maxEnergy, incrementEnergy);
		List<EnergyBar> ebar_templist = new ArrayList<>();
		ebar_templist.add(e);
		
		
		//No energies across whole server
		if (Bars.size() == 0) {
			Bars.put(p.getUniqueId(), ebar_templist);
			//UtilMessage.use(p, cooldownName);
			//System.out.println("HERE 1");
			
			return e; //return added energy bar	
		}
		
		//Player has no energies
		if (!(Bars.containsKey(p.getUniqueId()))) {
			Bars.put(p.getUniqueId(), ebar_templist);
		//	UtilMessage.use(p, cooldownName);
			//System.out.println("HERE 2");
			return e;
		}
		
		
		//Player has no energies 2
		List<EnergyBar> playerEnergiesList = Bars.get(p.getUniqueId());
		
		if (playerEnergiesList.size() == 0) {
			Bars.put(p.getUniqueId(), ebar_templist);
		//	UtilMessage.use(p, cooldownName);
		//	System.out.println("HERE 3");
			
			
			return e;
		}
		
		
		//If player has a energy with the same tag, return that energy
		for (EnergyBar e_temp : playerEnergiesList) {
			if (e_temp.getName().equalsIgnoreCase(cooldownName)) {
				return e_temp;
			}
			
		}
		
		
		//Begin adding new cooldown
		Bars.get(p.getUniqueId()).add(e);
		return e;	
	//	UtilMessage.use(p, cooldownName);
		//System.out.println("HERE 5");
		
		
	}	
	public static EnergyBar getEnergyBar(Player p, String cooldownName) {
		
		//No energies across whole server
		if (Bars.size() == 0) {
			
			return null;
		}
		
		//Player has no energies
		if (!(Bars.containsKey(p.getUniqueId()))) {
			return null;
		}
		
		
		//Player has no energies 2
		List<EnergyBar> playerEnergiesList = Bars.get(p.getUniqueId());
		
		if (playerEnergiesList.size() == 0) {

			return null;
		}
		
		//If player has a energy with the same tag, return that energy
		for (EnergyBar e_temp : playerEnergiesList) {
			if (e_temp.getName().equalsIgnoreCase(cooldownName)) {
				return e_temp;
			}
			
		}

		return null;	
		
		
	}	
	

	
}
