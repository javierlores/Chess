package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/* 
 *
 * Author: Javier
 *
 */
public class Board extends Observable implements Observer, Serializable
{
	public final static int ROW = 8;
	public final static int COL = 8;

	private Square[][] board = new Square[ROW][COL];
	private List<Piece> blackPieceSet = new ArrayList<Piece>();
	private List<Piece> whitePieceSet = new ArrayList<Piece>();
	
	private King whiteKing;
	private King blackKing;
	
	/*
	 * Sets up the board
	 */
	public void setupBoard()
	{
		for (int row = 0; row < ROW; row++)
		{
			for (int col = 0; col < COL; col++)
			{
				board[row][col] = new Square(row, col);
				board[row][col].addObserver(this);
			}
		}
		
		// Ensure piece sets are empty
		blackPieceSet.clear();
		whitePieceSet.clear();
		
		// Setup black back row
		add(PieceType.ROOK, PieceColor.BLACK, 0, 0);
		add(PieceType.KNIGHT, PieceColor.BLACK, 0, 1);
		add(PieceType.BISHOP, PieceColor.BLACK, 0, 2);
		add(PieceType.QUEEN, PieceColor.BLACK, 0, 3);
		add(PieceType.KING, PieceColor.BLACK, 0, 4);
		add(PieceType.BISHOP, PieceColor.BLACK, 0, 5);
		add(PieceType.KNIGHT, PieceColor.BLACK, 0, 6);
		add(PieceType.ROOK, PieceColor.BLACK, 0, 7);
		
		// Set up black pawns
		for (int col = 0; col < COL; col++) 
			add(PieceType.PAWN, PieceColor.BLACK, 1, col);
		
		// Set up white pawns
		for (int col = 0; col < COL; col++) 
			add(PieceType.PAWN, PieceColor.WHITE, 6, col);
		
		// Set up white back row
		add(PieceType.ROOK, PieceColor.WHITE, 7, 0);
		add(PieceType.KNIGHT, PieceColor.WHITE, 7, 1);
		add(PieceType.BISHOP, PieceColor.WHITE, 7, 2);
		add(PieceType.QUEEN, PieceColor.WHITE, 7, 3);
		add(PieceType.KING, PieceColor.WHITE, 7, 4);
		add(PieceType.BISHOP, PieceColor.WHITE, 7, 5);
		add(PieceType.KNIGHT, PieceColor.WHITE, 7, 6);
		add(PieceType.ROOK, PieceColor.WHITE, 7, 7);
		
		// Get references to white and black king
		blackKing = (King) board[0][4].getPiece();
		whiteKing = (King) board[7][4].getPiece();
		
		setChanged();
	}
	
	public King getEnemyKing(PieceColor color)
	{
		return (color == PieceColor.WHITE) ? blackKing : whiteKing;
	}
	
	public King getKing(PieceColor color)
	{
		return (color == PieceColor.WHITE) ? whiteKing : blackKing;
	}
	
	/*
	 * Gets the set of black pieces
	 */
	public List<Piece> getEnemyPieceSet(PieceColor color) 
	{ 
		return (color == PieceColor.WHITE) ? blackPieceSet : whitePieceSet;
	}
	
	/*
	 * Gets the set of white pieces
	 */
	public List<Piece> getPieceSet(PieceColor color) 
	{ 
		return (color == PieceColor.WHITE) ? whitePieceSet : blackPieceSet;
	}
	
	/*
	 * Gets the square located at the row and column passed in
	 */
	public Square getSquare(int row, int col)
	{
		return (isValidRow(row) && isValidCol(col)) ? board[row][col] : null;
	}
	
	/*
	 * Moves the piece passed in to the square passed in
	 */
	public void movePiece(Piece piece, Square square)
	{
		square.setPiece(piece);
		piece.getSquare().setPiece(null);
		piece.setSquare(square);
		
		setChanged();
	}
	
	public void remove(Piece piece)
	{
		piece.getSquare().setPiece(null);
		piece.setSquare(null);
		
		if (piece.isWhite() && whitePieceSet.contains(piece))
			whitePieceSet.remove(piece);
		else if (piece.isBlack() && blackPieceSet.contains(piece))
			blackPieceSet.remove(piece);
		
		setChanged();
	}
	
	/*
	 * Creates and adds a piece to the board as specified by the type and color passed in
	 * and places it on the square located at the row and column passed in.
	 */
	public void add(PieceType type, PieceColor color, int row, int col)
	{
		Piece piece = PieceFactory.createPiece(type, color, board[row][col], this);
		board[row][col].setPiece(piece);
		
		if (color == PieceColor.BLACK)
			blackPieceSet.add(piece);
		else
			whitePieceSet.add(piece);
		
		setChanged();
	}
	
	/*
	 * Sets all board states to neutral
	 */
	public void clearBoardStates() 
	{
		for (int row = 0; row < ROW; row++) 
			for (int col = 0; col < COL; col++)
				board[row][col].setState(SquareState.NEUTRAL);
		
		setChanged();
	}
	
	/*
	 * Checks if the row is a valid row
	 */
	public static boolean isValidRow(int row) { return (row >= 0 && row < ROW); }
	
	/*
	 * Checks if the column is a valid column
	 */
	public static boolean isValidCol(int col) { return (col >= 0 && col < COL); }

	@Override
	public void update(Observable o, Object arg) 
	{ 
		setChanged();
	}

	public void consume(Board board)
	{
		for (int row = 0; row < ROW; row++)
		{
			for (int col = 0; col < COL; col++)
			{
				this.board[row][col] = board.getSquare(row, col);
				this.board[row][col].addObserver(this);
			}
		}
		
		this.blackPieceSet = board.getPieceSet(PieceColor.BLACK);
		this.whitePieceSet = board.getPieceSet(PieceColor.WHITE);
		this.blackKing = board.getKing(PieceColor.BLACK);
		this.whiteKing = board.getKing(PieceColor.WHITE);
		setChanged();
	}
}
