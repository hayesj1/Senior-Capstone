package game.character;

/**
 * Interface for capturable Actors
 */
public interface ICapturable extends IBattlable {

	/**
	 * Clones this instance into a <code>PlayableActor</code> instance
	 * @return A PlayableActor cloned from this instance
	 */
	PlayableActor convertToPlayable();

	/**
	 * Attempt to capture this <code>IBattlable</code>. If successful, <code>capturer</code> claims this <code>IBattlable</code>
	 * @param capturer the <code>IBattlable</code> attempting to capture this <code>IBattlable</code>
	 * @return true on success, false if failure or this <code>IBattlable</code> is not capturable
	 * @see #isCapturable()
	 */
	default boolean capture(IBattlable capturer) {
		if (!isCapturable()) { return false; }
		else if(capturer instanceof PlayerActor) {
			PlayerActor player = (PlayerActor) capturer;
			int randNum = IBattlable.rand.nextInt(100) + 1;
			int catchChance = ( 1 - ( HP() / getStats().maxHP() ) ) * 100;
			if (randNum <= catchChance) {
				this.justCaptured();
				player.addToTeam(this);
				return true;
			} else {
				return false;
			}
		} else {
			return false; // capturer has no team
		}
	}

	/**
	 * @return True if this character is capturable, false otherwise
	 */
	boolean isCapturable();

	/**
	 * @return True if this IBattlable was captured in the current battle; false otherwise
	 */
	boolean wasJustCaptured();

	/** Flags this <code>IBattlable</code> as captured in the current battle */
	void justCaptured();

	/** Flags this <code>IBattlable</code> as captured in a previous battle */
	void clearJustCaptured();
}
