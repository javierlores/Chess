package models;

import java.io.Serializable;
import java.util.List;

/*
 * An abstract class that represents a game piece.
 */
public abstract class Piece implements Serializable
{
	protected PieceColor color;
	protected Board board;
	protected Square square;
	
	public Piece(PieceColor color, Square square, Board board) {
		this.color = color;
		this.board = board;
		this.square = square;
	}
	
	public abstract List<Square> getValidMoves();

	public boolean isWhite() { return (color == PieceColor.WHITE) ? true : false; }
	
	public boolean isBlack() { return (color == PieceColor.BLACK) ? true : false; }
	
	public PieceColor getColor() { return color; }
	
	public Square getSquare() { return square; }
	
	public void setSquare(Square square) { this.square = square; }
}
