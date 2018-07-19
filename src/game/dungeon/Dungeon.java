package game.dungeon;

import game.SuperDungeoneer;

import java.util.LinkedList;
import java.util.Random;

/**
 * Generate's a random maze using Prim's algorithm; Yields a different outcome every time
 *
 * @author Josh Beaulieu
 */
public class Dungeon {
	private static final char PASSAGE_CHAR = Tile.PASSAGE_CHAR;
	private static final char WALL_CHAR    = Tile.WALL_CHAR;
	private static final char BLOCKED_CHAR = Tile.BLOCKED_CHAR;

	/** Coordinates are stored  in (x,y) or (column, row) format */
	private final Tile map[][];
	private final int width;
	private final int height;
	private final int maxPassageLength;
	private final int minPassageLength;

	public Dungeon(final int width, final int height ){
		this.width = width;
		this.height = height;
		this.maxPassageLength = (width / 4);
		this.minPassageLength = 3;
		this.map = new Tile[width][height];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				this.map[i][j] = new Tile(i, j);
			}
		}

	}

	public void generateDungeon() {
		generateLayout();
		generateMonsters( (int) Math.ceil(Math.pow(width * height, 0.5f)) );
	}
	private void generateLayout() {
		final LinkedList<int[]> frontiers = new LinkedList<>();
		final Random random = new Random();

		int x = random.nextInt(width);
		int y = random.nextInt(height);
		frontiers.add(new int[] { x,y, x,y });

		while (!frontiers.isEmpty()) {
			final int[] f = frontiers.remove(random.nextInt(frontiers.size()));
			x = f[2];
			y = f[3];
			if(map[x][y].isWall()) {
				map[x][y].setStateAndOccupant(Tile.PASSAGE, null);
				map[ f[0] ][ f[1] ].setStateAndOccupant(Tile.PASSAGE, null);
				if (x >= 2 && map[x-2][y].isWall())
					frontiers.add(new int[] { x-1,y, x-2,y });
				if (y >= 2 && map[x][y-2].isWall())
					frontiers.add(new int[] { x,y-1, x,y-2 });
				if (x < width-2 && map[x+2][y].isWall())
					frontiers.add(new int[] { x+1,y, x+2,y });
				if (y < height-2 && map[x][y+2].isWall())
					frontiers.add(new int[] { x,y+1, x,y+2 });
			} else if (( x >= 4 &&  x < width-4 ) && ( y >= 4 &&  y < height-4 )) {
				boolean vertical   = isVerticalPassage(x, y);
				boolean horizontal = isHorizontalPassage(x, y);
				boolean isPassage  = vertical || horizontal;

				if (isPassage) {
					int len = passageLength(x, y, vertical);
					if (len < maxPassageLength && len >= minPassageLength) {
						boolean inc;
						if (vertical) {
							inc = y < height / 2;
						} else {
							inc = x < width / 2;
						}

						int offset1 = 1, offset2 = 2;
						if (!inc) {
							offset1 = -offset1;
							offset2 = -offset2;
						}
						if (vertical) {
							map[x][y+offset1].setStateAndOccupant(Tile.PASSAGE, null);
							map[x][y+offset2].setStateAndOccupant(Tile.PASSAGE, null);
						} else {
							map[x+offset1][y].setStateAndOccupant(Tile.PASSAGE, null);
							map[x+offset2][y].setStateAndOccupant(Tile.PASSAGE, null);
						}
					}
				}

			}
		}
	}
	private void generateMonsters(int n) {
		final Random random = new Random();
		while (n > 0) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			System.out.println(n);
			if (map[x][y].isPassage()) {
				map[x][y].setStateAndOccupant(Tile.MONSTER, SuperDungeoneer.getInstance().getRandMonster(random));
				n--;
				System.out.println("Generated Monster");
			}
		}

		//TODO: Boss Gen


	}

	private boolean isVerticalPassage(int x, int y) {
		boolean res = map[x][y-1].isPassage() || map[x][y+1].isPassage();
		return res && isPassage(x, y, true);//( isPassage(x-1, y, true) || isPassage(x+1, y, true) );
	}

	private boolean isHorizontalPassage(int x, int y) {
		boolean res = map[x-1][y].isPassage() || map[x+1][y].isPassage();
		return res && isPassage(x, y, false);
	}

	private boolean isPassage(int x, int y, boolean vertical) {
		if (map[x][y].isWall()) { return false; }

		if (vertical) {
			return isPassageHelper(x, y-1, true, false, maxPassageLength-1) || isPassageHelper(x, y+1, true, true, maxPassageLength-1);
		} else {
			return isPassageHelper(x-1, y, false, false, maxPassageLength-1) || isPassageHelper(x+1, y, false, true, maxPassageLength-1);
		}
	}
	private boolean isPassageHelper(int x, int y, boolean vertical, boolean increment, int lengthRemaining) {
		if (lengthRemaining == 0) { return true; }
		else {
			if (( x < 2 || x >= width-2 ) || ( y < 2 || y >= height-2 ) || map[x][y].isWall()) {
				return maxPassageLength - lengthRemaining >= minPassageLength;
			} else {
				int nextX = x + ( vertical ? 0 : ( increment ? 1 : -1 ) );
				int nextY = y + ( vertical ? ( increment ? 1 : -1 ) : 0 );
				return isPassageHelper(nextX, nextY, vertical, increment, lengthRemaining - 1);
			}
		}
	}

	private int passageLength(int x, int y, boolean vertical) {
		if (map[x][y].isWall()) { return 0; }

		int nextX1 = x + (vertical ? 0 : -1), nextX2 = x + (vertical ? 0 : 1);
		int nextY1 = y + (vertical ? -1 : 0), nextY2 = y + (vertical ? 1 : 0);
		return passageLengthHelper(nextX1, nextY1, vertical, false, 1) + passageLengthHelper(nextX2, nextY2, vertical, true, 1);
	}
	private int passageLengthHelper(int x, int y, boolean vertical, boolean increment, int length) {
		if (( x < 2 || x >= width - 2 ) || ( y < 2 || y >= height - 2 ) || map[x][y].isWall()) { return length; }

		int nextX = x + ( vertical ? 0 : ( increment ? 1 : -1 ) );
		int nextY = y + ( vertical ? ( increment ? 1 : -1 ) : 0 );
		return passageLengthHelper(nextX, nextY, vertical, increment, length + 1);
	}

	@Override
	public String toString(){
		final StringBuilder b = new StringBuilder();
		for (int x = 0; x < width + 2; x++)
			b.append(WALL_CHAR);
		b.append('\n');
		for (int y = 0; y < height; y++ ){
			b.append(WALL_CHAR);
			for (int x = 0; x < width; x++)
				b.append( !map[x][y].isBlocked() ? PASSAGE_CHAR : ( map[x][y].isWall() ? WALL_CHAR : BLOCKED_CHAR ) );
			b.append(WALL_CHAR);
			b.append('\n');
		}
		for (int x = 0; x < width + 2; x++)
			b.append(WALL_CHAR);
		b.append('\n');

		return b.toString();
	}
}