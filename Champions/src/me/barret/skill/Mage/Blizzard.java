package me.barret.skill.Mage;


import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import me.barret.Champions;
import me.barret.energy.EnergyBar;
import me.barret.energy.energyManager;
import me.barret.kits.Kit;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.channelSkill;
import me.barret.user.user;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Blizzard extends Skill implements channelSkill{
	private static Kit skillKit = kitManager.getKitFromString("Mage");
		
	private static SkillType skillType = SkillType.SWORD;
	
	static String skillName = "Blizzard";	
	
	static String[] description = {"You fucking","","ice"};	
	
	static int MaxLevel = 5;
	
	
	private static HashMap<Projectile, UUID> snowballData = new HashMap<Projectile, UUID>();
	
	 public Blizzard(Champions i)
	 {
		super(i, skillKit, skillName, skillType, description, MaxLevel);

	}
		
	@Override
	public void channel(Player p, user u, int lvl) 
	{
		EnergyBar blizzardEnergy = energyManager.getEnergyBar(p, skillName, 20000, 500);
		if (blizzardEnergy.use(1000)) {
			
			//p.spigot().sendMessage(ChatMessageType.);
			snowball(p, lvl);
		}

	}
	
	public void snowball(Player p, int lvl) {
		for (int i = 0; i < 1 + lvl; i++)
		{
			
			
			Projectile snowball = p.launchProjectile(Snowball.class);
			
			
			double mod = 0.25 + 0.15*lvl;
			
			
			double x = (2 - Math.random());
			double y = (2 - Math.random());
			double z = (2 - Math.random());
			
			Vector vec = p.getEyeLocation().getDirection().normalize();
			
			vec.setX(vec.getX() * mod * x);
			vec.setY(vec.getY() * mod * y);
			vec.setZ(vec.getZ() * mod * z);
			
			
			snowball.setVelocity(vec);
			
			snowballData.put(snowball, snowball.getUniqueId());
		}
	}
		
		
		
	@EventHandler(priority = EventPriority.LOWEST)
	public void snowballHit(EntityDamageByEntityEvent e)
	{
		if (e.getCause() != DamageCause.PROJECTILE)
		{
			
			return;		
		}
		Projectile proj = (Projectile) e.getDamager();	
		if (proj == null)
		{
			return;
		}
		
		if (!(e.getEntity() instanceof LivingEntity))
		{
			return;
		}
		
		if (snowballData.containsKey(proj))
		{
			e.setCancelled(true);
			LivingEntity victim = (LivingEntity) e.getEntity();
				
			//Vector vec = UtilVelocity.dirFromTo(proj.getLocation(), victim.getLocation());
			Vector vec = proj.getVelocity();
			vec.multiply(0.1);
			vec.setY(0.2);
			victim.setVelocity(vec);
			
			snowballData.remove(proj);
			proj.remove();
		}
	}	
		
			
		
		
}
