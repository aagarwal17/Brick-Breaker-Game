package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class PowerUp {

	// Fields
	private int x, y, dy, type, width, height, blue, blueAdd;
	
	private boolean isOnScreen;
	private boolean wasUsed;
	
	private Color color;
	
	public final static int WIDEPADDLE = 4;
	public final static int FASTBALL = 5;
	public final static int NARROWPADDLE = 6;
	public final static int SHRINKMAP = 7; 
	public final static int INVISIBLEMAP = 8;
	public final static int FAINTBALL = 9;
	public final static int RAIN = 10;
	public final static int EXTRABALL = 11;
	public final static int GRAVITY = 12;
	public final static int GUN = 13;
	
	public final static Color WIDECOLOR = Color.GREEN;
	public final static Color FASTCOLOR = Color.RED;
	public final static Color NARROWCOLOR = Color.PINK;
	public final static Color SHRINKCOLOR = Color.YELLOW;
	public final static Color INVISIBLECOLOR = Color.LIGHT_GRAY;
	public final static Color FAINTCOLOR = Color.DARK_GRAY;
	public final static Color RAINCOLOR = Color.CYAN;
	public final static Color EXTRABALLCOLOR = Color.MAGENTA;
	public final static Color GRAVITYCOLOR = Color.ORANGE;
	
	// constructor
	public PowerUp(int xStart, int yStart, int theType, int theWidth, int theHeight) {
		
		x = xStart;
		y = yStart;
		type = theType;
		width = theWidth;
		height = theHeight;
		blueAdd = 3;
		
		if(type < 4) { type = 4; }
		if(type > 13) { type = 13; }
		if(type == WIDEPADDLE) { color = WIDECOLOR; }
		if(type == FASTBALL) { color = FASTCOLOR; }
		if(type == NARROWPADDLE) { color = NARROWCOLOR; }
		if(type == SHRINKMAP) { color = SHRINKCOLOR; }
		if(type == INVISIBLEMAP) { color = INVISIBLECOLOR; }
		if(type == FAINTBALL) { color = FAINTCOLOR; }
		if(type == RAIN) { color = RAINCOLOR; }
		if(type == EXTRABALL) { color = EXTRABALLCOLOR; }
		if(type == GRAVITY) { color = GRAVITYCOLOR; }
		if(type == GUN) {
			
			blue = (int) (Math.random() * 255);
			color = new Color(40, 40, blue);
		}
		
		dy = (int) (Math.random() * 5 + 3);
		
		wasUsed = false;
		
	}
	
	public void draw(Graphics2D g) {
		if(type == GUN) {
			if(blue > 254 || blue < 10) {
				blueAdd = -blueAdd;
			}
			blue += blueAdd;
			if(blue > 255) {
				blue = 255;
			}
			if(blue < 0) {
				blue = 0;
			}
			color = new Color(40, 40, blue);
		}
		g.setColor(color);
		g.fillRect(x, y, width, height);
		
	}
	
	public void update() {
		
		y += dy;
		
		if(y > BBMain.HEIGHT) {
			isOnScreen = false;
		}
		
	}

	public int getX() {
		return x;
	}

	public void setX(int newX) {
		x = newX;
	}
	
	public int getY() { return y;}
	public void setY(int newY) { y = newY; }
	public int getDY() { return dy; }
	public void setDY(int newDY) { dy = newDY; }
	public int getType() { return type; }
	public boolean getIsOnScreen() { return isOnScreen; }
	public void setIsOnScreen(boolean onScreen) { isOnScreen = onScreen; }
	public boolean getWasUsed() { return wasUsed; }
	public void setWasUsed(boolean used) { wasUsed = used; }
	
	public Rectangle getRect() {
		
		return new Rectangle(x, y, width, height);
		
	}
	
}







