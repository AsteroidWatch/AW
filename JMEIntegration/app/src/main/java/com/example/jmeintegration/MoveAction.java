package com.example.jmeintegration;

import com.example.jmeintegration.Action;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;

public class MoveAction extends Action {

	public Camera target;
	public Geometry to;
	public float duration;

	public Vector3f destination;

	public Vector3f vectorNormalized;
	
	public MoveAction(Camera target, Geometry to, float duration, float timeStarted) {
		super(timeStarted);
		this.target = target;
		this.to = to;
		this.duration = duration;
		
		destination = to.getLocalTranslation().add(0, 0, to.getWorldScale().z * 5);
		vectorNormalized = destination.subtract(target.getLocation()).normalizeLocal();
	}
	
	@Override
	public void run(float currentTime) {
		if (completed) {
			return;
		}
						
		Vector3f vector = destination.subtract(target.getLocation());
						
		//System.out.println(vectorNormalized.length() < vector.length()); 
		
		if (vectorNormalized.length() < vector.length()) {
			target.setLocation(target.getLocation().add(vectorNormalized));
		} else {
			target.setLocation(destination);
			completed = true;
		}
	}
	
}
