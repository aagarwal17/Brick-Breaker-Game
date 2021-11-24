package main;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Gun {

	
	// Fields 
	private double x1, x2, y, width, height;
	private BufferedImage image;
	private Paddle thePaddle;	
	private ArrayList<Rocket> rockets;
	private boolean gunCanFire;
	private int gunFireCount, gunFireTarget;
	
	
	public Gun(double x, Paddle thePaddle) { 
		gunFireCount = 0;
		gunFireTarget = 6;
		gunCanFire = true;
		width = 20;
		height = 80;
		y = thePaddle.YPOS;
		rockets = new ArrayList<Rocket>();
		x1 = x - width;
		x2 = x + thePaddle.getWidth() + 20;
		this.thePaddle = thePaddle;
		try{
			image = ImageIO.read(getClass().getResourceAsStream("/gun.png"));
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void update() {
	
		if(!gunCanFire) {
			gunFireCount++;
			if(gunFireCount > gunFireTarget) {
				gunCanFire = true;
			}
		}
		
		for(int i = 0; i < rockets.size(); i++) {
			rockets.get(i).update();
			if(!rockets.get(i).getIsActive()){
				rockets.remove(i);
				i--;
			}
		}
		
	}
	
	public void draw(Graphics2D g) {
		
		for(int i = 0; i < rockets.size(); i++) {
			rockets.get(i).draw(g);
		}
		
		g.drawImage(image, (int)x1, (int)y, (int)width, (int)height, null);
		g.drawImage(image, (int)x2, (int)y, -(int)width, (int)height, null);
		
	}
	
	public void setPosition(int x) {
		
		x1 = x - width;
		
		x2 = x + thePaddle.getWidth() + 20;
	}
	
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_SPACE && gunCanFire) {
			thePaddle.playSound("file:./Resources/rocket.wav", 0);
			rockets.add(new Rocket(thePaddle.getX(), thePaddle));
			rockets.add(new Rocket(thePaddle.getX() + thePaddle.getWidth(), thePaddle));
			gunCanFire = false;
			gunFireCount = 0;
		}
	}
	
	public ArrayList<Rocket> getRockets() { return rockets; }
}
