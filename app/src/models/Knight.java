package models;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents the knight piece and contains the logic for the knight pieces move.
 */
public class Knight extends Piece
{

	public Knight(PieceColor color, Square square, Board board)
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
		
		// Check downward moves
		if (Board.isValidRow(row += 2))
		{
			for(int i = -1; i < 2; i += 2)
			{
				if (Board.isValidCol(col += i))
				{
					if ((board.getSquare(row, col).isOccupied()
							&& board.getSquare(row, col).getPiece().getColor() != color)
							|| !board.getSquare(row, col).isOccupied())
						validMoves.add(board.getSquare(row, col));
				}
				col = square.getCol();
			}
		}
		row = square.getRow();
		
		// Check upward moves
		if (Board.isValidRow(row -= 2))
		{
			for(int i = -1; i < 2; i += 2)
			{
				if (Board.isValidCol(col += i))
				{
					if ((board.getSquare(row, col).isOccupied()
							&& board.getSquare(row, col).getPiece().getColor() != color)
							|| !board.getSquare(row, col).isOccupied())
						validMoves.add(board.getSquare(row, col));
				}
				col = square.getCol();
			}
		}
		row = square.getRow();
		
		// Check right moves
		if (Board.isValidCol(col += 2))
		{
			for(int i = -1; i < 2; i += 2)
			{
				if (Board.isValidRow(row -= i))
				{
					if ((board.getSquare(row, col).isOccupied()
							&& board.getSquare(row, col).getPiece().getColor() != color)
							|| !board.getSquare(row, col).isOccupied())
						validMoves.add(board.getSquare(row, col));
				}
				row = square.getRow();
			}
		}		
		col = square.getCol();
		
		// Check left moves
		if (Board.isValidCol(col -= 2))
		{
			for(int i = -1; i < 2; i += 2)
			{
				if (Board.isValidRow(row += i))
				{
					if ((board.getSquare(row, col).isOccupied()
							&& board.getSquare(row, col).getPiece().getColor() != color)
							|| !board.getSquare(row, col).isOccupied())
						validMoves.add(board.getSquare(row, col));
				}
				row = square.getRow();
			}
		}
		col = square.getCol();
		
		return validMoves;
	}
}
