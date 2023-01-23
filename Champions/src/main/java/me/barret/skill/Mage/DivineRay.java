package me.barret.skill.Mage;

import me.barret.Champions;
import me.barret.build.BuildChangeEvent;
import me.barret.kits.Kit;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.user.user;
import me.barret.user.userManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class DivineRay extends Skill implements interactSkill
{
	private static Kit skillKit = kitManager.getKitFromString("Mage");

	private static SkillType skillType = SkillType.SWORD;

	static String skillName = "Divine Ray";

	static String[] description = {"Unleash a beam of glorious light to pierce your enemies"};

	static int MaxLevel = 4;

	private static HashMap<Player,Long> timeActivated = new HashMap<Player, Long>();

	private static HashMap<Player,Long> internalCD = new HashMap<Player, Long>();

	private static HashMap<Player, Integer> skillLevel = new HashMap<Player, Integer>();

	private static HashMap<Player,Long> lastIterateTime = new HashMap<Player, Long>();


	public DivineRay(Champions i)
	{
		super(i, skillKit, skillName, skillType, description, MaxLevel);
	}

	@Override
	public void activate(Player p, user u, int level) {
		if (internalCD.get(p) + 20 <=System.currentTimeMillis()) {
			internalCD.put(p, System.currentTimeMillis());
			timeActivated.put(p, System.currentTimeMillis());
			CreateRay(p, u, level);
			skillLevel.put(p, level);
		}
	}


	public void CreateRay(Player p, user u, int lvl){
		Vector iterateVector = p.getEyeLocation().getDirection(); //only for initial ray before bounce
		Location iterateLocation = p.getEyeLocation(); //only for initial ray before bounce
		World world = p.getWorld();

		//modifiable values

		double iterateDistance = 0.2; // BASICALLY DO NOT MODIFY, MIGHT MAKE ENTIRE RAYCAST NOT WORK. WORKING VALUES: 0.1 - the most tested, 0.2 works i think. 0.3 is INCONSISTENT.
		int maxBounces = 3 * lvl - lvl; //modify for balance sake, make depend on lvl of player skill
		double maxDistance = 128; //modify for balance sake, make depend on lvl of player skill
		long iteratorCooldown = 20; //delay in ms between iterations

		//end modifiable values

		Vector lastSafeVector = p.getEyeLocation().getDirection();
		Location lastSafeLocation = p.getEyeLocation();
		Vector unitAdditionVector = iterateVector.clone().normalize().multiply(iterateDistance); //normalize makes it magnitude of 1 in same direction.
		int bounces = 0;
		double distanceTraveled = 0;
		lastIterateTime.put(p,(long)0);

		while (bounces <= maxBounces) {


			while (iterateLocation.getBlock().isPassable()&& distanceTraveled <= maxDistance && iterateLocation.getY() <= world.getMaxHeight()+50) {
				if (System.currentTimeMillis() >= lastIterateTime.get(p)+iteratorCooldown) {

					lastSafeLocation = iterateLocation.clone();
					iterateVector = iterateVector.add(unitAdditionVector);
					iterateLocation = iterateLocation.add(unitAdditionVector);
					Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(0, 255, 0), Color.fromRGB(255, 255, 255), 1.0F); //green fade to white
					p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0, 0, 0, 0, dustTransition, true);
					distanceTraveled += iterateDistance; //adds iterateDistance to distance traveled, so it does not go on forever
					lastIterateTime.put(p,System.currentTimeMillis());
				}
			}
			if (distanceTraveled >= maxDistance || iterateLocation.getY() >= world.getMaxHeight()+50)
			{
				p.sendMessage("The divine ray traveled "+distanceTraveled+" blocks along "+bounces+" bounces.");
				break;
			}

		/*													GOOD DEBUG MESSAGES IF REFLECTING WRONG OR ANYTHING ELSE
		if(lastSafeLocation.getBlock().isPassable())
		{
			p.sendMessage("lastSafeLocation is not in a block");
		}
		else p.sendMessage("lastSafeLocation is in a block");

		if(iterateLocation.getBlock().isPassable())
		{
			p.sendMessage("iterateLocation is not in a block");
		}
		else p.sendMessage("iterateLocation is in a block");


		//p.sendMessage("trying to get location of hit");
		//p.sendMessage("blockface hit: "+ getHitBlockFace(p,lastSafeLocation,iterateLocation, iterateLocation.getBlock(), world));
		*/

			BlockFace hitFace = getHitBlockFace(p, lastSafeLocation, iterateLocation, iterateLocation.getBlock(), world);
			if (hitFace == BlockFace.SELF)
			{
				break;
			}
			Vector reflectedVector = getReflectedVector(iterateVector, hitFace);

			unitAdditionVector = reflectedVector.clone().normalize().multiply(iterateDistance);
			iterateVector = reflectedVector.clone().add(unitAdditionVector);
			bounces++;
			//p.sendMessage("bounce "+bounces+" at "+iterateLocation.getX()+", "+iterateLocation.getY()+", "+iterateLocation.getZ());

			lastIterateTime.put(p,(long)0);

			while (!iterateLocation.getBlock().isPassable()&& distanceTraveled <= maxDistance && iterateLocation.getY() <= world.getMaxHeight()+50) {
				if (System.currentTimeMillis() >= lastIterateTime.get(p)+iteratorCooldown) {
					lastSafeLocation = iterateLocation.clone();
					iterateVector = iterateVector.add(unitAdditionVector);
					iterateLocation = iterateLocation.add(unitAdditionVector);
					Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(0, 255, 0), Color.fromRGB(255, 255, 255), 1.0F); //green fade to white
					p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0, 0, 0, 0, dustTransition, true);
					distanceTraveled += iterateDistance; //adds iterateDistance to distance traveled, so it does not go on forever
					lastIterateTime.put(p,System.currentTimeMillis());
				}
			}
			if (distanceTraveled >= maxDistance || iterateLocation.getY() >= world.getMaxHeight()+50)
			{
				p.sendMessage("The divine ray traveled "+distanceTraveled+" blocks along "+bounces+" bounces.");
				break;
			}

		if (bounces == maxBounces)
		{
			p.sendMessage("The divine ray traveled "+distanceTraveled+" blocks along "+bounces+" bounces.");
		}
		}
	}

	public Vector getReflectedVector(Vector originalVec, BlockFace hitFace) {
		Vector normalVec = hitFace.getDirection().multiply(-1);
		double dotProduct = originalVec.dot(normalVec);
		Vector projectionVec = normalVec.multiply(dotProduct);
		Vector reflectedVec = originalVec.subtract(projectionVec.multiply(2));
		return reflectedVec;
	}


	public BlockFace getHitBlockFace(Player p,Location safeLoc,Location iterLoc,Block iterBlock, World world)
	{
		//try going back to using "location" of center of faces. make SURE they are accurate. spawn particles to be sure.
		//since iterateLoc particle spawns in right place, iterateLoc is a good reference for which block face it is. find least dist from iterateLoc to all blockFaces.
		//least distance to blockface == closest blockface. return that blockface and use as face for normal vector calculation

		Vector vectorBetweenSafeAndIter = iterLoc.toVector().subtract(safeLoc.toVector());
		BlockFace hitFace = BlockFace.SELF; //default value
		RayTraceResult result = world.rayTraceBlocks(safeLoc, vectorBetweenSafeAndIter,0.2);
		if (result != null)
		{
			hitFace = result.getHitBlockFace();
		}
		else p.sendMessage("There was no found hit between vec safeLoc and vec iterLoc");
		return hitFace;
	}



	@EventHandler
	public void OnBuildChange(BuildChangeEvent e)
	{
		Player p = userManager.getUser(e.getPlayerUUID()).toPlayer();
		internalCD.put(p,(long)0);
		lastIterateTime.put(p,(long)0);
	}

}



