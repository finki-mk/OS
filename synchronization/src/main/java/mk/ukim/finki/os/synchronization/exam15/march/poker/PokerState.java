package mk.ukim.finki.os.synchronization.exam15.march.poker;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

public class PokerState extends AbstractState {

	private static final int PREVIOUS_ROUND_NOT_FINISHED_POINTS = 7;
	private static final String PREVIOUS_ROUND_NOT_FINISHED = "Prethodnata grupa nema zavrseno so igrata.";
	private static final int NO_6_FINISHED_PLAYERS_POINTS = 7;
	private static final String NO_6_FINISHED_PLAYERS = "Nema 6 igraci koi zavrsile so igranje";

	private static final int PLAYERS_STIL_PLAYING_POINTS = 7;
	private static final String PLAYERS_STIL_PLAYING = "Ima uste igraci koi ne zavrsile so igranje";

	private static final int PLAYING_NOT_PARALLEL_POINTS = 7;
	private static final String PLAYING_NOT_PARALLEL = "ne e paralelno igranjeto";

	private static final int CARDS_NOT_DEALED_POINTS = 7;
	private static final String CARDS_NOT_DEALED = "Ne moze da igra koga kartite ne se podeleni";

	private static final int MAXIMUM_6_CUSTOMERS_POINTS = 7;
	private static final String MAXIMUM_6_CUSTOMERS = "Poveke od 6 igraci probuvaat da sednat istovremeno";

	private static final int NOT_ENOUGH_PLAYERS_POINTS = 7;
	private static final String NOT_ENOUGH_PLAYERS = "nema dovolno igraci za da se sostavi grupa";

	private BoundCounterWithRaceConditionCheck playersAtTable;
	private BoundCounterWithRaceConditionCheck peoplePlaying;
	private BoundCounterWithRaceConditionCheck peopleFinishedPlaying;
	private boolean emptyTable = true;
	private boolean cardsDealed = false;

	public PokerState() {
		playersAtTable = new BoundCounterWithRaceConditionCheck(0, 6,
				MAXIMUM_6_CUSTOMERS_POINTS, MAXIMUM_6_CUSTOMERS, null, 0, null);

		peoplePlaying = new BoundCounterWithRaceConditionCheck(0);
		peopleFinishedPlaying = new BoundCounterWithRaceConditionCheck(0);
	}

	/*
	 * Igracot sednuva na masata
	 */
	public void playerSeat() {
		synchronized (PokerState.class) {
			if (emptyTable) {
				emptyTable = false;
				playersAtTable.assertEquals(0,
						PREVIOUS_ROUND_NOT_FINISHED_POINTS,
						PREVIOUS_ROUND_NOT_FINISHED);
			}
		}
		log(playersAtTable.incrementWithMax(false), "Igrac sednuva");
		Switcher.forceSwitch(5);
	}

	/*
	 * Dilerot deli karti
	 */
	public void dealCards() {
		log(playersAtTable.assertEquals(6, NOT_ENOUGH_PLAYERS_POINTS,
				NOT_ENOUGH_PLAYERS), null);
		synchronized (PokerState.class) {
			cardsDealed = true;
		}
		Switcher.forceSwitch(5);
	}

	/*
	 * Igracot zapocnuva so igrata
	 */
	public void play() {
		synchronized (PokerState.class) {
			if (!cardsDealed) {
				log(new PointsException(CARDS_NOT_DEALED_POINTS,
						CARDS_NOT_DEALED), null);
			}
		}
		log(peoplePlaying.incrementWithMax(false), "Igracot igra poker");
		Switcher.forceSwitch(10);
		log(peoplePlaying.decrementWithMin(false), null);
		peopleFinishedPlaying.incrementWithMax(false);
	}

	/*
	 * Site igraci zavrsile so igranjeto, moze da vleze nova grupa
	 */
	public void endRound() {
		peopleFinishedPlaying.assertEquals(6, NO_6_FINISHED_PLAYERS_POINTS,
				NO_6_FINISHED_PLAYERS);
		log(peoplePlaying.assertEquals(0, PLAYERS_STIL_PLAYING_POINTS,
				PLAYERS_STIL_PLAYING), "Site igraci zavrsija so igranje.");

		synchronized (PokerState.class) {
			// reset scenario
			emptyTable = true;
			cardsDealed = false;
			playersAtTable.setValue(0);
			peopleFinishedPlaying.setValue(0);
		}
		Switcher.forceSwitch(3);
	}

	@Override
	public void finalize() {
		if (peoplePlaying.getMax() == 1) {
			logException(new PointsException(PLAYING_NOT_PARALLEL_POINTS,
					PLAYING_NOT_PARALLEL));
		}

	}

}