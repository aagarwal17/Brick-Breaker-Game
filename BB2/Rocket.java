package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Rocket {

	// fields 
	private boolean isActive;
	private double x1, y, dy, width, height;
	private BufferedImage image;
	private Paddle thePaddle;
	
	
	// Constructor
	public Rocket(double x, Paddle thePaddle) {
		this.thePaddle = thePaddle;
		height = 80;
		width = 20;
		x1 = x - width;
		dy = -6;
		y = thePaddle.YPOS - height;
		isActive = true;
		
		try{
			image = ImageIO.read(getClass().getResourceAsStream("/rocket.png"));
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void update() { 
		y += dy;
		dy -= .5;
		if(y < 0 - height) {
			isActive = false;
		}
	}
	
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)x1, (int)y, (int)width, (int)height, null);
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(boolean active) { 
		isActive = active; 
	}
	
	public Rectangle getRocketRect1() { return new Rectangle((int)x1, (int)y, (int)width, (int)height); }
	
}
