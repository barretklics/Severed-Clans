package me.barret.skill.Mage;



import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.barret.Champions;
import me.barret.cooldown.cooldownManager;
import me.barret.kits.Kit;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;

import me.barret.user.user;
import me.barret.utils.UtilEntity;

//Author 
public class FireBlast extends Skill implements interactSkill{
	private static Kit skillKit = kitManager.getKitFromString("Mage");
	
	private static SkillType skillType = SkillType.AXE;
	
	static String skillName = "Fire Blast";	
	
	static String[] description = {"You fucking","","throw FIRE BLAST"};	
	
	static int MaxLevel = 5;
	
	
    public FireBlast(Champions i)
    {
		super(i, skillKit, skillName, skillType, description, MaxLevel);

	}
	
	@Override
	public void activate(Player p, user u, int lvl) 
	{
		//TODO TOADD: CAN USE 5 THEN 6. INVIS TAG AND DISPLAY TAG
		if (cooldownManager.addCooldown(p, skillName + " " + lvl, 6- lvl, true)) {
			p.sendMessage("Whoosh");
			spawnGlowingBlock(p);
			
			
		}

	}


	private void spawnGlowingBlock(Player p) {
		Location loc = p.getLocation().add(0, 3, 0);
		loc.getBlock().setType(Material.BEDROCK);
		UtilEntity.createGlow(loc);
		
		
		
	}
	
	
	


}
