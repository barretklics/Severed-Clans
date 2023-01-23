package me.barret;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.barret.cooldown.cooldownListener;
import me.barret.energy.energyListener;
import me.barret.events.TickUpdateEvent;
import me.barret.gui.ButtonListener;
import me.barret.rightClick.rightClickListener;
/**
 * 
 * @author cerav
 *
 */
public class Core extends JavaPlugin implements Listener{
	@SuppressWarnings("unused")
	private Plugin main;

	@Override
	public void onEnable() {
		main = this;

		System.out.println("Hello, core has arrived");
		
		Bukkit.getPluginManager().registerEvents(new ButtonListener(), this); //Detects when kit is swapped		
		Bukkit.getPluginManager().registerEvents(new cooldownListener(), this); //Detects when kit is swapped		
		Bukkit.getPluginManager().registerEvents(new energyListener(), this); //Detects when kit is swapped		
		Bukkit.getPluginManager().registerEvents(new rightClickListener(), this); //Detects when kit is swapped		

		//TickUpdate Event
    	new BukkitRunnable()
    	{
    		@Override
    		public void run()
    		{
    			getServer().getPluginManager().callEvent(new TickUpdateEvent(1));
    		}

    	}.runTaskTimer(this, 0, 1);


	
	}
	
	public Core getInstance()
	{
		return this;
	}
	
	
	
}
