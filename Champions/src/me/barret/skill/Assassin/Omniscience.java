package me.barret.skill.Assassin;
import me.barret.Champions;
import me.barret.events.TickUpdateEvent;
import me.barret.kits.Kit;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.utils.localGlow;
import me.barret.user.user;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;

public class Omniscience extends Skill implements interactSkill {

    private static HashMap<Player, Long> timeOfGlow = new HashMap<Player, Long>();

    private static HashMap<Player, Boolean> glowActivated = new HashMap<Player, Boolean>();
    private static HashMap<Player, Integer> playerLevel = new HashMap<Player, Integer>();

    private static Kit skillKit = kitManager.getKitFromString("Assassin");        //sets class for skill to assassin

    private static SkillType skillType = SkillType.SWORD;                        //sets skill type to sword skill

    static String skillName = "Omniscience";

    static String[] description = {"See all. Know all."};

    static int MaxLevel = 3;

    public Omniscience(Champions i) {
        super(i, skillKit, skillName, skillType, description, MaxLevel);

    }


    @Override
    public void activate(Player p, user u, int lvl)  //activate occurs on right click - lvl is ability level selected in ench table
    {
        p.sendMessage("Activated Skill");
        playerLevel.put(p, lvl);
        doOmniscience(p, lvl);
    }

    public void doOmniscience(Player p, int lvl) {

        p.sendMessage("trying to add glow");
        localGlow.addGlow(p, lvl);
        timeOfGlow.put(p, System.currentTimeMillis());
        glowActivated.put(p, true);
        p.sendMessage("post glow line in code");


    }

    @EventHandler
    public void OnTickUpdate(TickUpdateEvent e) {
        for (Player p : timeOfGlow.keySet()) {
            if (glowActivated.get(p)) {
                if (System.currentTimeMillis() >= timeOfGlow.get(p) + 2000 + 1000 * playerLevel.get(p)) {

                    localGlow.removeLocalGlow(p, playerLevel.get(p));
                    glowActivated.put(p, false);
                }
            }
        }
    }
}
