


package main;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Ball {

	// Fields
	private double x, y, dx, dy, startDY;
	private int ballSize;
	private long speedTimer, faintTimer;
	private boolean altSpeed;
	private boolean faintColor;
	private ArrayList<AlphaComposite> alphas;
	private ArrayList<Point> tail;
	private int tailLength = 10;
	private boolean drawTail = false;
	private HUD theHud;
	
	private Color color;
	
	public Ball(int theX, int theY, int theDX, int theDY, int size, HUD theHud) {
		
		x = theX;
		y = theY;
		dx = theDX;
		dy = theDY;
		startDY = theDY;
		ballSize = size;
		altSpeed = false;
		alphas = new ArrayList<AlphaComposite>();
		tail = new ArrayList<Point>();
		initTail();
		color = Color.WHITE;
		this.theHud = theHud;
		
	}
	
	public void update() {
		
		if((System.nanoTime() - speedTimer) / 1000 > 4000000 && altSpeed) {
			if(dy < 0) {
				dy = -startDY;
			} else {
				dy = startDY;
			}
			
			altSpeed = false;
			drawTail = false;
			theHud.decrementMultiplier();
			
		}
		if((System.nanoTime() - faintTimer) / 1000 > 5000000 && faintColor) {
			
			faintColor = false;
			theHud.decrementMultiplier();
		}
		
		if(tail.size() < tailLength) {
			tail.add(new Point((int)x, (int)y));
		} else {
			tail.remove(0);
			tail.add(new Point((int)x, (int)y));
		}
		setPosition();
	}
	
	public void setPosition() {
		
		x += dx;
		y += dy;
		
		if(x < 0) {
			x = 0;
			dx = -dx;
			playSound("file:./Resources/metalhit.aif", 0);
		}
		if(y < 0){
			y = 0;
			dy = -dy;
			playSound("file:./Resources/metalhit.aif", 0);
		}
		if(x > BBMain.WIDTH - ballSize) {
			dx = -dx;
			x = BBMain.WIDTH - ballSize;
			playSound("file:./Resources/metalhit.aif", 0);
		}
		if(y > BBMain.HEIGHT - ballSize) {
			dy = -dy;
			y = BBMain.HEIGHT - ballSize;
			playSound("file:./Resources/metalhit.aif", 0);
		}
		
	}
	
	public void draw(Graphics2D g) {
		if(drawTail) {
			g.setColor(Color.RED);
			for(int i = 0; i < tail.size(); i++) {
				g.setComposite(alphas.get(i));
				g.fillOval((int)tail.get(i).getX(), (int)tail.get(i).getY(), ballSize, ballSize);
			}
		}
		
		if(!faintColor) {
			g.setColor(color);
		} else {
			g.setColor(PowerUp.FAINTCOLOR);
		}
		
		g.setStroke(new BasicStroke(4));
		g.fillOval((int)x, (int)y, ballSize, ballSize);
		
		
	}
	
	public void initTail() {
		float alpha = 1f;
		AlphaComposite alcom;
		
		for(int i = 0; i < tailLength; i++) {
			double alphaD = i * (100 / tailLength);
			alpha = (float) alphaD / 100;
	        alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
	        alphas.add(alcom);
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle((int)x, (int)y, ballSize, ballSize);
	}
	
	public void setDY(double theDY) { dy = theDY; }
	
	public double getDY() { return dy; }
	
	public void setDX(double theDX) { dx = theDX; }
	
	public double getDX() { return dx; }
	
	public double getX() { return x; }
	
	public void setY(double theY) {
		y = theY;
	}
	
	public void setX(double theX) { x = theX; }
	
	public void setColor(Color newColor) {
		color = newColor;
	}
	
	public boolean youLose() {
		boolean loser = false;
		
		if(y > BBMain.HEIGHT - ballSize * 2) {
			loser = true;
		}
		
		return loser;
	}
	
	public void fastBall() { 
		speedTimer = System.nanoTime();
		altSpeed = true;
		dy += dy;
	}
	public void faintBall() { 
		faintTimer = System.nanoTime();
		faintColor = true;
	}
	
	public void setDrawTail(boolean draw) {
		drawTail = draw;
	}
	
	public int getY() { return (int)y; }
	
	public int getSize() { return ballSize; }
	
	public double getStartDY() { return startDY; }
	
	public double getCenterX() { return x + ballSize / 2; }
	public double getCenterY() { return y + ballSize / 2; }
	
	public void playSound(String soundFile, int times) {
		
		try {
			URL soundLocation = new URL(soundFile);
			AudioInputStream audio = AudioSystem.getAudioInputStream(soundLocation);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.loop(times);
            clip.start();
        }
        
        catch(UnsupportedAudioFileException uae) {
            System.out.println(uae);
        }
        catch(IOException ioe) {
            System.out.println(ioe);
        }
        catch(LineUnavailableException lua) {
            System.out.println(lua);
        }
		
		
	}
}
