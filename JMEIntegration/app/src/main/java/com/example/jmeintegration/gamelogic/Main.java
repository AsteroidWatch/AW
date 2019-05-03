package com.example.jmeintegration.gamelogic;

import com.example.jmeintegration.Asteroid;
import com.jme3.app.SimpleApplication;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.jme3.cinematic.Cinematic;
import com.jme3.cinematic.MotionPath;
import com.jme3.cinematic.events.CinematicEvent;
import com.jme3.cinematic.events.CinematicEventListener;
import com.jme3.cinematic.events.MotionEvent;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

public class Main extends SimpleApplication {

    Cinematic cinematic;
    CameraNode motionCam;
    ArrayList<Geometry> geos = new ArrayList<Geometry>();

    ArrayList<MotionEvent> events = new ArrayList<MotionEvent>();

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    ArrayList<Geometry> generateGeos(int count) {
        ArrayList<Geometry> geos = new ArrayList<Geometry>();
        for (int i = 1; i < count + 1; i++) {
            Box box = new Box(0.5f, 0.5f, 0.5f);
            Geometry geo = new Geometry("Box", box);
            geo.scale((float) Math.pow(2, i), (float) Math.pow(2, i), (float) Math.pow(2, i));
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            geo.setMaterial(mat);
            geos.add(geo);
        }
        return geos;
    }

    ArrayList<Asteroid> getAsteroids(int count) {
        ArrayList<Asteroid> asteroids = new ArrayList<Asteroid>();
        for (int i = 1; i < count + 1; i++) {
            Asteroid asteroid = new Asteroid();
            asteroid.size = (float) Math.pow(2, i);
            Spatial spatial = assetManager.loadModel("asteroid" + i + ".obj");
            //Spatial spatial = assetManager.loadModel("someCube.obj");
            asteroid.model = getGeometry(spatial);
            asteroid.model.scale((float) Math.pow(2, i), (float) Math.pow(2, i), (float) Math.pow(2, i));
            System.out.println(asteroid);
            asteroids.add(asteroid);
        }
        return asteroids;
    }

    void asteroidsToGeos(ArrayList<Asteroid> asteroids) {
        for (Asteroid asteroid : asteroids) {
            geos.add(asteroid.model);
        }
    }

    void renderInWireframe() {
        RenderState renderState = new RenderState();
        renderState.setWireframe(true);
        getRenderManager().setForcedRenderState(renderState);
    }

    void layoutGeos(ArrayList<Geometry> geos) {
        for (int index = 0; index < geos.size(); index++) {
            boolean previousIndexNotNegative = index - 1 >= 0;
            Geometry geo = geos.get(index);
            Vector3f pos = new Vector3f(0, 0, 0);

            if (previousIndexNotNegative) {
                Geometry prevGeo = geos.get(index - 1);
                Vector3f prevGeoPos = prevGeo.getLocalTranslation();
                Vector3f prevGeoScale = prevGeo.getWorldScale();
                pos = prevGeoPos;
                pos.x = prevGeoPos.x + prevGeoScale.x/2 + geo.getWorldScale().x/2 + geo.getWorldScale().x - prevGeo.getWorldScale().x;
            }

            pos.z = -geo.getWorldScale().z/2;
            pos.y = geo.getWorldScale().y/2;

            geo.setLocalTranslation(pos);
            rootNode.attachChild(geo);
        }
    }

    int currentIndex = 0;
    float time = 0;

    void travelThroughGeos(int startIndex, int endIndex) {

        timeWithinEvent = 0;

        if (startIndex > endIndex) {
            int newStartIndex = endIndex;
            endIndex = startIndex;
            startIndex = newStartIndex;
        }

        while (startIndex < endIndex) {

            int accessingIndex = backwards ? endIndex - startIndex : startIndex;

            travelToGeo(geos.get(accessingIndex), geos.get(accessingIndex + (backwards ? -1 : 1)), 3, time);

            startIndex += 1;
            time += 3;

            timeWithinEvent += 3;
        }

    }

    int timeWithinEvent = 0;

    void travelToGeo(Geometry from, Geometry to, float duration, float startingTime) {
        MotionPath path = new MotionPath();
        path.setCurveTension(0);
        path.addWayPoint(from.getWorldTranslation().add(0, 0, from.getWorldScale().z * 5));
        path.addWayPoint(to.getWorldTranslation().add(0, 0, to.getWorldScale().z * 5));
        MotionEvent event = new MotionEvent(motionCam, path);

        event.addListener(new CinematicEventListener() {

            @Override
            public void onPause(CinematicEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onPlay(CinematicEvent arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStop(CinematicEvent arg0) {
                // TODO Auto-generated method stub

                int index = geos.indexOf(to);

                if (index == 0 || index == geos.size() - 1) {
                    changeOfDirection = false;
                    return;
                }

                if (changeOfDirection) {
                    if (backwards) {
                        travelThroughGeos(index, 0);
                    } else {
                        travelThroughGeos(index, geos.size() - 1);
                    }
                    changeOfDirection = false;
                    goToTime = true;
                }

            }

        });

        event.setInitialDuration(1);
        events.add(event);
        cinematic.addCinematicEvent(time, event);
    }

    boolean goToTime = false;

    @Override
    public void simpleInitApp() {

        cam.setFrustumFar(1999999999);

        settings.setFrameRate(30);

        settings.setVSync(true);

        flyCam.setEnabled(false);

        rootNode.addLight(new AmbientLight());

        //testSetup();

        initiateSizeComparisonScene();

    }

    void testSetup() {

        asteroidsToGeos(getAsteroids(200));

        lineUpGeos();

    }

    void lineUpGeos() {

        for (Geometry geo : geos) {
            rootNode.attachChild(geo);
            int previousIndex = geos.indexOf(geo) - 1;

            Geometry previousGeo = previousIndex >= 0 ? geos.get(previousIndex) : null;

            Vector3f previousScale = previousGeo != null ? previousGeo.getWorldScale() : new Vector3f(0, 0, 0);
            Vector3f scale = geo.getWorldScale();

            Vector3f previousPosition = previousGeo != null ? previousGeo.getLocalTranslation() : new Vector3f(0, 0, 0);

            float combinedDiameters = previousScale.x + scale.x;

            geo.setLocalTranslation(previousPosition.x + previousScale.x + previousScale.x / 2 + scale.x / 2 + combinedDiameters / 2, (scale.y / 2) + (scale.y / 3), 0);
        }

    }

    void initiateSizeComparisonScene() {

        ArrayList<Asteroid> asteroids = getAsteroids(20);

        asteroidsToGeos(asteroids);

        //geos = generateGeos(20);

        float duration = 100;

        cinematic = new Cinematic(rootNode, duration);

        motionCam = cinematic.bindCamera("cam", cam);

        motionCam.rotate(0, (float) Math.PI, 0);

        lineUpGeos();

        travelToGeo(geos.get(0), geos.get(0), 3, 0);
        time += 3;

        travelThroughGeos(0, geos.size() - 1);

        stateManager.attach(cinematic);

        cinematic.activateCamera(0, "cam");

        initKeys();

        cinematic.play();

    }

    @Override
    public void simpleUpdate(float tpf) {

        if (goToTime) {

            cinematic.setTime(time - timeWithinEvent);

            goToTime = false;

        }

        if (!pause) {
            for (Geometry geo : geos) {
                geo.rotate(0, 0.005f, 0);
            }
        }

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


    boolean backwards = false;
    boolean changeOfDirection = false;

    boolean pause = false;

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("rewind") && keyPressed) {
                backwards = true;
                changeOfDirection = true;
            }
            if (name.equals("fastforward") && keyPressed) {
                backwards = false;
                changeOfDirection = true;
            }
            if (name.equals("play/pause") && keyPressed) {
                pause = !pause;
                if (pause) {
                    cinematic.play();
                } else {
                    cinematic.pause();
                }
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