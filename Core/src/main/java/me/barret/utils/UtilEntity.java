package me.barret.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;

public class UtilEntity {

	
	public static void createGlow(Location l) {
		Entity e = l.getWorld().spawnEntity(l, EntityType.MAGMA_CUBE);
		
		MagmaCube magmaCube = (MagmaCube) e;
		magmaCube.setSize(2);
		magmaCube.setInvisible(true);
		magmaCube.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
		magmaCube.setGlowing(true);
		magmaCube.setGravity(false);
		magmaCube.setAI(false);
		magmaCube.setCustomName("wall slut");
		magmaCube.setCustomNameVisible(false);
		magmaCube.setSilent(true);
		//return (Entity) magmaCube;
		//Entity e = getNewEntity(EntityType.MAGMA_CUBE, l);
		//e = applyBitches(e);
		
		
	}
	

	public static Entity applyBitches(Entity e) {
		MagmaCube magmaCube = (MagmaCube) e;
		magmaCube.setSize(2);
		magmaCube.setInvisible(true);
		magmaCube.setInvulnerable(true); //only causes to not wall suffocate does not cancel kill by player or entity
		magmaCube.setGlowing(true);
		magmaCube.setGravity(false);
		magmaCube.setAI(false);
		magmaCube.setCustomName("wall slut");
		magmaCube.setCustomNameVisible(false);
		magmaCube.setSilent(true);
		return (Entity) magmaCube;
	} 
	
	
	
	
	
	
	
	
	
	
	public static Entity getNewEntity(EntityType e, Location l) {
		return EntityCreator.create(e, l);
		
	}
	       

	
	
	
	
	
}




