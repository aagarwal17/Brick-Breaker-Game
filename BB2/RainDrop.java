package main;

import java.awt.Color;
import java.awt.Graphics2D;

public class RainDrop {

	private int x, y, dy, dx, size;
	private Color color;
	
	public RainDrop() {
		
		x = (int) (Math.random() * BBMain.WIDTH);
		size = (int) (Math.random() * 20 + 2);
		dy = (int) (Math.random() * 10 + 5);
		dx = (int) (Math.random() * 10 - 5);
		color = new Color(250 - size * 10, 250 - size * 10, 250 - size * 10);
		y = 0;
		
	}
	
	public void update() {
		
		y += dy;
		x += dx;
		
	}
	
	public void draw(Graphics2D g) {
		
		g.setColor(color);
		g.drawOval(x, y, size, size);
		
	}
	
	public int getY() { return y; }
	
}
