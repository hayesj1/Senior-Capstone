package game.character;

import game.character.moves.Move;

public class PlayerActor extends PlayableActor {
	private static final int MAX_PARTY_SIZE = 6;

	private PlayableActor[] party = new PlayableActor[MAX_PARTY_SIZE];
	private int nextFreeSlot = 0;

	public PlayerActor(Species species, String name, Move[] learnedMoves, Stats stats) { this(species, name, learnedMoves, stats, 1); }
	public PlayerActor(Species species, String name, Move[] learnedMoves, Stats stats, int level) { this(species, name, learnedMoves, stats, level, null); }
	public PlayerActor(Species species, String name, Move[] learnedMoves, Stats stats, int level, PlayableActor[] party) {
		super(species, name, stats, level, learnedMoves);

		if (party != null) {
			for (int i = 0, j = 0; i < party.length && nextFreeSlot < MAX_PARTY_SIZE; i++) {
				if (party[i] != null) {
					this.party[nextFreeSlot] = party[i];
					nextFreeSlot++;
				}
			}
		}
	}

	/**
	 * Swaps the party member in <code>slot</code> with <code>pendingPartyMember</code>
	 * @param slot the slot in question; valid range is 1 to {@link #MAX_PARTY_SIZE}
	 * @param pendingPartyMember The just captured Actor
	 * @return the party member that was replaced by <code>pendingPartyMember</code>
	 */
	public PlayableActor swapPartyMember(int slot, PlayableActor pendingPartyMember) {
		PlayableActor removed = this.party[slot];
		this.party[slot] = pendingPartyMember;
		return removed;
	}

	public PlayableActor getNextAvailablePartyMember(PlayableActor old) {
		int partySlot = 1;
		while (party[partySlot-1] == old || party[partySlot-1].isIncapacitated()) {
			if (partySlot >= MAX_PARTY_SIZE) {
				return null;
			}

			partySlot++;
		}

		return party[partySlot-1];
	}

	/**
	 * Adds <code>captured</code> to this <code>IBattlable</code>'s team(if one exists)
	 * @param captured the newly captured <code>IBattlable</code>
	 */
	public void addToTeam(ICapturable captured) {
		if (nextFreeSlot >= MAX_PARTY_SIZE) {
			//TODO: Ask user for swapee
			//Capstone.setPendingPartyMember()
		} else {
			party[nextFreeSlot++] = captured.convertToPlayable();
		}

		captured.clearJustCaptured();
	}

	/**
	 * Adds <code>captured</code> to this <code>IBattlable</code>'s team(if one exists)
	 * @param pendingPartyMember the new party member
	 */
	public void addToTeam(PlayableActor pendingPartyMember) {
		if (nextFreeSlot >= MAX_PARTY_SIZE) {
			//TODO: Ask user for swapee
			//Capstone.setPendingPartyMember()
		} else {
			party[nextFreeSlot++] = pendingPartyMember;
		}
	}
}
