package com.example.jmeintegration;

public abstract class Action {

	public float timeStarted;
	
	public boolean completed = false;
	
	public Action(float timeStarted) {
		this.timeStarted = timeStarted; 
	} 
	
	public void run(float currentTime) {  
		
	}  
	
}
