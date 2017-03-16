package mk.ukim.finki.os.synchronization.exam15.march.sushibar;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

public class SushiBarState extends AbstractState {

	private static final int NO_6_FINISHED_CLIENTS_POINTS = 10;
	private static final String NO_6_FINISHED_CLIENTS = "Nema 6 klienti koi zavrsile so jadenje";

	private static final int CLIENTS_STIL_EATTING_POINTS = 10;
	private static final String CLIENTS_STIL_EATTING = "Ima uste klienti koi ne zavrsile so jadenje";

	private static final int EATING_NOT_PARALLEL_POINTS = 10;
	private static final String EATING_NOT_PARALLEL = "ne e paralelno jadenjeto";

	private static final int FOOD_NOT_SERVED_POINTS = 10;
	private static final String FOOD_NOT_SERVED = "Ne moze da jade koga hranata ne e posluzena";

	private static final int MAXIMUM_6_CUSTOMERS_POINTS = 10;
	private static final String MAXIMUM_6_CUSTOMERS = "Poveke od 6 posetiteli probuvaat da sednat istovremeno!!!";

	private static final int NOT_ENOUGH_CUSTOMERS_POINTS = 10;
	private static final String NOT_ENOUGH_CUSTOMERS = "nema dovolno posetiteli za da se sostavi grupa!!!";

	private BoundCounterWithRaceConditionCheck customersAtTable;
	private BoundCounterWithRaceConditionCheck peopleEating;
	private BoundCounterWithRaceConditionCheck peopleFinishedEating;
	private boolean emptyBar = true;
	private boolean foodServed = false;

	public SushiBarState() {
		customersAtTable = new BoundCounterWithRaceConditionCheck(0, 6,
				MAXIMUM_6_CUSTOMERS_POINTS, MAXIMUM_6_CUSTOMERS, null, 0, null);

		peopleEating = new BoundCounterWithRaceConditionCheck(0);
		peopleFinishedEating = new BoundCounterWithRaceConditionCheck(0);
	}

	/*
	 * Posetitel sednuva vo barot
	 */
	public void customerSeat() {
		synchronized (SushiBarState.class) {
			if (emptyBar) {
				emptyBar = false;
				customersAtTable.assertEquals(0, 10,
						"Prethodnata grupa nema zavrseno so jadenje.");
			}
		}
		log(customersAtTable.incrementWithMax(false), "Posetitel sednuva");
		Switcher.forceSwitch(5);
	}

	/*
	 * Kelnerot gi posluzuva klientite
	 */
	public void callWaiter() {
		log(customersAtTable.assertEquals(6, NOT_ENOUGH_CUSTOMERS_POINTS,
				NOT_ENOUGH_CUSTOMERS), null);
		synchronized (SushiBarState.class) {
			foodServed = true;
		}
		Switcher.forceSwitch(5);
	}

	/*
	 * Posetitel ruca
	 */
	public void customerEat() {
		synchronized (SushiBarState.class) {
			if (!foodServed) {
				log(new PointsException(FOOD_NOT_SERVED_POINTS, FOOD_NOT_SERVED),
						null);
			}
		}
		log(peopleEating.incrementWithMax(false), "Posetitel jade");
		Switcher.forceSwitch(10);
		log(peopleEating.decrementWithMin(false), null);
		peopleFinishedEating.incrementWithMax(false);
	}

	/*
	 * Site klienti zavrsile so jadenjeto, moze da vleze nova grupa
	 */
	public void eatingDone() {
		peopleFinishedEating.assertEquals(6, NO_6_FINISHED_CLIENTS_POINTS,
				NO_6_FINISHED_CLIENTS);
		log(peopleEating.assertEquals(0, CLIENTS_STIL_EATTING_POINTS,
				CLIENTS_STIL_EATTING), "Site klienti zavrsija so jadenje.");

		synchronized (SushiBarState.class) {
			// reset scenario
			emptyBar = true;
			foodServed = false;
			customersAtTable.setValue(0);
			peopleFinishedEating.setValue(0);
		}
		Switcher.forceSwitch(3);
	}

	@Override
	public void finalize() {
		if (peopleEating.getMax() == 1) {
			logException(new PointsException(EATING_NOT_PARALLEL_POINTS,
					EATING_NOT_PARALLEL));
		}

	}

}