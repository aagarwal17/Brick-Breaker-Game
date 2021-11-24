package main;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Background {

	// fields
	private BufferedImage image;
	private double x, moveScale, targetx;
	
	public Background(String file, double moveScale) {
		
		x = 0; 
		this.moveScale = moveScale;
		try{
			image = ImageIO.read(getClass().getResourceAsStream(file));
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void update() { 
		x = (targetx - x) * .1;
		if(x > BBMain.WIDTH) {
			x = 0;
		}
		if(x < -BBMain.WIDTH) {
			x = 0;
		}
	}
	
	public void draw(Graphics2D g) {
		
		g.drawImage(image, (int)x, 0, BBMain.WIDTH, BBMain.HEIGHT, null);
		
		if(x < 0) {
			g.drawImage(image, (int)x + BBMain.WIDTH, 0, BBMain.WIDTH, BBMain.HEIGHT, null);
		}
		if(x > 0) {
			g.drawImage(image, (int)x - BBMain.WIDTH, 0, BBMain.WIDTH, BBMain.HEIGHT, null);
		}
	}
	public void mouseMoved(int x) {
		targetx = moveScale * x;
	}
}
