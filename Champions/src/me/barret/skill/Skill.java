package me.barret.skill;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import me.barret.Champions;
import me.barret.ChampionsListener;
import me.barret.kits.Kit;
import me.barret.utils.UtilInventory;
import me.barret.utils.UtilItem;

public class Skill extends ChampionsListener<Champions>{
	
	public enum SkillType 
	{
		SWORD(0), AXE(1), BOW(2), PASSIVE_A(3), PASSIVE_B(4), PASSIVE_GLOBAL(5);
		
        private int i;

        SkillType(int i)
        {
            this.i = i;
        }
        public int toInt()
        {
            return i;
        }
        
        
        private static Map<Integer, SkillType> map = new HashMap<Integer, SkillType>();
        static {
        	for (SkillType type : SkillType.values()) {
        		map.put(type.toInt(), type);
        	}
        }
        
        public static SkillType valueOf(int i) {
        	return map.get(i);
        }
        
        
    }

	
	
	
	Kit kit;
	String name;
	String[] description;
	SkillType type;
	int level;
	int maxLevel;
	private int levelCost; //for tokens
	
	/**
	 * 
	 * 
	 * @param instance
	 * @param kitType
	 * @param name
	 * @param description
	 * @param maxlevel
	 */
	public Skill(Champions instance, Kit kitType, String skillName, SkillType skillType, String[] description, int maxlevel) {
		super(instance);
		this.kit = kitType;
		this.name = skillName;
		this.description = description;
		this.maxLevel = maxlevel;
		
		this.type = skillType;
			
	}
	
	public Skill level(int level)
	{
		this.level = level;
		return this;
	}
	

	public Skill getSkill() //Returns a whole skill
	{
		return this;
	}
	
	public Kit getKit() //Returns a kit ex. KitType.BRUTE, KitType.Knight, KitType.Assasin
	
	{
		return kit;
	}
	
	
	public SkillType getType() // returns skill type ex SkillType.SWORD, SkillType.AXE
	{
		return type;
	}

	public String getName()
	{
		return this.name;
	}
	
	public ArrayList<String> getDescription()
	{
		return new ArrayList<String>(Arrays.asList(description));
		//return description;
	}
	
	public int getLevelCost() {
		return this.levelCost;
	}
	
	
	
	
	
	
		
	public int getLevel()
	{
		return level;
	}
	
	
	public int getMaxLevel()
	{
		return maxLevel;
	}
	
	public void setLevel(int i) {
		this.level = i;
	}
	
	
	

}
