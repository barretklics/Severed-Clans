package me.barret.skill.Mechanist;

import me.barret.Champions;
import me.barret.cooldown.cooldownManager;
import me.barret.events.TickUpdateEvent;
import me.barret.kits.Kit;
import me.barret.kits.kitManager;
import me.barret.skill.Skill;
import me.barret.skill.interactSkill;
import me.barret.user.user;
import me.barret.utils.UtilTeam;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class GlueGlob extends Skill implements interactSkill{
    private static Kit skillKit = kitManager.getKitFromString("Mechanist"); 		//sets class for skill to assassin

    private static Skill.SkillType skillType = SkillType.AXE; 						//sets skill type to axe skill

    static String skillName = "Glue Glob";

    static String[] description = {"Globbed"};

    static int MaxLevel = 5;


    static

    Slime slime;
    long spawnTime;
    long detonateTime;

    long glowSwapTime;
    boolean glowing;
    boolean hasBounced;
    Vector initVec;

    boolean grounded;

    public GlueGlob(Champions i){

        super(i,skillKit, skillName, skillType, description, MaxLevel);
    }
    public void activate(Player p, user u, int lvl){
        if (cooldownManager.addCooldown(p, skillName + " " + lvl, 7 - lvl, true)){
            throwSlime(p, lvl);
        }
    }

    public void throwSlime(Player p, int l) {
        //Remove old slime
        if (slime != null){
            slime.setHealth(0);
        }





        Location loc = p.getLocation().clone();
        loc.setY(loc.getY() + 1);

        Slime s = (Slime) p.getWorld().spawnEntity(loc, EntityType.SLIME);
        s.setSize(0);
        s.setCustomNameVisible(true);
        s.setCustomName("Fat whore");

        s.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 255, true));
        //s.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 255, true));

        s.setVelocity(p.getLocation().getDirection().normalize().multiply(2));

        s.setInvulnerable(true);
        UtilTeam.addColor((Entity) s, ChatColor.YELLOW);


        this.spawnTime = System.currentTimeMillis();
        this.detonateTime = System.currentTimeMillis() + 3000;
        this.glowSwapTime = System.currentTimeMillis();
        this.slime = s;

        initVec = s.getVelocity().clone();
        grounded = false;
        hasBounced = false;

    }

    @EventHandler
    private void checkSlime(TickUpdateEvent e){
        if (slime == null) return;

        if (grounded) slime.setAI(false);
        else grounded = slime.isOnGround();


        if (System.currentTimeMillis() >= detonateTime) {

            slime.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, slime.getLocation(), 3);
            slime.getWorld().playSound(slime.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
            slime.setHealth(0);
            slime = null;
            return;
        }


        long percentAlive = spawnTime / detonateTime;

        if (System.currentTimeMillis() > glowSwapTime + 3000 * percentAlive){
            glowSwapTime = System.currentTimeMillis();
            glowing = !(glowing);
            slime.setGlowing(glowing);

        }



        if (!(grounded)){


            if (hasBounced) return;

            BlockFace face = (collideBlock((Entity) slime));

            if (face == BlockFace.UP) return;
            System.out.println("Boing");


            Vector v = slime.getVelocity().clone();


            System.out.println("Fit Face: " + face.toString());

            if ((face == BlockFace.NORTH) || (face == BlockFace.SOUTH)){
                v.setZ(v.getZ()*-1);
            }
            else if ((face == BlockFace.EAST) || (face == BlockFace.WEST)){
                v.setX(v.getX()*-1);
            }

            slime.setVelocity(v);
            hasBounced = true;
        }



    }

    private BlockFace collideBlock(Entity e){
        for (double i = -0.6; i < 0.6; i+= 0.1){
            for (double k = -0.6; k < 0.6; k+= 0.1){
                Location loc = e.getLocation();
                loc.add(i, 0, k);
                BlockFace face = getBlockFace(loc);
                if (!(loc.getBlock().isEmpty())) return face;
            }
        }
        return BlockFace.UP;
    }

    private BlockFace getBlockFace(Location l){
        BlockFace face = BlockFace.UP;

        int x = l.getBlockX();
        int z = l.getBlockZ();

        double xDist = x - slime.getLocation().getBlockX();

        double zDist = z - slime.getLocation().getBlockZ();

            if (xDist < 0) face = BlockFace.EAST;//
            if (xDist > 0) face = BlockFace.WEST;//
            if (zDist < 0) face = BlockFace.SOUTH;//
            if (zDist > 0) face = BlockFace.NORTH;//
        return face;
    }



    private RayTraceResult getRay(Entity e){
        RayTraceResult ray = e.getWorld().rayTraceBlocks(e.getLocation(), e.getVelocity().normalize(), 5);
        if (ray == null) return null;

        double distance = ray.getHitBlock().getLocation().distance(e.getLocation());
        System.out.println("Distance: " + distance);
        if (distance <= 5){
            return ray;
        }

        return null;
        //ray.getHitBlockFace();
        //
    }


    @EventHandler
    private void damageCancel(EntityDamageEvent e){
        if (e.getEntity() == slime){
            e.setCancelled(true);
        }
    }

}
