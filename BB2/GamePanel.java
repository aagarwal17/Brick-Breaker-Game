package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

public class GamePanel extends JPanel{

	// Fields
	private boolean running;
	private BufferedImage image;
	private Graphics2D g;
	private MyMouseMotionListener theMouseListener;
	private MyKeyListener theKeyListener;
	
	private int mousex;
	private String woodSound;
	public static final Color BGCOLOR = new Color(50, 50, 150);
	
	// Timers
	private long FPS = 60;
	private long targetTime = 1000 / FPS;
	private long startTime, elapsedTime, waitTime;
	
	// entities
	private Ball theBall;
	private Ball theExtraBall;
	private Paddle thePaddle;
	private Map theMap;
	private HUD theHud;
	private SchoolOfFish theSchool;
	//private Background bg;
	
	// lists of visuals and modifiers
	private ArrayList<PowerUp> powerUps;
	private ArrayList<RainDrop> rain;
	private ArrayList<BrickSplosion> brickSplosions;
	
	// modifier stuff
	private boolean raining = false;
	private double rainTimer;
	private boolean extraBallActive;
	private double extraBallTimer;
	private double gravityTimer;
	private boolean gravityActive;
	private double gravity;
	private boolean screenShakeActive;
	private long screenShakeTimer;
	
	// constructor
	public GamePanel() {
		
		this.setFocusable(true);
		this.requestFocus();
		
		init();
		
	}
	
	public void init() {
		theHud = new HUD();
		mousex = 0;
		woodSound = "file:./Resources/woodhit.aif";
		int dx = (int)(Math.random() * 12 - 6);
		theBall = new Ball(600, 600, dx, 8, 15, theHud);
		theExtraBall = new Ball(340, 220, dx, 4, 15, theHud);
		theMap = new Map(5, 10, theHud);	
		thePaddle = new Paddle(100, 30, theHud, theBall, theMap);
		theSchool = new SchoolOfFish();
		//bg = new Background("/ocean.jpg", .1);
		
		theMouseListener = new MyMouseMotionListener();
		theKeyListener = new MyKeyListener();
		
		powerUps = new ArrayList<PowerUp>();
		rain = new ArrayList<RainDrop>();
		brickSplosions = new ArrayList<BrickSplosion>();
		
		addMouseMotionListener(theMouseListener);
		addKeyListener(theKeyListener);
		
		running = true;
		extraBallActive = false;
		gravityActive = false;
		screenShakeActive = false;
		gravity = .5;
		image = new BufferedImage(BBMain.WIDTH, BBMain.HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		playSound("file:./Resources/oceanman.aif", 20);
		
	}

	public void playGame() {
		drawStart();
		
		// Game Loop
		while(running) {
			
			startTime = System.nanoTime();
			
			// update
			update();
			
			// render or draw
			draw();
			
			// display
			repaint();
			
			elapsedTime = System.nanoTime() - startTime; 
			
			waitTime = targetTime - elapsedTime / 1000000; 
			
			if(waitTime < 0) { waitTime = 5; }
			
			try { 
				Thread.sleep(waitTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void checkCollisions(){
		
		Rectangle ballRect = theBall.getRect();
		Rectangle paddleRect = thePaddle.getRect();
		Rectangle extraBallRect = theExtraBall.getRect();
		
		checkPowerUpCollisions(paddleRect);
		
		if(thePaddle.getGunActive()) {
			checkRocketCollisions();
		}
		
		
		checkBallPaddleCollisions(theBall, ballRect, paddleRect);
		
		if(extraBallActive) {
			checkBallPaddleCollisions(theExtraBall, extraBallRect, paddleRect);
		}
		
	}
	
	public void checkBallPaddleCollisions(Ball theBall, Rectangle ballRect, Rectangle paddleRect) {
		
		if(ballRect.intersects(paddleRect)) {
			
			theBall.setY(thePaddle.YPOS - theBall.getSize() - 1);
			
			theBall.setDY(-theBall.getDY());
			
			if(theBall.getX() + theBall.getSize() < mousex) {
				theBall.setDX(theBall.getDX() -.5);
			}
			if(theBall.getX() >= mousex) {
				theBall.setDX(theBall.getDX() + .5);
			}
			
			if(gravityActive) {
				theBall.setDY(-22);
			}
			
			playSound("file:./Resources/bounce.aif", 0);
		}
				
		for(int row = 0; row < theMap.getMapArray().length; row++) {
			for(int col = 0; col < theMap.getMapArray()[0].length; col++) {
				
				int brick = theMap.getMapArray()[row][col];
				
				if(brick > 0) {
					
					int brickx = col * theMap.getBrickWidth() + theMap.HOR_PAD + col * theMap.BETWEEN_PAD;
					int bricky = row * theMap.getBrickHeight() + theMap.VERT_PAD + row * theMap.BETWEEN_PAD;
					int brickWidth = theMap.getBrickWidth();
					int brickHeight = theMap.getBrickHeight();
					
					Rectangle brickRect = new Rectangle(brickx, bricky, brickWidth, brickHeight);
					
					if(ballRect.intersects(brickRect)) {
						
						if(theMap.getMapArray()[row][col] == 1) {
							brickSplosions.add(new BrickSplosion(brickx, bricky, theMap));
							playSound("file:./Resources/shatter.aif", 0);
							screenShakeActive = true;
							screenShakeTimer = System.nanoTime();
						}
						
						if(theMap.getMapArray()[row][col] > 3) {
							powerUps.add(new PowerUp(brickx, bricky, theMap.getMapArray()[row][col], brickWidth, brickHeight));
							theMap.setBrick(row, col, 3);
						} else {
							theMap.hitBrick(row, col);
							playSound(woodSound, 0);
						}
						
						if(ballRect.getMaxX() <= brickx + 5) {
							theBall.setDX(-theBall.getDX());
							theBall.setX(brickx - 1);
						} else if(ballRect.getMinX() >= brickRect.getMaxX() - 5) {
							theBall.setDX(-theBall.getDX());
							theBall.setX(brickx + theMap.getBrickWidth() + 1);
						} else {
							theBall.setDY(-theBall.getDY());
						}
						theHud.addScore(50);
					}
				}
			}
		}
	}
	
	
	public void checkPowerUpCollisions(Rectangle paddleRect) { 
		for(int i = 0; i < powerUps.size(); i++) {
			
			Rectangle puRect = powerUps.get(i).getRect();
			
			int puX = powerUps.get(i).getX();
			int puY = powerUps.get(i).getY();
			
			if(paddleRect.intersects(puRect)) {
				if(powerUps.get(i).getType() == PowerUp.WIDEPADDLE && !powerUps.get(i).getWasUsed()) {
					thePaddle.setWidth();
					powerUps.get(i).setWasUsed(true);
					playSound("file:./Resources/mushroom.aif", 0);
					theHud.addScore(20, puX, puY);
					theHud.incrementMultiplier();
				}
				if(powerUps.get(i).getType() == PowerUp.FASTBALL && !powerUps.get(i).getWasUsed()) {
					theBall.fastBall();
					theBall.setDrawTail(true);
					powerUps.get(i).setWasUsed(true);
					playSound("file:./Resources/laser2.aif", 0);
					theHud.addScore(100, puX, puY);
					theHud.incrementMultiplier();
				}
				if(powerUps.get(i).getType() == PowerUp.NARROWPADDLE && !powerUps.get(i).getWasUsed()) {
					thePaddle.shrinkWidth();
					powerUps.get(i).setWasUsed(true);
					playSound("file:./Resources/shrink.aif", 0);
					theHud.addScore(100, puX, puY);
					theHud.incrementMultiplier();
				}
				if(powerUps.get(i).getType() == PowerUp.SHRINKMAP && !powerUps.get(i).getWasUsed()) {
					theMap.setPowerUpShrink();
					powerUps.get(i).setWasUsed(true);
					playSound("file:./Resources/shrink.aif", 0);
					theHud.addScore(60, puX, puY);
					theHud.incrementMultiplier();
				}
				if(powerUps.get(i).getType() == PowerUp.INVISIBLEMAP && !powerUps.get(i).getWasUsed()) {
					theMap.setInvisible();
					powerUps.get(i).setWasUsed(true);
					playSound("file:./Resources/bell6.wav", 0);
					theHud.addScore(70, puX, puY);
					theHud.incrementMultiplier();
				}
				if(powerUps.get(i).getType() == PowerUp.FAINTBALL && !powerUps.get(i).getWasUsed()) {
					theBall.faintBall();
					powerUps.get(i).setWasUsed(true);
					playSound("file:./Resources/bell5.wav", 0);
					theHud.addScore(80, puX, puY);
					theHud.incrementMultiplier();
				}
				if(powerUps.get(i).getType() == PowerUp.RAIN && !powerUps.get(i).getWasUsed()) {
					raining = true;
					powerUps.get(i).setWasUsed(true);
					rainTimer = System.currentTimeMillis();
					playSound("file:./Resources/chimes.aif", 0);
					theHud.addScore(30, puX, puY);
					theHud.incrementMultiplier();
				}
				if(powerUps.get(i).getType() == PowerUp.EXTRABALL && !powerUps.get(i).getWasUsed()) {
					extraBallActive = true;
					int dx = (int)(Math.random() * 6 - 3);
					theExtraBall = new Ball((int)theBall.getX(), (int)theBall.getY()+ 1, (int)dx, (int)theBall.getDY() / 2, (int)theBall.getSize(), theHud);
					powerUps.get(i).setWasUsed(true);
					extraBallTimer = System.currentTimeMillis();
					theHud.addScore(120, puX, puY);
					theHud.incrementMultiplier();
				}
				if(powerUps.get(i).getType() == PowerUp.GRAVITY && !powerUps.get(i).getWasUsed()) {
					gravityActive = true;
					powerUps.get(i).setWasUsed(true);
					gravityTimer = System.currentTimeMillis();
					theHud.addScore(80, puX, puY);
				}
				if(powerUps.get(i).getType() == PowerUp.GUN && !powerUps.get(i).getWasUsed()) {
					thePaddle.setGunActive(true);
					playSound("file:./Resources/guns.aif", 0);
					powerUps.get(i).setWasUsed(true);
					theHud.addScore(80, puX, puY);
				}
			}
		}
	}
	
	public void update() {
		
		checkCollisions();
		
		//bg.update();
		
		theSchool.update();
		
		theHud.update();
		
		theBall.update();
		
		theMap.update();
		
		thePaddle.update();
		
		for(PowerUp pu : powerUps) {
			pu.update();
		}
		
		if(raining) {
			updateRain();
		}
		
		if(extraBallActive) {
			theExtraBall.update();
		}
		if((System.currentTimeMillis() - extraBallTimer) > 8000 && extraBallActive) {
			extraBallActive = false;
			theHud.decrementMultiplier();
		}
		
		if((System.nanoTime() - screenShakeTimer) / 1000000 > 300 && screenShakeActive) {
			screenShakeActive = false;
		}
		
		if(gravityActive) {
			updateGravity();
			gravity(theBall);
			if(extraBallActive) {
				gravity(theExtraBall);
			}
		}
		
		for(int i = 0; i < brickSplosions.size(); i++) {
			brickSplosions.get(i).update();
			if(!brickSplosions.get(i).getIsActive()){
				brickSplosions.remove(i);
			}
		}
	}
	
	public void draw() {
		
		// draw background
		g.setColor(BGCOLOR);
		g.fillRect(0, 0, BBMain.WIDTH, BBMain.HEIGHT);
		//bg.draw(g);
		theSchool.draw(g);
		
		for(BrickSplosion bs : brickSplosions) {
			bs.draw(g);
		}
		
		theMap.draw(g);
		
		theBall.draw(g);
		
		thePaddle.draw(g);
		
		theHud.draw(g);
		
		drawPowerUps();
		
		
		
		if(theMap.isThereAWin() == true) {
			drawWin();
			running = false;
		}
		
		//if(theBall.youLose() || theExtraBall.youLose()) {
		//	drawLoser();
		//	running = false;
		//}
		if(extraBallActive) {
			theExtraBall.draw(g);
		}
		if(raining) {
			drawRain();
		}
		
	}
	
	public void drawRain() {
		for(RainDrop rd : rain) {
			rd.draw(g);
		}
	}
	
	public void updateRain() {
		for(int i = 0; i < rain.size(); i++) {
			rain.get(i).update();
			if(rain.get(i).getY() > BBMain.HEIGHT) {
				rain.remove(i);
			}
			
		}
		
		if(System.currentTimeMillis() % 4 == 0) {
			rain.add(new RainDrop());
		}
		
		if(System.currentTimeMillis() - rainTimer > 10000) {
			rain.clear();
			raining = false;
			theHud.decrementMultiplier();
		}
		
	}
	
	public void updateGravity() {
		
		if(System.currentTimeMillis() - gravityTimer > 10000) {
			gravityActive = false;
			if(theBall.getDY() < 0){
				theBall.setDY(-theBall.getStartDY());
			} else {
				theBall.setDY(theBall.getStartDY());
			}
			
		}
		
	}
	
	public void drawStart() {

		for(int i = 100; i > 0; i--) {
			g.setColor(BGCOLOR);
			g.fillRect(0, 0, BBMain.WIDTH, BBMain.HEIGHT);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier New", Font.BOLD, i));
			g.drawString("Start!", BBMain.WIDTH / 2 - i * 2 + 20, BBMain.HEIGHT / 2);
			repaint();
			try { 
				Thread.sleep(20);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void drawWin() {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Courier New", Font.BOLD, 200));
		g.drawString("Winner!!", 80, 400);
	}
	
	public void drawPowerUps() {
		for (PowerUp pu : powerUps) {
			pu.draw(g);
		}
	}
	
	public void drawLoser() {
		g.setColor(Color.RED);
		g.setFont(new Font("Courier New", Font.BOLD, 50));
		g.drawString("Loser!!", 200, 200);
	}
	
	public void paintComponent(Graphics g) {
		
		Graphics2D g2 = (Graphics2D) g;
		int x = 0;
		int y = 0;
		
		if(screenShakeActive) {
			x = (int) (Math.random() * 10 - 5);
			y = (int) (Math.random() * 10 - 5);
			
		}
		g2.drawImage(image, x, y, BBMain.WIDTH, BBMain.HEIGHT, null);
		
		g2.dispose();
		
		
	}
	
	
	public void gravity(Ball ball) {
		ball.setDY(ball.getDY() + gravity);
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
	
	private class MyMouseMotionListener implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mousex = e.getX();
			thePaddle.mouseMoved(e.getX());
			//bg.mouseMoved(mousex);
		}
		
	}
	
	private class MyKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyPressed(KeyEvent e) {
			thePaddle.keyPressed(e.getKeyCode());
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
}
