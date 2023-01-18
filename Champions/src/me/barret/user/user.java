package me.barret.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.barret.build.Build;
import me.barret.build.buildManager;
import me.barret.kits.Kit;
import me.barret.kits.kitManager;
import me.barret.utils.UtilMessage;

public class user {
	
	
	private UUID uuid; //Player UUID
	
	private Kit currentKit; //Current Class
	
	
	
	private HashMap<Kit, List<Build>> allBuilds = new HashMap<Kit, List<Build>>();

	
	//Shows kits, index_of_active_build
	private HashMap<Kit, Integer> currentBuildIndexes = new HashMap<Kit, Integer>();
	
	public user(UUID _uuid) {
		
		uuid = _uuid;
		
	}

	
	public boolean initializeBuilds() {
		
		//System.out.println("Initializing Builds-----------------");
		
		for (Kit k : kitManager.getKits()) {
			
			//System.out.println(k.getKitName() + ": ");
			
			List<Build> temp = new ArrayList<Build>();
			
			
			temp.add(buildManager.getDefaultBuild(k)); //Build 0 (Default)
			temp.add(buildManager.getBuildFromDatabase(uuid, k, 1)); //Build 1
			temp.add(buildManager.getBuildFromDatabase(uuid, k, 2)); //Build 1
			temp.add(buildManager.getBuildFromDatabase(uuid, k, 3)); //Build 1
			temp.add(buildManager.getBuildFromDatabase(uuid, k, 4)); //Build 1
			
			
		//	System.out.println("0 - " + temp.get(0).asList().toString());
			//System.out.println("1 - " + temp.get(1).asList().toString());
		//	System.out.println("2 - " + temp.get(2).asList().toString());
		//	System.out.println("3 - " + temp.get(3).asList().toString());
		//	System.out.println("4 - " + temp.get(4).asList().toString());
			
			
			allBuilds.put(k, temp);	
			currentBuildIndexes.put(k, buildManager.getActiveBuildFromDatabase(uuid, k));
			
			
		}
		
		return true;
	}
	
	public List<Build> getBuildsForKit(Kit k) {
		return allBuilds.get(k);
	}
	
	
	
	
	/**
	 * Returns the current build associated with the current kit the player is wearing
	 * 
	 * @return active user build
	 */
	public Build getCurrentBuild() {
		if (currentKit == null) return null;
		return allBuilds.get(currentKit).get(currentBuildIndexes.get(currentKit));
	}
	
	public Build getCurrentBuildForKit(Kit k) {
		return this.getBuildsForKit(k).get(currentBuildIndexes.get(k));
	}
	
	
	
	
	
	
	
	/**
	 * 
	 * 
	 * @param k
	 * @return index of current build
	 */
	public Integer getActiveBuildIndex(Kit k) {
		return currentBuildIndexes.get(k);
	}
	
	
	
	/**
	 * 
	 * @param k
	 * @param i
	 */
	public void setActiveBuild(Kit k, int i) {
		currentBuildIndexes.put(k,  i);
		
		//UtilMessage.sendBasic(this.toPlayer(), "Build", "You equipped build " + i);
		//this.toPlayer().sendMessage(allBuilds.get(k).get(i).asList().toString());
		
		
	}
	
	

	public UUID getUUID() {
		return uuid;
	}
	
	
	/**
	 * 
	 * @return Current Champions kit
	 */
	public Kit getKit() {
		
		return currentKit;
	}
	
	/**
	 * 
	 * @param k what the current kit should be set to
	 */
	public void setCurrentKit(Kit k) {
		currentKit = k;
	}
	
	
	
	
	public Player toPlayer() {
		for(Player p : Bukkit.getOnlinePlayers())
		{	
			if (p != null) 
			{	
				
				user u = userManager.getUser(p.getUniqueId());
				if (u == this) return p;
			}
		}
		return null;
	}
}
