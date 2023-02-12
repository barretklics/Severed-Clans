package me.barret.utils;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;

public class UtilEntityPluralReturner {

    // Allay!, Axolotl!, Bat!, Bee!, Blaze!, Camel!, Cat!, CaveSpider!, Chicken!, Cod!, Cow!, Creeper!, Dolphin!, Donkey!, Drowned!, ElderGuardian!,
// EnderDragon!, Enderman!, Endermite!, Evoker!, Fox!, Frog!, Ghast!, Giant!, GlowSquid!, Goat!, Guardian!, Hoglin!, Horse!, Husk!, Illusioner!, IronGolem!, Llama!,
// MagmaCube!, Mule!, MushroomCow!, Ocelot!, Panda!, Parrot!, Phantom!, Pig!, Piglin!, PiglinBrute!, ZombifiedPiglin!, Pillager!, Player!, PolarBear!, PufferFish!, Rabbit!, Ravager!
// Salmon!, Sheep!, Shulker!, Silverfish!, Skeleton!, SkeletonHorse!, Slime!, Snowman!, Spider!, Squid!, Stray!, Strider!, Tadpole!, TraderLlama!, TropicalFish!, Turtle!, Vex!, Villager!,
// Vindicator!, WanderingTrader!, Warden!, Witch!, Wither!, WitherSkeleton!, Wolf!, Zoglin!, Zombie!, ZombieHorse!, ZombieVillager!
    public static String correctEntityPlural(Entity e)
    {
        String correctPlural = e.getName();
        EntityType entityType = e.getType();

        ArrayList<EntityType> exceptions = new ArrayList<EntityType>(Arrays.asList(EntityType.COD,EntityType.DROWNED,EntityType.ENDERMAN,EntityType.PUFFERFISH,EntityType.SALMON,EntityType.SHEEP,EntityType.SILVERFISH,EntityType.SNOWMAN,EntityType.TROPICAL_FISH,EntityType.VEX,EntityType.WITCH,EntityType.WOLF));
        if (exceptions.contains(entityType))
        {
            if(entityType.equals(EntityType.COD))
            {
                return "Cod";
            }
            if(entityType.equals(EntityType.DROWNED))
            {
                return "Drowned";
            }
            if(entityType.equals(EntityType.ENDERMAN))
            {
                return "Endermen";
            }
            if(entityType.equals(EntityType.PUFFERFISH))
            {
                return "Pufferfish";
            }
            if(entityType.equals(EntityType.SALMON))
            {
                return "Salmon";
            }
            if(entityType.equals(EntityType.SHEEP))
            {
                return "Sheep";
            }
            if(entityType.equals(EntityType.SILVERFISH))
            {
                return "Silverfish";
            }
            if(entityType.equals(EntityType.SNOWMAN))
            {
                return "Snowmen";
            }
            if(entityType.equals(EntityType.TROPICAL_FISH))
            {
                return "Tropical Fish";
            }
            if(entityType.equals(EntityType.VEX))
            {
                return "Vexes";
            }
            if(entityType.equals(EntityType.WITCH))
            {
                return "Witches";
            }
            if(entityType.equals(EntityType.WOLF))
            {
                return "Wolves";
            }

        }
            return correctPlural + "s";

    }
}
