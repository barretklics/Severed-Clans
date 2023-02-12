package me.barret.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.Arrays;

public class UtilEntityTypeColor {
    //Author Ethan Voraritskul 2/9/2023

    public static ChatColor getColorFromEntity(Entity e) {

        if (e instanceof LivingEntity) {
            EntityType entityType = e.getType();

            //manually pruned 1.19.3 livingEntity list (removed general types like AbstractHorse..)
            // "!" attached to entity name means I have put it in some chatColor ArrayList.

// Allay!, Axolotl!, Bat!, Bee!, Blaze!, Camel!, Cat!, CaveSpider!, Chicken!, Cod!, Cow!, Creeper!, Dolphin!, Donkey!, Drowned!, ElderGuardian!,
// EnderDragon!, Enderman!, Endermite!, Evoker!, Fox!, Frog!, Ghast!, Giant!, GlowSquid!, Goat!, Guardian!, Hoglin!, Horse!, Husk!, Illusioner!, IronGolem!, Llama!,
// MagmaCube!, Mule!, MushroomCow!, Ocelot!, Panda!, Parrot!, Phantom!, Pig!, Piglin!, PiglinBrute!, ZombifiedPiglin!, Pillager!, Player!, PolarBear!, PufferFish!, Rabbit!, Ravager!
// Salmon!, Sheep!, Shulker!, Silverfish!, Skeleton!, SkeletonHorse!, Slime!, Snowman!, Spider!, Squid!, Stray!, Strider!, Tadpole!, TraderLlama!, TropicalFish!, Turtle!, Vex!, Villager!,
// Vindicator!, WanderingTrader!, Warden!, Witch!, Wither!, WitherSkeleton!, Wolf!, Zoglin!, Zombie!, ZombieHorse!, ZombieVillager!

            //manually sorted and colorized by Ethan

            ArrayList<EntityType> aquaList = new ArrayList<>(Arrays.asList(EntityType.ALLAY,EntityType.GLOW_SQUID,EntityType.ILLUSIONER));
            ArrayList<EntityType> blackList = new ArrayList<>(Arrays.asList(EntityType.BAT,EntityType.WITHER,EntityType.WITHER_SKELETON));
            ArrayList<EntityType> blueList = new ArrayList<>(Arrays.asList(EntityType.PHANTOM,EntityType.STRAY,EntityType.VEX));
            ArrayList<EntityType> darkAquaList = new ArrayList<>(Arrays.asList(EntityType.DOLPHIN,EntityType.DROWNED,EntityType.GUARDIAN,EntityType.TROPICAL_FISH,EntityType.VINDICATOR,EntityType.WARDEN));
            ArrayList<EntityType> darkBlueList = new ArrayList<>(Arrays.asList(EntityType.PARROT,EntityType.SQUID,EntityType.TRADER_LLAMA,EntityType.WANDERING_TRADER));
            ArrayList<EntityType> darkGrayList = new ArrayList<>(Arrays.asList(EntityType.CAT,EntityType.COD,EntityType.COW,EntityType.DONKEY,EntityType.HORSE,EntityType.MULE,EntityType.PILLAGER,EntityType.RAVAGER,EntityType.TADPOLE,EntityType.VILLAGER,EntityType.ELDER_GUARDIAN,EntityType.SILVERFISH));
            ArrayList<EntityType> darkGreenList = new ArrayList<>(Arrays.asList(EntityType.CAVE_SPIDER,EntityType.FROG,EntityType.GIANT,EntityType.TURTLE,EntityType.ZOMBIE,EntityType.ZOMBIE_HORSE,EntityType.ZOMBIE_VILLAGER));
            ArrayList<EntityType> darkPurpleList = new ArrayList<>(Arrays.asList(EntityType.ENDER_DRAGON,EntityType.ENDERMAN,EntityType.ENDERMITE,EntityType.SHULKER,EntityType.WITCH));
            ArrayList<EntityType> darkRedList = new ArrayList<>(Arrays.asList(EntityType.MAGMA_CUBE,EntityType.MUSHROOM_COW,EntityType.SPIDER));
            ArrayList<EntityType> goldList = new ArrayList<>(Arrays.asList(EntityType.BLAZE,EntityType.CAMEL,EntityType.EVOKER,EntityType.FOX,EntityType.LLAMA,EntityType.PIGLIN_BRUTE,EntityType.PUFFERFISH));
            ArrayList<EntityType> grayList = new ArrayList<>(Arrays.asList());//generally don't use gray, as it is the same as the chat color before it. looks bad.
            ArrayList<EntityType> greenList = new ArrayList<>(Arrays.asList(EntityType.CREEPER,EntityType.SLIME));
            ArrayList<EntityType> lightPurpleList = new ArrayList<>(Arrays.asList(EntityType.AXOLOTL,EntityType.PIG,EntityType.ZOGLIN));
            ArrayList<EntityType> redList = new ArrayList<>(Arrays.asList(EntityType.HOGLIN,EntityType.PIGLIN,EntityType.ZOMBIFIED_PIGLIN,EntityType.SALMON,EntityType.STRIDER));
            ArrayList<EntityType> whiteList = new ArrayList<>(Arrays.asList(EntityType.CHICKEN,EntityType.GHAST,EntityType.GOAT,EntityType.PANDA,EntityType.PLAYER,EntityType.POLAR_BEAR,EntityType.RABBIT,EntityType.SHEEP,EntityType.SNOWMAN,EntityType.IRON_GOLEM,EntityType.SKELETON,EntityType.SKELETON_HORSE,EntityType.WOLF));
            ArrayList<EntityType> yellowList = new ArrayList<>(Arrays.asList(EntityType.BEE,EntityType.HUSK,EntityType.OCELOT));
/*
                    ChatColor.AQUA
                    ChatColor.BLACK
                    ChatColor.BLUE
                    ChatColor.DARK_AQUA
                    ChatColor.DARK_BLUE
                    ChatColor.DARK_GRAY
                    ChatColor.DARK_GREEN
                    ChatColor.DARK_PURPLE
                    ChatColor.DARK_RED
                    ChatColor.GOLD
                    ChatColor.GRAY
                    ChatColor.GREEN
                    ChatColor.LIGHT_PURPLE
                    ChatColor.RED
                    ChatColor.WHITE
                    ChatColor.YELLOW
*/

            if (aquaList.contains(entityType))
            {
                return ChatColor.AQUA;
            }
            else if (blackList.contains(entityType))
            {
                return ChatColor.BLACK;
            }
            else if (blueList.contains(entityType))
            {
                return ChatColor.BLUE;
            }
            else if (darkAquaList.contains(entityType))
            {
                return ChatColor.DARK_AQUA;
            }
            else if (darkBlueList.contains(entityType))
            {
                return ChatColor.DARK_BLUE;
            }
            else if (darkGrayList.contains(entityType))
            {
                return ChatColor.DARK_GRAY;
            }
            else if (darkGreenList.contains(entityType))
            {
                return ChatColor.DARK_GREEN;
            }
            else if (darkPurpleList.contains(entityType))
            {
                return ChatColor.DARK_PURPLE;
            }
            else if (darkRedList.contains(entityType))
            {
                return ChatColor.DARK_RED;
            }
            else if (goldList.contains(entityType))
            {
                return ChatColor.GOLD;
            }
            else if (grayList.contains(entityType))
            {
                return ChatColor.GRAY;
            }
            else if (greenList.contains(entityType))
            {
                return ChatColor.GREEN;
            }
            else if (lightPurpleList.contains(entityType))
            {
                return ChatColor.LIGHT_PURPLE;
            }
            else if (redList.contains(entityType))
            {
                return ChatColor.RED;
            }
            else if (whiteList.contains(entityType))
            {
                return ChatColor.WHITE;
            }
            else if (yellowList.contains(entityType))
            {
                return ChatColor.YELLOW;
            }

        }
        return ChatColor.GRAY; //Default
    }


}
