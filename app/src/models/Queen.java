package models;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents the queen piece and contains the logic for the queen pieces move.
 */
public class Queen extends Piece
{

	public Queen(PieceColor color, Square square, Board board)
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
		
		// Check to the left top and right bottom diagonals on the board
		for(int i = -1; i < 2; i += 2)
		{
			while (Board.isValidRow(row += (1 * i))
					&& Board.isValidCol(col += (-1 * i)))
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
			col = square.getCol();
		}
		
		// Check the right top and left bottom diagonals on the board
		for(int i = -1; i < 2; i += 2)
		{
			while (Board.isValidRow(row += (-1 * i))
					&& Board.isValidCol(col += (-1 * i)))
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
			col = square.getCol();
		}
		
		return validMoves;
	}
}
