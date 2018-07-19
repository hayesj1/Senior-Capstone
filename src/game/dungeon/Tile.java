package game.dungeon;

/**
 *
 * @author Jacob Hayes
 */
public class Tile {
	static final char PASSAGE_CHAR = '\u25A2';
	static final char WALL_CHAR    = '\u25A6';
	static final char BLOCKED_CHAR = '\u25A3';
	static final int WALL      = 0;
	static final int PASSAGE   = 1;
	static final int MONSTER   = 2;
	static final int BOSS      = 4;

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
	public boolean isBlocked() { return state == WALL || (( state == MONSTER || state == BOSS ) && occupant != null); }

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
