package me.barret.skill.Assassin;

import me.barret.Champions;
import me.barret.build.Build;
import me.barret.events.TickUpdateEvent;
import me.barret.kits.Kit;
import me.barret.kits.kitChangeEvent;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.user.userManager;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import me.barret.user.user;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class PoisonArrow extends Skill
{
    private static Kit skillKit = kitManager.getKitFromString("Assassin"); 		//sets class for skill to assassin

    private static SkillType skillType = SkillType.BOW; 						//sets skill type to bow skill

    static String skillName = "Poison Arrow";

    static String[] description = {"Prepare a poison arrow. Your next arrow will", "cause a hit player to receive poison."};

    static int MaxLevel = 3;

    private static HashMap<Player, Boolean> isPrepared = new HashMap<Player, Boolean>();
    private static HashMap<Player, Long> timeAtLastPrepareAttempt = new HashMap<Player, Long>(); //20ms prepare cooldown (prevents double activation chat spam)
    private static HashMap<Player, Boolean> particleTrailActive = new HashMap<Player, Boolean>();

    private static HashMap<Player, Boolean> preparedArrowFlying = new HashMap<Player, Boolean>();

    private static HashMap<Player, Long> timeAtLastPrepare = new HashMap<Player, Long>();

    private static HashMap<Player, Integer> playerLevel = new HashMap<Player, Integer>();

    private static HashMap<Player, Arrow> firedArrow = new HashMap<Player, Arrow>();

    public PoisonArrow(Champions i)
    {
        super(i, skillKit, skillName, skillType, description, MaxLevel);

    }



    @EventHandler
    public void prepareArrow(PlayerInteractEvent e) {

        Player p = e.getPlayer();

        user u = userManager.getUser(p.getUniqueId());

        Build build = u.getCurrentBuild();
        //p.sendMessage(""+build.getKit()); //returns Assassin@23u1y23123
        //p.sendMessage(""+build.getBow()); // PoisonArrow@g232s12ghj3123
       // p.sendMessage(""+build.getBow().getLevel()); // 0

       // p.sendMessage(""+build.getBuildSkillFromSkill(build.getBow()).getLevel());
        //add if statement to detect if user is both assassin and has right skill

     //   p.sendMessage("outside prepare checks");

    //    p.sendMessage("u.getCurrentSkills().getBow().getLevel(): " + u.getCurrentSkills().getBow().getLevel());


//add check for player skill, bugged out earlier, ask barret


             if (p.getInventory().getItemInMainHand().getType().equals(Material.BOW))
             {
                 //p.sendMessage("its a bow");
                 if ((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK))
                 {
                     //p.sendMessage("its a left click");

                    // p.sendMessage("Attempting prepare");
                    if (System.currentTimeMillis() >= timeAtLastPrepareAttempt.get(p) + 20)
                    {
                       // p.sendMessage("it was less than 20ms, moving on to cooldown check");
                    if (timeAtLastPrepare.get(p) + 25000 - (playerLevel.get(p) * 1000) <= System.currentTimeMillis()) //25s - lvl cooldown
                    {
                        p.sendMessage("You prepare a poison arrow.");
                        p.getWorld().playSound(p.getLocation(), Sound.ENCHANT_THORNS_HIT,(float)1.8,(float)1.8);
                        isPrepared.put(p, true);
                        timeAtLastPrepareAttempt.put(p, System.currentTimeMillis()); //20ms cooldown
                        timeAtLastPrepare.put(p, System.currentTimeMillis()); // real ability cooldown
                        //p.spawnParticle();
                        //p.playSound();
                        //p.sendMessage();
                    } else {
                        p.sendMessage("You cannot prepare a poison arrow for another " + (timeAtLastPrepare.get(p) + (25000 - playerLevel.get(p) * 1000) - System.currentTimeMillis()) / 1000 + " seconds");
                    }
                } //else p.sendMessage("hasn't been 20ms");
            }
        }
    }

    @EventHandler
    public void fireArrow(EntityShootBowEvent event)
    {
        Entity e = event.getEntity();
        if (e instanceof Player)
        {
        if (isPrepared.get(e))
            {

                Player p = (Player) e;
                p.getWorld().playSound(p.getLocation(), Sound.ENTITY_CAMEL_DASH_READY,(float)1.8,(float)1.8);
                firedArrow.put(p, (Arrow) event.getProjectile());
                isPrepared.put(p,false);
                particleTrailActive.put(p,true);
                preparedArrowFlying.put(p,true);
            }
        }
    }

    @EventHandler
    public void particleTrace(TickUpdateEvent e)
    {
        if (!particleTrailActive.isEmpty()) {
            for (Player p : particleTrailActive.keySet()) {
                if (particleTrailActive.get(p))
                {
                Arrow arrow = firedArrow.get(p);
                    Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(0, 255, 0), Color.fromRGB(255, 255, 255), 1.0F); //green fade to white
                   p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, arrow.getLocation(), 50, 0,0,0,0,dustTransition,true);
                   //spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force)

                }
            }
        }
    }

    @EventHandler
    public void arrowHit(ProjectileHitEvent e)
    {
        if (e.getEntity().getShooter() instanceof Player)
        {
            Player p = (Player) e.getEntity().getShooter();
            if(preparedArrowFlying.get(p))
            {

                if (e.getHitBlock() != null)
                {
                    particleTrailActive.put(p,false);
                    preparedArrowFlying.put(p,false);
                    p.sendMessage("Your poison arrow falls useless, failing to strike a target");
                }
                else if (e.getHitEntity() != null)
                {

                    ((LivingEntity) e.getHitEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 120 - playerLevel.get(p)*20,   playerLevel.get(p) - 1)); //
                    particleTrailActive.put(p,false);
                    preparedArrowFlying.put(p,false);
                    
                    e.getHitEntity().getWorld().playSound(p.getLocation(), Sound.ENTITY_FOX_SPIT,(float)1.8,(float)0.8);
                    e.getHitEntity().getWorld().spawnParticle(Particle.SNEEZE, e.getHitEntity().getLocation(), 50); //summons exploding green particle on hit

                    if (e.getHitEntity() instanceof Player) {

                        p.sendMessage("Your poison arrow strikes " + e.getHitEntity().getName()+", giving them poison " + (playerLevel.get(p)) + " for " + (6 - playerLevel.get(p)) + " seconds.");
                    }else p.sendMessage("Your poison arrow strikes a " + e.getHitEntity().getName()+", giving it poison " + (playerLevel.get(p)) + " for " + (6 - playerLevel.get(p)) + " seconds.");

                }



            }
         }
    }



    @EventHandler
    public void onBuildChange(kitChangeEvent e)
    {
        Player p = e.getPlayer();
        user u = userManager.getUser(p.getUniqueId());

        timeAtLastPrepareAttempt.put(p,(long)0); //20ms cd
        isPrepared.put(p,false);
        particleTrailActive.put(p,false);
        preparedArrowFlying.put(p,false);
        timeAtLastPrepare.put(p, (long)0); // real ability cooldown


       playerLevel.put(p,3); //change to variable once user.X.getBow().getLevel() is fixed

        //u.getCurrentSkills().getBow().getLevel(); returns 0 ....


    }

}
