package me.barret.build;

import java.util.UUID;

import me.barret.kits.Kit;
import me.barret.skill.skillManager;

public class buildManager {
	
	/**
	 * 
	 * 
	 * @param arg
	 * @return Build object
	 */
	public static Build getDefaultBuild(Kit arg)
	{
		switch(arg.getKitName()) 
		{
		case "Brute":
			
			return new Build(
					arg, 											//Kit
					null, 											  //Sword
					null, 											//Axe
					null, 											//Secondary
					null, 											//Passive A
					null, 											//Passive B
					null, 											//Global Passive C
					12);
			
		case "Knight":
			return new Build(
					arg, 
					null,
					null,
					null, 
					null, 
					null, 
					null, 
					12);

	
		case "Mechanist":
			return new Build(
					arg, 
					null, 
					null, 
					null, 
					null, 
					null, 
					null, 
					12);		
			
		case "Mage":
			return new Build(
					arg, 
					null,
					//skillManager.getSkill("Ice Prison").level(1),
					null,
					null, 
					null, 
					null, 
					null, 
					12);
			
		case "Ranger":
			return new Build(
					arg, 
					null, 
					null,
					null, 
					null, 
					null, 
					null, 
					12);

		case "Assassin":
			return new Build(
					arg, 
					null, 
					null,
					null, 
					null, 
					null, 
					null, 
					12);

			
		default:
			System.out.println("Critical Error in user.java defaultBuild(String arg)");
			return null;
		}	
		
		
		
	}
	
	//Database Grabbers
	/**
	 * 
	 * 
	 * @param uuid
	 * @param kit
	 * @param buildNumber
	 * @return ALWAYS NULL
	 */
	public static Build getBuildFromDatabase(UUID uuid, Kit kit, int buildNumber)
	{
		//System.out.println("ERROR CONNECTING TO DATABASE: Could not retrive class["+ kit + "] Build number [" + buildNumber + "] for player [" + uuid + "], sending a null build instead");
		return new Build(kit, null, null, null, null, null, null, 12);
	}

	
	/**
	 * 
	 * @param uuid
	 * @param kit
	 * @return ALWAYS 0
	 */
	public static int getActiveBuildFromDatabase(UUID uuid, Kit kit)
	{
		//System.out.println("ERROR CONNECTING TO DATABASE: Could not retrive active build for class["+ kit + "] for player [" + uuid + "], set to 0");
		return 0;
	}
}
