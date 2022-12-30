package me.barret.cooldown;

import java.util.UUID;

public class Cooldown {

	
	private String tag;
	private long beginTime;
	private long duration; //duration in seconds
	
//	private boolean resetOnDeath;
	private boolean notifyUsable;
	
	//Notify user when cooldown fails
	private boolean showFail = true;
	
	
	
	/**
	 * 
	 * 
	 * @param name
	 * @param duration
	 * @param notifyUsable
	 */
	public Cooldown(String name, long duration, boolean notifyUsable) {
		this.tag = name;
		this.duration = duration;
		this.notifyUsable = notifyUsable;
		
		this.beginTime = System.currentTimeMillis();
	}
	


	/**
	 * 
	 * @return if cooldown is passed
	 */
	public boolean isPassed() {
		return (System.currentTimeMillis() - beginTime >= duration*1000);
	}
	
	/**
	 * 
	 * @returnremaining cooldown time in seconds
	 */
	public double getRemainingTime() {
		return (double) ((duration*1000) -(System.currentTimeMillis() - beginTime))/1000;
	}
	
	
	public boolean notifyUsable() {
		return notifyUsable;
	}
	
	
	public String getName() {
		return this.tag;
	}
	
	
	public boolean showFail() {
		return this.showFail;
	}
	
}
