package me.barret.skill.Mage;

import me.barret.build.BuildChangeEvent;
import me.barret.skill.Skill;
import me.barret.skill.channelSkill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
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
import me.barret.skill.Skill.SkillType;
import me.barret.user.user;
import me.barret.user.userManager;
import me.barret.utils.UtilItem;
import me.barret.utils.UtilMessage;
import org.bukkit.util.*;

public class DivineRay extends Skill implements interactSkill
{
private static Kit skillKit = kitManager.getKitFromString("Mage");

	private static SkillType skillType = SkillType.SWORD;

	static String skillName = "Divine Ray";

	static String[] description = {"Unleash a beam of glorious light to pierce your enemies"};

	static int MaxLevel = 4;

//	private static HashMap<Location, Long> prisonData = new HashMap<Location, Long>();

	private static HashMap<Player,Boolean> isChanneling = new HashMap<Player,Boolean>();

	private static HashMap<Player,Long> timeActivated = new HashMap<Player, Long>();

	private static HashMap<Player,Long> internalCD = new HashMap<Player, Long>();

	private static HashMap<Player, Location> playerLoc = new HashMap<Player, Location>();

	private static HashMap<Player, Integer> skillLevel = new HashMap<Player, Integer>();


    public DivineRay(Champions i)
    {
		super(i, skillKit, skillName, skillType, description, MaxLevel);
	}

    @Override
	public void activate(Player p, user u, int level)
	{
		if (internalCD.get(p) + 20 <=System.currentTimeMillis()) {
			internalCD.put(p, System.currentTimeMillis());
			timeActivated.put(p, System.currentTimeMillis());
			CreateRay(p, u, level);
			skillLevel.put(p, level);
		}
	}


	public void CreateRay(Player p, user u, int lvl)
	{
		Vector iterateVector = p.getEyeLocation().getDirection(); //only for initial ray before bounce
		Location iterateLocation = p.getEyeLocation(); //only for initial ray before bounce
		World world = p.getWorld();
		double iterateDistance = 0.1;

		Vector lastSafeVector = p.getEyeLocation().getDirection();

		Vector unitAdditionVector = iterateVector.clone().normalize().multiply(iterateDistance); //normalize makes it magnitude of 1 in same direction.

		while(iterateLocation.getBlock().isPassable())
		{
			lastSafeVector = iterateVector.clone();
			iterateVector = iterateVector.add(unitAdditionVector);
			iterateLocation = iterateLocation.add(unitAdditionVector);

		}
		p.sendMessage("trying to get location of hit");
		p.sendMessage("blockface hit: "+ getHitBlockFace(p,lastSafeVector,iterateVector, iterateLocation, world));
		iterateLocation.getBlock().setType(Material.DIRT);
		Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(0, 255, 0), Color.fromRGB(255, 255, 255), 1.0F); //green fade to white
		p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0,0,0,0,dustTransition,true);
		p.sendMessage("Iterate Loc: "+iterateLocation);


	}

	public Vector getReflectedVector(Vector originalVec, Vector reflectedVec)
	{
		return null;
	}


	public BlockFace getHitBlockFace(Player p,Vector lastSafeVec, Vector iterVec,Location iterLoc, World world)
	{
		 //try going back to using "location" of center of faces. make SURE they are accurate. spawn particles to be sure.
																											//since iterateLoc particle spawns in right place, iterateLoc is a good reference for which block face it is. find least dist from iterateLoc to all blockFaces.
																											//least distance to blockface == closest blockface. return that blockface and use as face for normal vector calculation
		Location blockLocation = iterLoc.getBlock().getLocation();

		double centerX = blockLocation.getX() + 0.5;
		double centerY = blockLocation.getY() + 0.5;
		double centerZ = blockLocation.getZ() + 0.5;

		Location centerLocation = new Location(blockLocation.getWorld(), centerX, centerY, centerZ);

		Location topFace = centerLocation.clone();
		topFace.setY(centerLocation.getBlockY() + 0.5);

		Location bottomFace = centerLocation.clone();
		bottomFace.setY(centerLocation.getBlockY() - 0.5);

		Location northFace = centerLocation.clone();
		northFace.setZ(centerLocation.getBlockZ() - 0.5);

		Location eastFace = centerLocation.clone();
		eastFace.setX(centerLocation.getBlockX() + 0.5);

		Location southFace = centerLocation.clone();
		southFace.setZ(centerLocation.getBlockZ() + 0.5);

		Location westFace = centerLocation.clone();
		westFace.setX(centerLocation.getBlockX() - 0.5);

		BlockFace closestFace = null;
		double closestDistance = Double.MAX_VALUE;

		Location[] faceLocations = new Location[] {topFace, bottomFace, northFace, eastFace, southFace, westFace};
		String[] faceNames = new String[] {"TOP", "BOTTOM", "NORTH", "EAST", "SOUTH", "WEST"};

		for (int i = 0; i < faceLocations.length; i++) {
			double distance = iterLoc.distance(faceLocations[i]);
			if (distance < closestDistance) {
				closestDistance = distance;
				closestFace = BlockFace.valueOf(faceNames[i]);
			}
		}

		return closestFace;

	}



	@EventHandler
	public void OnBuildChange(BuildChangeEvent e)
	{
		Player p = userManager.getUser(e.getPlayerUUID()).toPlayer();
		internalCD.put(p,(long)0);

	}

}



