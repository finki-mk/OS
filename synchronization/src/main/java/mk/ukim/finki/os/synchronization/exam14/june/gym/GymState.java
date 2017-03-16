package mk.ukim.finki.os.synchronization.exam14.june.gym;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

public class GymState extends AbstractState {

	private static final int MAXIMUM_4_PLAYERS_POINTS = 8;
	private static final int MAXIMUM_12_PLAYERS_POINTS = 8;
	private static final int START_PLAY_POINTS = 10;
	private static final int FINISHED_PLAY_POINTS = 8;
	private static final int DRESSING_NOT_PARALLEL_POINTS = 8;
	private static final int PLAYING_NOT_PARALLEL_POINTS = 8;

	private static final String MAXIMUM_4_PLAYERS = "Poveke od 4 igraci se presoblekuvaat istovremeno!!!";
	private static final String MAXIMUM_12_PLAYERS = "Poveke od 12 igraci igraat istovremeno!!!";
	private static final String START_PLAY_MESSAGE = "Ne se prisutni 12 igraci za da zapocne igranjeto!!!";
	private static final String FINISHED_PLAY_MESSAGE = "Ne moze da se zatvori saalta. Seuste ima igraci vo nea!!!";
	private static final String DRESSING_NOT_PARALLEL = "Presoblekuvanjeto ne e paralelizirano!!!";
	private static final String PLAYING_NOT_PARALLEL = "Ne moze da se igra sam po sam!!!";

	private BoundCounterWithRaceConditionCheck dressingRoom;
	private BoundCounterWithRaceConditionCheck play;
	private BoundCounterWithRaceConditionCheck dressedPlayers;
	private BoundCounterWithRaceConditionCheck finishedPlayers;

	public GymState() {
		dressingRoom = new BoundCounterWithRaceConditionCheck(0, 4,
				MAXIMUM_4_PLAYERS_POINTS, MAXIMUM_4_PLAYERS, null, 0, null);
		play = new BoundCounterWithRaceConditionCheck(0, 12,
				MAXIMUM_12_PLAYERS_POINTS, MAXIMUM_12_PLAYERS, null, 0, null);

		dressedPlayers = new BoundCounterWithRaceConditionCheck(0);
		finishedPlayers = new BoundCounterWithRaceConditionCheck(0);

	}

	/**
	 * Moze da se presoblekuvaat maksimum 4 paralelno. Ne treba eden po eden.
	 */
	public void presobleci() {
		log(dressingRoom.incrementWithMax(false), "se presoblekuvam");
		Switcher.forceSwitch(10);
		log(dressingRoom.decrementWithMin(false), null);
		dressedPlayers.incrementWithMax(false);
	}

	/**
	 * Treba da se presobleceni 12 igraci za da zapocne igrata
	 */
	public void sportuvaj() {
		log(dressedPlayers.assertEquals(12, START_PLAY_POINTS,
				START_PLAY_MESSAGE), "zapocnuvam na sportuvam");
		log(play.incrementWithMax(false), null);
		Switcher.forceSwitch(10);
		log(play.decrementWithMin(false), null);
		log(finishedPlayers.incrementWithMax(false), null);
	}

	/**
	 * Treba site 12 igraci da zavrsile so igranjeto. Se povikuva samo od eden.
	 */
	public void slobodnaSala() {
		log(finishedPlayers.assertEquals(12, FINISHED_PLAY_POINTS,
				FINISHED_PLAY_MESSAGE), "zatvoram sala");
		log(dressedPlayers.checkRaceCondition(), null);
		dressedPlayers.setValue(0);
		finishedPlayers.setValue(0);
	}

	@Override
	public void finalize() {
		if (dressingRoom.getMax() == 1) {
			new PointsException(DRESSING_NOT_PARALLEL_POINTS,
					DRESSING_NOT_PARALLEL);
		}

		if (play.getMax() == 1) {
			new PointsException(PLAYING_NOT_PARALLEL_POINTS,
					PLAYING_NOT_PARALLEL);
		}
	}

}
