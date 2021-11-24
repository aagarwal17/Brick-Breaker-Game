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
	private int width;
	private int height;
	private int startWidth;
	private int startHeight;
	private long widthTimer;
	private boolean altWidth;
	private double targetX;
	
	public final int YPOS = Main.HEIGHT - 100;
	
	//Constructor
	public Paddle(int theWidth, int theHeight)
	{
		altWidth = false;
		width = theWidth;
		startWidth = theWidth;
		height = theHeight;
		
		x = Main.WIDTH/2 - width/2;
	}
	
	//update
	public void update()
	{
		if((System.nanoTime() - widthTimer)/1000 > 9000000)
		{
			width = startWidth;
			altWidth = false;
		}
		
		x += (targetX - x) * .3;
	}
	
	//draw
	public void draw(Graphics2D g)
	{
		
		g.setColor(Color.BLUE);
		g.fillRect((int) x, YPOS, width, height);
		
		if(altWidth == true)
		{
			g.setColor(Color.CYAN);
			g.setFont(new Font("Courier New", Font.BOLD, 18));
			g.drawString("Shrinking in " + (9 - (System.nanoTime() - widthTimer)/1000000000),  (int) x, YPOS + 18);
		}
	}
	
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

	public Rectangle getRectangle()
	{
		return new Rectangle((int)x, YPOS, width, height);
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int newWidth)
	{
		altWidth = true;
		width = newWidth;
		setWidthTimer();
	}
	
	public void setWidthTimer()
	{
		widthTimer = System.nanoTime();
	}
	
	public double getX()
	{
		return x;
	}
	
	public void setX(double theX)
	{
		x = theX;
	}
}
