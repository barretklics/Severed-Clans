package me.barret.skill.Assassin;

import me.barret.Champions;
import me.barret.build.BuildChangeEvent;
import me.barret.cooldown.cooldownManager;
import me.barret.events.TickUpdateEvent;
import me.barret.kits.Kit;
import me.barret.kits.kitChangeEvent;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.user.userManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import me.barret.user.user;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;

public class PoisonArrow extends Skill
{
    private static Kit skillKit = kitManager.getKitFromString("Assassin"); 		//sets class for skill to Assassin

    private static SkillType skillType = SkillType.BOW; 						//sets skill type to bow skill

    static String skillName = "Poison Arrow";

    static String[] description = {"Prepare a poison arrow. Your next arrow will", "poison an enemy"};

    static int MaxLevel = 3;

    private static HashMap<Player, Boolean> isPrepared = new HashMap<Player, Boolean>();
    private static HashMap<Player, Long> timeAtLastPrepareAttempt = new HashMap<Player, Long>(); //20ms prepare cooldown (prevents double activation chat spam)
    private static HashMap<Player, Boolean> particleTrailActive = new HashMap<Player, Boolean>();

    private static HashMap<Player, Boolean> preparedArrowFlying = new HashMap<Player, Boolean>();

    private static HashMap<Player, Integer> playerLevel = new HashMap<Player, Integer>();

    private static HashMap<Player, Arrow> firedArrow = new HashMap<Player, Arrow>();

    public PoisonArrow(Champions i)
    {
        super(i, skillKit, skillName, skillType, description, MaxLevel);

    }

    // playerLevel.put(p,3); //HARDCODED 3 BECAUSE BOW SKILLS DONT RETURN LEVEL PROPERLY FOR SOME REASON?
    //IN ON BUILD CHANGE AT BOTTOM OF THIS FILE

    @EventHandler
    public void prepareArrow(PlayerInteractEvent e) {

        Player p = e.getPlayer();
        //add if statement to detect if user is both ranger and has right skill

        //   p.sendMessage("outside prepare checks");
        user u = userManager.getUser(p.getUniqueId());
        if (userManager.getUser(p.getUniqueId()).getCurrentBuild() == null || userManager.getUser(p.getUniqueId()).getCurrentBuild().getBow() ==null||!userManager.getUser(p.getUniqueId()).getCurrentBuild().getBow().getName().equalsIgnoreCase(skillName)) {
            return;
        }

        if (p.getInventory().getItemInMainHand().getType().equals(Material.BOW))
        {
            if ((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK))
            {
                if (System.currentTimeMillis() >= timeAtLastPrepareAttempt.get(p) + 20) //20ms internal cd on all skills
                {
                    if (cooldownManager.addCooldown(p, skillName, 20 - playerLevel.get(p), true))
                    {
                        p.getWorld().playSound(p.getLocation(), Sound.ENCHANT_THORNS_HIT,(float)1.8,(float)1.8);
                        isPrepared.put(p, true);
                        timeAtLastPrepareAttempt.put(p, System.currentTimeMillis()); //20ms cooldown
                    }
                }
            }
        }
    }

    @EventHandler
    public void fireArrow(EntityShootBowEvent event)
    {
        if (isPrepared.isEmpty() || event.getEntityType() != EntityType.PLAYER)
        {
            return;
        }
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

                    //change particle or particle color here

                    Arrow arrow = firedArrow.get(p);
                    Particle.DustTransition dustTransition = new Particle.DustTransition(Color.fromRGB(0, 255, 0), Color.fromRGB(255, 255, 255), 1.0F); //green fade to white (poison)
                    p.getWorld().spawnParticle(Particle.DUST_COLOR_TRANSITION, arrow.getLocation(), 50, 0,0,0,0,dustTransition,true);
                    //spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force)

                }
            }
        }
    }

    @EventHandler
    public void onArrowInWater(TickUpdateEvent e) {
        if (!particleTrailActive.isEmpty()) {
            for (Player p : particleTrailActive.keySet()) {
                if (particleTrailActive.get(p)) {
                    Arrow arrow = firedArrow.get(p);
                    if (arrow.getLocation().getBlock().equals(Material.WATER) || arrow.getLocation().getBlock().equals(Material.WATER_CAULDRON));
                    {
                        //do nothing
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArrowInLava(TickUpdateEvent e) {
        if (!particleTrailActive.isEmpty()) {
            for (Player p : particleTrailActive.keySet()) {
                if (particleTrailActive.get(p)) {
                    Arrow arrow = firedArrow.get(p);
                    if (arrow.getLocation().getBlock().equals(Material.LAVA) || arrow.getLocation().getBlock().equals(Material.LAVA_CAULDRON));
                    {
                        //do nothing
                    }
                }
            }
        }
    }

    @EventHandler
    public void onArrowHit(ProjectileHitEvent e)
    {
        if (e.getEntity().getShooter() instanceof Player)
        {
            Player p = (Player) e.getEntity().getShooter();
            user u = userManager.getUser(p.getUniqueId());
            if(u.getCurrentBuild()!=null && u.getCurrentBuild().getBow()!=null && u.getCurrentBuild().getBow().getName().equalsIgnoreCase(skillName)&&!preparedArrowFlying.isEmpty() && preparedArrowFlying.containsKey(p)&& preparedArrowFlying.get(p))
            {

                if (e.getHitBlock() != null)
                {
                    Location hitBlockLocation = e.getHitBlock().getLocation();

                    hitBlockCosmetics(p,hitBlockLocation); //does cosmetics if hits block
                    doBlockHitEffect(p,hitBlockLocation); //does something to hit entity or surroundings if arrow hits block (ie poisons them or explodes them)

                    particleTrailActive.put(p,false);
                    preparedArrowFlying.put(p,false);
                }
                else if (e.getHitEntity() != null)
                {
                    LivingEntity hitEntity = (LivingEntity) e.getHitEntity();
                    Location hitLocation = hitEntity.getLocation();

                    hitEntityCosmetics(p,hitEntity, hitLocation); //does cosmetics if hits entity
                    doEntityHitEffect(p,hitEntity,hitLocation); //does something to hit entity or surroundings if arrow hits entity (ie poisons them or explodes them)

                    particleTrailActive.put(p,false);
                    preparedArrowFlying.put(p,false);



                }

            }
        }
    }

    public void doEntityHitEffect(Player p, LivingEntity hitEntity, Location hitLocation)
    {

        //do something if arrow hits entity

        hitEntity.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 120 - playerLevel.get(p)*20,   playerLevel.get(p) - 1)); //poison effect on hit entity
        p.sendMessage("level: "+playerLevel.get(p));

        //call damage message here

        ArrayList<Entity> damagedEntities = new ArrayList<Entity>() {};
        damagedEntities.add(hitEntity);
        me.barret.utils.UtilDamageMessage.damageMessage(p,damagedEntities,skillName,"poisoned");
    }

    public void doBlockHitEffect(Player p, Location blockHitLocation)
    {
        //do something if arrow hits block

        //call util damage message here

        ArrayList<Entity> damagedEntities = new ArrayList<Entity>() {};
        me.barret.utils.UtilDamageMessage.damageMessage(p,damagedEntities,skillName,"poisoned");

        //do nothing
    }

    public void hitEntityCosmetics(Player p, LivingEntity hitEntity, Location hitLocation)
    {
        //do cosmetic something if arrow hits entity


        p.getWorld().playSound(hitLocation, Sound.ENTITY_FOX_SPIT,(float)1.8,(float)0.8);
        p.getWorld().spawnParticle(Particle.SNEEZE, hitEntity.getLocation(), 50); //summons exploding green particle on hit

    }

    public void hitBlockCosmetics(Player p, Location hitBlockLocation)
    {
        //do cosmetic something if arrow hits block

        //do nothing
    }



    /**
     * Barret add null check and new event
     * @param e
     */
    @EventHandler
    public void onBuildChange(BuildChangeEvent e)
    {
        if (e.getNewBuild() == null) return;
        if (e.getNewBuild().getBow() == null) return;
        //ethan you need to check for name like (u.getCurrentBuild().getSword().getName() == skillName) thx
        Player p = e.getPlayer();
        user u = userManager.getUser(p.getUniqueId());

        timeAtLastPrepareAttempt.put(p,(long)0); //20ms cd
        isPrepared.put(p,false);
        particleTrailActive.put(p,false);
        preparedArrowFlying.put(p,false);


        playerLevel.put(p,3); //HARDCODED 3 BECAUSE BOW SKILLS DONT RETURN LEVEL PROPERLY FOR SOME REASON?

        //u.getCurrentSkills().getBow().getLevel(); returns 0 ....


    }

}
