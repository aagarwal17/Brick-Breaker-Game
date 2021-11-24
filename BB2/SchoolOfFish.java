package main;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class SchoolOfFish {

	private ArrayList<Fish> school;
	private int addCount, targetAdd;
	
	public SchoolOfFish() {
		addCount = 0;
		targetAdd = (int)(Math.random() * 100 + 50);
		school = new ArrayList<Fish>();
	}
	
	public void update() {
		addCount++;
		if(targetAdd < addCount) {
			addCount = 0;
			targetAdd = (int)(Math.random() * 100 + 50);
			school.add(new Fish());
		}
		
		for(int i = 0; i < school.size(); i++) {
			school.get(i).update();
			if(!school.get(i).getIsActive()) {
				school.remove(i); 
				i--;
			}
		}
		
	}
	
	public void draw(Graphics2D g) {
		for (Fish f : school) {
			f.draw(g);
		}
	}
	
}
