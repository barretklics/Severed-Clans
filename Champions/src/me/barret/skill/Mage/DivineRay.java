package me.barret.skill.Mage;

import me.barret.skill.Skill;
import me.barret.skill.channelSkill;

import java.util.HashMap;

import org.bukkit.*;
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

			Location l = p.getLocation().clone();
			Location initialLoc = p.getLocation();
			Vector v = l.getDirection().clone();
			v.multiply(15 + lvl); //multiplies location vector by 5. is only used to set facing direction later
			l.add(v);

			Location eyelocation = p.getEyeLocation();
			BlockIterator blocksToAddFoot = new BlockIterator(eyelocation, -1, 15 + lvl); //15 in here represents 15 blocks + lvl (lvl 5 is 20 block tp)
			Location blockToAdd = eyelocation;
			while (blocksToAddFoot.hasNext() && blockToAdd.getBlock().isPassable()) {
				blockToAdd = blocksToAddFoot.next().getLocation();

				if (blockToAdd.getBlock().isPassable() && blockToAdd.add(0, 1, 0).getBlock().isPassable())
				{
					p.spawnParticle(Particle.FLAME, blockToAdd.add(0, 1, 0), 0,l.getDirection().getX() ,l.getDirection().getY(),l.getDirection().getZ(), 10);
					//spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data)


				}
			}
		}
	}
}

