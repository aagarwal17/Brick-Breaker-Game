package main;

import javax.swing.JFrame;

public class BBMain {

	public static final int WIDTH = 1024, HEIGHT = 768;
	
	public static void main(String[]  args) {
		
		JFrame theFrame = new JFrame("Brick Breaker");
		
		GamePanel thePanel = new GamePanel();
		theFrame.setSize(WIDTH, HEIGHT);
			
		theFrame.setLocationRelativeTo(null);
		theFrame.setResizable(false);
		
		theFrame.add(thePanel);
		
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setVisible(true);
		
		thePanel.playGame();
		
	}
	
}
