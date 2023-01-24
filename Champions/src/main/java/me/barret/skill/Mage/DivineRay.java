package me.barret.skill.Mage;

import me.barret.Champions;
import me.barret.build.BuildChangeEvent;
import me.barret.events.TickUpdateEvent;
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
import java.util.Timer;
import java.util.TimerTask;

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

	private static HashMap<Player, Boolean> canIterate = new HashMap<Player, Boolean>();
	private static HashMap<Player, Boolean> canIterateInsideBlock = new HashMap<Player, Boolean>();

	private static HashMap<Player, Location> lastSafeLocationMap = new HashMap<Player, Location>();
	private static HashMap<Player, Location> iterateLocationMap = new HashMap<Player, Location>();

	private static HashMap<Player, Vector> iterateVectorMap = new HashMap<Player, Vector>();

	private static HashMap<Player, Vector> lastSafeVectorMap = new HashMap<Player, Vector>();

	private static HashMap<Player, Vector> unitAdditionVectorMap = new HashMap<Player, Vector>();
	private static HashMap<Player, Integer> bouncesMap = new HashMap<Player,Integer>();

	private static HashMap<Player, Integer> maxBouncesMap = new HashMap<Player,Integer>();

	private static HashMap<Player, Double> maxDistanceMap = new HashMap<Player, Double>();

	private static HashMap<Player, Double> distanceTraveledMap = new HashMap<Player, Double>();

	private static HashMap<Player, Double> iterateDistanceMap = new HashMap<Player, Double>();

	private static HashMap<Player,Integer> maxInsideIterations = new HashMap<Player,Integer>();

	private static HashMap<Player,Integer> currentInsideIterations = new HashMap<Player,Integer>();

	private static HashMap<Player, Integer>  iterationsPerTick = new HashMap<Player,Integer>();

	public DivineRay(Champions i)
	{
		super(i, skillKit, skillName, skillType, description, MaxLevel);
	}

	@Override
	public void activate(Player p, user u, int level) {
		if (internalCD.get(p) + 20 <=System.currentTimeMillis()) {
			internalCD.put(p, System.currentTimeMillis());
			timeActivated.put(p, System.currentTimeMillis());
			//p.sendMessage("activate");
			CreateRay(p, u, level);
			//p.sendMessage("activate post create ray");
			skillLevel.put(p, level);
		}
	}


	@EventHandler
	public void rayIterator(TickUpdateEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (userManager.getUser(p.getUniqueId()).getCurrentBuild() == null || !userManager.getUser(p.getUniqueId()).getCurrentBuild().getSword().getName().equalsIgnoreCase(skillName) || canIterate.get(p) == false)
			//canIterate logic may be problematic while building new format
			{
				return;
			}
			if (canIterate.get(p)) {

				for (int i = 0; i <= iterationsPerTick.get(p); i++) {//this for loop increases the speed of iterations. essentially, every tick it does 5 0.2 block iterations.
					//p.sendMessage("initializing vars in rayIterator");
					double distanceTraveled = distanceTraveledMap.get(p);
					double maxDistance = maxDistanceMap.get(p);
					Location iterateLocation = iterateLocationMap.get(p);
					World world = p.getWorld();
					int bounces = bouncesMap.get(p);
					int maxBounces = maxBouncesMap.get(p);
					Location lastSafeLocation = lastSafeLocationMap.get(p);
					Vector iterateVector = iterateVectorMap.get(p);
					Vector lastSafeVector = lastSafeVectorMap.get(p);
					Vector unitAdditionVector = unitAdditionVectorMap.get(p);
					double iterateDistance = iterateDistanceMap.get(p);

					if (bounces <= maxBounces && distanceTraveled <= maxDistance && iterateLocation.getY() <= world.getMaxHeight() + 50) { //LATER TO DO: Add checks for max iterations ONLY IF is inside block. have terminate after its gone 3 iterations while in block

						if (!canIterateInsideBlock.get(p))//if the ray is NOT allowed to iterate inside blocks
						{
							if (!iterateLocation.getBlock().isPassable()) //if the block is NOT passable
							{
								canIterate.put(p, false);
								doReflection(p, lastSafeLocation, iterateLocation, iterateVector,lastSafeVector); //time to reflect
							}
						}
						if (canIterateInsideBlock.get(p))//if the ray IS ALLOWED inside blocks
						{
							currentInsideIterations.put(p,currentInsideIterations.get(p)+1); //iter += 1;
							if (iterateLocation.getBlock().isPassable()) //if the block IS PASSABLE
							{
								canIterateInsideBlock.put(p, false);
							}
							else if (false)//maxInsideIterations.get(p) <= currentInsideIterations.get(p))
							{
							canIterate.put(p,false);
							canIterateInsideBlock.put(p, false);
							p.sendMessage("should stop the ray outright. corner case?");
							break;
							}
						}

						lastSafeLocation = iterateLocation.clone();
						lastSafeVector=iterateVector.clone();
						if(iterateVector.clone().toLocation(world).getBlock().isPassable()) {
							iterateVector = iterateVector.add(unitAdditionVector);
						}
						iterateLocation = iterateLocation.add(unitAdditionVector);
						distanceTraveled += iterateDistance; //adds iterateDistance to distance traveled, so it does not go on forever

					//MAKE MESSAGES TO SEE IF VECTORS ARE IN BLOCKS AT 1 POINT
						p.sendMessage("lastSafeLocation "+lastSafeLocation.clone().getBlock().getType());
						p.sendMessage("iterateLocation "+iterateLocation.clone().getBlock().getType());
						p.sendMessage("lastSafeVector "+lastSafeVector.clone().toLocation(world).getBlock().getType());
						p.sendMessage("iterateVector "+iterateVector.clone().toLocation(world).getBlock().getType());

						lastSafeLocationMap.put(p, lastSafeLocation);
						iterateVectorMap.put(p, iterateVector);
						iterateLocationMap.put(p, iterateLocation);
						distanceTraveledMap.put(p, distanceTraveled);

						spawnRainbowParticle(p, lastSafeLocation, bounces);
					}

					if (bounces >= maxBounces || distanceTraveled >= maxDistance) {
						p.sendMessage("The divine ray traveled " + (int) distanceTraveled + " blocks along " + bounces + " bounces.");
						canIterate.put(p, false);
						return;
					}
				}
			}
		}
	}

	public void spawnRainbowParticle(Player p, Location iterateLocation, int bounces)
	{
		int bounceIndex =  bounces % 6;
		Color white = Color.fromRGB(255,255,255);
		Color black = Color.fromRGB(0,0,0);
		Color red = Color.fromRGB(255,0,0);
		Color orange = Color.fromRGB(255,127,0);
		Color yellow = Color.fromRGB(	255, 255, 0);
		Color green = Color.fromRGB(	0, 255, 0);
		Color blue = Color.fromRGB(	0, 0, 255);
		Color indigo = Color.fromRGB(75, 0, 130);
		Color violet = Color.fromRGB(	148, 0, 211);

		Color[] colorArray = {red,orange,yellow,green,blue,indigo,violet};

		/*
		if (bounceIndex != 6)
		{
			Particle.DustTransition rainbowTransition = new Particle.DustTransition(colorArray[bounceIndex], colorArray[bounceIndex+1], 1.0F); //rainbow fade to next color in rainbow
			p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0, 0, 0, 0, rainbowTransition, true);
		}
		else if (bounceIndex == 6)
		{
				Particle.DustTransition rainbowTransition = new Particle.DustTransition(colorArray[bounceIndex], colorArray[0], 1.0F); //rainbow fade to next color in rainbow. cool, blue is a little weird, doesnt flow as well as id hoped
				p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0, 0, 0, 0, rainbowTransition, true);
				//this block fades from rainbow color to rainbow color + 1 (red fades to orange, orange fades to yellow)
		}
		*/


		/*
		Particle.DustTransition rainbowTransition = new Particle.DustTransition(black, colorArray[bounceIndex], 1.0F); //black to rainbow. cool, but not "glorious"
		p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0, 0, 0, 0, rainbowTransition, true);
		//this block fades from black to rainbow color.
		*/



		Particle.DustTransition rainbowTransition = new Particle.DustTransition(white, colorArray[bounceIndex], 1.0F); //white to rainbow. slightly washed out
		p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0, 0, 0, 0, rainbowTransition, true);
		//this block fades from white to rainbow color.



		/*
		Particle.DustTransition rainbowTransition = new Particle.DustTransition(colorArray[bounceIndex],white, 1.0F); //rainbow to white.
		p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0, 0, 0, 0, rainbowTransition, true);
		//this block fades from a rainbow color to white
		*/

		/*
		Particle.DustTransition rainbowTransition = new Particle.DustTransition(colorArray[bounceIndex],black, 1.0F); //rainbow to white.
		p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0, 0, 0, 0, rainbowTransition, true);
		//this block fades from a rainbow color to black
		*/

	}

	public void doReflection(Player p, Location lastSafeLocation, Location iterateLocation,Vector iterateVector, Vector lastSafeVector) {
		BlockFace hitFace = getHitBlockFace(p, lastSafeLocation, iterateLocation);
		if (hitFace == BlockFace.SELF)
		{
			p.sendMessage("HITFACE WAS SELF"); //when hits a non-solid non-passible block. add some checks to keep going.
			return;
		}
		Vector reflectedVector = getReflectedVector(lastSafeVector, hitFace);

		unitAdditionVectorMap.put(p,reflectedVector.clone().normalize().multiply(iterateDistanceMap.get(p))); //unitAdditionVector = reflectedVector.clone().normalize().multiply(iterateDistance);
		iterateVectorMap.put(p,reflectedVector.clone().add(unitAdditionVectorMap.get(p))); //iterateVector = reflectedVector.clone().add(unitAdditionVector);
		bouncesMap.put(p,bouncesMap.get(p)+1); //bounces++;
		canIterateInsideBlock.put(p,true); //STARTS ITERATING AGAIN, equivalent to a second while loop, but now with new parameters because of the above hashmap stores.
		canIterate.put(p,true);
	}

	public void CreateRay(Player p, user u, int lvl){




		//modifiable values

		double iterateDistance = 0.1; // BASICALLY DO NOT MODIFY, MIGHT MAKE ENTIRE RAYCAST NOT WORK. WORKING VALUES: 0.1 - the most tested, 0.2 works i think. 0.3 is INCONSISTENT.
		iterateDistanceMap.put(p,iterateDistance);
		int maxBounces = 3 * lvl - lvl; //modify for balance sake, make depend on lvl of player skill
		maxBouncesMap.put(p,maxBounces);
		double maxDistance = 128; //modify for balance sake, make depend on lvl of player skill
		maxDistanceMap.put(p,maxDistance);
		int insideIterationsCap = 2; //3 iterations (starts at 0) inside blocks at max. hopefully kills corner phase
		maxInsideIterations.put(p,insideIterationsCap);

		int iterPerTick = 8;
		iterationsPerTick.put(p,iterPerTick);

		//end modifiable values



		Vector lastSafeVector = p.getEyeLocation().getDirection();
		lastSafeVectorMap.put(p,lastSafeVector);
		Vector iterateVector = p.getEyeLocation().getDirection(); //only for initial ray before bounce
		iterateVectorMap.put(p,iterateVector);
		Location iterateLocation = p.getEyeLocation(); //only for initial ray before bounce
		iterateLocationMap.put(p,iterateLocation);
		Location lastSafeLocation = p.getEyeLocation();
		lastSafeLocationMap.put(p,lastSafeLocation);
		Vector unitAdditionVector = iterateVector.clone().normalize().multiply(iterateDistance); //normalize makes it magnitude of 1 in same direction.
		unitAdditionVectorMap.put(p,unitAdditionVector);
		int bounces = 0;
		bouncesMap.put(p,bounces);
		double distanceTraveled = 0;
		distanceTraveledMap.put(p,distanceTraveled);
		int insideIterations = 0;
		currentInsideIterations.put(p,insideIterations);

		canIterate.put(p,true);
		canIterateInsideBlock.put(p,false);

//end the CreateRay here.
/*
		World world = p.getWorld();
			//first while loop


				lastSafeLocation = iterateLocation.clone();
				iterateVector = iterateVector.add(unitAdditionVector);
				iterateLocation = iterateLocation.add(unitAdditionVector);
				Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(0, 255, 0), Color.fromRGB(255, 255, 255), 1.0F); //green fade to white
				p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0, 0, 0, 0, dustTransition, true);
				distanceTraveled += iterateDistance; //adds iterateDistance to distance traveled, so it does not go on forever

			//first while loop END



			BlockFace hitFace = getHitBlockFace(p, lastSafeLocation, iterateLocation);
			if (hitFace == BlockFace.SELF)
			{
				break;
			}
			Vector reflectedVector = getReflectedVector(iterateVector, hitFace);

			unitAdditionVector = reflectedVector.clone().normalize().multiply(iterateDistance);
			iterateVector = reflectedVector.clone().add(unitAdditionVector);
			bounces++;
			//p.sendMessage("bounce "+bounces+" at "+iterateLocation.getX()+", "+iterateLocation.getY()+", "+iterateLocation.getZ());

			while (!iterateLocation.getBlock().isPassable()&& distanceTraveled <= maxDistance && iterateLocation.getY() <= world.getMaxHeight()+50) {


					lastSafeLocation = iterateLocation.clone();
					iterateVector = iterateVector.add(unitAdditionVector);
					iterateLocation = iterateLocation.add(unitAdditionVector);
					Particle.DustTransition crustTransition = new Particle.DustTransition(Color.fromRGB(0, 255, 0), Color.fromRGB(255, 255, 255), 1.0F); //green fade to white
					p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, 50, 0, 0, 0, 0, crustTransition, true);
					distanceTraveled += iterateDistance; //adds iterateDistance to distance traveled, so it does not go on forever

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
 */
		}







	public Vector getReflectedVector(Vector originalVec, BlockFace hitFace) {
		Vector normalVec = hitFace.getDirection().multiply(-1);
		double dotProduct = originalVec.dot(normalVec);
		Vector projectionVec = normalVec.multiply(dotProduct);
		Vector reflectedVec = originalVec.subtract(projectionVec.multiply(2));
		return reflectedVec;
	}


	public BlockFace getHitBlockFace(Player p,Location safeLoc,Location iterLoc)
	{
		World world = p.getWorld();
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
		canIterate.put(p,false);
		canIterateInsideBlock.put(p,false);

	}

}



