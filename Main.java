import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/**
 * 
 * @author Arun Agarwal Period 7
 *
 */
public class Main
{
	
	/**
	 * width and height of frame
	 */
	public static final int WIDTH = 640; 
	public static final int HEIGHT = 480;
	
	/**
	 * main method
	 * @param args
	 */
	public static void main(String[]  args) 
	{
		
		JFrame theFrame = new JFrame("Arun's Brick Breaker");
		
		GamePanel thePanel = new GamePanel();
		theFrame.setSize(WIDTH, HEIGHT);
			
		theFrame.setLocationRelativeTo(null);
		theFrame.setResizable(false);
		//theFrame.addKeyListener(thePanel.getListener());
		
		theFrame.add(thePanel);
	
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setVisible(true);
		
		thePanel.run();
		
	}
	
}
