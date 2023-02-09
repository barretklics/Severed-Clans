package me.barret.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.Arrays;

import me.barret.utils.UtilEntityTypeColor;

import static me.barret.utils.UtilEntityTypeColor.getColorFromEntity;

public class aSlashAnParser {
    //Author Ethan Voraritskul 2/9/2023

    public static String aSlashAnPlusName(Entity e)
    {
        String entityName = e.getName();

        char firstLetter = entityName.charAt(0);

        ArrayList<Character> vowels = new ArrayList<Character>(Arrays.asList('a','e','i','o','u','A','E','I','O','U')) {};

        String returnedString;
        if (vowels.contains(firstLetter))
        {
            returnedString = " an " + getColorFromEntity(e) + entityName + ChatColor.GRAY+ " ";
        }
        else
        {
            returnedString = " a " + getColorFromEntity(e) + entityName + ChatColor.GRAY+ " ";
        }
        return returnedString;

    }
}
