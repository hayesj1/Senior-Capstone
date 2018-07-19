package game.dungeon;

public class DungeonComponent {
	public static void main(String[] args){
		Dungeon dungeon = new Dungeon(40,20);
		dungeon.generateDungeon();
		System.out.println(dungeon);
	}
}
