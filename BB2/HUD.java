package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class HUD {

	// Field
	private int score;
	private ArrayList<AddScore> scores;
	private int multiplier;
	
	// constructor
	public HUD () {
		
		init();
		
	}
	
	public void init() {
		score = 0;
		scores = new ArrayList<AddScore>();
		multiplier = 1;
	}
	
	public void draw(Graphics2D g) {
		g.setFont(new Font("Courier New", Font.BOLD, 20));
		g.setColor(Color.WHITE);
		g.drawString("Score: " + score, 20, 20);
		g.drawString("Multiplier: " + multiplier, 20, 42);
		for(AddScore as : scores) {
			as.draw(g);
		}
		
	}
	
	public void update() { 
		for(int i = 0; i < scores.size(); i++) {
			scores.get(i).update();
			if(!scores.get(i).getIsActive()) {
				scores.remove(i);
			}
		}
		
	}
	
	public int getScore() { return score; }
	
	public void addScore(int scoreToAdd, int x, int y) {
		score = score + scoreToAdd * multiplier;
		scores.add(new AddScore(scoreToAdd, x, y));
		
	}
	
	public void addScore(int scoreToAdd) {
		score += scoreToAdd * multiplier;
	}
	public void decrementMultiplier() { 
		
		multiplier--;
		if(multiplier < 1) {
			multiplier = 1;
		}
	}
	public void incrementMultiplier() { multiplier++; }
}
