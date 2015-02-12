package models;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents the rook piece and contains the logic for the rook pieces move.
 */
public class Rook extends Piece
{
	private boolean moved = false;
	
	public Rook(PieceColor color, Square square, Board board)
	{
		super(color, square, board);
	}
	
	@Override
	public List<Square> getValidMoves() {
		List<Square> validMoves = new ArrayList<Square>();

		if (square == null) return validMoves;
		
		int row = square.getRow();
		int col = square.getCol();
		
		// Check up and down the board
		for(int i = -1; i < 2; i += 2)
		{
			while (Board.isValidRow(row += i))
			{
				if (!board.getSquare(row, col).isOccupied())
					validMoves.add(board.getSquare(row, col));
				else
				{
					if (board.getSquare(row, col).getPiece().getColor() != color)
						validMoves.add(board.getSquare(row, col));
					break;
				}
			}
			row = square.getRow();
		}
		
		// Check left and right on the board
		for(int i = -1; i < 2; i += 2)
		{
			while (Board.isValidCol(col += i))
			{
				if (!board.getSquare(row, col).isOccupied())
					validMoves.add(board.getSquare(row, col));
				else
				{
					if (board.getSquare(row, col).getPiece().getColor() != color)
						validMoves.add(board.getSquare(row, col));
					break;
				}
			}
			col = square.getCol();
		}
		
		return validMoves;
	}
	
	public boolean hasMoved() { return moved; }
	
	public void setMoved() { this.moved = true; }
}
