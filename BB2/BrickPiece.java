package main;

import java.awt.Color;
import java.awt.Graphics2D;

public class BrickPiece {

	// fields
	private double dx, dy, y, x, gravity;
	private Color color;
	private Map theMap;
	private int size;
	
	public BrickPiece(double brickx, double bricky, Map theMap){
		this.theMap = theMap;
		x = brickx + theMap.getBrickWidth() / 2;
		y = bricky + theMap.getBrickHeight() / 2;
		dx = (int) (Math.random() * 16 - 8);
		dy = (int) (Math.random() * 16 - 8);
		size = (int) (Math.random() * 15 + 2);
		color = new Color(0, 200, 200);
		gravity = .4;
	}
	
	public void update(){ 
		
		x += dx;
		y += dy;
		dy += gravity;
		
	}
	
	public void draw(Graphics2D g) {
		g.setColor(color);
		g.fillRect((int)x, (int)y, size, size);
	}
	
}
