
/*
package me.barret.skill.nms;

import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.pathfinder.PathFinder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R2.CraftWorld;
import net.minecraft.world.entity.EntityType;
public class BombSlime extends Slime{
    public BombSlime(World world, Location l){
       // super((EntityType<? extends Slime>) EntityType, ((CraftWorld) world).getHandle());
        super(EntityType.SLIME, ((CraftWorld) world).getHandle());

        for (WrappedGoal g: this.goalSelector.getAvailableGoals()){
            this.goalSelector.removeGoal(g.getGoal());
        }

        this.setPos(l.getX(), l.getY(), l.getZ());

    }


}
*/