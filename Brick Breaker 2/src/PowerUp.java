import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
/**
 * 
 * @author Arun Agarwal Period 7
 *
 */
public class PowerUp 
{

	// Fields
	private int x;
	private int y;
	private int dy;
	private int type;
	private int width;
	private int height;

	private boolean isOnScreen;
	private boolean wasUsed;
	
	private Color color;
	
	public final static int WIDEPADDLE = 5;
	public final static int FASTBALL = 6;
	
	public final static Color WIDECOLOR = Color.MAGENTA;
	public final static Color FASTCOLOR = Color.PINK;
	
	// constructor
	
	public PowerUp(int xStart, int yStart, int theType, int theWidth, int theHeight) 
	{
		x = xStart;
		y = yStart;
		type = theType;
		width = theWidth;
		height = theHeight;
		
		if(type < 5) { type = 5; }
		if(type > 6) { type = 6; }
		
		if(type == WIDEPADDLE) 
		{ 
			color = WIDECOLOR; 
		}
		if(type == FASTBALL) 
		{ 
			color = FASTCOLOR; 
		}
		
		dy = (int) (Math.random() * 5 + 3);
		
		wasUsed = false;
	}
	
	public void draw(Graphics2D g) 
	{
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}
	
	public void update() 
	{
		
		y += dy;
		
		if(y > Main.HEIGHT) 
		{
			isOnScreen = false;
		}
		
	}

	public int getX() 
	{
		return x;
	}

	public void setX(int newX) 
	{
		x = newX;
	}
	
	public int getY() 
	{ 
		return y;
	}
	
	public void setY(int newY) 
	{ 
		y = newY; 
	}
	
	public int getDY() 
	{ 
		return dy; 
	}
	
	public void setDY(int newDY) 
	{ 
		dy = newDY; 
	}
	
	public int getType() 
	{ 
		return type; 
	}
	
	public boolean getIsOnScreen() 
	{ 
		return isOnScreen; 
	}
	
	public void setIsOnScreen(boolean onScreen) 
	{ 
		isOnScreen = onScreen; 
	}
	
	public boolean getWasUsed() 
	{ 
		return wasUsed; 
	}
	
	public void setWasUsed(boolean used) 
	{ 
		wasUsed = used; 
	}
	
	public Rectangle getRectangle() 
	{
		return new Rectangle(x, y, width, height);	
	}
	
}