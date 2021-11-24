package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class AddScore {
	
	private int x, y, dy, score, fontSize;
	private long startTime;
	private boolean isActive;

	public AddScore(int score, int x, int y){
		
		this.x = x;
		this.y = y;
		this.score = score;
		dy = -1; 
		startTime = System.nanoTime();
		isActive = true;
		fontSize = 12;
		
	}
	
	public void update() {
		y += dy;
		fontSize++;
		if((System.nanoTime() - startTime) / 1000000 > 400) {
			isActive = false;
		}
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Courier New", Font.BOLD, fontSize + 2));
		g.drawString("+" + score, x, y);
		g.setColor(Color.RED);
		g.setFont(new Font("Courier New", Font.BOLD, fontSize));
		g.drawString("+" + score, x, y);
		
	}
	
	public boolean getIsActive() { return isActive; }
	
}
