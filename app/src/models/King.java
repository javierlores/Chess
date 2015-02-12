package models;

import java.util.ArrayList;
import java.util.List;

/*
 * Represents the king piece and contains the logic for the king pieces move.
 */
public class King extends Piece
{
	private boolean moved = false;
	private boolean inCheck = false;

	public King(PieceColor color, Square square, Board board){
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
			if (Board.isValidRow(row += i))
			{
				if (!board.getSquare(row, col).isOccupied())
					validMoves.add(board.getSquare(row, col));
				else
				{
					if (board.getSquare(row, col).getPiece().getColor() != color)
						validMoves.add(board.getSquare(row, col));
				}
			}
			row = square.getRow();
		}
		
		// Check left and right on the board
		for(int i = -1; i < 2; i += 2)
		{
			if (Board.isValidCol(col += i))
			{
				if (!board.getSquare(row, col).isOccupied())
					validMoves.add(board.getSquare(row, col));
				else
				{
					if (board.getSquare(row, col).getPiece().getColor() != color)
						validMoves.add(board.getSquare(row, col));
				}
			}
			col = square.getCol();
		}
		
		// Check to the left top and right bottom diagonals on the board
		for(int i = -1; i < 2; i += 2)
		{
			if (Board.isValidRow(row += (1 * i))
					&& Board.isValidCol(col += (-1 * i)))
			{
				if (!board.getSquare(row, col).isOccupied())
					validMoves.add(board.getSquare(row, col));
				else
				{
					if (board.getSquare(row, col).getPiece().getColor() != color)
						validMoves.add(board.getSquare(row, col));
				}
			}
			row = square.getRow();
			col = square.getCol();
		}
		
		// Check the right top and left bottom diagonals on the board
		for(int i = -1; i < 2; i += 2)
		{
			if (Board.isValidRow(row += (-1 * i))
					&& Board.isValidCol(col += (-1 * i)))
			{
				if (!board.getSquare(row, col).isOccupied())
					validMoves.add(board.getSquare(row, col));
				else
				{
					if (board.getSquare(row, col).getPiece().getColor() != color)
						validMoves.add(board.getSquare(row, col));
				}
			}
			row = square.getRow();
			col = square.getCol();
		}
		
		return validMoves;
	}
	
	/*
	 * If the king is in a position to castle, returns the square the king will be moved to.
	 * Otherwise returns null.
	 */
	public Square getCastelable()
	{
		// Check for castling
		if (isBlack() && !moved && !inCheck)
		{
			if (!board.getSquare(0, 5).isOccupied()
					&& !board.getSquare(0, 6).isOccupied()
					&& board.getSquare(0, 7).isOccupied()
					&& board.getSquare(0, 7).getPiece() instanceof Rook
					&& !((Rook) board.getSquare(0, 7).getPiece()).hasMoved())
				return board.getSquare(0, 6);
		} 
		else if (isWhite() && !moved && !inCheck)
		{
			if (!board.getSquare(7, 5).isOccupied()
					&& !board.getSquare(7, 6).isOccupied()
					&& board.getSquare(7, 7).isOccupied()
					&& board.getSquare(7, 7).getPiece() instanceof Rook
					&& !((Rook) board.getSquare(7, 7).getPiece()).hasMoved())
				return board.getSquare(7, 6);
		}
		return null;
	}
	
	public boolean isInCheck() { return inCheck; }
	
	public void setInCheck(boolean inCheck) { this.inCheck = inCheck; }

	public void setMoved() { this.moved = true; }
}
