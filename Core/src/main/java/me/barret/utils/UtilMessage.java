package me.barret.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class UtilMessage {

	
	
	/**
	 * Use to send a basic message
	 * @param p Player to send message to
	 * @param prefix Message prefix
	 * @param message Message Content
	 */
	public static void sendBasic(Player p, String prefix, String message) {
		p.sendMessage(prefix + ChatColor.GRAY + message);
	}
	
	/**
	 * 
	 * @param p
	 * @param skillname
	 * @param remaining
	 */
	public static void cooldown(Player p, String skillname, double remaining)
	{
		p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You cannot use " + ChatColor.GREEN + skillname + ChatColor.GRAY + " for " + remaining + " seconds");
	}

	
	
	public static void recharge(Player p, String skillname) {
		p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You can use " + ChatColor.GREEN + skillname + ChatColor.GRAY + " again");
	}
	
	
	
	
	/**
	 * 
	 * @param p
	 * @param skillname
	 */
	public static void use(Player p, String skillname)
	{
		p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "]" + ChatColor.GRAY + " You used " + ChatColor.GREEN + skillname);
	}

	/**
	 * 
	 * @param p
	 * @param string
	 */
	public static void skill(Player p, String string)
	{
		p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "] " + ChatColor.GRAY + string);
	}	
	
	
	/**
	 * 
	 * @param p
	 * @param string
	 */
	public static void exhaust(Player p, String string)
	{
		p.sendMessage(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "Skill" + ChatColor.DARK_AQUA + "] " + ChatColor.GRAY + "you are too exhausted to use " + ChatColor.GREEN + string);
	}
}
