package mk.ukim.finki.os.synchronization.problems;

import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import mk.ukim.finki.os.synchronization.*;

/**
 * 
 * @author ristes
 */
public class SmokersAndAgent {

	public static int NUM_RUNS = 10;

	// TODO: definirajte gi semaforite i ostanatite promenlivi ovde
	static Semaphore accessTable;
	static Semaphore emptyTable;
	static Semaphore wait[];
	static boolean waiting[];

	/**
	 * Metod koj treba da gi inicijalizira vrednostite na semaforite i
	 * ostanatite promenlivi za sinhronizacija.
	 * 
	 * TODO: da se implementira
	 * 
	 */
	public static void init() {
		emptyTable = new Semaphore(1);
		accessTable = new Semaphore(0);
		wait = new Semaphore[3];
		wait[0] = new Semaphore(0);
		wait[1] = new Semaphore(0);
		wait[2] = new Semaphore(0);
		waiting = new boolean[3];

	}

	static class Agent extends TemplateThread {

		public Agent(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			// ceka da mu kazat deka masata e praza i moze da vleze da ja
			// popolni
			Switcher.forceSwitch(10);
			emptyTable.acquire();
			// gi postavuva stavkite
			state.putItems();
			Switcher.forceSwitch(10);
			// im kazuva na tie sto cekaat da prodolzat
			for (int i = 0; i < 3; i++) {
				// proveruvam dali soodvetnio pusac ceka. Ako ne ceka, ne treaba
				// da go oslobodi
				Switcher.forceSwitch(10);
				if (waiting[i]) {
					Switcher.forceSwitch(10);
					waiting[i] = false;
					wait[i].release();
					Switcher.forceSwitch(10);
				}
			}
			accessTable.release();
			Switcher.forceSwitch(10);
		}
	}

	static class Smoker extends TemplateThread {
		private int type;

		public Smoker(int numRuns, int type) {
			super(numRuns);
			this.type = type % 3;
		}

		@Override
		public void execute() throws InterruptedException {
			// semafor koj regulira dali nekoj moze da vleze i da proveri dali
			// negovite stavki se postaveni. Ako nikoj ne pristapuva kon masata,
			// togas ke vleze
			Switcher.forceSwitch(10);
			accessTable.acquire();
			// proveruva dali negovite satvki se postaveni
			if (state.hasMyItems(type)) {
				state.consume(type);
				Switcher.forceSwitch(10);
				emptyTable.release();
			} else {

				// ako ne se postaveni, treba da cekame dodeka agentot ne
				// postavi novi stavki

				// kazuva deka ceka. Moze da se sluci da nema nikoj koj sto
				// ceka, i mora da ima mehanizam s okoj ke se kaze koi pusaci
				// cekaat.
				waiting[type] = true;
				Switcher.forceSwitch(10);
				// oslobodi ja masata
				accessTable.release();
				Switcher.forceSwitch(10);
				// cekaj da te povika agentot po postavuavnjeto na stavkite
				wait[type].acquire();
				Switcher.forceSwitch(10);
			}
		}
	}

	// <editor-fold defaultstate="collapsed" desc="This is the template code" >
	static State state;

	static class State extends AbstractState {

		private static final String MULTIPLE_TABLE_ACCESS = "Multiple processes access the table. The access MUST be exclusive!";
		private static final String REPETITIVE_ACCESS = "Same person makes multiple checks for items in same cycle";
		private boolean[] tableItems;
		private final String[] itemNames;
		private int i = 0;

		private BoundCounterWithRaceConditionCheck tableAccess;
		private BoundCounterWithRaceConditionCheck[] itemAccess;

		public State(int capacity) {
			// initially false
			tableItems = new boolean[3];
			itemNames = new String[] { "Tutun", "Rizla", "Kibrit" };

			tableAccess = new BoundCounterWithRaceConditionCheck(0, 1, 10,
					MULTIPLE_TABLE_ACCESS, null, 0, null);
			itemAccess = new BoundCounterWithRaceConditionCheck[3];
			itemAccess[0] = new BoundCounterWithRaceConditionCheck(0, 1, 10,
					REPETITIVE_ACCESS, null, 0, null);
			itemAccess[1] = new BoundCounterWithRaceConditionCheck(0, 1, 10,
					REPETITIVE_ACCESS, null, 0, null);
			itemAccess[2] = new BoundCounterWithRaceConditionCheck(0, 1, 10,
					REPETITIVE_ACCESS, null, 0, null);

		}

		public boolean hasMyItems(int type) {
			try {
				log(tableAccess.incrementWithMax(), "checking other item for: "
						+ itemNames[type]);
				log(itemAccess[type].incrementWithMax(false), null);
				synchronized (this) {
					return tableItems[(type + 1) % 3]
							&& tableItems[(type + 3 - 1) % 3];
				}
			} finally {
				log(tableAccess.decrementWithMin(), null);
			}
		}

		public void consume(int type) {
			log(tableAccess.incrementWithMax(), "consuming items with my: "
					+ itemNames[type]);
			synchronized (this) {
				tableItems[0] = tableItems[1] = tableItems[2] = false;
			}
			itemAccess[0].decrementWithMin(false);
			itemAccess[1].decrementWithMin(false);
			itemAccess[2].decrementWithMin(false);
			log(tableAccess.decrementWithMin(), null);
		}

		public void putItems() {
			int x = 0;
			synchronized (this) {
				x = i;
				i++;
			}
			int a = (x + 1) % 3;
			int b = (x + 3 - 1) % 3;
			log(tableAccess.incrementWithMax(), "putting items: "
					+ itemNames[a] + " and " + itemNames[b]);
			synchronized (this) {
				tableItems[a] = tableItems[b] = true;
			}
			log(tableAccess.decrementWithMin(), null);

		}

		public void finalize() {

		}
	}

	public static void main(String[] args) {
		try {
			Scanner s = new Scanner(System.in);
			int brKonzumeri = 3;
			int numIterations = 10;
			s.close();

			HashSet<TemplateThread> threads = new HashSet<TemplateThread>();

			for (int i = 0; i < brKonzumeri; i++) {
				Smoker c = new Smoker(numIterations, i);
				threads.add(c);
			}
			Agent p = new Agent(3 * numIterations);
			threads.add(p);

			state = new State(brKonzumeri);

			init();

			// start the threads
			for (Thread t : threads) {
				t.start();
			}

			// wait threads to finish
			for (Thread t : threads) {
				t.join(20000);
			}

			// check for deadlock
			for (TemplateThread t : threads) {
				if (t.isAlive()) {
					t.interrupt();
					System.out.println("interrupted: " + t);
					if (t instanceof Smoker) {
						t.setException(new PointsException(25, "DEADLOCK"));
					}
					if (t instanceof Agent) {
						if (t.iteration < numIterations) {
							t.setException(new PointsException(25, "DEADLOCK"));
						}
					}
				}
			}

			// print the status
			state.printStatus();

			System.out.println(System.currentTimeMillis());

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// </editor-fold>
}
