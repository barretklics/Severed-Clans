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
import me.barret.cooldown.cooldownManager;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
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

	private static HashMap<Player, Integer>  particleCountMap = new HashMap<Player,Integer>();

	private static HashMap<Player, Boolean> nonPassableNonSolidCase = new HashMap<Player, Boolean>();

	private static HashMap<Entity, Long> coloredEntityMap = new HashMap<Entity, Long>();

	private static HashMap<Player, Double> damageDealtMap = new HashMap<Player, Double>();

	private static HashMap<Player, ArrayList<LivingEntity>> hitEntities = new HashMap<Player, ArrayList<LivingEntity>>();

	private static HashMap<Player, Long> timeOfLastDamage = new HashMap<Player, Long>();

	private static HashMap<LivingEntity, Long> lastBeamHitTimeForEntity = new HashMap<LivingEntity,Long>();

	private static HashMap<LivingEntity, Boolean> isEntityBeamDamageable = new HashMap<LivingEntity, Boolean>();

	private static HashMap<Player, Boolean> secondActivate = new HashMap<Player, Boolean>();

	public DivineRay(Champions i)
	{
		super(i, skillKit, skillName, skillType, description, MaxLevel);
	}

	public void CreateRay(Player p, user u, int lvl){

		//modifiable values

		double iterateDistance = 0.1; // BASICALLY DO NOT MODIFY, MIGHT MAKE ENTIRE RAYCAST NOT WORK. WORKING VALUES: 0.1 - the most tested, //0.2 works i think. 0.3 is INCONSISTENT. - 0.1 is best for non phase sake
		iterateDistanceMap.put(p,iterateDistance);
		int maxBounces = 3 * lvl - lvl; //modify for balance sake, make depend on lvl of player skill
		maxBouncesMap.put(p,maxBounces);
		double maxDistance = 64 * (lvl + 1) - 32; //modify for balance sake, make depend on lvl of player skill
		maxDistanceMap.put(p,maxDistance);

		int iterPerTick = lvl + 1; //speed at which ray travels. (too high looks choppy)
		iterationsPerTick.put(p,iterPerTick);

		double damageDealt = 1.5*lvl + 0.5;
		damageDealtMap.put(p,damageDealt);

		int iterPerParticle = 2;
		iterationsPerParticleMap.put(p,iterPerParticle);

		int particleCount = 25;
		particleCountMap.put(p,particleCount);

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
	}

	@Override
	public void activate(Player p, user u, int level) {
		if (internalCD.get(p) + 20 <=System.currentTimeMillis()) {
			if(cooldownManager.addCooldown(p,skillName,20-level,true))
			{
				internalCD.put(p, System.currentTimeMillis());
				if(canIterate.get(p)) //checks for activation while beam is alive, will kill old beam. fixes bug with ramping damage if cooldown is too short and activations are spammed before beam dies
				{
					secondActivate.put(p,true);
				}
				timeActivated.put(p, System.currentTimeMillis());
				killAllCubes();
				CreateRay(p, u, level);
				skillLevel.put(p, level);
				nonPassableNonSolidCase.put(p,false);
			}
		}
	}


	@EventHandler
	public void rayIterator(TickUpdateEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (userManager.getUser(p.getUniqueId()).getCurrentBuild() == null ||userManager.getUser(p.getUniqueId()).getCurrentBuild().getSword() ==null|| !userManager.getUser(p.getUniqueId()).getCurrentBuild().getSword().getName().equalsIgnoreCase(skillName) || canIterate.get(p) == false)
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

					if (bounces <= maxBounces && distanceTraveled <= maxDistance && iterateLocation.getY() <= world.getMaxHeight() + 50) { //checks for bounce limit, travel distance limit, and if ray is 50 above height limit

						Boolean cornerCase = tryCornerCase(p,lastSafeLocation,iterateLocation); //basically is a check for kissing bird corner scenario. may make isPassable redundant, but redundancies for safety's sake are good

						if(iterateLocation.getBlock().isPassable()&&!cornerCase) {
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
							killAllCubes();
							glowLocationMap.put(p,iterateLocation.getBlock().getLocation());
							iterateLocation.subtract(unitAdditionVector);
							lastSafeLocation.subtract(unitAdditionVector);
							doReflection(p,lastSafeLocation,iterateLocation,unitAdditionVector);
							makeCubesGlow(p);
							bounces++;
							bouncesMap.put(p,bounces);
							distanceTraveled += iterateDistance;
							distanceTraveledMap.put(p,distanceTraveled);
							if(bounces < maxBounces)
								p.getWorld().playSound(iterateLocation,Sound.BLOCK_AMETHYST_CLUSTER_STEP,(float) 1.8,(float) 1.8);
							else p.getWorld().playSound(iterateLocation,Sound.BLOCK_AMETHYST_CLUSTER_BREAK,(float) 1.8,(float) 1.8);
						}

						if (bounces >= maxBounces || distanceTraveled >= maxDistance || nonPassableNonSolidCase.get(p) || secondActivate.get(p)) {
							p.getWorld().playSound(iterateLocation,Sound.BLOCK_AMETHYST_BLOCK_CHIME,(float) 1.8,(float) 1.8);
							resetDamageToStandard(p);
							p.sendMessage("The divine ray traveled " + (int) distanceTraveled + " blocks along " + bounces + " bounces.");
							killAllCubes();
							canIterate.put(p, false);
							secondActivate.put(p,false);
							return;
						}
					}
				}
			}
		}
	}


	//Caused by: java.lang.IllegalArgumentException: Start location is null!
	//	at org.apache.commons.lang.Validate.notNull(Validate.java:192) ~[commons-lang-2.6.jar:2.6]
	//	at org.bukkit.craftbukkit.v1_19_R2.CraftWorld.rayTraceEntities(CraftWorld.java:878) ~[spigot-1.19.3-R0.1-SNAPSHOT.jar:3635-Spigot-d90018e-941d7e9]
	//	at org.bukkit.craftbukkit.v1_19_R2.CraftWorld.rayTraceEntities(CraftWorld.java:873) ~[spigot-1.19.3-R0.1-SNAPSHOT.jar:3635-Spigot-d90018e-941d7e9]
	//	at org.bukkit.craftbukkit.v1_19_R2.CraftWorld.rayTraceEntities(CraftWorld.java:863) ~[spigot-1.19.3-R0.1-SNAPSHOT.jar:3635-Spigot-d90018e-941d7e9]
	//	at me.barret.skill.Mage.DivineRay.getHitEntity(DivineRay.java:271) ~[?:?]
	//	at me.barret.skill.Mage.DivineRay.rayInEntity(DivineRay.java:234) ~[?:?]
	@EventHandler
	public void rayInEntity(TickUpdateEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (userManager.getUser(p.getUniqueId()).getCurrentBuild() == null || userManager.getUser(p.getUniqueId()).getCurrentBuild().getSword() ==null||!userManager.getUser(p.getUniqueId()).getCurrentBuild().getSword().getName().equalsIgnoreCase(skillName)) {
				return;
			}
				else{
					if (getHitEntity(p) == null) {
						return;
					} else {
						Entity hitEntity = getHitEntity(p);

						if (hitEntity instanceof Player) { //casts to hit entity to player because of team color method gimmick
							Player hitPlayer = (Player) hitEntity;
							if (hitPlayer == p) //if caster of DivineRay hits self with beam, cancels glow on caster.
								return;

							else {
								hitPlayer.setGlowing(true);
								playerColorer(hitPlayer, p);

							}
						}
						else {
							hitEntity.setGlowing(true);
							entityColorer(hitEntity, p);
						}
						coloredEntityMap.put(hitEntity, System.currentTimeMillis());
						if (hitEntity instanceof LivingEntity) {
							if (hitEntity != p) {//if caster of DivineRay hits self with beam, cancels damage on caster.

									if(!isEntityBeamDamageable.containsKey((LivingEntity) hitEntity)||isEntityBeamDamageable.get(hitEntity)) {
										isEntityBeamDamageable.put((LivingEntity)hitEntity,false);
										lastBeamHitTimeForEntity.put((LivingEntity) hitEntity,System.currentTimeMillis());
										dealDamage(p, (LivingEntity) hitEntity);

									}



							}
						}


					}
				}
			}
		}

		public void dealDamage(Player p,LivingEntity e) //needs to do a time check for each entity to not increment it twice if it is in an entity twice within 0.5s (mc damage cooldown) of first hit on that entity
		{
			if (e instanceof MagmaCube)
			{
				if (e.getCustomName().equalsIgnoreCase(uuidStorage.get(p)))
				{
					return;
				}
			}

			if (!hitEntities.isEmpty() && hitEntities.containsKey(p))
			{
				ArrayList<LivingEntity> eList = hitEntities.get(p);
				eList.add(e);
				hitEntities.put(p,eList);
			}
			else
			{
				ArrayList<LivingEntity> eList = new ArrayList<>();
				eList.add(e);
				hitEntities.put(p,eList);
			}

			ArrayList<LivingEntity> entityList = hitEntities.get(p);
			EntityType type = e.getType();
			int countOfSameType = 0;

			for (int i = 0; i < entityList.size(); i++)
			{
				if(entityList.get(i).getType() == type)
				{
					countOfSameType ++;
				}
			}


			p.sendMessage("Damage dealt: "+countOfSameType * damageDealtMap.get(p)+" on "+ type);
			e.damage(countOfSameType * damageDealtMap.get(p));

		}

		public void resetDamageToStandard(Player p)
		{
			if (!hitEntities.isEmpty())
			{
				if (hitEntities.containsKey(p))
				{
					ArrayList<LivingEntity> entityList = hitEntities.get(p);
					entityList.clear();
					hitEntities.put(p,entityList);
				}
			}
		}

	@EventHandler
	public void hitCooldown(TickUpdateEvent event)
	{
	if (lastBeamHitTimeForEntity.isEmpty()) {
		return;
	} else {
	for (LivingEntity e : lastBeamHitTimeForEntity.keySet()) {
		if (System.currentTimeMillis() > lastBeamHitTimeForEntity.get(e) + 500) {

			isEntityBeamDamageable.put(e,true);
		}
	}
}
}

	@EventHandler
	public void removeEntityGlow(TickUpdateEvent event) {
		if (coloredEntityMap.isEmpty()) {
			return;
		} else {
			for (Entity e : coloredEntityMap.keySet()) {
				if (System.currentTimeMillis() > coloredEntityMap.get(e) + 5000) {

					if (e instanceof Player)
					{
						e.setGlowing(false);
						playerColorReset((Player) e);
					}
					e.setGlowing(false);
					entityColorReset(e);
				}
			}
		}
	}
	public Entity getHitEntity(Player p)
	{
		if (!lastSafeLocationMap.containsKey(p) || !unitAdditionVectorMap.containsKey(p) || p.getWorld().rayTraceEntities(lastSafeLocationMap.get(p),unitAdditionVectorMap.get(p),0.1) == null)
		{
			return null;
		}
		else
		{
			RayTraceResult result = p.getWorld().rayTraceEntities(lastSafeLocationMap.get(p),unitAdditionVectorMap.get(p),0.1);
			Entity hitEntity = result.getHitEntity();
			return hitEntity;
		}
	}
	public Boolean tryCornerCase(Player p, Location safeLoc, Location iterLoc)
	{
		if (p.getWorld().rayTraceBlocks(safeLoc,unitAdditionVectorMap.get(p),0.1) == null)
		{
			return false;
		}
		else return true;
	}

	public void entityColorer(Entity e, Player p)
	{
		int bounces = bouncesMap.get(p);
		int bounceIndex = bounces%7;
		ChatColor[] colorArray = {ChatColor.RED,ChatColor.GOLD,ChatColor.YELLOW,ChatColor.GREEN,ChatColor.BLUE,ChatColor.DARK_BLUE,ChatColor.DARK_PURPLE};
		UtilTeam.addColor(e, colorArray[bounceIndex]);
	}
	public void playerColorer(Player hitPlayer, Player p)
	{
		int bounces = bouncesMap.get(p);
		int bounceIndex = bounces%7;
		ChatColor[] colorArray = {ChatColor.RED,ChatColor.GOLD,ChatColor.YELLOW,ChatColor.GREEN,ChatColor.BLUE,ChatColor.DARK_BLUE,ChatColor.DARK_PURPLE};
		UtilTeam.addColorToPlayer(hitPlayer, colorArray[bounceIndex]);
	}


	public void playerColorReset (Player p)
	{
		UtilTeam.removeColorFromPlayer(p);
	}
	public void entityColorReset(Entity e)
	{
		UtilTeam.removeColor(e);
	}
	private void applyEffects(MagmaCube m, Player p)
	{
		entityColorer((Entity)m,p);
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
		BlockFace hitFace = getHitBlockFace(p,lastSafeLocation,iterateLocation,false);
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



	public BlockFace getHitBlockFace(Player p,Location safeLoc,Location iterLoc, Boolean bypassDefaultVal) //good with new method :)
	{
		//p.sendMessage("getting blockface");
		World world = p.getWorld();
		BlockFace hitFace = null;
		Vector vectorBetweenSafeAndIter = iterLoc.toVector().subtract(safeLoc.toVector());
		if(!bypassDefaultVal)
		{
			hitFace = BlockFace.SELF; //default value
		}

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
		else
		{
			p.sendMessage("The ray stopped because it was inside a non-passable, non solid block.");
			nonPassableNonSolidCase.put(p,true);
		}
		return hitFace;
	}

	public void makeCubesGlow(Player p)
	{
		MagmaCube mag = (MagmaCube) p.getWorld().spawnEntity(glowLocationMap.get(p).clone().add(0.5,0,0.5), EntityType.MAGMA_CUBE,false);
		applyEffects(mag, p);
		List<Location> cubeLocations= new ArrayList<Location>();
		cubeLocations.add(mag.getLocation());

		if(storedLocations.containsKey(p))
			storedLocations.get(p).add(mag.getLocation());
		else storedLocations.put(p,cubeLocations);
	}
	public void killAllCubes()
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
			p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, iterateLocation, particleCountMap.get(p), 0, 0, 0, 0, rainbowTransition, true);
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

	@EventHandler
	public void OnBuildChange(BuildChangeEvent e)
	{
		Player p = userManager.getUser(e.getPlayerUUID()).toPlayer();
		p.setGlowing(false);
		internalCD.put(p,(long)0);
		canIterate.put(p,false);
		uuidStorage.put(p,p.getUniqueId().toString());
		nonPassableNonSolidCase.put(p,false);
		timeOfLastDamage.put(p,(long)0);
		secondActivate.put(p,false);
	}

}

