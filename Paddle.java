import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * 
 * @author Arun Agarwal Period 7
 *
 */
public class Paddle 
{
	//Fields
	private double x;
	private double width;
	private double height;
	private double startWidth;
	private double startHeight;
	private long widthTimer;
	private boolean altWidth;
	private double targetX;
	private double dx;
	
	public final int YPOS = Main.HEIGHT - 100;
	
	//Constructor
	public Paddle(int theWidth, int theHeight)
	{
		altWidth = false;
		width = theWidth;
		startWidth = theWidth;
		height = theHeight;
		dx = 0;
		
		x = Main.WIDTH/2 - width/2;
	}
	
	/**
	 * updates paddle
	 */
	public void update()
	{
		if((System.nanoTime() - widthTimer)/1000 > 9000000)
		{
			width = startWidth;
			altWidth = false;
		}
		
		x += (targetX - x) * .3;
	}
	
	/**
	 * draws paddle
	 * @param g
	 */
	public void draw(Graphics2D g)
	{
		
		g.setColor(Color.BLUE);
		g.fillRect((int) x, YPOS, (int) width, (int) height);
		
		if(altWidth == true)
		{
			g.setColor(Color.CYAN);
			g.setFont(new Font("Courier New", Font.BOLD, 18));
			g.drawString("Reverting in " + (9 - (System.nanoTime() - widthTimer)/1000000000),  (int) x, YPOS + 36);
		}
	}
	
	/**
	 * code for paddle boundaries with mouse
	 * @param mouseXPos
	 */
	public void mouseMoved(int mouseXPos)
	{
		targetX = mouseXPos - width/2;
		
		if(targetX > Main.WIDTH - width)
		{
			targetX = Main.WIDTH - width;
		}
		
		if(targetX < 0)
		{
			targetX = 0;
		}
	}

	/**
	 * returns a rectangle for the Paddle
	 * @return new Rectangle
	 */
	public Rectangle getRectangle()
	{
		return new Rectangle((int)x, YPOS, (int) width, (int) height);
	}
	
	/**
	 * returns the width of the paddle
	 * @return width
	 */
	public double getWidth()
	{
		return width;
	}
	
	/**
	 * sets the width of the Paddle to double the size for the WidePaddle Power Up
	 */
	public void setWidth() { 
		altWidth = true;
		width = width * 2; 
		if(width > 400) {
			width = 400;
		}
		setWidthTimer();
	}
	
	/**
	 * sets the width of the Paddle to half the size for the NarrowPaddle Power Up
	 */
	public void shrinkWidth() { 
		altWidth = true;
		width = width / 2; 
		setWidthTimer();
	}
	
	
	/**
	 * sets the WidthTimer to nano time/seconds
	 */
	public void setWidthTimer()
	{
		widthTimer = System.nanoTime();
	}
	
	/**
	 * returns the x position of the Paddle
	 * @return x
	 */
	public double getX()
	{
		return x;
	}
	
	/**
	 * sets the x value of the paddle to theX
	 * @param theX
	 */
	public void setX(double theX)
	{
		x = theX;
	}
}
