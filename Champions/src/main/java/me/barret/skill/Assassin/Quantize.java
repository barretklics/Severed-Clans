package me.barret.skill.Assassin;


	import java.util.HashMap;

	import me.barret.build.BuildChangeEvent;
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
import me.barret.events.TickUpdateEvent;
	import me.barret.kits.Kit;
	import me.barret.kits.kitChangeEvent;
	import me.barret.kits.kitManager;
	import me.barret.skill.Skill;
	import me.barret.skill.interactSkill;
	import me.barret.user.user;
	import me.barret.user.userManager;
	//Author: ethvor - second skill started: 1-1-22 finished:
	public class Quantize extends Skill implements interactSkill{ //inheritance
		
		
		private static Kit skillKit = kitManager.getKitFromString("Assassin"); 		//sets class for skill to assassin
		
		private static SkillType skillType = SkillType.AXE; 						//sets skill type to axe skill
		
		static String skillName = "Quantize";
		
		static String[] description = {"Experience quantum entanglement, teleporting forwards ","a great distance with the ability to return to your previous location."};
		
		static int MaxLevel = 5;
		
		
		
		private static HashMap<Player, Long> timeSinceLastQuantize = new HashMap<Player, Long>(); //used for internal cd of 20ms that prevents double usage when clicking blocks
		
		private static HashMap<Player, Location> lastIteratedPassableLocation = new HashMap<Player, Location>(); //used for iterated path tracing to prevent block phasing
		
		private static HashMap<Player, Long> timeOfEntanglement = new HashMap<Player, Long>();
		
		private static HashMap<Player, Boolean> isEntangled = new HashMap<Player, Boolean>();
		
		private static HashMap<Player, Location> initialLocation = new HashMap<Player,Location>();
		
		private static HashMap<Player, Integer> skillLevel = new HashMap<Player,Integer>();
		
		private static HashMap<Player, Long> quantizeCooldown = new HashMap<Player,Long>();
		
		private static HashMap<Player, Boolean> isQuantizeOnCooldown = new HashMap<Player,Boolean>();
		public Quantize(Champions i)
		{
				super(i, skillKit, skillName, skillType, description, MaxLevel);
				
		}

		@Override
		public void activate(Player p, user u, int lvl)  //activate occurs on right click - lvl is ability level selected in ench table
		{
			if (isQuantizeOnCooldown.get(p) == false)
			{
			doQuantize(p,lvl);
			skillLevel.put(p, lvl);
			}
			else p.sendMessage("Quantize is on cooldown for " + Long.toString((quantizeCooldown.get(p) + 20000 - (lvl *1000)  - System.currentTimeMillis())/1000 ) + " more seconds");
		}

		/**
		 * Barret edit to build change event and null checking
		 * @param e
		 */
		//need a few cases: when player changes build ACTIVATES THIS THEORETICAL EVENT. when player logs into this build ACTIVATES THIS THEORETICAL EVENT when player changes from knight to assassin and this build is selected ACTIVATES THIS THEORETICAL EVENT @barret
		//DOESNT WORK. ONLY DETECTS WHEN GOING FROM ASSASSIN TO NAKED and V.V.
		@EventHandler // WILL BE CALLED every time kit is changed.
		//this is going to listen to spigot api's event handler which barret used to make a custom event buildchange
		private void onBuildChange(BuildChangeEvent e) //needs new event
		{
			Player p = e.getPlayer(); //spigot
			user u = userManager.getUser(p.getUniqueId()); //barret user type calls spigot api to get players uuid

			if (e.getNewBuild() == null) return;
			if (e.getNewBuild().getAxe() == null) return;


			if(u.getCurrentBuild().getAxe().getName() == skillName)// checks if player build contains axe skill called skillname ("flash")
			{
				timeSinceLastQuantize.put(p, System.currentTimeMillis()); //sets time since last flash to "current time"
				isEntangled.put(p, false);
				isQuantizeOnCooldown.put(p, false);
			}
		}

		public void doQuantize(Player p, int lvl)
		{
			if (p.getEyeLocation().getBlock().isPassable() && p.getEyeLocation().add(0,-1,0).getBlock().isPassable()&& isEntangled.get(p) == false) //disallows initiation of teleport from within solid blocks or unpassable blocks (doors, trapdoors, etc)
			{
				if (timeSinceLastQuantize.get(p)+20<=System.currentTimeMillis() && isEntangled.get(p) == false) //checks for cooldown on flash and if player has charges.
				{
					doEntangle(p,lvl);

					timeSinceLastQuantize.put(p, System.currentTimeMillis()); //set up cooldown for 2 right clicks in a row (20ms internal cd)

				}
				
			}
			
			else if (timeSinceLastQuantize.get(p)+20<=System.currentTimeMillis() && isEntangled.get(p) == true)//checks cooldown for 2 right clicks in a row (20ms internal cd)
			{
				doDetangle(p,lvl);
				
				timeSinceLastQuantize.put(p, System.currentTimeMillis()); //set up cooldown for 2 right clicks in a row (20ms internal cd)
			}
			else p.sendMessage("Cannot initiate Quantize while within unpassable blocks.");
		}
			
			
			
			public void doEntangle(Player p, int lvl)
			{
				
				if (p.getEyeLocation().getBlock().isPassable() && p.getEyeLocation().add(0,-1,0).getBlock().isPassable()) // checks initial head and foot block of player to see if both are within a passable block.
				{
					
					Location l = p.getLocation().clone();
					Location initialLoc = p.getLocation();
					Vector v = l.getDirection().clone();
					v.multiply(5); //multiplies location vector by 5. is only used to set facing direction later
					l.add(v);	
				
					Location eyelocation = p.getEyeLocation();
					BlockIterator blocksToAddFoot = new BlockIterator(eyelocation, -1, 15 + lvl); //15 in here represents 15 blocks + lvl (lvl 5 is 20 block tp)
					Location blockToAdd = eyelocation;
					while(blocksToAddFoot.hasNext()&&blockToAdd.getBlock().isPassable()) 
					{
						blockToAdd = blocksToAddFoot.next().getLocation();
	                  
						if (blockToAdd.getBlock().isPassable() && blockToAdd.add(0,1,0).getBlock().isPassable()) //checks if foot block and eye block in path are both passable. if so, adds to last possible loc HashMap
						{
							p.spawnParticle(Particle.WAX_ON,blockToAdd.add(0, 1, 0),1);
							lastIteratedPassableLocation.put(p, blockToAdd.add(0,-1,0));//
						}
					}
	              
	            
					Location tpLoc = lastIteratedPassableLocation.get(p).setDirection(v);
					tpLoc.add(0.5, -0.5, 0.5);
					if(tpLoc.getBlock().isPassable())
					{
						initialLocation.put(p, initialLoc);
						p.teleport(tpLoc);
						isEntangled.put(p, true);			//sets status of player to entangled. next time axe is clicked, it will pick "doDetangle" because of this.
						timeOfEntanglement.put(p,System.currentTimeMillis());
						p.playSound(p, Sound.BLOCK_BEACON_ACTIVATE,(float)0.5,(float)1.5);
					}
					
					else p.sendMessage("Quantize Block Phase Cancelled");
	             
					
					p.setFallDistance(0); //sets fall dist 0 causing player to not take fall damage
				}
				
				// else do nothing if initial head and foot check fails
				
			}
			
			public void doDetangle(Player p, int lvl)
			{
				if (System.currentTimeMillis() <= timeOfEntanglement.get(p) + 2000 + lvl*1000)
				{
					
					p.teleport(initialLocation.get(p));
					p.setFallDistance(0); //sets fall dist 0 causing player to not take fall damage
					isEntangled.put(p, false);
					p.playSound(p, Sound.BLOCK_BEACON_DEACTIVATE,(float)0.5,(float)1.5);
					quantizeCooldown.put(p, System.currentTimeMillis()); //adds player cooldown to using ability. varies with level (occurs here because player SUCCESSFULLY used ability to recall to previous position)
					isQuantizeOnCooldown.put(p,true);
					p.sendMessage("Quantize went on cooldown for " + Integer.toString(20-lvl) + " seconds");
				}
				
			}
			
			@EventHandler
			private void detangleExpireCatcher(TickUpdateEvent t) 
			{ 
			
				for(Player p:timeOfEntanglement.keySet()) 
				{				
					if (isEntangled.get(p)) //checks to see if player is currently entangled (has not yet recalled)
					{
						user u = userManager.getUser(p.getUniqueId());
						if(u.getCurrentBuild().getAxe().getName() == skillName)
						{//if skill is Quantize
							if(System.currentTimeMillis()> timeOfEntanglement.get(p) + 2000 + skillLevel.get(p)*1000 && isQuantizeOnCooldown.get(p) == false)
							{
								isEntangled.put(p,false);
								quantizeCooldown.put(p, System.currentTimeMillis()); //adds player cooldown to using ability. varies with level (occurs here because player FAILED to use ability to recall to previous position)
								isQuantizeOnCooldown.put(p, true);
								p.sendMessage("You failed to use detangle");
								p.sendMessage("Quantize went on cooldown for " + Integer.toString(20-skillLevel.get(p)) + " seconds");
								p.playSound(p, Sound.BLOCK_BEACON_DEACTIVATE,(float)0.5,(float)0.5);
							}
						}
					}
				}
				
					
				
			}
			
			@EventHandler
			private void quantizeCooldown(TickUpdateEvent t) 
			{ 
				for(Player p:timeOfEntanglement.keySet()) 
				{								
					if (isQuantizeOnCooldown.get(p) && System.currentTimeMillis() >= (quantizeCooldown.get(p) + 20000 - skillLevel.get(p)*1000))
						{
								p.sendMessage("Quantize came off cooldown ");
								isQuantizeOnCooldown.put(p, false);
						}
				}
			}
	}
	
	
		
