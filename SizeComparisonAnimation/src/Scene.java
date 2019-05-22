import java.util.ArrayList;

import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.cinematic.events.CinematicEventListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.ChaseCamera;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.opencl.Event;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Sphere;

public class Scene extends SimpleApplication {
	
	ArrayList<Geometry> geos = new ArrayList<Geometry>();
	
	Cinematic cinematic;
	
	float currentTime = 0; 
	float updatedTime = 0;
	
	boolean camSequenceStarted;
	
	Spatial targetSpatial;
	
	CameraNode motionCam;
		
	@Override
	public void simpleInitApp() {
		//flyCam.setEnabled(false);
		//flyCam.setMoveSpeed(5);
		setUpScene();
	}
	
	void renderInWireframe() {
		RenderState renderState = new RenderState();
		renderState.setWireframe(true);
		getRenderManager().setForcedRenderState(renderState);
	}
	
	Geometry createSphere(float radius, Vector3f position) {
		Sphere s = new Sphere(30, 30, radius);
		Geometry geo = new Geometry("Sphere", s);
		geo.setLocalTranslation(position);
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.Blue); 
		geo.setMaterial(mat);
		
		return geo;
	}
		
	public void setUpScene() {
		renderInWireframe();
		
		float xOffset = 3;
		float yOffset = -1;
		
		geos.add(createSphere(1, new Vector3f(0, 1 + yOffset, -1f))); 
		geos.add(createSphere(1.5f, new Vector3f(1f + 1.5f + xOffset, 1.5f + yOffset, -1.5f))); 
		geos.add(createSphere(3f, new Vector3f(geos.get(1).getLocalTranslation().x + 1.5f + 3f + xOffset, 3f + yOffset, -3f))); 
		geos.add(createSphere(4f, new Vector3f(geos.get(2).getLocalTranslation().x + 3f + 4f + xOffset, 4f + yOffset, -4f))); 
		geos.add(createSphere(6f, new Vector3f(geos.get(3).getLocalTranslation().x + 4f + 6f + xOffset, 6f + yOffset, -6f))); 
		geos.add(createSphere(10f, new Vector3f(geos.get(4).getLocalTranslation().x + 6f + 10f + xOffset, 10f + yOffset, -10f))); 
		geos.add(createSphere(20f, new Vector3f(geos.get(5).getLocalTranslation().x + 10f + 20f + xOffset, 20f + yOffset, -20f))); 
		geos.add(createSphere(25f, new Vector3f(geos.get(6).getLocalTranslation().x + 20f + 25f + xOffset, 25f + yOffset, -22f))); 

		
		for (Geometry geo : geos) {
			rootNode.attachChild(geo);
		}
					
		setUpCinematic();
		
	} 	
	
	void setUpCinematic() {
		
		// Camera Cinematic
		
		cinematic = new Cinematic(rootNode, geos.size());
		
		motionCam = cinematic.bindCamera("Motion Camera", cam);		
		
		motionCam.rotate(0, (float) Math.PI, 0);
		
		motionCam.setEnabled(true);
		
		MotionPath path = new MotionPath();

		path.addWayPoint(motionCam.getWorldTranslation());
		
		for (int i = 0; i < geos.size(); i++) {
			path.addWayPoint(new Vector3f(geos.get(i).getWorldTranslation().x, geos.get(i).getWorldTranslation().y, geos.get(i).getWorldTranslation().z + geos.get(i).getLocalScale().z / 2 + 5));
		}
		
		MotionEvent event = new MotionEvent (motionCam, path);
		cinematic.addCinematicEvent(0, event);
												
		cinematic.activateCamera(geos.size(), "Motion Camera");
		
		stateManager.attach(cinematic);
		
		rootNode.attachChild(motionCam);
		
		cinematic.play();
		
		CinematicEventListener cel = new CinematicEventListener() {
			  public void onPlay(CinematicEvent cinematic) {
			    System.out.println("play");
			  }

			  public void onPause(CinematicEvent cinematic) {
				cinematic.pause(); 
			    System.out.println("pause");
			  }

			  public void onStop(CinematicEvent cinematic) {
			    System.out.println("stop");
			  }
		};
		cinematic.addListener(cel);


	}
	
	@Override
	public void simpleUpdate(float tpf) {		
				
		System.out.println(motionCam.getWorldTranslation().z);
		
		currentTime += tpf;
		
		for (Geometry geo : geos) {
			geo.rotate(0, 0.1f, 0);
		}
		
	}
	
//	public static void main(String[] args) {
//		Scene app = new Scene();
//		app.start();
//	}

}
