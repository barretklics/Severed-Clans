package me.barret.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UtilDamageMessage {
    //author: Ethan Voraritskul 2/9/2023

    // ArrayList<Entity> damagedEntities = new ArrayList<Entity>() {};
    // damagedEntities.add(Entity);
    // me.barret.utils.UtilDamageMessage.damageMessage(p,damagedEntities,skillName);

    public static void damageMessage(Player p, ArrayList<Entity> damagedEntities, String skillName, String verbOrPhrase) {

        if (damagedEntities.size() == 0) {

            double randomNumber = Math.random() * 10;

            if ((int)randomNumber == 69)
            {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " Your " + ChatColor.GREEN + skillName + ChatColor.GRAY + " fell useless. Like you." + ChatColor.RED + ChatColor.BOLD + " worthless.");
            }
            else {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " Your " + ChatColor.GREEN + skillName + ChatColor.GRAY + " fell useless.");
                return;
            }
        }


        if (damagedEntities.contains(p) && damagedEntities.size() == 3) {

        }

        if (!damagedEntities.contains(p) && damagedEntities.size() == 3) {

        }

        if (damagedEntities.contains(p) && damagedEntities.size() == 1) {
            p.sendMessage("You damaged yourself with " + skillName + ".");
            p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " yourself with " + ChatColor.GREEN + skillName);
            return;
        }

        if (!damagedEntities.contains(p) && damagedEntities.size() == 1) {
            Entity Entity1 = damagedEntities.get(0);
            if (Entity1 instanceof Player) {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " " + ((Player) Entity1).getDisplayName() + " with " + ChatColor.GREEN + skillName);
                return;
            }

            if (!(Entity1 instanceof Player)) {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + ChatColor.GREEN + skillName);
                return;
            }
        }


        if (damagedEntities.contains(p) && damagedEntities.size() == 2) {
            Entity Entity1 = damagedEntities.get(0);
            Entity Entity2 = damagedEntities.get(1);

            if (Entity1 != p && Entity2 == p) {
                if (Entity1 instanceof Player) {
                    p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " yourself and " + ((Player) Entity1).getDisplayName() + " with " + ChatColor.GREEN + skillName);
                    return;
                }

                if (!(Entity1 instanceof Player)) {
                    p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " yourself and"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + ChatColor.GREEN + skillName);
                    return;
                }
            } else if (Entity1 == p && Entity2 != p) {
                if (Entity2 instanceof Player) {

                    p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " yourself and " + ((Player) Entity2).getDisplayName() + " with " + ChatColor.GREEN + skillName);
                    return;
                }

                if (!(Entity2 instanceof Player)) {
                    p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " yourself and"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + ChatColor.GREEN + skillName);
                    return;
                }
            }

        }

        if (damagedEntities.contains(p) && damagedEntities.size() > 2) {

            ArrayList<Entity> withoutYourself = (ArrayList<Entity>) damagedEntities.clone();
            withoutYourself.remove(p);
            Boolean homogeneous = true;
            EntityType typeof0 = withoutYourself.get(0).getType();
            Entity homogenousEntity = withoutYourself.get(0);

            for (int i = 0; i< withoutYourself.size();i++)
            {
                if(typeof0 != withoutYourself.get(i).getType())
                {
                    homogeneous = false;
                }
            }

            if (homogeneous)
            {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " yourself and " + ChatColor.GREEN + (damagedEntities.size() - 1) + " " + UtilEntityTypeColor.getColorFromEntity(homogenousEntity) + UtilEntityPluralReturner.correctEntityPlural(homogenousEntity) + ChatColor.GRAY + " with " + ChatColor.GREEN + skillName);
                return;
            }

            p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " yourself and " + ChatColor.GREEN + (damagedEntities.size() - 1) + ChatColor.GRAY + " other entities with " + ChatColor.GREEN + skillName);
            return;
        }



        if (!damagedEntities.contains(p) && damagedEntities.size() == 2) {
            Entity Entity1 = damagedEntities.get(0);
            Entity Entity2 = damagedEntities.get(1);

            if (Entity1 instanceof Player && Entity2 instanceof Player) {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " " + ((Player) Entity1).getDisplayName() + " and " + ((Player) Entity2).getDisplayName() + " with " + ChatColor.GREEN + skillName);
                return;
            }

            if (!(Entity1 instanceof Player) && (Entity2 instanceof Player)) {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " " + ((Player) Entity2).getDisplayName() + " and"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + ChatColor.GREEN + skillName);
                return;
            }

            if ((Entity1 instanceof Player) && !(Entity2 instanceof Player)) {
                //You damaged"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with "
               // p.sendMessage(" You damaged " + ((Player) Entity1).getDisplayName() + " and"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + skillName + ".");
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " " + ((Player) Entity1).getDisplayName() + " and"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + ChatColor.GREEN + skillName);
                return;
            }
            if (!(Entity1 instanceof Player) && !(Entity2 instanceof Player)) {

                Boolean homogeneous = true;
                EntityType typeof0 = damagedEntities.get(0).getType();
                Entity homogenousEntity = damagedEntities.get(0);

                for (int i = 0; i< damagedEntities.size();i++)
                {
                    if(typeof0 != damagedEntities.get(i).getType())
                    {
                        homogeneous = false;
                    }
                }


                if (homogeneous)
                {
                    p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " " + ChatColor.GREEN + damagedEntities.size() + " " + UtilEntityTypeColor.getColorFromEntity(homogenousEntity) + UtilEntityPluralReturner.correctEntityPlural(homogenousEntity) + ChatColor.GRAY + " with " + ChatColor.GREEN + skillName);
                    return;
                }


                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + aSlashAnParser.aSlashAnPlusName(Entity1)+"and"+aSlashAnParser.aSlashAnPlusName(Entity2)+"with " + ChatColor.GREEN + skillName);
                return;
            }
        }


        if (!damagedEntities.contains(p) && damagedEntities.size() > 2) {


            Boolean homogeneous = true;
            EntityType typeof0 = damagedEntities.get(0).getType();
            Entity homogenousEntity = damagedEntities.get(0);

            for (int i = 0; i< damagedEntities.size();i++)
            {
                if(typeof0 != damagedEntities.get(i).getType())
                {
                    homogeneous = false;
                }
            }


            if (homogeneous)
            {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " " + ChatColor.GREEN + damagedEntities.size() + " " + UtilEntityTypeColor.getColorFromEntity(homogenousEntity) + UtilEntityPluralReturner.correctEntityPlural(homogenousEntity) + ChatColor.GRAY + " with " + ChatColor.GREEN + skillName);
                return;
            }

            p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You " + verbOrPhrase + " " + ChatColor.GREEN + damagedEntities.size() + ChatColor.GRAY+ " entities with " + ChatColor.GREEN + skillName);
            return;
        }
    }
}
