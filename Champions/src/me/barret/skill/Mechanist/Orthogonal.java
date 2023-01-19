package me.barret.skill.Mechanist;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import me.barret.Champions;
import me.barret.build.BuildChangeEvent;
import me.barret.cooldown.cooldownManager;
import me.barret.events.TickUpdateEvent;
import me.barret.kits.Kit;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.user.user;
import me.barret.user.userManager;
//Author: ethvor - second skill started: 1-1-22 finished:1-1-22
public class Orthogonal extends Skill implements interactSkill
{ //inheritance


	private static Kit skillKit = kitManager.getKitFromString("Mechanist"); 		//sets class for skill to assassin

	private static SkillType skillType = SkillType.AXE; 						//sets skill type to axe skill

	static String skillName = "Orthogonal";

	static String[] description = {"Summon a wall of blocks ","at distance to stop your enemies!"};

	static int MaxLevel = 5;

	private static HashMap<Player, Location> lastIteratedPassableLocation = new HashMap<Player, Location>(); //used for iterated path tracing to prevent block phasing

	private static HashMap<Player, Boolean> isPreviewingWall = new HashMap<Player, Boolean>();

	private static HashMap<Player, Integer> skillLevel = new HashMap<Player,Integer>();

	private static HashMap<Player, Location> storedGlowLocation = new HashMap<Player, Location>();

	private static HashMap<Player,String> uuidStorage = new HashMap<Player, String>();

	private static HashMap<Player,Yaw> lastYaw = new HashMap<Player,Yaw>();

	private static HashMap<Player,Boolean> isPreviewFacingOrthogonal = new HashMap<Player,Boolean>(); //toggled by shift

	private static HashMap<Player, Boolean> wasLastSummonedPreviewOrthogonal = new HashMap<Player,Boolean>(); //is good for detecting change and not summoning more cubes than necessary

	private static HashMap<Player, Long> timeAtLastActivation = new HashMap<Player, Long>();


	
	private static HashMap<Player, List<Location>> storedLocations = new HashMap<Player, List<Location>>();
	

	private static HashMap<Location,Long> wallData = new HashMap<Location, Long>();

	public Orthogonal(Champions i)
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
				BlockIterator blocksToAddEye = new BlockIterator(eyelocation, 0, 10 + skillLevel.get(p)); //range ranges from 10 to 15 based on level
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

					//highlightLocation.add(0,1,0); //puts preview 1 block above ground level - may do this if we can figure out how to summon entities already possessing nbt - right now it collides and pushes player

					highlightLocation.add(0,-0.1,0);

					///summon minecraft:magma_cube ~ ~ ~ {NoAI:1,ActiveEffects:[{Id:24,Amplifier:1,Duration:1000000},{Id:14,Amplifier:1,Duration:100000}],NoGravity:1,Silent:1b,Size:1}
					//MagmaCube magmaCube = (MagmaCube) p.getWorld().spawnEntity(highlightLocation, EntityType.MAGMA_CUBE);
					//store raycast block in hash - if current raycast block != stored ray in hash set = also in this if is where I kill and summon one new mob for center

					//this nasty conditional essentially only does nothing if:
					//the player's raycasted target block doenst change (targeted = last)
					//AND the player's shift toggle for whether the wall is orthogonal or parallel to their faced cardinal direction has not changed (current = last)
					//AND the player's current cardinal direction has not changed from the last read cardinal direction
					//
					//if all three of those havent changed, it does nothing.
					//if any have changed, it updates the blocks by killing and summoning new blocks.
					if((Yaw.getYaw(p)==lastYaw.get(p))&&
					(wasLastSummonedPreviewOrthogonal.get(p)==isPreviewFacingOrthogonal.get(p))&&
					((highlightLocation.getBlock().getX() == storedGlowLocation.get(p).getBlock().getX())&&
					(highlightLocation.getBlock().getY() == storedGlowLocation.get(p).getBlock().getY())&&
					(highlightLocation.getBlock().getZ() == storedGlowLocation.get(p).getBlock().getZ())))
					{
						//do nothing
					}
					else //else update
					{

						storedGlowLocation.put(p, highlightLocation);
						wasLastSummonedPreviewOrthogonal.put(p,isPreviewFacingOrthogonal.get(p));

						for(org.bukkit.entity.Entity ent : p.getWorld().getEntities())
						{
							if(ent instanceof MagmaCube)
							{
								if (ent.getCustomName().equalsIgnoreCase(uuidStorage.get(p)))
								{
									ent.teleport(ent.getLocation().add(0,-500,0));
									ent.remove();
								}
							}
						}

						List<Location> cubeLocations= new ArrayList<Location>();
						lastYaw.put(p, Yaw.getYaw(p));

						for (int i = -2; i < 2; i++) {
						
							if (((Yaw.getYaw(p)==Yaw.WEST||Yaw.getYaw(p)==Yaw.EAST)&&isPreviewFacingOrthogonal.get(p)==true)
							|| ((Yaw.getYaw(p)==Yaw.NORTH||Yaw.getYaw(p)==Yaw.SOUTH)&&isPreviewFacingOrthogonal.get(p)==false))
							{ 
								//summons north-south wall preview
								MagmaCube mag = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().add(0,0,i), EntityType.MAGMA_CUBE,false);
								applyEffects(mag, p);
								cubeLocations.add(mag.getLocation());
								
							}
							else {//summons east-west wall preview
								MagmaCube mag = (MagmaCube) p.getWorld().spawnEntity(highlightLocation.clone().add(i,0,0), EntityType.MAGMA_CUBE,false);
								applyEffects(mag, p);
								cubeLocations.add(mag.getLocation());
							}
						}
						storedLocations.put(p, cubeLocations);
					}
				}
				
			}
		}
	}
	
	private void applyEffects(MagmaCube m, Player p)
	{
		m.setInvisible(true);
		m.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
		m.setSize(2);
		m.setGlowing(true);
		m.setGravity(false);
		m.setAI(false);
		m.setCustomName(p.getUniqueId().toString());
		m.setCustomNameVisible(false);
		m.setSilent(true);
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

	@EventHandler //toggles orthogonal and parallel viewing modes
	public void SneakOrthSwitch(PlayerToggleSneakEvent event)
	{
		Player p = event.getPlayer();
		if(isPreviewingWall.get(p))
		{
			if(p.isSneaking())
			{
				if (isPreviewFacingOrthogonal.get(p) == false)
				{
					p.sendMessage("swapped to orthogonal wall preview");
					isPreviewFacingOrthogonal.put(p, true);
				}
				else if (isPreviewFacingOrthogonal.get(p) == true)
				{
					p.sendMessage("swapped to parallel wall preview");
				isPreviewFacingOrthogonal.put(p,false);
				}
			}
		}
	}


	@EventHandler
	private void activateSkill(PlayerInteractEvent event) //if player LEFT clicks while previewing, sets isPreviewingWall to false (cancels preview) and activates skill
	{
		Player p = event.getPlayer();
		user u = userManager.getUser(p.getUniqueId());
		if (u.getCurrentBuild() == null) return;
		if (u.getCurrentBuild().getAxe() == null) return;
		if (u.getCurrentBuild().getAxe().getName() != this.getName()) return;
		
		int lvl = skillLevel.get(p);
		
		
		if (!((event.getAction() == Action.LEFT_CLICK_AIR) || (event.getAction() == Action.LEFT_CLICK_BLOCK))) return;
		if (isPreviewingWall.isEmpty()) return;

		if (isPreviewingWall.get(p)) {
			if ((cooldownManager.addCooldown(p, skillName + " " + lvl, 10- lvl, true)))
			{//if it is on cooldown, return {

				for (org.bukkit.entity.Entity ent : p.getWorld().getEntities())
				{
					if (ent instanceof MagmaCube)
					{
						if (ent.getCustomName().equalsIgnoreCase(uuidStorage.get(p)))
						{
							ent.teleport(ent.getLocation().add(0, -500, 0));
							ent.remove();
						}
					}
				}

					
				event.setCancelled(true); // should stop player from killing blocks or hitting people when activating the skill
				//p.sendMessage("Skill Activated");
				createWall(p);
				isPreviewingWall.put(p, false);
				timeAtLastActivation.put(p, System.currentTimeMillis());
				return;
			}
		}	
	}

	void createWall(Player p)
	{
		//p.sendMessage("attempting wall create");
		for (Location l : storedLocations.get(p)) {
			Location newLoc = checkColumn(p,l.clone());
			makeColumn(p,l,newLoc);
		}
	}

	void makeColumn(Player p, Location highlightLoc,Location lastGood)
	{
		//p.sendMessage("attempting column create");


		Location iterateLoc = highlightLoc;
		iterateLoc.add(0,2,0);
		while(iterateLoc.getY() <= lastGood.getY())
		{
			if (iterateLoc.getBlock().isEmpty())
			{
				long minimumTime = (3 + skillLevel.get(p)) * 1300; //minimum ice prison block time in ms

				long random = (long) (Math.random() * 500); //added random time to each block

				long expireTime = System.currentTimeMillis() + minimumTime + random;

				iterateLoc.getBlock().setType(Material.NETHERITE_BLOCK);
				wallData.put(iterateLoc.getBlock().getLocation(),expireTime);
				iterateLoc.setY(iterateLoc.getY()+1);
			}
			else return;

		}
	}

	//checks a column, returning last safe location to spawn a block
	Location checkColumn(Player p,Location loc)
	{
		Location checkLoc = loc.clone().add(0,2,0);
		int checkCount = 1;
		Location lastgood = loc;

		while (checkLoc.getBlock().isEmpty()&&checkCount<=3)
		{
			lastgood = checkLoc;
			//checkLoc.getBlock().setType(Material.GLASS); //useful debug to see where it checks
			checkLoc = checkLoc.clone().add(0,1,0);
			checkCount++;
		}
		//lastgood.getBlock().setType(Material.STONE); //useful debug to see last good block checked
		return lastgood;
	}



	//erases wall - taken from barret decay prison in ice prison.java
	@EventHandler
	private void decayWall(TickUpdateEvent e)
	{
			for (Location l : wallData.keySet())
			{
				if (wallData.get(l) <= System.currentTimeMillis())
				{
					if (l.getBlock().getType() == Material.NETHERITE_BLOCK)
					{
						l.getBlock().setType(Material.AIR);
					}
				}
			}
	}



	@EventHandler
	private void cancelPreview(PlayerInteractEvent event) //if player right clicks while previewing, sets isPreviewingWall to false (cancels preview)
	{

		Player p = event.getPlayer();
		if (isPreviewingWall.get(p) != null) {
			if (isPreviewingWall.get(p)) {
				if (System.currentTimeMillis() >= timeAtLastActivation.get(p) + 20) {
					if ((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)) {

						for (org.bukkit.entity.Entity ent : p.getWorld().getEntities()) {
							if (ent instanceof MagmaCube)
								if (ent.getCustomName().equalsIgnoreCase(uuidStorage.get(p))) {
									ent.teleport(ent.getLocation().add(0, -500, 0));
									ent.remove();
								}

						}
						isPreviewingWall.put(p, false);
						timeAtLastActivation.put(p, System.currentTimeMillis());
						p.sendMessage("preview cancelled");
					}
				}
			}
		}
	}

	@EventHandler		
	private void onBuildChange(BuildChangeEvent e) //needs new event
	{
		Player p = e.getPlayer(); //spigot
		if (e.getNewBuild() == null) return;
		if (e.getNewBuild().getAxe() == null) return;
		
		
		if(e.getNewBuild().getAxe().getName() == skillName)// checks if player build contains axe skill called skillname ("Orthogonal")
		{
			isPreviewingWall.put(p, false);
			storedGlowLocation.put(p,p.getLocation());
			uuidStorage.put(p, p.getUniqueId().toString());
			isPreviewFacingOrthogonal.put(p,true);
			timeAtLastActivation.put(p, System.currentTimeMillis());
		}

	}
}


