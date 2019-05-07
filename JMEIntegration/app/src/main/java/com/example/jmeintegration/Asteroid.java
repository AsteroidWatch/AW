package com.example.jmeintegration;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Geometry;

public class Asteroid {

	public String id = null;
	public float size = 0;
	public Geometry model;
	public BitmapText text;
	
	public Asteroid(String id, float size, Geometry model, AssetManager assetManager) {
		this.id = id;
		this.size = size;
		this.model = model;
				
		BitmapFont font = assetManager.loadFont("Interface/Fonts/Default.fnt");

		text = new BitmapText(font, false);
		
		text.setText(id + "\n" + (int) size);
		
		text.scale(model.getWorldScale().y / 100);
		
	}
	
}
