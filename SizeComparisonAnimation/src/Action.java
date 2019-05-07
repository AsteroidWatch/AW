

public abstract class Action {

	float timeStarted;
	
	boolean completed = false;
	
	public Action(float timeStarted) {
		this.timeStarted = timeStarted; 
	} 
	
	public void run(float currentTime) {  
		
	}  
	
}
