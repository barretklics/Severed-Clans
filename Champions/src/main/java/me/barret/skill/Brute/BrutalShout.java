package me.barret.skill.Brute;

import me.barret.Champions;
import me.barret.cooldown.cooldownManager;
import me.barret.kits.Kit;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.user.user;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class BrutalShout extends Skill implements interactSkill {

    private static Kit skillKit = kitManager.getKitFromString("Brute"); 		//sets class for skill to brute

    private static SkillType skillType = SkillType.AXE; 						//sets skill type to axe skill

    static String skillName = "Brutal Shout";

    static String[] description = {"Cripple your enemies with fear and enter a frenzy"," by shouting from the depths of your belly"};

    static int MaxLevel = 3;

    private static HashMap<Player, Long> timeAtLastActivateAttempt = new HashMap<Player,Long>(){};

    public BrutalShout(Champions i)
    {
        super(i, skillKit, skillName, skillType, description, MaxLevel);

    }

    @Override
    public void activate(Player p, user u, int lvl)
    {
        if (System.currentTimeMillis() >= timeAtLastActivateAttempt.get(p) + 20) //20ms internal cd on all skills
        {
            if (cooldownManager.addCooldown(p, skillName, 15 - lvl, true))
            {
                shout(p,lvl);
                timeAtLastActivateAttempt.put(p, System.currentTimeMillis()); //20ms cooldown
            }
        }
    }

    public void shout(Player p, int lvl)
    {

    }
    public ArrayList<Entity> entityGetter(Player p, int lvl)
    {

        //
        return null;

    }

}
