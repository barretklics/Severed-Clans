package me.barret.skill.Mage;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityPickupItemEvent;

import me.barret.Champions;
import me.barret.cooldown.cooldownManager;
import me.barret.events.TickUpdateEvent;
import me.barret.kits.Kit;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.user.user;
import me.barret.utils.UtilItem;
import me.barret.utils.UtilMessage;

public class IcePrison extends Skill implements interactSkill{
	
	
	private static Kit skillKit = kitManager.getKitFromString("Mage");
	
	private static SkillType skillType = SkillType.AXE;
	
	static String skillName = "Ice Prison";	
	
	static String[] description = {"You fucking","","throw kidd"};	
	
	static int MaxLevel = 5;
	
	private static final String itemName = ChatColor.BLUE + "Ice Prison";
	private static HashMap<Location, Long> prisonData = new HashMap<Location, Long>();
	
	private static HashMap<Entity, Integer> prisonItem = new HashMap<Entity, Integer>();
	
	
	
    public IcePrison(Champions i)
    {
		super(i, skillKit, skillName, skillType, description, MaxLevel);

	}
	
	@Override
	public void activate(Player p, user u, int lvl) 
	{
		if (cooldownManager.addCooldown(p, skillName + " " + lvl, 22- lvl, true)) {
			throwPrison(p, lvl);
		}

	}


	public void throwPrison(Player p, int level)
	{
		Location loc = p.getEyeLocation();
		loc.setY(loc.getY() - 0.75);
		Item thrownItem = p.getWorld().dropItem(loc, UtilItem.disposable(itemName, Material.ICE));	
		thrownItem.setVelocity(p.getEyeLocation().getDirection());
		
		prisonItem.put(thrownItem, level);
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_SILVERFISH_HURT, 2.0F, 1.0F);
	}	

	
	//Activation conditions
	@EventHandler
	private void prisonTick(TickUpdateEvent e)
	{
		if (e.getTicks() == 1)
		{
			for (Entity ent : prisonItem.keySet())
			{
				if(ent.isOnGround())
				{
					createPrison(ent.getLocation(), prisonItem.get(ent));
					
					prisonItem.remove(ent);
					ent.remove();
					
					
					return;		
				}
				else
				{
					ent.getWorld().playSound(ent.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.3f, 1f);
				}

			}
		}
	}	
	
	@EventHandler
	private void killPickup (EntityPickupItemEvent e)
	{
		Item i = e.getItem();
		if ( (prisonItem.containsKey(i) || (i.getItemStack().getItemMeta().getDisplayName() == itemName) ))
		{
			e.setCancelled(true);
			createPrison(e.getEntity().getLocation(), prisonItem.get(i));
			prisonItem.remove(i);
			i.remove();
		}
	}
	
	@EventHandler
	private void hitByIcePrison(EntityDamageByEntityEvent e)
	{
		if (e.getCause() == DamageCause.PROJECTILE)
		{
			if (prisonItem.containsKey(e.getDamager()))
			{
				e.setCancelled(true);
				createPrison(e.getEntity().getLocation(), prisonItem.get(e.getEntity()));
				
				prisonItem.remove(e.getDamager());
				e.getDamager().remove();
			}
		}
	}
	
	//Creation
	private void createPrison(Location l, int level)
	{

		int rad = 6;
		//x^2 + y^2 + z^2 = rad^2
		
		World world = l.getWorld();
		
		int x = l.getBlockX();
		int y = l.getBlockY() + 1; //start 1 block above contact point
		int z = l.getBlockZ();
		
		for (int i = x - rad; i <= x + rad; i ++)
		{
			for (int j = y - rad; j <= y + rad; j ++)
			{
				for (int k = z - rad; k <= z + rad; k ++)
				{
					int a = (i-x) * (i-x);
					int b = (j-y) * (j-y);
					int c = (k-z) * (k-z);
					
					
					if ( ((a + b + c) < (rad*rad)) && ( (a + b + c) >= ((rad-1)*(rad-1)) ) )
					//if ( ((a + b + c) <= (rad*rad)) && ( (a + b + c) >= ((rad-1)*(rad-1)) ) ) THIS ICE PRISON HAS A NIPPLE
						
					{
						Location blockLoc = new Location(world, i, j, k);
						
						if (blockLoc.getBlock().getType() == Material.AIR)
						{
							blockLoc.getBlock().setType(Material.ICE);
							
							long minimumTime = (3 + level) * 1000; //minimum ice prison block time in ms
							
							long random = (long) (Math.random() * 500); //added random time to each block
							
							
							long expireTime = System.currentTimeMillis() + minimumTime + random;
							
							prisonData.put(blockLoc, expireTime);
						}
					}
				}
			}
		}
	}
	
	//Erase Prison
	@EventHandler
	private void decayPrison(TickUpdateEvent e)
	{
		if (e.getTicks() == 1)
		{
			for (Location l : prisonData.keySet())
			{
				if (prisonData.get(l) <= System.currentTimeMillis())
				{
					if (l.getBlock().getType() == Material.ICE)
					{
						l.getBlock().setType(Material.AIR);					
					}
				}
			}
		}
	}
	
	
	//Prevent Prison Vanilla decay and breaking
	@EventHandler(priority = EventPriority.LOWEST)
	private void icePrisonBreakEvent(BlockBreakEvent e)
	{
		if (!(e.getBlock().getType() == Material.ICE))
		{
			return;
		}
		
		if (!(prisonData.containsKey(e.getBlock().getLocation())))
		{
			return;
		}
		e.setCancelled(true);
		e.getBlock().setType(Material.AIR); //allows breaking
	}
	
	

	
	@EventHandler
	private void icePrisonMelt(BlockFadeEvent e)
	{
		if (!(e.getBlock().getType() == Material.ICE))
		{
			return;
		}
		
		if (!(prisonData.containsKey(e.getBlock().getLocation())))
		{
			return;
		}
		
		e.setCancelled(true);
	}
}
