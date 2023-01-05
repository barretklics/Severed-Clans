package me.barret.rightClick;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


import me.barret.utils.UtilInventory;
import me.barret.utils.UtilItem;

public class rightClickListener implements Listener{
	
	@EventHandler
	public void rightClick(PlayerInteractEvent event)
	{
		
		
		if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() ==  Action.RIGHT_CLICK_BLOCK))
		{
			Player p = event.getPlayer();
			if (UtilItem.isSword(UtilInventory.getItemInMainHand(p))) {
				rightClickManager.playerUseRightClick(p);
				
			}

		}
	}
}
