import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.jme3.app.SimpleApplication;
import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.MotionPathListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;

public class SizeComparisonSequence extends SimpleApplication {

	Cinematic cinematic;
	CameraNode motionCam;
	ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
	
	ArrayList<MotionEvent> events = new ArrayList<MotionEvent>();
		
	public static void main(String[] args) {
		SizeComparisonSequence app = new SizeComparisonSequence();
		app.start();
	}
	
	ArrayList<Asteroid> getAsteroids(int count) {
		ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
		for (int i = 1; i < count + 1; i++) {
			Spatial spatial = assetManager.loadModel("asteroid" + i + ".obj");
			float size = (float) Math.pow(2, i);
			Geometry model = getGeometry(spatial);
			model.scale((float) Math.pow(2, i), (float) Math.pow(2, i), (float) Math.pow(2, i));
			Asteroid asteroid = new Asteroid("Asteroid " + i, size, model, assetManager);
			asteroids.add(asteroid);
		}
		return asteroids;
	}
		
	void renderInWireframe() {
		RenderState renderState = new RenderState();
		renderState.setWireframe(true);
		getRenderManager().setForcedRenderState(renderState);
	}
								
	@Override
	public void simpleInitApp() {
						
		cam.setFrustumFar(1999999999);
		
		settings.setFrameRate(30);
		
		settings.setVSync(true);
						
		flyCam.setEnabled(false);				
		
		rootNode.addLight(new AmbientLight()); 
				
		initiateSizeComparisonScene(); 
						
	} 
		
	void lineUpAsteroids() {
		
		for (Asteroid asteroid : asteroids) {
			rootNode.attachChild(asteroid.model);
			int previousIndex = asteroids.indexOf(asteroid) - 1;
			
			Asteroid previousAsteroid = previousIndex >= 0 ? asteroids.get(previousIndex) : null;
			
			Vector3f previousScale = previousAsteroid != null ? previousAsteroid.model.getWorldScale() : new Vector3f(0, 0, 0);
			Vector3f scale = asteroid.model.getWorldScale();
			
			Vector3f previousPosition = previousAsteroid != null ? previousAsteroid.model.getLocalTranslation() : new Vector3f(0, 0, 0);
			
			float combinedDiameters = previousScale.x + scale.x;
			
			asteroid.model.setLocalTranslation(previousPosition.x + previousScale.x + previousScale.x / 2 + scale.x / 2 + combinedDiameters / 2, (scale.y / 2) + (scale.y / 3), -scale.z / 2);
		}
		
	} 
	
	void addText() {
		for (Asteroid asteroid : asteroids) {
			asteroid.text.setLocalTranslation(asteroid.model.getLocalTranslation().add(-asteroid.model.getWorldScale().x * 1.5f, asteroid.model.getWorldScale().y * 1, 0));
			rootNode.attachChild(asteroid.text);
		}
	}
		
//	void setUpCamSequence() {
//		
//		int nAsteroidsToTraverse = asteroids.size();
//		
//		moveCamTo(asteroids.get(0).model.getLocalTranslation().add(0, 0, asteroids.get(0).model.getWorldScale().z * 5));
//		
//		MotionPath motionPath = new MotionPath();
//		
//		MotionEvent asteroidTraversing;
//		
//		asteroidTraversing = new MotionEvent(motionCam, motionPath);
//				
//		motionPath.addWayPoint(asteroids.get(0).model.getLocalTranslation().add(0, 0, asteroids.get(0).model.getWorldScale().z * 5));
//		
//		for (int i = 0 ; i < nAsteroidsToTraverse - 1; i++) {
//			
//			Geometry asteroidGeoNext = asteroids.get(i + 1).model;
//			
//			motionPath.addWayPoint(asteroidGeoNext.getLocalTranslation().add(0, 0, asteroidGeoNext.getWorldScale().z * 5));
//			
//		}
//		
//		asteroidTraversing.setInitialDuration(nAsteroidsToTraverse);
//		
//		motionPath.addListener(new MotionPathListener() {
//
//			@Override
//			public void onWayPointReach(MotionEvent arg0, int arg1) { 
//				System.out.println("Way point reached"); 
//				waitFor(3); 
//			} 
//			
//		});
//		
//		cinematic.addCinematicEvent(5, asteroidTraversing);
//		
//	}

	
	void moveCamTo(Vector3f vec) {
		motionCam.setLocalTranslation(vec);
	}
	
	void initiateSizeComparisonScene() {
		
		asteroids = getAsteroids(20);
		
//		float duration = 100;
//		
//		cinematic = new Cinematic(rootNode, duration);
						
		//motionCam = cinematic.bindCamera("cam", cam);
//								
//		motionCam.rotate(0, (float) Math.PI, 0);
						
		lineUpAsteroids();
		
		addText(); 
		
		startSequence();
				
		//setUpCamSequence(); 
		
//		stateManager.attach(cinematic); 
//						
//		cinematic.activateCamera(0, "cam");
														
		initKeys();
				
		//scheduledActions.add(new MoveAction(cam, geos.get(5), 1000));
		
//		cinematic.play();
		
	}
	
	ArrayList<Action> scheduledActions = new ArrayList<Action>();

	
	
	
	void startSequence() { 
		for (int i = 0; i < asteroids.size(); i++) { 
			Asteroid asteroid = asteroids.get(i); 
			scheduledActions.add(new MoveAction(cam, asteroid.model, 3, currentTime)); 
		} 
	} 
	
	float currentTime = 0;
	
	int index = 0;
	
	void attendActions(float tpf) { 
		
		if (scheduledActions.size() < 1 || index > scheduledActions.size() - 1) {
			return;
		}
		
		if (scheduledActions.get(index).completed == false) {
			scheduledActions.get(index).run(currentTime);
		} 
		
		if (scheduledActions.get(index).completed == true) {
			index += backwards ? -1 : 1;
			if (index < 0) {
				index = 0;
			}
		}
		
		System.out.println(index); 
		
	} 
	
	void reverseDirection() {
		for (int i = (backwards ? 1 : scheduledActions.size() - 2); backwards ? (i < scheduledActions.size()) : (i > 1); i += (backwards ? 1 : -1)) {
			Action action = scheduledActions.get(i);
			if ((MoveAction) action != null) {
				MoveAction moveAction = (MoveAction) action;
				
				moveAction.to = asteroids.get(i + (backwards ? -1 : 1)).model;
				moveAction.destination = moveAction.to.getLocalTranslation().add(0, 0, moveAction.to.getWorldScale().z * 5);
				moveAction.vectorNormalized = moveAction.destination.subtract(moveAction.target.getLocation()).normalizeLocal();
				 
			}
		}
	}
	
	@Override
	public void simpleUpdate(float tpf) {
		if (!pause) { 
			currentTime += tpf;
			
			attendActions(tpf);
		} 
		
		//System.out.println(motionCam.getLocalTranslation());
		
		//System.out.println(currentTime); 
//		
//		if (pause == false) {
//			for (Geometry geo : geos) {
//				geo.rotate(0, 0.005f, 0); 
//			}
//		}
				 
	}     
	
    private void initKeys() {
        // You can map one or several inputs to one named action
        inputManager.addMapping("rewind",  new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("fastforward",  new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("play/pause",  new KeyTrigger(KeyInput.KEY_P));
        
        // Add the names to the action listener.
        inputManager.addListener(actionListener, "rewind");
        inputManager.addListener(actionListener, "fastforward");
        inputManager.addListener(actionListener, "play/pause");
    }
        
    boolean pause = false;
    
    public void togglePausePlay() {
    	if (pause) {
    		pause = false;
    		//cinematic.play();
    	} else {
    		pause = true;
			//cinematic.pause();
    	}
    }
    
    void waitFor(float seconds) {
    	togglePausePlay();
		scheduledActions.add(new WaitAction(currentTime, 3) {
			@Override
			public void task() {
				togglePausePlay();
			}
		});
    }
    
    public static boolean backwards = false;
    
    public void playBackwards() {
    	backwards = true;
    	reverseDirection();
    	//cinematic.setSpeed(-1);
    }
    
    public void playForwards() {
    	backwards = false;
    	reverseDirection();
    	//cinematic.setSpeed(1);
    }
    
    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("rewind") && keyPressed) {
            	playBackwards();
            }
            if (name.equals("fastforward") && keyPressed) {
            	playForwards();
            }
            if (name.equals("play/pause") && keyPressed) {
            	togglePausePlay();
            }
        }
    }; 
    
    public Geometry getGeometry(Spatial spatial){
    	System.out.println("getMyGeometry()");
		Geometry g = null;
		
		final List<Spatial> ants = new LinkedList<Spatial>();
		//node.breadthFirstTraversal(new SceneGraphVisitor() {
		spatial.depthFirstTraversal(new SceneGraphVisitor() { 
			@Override 
			public void visit(Spatial spatial) {
				//System.out.println("visit class is " + spatial.getClass().getName());
				//System.out.println("visit spatial is " + spatial);
				if (spatial.getClass().getName().equals("com.jme3.scene.Geometry")) {
					ants.add(spatial);
				}
			}
		});
		if (!ants.isEmpty()) {
			//redundant - borrowed from Quixote TerrainTrackControl
			for (int i = 0;i < ants.size();i++){
				if (ants.get(i).getClass().getName().equals("com.jme3.scene.Geometry")){
					g = (Geometry)ants.get(i);
					//System.out.println("g (" + i + "/" + (ants.size() - 1) + ")=" + g);
					return(g);
				}
			}
		}
		else
		{
			System.out.println("getMyGeometry()-Geometry not found");
		}
		return(g);
	}

}
