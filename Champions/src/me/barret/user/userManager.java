package me.barret.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.barret.kits.Kit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;




public class userManager implements Listener
{

	private static List<user> users = new ArrayList<>();
	
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event)
	{
		Player p = event.getPlayer();
		if (users.size() == 0 || users == null) {
			user u = new user(p.getUniqueId());
			users.add(u);
			u.initializeBuilds();
			
			
			
		}
		
		for(user u: users) {
			if (u.getUUID() == p.getUniqueId()) return; //stop executing if user is in list
		}
		
		
		user u = new user(p.getUniqueId());
		users.add(u);
		u.initializeBuilds();
		
	}

	
	
	/**
	 * 
	 * 
	 * @param uuid
	 * @return returns the internal 'user' in regards to the Champions plugin
	 */
	public static user getUser(UUID uuid)
	{
		for (user u : users)
		{
			if (u.getUUID() == uuid)
			{
				return u;
			}
		}
		return null;
	}
	
	
	
}
