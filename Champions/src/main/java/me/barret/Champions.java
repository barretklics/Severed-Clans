package me.barret;

import java.net.http.WebSocket.Listener;

import me.barret.kits.listeners.AssassinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.barret.events.TickUpdateEvent;
import me.barret.kits.kitChangeListener;
import me.barret.kits.kitManager;
import me.barret.kits.editor.kitSelector;
import me.barret.skill.skillListener;
import me.barret.skill.skillManager;
import me.barret.user.userManager;

public class Champions extends JavaPlugin implements Listener{
	private Plugin main;
	
	public void onEnable() {
		main = this;
		
		System.out.println("Hello, Champions has arrived");
		Bukkit.getPluginManager().registerEvents(new kitChangeListener(), this); //Detects when kit is swapped		
		Bukkit.getPluginManager().registerEvents(new userManager(), this); //enables user management
		Bukkit.getPluginManager().registerEvents(new skillListener(), this); //enables kit selector
		Bukkit.getPluginManager().registerEvents(new kitSelector(), this); //enables kit selector
		Bukkit.getPluginManager().registerEvents(new AssassinListener(), this); //enables kit selector
		
		
		
		new kitManager(this); //Load all kits
		new skillManager(this);
		
		
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
	
	public Champions getInstance() {
		return this;
	}


}
