package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

public class BrickSplosion {

	// fields
	private ArrayList<BrickPiece> pieces;
	private Color color;
	private int x, y;
	private Map theMap;
	private boolean isActive;
	private long startTime;
	
	// pass in the upper left corner of the brick
	public BrickSplosion(int thex, int they, Map theMap){
		
		x = thex;
		y = they;
		this.theMap = theMap;
		isActive = true;
		startTime = System.nanoTime();
		init();
		
	}
	
	public void init() {

		pieces = new ArrayList<BrickPiece>();
		
		int randNum = (int) (Math.random() * 20 + 5);
		
		for(int i = 0; i < randNum; i++) {
			pieces.add(new BrickPiece(x, y, theMap));
		}
	}
	
	public void update() {
		for(BrickPiece bp : pieces) {
			bp.update();
		}
		if((System.nanoTime() - startTime) / 1000000 > 2000 && isActive) {
			isActive = false;
		}
		
	}
	
	public void draw(Graphics2D g) {
		for(BrickPiece bp : pieces) {
			bp.draw(g);
		}
	}
	
	public boolean getIsActive() { return isActive; }
}
