package controllers;

import java.util.List;

import views.View;
import models.Board;
import models.Game;
import models.King;
import models.Pawn;
import models.Piece;
import models.PieceColor;
import models.PieceType;
import models.Rook;
import models.Square;
import models.SquareState;

/*
 * The controller class that handles updating the model. 
 */
public class Controller
{
	public static final int MESSAGE_SQUARE_CLICKED = 1;
	public static final int MESSAGE_PAWN_PROMOTION = 2;
	public static final int MESSAGE_NEW_GAME = 3;
	public static final int MESSAGE_LOAD_GAME = 4;
	
	private Game game;
	private Board board;
	private Square currentSquare;

	public Controller(Game game)
	{
		this.game = game;
		this.board = game.getBoard();
	}
	
	public Board getBoard() { return board; }
	
	public Game getGame() { return game; }
	
	/*
	 * Receives event messages.
	 */
	public void handleMessage(int message, Object data)
	{
		switch (message) 
		{
		case MESSAGE_SQUARE_CLICKED:
			handleSquareClicked(data);
			break;
		case MESSAGE_PAWN_PROMOTION:
			handlePawnPromotion(data);
			break;
		case MESSAGE_NEW_GAME:
			beginGame();
			break;
		case MESSAGE_LOAD_GAME:
			loadGame(data);
			break;
		}
	}

	/*
	 * Sets up a new game.
	 */
	public void beginGame()
	{
		game.beginGame();
		currentSquare = null;
	}
	
	/*
	 * Loads a game.
	 */
	public void loadGame(Object data)
	{
		if (data == null || !(data instanceof Game)) return;
		Game loadGame = (Game) data;
		game.consume(loadGame);
		game.notifyObservers();
	}
	
	/*
	 *  Handles the users board cell clicks
	 */
	private void handleSquareClicked(Object data)
	{
		Square squareClicked = (data instanceof Square) ? (Square) data : null;
		if (squareClicked == null) return;
		
		if (isPieceMoveAttempt(squareClicked))
		{
			if (errorOnMove(squareClicked))
				game.notifyObservers();
			else
				movePiece(squareClicked);
		}
		else if (isPieceSelectionAttempt(squareClicked))
		{
			if (errorOnPieceSelection(squareClicked))
				game.notifyObservers();
			else 
				selectPiece(squareClicked);
		}
	}
	
	/*
	 * Handles the promotion of a pawn
	 */
	private void handlePawnPromotion(Object data) 
	{
		String promotion = null;
		if (data instanceof String) promotion = (String) data;
		
		// Remove pawn
		board.remove(currentSquare.getPiece());
		
		// Add new piece
		switch(promotion) 
		{
			case "Rook":
				board.add(PieceType.ROOK, game.getTurn(), currentSquare.getRow(), currentSquare.getCol());
				((Rook) currentSquare.getPiece()).setMoved();
				currentSquare = null;
				handleKingsInCheck();
				break;
			case "Knight":
				board.add(PieceType.KNIGHT, game.getTurn(), currentSquare.getRow(), currentSquare.getCol());
				currentSquare = null;
				handleKingsInCheck();
				break;
			case "Bishop":
				board.add(PieceType.BISHOP, game.getTurn(), currentSquare.getRow(), currentSquare.getCol());
				currentSquare = null;
				handleKingsInCheck();
				break;
			case "Queen":
				board.add(PieceType.QUEEN, game.getTurn(), currentSquare.getRow(), currentSquare.getCol());
				currentSquare = null;
				handleKingsInCheck();
				break;
		}
	}
	
	/*
	 * Checks if the user has selected a valid move
	 */
	private boolean isPieceMoveAttempt(Square squareClicked)
	{	
		return (squareClicked.isHighlighted() || squareClicked.isCastleable());
	}
	
	/*
	 * Checks for an error on a move selection
	 */
	private boolean errorOnMove(Square squareClicked)
	{
		// Check if the king is currently in check and remember temporarily
		boolean isKingPreviouslyInCheck = false;
		if (isKingInCheck(game.getTurn()))
			isKingPreviouslyInCheck = true;
		
		// Get piece in square to be moved temporarily
		Piece piece = null;
		if (squareClicked.isOccupied())
		{
			piece = squareClicked.getPiece();
			piece.setSquare(null);
		}
	
		// Move the piece temporarily
		board.movePiece(currentSquare.getPiece(), squareClicked);
		
		// Ensure move does not result in placing own king in check
		if (isKingInCheck(game.getTurn()))
		{
			// Move the pieces back
			board.movePiece(squareClicked.getPiece(), currentSquare);
			if (piece != null) 
			{
				squareClicked.setPiece(piece);
				piece.setSquare(squareClicked);
			}
			
			if (isKingPreviouslyInCheck)
				game.addModelError("Your king is in check!");
			else
				game.addModelError("That would place your king in check!");
			
			return true;
		}
		
		// Move the pieces back
		board.movePiece(squareClicked.getPiece(), currentSquare);
		if (piece != null)
		{
			squareClicked.setPiece(piece);
			piece.setSquare(squareClicked);
		}
		
		return false;
	}
	
	/*
	 * Moves the currently selected piece to the square passed in.
	 */
	private void movePiece(Square squareClicked)
	{
		board.movePiece(currentSquare.getPiece(), squareClicked);
		
		if (squareClicked.getPiece() instanceof Pawn)((Pawn) squareClicked.getPiece()).setMoved();
		if (squareClicked.getPiece() instanceof Rook) ((Rook) squareClicked.getPiece()).setMoved();
		if (squareClicked.getPiece() instanceof King)((King) squareClicked.getPiece()).setMoved();
		
		// Handle castling
		if (squareClicked.isCastleable())
		{
			// Move the rook
			board.movePiece(board.getSquare(squareClicked.getRow(), 
					squareClicked.getCol() + 1).getPiece(), 
					board.getSquare(squareClicked.getRow(), squareClicked.getCol() - 1));
			
			((Rook) board.getSquare(squareClicked.getRow(), squareClicked.getCol()-1).getPiece()).setMoved();
			
			currentSquare = null;
		}
		
		// Handle pawn promotion
		if (squareClicked.getPiece() instanceof Pawn
				&& !((Pawn) squareClicked.getPiece()).isPromoted())
		{
			Pawn pawn = (Pawn) squareClicked.getPiece();
			
			if (pawn.isWhite() && squareClicked.getRow() == 0)
			{
				pawn.promote();
				currentSquare = squareClicked;
			}
			else if (pawn.isBlack() && squareClicked.getRow() == 7)
			{
				pawn.promote();
				currentSquare = squareClicked;
			}
		}
		else
			currentSquare = null;

		// Handle checks
		King enemyKing = board.getEnemyKing(game.getTurn());		
		if (isKingInCheck(enemyKing.getColor()))
		{
				enemyKing.setInCheck(true);
				
				// Check for a check mate
				if (isKingInCheckMate(enemyKing))
				{
					game.setWon();
					return;
				}
		}
		else
			enemyKing.setInCheck(false);
		
		// Clear own king check
		board.getKing(game.getTurn()).setInCheck(false);
		
		board.clearBoardStates();
		board.notifyObservers();
		game.nextTurn();
	}
	
	/*
	 * Checks if an attempt was made to select a piece
	 */
	private boolean isPieceSelectionAttempt(Square squareClicked)
	{
		return (squareClicked.isOccupied());
	}
	
	/*
	 * Checks for an error on a piece selection
	 */
	private boolean errorOnPieceSelection(Square squareClicked)
	{
		if (squareClicked.getPiece().getColor() != game.getTurn())
		{
			game.addModelError("It's " + ((String) game.getTurn().toString()).toLowerCase() + "'s turn!");
			return true;
		}
		return false;
	}
	
	/*
	 * Selects the piece passed in.
	 */
	private void selectPiece(Square squareClicked)
	{
		board.clearBoardStates();
		
		// Handle deselection of current piece
		if (currentSquare == squareClicked)
			currentSquare = null;
		else
		{		
			// Set selected square
			currentSquare = squareClicked;
			currentSquare.setState(SquareState.SELECTED);
			
			// Highlight valid moves
			List<Square> validMoves = currentSquare.getPiece().getValidMoves();
			for (Square square : validMoves)
				square.setState(SquareState.HIGHLIGHTED);
			
			// Check for a castling move
			if (currentSquare.getPiece() instanceof King)
			{
				Square castleableSquare = ((King) currentSquare.getPiece()).getCastelable();
				if (castleableSquare != null) castleableSquare.setState(SquareState.CASTLEABLE);
			}
		}

		// Update view
		board.notifyObservers();
	}
	
	/*
	 * Handles king checks and checkmates
	 */
	private void handleKingsInCheck()
	{
		King enemyKing = board.getEnemyKing(game.getTurn());		
		if (isKingInCheck(enemyKing.getColor()))
		{
				enemyKing.setInCheck(true);
				
				if (isKingInCheckMate(enemyKing))
				{
					game.setWon();
					return;
				}
		}
		else
			enemyKing.setInCheck(false);
		
		// Clear own king check
		board.getKing(game.getTurn()).setInCheck(false);
	}
	
	/*
	 * Checks if the king of the color passed in is in check
	 */
	private boolean isKingInCheck(PieceColor color)
	{
		List<Piece> pieceSet = board.getEnemyPieceSet(color);
		for (Piece piece : pieceSet)
		{
			List<Square> validMoves = piece.getValidMoves();
			for (Square square : validMoves)
			{
				if (square.isOccupied() && square.getPiece() instanceof King)
					return true;
			}
		}
		return false;
	}
	
	/*
	 * Checks if the king passed in is in check mate
	 */
	private boolean isKingInCheckMate(King king)
	{
		List<Square> validMoves = king.getValidMoves();
		Square currentKingSquare = king.getSquare();

		for(Square square : validMoves)
		{
			// Get piece square to be moved temporarily
			Piece piece = null;
			if (square.isOccupied())
			{
				piece = square.getPiece();
				piece.setSquare(null);
			}
			board.movePiece(king, square);
			
			if (!isKingInCheck(king.getColor()))
			{
				// Move the king back
				board.movePiece(king, currentKingSquare);
				if (piece != null) 
				{
					square.setPiece(piece);
					piece.setSquare(square);
				}
				return false;
			}
			
			// Move the king back
			board.movePiece(king, currentKingSquare);
			if (piece != null) 
			{
				square.setPiece(piece);
				piece.setSquare(square);
			}
		}
		return true; 
	}
	
	public static void main(String[] args)
	{
		Game model = new Game();
		Controller controller = new Controller(model);
		View view = new View(model, controller);
		
		controller.beginGame();
	}
}
