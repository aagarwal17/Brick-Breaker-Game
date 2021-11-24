import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * 
 * @author Arun Agarwal Period 7
 *
 */
public class Main extends JFrame
{
	
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	
	public Main()
	{
		setSize(WIDTH, HEIGHT);
		setTitle("The Blaze Breaker");
		CardLayout c1 = new CardLayout();
		JPanel overall = new JPanel();
		overall.setLayout(c1);
		add(overall);
		
		JPanel homescreen = new HomePanel();
		
		JPanel map1 = new GamePanel();
		
		JPanel map2 = new MapPanel2();
		
		JPanel map3 = new MapPanel3();
		
		overall.add(homescreen, "WELCOME");
		overall.add(map1, "Level One:");
		overall.add(map2, "Level Two:");
		overall.add(map3, "Level Three:");
		
		JMenuBar mBar = new JMenuBar();
		JMenu level = new JMenu("Levels");
		JMenuItem home = new JMenuItem("Return to Home");
		JMenuItem level1 = new JMenuItem("Level 1");
		JMenuItem level2 = new JMenuItem("Level 2");
		JMenuItem level3 = new JMenuItem("Level 3");
		
		level.add(level1);
		level.add(level2);
		level.add(level3);
		
		mBar.add(level);
		mBar.add(home);
		
		
		home.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				c1.show(overall, "WELCOME");
				System.out.print("WELCOME!");
				
			}
		});
		
		level1.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				c1.show(overall, "Level One:");
				System.out.print("Level One:");
				
			}
			
		});
		
		level2.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				c1.show(overall, "Level Two:");
				System.out.print("Level Two:");
				
			}
			
		});
		
		level3.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				c1.show(overall, "Level Three:");
				System.out.print("Level Three:");
				
			}
			
		});
		
		
		setJMenuBar(mBar);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
	}

	public static void main(String[] args)
	{
		new Main();
	}
	
}
