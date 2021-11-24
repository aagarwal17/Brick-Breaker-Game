package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Map {

	// Fields
	private int[][] theMap;
	private int brickHeight, brickWidth, startWidth, startHeight, blueAdd, blue;
	
	public final int HOR_PAD = 100, VERT_PAD = 60, BETWEEN_PAD = 20;
	public final static Color BCOLORWEAK = new Color(0, 200, 200);
	public final static Color BCOLORMID = new Color(0, 150, 150);
	public final static Color BCOLORSTRONG = new Color(0, 100, 100);
	
	private long shrinkTimer;
	private long invisibleTimer;
	
	private boolean smallActive;
	private boolean invisible;
	private HUD theHud;
		
	public Map(int row, int col, HUD theHud) {
		
		invisible = false;
		smallActive = false;
		blueAdd = 3;
		blue = 255;
		initMap(row, col);
		
		brickWidth = (BBMain.WIDTH - 2 * HOR_PAD) / col - BETWEEN_PAD;
		brickHeight = (BBMain.HEIGHT / 2 - VERT_PAD * 2) / row - BETWEEN_PAD;
		startWidth = brickWidth;
		startHeight = brickHeight;
		this.theHud = theHud;
		
	}
	
	public void initMap(int row, int col) {
		
		theMap = new int[row][col];
		
		for(int i = 0; i < theMap.length; i++) {
			for(int j = 0; j < theMap[0].length; j++) {
				int r = (int) (Math.random() * 3 + 1);
				theMap[i][j] = r;
			}
		}
		for(int i = 0; i < 20; i ++) {
			int r = (int)(Math.random() * theMap.length);
			int c = (int)(Math.random() * theMap[0].length);
			int type = (int)(Math.random() * 10 + 4);
			theMap[r][c] = type;
		}
	}
	
	public void draw(Graphics2D g) {
		if(!invisible || System.currentTimeMillis() % 50 == 0) {
			if(System.currentTimeMillis() % 50 == 0 && invisible) {
				playSound("file:./Resources/electricshock.wav", 0);
			}
			for(int row = 0; row < theMap.length; row++) {
				for(int col = 0; col < theMap[0].length; col++) {
					if(theMap[row][col] > 0) {
						
						if(theMap[row][col] == 1) {
							g.setColor(BCOLORWEAK);
						}
						if(theMap[row][col] == 2) {
							g.setColor(BCOLORMID);
						}
						if(theMap[row][col] == 3) {
							g.setColor(BCOLORSTRONG);
						}
						if(theMap[row][col] == PowerUp.WIDEPADDLE) {
							g.setColor(PowerUp.WIDECOLOR);
						}
						if(theMap[row][col] == PowerUp.FASTBALL) {
							g.setColor(PowerUp.FASTCOLOR);
						}
						if(theMap[row][col] == PowerUp.NARROWPADDLE) {
							g.setColor(PowerUp.NARROWCOLOR);
						}
						if(theMap[row][col] == PowerUp.SHRINKMAP) {
							g.setColor(PowerUp.SHRINKCOLOR);
						}
						if(theMap[row][col] == PowerUp.INVISIBLEMAP) {
							g.setColor(PowerUp.INVISIBLECOLOR);
						}
						if(theMap[row][col] == PowerUp.FAINTBALL) {
							g.setColor(PowerUp.FAINTCOLOR);
						}
						if(theMap[row][col] == PowerUp.RAIN) {
							g.setColor(PowerUp.RAINCOLOR);
						}
						if(theMap[row][col] == PowerUp.EXTRABALL) {
							g.setColor(PowerUp.EXTRABALLCOLOR);
						}
						if(theMap[row][col] == PowerUp.GRAVITY) {
							g.setColor(PowerUp.GRAVITYCOLOR);
						}
						if(theMap[row][col] == PowerUp.GUN) {
							if(blue > 254 || blue < 10) {
								blueAdd = -blueAdd;
							}
							blue += blueAdd;
							g.setColor(new Color(40, 40, blue));
						}
						g.fillRoundRect(col * brickWidth + HOR_PAD + BETWEEN_PAD * col, row * brickHeight + VERT_PAD + BETWEEN_PAD * row, brickWidth, brickHeight, 10, 10);
						g.setStroke(new BasicStroke(2));
						//g.setColor(GamePanel.BGCOLOR);
						//g.drawRect(col * brickWidth + HOR_PAD, row * brickHeight + VERT_PAD, brickWidth, brickHeight);
					}
				}
			}
		}
		
	}
	
	public void update() { 
		if((System.nanoTime() - shrinkTimer) / 1000 > 8000000 && smallActive) {
			brickHeight = startHeight;
			brickWidth = startWidth;
			smallActive = false;
			theHud.decrementMultiplier();
		}
		if((System.nanoTime() - invisibleTimer) / 1000 > 4000000 && invisible) {
			
			theHud.decrementMultiplier();
			invisible = false;
		}
	}
	
	public boolean isThereAWin() {
		
		boolean thereIsAWin = false;
		
		int bricksRemaining = 0;
		
		for(int row = 0; row < theMap.length; row++) {
			for(int col = 0; col < theMap[0].length; col++) {
				bricksRemaining += theMap[row][col];
			}
		}
		
		if(bricksRemaining == 0) {
			thereIsAWin = true;
		}
		
		return thereIsAWin;
	}
	
	public int[][] getMapArray() { return theMap; }
	
	public void setBrick(int row, int col, int value) {
		theMap[row][col] = value;
	}
	
	public int getBrickWidth() { return brickWidth; }
	
	public void setPowerUpShrink() {
		shrinkTimer = System.nanoTime();
		brickWidth /= 2;
		brickHeight /= 2;
		smallActive = true;
		
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
	
	public void setInvisible() { 
		invisible = true;
		invisibleTimer = System.nanoTime();
	}
	
	public int getBrickHeight() { return brickHeight; }
	
	public void hitBrick(int row, int col) {
		if(theMap[row][col] < 10){
			theMap[row][col] -= 1;
			if(theMap[row][col] < 0) {
				theMap[row][col] = 0;
			}
		}
		
	}
	
}
