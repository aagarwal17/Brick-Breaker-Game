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
	public final static int NARROWPADDLE = 7;
	public final static int EXTRABALL = 8;
	
	public final static Color WIDECOLOR = Color.BLUE;
	public final static Color FASTCOLOR = Color.PINK;
	public final static Color NARROWCOLOR = Color.MAGENTA;
	public final static Color EXTRABALLCOLOR = Color.WHITE;
	
	// constructor
	
	public PowerUp(int xStart, int yStart, int theType, int theWidth, int theHeight) 
	{
		x = xStart;
		y = yStart;
		type = theType;
		width = theWidth;
		height = theHeight;
		
		if(type < 5) { type = 5; }
		if(type > 8) { type = 8; }
		
		if(type == WIDEPADDLE) 
		{ 
			color = WIDECOLOR; 
		}
		
		if(type == FASTBALL) 
		{ 
			color = FASTCOLOR; 
		}
		
		if(type == NARROWPADDLE) 
		{ 
			color = NARROWCOLOR; 
		}
		
		if(type == EXTRABALL) 
		{ 
			color = EXTRABALLCOLOR; 
		}
		
		dy = (int) (Math.random() * 5 + 3);
		
		wasUsed = false;
	}
	
	/**
	 * draw Power Ups
	 * @param g
	 */
	public void draw(Graphics2D g) 
	{
		g.setColor(color);
		g.fillRect(x, y, width, height);
	}
	
	/**
	 * updates status of Power Ups
	 */
	public void update() 
	{
		
		y += dy;
		
		if(y > Main.HEIGHT) 
		{
			isOnScreen = false;
		}
		
	}

	/**
	 * returns x value of PowerUp
	 * @return x
	 */
	public int getX() 
	{
		return x;
	}

	/**
	 * sets x value to the newX
	 * @param newX
	 */
	public void setX(int newX) 
	{
		x = newX;
	}
	
	/**
	 * returns y value of PowerUp
	 * @return y
	 */
	public int getY() 
	{ 
		return y;
	}
	
	/**
	 * sets y value to the newY
	 * @param newY
	 */
	public void setY(int newY) 
	{ 
		y = newY; 
	}
	
	/**
	 * returns the position of dy
	 * @return dy
	 */
	public int getDY() 
	{ 
		return dy; 
	}
	
	/**
	 * sets dy to the newDY
	 * @param newDY
	 */
	public void setDY(int newDY) 
	{ 
		dy = newDY; 
	}
	
	/**
	 * returns the type of Power Up
	 * @return type
	 */
	public int getType() 
	{ 
		return type; 
	}
	
	/**
	 * returns true or false depending on if the Power Up is on screen
	 * @return isOnScreen
	 */
	public boolean getIsOnScreen() 
	{ 
		return isOnScreen; 
	}
	
	/**
	 *  sets isOnScreen to onScreen (puts it on screen)
	 *  @param onScreen
	 */
	public void setIsOnScreen(boolean onScreen) 
	{ 
		isOnScreen = onScreen; 
	}
	
	/**
	 * returns whether the Power Up was used
	 * @return wasUSed
	 */
	public boolean getWasUsed() 
	{ 
		return wasUsed; 
	}
	
	/**
	 * sets wasUsed to used (says that it was used)
	 * @param used
	 */
	public void setWasUsed(boolean used) 
	{ 
		wasUsed = used; 
	}
	
	/**
	 * returns a rectangle for the Power Up
	 * @return new Rectangle
	 */
	public Rectangle getRectangle() 
	{
		return new Rectangle(x, y, width, height);	
	}
	
}