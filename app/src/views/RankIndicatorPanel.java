package views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import models.Board;

/*
 * Draws the rank indicators
 */
public class RankIndicatorPanel extends JPanel 
{
	private static final String[] RANKS = { "1", "2", "3", "4", "5", "6", "7", "8"};
	
	public RankIndicatorPanel()
	{
		super();
		
		setLayout(new GridLayout(Board.ROW, 1));
		
		for (int row = 0; row < Board.ROW; row++)
		{
			JLabel rankLabel = new JLabel(RANKS[(RANKS.length - 1)- row], SwingConstants.CENTER);
			rankLabel.setPreferredSize(new Dimension(32, 32));
			rankLabel.setFont(new Font("Serif", Font.PLAIN, 16));
			add(rankLabel);
		}
	}
}
