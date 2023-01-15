package me.barret.skill.Assassin;
import me.barret.Champions;
import me.barret.kits.Kit;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.utils.addLocalGlow;
import me.barret.user.user;
import org.bukkit.entity.Player;

public class Omniscience extends Skill implements interactSkill {

    private static Kit skillKit = kitManager.getKitFromString("Assassin"); 		//sets class for skill to assassin

    private static SkillType skillType = SkillType.SWORD; 						//sets skill type to sword skill

    static String skillName = "Omniscience";

    static String[] description = {"See all. Know all."};

    static int MaxLevel = 3;
    public Omniscience(Champions i)
    {
        super(i, skillKit, skillName, skillType, description, MaxLevel);

    }


    @Override
    public void activate(Player p, user u, int lvl)  //activate occurs on right click - lvl is ability level selected in ench table
    {
        p.sendMessage("Activated Skill");
        doOmniscience(p,lvl);
    }

    void doOmniscience(Player p, int lvl)
    {
        p.sendMessage("trying to add glow");
        addLocalGlow.addGlow(p);
        p.sendMessage("post glow line in code");
    }
}
