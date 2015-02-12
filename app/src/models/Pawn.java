package models;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents the pawn piece and contains the logic for the pawn pieces move.
 */
public class Pawn extends Piece
{
	private boolean moved = false;
	private boolean promoted = false;
	
	public Pawn(PieceColor color, Square square, Board board)
	{
		super(color, square, board);
	}

	@Override
	public List<Square> getValidMoves()
	{
		List<Square> validMoves = new ArrayList<Square>();
		
		if (square == null) return validMoves;

		int row = square.getRow();
		int col = square.getCol();
		
		if (isWhite()) {
			row -= 1;

			if (Board.isValidRow(row) 
					&& Board.isValidCol(col) 
					&& !board.getSquare(row, col).isOccupied())
			{
				validMoves.add(board.getSquare(row, col));
				
				// Check if first move then allowed to move two squares
				row -= 1;
				if (Board.isValidRow(row) 
						&& Board.isValidCol(col) 
						&& !moved)
				{
					if (!board.getSquare(row, col).isOccupied())
						validMoves.add(board.getSquare(row, col));
				}

				row += 1;
			} 
		}
		else if (isBlack())
		{
			row += 1;
			
			if (Board.isValidRow(row) 
					&& Board.isValidCol(col) 
					&& !board.getSquare(row, col).isOccupied())
			{
				validMoves.add(board.getSquare(row, col));
				
				// Check if first move then allowed to move two squares
				row += 1;
				if (Board.isValidRow(row) 
						&& Board.isValidCol(col) 
						&& !moved)
				{
					if (!board.getSquare(row, col).isOccupied())
						validMoves.add(board.getSquare(row, col));
				}
				
				row -= 1;
			} 
		}
		
		// Check diagonals for enemy pieces
		if (Board.isValidCol(col -= 1)
				&& Board.isValidRow(row) 
				&& board.getSquare(row, col).isOccupied()
				&& board.getSquare(row, col).getPiece().getColor() != color)
			validMoves.add(board.getSquare(row, col));
		
		if (Board.isValidCol(col += 2)
				&& Board.isValidRow(row) 
				&& board.getSquare(row, col).isOccupied()
				&& board.getSquare(row, col).getPiece().getColor() != color)
			validMoves.add(board.getSquare(row, col));
		
		return validMoves;
	}
	
	public boolean isPromoted() { return promoted; }
	
	public boolean hasMoved() { return moved; }
	
	public void promote() { this.promoted = true; }
	
	public void setMoved() { this.moved = true; }
}
