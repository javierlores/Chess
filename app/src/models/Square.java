package models;

import java.io.Serializable;
import java.util.Observable;

/*
 * Represents a single square on the game board.
 */
public class Square extends Observable implements Serializable 
{
	private int row;
	private int col;
	private SquareState state;
	private Piece piece;
	
	public Square(int row, int col)
	{
		this.row = (Board.isValidRow(row)) ? row : 0;
		this.col= (Board.isValidCol(col)) ? col : 0;
		state = SquareState.NEUTRAL;
		piece = null;
	}
	
	public Square(int row, int col, Piece piece)
	{
		this(row, col);
		this.piece = piece;
	}
	
	public SquareState getState() { return state; }
	
	public Piece getPiece() { return piece; }
	
	public int getRow() { return row; }
	
	public int getCol() { return col; }

	public boolean isHighlighted() { return (state == SquareState.HIGHLIGHTED); }
	
	public boolean isSelected() { return (state == SquareState.SELECTED); }
	
	public boolean isOccupied() { return (piece != null); }
	
	public boolean isCastleable() { return (state == SquareState.CASTLEABLE); }
	
	public void setState(SquareState state)
	{
		this.state = state;
		setChanged();
		notifyObservers();
	}
	
	public void setPiece(Piece piece)
	{
		this.piece = piece;
		setChanged();
		notifyObservers();
	}
}