package me.barret.kits.listeners;

import me.barret.kits.kitChangeEvent;
import me.barret.kits.type.Assassin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class AssassinListener implements Listener {

    @EventHandler
    private void doSpeed(kitChangeEvent e){
        //Assasin speed 1
        if (e.getNewKit() instanceof Assassin){
            e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));

        }
        else{
            e.getPlayer().removePotionEffect(PotionEffectType.SPEED);
        }
    }
}
