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
import me.barret.utils.UtilTeam;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;

public class DivineRay extends Skill implements interactSkill
{
	private static Kit skillKit = kitManager.getKitFromString("Mage");

	private static SkillType skillType = SkillType.SWORD;

	static String skillName = "Divine Ray";

	static String[] description = {"Unleash a beam of glorious light to pierce your enemies"};

	static int MaxLevel = 4;

	private static HashMap<Player,Long> timeActivated = new HashMap<Player, Long>();

	private static HashMap<Player,String> uuidStorage = new HashMap<Player, String>();

	private static HashMap<Player,Long> internalCD = new HashMap<Player, Long>();

	private static HashMap<Player, Integer> skillLevel = new HashMap<Player, Integer>();

	private static HashMap<Player, Boolean> canIterate = new HashMap<Player, Boolean>();

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

	private static HashMap<Player, Integer>  iterationsPerTick = new HashMap<Player,Integer>();

	private static HashMap<Player, Integer>  totalIterationsMap = new HashMap<Player,Integer>();

	private static HashMap<Player, Integer>  iterationsPerParticleMap = new HashMap<Player,Integer>();

	private static HashMap<Player, List<Location>> storedLocations = new HashMap<Player, List<Location>>();

	private static HashMap<Player,Location> glowLocationMap = new HashMap<Player, Location>();
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
					int totalIterations = totalIterationsMap.get(p);

					if (bounces <= maxBounces && distanceTraveled <= maxDistance && iterateLocation.getY() <= world.getMaxHeight() + 50) { //LATER TO DO: Add checks for max iterations ONLY IF is inside block. have terminate after its gone 3 iterations while in block

//iterate here.
						if(iterateLocation.getBlock().isPassable()) {
							lastSafeLocation = iterateLocation.clone();
							lastSafeLocationMap.put(p,lastSafeLocation);
							iterateLocation.add(unitAdditionVector);
							iterateLocationMap.put(p,iterateLocation);
							distanceTraveled += iterateDistance;
							totalIterations ++;
							totalIterationsMap.put(p,totalIterations);

							distanceTraveledMap.put(p,distanceTraveled);
							spawnRainbowParticle(p,iterateLocation,bounces,totalIterations);

						}else
						{
							glowLocationMap.put(p,iterateLocation.getBlock().getLocation());
							iterateLocation.subtract(unitAdditionVector);
							lastSafeLocation.subtract(unitAdditionVector);
							doReflection(p,lastSafeLocation,iterateLocation,unitAdditionVector);
							makeBounceGlow(p);
							bounces++;
							bouncesMap.put(p,bounces);
							distanceTraveled += iterateDistance;
							distanceTraveledMap.put(p,distanceTraveled);
							if(bounces < maxBounces)
								p.getWorld().playSound(iterateLocation,Sound.BLOCK_AMETHYST_CLUSTER_STEP,(float) 1.8,(float) 1.8);
							else p.getWorld().playSound(iterateLocation,Sound.BLOCK_AMETHYST_CLUSTER_BREAK,(float) 1.8,(float) 1.8);
						}

						if (bounces >= maxBounces || distanceTraveled >= maxDistance) {
							p.getWorld().playSound(iterateLocation,Sound.BLOCK_AMETHYST_BLOCK_CHIME,(float) 1.8,(float) 1.8);
							p.sendMessage("The divine ray traveled " + (int) distanceTraveled + " blocks along " + bounces + " bounces.");
							killBounceGlow();
							canIterate.put(p, false);
							return;
						}
					}
				}
			}
		}
	}

	public void cubeColor(MagmaCube m, Player p)
	{
		int bounces = bouncesMap.get(p);
		p.sendMessage("bounces: "+bounces);
		int bounceIndex = bounces%7;
		ChatColor[] colorArray = {ChatColor.RED,ChatColor.GOLD,ChatColor.YELLOW,ChatColor.GREEN,ChatColor.BLUE,ChatColor.LIGHT_PURPLE,ChatColor.DARK_PURPLE};
		p.sendMessage("color: "+colorArray[bounceIndex]);
		UtilTeam.addColor((Entity) m, colorArray[bounceIndex]);
	}

	private void applyEffects(MagmaCube m, Player p)
	{
		cubeColor(m,p);
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
	//may need a smaller it function to iterate from safe location to idealLoc which is as close to block as possible. will not need to be mirrored. find intersect location using same method as getHitBlockFace?

	public void doReflection(Player p, Location lastSafeLocation, Location iterateLocation, Vector unitAdditionVector)
	{
		BlockFace hitFace = getHitBlockFace(p,lastSafeLocation,iterateLocation);
		Vector reflectedVec = getReflectedVector(p,iterateLocation,unitAdditionVector,hitFace);
		unitAdditionVectorMap.put(p,reflectedVec);
	}

	public Vector getReflectedVector(Player p, Location loc,Vector originalVec, BlockFace hitFace) {


		Vector normalVec = hitFace.getDirection();//.multiply(-1);
		double dotProduct = originalVec.dot(normalVec);
		Vector projectionVec = normalVec.multiply(dotProduct);
		Vector reflectedVec = originalVec.subtract(projectionVec.multiply(2));
		reflectedVec.normalize().multiply(iterateDistanceMap.get(p));
		//reflectedVec = lastSafeLocation.clone().toVector().add(reflectedVec);


		return reflectedVec;
	}


	public BlockFace getHitBlockFace(Player p,Location safeLoc,Location iterLoc) //good with new method :)
	{
		//p.sendMessage("getting blockface");
		World world = p.getWorld();

		Vector vectorBetweenSafeAndIter = iterLoc.toVector().subtract(safeLoc.toVector());
		BlockFace hitFace = BlockFace.SELF; //default value
		//p.sendMessage("getting ray trace");
		RayTraceResult result = world.rayTraceBlocks(safeLoc, vectorBetweenSafeAndIter,0.2);


		//p.sendMessage("got ray trace:"+result);
		if (result != null)
		{
			hitFace = result.getHitBlockFace();
			Block hitBlock = iterateLocationMap.get(p).getBlock(); //default value
			if(result.getHitBlock() != null)
			{
				hitBlock = result.getHitBlock();
			}

			//p.sendMessage("hitface "+ hitFace);
		}
		else p.sendMessage("There was no found hit between vec safeLoc and vec iterLoc");
		return hitFace;
	}

	public void makeBounceGlow(Player p)
	{
		MagmaCube mag = (MagmaCube) p.getWorld().spawnEntity(glowLocationMap.get(p).clone().add(0.5,0,0.5), EntityType.MAGMA_CUBE,false);
		applyEffects(mag, p);
		List<Location> cubeLocations= new ArrayList<Location>();
		cubeLocations.add(mag.getLocation());

		if(storedLocations.containsKey(p))
			storedLocations.get(p).add(mag.getLocation());
		else storedLocations.put(p,cubeLocations);
	}
	public void killBounceGlow()
	{
		for (Player p: uuidStorage.keySet()) {
			for (org.bukkit.entity.Entity ent : p.getWorld().getEntities()) {
				if (ent instanceof MagmaCube) {
					if (ent.getCustomName().equalsIgnoreCase(uuidStorage.get(p))) {
						ent.teleport(ent.getLocation().add(0, -500, 0));
						ent.remove();
					}
				}
			}
		}
	}

	public void spawnRainbowParticle(Player p, Location iterateLocation, int bounces,int totalIterations)
	{
		if(totalIterations % iterationsPerParticleMap.get(p) == 0) {
			int bounceIndex = bounces % 7;
			Color white = Color.fromRGB(255, 255, 255);
			Color black = Color.fromRGB(0, 0, 0);
			Color red = Color.fromRGB(255, 0, 0);
			Color orange = Color.fromRGB(255, 127, 0);
			Color yellow = Color.fromRGB(255, 255, 0);
			Color green = Color.fromRGB(0, 255, 0);
			Color blue = Color.fromRGB(0, 0, 255);
			Color indigo = Color.fromRGB(75, 0, 130);
			Color violet = Color.fromRGB(148, 0, 211);

			Color[] colorArray = {red, orange, yellow, green, blue, indigo, violet};

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
	}

	public void CreateRay(Player p, user u, int lvl){

		//modifiable values

		double iterateDistance = 0.1; // BASICALLY DO NOT MODIFY, MIGHT MAKE ENTIRE RAYCAST NOT WORK. WORKING VALUES: 0.1 - the most tested, 0.2 works i think. 0.3 is INCONSISTENT.
		iterateDistanceMap.put(p,iterateDistance);
		int maxBounces = 3 * lvl - lvl; //modify for balance sake, make depend on lvl of player skill
		maxBouncesMap.put(p,maxBounces);
		double maxDistance = 128; //modify for balance sake, make depend on lvl of player skill
		maxDistanceMap.put(p,maxDistance);

		int iterPerTick = 4; //it seems like iterations per tick does not affect phasing.
		iterationsPerTick.put(p,iterPerTick);

		int iterPerParticle = 1; //tweak this
		iterationsPerParticleMap.put(p,iterPerParticle);

		//end modifiable values



		Vector lastSafeVector = p.getEyeLocation().getDirection();
		lastSafeVectorMap.put(p,lastSafeVector);
		Vector iterateVector = p.getEyeLocation().getDirection(); //only for initial ray before bounce
		iterateVectorMap.put(p,iterateVector);
		Location iterateLocation = p.getEyeLocation(); //only for initial ray before bounce
		iterateLocationMap.put(p,iterateLocation);
		Location lastSafeLocation = p.getEyeLocation();
		lastSafeLocationMap.put(p,lastSafeLocation);
		Vector unitAdditionVector = p.getEyeLocation().getDirection().normalize().multiply(iterateDistance); //normalize makes it magnitude of 1 in same direction.
		unitAdditionVectorMap.put(p,unitAdditionVector);
		int bounces = 0;
		bouncesMap.put(p,bounces);
		double distanceTraveled = 0;
		distanceTraveledMap.put(p,distanceTraveled);
		int totalIterations = 0;
		totalIterationsMap.put(p,totalIterations);

		canIterate.put(p,true);


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











	@EventHandler
	public void OnBuildChange(BuildChangeEvent e)
	{
		Player p = userManager.getUser(e.getPlayerUUID()).toPlayer();
		internalCD.put(p,(long)0);
		canIterate.put(p,false);
		uuidStorage.put(p,p.getUniqueId().toString());


	}

}

