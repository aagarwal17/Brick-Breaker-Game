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

import javax.swing.JPanel;


/**
 * 
 * @author Arun Agarwal Period 7
 *
 */
public class MapPanel2 extends JPanel implements KeyListener
{
	//Fields
	private boolean running;
	private BufferedImage image;
	private Graphics2D g;
	private MyMouseMotionListener theMouseListener;
	private int mousex;

	private Ball theBall;
	private Paddle thePaddle;
	private Map theMap;
	private Score theScore;
	private ArrayList<PowerUp> powerUps;
	
	//constructor
	public MapPanel2()
	{
		initialize();
	}
	
	public void initialize()
	{
		mousex = 0;
		theBall = new Ball();
		theBall.setBallSize(10);
		thePaddle = new Paddle(60,10);
		theMap = new Map(8, 14);
		theScore = new Score();
		theMouseListener = new MyMouseMotionListener();
		powerUps = new ArrayList<PowerUp>();
		
		
		addMouseMotionListener(theMouseListener);
		
		running = true;
		image = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	
	public void run()
	{
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
				Thread.sleep(30);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
	
	
	public void checkCollisions()
	{
		Rectangle ballRectangle = theBall.getRectangle();
		Rectangle paddleRectangle = thePaddle.getRectangle();
		
		
		for(int i = 0; i < powerUps.size(); i++)
		{
			Rectangle powerUpRectangle = powerUps.get(i).getRectangle();
			if(paddleRectangle.intersects(powerUpRectangle))
			{
				int powerupX = powerUps.get(i).getX();
				int powerupY = powerUps.get(i).getY();
				if(paddleRectangle.intersects(powerUpRectangle)) 
				{
					if(powerUps.get(i).getType() == PowerUp.WIDEPADDLE && !powerUps.get(i).getWasUsed()) 
					{
						thePaddle.setWidth(thePaddle.getWidth() * 2);
						powerUps.get(i).setWasUsed(true);
					}
					if(powerUps.get(i).getType() == PowerUp.FASTBALL && !powerUps.get(i).getWasUsed()) 
					{
						//theBall.fastBall();
					//	theBall.setDrawTail(true);
						powerUps.get(i).setWasUsed(true);
					}
					
				}
			}
		}	
		
		if(ballRectangle.intersects(paddleRectangle))
		{
			theBall.setDy(-theBall.getDy());
			
			if(theBall.getX() < mousex + thePaddle.getWidth()/4)
			{
				theBall.setDx(theBall.getDx() - .7);
			}
			
			if(theBall.getX() < mousex + thePaddle.getWidth() && theBall.getX() > mousex + thePaddle.getWidth()/4)
			{
				theBall.setDx(theBall.getDx() - .7);
			}
		}
		
		A: for(int row = 0; row < theMap.getMapArray().length; row++)
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
							theMap.setBrick(row,  col,  5);
						}
						else
						{
							theMap.hitBrick(row, col);
						}
						
						theMap.hitBrick(row,  col);
						
						theBall.setDy(-theBall.getDy());
						
						theScore.addScore(100);
						
						break A;
					}
				}
			}
		}
	}
	
	public void update() 
	{
		checkCollisions();
		theBall.update();
		thePaddle.update();
		
		for(PowerUp powerup: powerUps)
		{
			powerup.update();
		}
	}
	
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
		
		if(theBall.lose())
		{
			drawLoser();
			running = false;
		}
	}
	
	public void drawWin()
	{
		g.setColor(Color.CYAN);
		g.setFont(new Font("Courier New", Font.BOLD, 50));
		g.drawString("Winner!", 225, 260);
		
		g.setFont(new Font("serif", Font.BOLD, 30));
		g.drawString("Press Enter to Restart", 170, 320);
	}
	
	public void drawLoser()
	{
		g.setColor(Color.CYAN);
		g.setFont(new Font("Courier New", Font.BOLD, 50));
		g.drawString("Loser!", 225, 260);
		
		g.setFont(new Font("serif", Font.BOLD, 30));
		g.drawString("Press Enter to Restart", 170, 320);
	}
	
	public void drawPowerUps()
	{
		for(PowerUp powerup: powerUps)
		{
			powerup.draw(g);
		}
	}
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(image, 0, 0, Main.WIDTH, Main.HEIGHT, null);
		g2.dispose();
	}

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
	
	public void keyPressed(KeyEvent e) 
	{
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			if(!running)
			{
				running = true;
				
				theBall.setX(200);
				theBall.setY(200);
				
				theBall.setDx(2);
				theBall.setDy(6);
				
				thePaddle.setX(Main.WIDTH/2 - getWidth()/2);
				theScore.initialize();
				theMap.initializeMap(8, 14);
				
				run();
			}
		}
		
		
	}
	
	public void keyReleased(KeyEvent e) {}

	public void keyTyped(KeyEvent e) {}
}
		