package models;

import java.util.Observable;
import java.util.Observer;

/*
 * The main model class the represents a game of chess.
 */
public class Game extends Model implements Observer
{
	private Board board = new Board();
	private GameStatus gameStatus;
	private PieceColor turn;
	
	public Game()
	{
		beginGame();
		board.addObserver(this);
	}

	/*
	 * Initiates a new game.
	 */
	public void beginGame()
	{
		board.setupBoard();
		turn = PieceColor.WHITE;
		gameStatus = GameStatus.ONGOING;
		setChanged();
		notifyObservers();
	}
	
	public Board getBoard() { return board; }
	
	public PieceColor getTurn() { return turn; }
	
	public GameStatus getGameStatus() { return gameStatus; }
	
	/*
	 * Sets the game to the next players turn.
	 */
	public void nextTurn()
	{
		turn = (turn == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
		setChanged();
		notifyObservers();
	}
	
	/*
	 * Sets the game as finished and won.
	 */
	public void setWon()
	{
		board.clearBoardStates();
		gameStatus = GameStatus.WON;
		makeModelInvalid();
		setChanged();
		notifyObservers();
	}

	@Override
	public void update(Observable o, Object arg)
	{
		setChanged();
		notifyObservers();
	}
	
	@Override
	public void addModelError(String errorMessage)
	{
		super.addModelError(errorMessage);
		setChanged();
	}
	
	/*
	 * Copies the game passed in and sets as the current game.
	 */
	public void consume(Game game)
	{
		this.board.consume(game.getBoard());
		this.gameStatus = game.getGameStatus();
		this.turn = game.getTurn();
		setChanged();
	}
}
