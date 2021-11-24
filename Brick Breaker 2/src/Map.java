import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * 
 * @author Arun Agarwal Period 7
 *
 */
public class Map 
{
	//fields
	private int [][] theMap;
	private int brickWidth;
	private int brickHeight;
	
	public final int HOR_PAD = 80;
	public final int VERT_PAD = 50;
	
	//constructor
	public Map(int row, int col)
	{
		initializeMap(row, col);
		
		brickWidth = (Main.WIDTH - 2 * HOR_PAD)/col;
		brickHeight = (Main.HEIGHT/2 - VERT_PAD * 2)/row;
	}
	
	public void initializeMap(int row, int col)
	{
		theMap = new int[row][col];
		
		for(int i = 0; i < theMap.length; i++) 
		{
			for(int j = 0; j < theMap[0].length; j++) 
			{
				int r = (int)(Math.random() * 4 + 1);
				theMap[i][j] = r;
			}
		}
		
		for(int i = 0; i < 4; i ++) 
		{
			int r = (int)(Math.random() * theMap.length);
			int c = (int)(Math.random() * theMap[0].length);
			int type = (int)(Math.random() * 2 + 5);
			theMap[r][c] = type;
		}
	}
	
	public void draw(Graphics2D g)
	{
	
		for(int row = 0; row < theMap.length; row++) 
		{
			for(int col = 0; col < theMap[0].length; col++) 
			{
				if(theMap[row][col] > 0)
				{
					if(theMap[row][col] == 1)
					{
						g.setColor(Color.GREEN);
					}
					if(theMap[row][col] == 2)
					{
						g.setColor(Color.YELLOW);
					}
					if(theMap[row][col] == 3)
					{
						g.setColor(Color.ORANGE);
					}
					if(theMap[row][col] == 4)
					{
						g.setColor(Color.RED);
					}
					if(theMap[row][col] == PowerUp.WIDEPADDLE) 
					{
						g.setColor(PowerUp.WIDECOLOR);
					}
					if(theMap[row][col] == PowerUp.FASTBALL) 
					{
						g.setColor(PowerUp.FASTCOLOR);
					}
				g.fillRect(col * brickWidth + HOR_PAD, row * brickHeight + VERT_PAD, brickWidth, brickHeight);
				g.setStroke(new BasicStroke(2));
				g.setColor(Color.BLACK);
				g.drawRect(col * brickWidth + HOR_PAD, row * brickHeight + VERT_PAD, brickWidth, brickHeight);
				}
			}
		}
	}
	
	public boolean win()
	{
		boolean thereIsAWin = false;
		
		int bricksRemaining = 0;
		
		for(int row = 0; row < theMap.length; row++) {
			for(int col = 0; col < theMap[0].length; col++) {
				bricksRemaining += theMap[row][col];
			}
		}
		
		if(bricksRemaining == 0) {
			thereIsAWin = true;
		}
		
		return thereIsAWin;
	}
	public int[][] getMapArray()
	{
		return theMap;
	}
	
	public void setBrick(int row, int col, int value)
	{
		theMap[row][col] = value;
	}
	
	public int getBrickWidth()
	{
		return brickWidth;
	}
	
	public int getBrickHeight()
	{
		return brickHeight;
	}
	
	public void hitBrick(int row, int col)
	{
		theMap[row][col] -= 1;
		if(theMap[row][col] < 0)
		{
			theMap[row][col] = 0;
		}
	}
}
