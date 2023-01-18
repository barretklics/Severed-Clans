package me.barret.skill.Assassin;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import me.barret.Champions;
import me.barret.build.Build;
import me.barret.build.BuildChangeEvent;
import me.barret.events.TickUpdateEvent;
import me.barret.kits.Kit;
import me.barret.kits.kitChangeEvent;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.user.user;
import me.barret.user.userManager;
//Author: ethvor - first skill started: 12-30-22 finished: 1-1-23
public class Flash extends Skill implements interactSkill{ //inheritance
	
	//the first skill i ever coded
	private static Kit skillKit = kitManager.getKitFromString("Assassin"); 		//sets class for skill to assassin
	
	private static SkillType skillType = SkillType.AXE; 						//sets skill type to axe skill
	
	static String skillName = "Flash";
	
	static String[] description = {"Teleport forward a short distance"};
	
	static int MaxLevel = 3;
	

	
	private static HashMap<Player, Long> timeAtLastChargeIncrement = new HashMap<Player, Long>();
	
	private static HashMap<Player, Integer> chargesOwned = new HashMap<Player, Integer>();
	
	private static HashMap<Player, Long> timeSinceLastFlash = new HashMap<Player, Long>();
	
	private static HashMap<Player, Location> lastIteratedPassableLocation = new HashMap<Player, Location>();
	
	
	
	public Flash(Champions i)
	{
			super(i, skillKit, skillName, skillType, description, MaxLevel);
			
	}

	@Override
	public void activate(Player p, user u, int lvl)  //activate occurs on right click - lvl is ability level selected in ench table
	{
	doFlash(p,lvl);
	}

	public void doFlash(Player p, int lvl)
	{
		if (p.getEyeLocation().getBlock().isPassable() && p.getEyeLocation().add(0,-1,0).getBlock().isPassable()) //checks if player location upon skill activation is within blocks
		{
			if (chargesOwned.get(p) > 0 && timeSinceLastFlash.get(p)+20<=System.currentTimeMillis()) //checks for cooldown on flash and if player has charges.
			{
				doTeleport(p,lvl);
				timeAtLastChargeIncrement.put(p, System.currentTimeMillis()); //makes it so that a player cannot get a charge instantly after flashing. waits 2.5s after each flash ?
				chargesOwned.put(p,chargesOwned.get(p) - 1); //decrement charges	
				timeSinceLastFlash.put(p, System.currentTimeMillis()); //set cooldown for 2 flashes in a row
				p.sendMessage("Charges [-]: " + chargesOwned.get(p));
			
		}
		}
		//else p.sendMessage("Cannot use flash while within unpassable blocks.");
	}
		
	
	
	/* TODELETE old method-----------!!!!!!!!!!!!!!!!!
	//DOESNT WORK. ONLY DETECTS WHEN GOING FROM ASSASSIN TO NAKED.
	@EventHandler // WILL BE CALLED every time kit is changed. 
	//this is going to listen to spigot api's event handler which barret used to make a custom event buildchange
	private void onBuildChange(kitChangeEvent e) //needs new event
	{ 
		Player p = e.getPlayer(); //spigot
		user u = userManager.getUser(p.getUniqueId()); //barret user type calls spigot api to get players uuid
		if(u.getCurrentBuild().getAxe().getName() == skillName)// checks if player build contains axe skill called skillname ("flash")
		{ 
			timeSinceLastFlash.put(p, System.currentTimeMillis()); //sets time since last flash to "current time"
			chargesOwned.put(p, 0); //sets the player's charges to 0 within a hash map called chargesOwned
			timeAtLastChargeIncrement.put(p, System.currentTimeMillis()); //logs last time player was given a flash charge
			p.sendMessage("Charges (INITIAL): " + chargesOwned.get(p));
		}		
	}
	*/
	
	
	/**
	 * @author cerav
	 * Example of on build change to replace old
	 * @param e
	 */
	@EventHandler
	private void onBarretDoSomethingEvent(BuildChangeEvent e) {
		Build newBuild = e.getNewBuild(); //The build that is changed into
		Player p = e.getPlayer();
		
		if (e.getNewBuild() == null) return; //null check
		if (e.getNewBuild().getAxe() == null) return; //null check
		if(newBuild.getAxe().getName() == skillName)// checks if player build contains axe skill called skillname ("flash")
		{ 
			timeSinceLastFlash.put(p, System.currentTimeMillis()); //sets time since last flash to "current time"
			chargesOwned.put(p, 0); //sets the player's charges to 0 within a hash map called chargesOwned
			timeAtLastChargeIncrement.put(p, System.currentTimeMillis()); //logs last time player was given a flash charge
			p.sendMessage("Charges (INITIAL): " + chargesOwned.get(p));
		}		
		
		
		
	}
	
		
		@EventHandler	//adds charges every 2.5s after checks
		private void onTickUpdate(TickUpdateEvent t) { //every tick that server updates, calls TickUpdateEvent -> calls onTickUpdate


			for (Player p : timeAtLastChargeIncrement.keySet()) {

				user u = userManager.getUser(p.getUniqueId());
				if (u.getCurrentBuild() != null) {
					if (u.getCurrentBuild().getAxe().getName() == skillName)//if skill is flash
					{


						if (System.currentTimeMillis() >= timeAtLastChargeIncrement.get(p) + 2500) //if charge cooldown done (2.5s)
						{
							if (chargesOwned.get(p) < u.getCurrentBuild().getBuildSkillFromSkill(u.getCurrentBuild().getAxe()).getLevel()) { //barret moment - if charges is not greater than current level
								chargesOwned.put(p, chargesOwned.get(p) + 1); //charges = charges + 1 in hashmap called chargesOwned
								timeAtLastChargeIncrement.put(p, System.currentTimeMillis());
								p.sendMessage("Charges [+]:  " + chargesOwned.get(p));
							}
						}

					}
				}
			}
		}
		
		public void doTeleport(Player p, int lvl)
		{
			if (p.getEyeLocation().getBlock().isPassable() && p.getEyeLocation().add(0,-1,0).getBlock().isPassable()) // checks initial head and foot block of player to see if both are within a passable block.
			{
				Location l = p.getLocation().clone();
				Vector v = l.getDirection().clone();
				v.multiply(5); //multiplies location vector by 5. is only used to set facing direction later
				l.add(v);	
			
				Location eyelocation = p.getEyeLocation();
				BlockIterator blocksToAddFoot = new BlockIterator(eyelocation, -1, 5);
				Location blockToAdd = eyelocation;
				while(blocksToAddFoot.hasNext()&&blockToAdd.getBlock().isPassable()) 
				{
					blockToAdd = blocksToAddFoot.next().getLocation();
                  
					if (blockToAdd.getBlock().isPassable() && blockToAdd.add(0,1,0).getBlock().isPassable()) //checks if foot block and eye block in path are both passable. if so, adds to last possible loc HashMap
					{
						p.getWorld().spawnParticle(Particle.WAX_ON,blockToAdd.add(0, 1, 0),1);
						lastIteratedPassableLocation.put(p, blockToAdd.add(0,-1,0));//
					}
				}
              
            
				Location tpLoc = lastIteratedPassableLocation.get(p).setDirection(v);
				tpLoc.add(0.5, -0.5, 0.5);
				if(tpLoc.getBlock().isPassable())
				{
					p.teleport(tpLoc);
					p.getWorld().playSound(tpLoc, Sound.ENTITY_WITHER_SHOOT,(float) 1.50,(float)2);
					p.getWorld().playSound(tpLoc, Sound.ENTITY_ENDERMITE_DEATH,(float) 1.50,(float)2 );
				}
				
				else p.sendMessage("Flash Block Phase Cancelled");
             
				
				p.setFallDistance(0); //sets fall dist 0 causing player to not take fall damage
			}
			
			// else do nothing if initial head and foot check fails
			
		}
}

