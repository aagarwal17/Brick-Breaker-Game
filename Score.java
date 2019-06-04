import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * 
 * @author Arun Agarwal Period 7
 *
 */
public class Score 
{

	//Fields
	private int score;
	
	//Constructor
	public Score()
	{
		initialize();
	}
	
	/**
	 * initializes score to 0;
	 */
	public void initialize()
	{
		score = 0;
		
	}
	/**
	 * draw Score label
	 * @param g
	 */
	public void draw(Graphics2D g)
	{
		g.setFont(new Font("Courier New", Font.BOLD, 14));
		g.setColor(Color.CYAN);
		g.drawString("Score: " + score, 20, 20);
	}
	
	/**
	 * returns player's score
	 * @return score
	 */
	public int getScore()
	{
		return score;
	}
	
	/**
	 * adds scoreToAdd to total score
	 * @param scoreToAdd
	 */
	public void addScore(int scoreToAdd)
	{
		score += scoreToAdd;
	}
}
