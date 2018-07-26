package game.exception;

import game.character.IBattlable;
import game.character.moves.Move;

/** Thrown when a move is used when it has no uses left */
public class MoveOutOfUsesException extends Throwable {
	private Move move;
	private String thrower;

	/**
	 * Constructs a new throwable with {@code null} as its detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 *
	 * <p>The {@link #fillInStackTrace()} method is called to initialize
	 * the stack trace data in the newly created throwable.
	 */
	public MoveOutOfUsesException() {
		super();
	}

	public MoveOutOfUsesException(Move move, IBattlable battlable) {
		this.move = move;
		this.thrower = battlable.toString();
	}

	/**
	 * Returns the detail message string of this throwable.
	 *
	 * @return the detail message string of this {@code Throwable} instance
	 * (which may be {@code null}).
	 */
	@Override
	public String getMessage() {
		return this.thrower+" tried to attack with "+this.move.getName()+", but it is out of uses!";
	}
}
