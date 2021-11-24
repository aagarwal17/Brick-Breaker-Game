package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Fish {

	// Fields
	private int x, y, picNum, dx, width, height;
	private String fileName;
	private BufferedImage image;
	private boolean isActive;
	private boolean right;
	private float alpha;
	private double alphaD;
	AlphaComposite alcom;
	
	public Fish() {
		
		alpha = 1f;
		
		alpha = (float) Math.random();
		int rand = (int) (Math.random() * 2 + 1);
		width = (int)(Math.random() * 400 + 100);
		height = (int)(Math.random() * 200 + 50);
		
		picNum = (int)(Math.random() * 9 + 1);
		fileName = "/fish" + picNum + ".png";
		
		if(rand == 1) {			
			x = -300;
			dx = (int)(Math.random() * 6 + 2);
			right = true;
		} else if(rand == 2) {
			x = BBMain.WIDTH + width;
			dx = -(int)(Math.random() * 6 + 2);
			right = false;
		}
		y = (int)(Math.random() * BBMain.HEIGHT - 100);
		
		try{
			image = ImageIO.read(getClass().getResourceAsStream(fileName));
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		isActive = true;
	}
	
	public void update() { 
		x += dx;
		if(x > BBMain.WIDTH + 300 || x < -300) {
			isActive = false;
		}
	}
	
	public void draw(Graphics2D g) {
		
        alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g.setComposite(alcom);
		if(right) {
			g.drawImage(image, x, y, width, height, null);
		} else {
			g.drawImage(image, x, y, -width, height, null);
		}
		alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
        g.setComposite(alcom);
	}
	
	public boolean getIsActive() { return isActive; }
	
}
