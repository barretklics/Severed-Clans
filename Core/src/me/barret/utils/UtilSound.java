package me.barret.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class UtilSound {
	/**
	 * 
	 * @param s
	 * @param p
	 * @param loudness
	 * @param octave
	 */
	public static void playSoundToPlayer(Player p, Sound s, int loudness, int octave) {
		p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, loudness, octave);
	}
}
