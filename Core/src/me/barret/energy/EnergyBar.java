package me.barret.energy;

import org.bukkit.ChatColor;

/**
 * 
 * @author cerav
 *
 */


public class EnergyBar {

	private String tag;
	private int maxEnergy;
	private int regainPerTick;
	
	
	private int currentEnergy;
	
	private boolean using; //whether energy is being used, to disable recharge
	private long lastUsedTime;
	
	
	public EnergyBar(String tag, int maxEnergy, int regainPerTick) {
		this.tag = tag;
		this.maxEnergy = maxEnergy;
		this.regainPerTick = regainPerTick;
		
		
		this.currentEnergy = this.maxEnergy;
		
		
	}
	
	/**
	 * 
	 * @param energyToUse
	 * @return true if player had enough energy to use, false if not
	 */
	public boolean use(int energyToUse) {
		if (this.currentEnergy >= energyToUse) {
			this.currentEnergy = this.currentEnergy - energyToUse;
			this.using = true;
			this.lastUsedTime = System.currentTimeMillis();
			
			
			
			return true;
		}
		else {
			this.using = false;
			return false;
		}
	}
	
	
	
	public int getEnergy() {
		return this.currentEnergy;
	}
	
	public int getMaxEnergy() {
		return this.maxEnergy;
	}
	
	public int getIncrementEnergy() {
		return this.regainPerTick;
	}
	
	public void setEnergy(int newEnergy) {
		this.currentEnergy = newEnergy;
	}
	
	public String getName() {
		return this.tag;
	}
	
	
	
	public String getDisplay()
	{
		
		
		String box = "\u2580";
		String bar = ChatColor.WHITE + this.tag + ": ";
		for (int i = 0; i <= this.maxEnergy; i+= (this.maxEnergy/ 20))
		{
			if (this.currentEnergy < i)
			{
				bar += ChatColor.RED + box;
			}
			else
			{
				bar += ChatColor.GREEN + box;	
			}	
		}
		return bar;	
	}
	
	
	

	
	
	
}
	

