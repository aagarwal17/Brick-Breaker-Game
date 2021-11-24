import java.awt.BasicStroke;
import java.awt.Color;
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
	private int ballSize = 20;
	
	
	public Ball()
	{
		x = 200;
		y = 200;
		dx = 1;
		dy = 3;
		
	}
	
	public void update()
	{
		setPosition();
	}
	
	
	public void setPosition()
	{
		x += dx;
		y += dy;
		
		if(x < 0)
		{
			dx = -dx;
		}
		if(y < 0)
		{
			dy = -dy;
		}
		if(x > Main.WIDTH - ballSize)
		{
			dx = -dx;
		}
		if(y > Main.HEIGHT - ballSize)
		{
			dy = -dy;
		}
	}
	
	public void draw(Graphics2D g)
	{
		g.setColor(Color.WHITE);
		g.setStroke(new BasicStroke(4));
		g.drawOval((int)x,(int) y, ballSize, ballSize);
	}
	
	public Rectangle getRectangle()
	{
		return new Rectangle((int)x,(int) y, ballSize, ballSize);
	}
	
	public void setDy(double theDy)
	{
		dy = theDy;
	}
	public double getDy()
	{
		return dy;
	}
	
	public void setDx(double theDx)
	{
		dx = theDx;
	}
	
	public double getDx()
	{
		return dx;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void setX(double theX)
	{
		x = theX;
	}
	
	public int getSize()
	{
		return ballSize;
	}
	
	public void setBallSize(int theSize)
	{
		ballSize = theSize;
	}
	
	public void setY(double theY)
	{
		y = theY;
	}
	
	public boolean lose()
	{
		boolean loser = false;
		
		if(y > Main.HEIGHT - ballSize * 2) {
			loser = true;
		}
		
		return loser;
	}
}
