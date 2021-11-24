package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Bubble {

	private double x, y, dx, dy, size, targetx, targety;
	private int newXCount, newYCount, newYTarget, newXTarget;
	private boolean isActive;

	public Bubble(double x, double y){
		isActive = true;
		dy = Math.random() * 6 + 2;
		this.x = x;
		this.y = y;
		targetx =  x + (Math.random() * 20 - 10);
		targety = y - (Math.random() * 200 + 50);
		newYCount = 0;
		newXCount = 0;
		size = Math.random() * 20 + 10;
		newXTarget = (int)(Math.random() * 20 + 20);
		newYTarget = (int)(Math.random() * 50 + 20);
	}
	
	public void update() {
		x += (targetx - x) * .05;
		y -= dy;
		
		if(newXTarget < newXCount) {
			newXTarget = (int)(Math.random() * 50 - 10);
			newXCount = 0;
			targetx = x + Math.random() * 100 - 50;
		}
		if(newYTarget < newYCount) {
			newYTarget = (int)(Math.random() * 80 + 40);
			newYCount = 0;
			dy = (Math.random() * 6 + 2);
		}
		if(y < 0) {
			isActive = false;
		}
		
		newXCount++;
		newYCount++;
	}
	
	public void draw(Graphics2D g) {
		AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .6f);
        g.setComposite(alcom);
		g.setColor(Color.BLUE);
		g.fillOval((int)x, (int)y, (int)size, (int)size);
		g.setStroke(new BasicStroke(2));
		g.setColor(Color.WHITE);
		g.drawOval((int)x, (int)y, (int) size, (int)size);
		
		g.setColor(Color.WHITE);
		g.fillOval((int)x + 4, (int)y + 4, (int) size / 4, (int)size / 4);
		alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g.setComposite(alcom);
	}
	
	public boolean getIsActive() { return isActive; }
}
