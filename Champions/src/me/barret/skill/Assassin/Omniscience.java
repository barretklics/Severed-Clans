package me.barret.skill.Assassin;
import me.barret.Champions;
import me.barret.events.TickUpdateEvent;
import me.barret.kits.Kit;
import me.barret.kits.kitChangeEvent;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.user.userManager;
import me.barret.utils.localGlow;
import me.barret.user.user;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import java.util.HashMap;

public class Omniscience extends Skill implements interactSkill {

    private static HashMap<Player, Long> timeOfGlow = new HashMap<Player, Long>(); //used to remove glow from players after 2 + lvl seconds
    private static HashMap<Player, Boolean> glowActivated = new HashMap<Player, Boolean>(); //used to check whether glow is currently active
    private static HashMap<Player, Integer> playerLevel = new HashMap<Player, Integer>();
    private static HashMap<Player, Long> timeAtLastActivation = new HashMap<Player,Long>(); //used for 20ms internal cooldown preventing double clicks by accident

    private static HashMap<Player, Long> timeAtLastGlow = new HashMap<Player,Long>(); //used for cooldown

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
       if (timeAtLastActivation.get(p) + 20 <= System.currentTimeMillis())
       {
           playerLevel.put(p,lvl);
           doOmniscience(p, lvl);
           timeAtLastActivation.put(p, System.currentTimeMillis());
       }
    }

    public void doOmniscience(Player p, int lvl) {

        if((timeAtLastGlow.get(p) + (2000 + lvl) + (20000 - lvl*2000) <= System.currentTimeMillis()))
        {
            p.sendMessage("For " + (2 + lvl) + " seconds, all players within 15 blocks will be revealed to you.");
            timeOfGlow.put(p, System.currentTimeMillis());
            glowActivated.put(p, true);
            timeAtLastGlow.put(p, System.currentTimeMillis());
        }
        else
        {
            p.sendMessage("Omniscience is still on cooldown for "+ ((timeAtLastGlow.get(p) + (2000 + lvl) + (20000 - lvl*2000) - System.currentTimeMillis())/1000) + " more seconds");
        }



    }
    @EventHandler
    public void glowAdder(TickUpdateEvent e) {
        for (Player p : timeOfGlow.keySet()) {
            if (glowActivated.get(p)) {
                if (System.currentTimeMillis() <= timeOfGlow.get(p) + 2000 + 1000 * playerLevel.get(p)) {

                    localGlow.addGlow(p, playerLevel.get(p));
                }
            }
        }
    }


    @EventHandler
    public void glowRemover(TickUpdateEvent e) {
        for (Player p : timeOfGlow.keySet()) {
            if (glowActivated.get(p)) {
                if (System.currentTimeMillis() >= timeOfGlow.get(p) + 2000 + 1000 * playerLevel.get(p)) {

                    localGlow.removeLocalGlow(p, playerLevel.get(p));
                    glowActivated.put(p, false);
                    p.sendMessage("What was revealed is now hidden again.");
                }
            }
        }
    }

    @EventHandler // WILL BE CALLED every time kit is changed.
    //this is going to listen to spigot api's event handler which barret used to make a custom event buildchange
    private void onBuildChange(kitChangeEvent e) //needs new event
    {
        Player p = e.getPlayer(); //spigot
        user u = userManager.getUser(p.getUniqueId()); //barret user type calls spigot api to get players uuid

        if (u.getCurrentSkills().getSword().getName() == skillName)// checks if player build contains axe skill called skillname ("Orthogonal")
        {
            timeAtLastActivation.put(p, System.currentTimeMillis());
            timeAtLastGlow.put(p,(long)0);

        }
    }

}
