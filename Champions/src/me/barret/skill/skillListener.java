package me.barret.skill;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import me.barret.build.Build;
import me.barret.energy.EnergyBar;
import me.barret.energy.energyManager;
import me.barret.events.TickUpdateEvent;
import me.barret.rightClick.rightClickManager;
import me.barret.user.user;
import me.barret.user.userManager;
import me.barret.utils.UtilInventory;
import me.barret.utils.UtilItem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
public class skillListener implements Listener{
	
	public skillListener() {}
	
	
	
	@EventHandler
	public void interactSkill(PlayerInteractEvent event)
	{
		
		
		if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() ==  Action.RIGHT_CLICK_BLOCK))
		{
			Player p = event.getPlayer();
			user u = userManager.getUser(p.getUniqueId());
	
			Build b = u.getCurrentSkills();
			
			if (b != null)
			{
	
				if (UtilItem.isSword(UtilInventory.getItemInMainHand(p)) && (b.getSword() != null) && (b.getSword() instanceof interactSkill))
				{
					
					interactSkill skill = (interactSkill) b.getSword();
					int level = b.getBuildSkillFromSkill(b.getSword()).getLevel();
					level = levelBoost(p, level);
					skill.activate(p, u, level);
				}
				
				if (UtilItem.isAxe(UtilInventory.getItemInMainHand(p)) && (b.getAxe() instanceof interactSkill))
				{
					interactSkill skill = (interactSkill) b.getAxe();
					int level = b.getBuildSkillFromSkill(b.getAxe()).getLevel();
					level = levelBoost(p, level);
					skill.activate(p, u, level);
				}		
			}
		}
	}

	
	
	@EventHandler
	public void channelSkill(TickUpdateEvent e)
	{
		if (e.getTicks() == 1)
		{
			for(Player p : Bukkit.getOnlinePlayers())
			{
				user u = userManager.getUser(p.getUniqueId());

				Build b = u.getCurrentSkills();
				
				if (b == null) return;
				if ((rightClickManager.isHoldingRightClick(p)) && (UtilItem.isSword(UtilInventory.getItemInMainHand(p))) && (b.getSword() instanceof channelSkill))
				{
					channelSkill skill = (channelSkill) b.getSword();
					int level = b.getBuildSkillFromSkill(b.getSword()).getLevel();
					level = levelBoost(p, level);
					skill.channel(p, u, level);	
				}
				//Display
				if ((UtilItem.isSword(UtilInventory.getItemInMainHand(p))) && (b.getSword() instanceof channelSkill))
				{
					EnergyBar bar = energyManager.getEnergyBar(p, b.getSword().getName());
					if (bar == null) return;
					p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(bar.getDisplay()));
				}
				
				

			
			}
		}
	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private int levelBoost(Player p, int level)
	{
		if (UtilItem.isBooster(UtilInventory.getItemInMainHand(p)))
		{
			return level + 1;
		}
		return level;
		
	}	
	
	
	
}
