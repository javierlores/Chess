package views;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import models.Board;

/*
 * Draws the file indicators
 */
public class FileIndicatorPanel extends JPanel
{
	private static final String[] FILES = { "a", "b", "c", "d", "e", "f", "g", "h"};
	
	public FileIndicatorPanel()
	{
		super();
		
		setLayout(new GridLayout(1, Board.COL + 2, 65, 0));
		
		add(new JLabel(""));
		for (int col = 1; col < Board.COL + 1; col++)
		{
			JLabel fileLabel = new JLabel(FILES[col - 1], SwingConstants.CENTER);
			fileLabel.setPreferredSize(new Dimension(32, 32));
			fileLabel.setFont(new Font("Serif", Font.PLAIN, 16));
			add(fileLabel);
		}
		add(new JLabel(""));
	}
}
