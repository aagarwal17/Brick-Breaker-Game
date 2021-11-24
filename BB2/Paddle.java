package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Paddle {

	// Fields
	private double x, targetx;
	private double width, height, startWidth, startHeight;
	private long widthTimer;
	private boolean altWidthActive;
	private HUD theHud;
	private Ball theBall;
	private ArrayList<Bubble> bubbles;
	private int bubbleCount, bubbleTarget;
	private BufferedImage sb;
	private Gun theGun;
	private int gunLoopCount, gunTargetCount;
	private boolean gunActive;
	public final int YPOS = BBMain.HEIGHT - 150;
	
	// Constructor
	public Paddle(int theWidth, int theHeight, HUD theHud, Ball theBall, Map theMap) {
		
		gunActive = false;
		gunLoopCount = 0;
		gunTargetCount = 500;
		altWidthActive = false;
		width = theWidth;
		startWidth = theWidth;
		height = theHeight;
		startHeight = height;
		this.theHud = theHud;
		this.theBall = theBall;
		bubbleCount = 0;
		bubbleTarget = (int)(Math.random() * 100 + 20);
		bubbles = new ArrayList<Bubble>();
		x = BBMain.WIDTH / 2 - width / 2;
		theGun = new Gun(x, this);
		
		try{
			sb = ImageIO.read(getClass().getResourceAsStream("/sb.png"));
		} catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	// update
	public void update() {
		
		if(gunActive) {
			gunLoopCount++;
			if(gunLoopCount > gunTargetCount) {
				gunActive = false;
				gunLoopCount = 0;
			}
		}
		
		if((System.nanoTime() - widthTimer) / 1000 > 8000000 && altWidthActive) {
			altWidthActive = false;
			//width = startWidth;
			theHud.decrementMultiplier();
		}
		
		if(!altWidthActive && Math.abs((startWidth - width) * .02) > .1) {
			width += (startWidth - width) * .02;
		}
		
		x += (targetx - x) * .5;
		int dif = (int) Math.abs(targetx - x) / 5;
		height = startHeight - dif;
		if(height < 5) { height = 5; }
		
		theGun.setPosition((int)x);
		
		updateBubbles();
		
		theGun.update();
		
	}
	
	public void updateBubbles() {
		bubbleCount++;
		if(bubbleTarget < bubbleCount) {
			playSound("file:./Resources/bubble.wav", 0);
			bubbles.add(new Bubble(x + width / 2, YPOS));
			bubbleCount = 0;
			bubbleTarget = (int)(Math.random() * 60 + 1);
		}
		for(int i = 0; i < bubbles.size(); i++) {
			bubbles.get(i).update();
			if(!bubbles.get(i).getIsActive()) {
				bubbles.remove(i);
			}
		}
	}
	
	// draw
	public void draw(Graphics2D g) {
				
		//drawPaddle(g);
		g.drawImage(sb, (int) x,  YPOS, (int)width, (int)startWidth, null);
		
		drawBubbles(g);
		if(gunActive) {
			theGun.draw(g);
		}
		
		
	}
	
	public void drawPaddle(Graphics2D g) {
		double yDraw = YPOS + (startHeight - height) / 2;
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int) x,  (int)yDraw,(int) width, (int)height);
		
		// whites
		g.setColor(Color.WHITE);
		g.fillOval((int) x + 5, YPOS + 5, 20, 20);
		g.fillOval((int) x + (int)width - 25, YPOS + 5, 20, 20);
		
		// pupils
		double leftEyeDraw = x + 9;
		double rightEyeDraw = x + width - 21;
		double ballx = theBall.getX();
		
		
		if(leftEyeDraw > ballx) {
			leftEyeDraw -= 2;
			rightEyeDraw -= 2;
		} else if(rightEyeDraw < ballx){
			leftEyeDraw += 2;
			rightEyeDraw += 2;
		}
		g.setColor(Color.BLACK);
		g.fillOval((int)leftEyeDraw, YPOS + 6, 12, 12);
		g.fillOval((int)rightEyeDraw, YPOS + 6, 12, 12);
	}
	
	public void drawBubbles(Graphics2D g) {
		for(Bubble b : bubbles) {
			b.draw(g);
		}
	}
	
	public void mouseMoved(int mouseXPos) {
		targetx = mouseXPos - width / 2;
		
		if(targetx > BBMain.WIDTH - width) {
			targetx = BBMain.WIDTH - width;
		}
		if(targetx < 0) {
			targetx = 0;
		}
		
	}
	
	public Rectangle getRect() {
		return new Rectangle((int)x, YPOS, (int)width, (int)height);
	}
	
	public double getWidth() { return width; }
	
	public void setWidth() { 
		altWidthActive = true;
		width = width * 2; 
		if(width > 400) {
			width = 400;
		}
		setWidthTimer();
	}
	
	public void shrinkWidth() { 
		altWidthActive = true;
		width = width / 2; 
		setWidthTimer();
	}
	
	public void setWidthTimer() {
		widthTimer = System.nanoTime();
	}
	
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
	public void keyPressed(int k) {
		if(gunActive) {
			theGun.keyPressed(k);
		}
		
	}
	public double getX() { return x; }
	
	public Gun getGun() {  return theGun; }
	
	public void setGunActive(boolean active){ 
		if(active == true) {
			gunLoopCount = 0;
		}
		gunActive = active; 
		}
	
	public boolean getGunActive() { 
		
		return gunActive;
		
		}
}
