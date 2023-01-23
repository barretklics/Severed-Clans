package me.barret.kits;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import me.barret.utils.UtilArmor;
public class Kit {

	protected String Name;
	protected String Prefix;
	
	protected Material helmet;
	protected Material chestplate;
	protected Material leggings;
	protected Material boots;

	
	

	
	public Kit(String ClassName, String ClassPrefix, Material _helmet, Material _chestplate,Material _leggings,Material _boots)
	{
		Name = ClassName;
		Prefix = ClassPrefix;
		helmet = _helmet;
		chestplate = _chestplate;
		leggings = _leggings;
		boots = _boots;
		
		kitManager.addKit(this);

	//	System.out.println("Loaded kit: " + Name);
	}
	
	public String getKitName()
	{
		return Name;
	}
	
	
	public String getKitPrefix()
	{
		return Prefix;
	}	
	
	
	public Material getKitHelmet()
	{
		return helmet;
	}
	
	public Material getKitChestplate()
	{
		return chestplate;
	}	
	
	public Material getKitLeggings()
	{
		return leggings;
	}
	
	public Material getKitBoots()
	{
		return boots;
	}
	
	
	
	
	/**
	 * @Author barret
	 * 
	 * @return Kit Object from current player armor
	 */
	public static Kit getKit(Player p)
	{
		
		Material helmet = UtilArmor.getHelmet(p);
		Material chestplate = UtilArmor.getChestplate(p);
		Material leggings = UtilArmor.getLeggings(p);
		Material boots = UtilArmor.getBoots(p);
		

		for (Kit k : kitManager.getKits()) 
		{
			if (helmet == k.getKitHelmet() & chestplate == k.getKitChestplate() & leggings == k.getKitLeggings() & boots == k.getKitBoots())
			{
				return k;
			}
		}
		
		
		//No kit, assign naked if possible
		for (Kit k : kitManager.getKits()) {
			if (k.getKitName().equalsIgnoreCase("naked")) {
				return k;
			}
				
		}

		return null;
	
	}
	
	
}
