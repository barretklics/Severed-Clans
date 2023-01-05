package me.barret.skill.Mechanist;


import java.util.HashMap;

import javax.swing.text.html.parser.Entity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
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
//Author: ethvor - second skill started: 1-1-22 finished:1-1-22
public class Orthagonal extends Skill implements interactSkill
{ //inheritance
	
	
	private static Kit skillKit = kitManager.getKitFromString("Mechanist"); 		//sets class for skill to assassin
	
	private static SkillType skillType = SkillType.AXE; 						//sets skill type to axe skill
	
	static String skillName = "Orthagonal";
	
	static String[] description = {"Summon a wall of pistons ","at distance to stop your enemies!"};
	
	static int MaxLevel = 5;
	
	private static HashMap<Player, Location> lastIteratedPassableLocation = new HashMap<Player, Location>(); //used for iterated path tracing to prevent block phasing
	
	private static HashMap<Player, Boolean> isPreviewingWall = new HashMap<Player, Boolean>();
	
	private static HashMap<Player, Integer> skillLevel = new HashMap<Player,Integer>();
	
	private static HashMap<Player, Location> storedGlowLocation = new HashMap<Player, Location>();
	
	private static HashMap<Player,String> uuidStorage = new HashMap<Player, String>();
	
	private static HashMap<Player,Yaw> lastYaw = new HashMap<Player,Yaw>();
	
	private static HashMap<Player,Boolean> isWallFacingOrthagonal = new HashMap<Player,Boolean>(); //toggled by shift
	
	private static HashMap<Player, Boolean> wasLastSummonedWallOrthagonal = new HashMap<Player,Boolean>(); //is good for detecting change and not summoning more cubes than necessary
	
	private static HashMap<Player, Long> timeAtLastActivation = new HashMap<Player, Long>();
	

	public Orthagonal(Champions i)
	{
			super(i, skillKit, skillName, skillType, description, MaxLevel);
			
	}
	
	public enum Yaw {	//code for Yaw method taken from comment by Adrian Sohn - https://stackoverflow.com/questions/35831619/get-the-direction-a-player-is-looking
	    NORTH, SOUTH, EAST, WEST;
		public static Yaw getYaw(Player p) {
		    float yaw = p.getLocation().getYaw();
		    if (yaw < 0) {
		        yaw += 360;
		    }
		    if (yaw >= 315 || yaw < 45) {
		        return SOUTH;
		    } else if (yaw < 135) {
		        return WEST;
		    } else if (yaw < 225) {
		        return NORTH;
		    } else if (yaw < 315) {
		        return EAST;
		    }
		    return NORTH;
		}
	}
		
	public void activate(Player p, user u, int lvl)  //activate occurs on right click - lvl is ability level selected in ench table
	{
		if (isPreviewingWall.get(p)==false)
		{
		if (System.currentTimeMillis() >= timeAtLastActivation.get(p) + 20)
		{

		isPreviewingWall.put(p,true);

		skillLevel.put(p, lvl);
		timeAtLastActivation.put(p, System.currentTimeMillis());
		

		}
	}
	}
	
	
	
	
	//summon magma_cube ~ ~ ~ {Silent:1b,Invulnerable:1b,Glowing:1b,NoAI:1b,Size:1,CustomName:"{\"text\":\"Marker\"}",ActiveEffects:[{Id:14b,Amplifier:1b,Duration:30000,ShowParticles:0b}]}
	

	
		
	
	@EventHandler
	private void glowingPreview(TickUpdateEvent e)
	{
		
		for(Player p:isPreviewingWall.keySet())
		{
	
			if (isPreviewingWall.get(p) == true)
			{
						Location l = p.getLocation().clone();
						//Location initialLoc = p.getLocation();
						Vector v = l.getDirection().clone();
						Vector orth = v.getCrossProduct(v);
					
						Location eyelocation = p.getEyeLocation();
						BlockIterator blocksToAddEye = new BlockIterator(eyelocation, 0, 10); //10 in here represents 10 blocks
						Location blockToAdd = eyelocation;
						while(blocksToAddEye.hasNext()&&blockToAdd.getBlock().isPassable()) 
						{
							blockToAdd = blocksToAddEye.next().getLocation();
			              
							if (blockToAdd.getBlock().isPassable() && blockToAdd.add(0,1,0).getBlock().isPassable()) //checks if foot block and eye block in path are both passable. if so, adds to last possible loc HashMap
							{
	
								lastIteratedPassableLocation.put(p, blockToAdd.add(0,-1,0));//
							}
						}
						
						
						
						Location tempHighlightLocation = lastIteratedPassableLocation.get(p);
						
						
						
						tempHighlightLocation.setDirection(orth); //make conditional for pressing shift to change direction of wall
						
						tempHighlightLocation.add(0.5,1,0.5);		//may need tweaking when i summon entities
						Location highlightLocation = tempHighlightLocation;

						while (tempHighlightLocation.getBlock().isPassable()) //iterates downwards until it hits a solid block (this only activates when player is looking off the ground)
						{
							tempHighlightLocation.add(0,-1,0);
						}
							highlightLocation = tempHighlightLocation;
						
						
							if(!highlightLocation.getBlock().isPassable()); //if its not passable (redundant check i think)
							{
								
								
								//highlightLocation.add(0,1,0); //puts preview 1 block above ground level - may do this if we can figure out how to summon entities already possessing nbt
								
								highlightLocation.add(0,-0.1,0);
								
								///summon minecraft:magma_cube ~ ~ ~ {NoAI:1,ActiveEffects:[{Id:24,Amplifier:1,Duration:1000000},{Id:14,Amplifier:1,Duration:100000}],NoGravity:1,Silent:1b,Size:1}
								//MagmaCube magmaCube = (MagmaCube) p.getWorld().spawnEntity(highlightLocation, EntityType.MAGMA_CUBE);
								//store raycast block in hash - if current raycast block != stored ray in hash set = also in this if is where I kill and summon one new mob for center
								
								//this nasty conditional essentially only does nothing if:
								//the player's raycasted target block doenst change (targeted = last)
								//AND the player's shift toggle for whether the wall is orthagonal or parallel to their faced cardinal direction has not changed (current = last)
								//AND the player's current cardinal direction has not changed from the last read cardinal direction
								//
								//if all three of those havent changed, it does nothing.
								//if any have changed, it updates the blocks by killing and summoning new blocks.
								if((Yaw.getYaw(p)==lastYaw.get(p))&&
								(wasLastSummonedWallOrthagonal.get(p)==isWallFacingOrthagonal.get(p))&&
								((highlightLocation.getBlock().getX() == storedGlowLocation.get(p).getBlock().getX())&&
								(highlightLocation.getBlock().getY() == storedGlowLocation.get(p).getBlock().getY())&&
								(highlightLocation.getBlock().getZ() == storedGlowLocation.get(p).getBlock().getZ()))) 
								{
									//do nothing
								}
								else //else update
								{
									
									storedGlowLocation.put(p, highlightLocation);
									wasLastSummonedWallOrthagonal.put(p,isWallFacingOrthagonal.get(p));
									
									for(org.bukkit.entity.Entity ent : p.getWorld().getEntities()) {
										  if(ent instanceof MagmaCube)
											  if (ent.getCustomName().equalsIgnoreCase(uuidStorage.get(p)))
											  {
												ent.teleport(ent.getLocation().add(0,-500,0));
												ent.remove();
											  }
										      
										}
									
									//summon cube here
									

									//center
								
									MagmaCube magmaCube = (MagmaCube) p.getWorld().spawnEntity(highlightLocation, EntityType.MAGMA_CUBE,false);
									
									magmaCube.setSize(2);
									magmaCube.setInvisible(true);
									magmaCube.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
									magmaCube.setGlowing(true);
									magmaCube.setGravity(false);
									magmaCube.setAI(false);
									magmaCube.setCustomName(p.getUniqueId().toString());
									magmaCube.setCustomNameVisible(false);
									magmaCube.setSilent(true);

									//end center
									
									
									//need conditional here based on changing of shift orth toggle and last yaw get.
									
									//start flank cubes preview
									
									//p.sendMessage("right above flank conditionals");
									//p.sendMessage(""+Yaw.getYaw(p));
									//p.sendMessage(""+isWallFacingOrthagonal.get(p));

									
									//north: negative z --- south: positive z --- east: positive x --- west: negative x
									
									if ((Yaw.getYaw(p)==Yaw.WEST||Yaw.getYaw(p)==Yaw.EAST)&&isWallFacingOrthagonal.get(p)==true) //summons north-south wall preview
									{
									//	p.sendMessage("inside case 1");
										
										lastYaw.put(p, Yaw.getYaw(p));
										
										MagmaCube innerPoscase1 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().add(0,0,1), EntityType.MAGMA_CUBE,false);//south inner
										innerPoscase1.setInvisible(true);
										innerPoscase1.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										innerPoscase1.setSize(2);
										innerPoscase1.setGlowing(true);
										innerPoscase1.setGravity(false);
										innerPoscase1.setAI(false);
										innerPoscase1.setCustomName(p.getUniqueId().toString());
										innerPoscase1.setCustomNameVisible(false);
										innerPoscase1.setSilent(true);
										
										MagmaCube innerNegcase1 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().subtract(0,0,1), EntityType.MAGMA_CUBE,false);//north inner
										innerNegcase1.setInvisible(true);
										innerNegcase1.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										innerNegcase1.setSize(2);
										innerNegcase1.setGlowing(true);
										innerNegcase1.setGravity(false);
										innerNegcase1.setAI(false);
										innerNegcase1.setCustomName(p.getUniqueId().toString());
										innerNegcase1.setCustomNameVisible(false);
										innerNegcase1.setSilent(true);
										
										MagmaCube outerPoscase1 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().add(0,0,2), EntityType.MAGMA_CUBE,false);//south outer
										outerPoscase1.setInvisible(true);
										outerPoscase1.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										outerPoscase1.setSize(2);
										outerPoscase1.setGlowing(true);
										outerPoscase1.setGravity(false);
										outerPoscase1.setAI(false);
										outerPoscase1.setCustomName(p.getUniqueId().toString());
										outerPoscase1.setCustomNameVisible(false);
										outerPoscase1.setSilent(true);
										
										MagmaCube outerNegcase1 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().subtract(0,0,2), EntityType.MAGMA_CUBE,false);//north outer
										outerNegcase1.setInvisible(true);
										outerNegcase1.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										outerNegcase1.setSize(2);
										outerNegcase1.setGlowing(true);
										outerNegcase1.setGravity(false);
										outerNegcase1.setAI(false);
										outerNegcase1.setCustomName(p.getUniqueId().toString());
										outerNegcase1.setCustomNameVisible(false);
										outerNegcase1.setSilent(true);

									}
									
									if ((Yaw.getYaw(p)==Yaw.WEST||Yaw.getYaw(p)==Yaw.EAST)&&isWallFacingOrthagonal.get(p)==false) //summons east-west wall preview
									{
										//p.sendMessage("inside case 2");
										
										lastYaw.put(p, Yaw.getYaw(p));
										
										MagmaCube innerPoscase2 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().add(1,0,0), EntityType.MAGMA_CUBE,false);//west inner
										innerPoscase2.setInvisible(true);
										innerPoscase2.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										innerPoscase2.setSize(2);
										innerPoscase2.setGlowing(true);
										innerPoscase2.setGravity(false);
										innerPoscase2.setAI(false);
										innerPoscase2.setCustomName(p.getUniqueId().toString());
										innerPoscase2.setCustomNameVisible(false);
										innerPoscase2.setSilent(true);
										
										MagmaCube innerNegcase2 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().subtract(1,0,0), EntityType.MAGMA_CUBE,false);//east inner
										innerNegcase2.setInvisible(true);
										innerNegcase2.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										innerNegcase2.setSize(2);
										innerNegcase2.setGlowing(true);
										innerNegcase2.setGravity(false);
										innerNegcase2.setAI(false);
										innerNegcase2.setCustomName(p.getUniqueId().toString());
										innerNegcase2.setCustomNameVisible(false);
										innerNegcase2.setSilent(true);
										
										MagmaCube outerPoscase2 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().add(2,0,0), EntityType.MAGMA_CUBE,false);//west outer
										outerPoscase2.setInvisible(true);
										outerPoscase2.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										outerPoscase2.setSize(2);
										outerPoscase2.setGlowing(true);
										outerPoscase2.setGravity(false);
										outerPoscase2.setAI(false);
										outerPoscase2.setCustomName(p.getUniqueId().toString());
										outerPoscase2.setCustomNameVisible(false);
										outerPoscase2.setSilent(true);
										
										MagmaCube outerNegcase2 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().subtract(2,0,0), EntityType.MAGMA_CUBE,false);//east outer
										outerNegcase2.setInvisible(true);
										outerNegcase2.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										outerNegcase2.setSize(2);
										outerNegcase2.setGlowing(true);
										outerNegcase2.setGravity(false);
										outerNegcase2.setAI(false);
										outerNegcase2.setCustomName(p.getUniqueId().toString());
										outerNegcase2.setCustomNameVisible(false);
										outerNegcase2.setSilent(true);
									}
									
									if ((Yaw.getYaw(p)==Yaw.NORTH||Yaw.getYaw(p)==Yaw.SOUTH)&&isWallFacingOrthagonal.get(p)==true) //summons east-west wall preview
									{
										//p.sendMessage("inside case 3");
										
										lastYaw.put(p, Yaw.getYaw(p));
										
										MagmaCube innerPoscase2 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().add(1,0,0), EntityType.MAGMA_CUBE,false);//west inner
										innerPoscase2.setInvisible(true);
										innerPoscase2.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										innerPoscase2.setSize(2);
										innerPoscase2.setGlowing(true);
										innerPoscase2.setGravity(false);
										innerPoscase2.setAI(false);
										innerPoscase2.setCustomName(p.getUniqueId().toString());
										innerPoscase2.setCustomNameVisible(false);
										innerPoscase2.setSilent(true);
										
										MagmaCube innerNegcase2 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().subtract(1,0,0), EntityType.MAGMA_CUBE,false);//east inner
										innerNegcase2.setInvisible(true);
										innerNegcase2.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										innerNegcase2.setSize(2);
										innerNegcase2.setGlowing(true);
										innerNegcase2.setGravity(false);
										innerNegcase2.setAI(false);
										innerNegcase2.setCustomName(p.getUniqueId().toString());
										innerNegcase2.setCustomNameVisible(false);
										innerNegcase2.setSilent(true);
										
										MagmaCube outerPoscase2 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().add(2,0,0), EntityType.MAGMA_CUBE,false);//west outer
										outerPoscase2.setInvisible(true);
										outerPoscase2.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										outerPoscase2.setSize(2);
										outerPoscase2.setGlowing(true);
										outerPoscase2.setGravity(false);
										outerPoscase2.setAI(false);
										outerPoscase2.setCustomName(p.getUniqueId().toString());
										outerPoscase2.setCustomNameVisible(false);
										outerPoscase2.setSilent(true);
										
										MagmaCube outerNegcase2 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().subtract(2,0,0), EntityType.MAGMA_CUBE,false);//east outer
										outerNegcase2.setInvisible(true);
										outerNegcase2.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										outerNegcase2.setSize(2);
										outerNegcase2.setGlowing(true);
										outerNegcase2.setGravity(false);
										outerNegcase2.setAI(false);
										outerNegcase2.setCustomName(p.getUniqueId().toString());
										outerNegcase2.setCustomNameVisible(false);
										outerNegcase2.setSilent(true);
									}
									
									if ((Yaw.getYaw(p)==Yaw.NORTH||Yaw.getYaw(p)==Yaw.SOUTH)&&isWallFacingOrthagonal.get(p)==false) //summons north-south wall preview
									{
										//p.sendMessage("inside case 4");
										
										lastYaw.put(p, Yaw.getYaw(p));
										
										MagmaCube innerPoscase1 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().add(0,0,1), EntityType.MAGMA_CUBE,false);//south inner
										innerPoscase1.setInvisible(true);
										innerPoscase1.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										innerPoscase1.setSize(2);
										innerPoscase1.setGlowing(true);
										innerPoscase1.setGravity(false);
										innerPoscase1.setAI(false);
										innerPoscase1.setCustomName(p.getUniqueId().toString());
										innerPoscase1.setCustomNameVisible(false);
										innerPoscase1.setSilent(true);
										
										MagmaCube innerNegcase1 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().subtract(0,0,1), EntityType.MAGMA_CUBE,false);//north inner
										innerNegcase1.setInvisible(true);
										innerNegcase1.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										innerNegcase1.setSize(2);
										innerNegcase1.setGlowing(true);
										innerNegcase1.setGravity(false);
										innerNegcase1.setAI(false);
										innerNegcase1.setCustomName(p.getUniqueId().toString());
										innerNegcase1.setCustomNameVisible(false);
										innerNegcase1.setSilent(true);
										
										MagmaCube outerPoscase1 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().add(0,0,2), EntityType.MAGMA_CUBE,false);//south outer
										outerPoscase1.setInvisible(true);
										outerPoscase1.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										outerPoscase1.setSize(2);
										outerPoscase1.setGlowing(true);
										outerPoscase1.setGravity(false);
										outerPoscase1.setAI(false);
										outerPoscase1.setCustomName(p.getUniqueId().toString());
										outerPoscase1.setCustomNameVisible(false);
										outerPoscase1.setSilent(true);
										
										MagmaCube outerNegcase1 = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().subtract(0,0,2), EntityType.MAGMA_CUBE,false);//north outer
										outerNegcase1.setInvisible(true);
										outerNegcase1.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
										outerNegcase1.setSize(2);
										outerNegcase1.setGlowing(true);
										outerNegcase1.setGravity(false);
										outerNegcase1.setAI(false);
										outerNegcase1.setCustomName(p.getUniqueId().toString());
										outerNegcase1.setCustomNameVisible(false);
										outerNegcase1.setSilent(true);

									}
									
									//end flank cube preview
									
								
									
								
								} 

						}
		}
	}
			
}
				
				
@EventHandler
private void cubeInvulnerability(EntityDamageByEntityEvent event)
{
	for (Player p:uuidStorage.keySet())
	{
	if(event.getEntity().getCustomName().equalsIgnoreCase(uuidStorage.get(p)))
	{
		if(event.getEntityType() == EntityType.MAGMA_CUBE)
		event.setCancelled(true);
	}
	}
}

@EventHandler
public void SneakOrthSwitch(PlayerToggleSneakEvent event) 
{
Player p = event.getPlayer();
if(isPreviewingWall.get(p))
{
	if(p.isSneaking()) 
	{
		if (isWallFacingOrthagonal.get(p) == false)
		{
			p.sendMessage("swapped to orthogonal wall preview");
			isWallFacingOrthagonal.put(p, true);
		}
		else if (isWallFacingOrthagonal.get(p) == true)
		{
			p.sendMessage("swapped to parallel wall preview");
		isWallFacingOrthagonal.put(p,false);
		}
		
	}
}

}





	
	@EventHandler
	private void cancelPreview(PlayerInteractEvent event) //if player right clicks while previewing, sets isPreviewingWall to false (cancels preview)
	{
		
		Player p = event.getPlayer();
			
			if(isPreviewingWall.get(p))
			{
				if (System.currentTimeMillis() >= timeAtLastActivation.get(p) + 20)
				{
					if((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK))
					{

						for(org.bukkit.entity.Entity ent : p.getWorld().getEntities()) {
							  if(ent instanceof MagmaCube)
								  if (ent.getCustomName().equalsIgnoreCase(uuidStorage.get(p)))
								  {
									ent.teleport(ent.getLocation().add(0,-500,0));
									ent.remove();
								  }
							      
							}
					isPreviewingWall.put(p, false);
					timeAtLastActivation.put(p, System.currentTimeMillis());
				}
			}
			}
		}
	

	
	//need a few cases: when player changes build ACTIVATES THIS THEORETICAL EVENT. when player logs into this build ACTIVATES THIS THEORETICAL EVENT when player changes from knight to assassin and this build is selected ACTIVATES THIS THEORETICAL EVENT @barret
	//DOESNT WORK. ONLY DETECTS WHEN GOING FROM ASSASSIN TO NAKED and V.V.
			@EventHandler // WILL BE CALLED every time kit is changed. 
			//this is going to listen to spigot api's event handler which barret used to make a custom event buildchange
			private void onBuildChange(kitChangeEvent e) //needs new event
			{ 
				Player p = e.getPlayer(); //spigot
				user u = userManager.getUser(p.getUniqueId()); //barret user type calls spigot api to get players uuid
				if(u.getCurrentSkills().getAxe().getName() == skillName)// checks if player build contains axe skill called skillname ("Orthogonal")
				{ 
					isPreviewingWall.put(p, false);
					storedGlowLocation.put(p,p.getLocation());
					uuidStorage.put(p, p.getUniqueId().toString());
					isWallFacingOrthagonal.put(p,true);
					timeAtLastActivation.put(p, System.currentTimeMillis());
				}		
			}

}

