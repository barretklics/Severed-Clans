package me.barret.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.barret.build.Build;
import me.barret.events.TickUpdateEvent;
import me.barret.user.user;
import me.barret.user.userManager;
import me.barret.utils.UtilMessage;


public class kitChangeListener implements Listener{

	
	//Detects When a player changes kits

	
	protected String KitChangeMessage = ChatColor.BLUE + "Class> " + ChatColor.GRAY + "You changed to " + ChatColor.WHITE;

	
	//TODO performance improvements. This does not need to be ran every tick. Armor break/put on event with occasional sanity checks?
	@EventHandler
	public void updateKit(TickUpdateEvent e)
	{
		for(Player p : Bukkit.getOnlinePlayers())
		{	
			if (p != null) 
			{	

				user u = userManager.getUser(p.getUniqueId());
				
				
				
				//Gets raw kit from armor
				Kit k = Kit.getKit(p);
				

				//Set new champions kit based on that armor
				if ((u.getKit() != k)) {

					Bukkit.getPluginManager().callEvent(new kitChangeEvent(p, p.getUniqueId(), u.getKit(), k));
					u.setCurrentKit(k);
					
				}	
			}
		}	
	}
	
	//Place any on kit change logic in here
	@EventHandler
	public void kitChanged(kitChangeEvent e) {
		Player p = e.getPlayer();
		user u = userManager.getUser(p.getUniqueId());
		
		
		
		if (e.getNewKit() == null) {
			UtilMessage.sendBasic(p, KitChangeMessage, "Naked");
			return;
			
		}
		int bid = u.getActiveBuildIndex(e.getNewKit());
		
		Build b = u.getBuildsForKit(e.getNewKit()).get(bid);
				
				
		//Send a message when kit is changed
		UtilMessage.sendBasic(p, KitChangeMessage, e.getNewKit().getKitName());
		
		String build_name;
		
		if (bid == 0) build_name = "Default Build";
		else build_name = "Build " + bid;
			
		ArrayList<String> list = b.asList();
		String str = String.join(" \n", list);
		
		UtilMessage.sendBasic(p, ChatColor.BLUE + e.getNewKit().getKitName() + "> ", "Applied " + ChatColor.GREEN + build_name);
		p.sendMessage(str);
		
		
		
		
		
		//TODO
		//Play a sound
		
	}
	
	
	
	
}	
	
	
	
	
	
	

