package views;

import java.awt.BorderLayout;

import java.util.Observable;
import java.util.Observer;


import javax.swing.JFrame;

import javax.swing.JOptionPane;


import controllers.Controller;

import models.Board;
import models.Game;
import models.GameStatus;
import models.PieceColor;

/*
 * The view that receives all user actions and lets the controller know.
 */
public class View extends JFrame implements Observer
{
	private Game game;
	private Board board;
	private Controller controller;
	
	private BoardPanel boardPanel;
	
	public View(Game game, Controller controller)
	{
		super("Chess");
		
		this.game = game;
		this.board = game.getBoard();
		this.controller = controller;
		game.addObserver(this);

		setLayout(new BorderLayout());
		
		boardPanel = new BoardPanel(controller, board);
		add(boardPanel, BorderLayout.CENTER);
		add(new RankIndicatorPanel(), BorderLayout.LINE_START);
		add(new RankIndicatorPanel(), BorderLayout.LINE_END);
		add(new FileIndicatorPanel(), BorderLayout.PAGE_START);
		add(new FileIndicatorPanel(), BorderLayout.PAGE_END);
		
		MenuBar menuBar = new MenuBar(controller);	
	    setJMenuBar(menuBar);
		
		setSize(800, 800);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	@Override
	public void update(Observable model, Object data)
	{		
		if (game.isModelValid())
			boardPanel.updateBoard();
		else
		{
			if (game.getGameStatus() == GameStatus.ONGOING)
			{
				JOptionPane.showMessageDialog(
						this, 
						game.getModelError(), 
						"Error!", 
						JOptionPane.ERROR_MESSAGE);
				
				game.makeModelValid();
			}
			else
			{
				game.makeModelValid();
				boardPanel.updateBoard();
				
				String message = null;
				
				if (game.getGameStatus() == GameStatus.WON)
				{
					if (game.getTurn() == PieceColor.WHITE)
						message = "White wins!\n" + "Would you like to play again?";
					else 
						message = "Black wins!\n" + "Would you like to play again?";
				}
					
				Object[] options = { "New Game!", "Exit Game" };
				int choice = JOptionPane.showOptionDialog(
						this, 
						message,
						"Game Over!", 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE,
						null, 
						options, 
						options[0]);
				
				switch (choice)
				{
					case 0:
						controller.handleMessage(Controller.MESSAGE_NEW_GAME, null);
						break;
					default:
						System.exit(0);
				}
			}
		}
	}
}
