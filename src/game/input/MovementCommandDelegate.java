package game.input;

import game.SuperDungeoneer;
import game.character.PlayerActor;
import game.dungeon.Dungeon;
import game.dungeon.Tile;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.command.Command;

/**
 * Input delegate to handle movement and dungeon interaction commands
 */
public class MovementCommandDelegate implements ICommandDelegate {
	private Dungeon map;
	private boolean initialized;

	public MovementCommandDelegate() {
		this.map = null;
		this.initialized = false;
	}

	/**
	 * Initialize state. After the call returns, input will be processable if:
	 * <code>battle.needsUserAction</code> returns <code>true</code>
	 * and
	 * <code>battle.getActiveActor</code> does not return <code>null</code>
	 */
	public void init(Dungeon map) {
		this.map = map;
		this.initialized = true;
	}

	/**
	 * Execute the action
	 * @param command The command that was triggered
	 * @param container The GUIContext
	 */
	@Override
	public void action(Command command, GameContainer container) {
		if (!initialized || SuperDungeoneer.getInstance().getPlayer() == null || !map.isGenerated()) { return; }

		int x = 0, y = 0;
		if (command == DemoInputHandler.forward) { x = 1; }
		else if (command == DemoInputHandler.backward) { x = -1; }
		else if (command == DemoInputHandler.up) { y = -1; }
		else if (command == DemoInputHandler.down) { y = 1; }
		else if (command == DemoInputHandler.interact) { SuperDungeoneer.getInstance().interacted(); }
		else if (command == DemoInputHandler.runAway) { SuperDungeoneer.getInstance().runAway(); }

		if ((x == 0 && y == 0) || SuperDungeoneer.getInstance().isBattling()) { return; }

		int[] pp = SuperDungeoneer.getInstance().getPlayerPos();
		int xP = pp[0], yP = pp[1];
		try {
			Tile newTile  = map.getTileAt(xP+x, yP+y);
			if (newTile.isPassage() || (!newTile.isWall() && newTile.getOccupant() == null)) {
				PlayerActor player = (PlayerActor) map.getTileAt(xP, yP).setStateAndOccupant(Tile.PASSAGE, null);
				newTile.setStateAndOccupant(Tile.ACTOR, (player == null ? SuperDungeoneer.getInstance().getPlayer() : player));
				SuperDungeoneer.getInstance().movePlayer(x, y);
			}
		} catch (IllegalArgumentException iae) {
			System.err.println("Out of bounds movement attempted!"); return; }
	}

}

