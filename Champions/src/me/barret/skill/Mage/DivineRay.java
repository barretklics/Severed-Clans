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
		if (p.getEyeLocation().getBlock().isPassable() && p.getEyeLocation().add(0, -1, 0).getBlock().isPassable()) // checks initial head and foot block of player to see if both are within a passable block.
		{




			Location initialLoc = p.getLocation();
			Location eyelocation = p.getEyeLocation();
			World world = p.getWorld();

			Vector initialVec = p.getEyeLocation().getDirection();
			Vector iterateVec = p.getEyeLocation().getDirection(); // COOL IDEA: whenever it hits a block, make it make that block glow for a moment for all players (cube zone). maybe make the beam FAST but not instant? ;)

			Location iterateLocation = p.getEyeLocation();



			double totalDistance = 0;
			int bounces = 0;
			while(totalDistance <=512 && bounces < 2) //instead of 512: make upper bound at lvl 5, make a function based on lvl and charge percentage out of 100% (5s held)
			{



				if (iterateLocation.getBlock().isPassable())
				{
					p.sendMessage("iterating");


					iterateVec = initialVec.clone(); //uncomment if doesnt work

					iterateLocation.add(iterateVec.multiply(0.1)); //distance traveled per iteration

					Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(0, 255, 0), Color.fromRGB(255, 255, 255), 1.0F); //green fade to white
					p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0,0,0,0,dustTransition,true);
					p.sendMessage("spawned particles in iterate");

				}
				else
				{
					p.sendMessage("bounce"); //bounce doesnt work rn

					//reflectedVec = originalVec - 2 * ( originalVec . normalVec ) * normalVec
					//normal vec is the vector orthogonal to the plane the originalVec is reflecting off of, and . is the dot product

					Vector originalVec = iterateVec.clone();
					BlockFace surface = iterateLocation.getBlock().getFace(iterateLocation.getBlock());
					Vector normalVec = surface.getOppositeFace().getDirection();
					if(originalVec.dot(normalVec)>0) {
						normalVec=normalVec.multiply(-1);
					}

					Vector reflectedVec = originalVec.subtract(normalVec.multiply(2*(originalVec.dot(normalVec)))); // reflection

					//Vector unitVector = reflectedVec.normalize();
					//Vector zeroLengthVector = unitVector.multiply(0.1);
					//initialVec = zeroLengthVector;
					//initialVec = reflectedVec;
					iterateVec = reflectedVec;
					iterateLocation.add(iterateVec.multiply(0.1));
					if (!iterateLocation.add(iterateVec.multiply(0.1)).getBlock().isPassable())
					{
						iterateLocation.subtract(iterateVec.multiply(0.1));
					}
					Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(255, 0, 0), Color.fromRGB(0, 0, 0), 1.0F); //red fade to black?
					p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0,0,0,0,dustTransition,true);
					p.sendMessage("spawned particles in bounce");
					bounces ++;
					justBounced.put(p,true);



				}
			}


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



