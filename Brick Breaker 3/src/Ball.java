import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * 
 * @author Arun Agarwal Pd. 7
 *
 */
public class Ball 
{
	//Fields
	private double x;
	private double y;
	private double dx;
	private double dy;
	private double startDy;
	private int ballSize = 20;
	private boolean altSpeed;
	private long speedTimer;
	
	//constructor
	public Ball()
	{
		x = 200;
		y = 200;
		dx = 1;
		dy = 3;
		startDy = 3;
		altSpeed = false;
		
	}
	
	/**
	 * updates ball
	 */
	public void update()
	{
		if((System.nanoTime() - speedTimer) / 1000 > 4000000 && altSpeed) 
		{
			if(dy < 0) {
				dy = -startDy;
			} else {
				dy = startDy;
			}
			
			altSpeed = false;
		}
		
		setPosition();
	}
	
	/**
	 * sets Start Position of Ball
	 */
	public void setPosition()
	{
		x += dx;
		y += dy;
		
		if(x < 0)
		{
			x = 0;
			dx = -dx;
		}
		if(y < 0)
		{
			y = 0;
			dy = -dy;
		}
		if(x > Main.WIDTH - ballSize)
		{
			dx = -dx;
			x = Main.WIDTH - ballSize;
		}
		if(y > Main.HEIGHT - ballSize)
		{
			dy = -dy;
			y = Main.HEIGHT - ballSize;
		}
	}
	
	/**
	 * draws Ball
	 * @param g
	 */
	public void draw(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(4));
		g.drawOval((int)x,(int) y, ballSize, ballSize);
		
		if(altSpeed == true)
		{
			g.setColor(Color.YELLOW);
			g.setFont(new Font("Courier New", Font.BOLD, 12));
			g.drawString("Speed Reverting in " + (7 - (System.nanoTime() - speedTimer)/500000000),  440, 20);
		}
	}
	/**
	 * Gets rectangle around ball
	 * @return rectangle around ball
	 */
	public Rectangle getRectangle()
	{
		return new Rectangle((int)x,(int) y, ballSize, ballSize);
	}
	
	/**
	 * sets the change in y direction to theDy
	 * @param theDy
	 */
	public void setDy(double theDy)
	{
		dy = theDy;
	}
	/**
	 * returns the change in y
	 * @return dy
	 */
	public double getDy()
	{
		return dy;
	}
	
	/**
	 * sets the change in x direction to theDx
	 * @param theDx
	 */
	public void setDx(double theDx)
	{
		dx = theDx;
	}
	
	/**
	 * returns the change in x
	 * @return dx
	 */
	public double getDx()
	{
		return dx;
	}
	
	/**
	 * returns the x position of the ball
	 * @return x
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * returns the y position of the ball
	 * @return y
	 */
	public int getY()
	{
		return (int) y;
	}
	
	/**
	 * sets the x position to theX
	 * @param theX
	 */
	public void setX(double theX)
	{
		x = theX;
	}
	
	/**
	 * returns the size of the Ball
	 * @return ballSize
	 */
	public int getSize()
	{
		return ballSize;
	}
	
	/**
	 * sets the size of the Ball to theSize
	 * @param theSize
	 */
	public void setBallSize(int theSize)
	{
		ballSize = theSize;
	}
	
	/**
	 * sets the y position to theY
	 * @param theY
	 */
	public void setY(double theY)
	{
		y = theY;
	}
	
	/**
	 * determines if player has lost game or not and returns loser
	 * @return loser
	 */
	public boolean lose()
	{
		boolean loser = false;
		
		if(y > Main.HEIGHT - ballSize * 2) {
			loser = true;
		}
		
		return loser;
	}
	
	/**
	 * makes altSpeed true and doubles the change in y direction
	 */
	public void fastBall() 
	{ 
		speedTimer = System.nanoTime();
		altSpeed = true;
		dy += dy;
	}
	
	/**
	 * Sets speed timer to System.nanoTime()
	 */
	public void setSpeedTimer()
	{
		speedTimer = System.nanoTime();
	}
}
