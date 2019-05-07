

interface Task {
	
	public void task();
	
}

public class WaitAction extends Action implements Task {
	
	float waitTime; 
	
	public WaitAction(float timeStarted, float waitTime) {
		super(timeStarted);
		this.waitTime = waitTime;
	} 
	
	@Override
	public void run(float currentTime) {
		if (completed) {
			return;
		}
		if (currentTime - timeStarted >= waitTime) {
			completed = true;
			task();
		}  
	} 

	@Override
	public void task() {
		// TODO Auto-generated method stub
		
	}
		
}
