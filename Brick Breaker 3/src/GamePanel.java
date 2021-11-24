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
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * 
 * @author Arun Agarwal Period 7
 *
 */
public class GamePanel extends JPanel
{
	//Fields
	private boolean running;
	private BufferedImage image;
	private Graphics2D g;
	private MyMouseMotionListener theMouseListener;
	private MyKeyListener theKeyListener;
	private int mousex;

	private Ball theBall;
	private Ball theExtraBall;
	private Paddle thePaddle;
	private Map theMap;
	private Score theScore;
	private ArrayList<PowerUp> powerUps;
	
	private boolean extraBallActive;
	private double extraBallTimer;
	
	//constructor
	public GamePanel()
	{
		initialize();
	}
	
	//public KeyListener getListener()
	//{
	//	return theKeyListener;
	//}
	
/**
 * initializes all necessary objects for the game	
 */
	public void initialize()
	{
		mousex = 0;
		theBall = new Ball();
		theExtraBall = new Ball();
		thePaddle = new Paddle(80,20);
		theMap = new Map(5, 12);
		theScore = new Score();
		theMouseListener = new MyMouseMotionListener();
		theKeyListener = new MyKeyListener();
		powerUps = new ArrayList<PowerUp>();
		
		
		addMouseMotionListener(theMouseListener);
		addKeyListener(theKeyListener);
		
		running = true;
		extraBallActive = false;
		image = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
/**
 * when the game is running, it will call drawStart, update, draw, and repaint. The "speed" of the ball is referenced by using Thread.sleep. I have set it to 10 for a decent level of difficulty :)	
 */
	public void run()
	{
		drawStart();
		
		//Game loop
		while(running)
		{
			//update
			update();
			
			//render or draw
			draw();
			
			//display
			repaint();
			
			try
			{
				Thread.sleep(10);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * general code for if the ball and the extra ball collide with the paddle, bricks, and power ups
	 */
	public void checkCollisions()
	{
		Rectangle ballRectangle = theBall.getRectangle();
		Rectangle paddleRectangle = thePaddle.getRectangle();
		Rectangle extraBallRectangle = theExtraBall.getRectangle();
		
		checkPowerUpCollisions(paddleRectangle);
		
		checkBallPaddleCollisions(theBall, ballRectangle, paddleRectangle);
		
		if(extraBallActive)
		{
			checkBallPaddleCollisions(theExtraBall, extraBallRectangle, paddleRectangle);
		}
		
	}
	
	/*public void restart()
	{
		theMap = new Map(5, 12);
		mousex = 0;
		theExtraBall = new Ball();
		theScore = new Score();
		powerUps = new ArrayList<PowerUp>();
		
	
		running = true;
		extraBallActive = false;
		image = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		repaint();
		run();
		revalidate();
	}
	
	/**
	 * code for when the ball collides with the paddle. It's direction is changed during collision for more interesting play
	 * @param theBall
	 * @param ballRectangle
	 * @param paddleRectangle
	 */
	public void checkBallPaddleCollisions(Ball theBall, Rectangle ballRectangle, Rectangle paddleRectangle) 
	{
		
		if(ballRectangle.intersects(paddleRectangle))
		{
			theBall.setY(thePaddle.YPOS - theBall.getSize());
			
			theBall.setDy(-theBall.getDy());
			
			if(theBall.getX() + theBall.getSize() < mousex - thePaddle.getWidth()/3)
			{
				theBall.setDx(theBall.getDx() - .9);
			}
			
			if(theBall.getX() >= mousex + thePaddle.getWidth()/3)
			{
				theBall.setDx(theBall.getDx() + .9);
			}
		}
		
		for(int row = 0; row < theMap.getMapArray().length; row++)
		{
			for(int col = 0; col < theMap.getMapArray()[0].length; col++)
			{
				if(theMap.getMapArray()[row][col] > 0)
				{
					int brickX = col * theMap.getBrickWidth() + theMap.HOR_PAD;
					int brickY = row * theMap.getBrickHeight() + theMap.VERT_PAD;
					int brickWidth = theMap.getBrickWidth();
					int brickHeight = theMap.getBrickHeight();
					
					Rectangle brickRectangle = new Rectangle(brickX, brickY, brickWidth, brickHeight);
					
					if(ballRectangle.intersects(brickRectangle)) 
					{
						
						if(theMap.getMapArray()[row][col] > 4)
						{
							powerUps.add(new PowerUp(brickX, brickY, theMap.getMapArray()[row][col], brickWidth, brickHeight));
							theMap.setBrick(row,  col,  4);
						}
						else
						{
							theMap.hitBrick(row, col);
						}
						
						if(ballRectangle.getMaxX() <= brickX + 5) 
						{
							theBall.setDx(-theBall.getDx());
							theBall.setX(brickX - 1);
						} 
						else if(ballRectangle.getMinX() >= brickRectangle.getMaxX() - 5) 
						{
							theBall.setDx(-theBall.getDx());
							theBall.setX(brickX + theMap.getBrickWidth() + 1);
						} 
						else 
						{
							theBall.setDy(-theBall.getDy());
						}
						
						theScore.addScore(50);
						
					}
				}
			}
		}
	}
	
	/**
	 * code for when the ball collides with the power up bricks. It will set setWasUsed to true if collision occurs
	 * @param paddleRectangle
	 */
	public void checkPowerUpCollisions(Rectangle paddleRectangle)
	{

		for(int i = 0; i < powerUps.size(); i++)
		{
			Rectangle powerUpRectangle = powerUps.get(i).getRectangle();
			
			if(paddleRectangle.intersects(powerUpRectangle))
			{
					if(powerUps.get(i).getType() == PowerUp.WIDEPADDLE && !powerUps.get(i).getWasUsed()) 
					{
						thePaddle.setWidth();
						powerUps.get(i).setWasUsed(true);
					}
					if(powerUps.get(i).getType() == PowerUp.FASTBALL && !powerUps.get(i).getWasUsed()) 
					{
						theBall.fastBall();
						powerUps.get(i).setWasUsed(true);
					}
					if(powerUps.get(i).getType() == PowerUp.NARROWPADDLE && !powerUps.get(i).getWasUsed()) 
					{
						thePaddle.shrinkWidth();
						powerUps.get(i).setWasUsed(true);
					}
					if(powerUps.get(i).getType() == PowerUp.EXTRABALL && !powerUps.get(i).getWasUsed()) 
					{
						extraBallActive = true;
						int dx = (int)(Math.random() * 6 - 3);
						theExtraBall = new Ball();
						powerUps.get(i).setWasUsed(true);
						extraBallTimer = System.currentTimeMillis();
					}
					
			}
		}
	}	
	
	/**
	 * updates ball, power ups, paddle, and extra ball
	 */
	public void update() 
	{
		checkCollisions();
		theBall.update();
		thePaddle.update();
		
		for(PowerUp powerup: powerUps)
		{
			powerup.update();
		}
		
		if(extraBallActive) 
		{
			theExtraBall.update();
		}
		if((System.currentTimeMillis() - extraBallTimer) > 9000 && extraBallActive) 
		{
			extraBallActive = false;
		}
	}
	
	/**
	 * draws everything necessary for the game (background, map, ball, paddle, score, power ups, and extra ball(s))
	 */
	public void draw()
	{
		//draws background
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
		
		theMap.draw(g);
		theBall.draw(g);
		thePaddle.draw(g);
		theScore.draw(g);
		
		drawPowerUps();
		
		if(theMap.win() == true)
		{
			drawWin();
			running = false;
		}
		
		if(theBall.lose() || theExtraBall.lose())
		{
			drawLoser();
			running = false;
		}
		
		if(extraBallActive) 
		{
			theExtraBall.draw(g);
		}
	}
	
	/**
	 * draws Loading/Starting Screen. Includes the name of the game, a key for the brick colors, and a count down for the game initiation
	 */
	public void drawStart() 
	{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT);
			
			
			g.setColor(Color.RED);
			g.setFont(new Font("Courier New", Font.BOLD, 70));
			g.drawString("BLAZE'S BRICK", 40, 80);
			g.drawString("BREAKER!!!", 110, 160);
			g.setColor(Color.YELLOW);
			g.setFont(new Font("Courier New", Font.BOLD, 10));
			g.drawString("Key: ", 20, 300);
			g.drawString("Red = 4X ", 20, 312);
			g.drawString("Orange = 3X ", 20, 324);
			g.drawString("Yellow = 2X ", 20, 336);
			g.drawString("Green = 1X ", 20, 348);
			g.drawString("White = Extra Ball ", 20, 360);
			g.drawString("Light Pink = Fast Ball ", 20, 372);
			g.drawString("Blue = Wide Paddle ", 20, 384);
			g.drawString("Pink = Narrow Paddle ", 20, 396);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier New", Font.BOLD, 60));
			g.drawString("Starting in", 110, 260);
			repaint();
			try 
			{ 
				Thread.sleep(1800);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
			
			
			g.setColor(Color.RED);
			g.setFont(new Font("Courier New", Font.BOLD, 70));
			g.drawString("BLAZE'S BRICK", 40, 80);
			g.drawString("BREAKER!!!", 110, 160);
			g.setColor(Color.YELLOW);
			g.setFont(new Font("Courier New", Font.BOLD, 10));
			g.drawString("Key: ", 20, 300);
			g.drawString("Red = 4X ", 20, 312);
			g.drawString("Orange = 3X ", 20, 324);
			g.drawString("Yellow = 2X ", 20, 336);
			g.drawString("Green = 1X ", 20, 348);
			g.drawString("White = Extra Ball ", 20, 360);
			g.drawString("Light Pink = Fast Ball ", 20, 372);
			g.drawString("Blue = Wide Paddle ", 20, 384);
			g.drawString("Pink = Narrow Paddle ", 20, 396);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier New", Font.BOLD, 80));
			g.drawString("3", 280, 260);
			repaint();
			try 
			{ 
				Thread.sleep(500);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
			
			
			g.setColor(Color.RED);
			g.setFont(new Font("Courier New", Font.BOLD, 70));
			g.drawString("BLAZE'S BRICK", 40, 80);
			g.drawString("BREAKER!!!", 110, 160);
			g.setColor(Color.YELLOW);
			g.setFont(new Font("Courier New", Font.BOLD, 10));
			g.drawString("Key: ", 20, 300);
			g.drawString("Red = 4X ", 20, 312);
			g.drawString("Orange = 3X ", 20, 324);
			g.drawString("Yellow = 2X ", 20, 336);
			g.drawString("Green = 1X ", 20, 348);
			g.drawString("White = Extra Ball ", 20, 360);
			g.drawString("Light Pink = Fast Ball ", 20, 372);
			g.drawString("Blue = Wide Paddle ", 20, 384);
			g.drawString("Pink = Narrow Paddle ", 20, 396);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier New", Font.BOLD, 80));
			g.drawString("2", 280, 260);
			repaint();
			try 
			{ 
				Thread.sleep(1000);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
			
			
			g.setColor(Color.RED);
			g.setFont(new Font("Courier New", Font.BOLD, 70));
			g.drawString("BLAZE'S BRICK", 40, 80);
			g.drawString("BREAKER!!!", 110, 160);
			g.setColor(Color.YELLOW);
			g.setFont(new Font("Courier New", Font.BOLD, 10));
			g.drawString("Key: ", 20, 300);
			g.drawString("Red = 4X ", 20, 312);
			g.drawString("Orange = 3X ", 20, 324);
			g.drawString("Yellow = 2X ", 20, 336);
			g.drawString("Green = 1X ", 20, 348);
			g.drawString("White = Extra Ball ", 20, 360);
			g.drawString("Light Pink = Fast Ball ", 20, 372);
			g.drawString("Blue = Wide Paddle ", 20, 384);
			g.drawString("Pink = Narrow Paddle ", 20, 396);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier New", Font.BOLD, 80));
			g.drawString("1", 280, 260);
			repaint();
			try 
			{ 
				Thread.sleep(1500);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
			
			
			g.setColor(Color.RED);
			g.setFont(new Font("Courier New", Font.BOLD, 70));
			g.drawString("BLAZE'S BRICK", 40, 80);
			g.drawString("BREAKER!!!", 110, 160);
			g.setColor(Color.YELLOW);
			g.setFont(new Font("Courier New", Font.BOLD, 10));
			g.drawString("Key: ", 20, 300);
			g.drawString("Red = 4X ", 20, 312);
			g.drawString("Orange = 3X ", 20, 324);
			g.drawString("Yellow = 2X ", 20, 336);
			g.drawString("Green = 1X ", 20, 348);
			g.drawString("White = Extra Ball ", 20, 360);
			g.drawString("Light Pink = Fast Ball ", 20, 372);
			g.drawString("Blue = Wide Paddle ", 20, 384);
			g.drawString("Pink = Narrow Paddle ", 20, 396);
			g.setColor(Color.WHITE);
			g.setFont(new Font("Courier New", Font.BOLD, 80));
			g.drawString("Go!", 260, 260);
			repaint();
			try 
			{ 
				Thread.sleep(1750);
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
			g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
			
	}
	
	/**
	 * if the player Wins the game, the Winner screen will appear.
	 */
	public void drawWin()
	{
		g.setColor(Color.CYAN);
		g.setFont(new Font("Courier New", Font.BOLD, 50));
		g.drawString("Winner!", 225, 260);
		
		//g.setFont(new Font("serif", Font.BOLD, 30));
		//g.drawString("Press Enter to Restart", 170, 315);
	}
	
	/**
	 * if the player Loses the game, the Loser screen will appear.
	 */
	public void drawLoser()
	{
		g.setColor(Color.CYAN);
		g.setFont(new Font("Courier New", Font.BOLD, 50));
		g.drawString("Loser!", 225, 260);
		
		//g.setFont(new Font("serif", Font.BOLD, 30));
		//g.drawString("Press Enter to Restart", 170, 315);
	}
	
	/**
	 * draws Power ups for the game
	 */
	public void drawPowerUps()
	{
		for(PowerUp powerup: powerUps)
		{
			powerup.draw(g);
		}
	}
	
	/**
	 * paintComponent method necessary to paint everything
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(image, 0, 0, Main.WIDTH, Main.HEIGHT, null);
		g2.dispose();
	}

	/**
	 * 
	 * @author Arun Agarwal Period 7
	 * code to make Paddle move with mouse
	 */
	private class MyMouseMotionListener implements MouseMotionListener 
	{

		public void mouseDragged(MouseEvent e) 
		{	
		}

		public void mouseMoved(MouseEvent e) 
		{
			mousex = e.getX();
			thePaddle.mouseMoved(e.getX());
		}
	}
	
	/*/**
	 * 
	 * @author Arun Agarwal Period 7
	 * Key Listener to restart game on press of enter
	 */
	private class MyKeyListener implements KeyListener 
	{
		
		public void keyPressed(KeyEvent e) 
		{
			
			//if(e.getKeyCode() == KeyEvent.VK_ENTER)
			//{
			//	System.out.print("almost");
			//	if(!running)
			//	{
			//		System.out.print("restart");
			//		restart();	
			//	}
			//}
		}
		public void keyReleased(KeyEvent e) {}
		
		public void keyTyped(KeyEvent e) {}
	}
}