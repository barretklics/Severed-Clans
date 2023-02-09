package me.barret.skill.Ranger;


import it.unimi.dsi.fastutil.Hash;
import me.barret.Champions;
import me.barret.build.BuildChangeEvent;
import me.barret.cooldown.cooldownManager;
import me.barret.events.TickUpdateEvent;
import me.barret.kits.Kit;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.user.user;
import me.barret.user.userManager;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class BinaryExplosive extends Skill implements interactSkill {
    private static Kit skillKit = kitManager.getKitFromString("Ranger");

    private static SkillType skillType = SkillType.AXE;

    static String skillName = "Binary Explosive";

    static String[] description = {"Throw out an explosive satchel which can be detonated with a followup shot."};

    static int MaxLevel = 3;


    public BinaryExplosive(Champions i) {
        super(i, skillKit, skillName, skillType, description, MaxLevel);

    }

    private static HashMap<Player, Integer> levelStorage = new HashMap<Player, Integer>();

    private static HashMap<Player, UUID> uuidStorage = new HashMap<Player, UUID>();
    private static HashMap<Player, Boolean> explosiveOnGround = new HashMap<Player, Boolean>();

    private static HashMap<Player, Long> timeOfLaunch = new HashMap<Player, Long>();
    private static HashMap<Player, Long> timeHitGround = new HashMap<Player, Long>();

    private static HashMap<Player, Boolean> explosiveLive = new HashMap<Player, Boolean>();
    private static HashMap<Player, Boolean> salmonDamagedByEntity = new HashMap<Player, Boolean>();

    private static HashMap<Player, Salmon> salmonStorage = new HashMap<Player, Salmon>();

    private static HashMap<Player, Long> internalCD = new HashMap<Player, Long>();

    @Override
    public void activate(Player p, user u, int lvl) { // MAKE THIS A CHARGE SKILL, MAKE VELOCITY SCALE WITH CHARGE

        if (internalCD.get(p) + 20 <=System.currentTimeMillis()) {
            if (cooldownManager.addCooldown(p, skillName, 20 - lvl, true)) {
                internalCD.put(p, System.currentTimeMillis());

                killSalmon(p);
                levelStorage.put(p, lvl);
                uuidStorage.put(p, p.getUniqueId());
                throwExplosive(p, u, lvl);
            }
        }
    }
    public void throwExplosive(Player p, user u, int lvl) {

        timeOfLaunch.put(p, System.currentTimeMillis());
        Salmon b = (Salmon) p.getWorld().spawnEntity(p.getEyeLocation().clone(), EntityType.SALMON, false);
        b.setFireTicks(1000);
        b.setCustomName(p.getUniqueId().toString());
        b.setCustomNameVisible(false);
        b.setVelocity(p.getLocation().getDirection().normalize().multiply(2));
        explosiveLive.put(p,true);
        explosiveOnGround.put(p, false);
    }

    @EventHandler
    public void betterProjectileDetector(TickUpdateEvent event)
    {
        if (salmonStorage.isEmpty())
        {
            return;
        }

        for (Player p: salmonStorage.keySet())
        {
            if (salmonStorage.get(p) == null)
            {
                return;
            }
            Salmon salmon = salmonStorage.get(p);
            BoundingBox salmonBox = salmon.getBoundingBox().expand(0.75);

            for(Entity e: p.getWorld().getNearbyEntities(salmonBox))
            {
                if (e instanceof Projectile)
                {
                    if (e instanceof Arrow)
                    {
                        if (((Arrow) e).isInBlock())
                        {
                            return;
                        }
                    }


                  //  p.sendMessage("better detector detected projectile");
                    salmonDamagedByEntity.put(p,true);
                    e.remove();
                }
            }

        }
    }

    @EventHandler
    public void exploder(TickUpdateEvent e) {
        if (levelStorage.isEmpty()) {
            return;
        }
        for (Player p : levelStorage.keySet()) {
            user u = userManager.getUser(p.getUniqueId());
            if (u.getCurrentBuild() == null || u.getCurrentBuild().getAxe() == null || !u.getCurrentBuild().getAxe().getName().equalsIgnoreCase(skillName)) {
                return;
            }

            if (explosiveLive.get(p)) {

                for (LivingEntity livingEntity : p.getWorld().getLivingEntities()) {
                    if (livingEntity instanceof Salmon && livingEntity.getCustomName().equalsIgnoreCase(p.getUniqueId().toString())) {
                        Salmon BinaryExplosive = (Salmon) livingEntity;
                        salmonStorage.put(p,BinaryExplosive);

                        if (System.currentTimeMillis() > timeOfLaunch.get(p) + 5000) {
                         //   p.sendMessage("Blew up after 6s");
                            BinaryExplosive.remove();
                            p.getWorld().createExplosion(BinaryExplosive.getLocation(), 4, false, false, p);

                            explosiveOnGround.put(p, false);
                            explosiveLive.put(p, false);
                            salmonStorage.put(p,null);
                            p.getWorld().spawnParticle(Particle.FLAME, BinaryExplosive.getLocation(), 150);
                            explosionEntityDamager(p,levelStorage.get(p),BinaryExplosive.getLocation());
                        }

                        if (explosiveOnGround.containsKey(p) && explosiveOnGround.get(p) && timeHitGround.containsKey(p) && timeHitGround.get(p) != 0 && (System.currentTimeMillis() > (timeHitGround.get(p) + 3000))) { //explodes 3s after ground
                         //   p.sendMessage("Blew up 3s after hit ground");
                            BinaryExplosive.remove();
                            p.getWorld().createExplosion(BinaryExplosive.getLocation(), 4, false, false, p);
                            explosiveOnGround.put(p, false);
                            explosiveLive.put(p, false);
                            salmonDamagedByEntity.put(p,false);
                            salmonStorage.put(p,null);
                            p.getWorld().spawnParticle(Particle.FLAME, BinaryExplosive.getLocation(), 150);
                            explosionEntityDamager(p,levelStorage.get(p),BinaryExplosive.getLocation());
                        }

                        if (!salmonDamagedByEntity.isEmpty() && salmonDamagedByEntity.containsKey(p) && salmonDamagedByEntity.get(p)) {
                           // p.sendMessage("Entity damage caused explosion");
                            //createExplosion(Location loc, float power, boolean setFire, boolean breakBlocks, Entity source)
                            BinaryExplosive.remove();
                            p.getWorld().createExplosion(BinaryExplosive.getLocation(), 4, false, false, p);
                            explosiveOnGround.put(p, false);
                            explosiveLive.put(p, false);
                            salmonDamagedByEntity.put(p,false);
                            salmonStorage.put(p,null);
                            p.getWorld().spawnParticle(Particle.FLAME, BinaryExplosive.getLocation(), 150);
                            explosionEntityDamager(p,levelStorage.get(p),BinaryExplosive.getLocation());
                        }

                        if (!explosiveOnGround.isEmpty() && explosiveOnGround.containsKey(p) && BinaryExplosive.isOnGround() && !explosiveOnGround.get(p)) {
                          //  p.sendMessage("hit ground");
                            explosiveOnGround.put(p, true);
                            timeHitGround.put(p, System.currentTimeMillis());
                            explosiveOnGround.put(p, true);
                            BinaryExplosive.setAI(false);
                        }
                    }
                }
            }
        }
    }

    public void explosionEntityDamager(Player p, int lvl, Location explosionLoc)
    {
        ArrayList<Entity> damagedEntities = new ArrayList<Entity>() {};
    for (Entity e: p.getWorld().getNearbyEntities(explosionLoc,4,4,4))
    {
        if (e instanceof LivingEntity) {

            if (e != p) {
                e.setFireTicks(lvl * 20);
                ((LivingEntity) e).damage(2 * lvl, p);
                damagedEntities.add(e);
            }
            else
            {
                e.setFireTicks(lvl  * 20);
                damagedEntities.add(p);
            }
        }

    }

    damageMessage(p,damagedEntities);

    }

    public void damageMessage(Player p, ArrayList<Entity> damagedEntities) {

        if (damagedEntities.size() == 0) {
            p.sendMessage("Your " + skillName + " exploded uselessly.");
            return;
        }

        if (damagedEntities.contains(p) && damagedEntities.size() == 1) {
            p.sendMessage("You damaged yourself with " + skillName + ".");
            return;
        }

        if (!damagedEntities.contains(p) && damagedEntities.size() == 1)
        {
            Entity Entity1 = damagedEntities.get(0);
            if (Entity1 instanceof Player) {
                p.sendMessage("You damaged " + ((Player) Entity1).getDisplayName() + " with " + skillName + ".");
                return;
            }

            if (!(Entity1 instanceof Player)) {
                p.sendMessage("You damaged a " + Entity1.getName() + " with " + skillName + ".");
                return;
            }
        }


        if (damagedEntities.contains(p) && damagedEntities.size() == 2) {
            Entity Entity1 = damagedEntities.get(0);
            Entity Entity2 = damagedEntities.get(1);

            if (Entity1 != p && Entity2 == p) {
                if (Entity1 instanceof Player) {
                    p.sendMessage("You damaged yourself and " + ((Player) Entity1).getDisplayName() + " with " + skillName + ".");
                    return;
                }

                if (!(Entity1 instanceof Player)) {
                    p.sendMessage("You damaged yourself and a " + Entity1.getName() + " with " + skillName + ".");
                    return;
                }
            } else if (Entity1 == p && Entity2 != p) {
                if (Entity2 instanceof Player) {
                    p.sendMessage("You damaged yourself and " + ((Player) Entity2).getDisplayName() + " with " + skillName + ".");
                    return;
                }

                if (!(Entity2 instanceof Player)) {
                    p.sendMessage("You damaged yourself and a " + Entity2.getName() + " with " + skillName + ".");
                    return;
                }
            }

        }

        if (damagedEntities.contains(p) && damagedEntities.size() > 2) {
            p.sendMessage("You damaged yourself and " + (damagedEntities.size() - 1) + " other entities with " + skillName + ".");
            return;
        }


        if (!damagedEntities.contains(p) && damagedEntities.size() == 2) {
            Entity Entity1 = damagedEntities.get(0);
            Entity Entity2 = damagedEntities.get(1);

            if (Entity1 instanceof Player && Entity2 instanceof Player) {
                p.sendMessage("You damaged " + ((Player) Entity1).getDisplayName() + " and " + ((Player) Entity2).getDisplayName() + " with " + skillName + ".");
                return;
            }

            if (!(Entity1 instanceof Player) && (Entity2 instanceof Player)) {
                p.sendMessage("You damaged " + ((Player) Entity2).getDisplayName() + " and a " + Entity1.getName() + " with " + skillName + ".");
                return;
            }

            if ((Entity1 instanceof Player) && !(Entity2 instanceof Player)) {
                p.sendMessage("You damaged " + ((Player) Entity1).getDisplayName() + " and a " + Entity2.getName() + " with " + skillName + ".");
                return;
            }
            if (!(Entity1 instanceof Player) && !(Entity2 instanceof Player)) {
                p.sendMessage("You damaged 2 non-player entities with " + skillName + ".");
                return;
            }
        }

        if (!damagedEntities.contains(p) && damagedEntities.size() > 2) {
            p.sendMessage("You damaged " + damagedEntities.size() + " entities with " + skillName + ".");
        }
    }


    public void killSalmon(Player p)
    {
    for (LivingEntity e: p.getWorld().getLivingEntities())
    {
        if (e instanceof Salmon)
        {
            if (e.getCustomName().equalsIgnoreCase(p.getUniqueId().toString()))
            {
                salmonStorage.put(p,null);
                e.remove();
            }
        }
    }
    }



    @EventHandler
    public void explosiveDamageDetector(EntityDamageEvent event)
    {

        if (uuidStorage.isEmpty())
        {
            return;
        }
        Entity e =  event.getEntity();
        for(Player p: uuidStorage.keySet())
        if(e instanceof Salmon && e.getCustomName().equalsIgnoreCase(uuidStorage.get(p).toString()))
        {
        EntityDamageEvent.DamageCause cause = event.getCause();

        event.setDamage(0.5);
        if (cause == EntityDamageEvent.DamageCause.FALL)
        {
          //  p.sendMessage("cancelled fall damage on explosive");
            event.setCancelled(true);
            return;
        }

            if (cause == EntityDamageEvent.DamageCause.FIRE || cause == EntityDamageEvent.DamageCause.FIRE_TICK)
            {
                //  p.sendMessage("cancelled fire damage on explosive");
                event.setCancelled(true);
                return;
            }

        if (cause == EntityDamageEvent.DamageCause.PROJECTILE)
        {
           // p.sendMessage("projectile hit explosive");
            salmonDamagedByEntity.put(p,true);

        }

            if (cause == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
            {
              //  p.sendMessage("melee hit explosive");
                salmonDamagedByEntity.put(p,true);
            }
        }
    }
    @EventHandler
    public void cosmetics(TickUpdateEvent e) {

        if (explosiveLive.isEmpty() || levelStorage.isEmpty()) {
            return;
        }

        for (Player p : levelStorage.keySet()) {
            user u = userManager.getUser(p.getUniqueId());
            if (u.getCurrentBuild() == null || u.getCurrentBuild().getAxe() == null || !u.getCurrentBuild().getAxe().getName().equalsIgnoreCase(skillName)) {
                return;
            }

            if (!explosiveLive.containsKey(p)) {
                return;
            }

            if (explosiveLive.get(p)) {

                for (LivingEntity livingEntity : p.getWorld().getLivingEntities()) {
                    if (livingEntity instanceof Salmon && livingEntity.getCustomName().equalsIgnoreCase(p.getUniqueId().toString())) {
                        Salmon BinaryExplosive = (Salmon) livingEntity;

                        Location explosiveLoc = BinaryExplosive.getLocation();

                        //PARTICLE HERE
                        //spawnParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force)

                       // Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(255, 255, 255), 1.0F);
                       // p.getWorld().spawnParticle(Particle.REDSTONE, explosiveLoc, 50, dustOptions);
                        p.getWorld().playSound(explosiveLoc, Sound.ENTITY_CAT_HISS, 0.4F,5.5F);

                    }
                }
            }
        }
    }



    @EventHandler
    public void onBuildChange(BuildChangeEvent e)
    {
        Player p = userManager.getUser(e.getPlayerUUID()).toPlayer();
        explosiveLive.put(p,false);
        explosiveOnGround.put(p,false);
        salmonDamagedByEntity.put(p,false);
        uuidStorage.put(p,p.getUniqueId());
        internalCD.put(p,(long)0);
    }


    }
