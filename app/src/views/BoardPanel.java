package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import controllers.Controller;

import models.Bishop;
import models.Board;
import models.King;
import models.Knight;
import models.Pawn;
import models.Piece;
import models.Queen;
import models.Rook;
import models.Square;

public class BoardPanel extends JPanel
{
	private BoardCell[][] boardCells = new BoardCell[Board.ROW][Board.COL];
	private Controller controller;
	private Board board;
	
	private Icon blackPawn = new ImageIcon(getClass().getResource("/black_pawn.png"));
	private Icon blackRook = new ImageIcon(getClass().getResource("/black_rook.png"));
	private Icon blackKnight = new ImageIcon(getClass().getResource("/black_knight.png"));
	private Icon blackBishop = new ImageIcon(getClass().getResource("/black_bishop.png"));
	private Icon blackQueen = new ImageIcon(getClass().getResource("/black_queen.png"));
	private Icon blackKing = new ImageIcon(getClass().getResource("/black_king.png"));
	private Icon whitePawn = new ImageIcon(getClass().getResource("/white_pawn.png"));
	private Icon whiteRook = new ImageIcon(getClass().getResource("/white_rook.png"));
	private Icon whiteKnight = new ImageIcon(getClass().getResource("/white_knight.png"));
	private Icon whiteBishop = new ImageIcon(getClass().getResource("/white_bishop.png"));
	private Icon whiteQueen = new ImageIcon(getClass().getResource("/white_queen.png"));
	private Icon whiteKing = new ImageIcon(getClass().getResource("/white_king.png"));
	
	private static final String[] PROMOTION_TYPES = {"Queen", "Bishop", "Knight", "Rook"};
	
	public BoardPanel(Controller controller, Board board)
	{
		super();
		this.controller = controller;
		this.board = board;
		
		setLayout(new GridLayout(Board.ROW, Board.COL));
		
		Border raisedBevel = BorderFactory.createRaisedBevelBorder();
		Border loweredBevel = BorderFactory.createLoweredBevelBorder();
		setBorder(BorderFactory.createCompoundBorder(raisedBevel, loweredBevel));
		
		MouseListener mouseListener = new MouseListener();
		
		for (int row = 0; row < Board.ROW; row++)
		{
			for (int col = 0; col < Board.COL; col++)
			{
				boardCells[row][col] = new BoardCell(new BorderLayout(), row, col);
				boardCells[row][col].addMouseListener(mouseListener);
				add(boardCells[row][col]);
				
				paintCell(boardCells[row][col]);
			}
		}
	}
	
	public void updateBoard()
	{
		Square square;
		
		for(int row = 0; row < Board.ROW; row++)
		{
			for(int col = 0; col < Board.COL; col++)
			{
				square = board.getSquare(row, col);
				boardCells[row][col].removeAll();
				paintCell(boardCells[row][col]);
				
				if (square.isOccupied())
				{
					if (isPromoteablePawn(square))
							promotePawn(square);
					
					if (isKingInCheck(square))
						boardCells[square.getRow()][square.getCol()].setBackground(Color.RED);
					
					if (square.isHighlighted())
						boardCells[square.getRow()][square.getCol()]
								.setBorder(BorderFactory.createLineBorder(Color.RED, 5));
					else if (square.isSelected())
						boardCells[square.getRow()][square.getCol()]
								.setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
					else
						boardCells[square.getRow()][square.getCol()].setBorder(null);

					boardCells[row][col].add(getLabel(square.getPiece()), 
							BorderLayout.CENTER);
					
					boardCells[row][col].revalidate();
					boardCells[row][col].repaint();
				}
				else
				{
					if (square.isHighlighted())
						boardCells[square.getRow()][square.getCol()]
								.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 5));
					else if (square.isCastleable())
						boardCells[square.getRow()][square.getCol()]
								.setBorder(BorderFactory.createLineBorder(Color.CYAN, 5));
					else
						boardCells[square.getRow()][square.getCol()].setBorder(null);
					
					boardCells[row][col].revalidate();
					boardCells[row][col].repaint();
				}
			}
		}
	}
	
	private void paintCell(BoardCell cell)
	{
		int row = cell.getRow();
		int col = cell.getCol();

		if (row % 2 == 0)
		{
			if (col % 2 != 0)
				cell.setBackground(Color.DARK_GRAY);
			else
				cell.setBackground(Color.WHITE);
		}
		else
		{
			if (col % 2 == 0)
				cell.setBackground(Color.DARK_GRAY);
			else
				cell.setBackground(Color.WHITE);
		}
	}
	
	private JLabel getLabel(Piece piece)
	{
		if (piece.isBlack())
		{
			if (piece instanceof Pawn)
				return new JLabel(blackPawn);
			else if (piece instanceof Rook)
				return new JLabel(blackRook);
			else if (piece instanceof Knight)
				return new JLabel(blackKnight);
			else if (piece instanceof Bishop)
				return new JLabel(blackBishop);
			else if (piece instanceof Queen)
				return new JLabel(blackQueen);
			else if (piece instanceof King)
				return new JLabel(blackKing);
		} else {
			if (piece instanceof Pawn)
				return new JLabel(whitePawn);
			else if (piece instanceof Rook)
				return new JLabel(whiteRook);
			else if (piece instanceof Knight)
				return new JLabel(whiteKnight);
			else if (piece instanceof Bishop)
				return new JLabel(whiteBishop);
			else if (piece instanceof Queen)
				return new JLabel(whiteQueen);
			else if (piece instanceof King)
				return new JLabel(whiteKing);
		}
		return new JLabel();
	}
	
	private boolean isPromoteablePawn(Square square)
	{
		return (square.getPiece() instanceof Pawn && ((Pawn) square.getPiece()).isPromoted());
	}
	
	private void promotePawn(Square square)
	{
		Pawn pawn = (Pawn) square.getPiece();
		
		String selection = null;
		if (pawn.isWhite())
			selection = (String) JOptionPane.showInputDialog(
					this, 
					"Select a piece to promoto to:",
					"Piece Chooser",
					JOptionPane.PLAIN_MESSAGE,
					whitePawn,
					PROMOTION_TYPES,
					PROMOTION_TYPES[0]);
		else if (pawn.isBlack())
			selection = (String) JOptionPane.showInputDialog(
					this, 
					"Select a piece to promoto to:",
					"Piece Chooser",
					JOptionPane.PLAIN_MESSAGE,
					blackPawn,
					PROMOTION_TYPES,
					PROMOTION_TYPES[0]);
		
		if (selection != null && selection.length() > 0)
			controller.handleMessage(Controller.MESSAGE_PAWN_PROMOTION, selection);
	}
	
	private boolean isKingInCheck(Square square)
	{
		return (square.isOccupied()
				&& square.getPiece() instanceof King
				&& ((King) square.getPiece()).isInCheck());
	}
	
	/*
	 * Handles mouse actions
	 */
	private class MouseListener extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent event)
		{
			BoardCell cell = (BoardCell) event.getSource();
			Square square = board.getSquare(cell.getRow(), cell.getCol());
			
			controller.handleMessage(Controller.MESSAGE_SQUARE_CLICKED, square);
		}
	}
}
