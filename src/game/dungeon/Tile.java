package game.dungeon;

/**
 *
 * @author Jacob Hayes
 */
public class Tile {
	static final char PASSAGE_CHAR = '\u25A2';
	static final char WALL_CHAR    = '\u25A6';
	static final char BLOCKED_CHAR = '\u25A3';
	public static final int WALL      = 1;
	public static final int PASSAGE   = 2;
	public static final int ACTOR     = 4;
	public static final int BOSS      = 8;

	private int x;
	private int y;
	private int state;
	private Object occupant;

	public Tile(int x, int y) { this(x, y, WALL, null); }
	public Tile(int x, int y, int state, Object occupant) {
		this.x = x;
		this.y = y;
		this.state = state;
		this.occupant = occupant;
	}

	public boolean isWall() { return state == WALL; }
	public boolean isPassage() { return state == PASSAGE; }
	public boolean isBlocked() { return state == WALL || (( state == ACTOR || state == BOSS ) && occupant != null); }

	/**
	 * Returns a string representation of the object. In general, the
	 * {@code toString} method returns a string that
	 * "textually represents" this object. The result should
	 * be a concise but informative representation that is easy for a
	 * person to read.
	 * It is recommended that all subclasses override this method.
	 * <p>
	 * The {@code toString} method for class {@code Object}
	 * returns a string consisting of the name of the class of which the
	 * object is an instance, the at-sign character `{@code @}', and
	 * the unsigned hexadecimal representation of the hash code of the
	 * object. In other words, this method returns a string equal to the
	 * value of:
	 * <blockquote>
	 * <pre>
	 * getClass().getName() + '@' + Integer.toHexString(hashCode())
	 * </pre></blockquote>
	 *
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return "("+this.getX()+", "+this.getY()+"): "+this.getState()+" | "+(this.getOccupant() == null ? "NULL" : this.getOccupant().toString());
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return  y;
	}
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getState() {
		return state;
	}
	public Object getOccupant() {
		return occupant;
	}
	public Object setStateAndOccupant(int state, Object occupant) {
		this.state = state;
		Object ret = this.occupant;
		this.occupant = occupant;

		return ret;
	}

	public Object setOccupant(Object occupant) {
		if (occupant.getClass().equals(this.occupant.getClass())) {
			Object ret = this.occupant;
			this.occupant = occupant;
			return ret;
		} else {
			throw new IllegalArgumentException("New Occupant is not of compatable type! Use setStateAndOccupant() instead!");
		}
	}
}
