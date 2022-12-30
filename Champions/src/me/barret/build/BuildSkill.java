package me.barret.build;

import me.barret.skill.Skill;

public class BuildSkill {

	Skill s;
	int level;
	
	public BuildSkill(Skill s, int level) {
		this.s = s;
		this.level = level;
	}
	
	public void increment() {
		if (this.level < this.s.getMaxLevel()) this.level++;
	}
	
	public void decrement() {
		if (this.level > 0) this.level--;
	}
	
	public Skill getSkill() {
		return this.s;
	}
	
	public int getLevel() {
		return this.level;
	}
	
	
	public void setSkill(Skill s) {
		this.s = s;
	}
	
	public void setLevel(int i) {
		this.level = i;
	}
	
}
