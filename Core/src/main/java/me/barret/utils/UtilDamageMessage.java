package me.barret.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class UtilDamageMessage {
    //author: Ethan Voraritskul 2/9/2023

    // me.barret.utils.UtilDamageMessage.damageMessage(p,damagedEntities,skillName);
    public static void damageMessage(Player p, ArrayList<Entity> damagedEntities, String skillName) {

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

        if (damagedEntities.contains(p) && damagedEntities.size() == 1) {
            p.sendMessage("You damaged yourself with " + skillName + ".");
            p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged yourself with " + ChatColor.GREEN + skillName);
            return;
        }

        if (!damagedEntities.contains(p) && damagedEntities.size() == 1) {
            Entity Entity1 = damagedEntities.get(0);
            if (Entity1 instanceof Player) {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged " + ((Player) Entity1).getDisplayName() + " with " + ChatColor.GREEN + skillName);
                return;
            }

            if (!(Entity1 instanceof Player)) {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + ChatColor.GREEN + skillName);
                return;
            }
        }


        if (damagedEntities.contains(p) && damagedEntities.size() == 2) {
            Entity Entity1 = damagedEntities.get(0);
            Entity Entity2 = damagedEntities.get(1);

            if (Entity1 != p && Entity2 == p) {
                if (Entity1 instanceof Player) {
                    p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged yourself and " + ((Player) Entity1).getDisplayName() + " with " + ChatColor.GREEN + skillName);
                    return;
                }

                if (!(Entity1 instanceof Player)) {
                    p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged yourself and"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + ChatColor.GREEN + skillName);
                    return;
                }
            } else if (Entity1 == p && Entity2 != p) {
                if (Entity2 instanceof Player) {

                    p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged yourself and " + ((Player) Entity2).getDisplayName() + " with " + ChatColor.GREEN + skillName);
                    return;
                }

                if (!(Entity2 instanceof Player)) {
                    p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged yourself and"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + ChatColor.GREEN + skillName);
                    return;
                }
            }

        }

        if (damagedEntities.contains(p) && damagedEntities.size() > 2) {
            p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged yourself and " + ChatColor.GREEN + (damagedEntities.size() - 1) + ChatColor.GRAY + " other entities with " + ChatColor.GREEN + skillName);
            return;
        }

        if (!damagedEntities.contains(p) && damagedEntities.size() == 2) {
            Entity Entity1 = damagedEntities.get(0);
            Entity Entity2 = damagedEntities.get(1);

            if (Entity1 instanceof Player && Entity2 instanceof Player) {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged " + ((Player) Entity1).getDisplayName() + " and " + ((Player) Entity2).getDisplayName() + " with " + ChatColor.GREEN + skillName);
                return;
            }

            if (!(Entity1 instanceof Player) && (Entity2 instanceof Player)) {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged " + ((Player) Entity2).getDisplayName() + " and"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + ChatColor.GREEN + skillName);
                return;
            }

            if ((Entity1 instanceof Player) && !(Entity2 instanceof Player)) {
                //You damaged"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with "
               // p.sendMessage(" You damaged " + ((Player) Entity1).getDisplayName() + " and"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + skillName + ".");
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged " + ((Player) Entity1).getDisplayName() + " and"+ aSlashAnParser.aSlashAnPlusName(Entity1) +"with " + ChatColor.GREEN + skillName);
                return;
            }
            if (!(Entity1 instanceof Player) && !(Entity2 instanceof Player)) {
                p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged" + aSlashAnParser.aSlashAnPlusName(Entity1)+"and"+aSlashAnParser.aSlashAnPlusName(Entity2)+"with " + ChatColor.GREEN + skillName);
                return;
            }
        }

        if (!damagedEntities.contains(p) && damagedEntities.size() > 2) {
            p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You damaged " + ChatColor.GREEN + damagedEntities.size() + ChatColor.GRAY+ " entities with " + ChatColor.GREEN + skillName);
        }
    }
}
