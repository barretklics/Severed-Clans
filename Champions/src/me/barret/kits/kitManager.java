package me.barret.kits;

import java.util.HashSet;
import java.util.Set;

import me.barret.Champions;
import me.barret.kits.type.Assasin;
import me.barret.kits.type.Brute;
import me.barret.kits.type.Knight;
import me.barret.kits.type.Mage;
import me.barret.kits.type.Mechanist;
import me.barret.kits.type.Naked;
import me.barret.kits.type.Ranger;

public class kitManager {
	Champions plugin;
	
	
    public static Set<Kit> kits = new HashSet<>(); //kits that are loaded in
	
	
	public kitManager(Champions plugin)
	{
		this.plugin = plugin;
		
	    new Assasin();
	    new Brute();
	    new Knight();
	    new Mage();
	    new Mechanist();
	    new Ranger();
	    //new Naked();
	}
	
	public static void addKit(Kit k) {
		kits.add(k);
	}
	
	public static Set<Kit> getKits(){
		return kits;
	}
	
	
	public static Kit getKitFromString(String kitname) {
		for (Kit k : kits) {
			if (k.getKitName().contains(kitname))
				return k;
		}
		return null;
	}
	
	
}