package me.barret.skill.Mage;

import me.barret.skill.Skill;
import me.barret.skill.channelSkill;

import java.util.HashMap;

import org.bukkit.*;
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
import org.bukkit.util.BlockIterator;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

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

	private static HashMap<Player, Location> playerLoc = new HashMap<Player, Location>();

	private static HashMap<Player, Integer> skillLevel = new HashMap<Player, Integer>();

	private static HashMap<Player, Boolean> justBounced = new HashMap<Player, Boolean>();
    public DivineRay(Champions i)
    {
		super(i, skillKit, skillName, skillType, description, MaxLevel);
	}

    @Override
	public void activate(Player p, user u, int level)
	{
    //	isChanneling.put(p, true);
    	timeActivated.put(p, System.currentTimeMillis());
    	//p.sendMessage("You started channeling Divine Ray");
		Ray(p,u,level);
    	skillLevel.put(p,level);
	}


	public void Ray(Player p, user u, int lvl) {
		Location initialLoc = p.getEyeLocation();
		Vector initialVec = p.getEyeLocation().getDirection();
		Vector iterateVec = initialVec.clone();
		Location iterateLocation = initialLoc.clone();
		int maxDistance = 64;
		int maxBounces = 2;
		int maxHeight = p.getWorld().getMaxHeight() + 50;

		double totalDistance = 0;
		double iterateDistance = 0.1;
		int bounces = 0;
		while(totalDistance <=maxDistance && bounces < maxBounces) {


			if (iterateLocation.getY() > maxHeight) {
				break;
			}

			if (!iterateLocation.add(iterateVec).getBlock().isPassable()) {
				Vector originalVec = iterateVec.clone();
				BlockFace surface = iterateLocation.getBlock().getFace(iterateLocation.getBlock().getRelative(BlockFace.DOWN));
				Vector normalVec = surface.getOppositeFace().getDirection();
				if(originalVec.dot(normalVec)>0) {
					normalVec=normalVec.multiply(-1);
				}
				Vector reflectedVec = originalVec.subtract(normalVec.multiply(2*(originalVec.dot(normalVec))));
				iterateVec = reflectedVec;
				bounces++;
				Particle.DustTransition dustTransition = new Particle.DustTransition(Color.RED, Color.BLACK, 1.0F);
				p.getWorld().spawnParticle(Particle.REDSTONE, iterateLocation, 50, 0,0,0,0,dustTransition,true);

			} else {
				iterateLocation.add(iterateVec.normalize().multiply(iterateDistance));
				Particle.DustTransition dustTransition = new Particle.DustTransition(Color.GREEN, Color.WHITE, 1.0F);
				p.getWorld().spawnParticle(Particle.REDSTONE, iterateLocation, 50, 0,0,0,0,dustTransition,true);
			}
			totalDistance += iterateDistance;
		}
	}


	public double dot(double x1, double y1, double z1, double x2, double y2, double z2) {
		return x1 * x2 + y1 * y2 + z1 * z2;
			}


			/*
			BlockIterator blocksToAddFoot = new BlockIterator(eyelocation, -1, 512); //15 in here represents 15 blocks + lvl (lvl 5 is 20 block tp)
			Location blockToAdd = eyelocation;

			while (blocksToAddFoot.hasNext() && blockToAdd.getBlock().isPassable()) {
				blockToAdd = blocksToAddFoot.next().getLocation();

				if (blockToAdd.getBlock().isPassable() && blockToAdd.add(0, 1, 0).getBlock().isPassable())
				{
					p.spawnParticle(Particle.FLAME, blockToAdd.add(0, 1, 0), 0,l.getDirection().getX() ,l.getDirection().getY(),l.getDirection().getZ(), 10);
					//spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data)
*/

}



