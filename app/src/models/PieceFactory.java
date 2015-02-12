package models;

/*
 * A factory class that builds a piece and places it on the board.
 */
public class PieceFactory 
{
	public static Piece createPiece(PieceType type, PieceColor color, Square square, Board board) {
		switch(type) {
		case PAWN:
			return new Pawn(color, square, board);
		case ROOK:
			return new Rook(color, square, board);
		case KNIGHT:
			return new Knight(color, square, board);
		case BISHOP:
			return new Bishop(color, square, board);
		case QUEEN:
			return new Queen(color, square, board);
		case KING:
			return new King(color, square, board);
		default:
			return null;
		}
	}
}
