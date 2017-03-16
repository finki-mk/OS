package mk.ukim.finki.os.synchronization.problems;

import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.Switcher;
import mk.ukim.finki.os.synchronization.TemplateThread;

/**
 * 
 * @author ristes
 */
public class ProducerController {

	public static int NUM_RUNS = 10;

	// TODO: definirajte gi semaforite i ostanatite promenlivi ovd

	static Semaphore accessBuffer;
	static Semaphore canCheck;
	static Semaphore lock;
	static int numChecks;

	public static void init() {
		accessBuffer = new Semaphore(1);
		canCheck = new Semaphore(10);
		lock = new Semaphore(1);
		numChecks = 0;
	}

	static class Producer extends TemplateThread {

		public Producer(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			/* Producer */
			accessBuffer.acquire();
			state.produce();
			accessBuffer.release();
		}
	}

	static class Controller extends TemplateThread {

		public Controller(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			/* Controller */
			lock.acquire();
			if (numChecks == 0) {
				accessBuffer.acquire();
			}
			// moze da bide i pogolemo od 10
			numChecks++;
			lock.release();
			// Ovde ke pominat max 10. Ste posle desetiot ke cekaat dodeka ne
			// zavrsi nekoja zapocnatite proverki
			canCheck.acquire();

			state.check();// 10 sec
			// ovoj lock ni e ptrebno zaradi numChecks (shared promenliva) i ne
			// smee paralelno da se modificira
			lock.acquire();
			numChecks--;
			// zavrsila proverkata
			canCheck.release();
			// ako sum posleden koj proveruva
			if (numChecks == 0) {
				// oslobodi go pristapot do baferot (za eventualno da moze da se
				// dodade vo nego
				accessBuffer.release();
			}
			lock.release();
		}
	}

	// <editor-fold defaultstate="collapsed" desc="This is the template code" >
	static State state;

	static class State extends AbstractState {

		private BoundCounterWithRaceConditionCheck numberOfParallelChecks;
		private BoundCounterWithRaceConditionCheck bufferAccess;

		public State(int capacity) {
			numberOfParallelChecks = new BoundCounterWithRaceConditionCheck(0,
					10, 10,
					"The maximum number of parallel checks is exceeded!!!",
					null, 0, null);
			bufferAccess = new BoundCounterWithRaceConditionCheck(0, 1, 10,
					"Only one producing is allowed at a time!!!", null, 0, null);
		}

		public void check() {
			log(numberOfParallelChecks.incrementWithMax(false), "checking item");
			synchronized (bufferAccess) {
				logException(bufferAccess.incrementWithMax(false));
				bufferAccess.decrementWithMin(false);
			}
			Switcher.forceSwitch(5);
			numberOfParallelChecks.decrementWithMin(false);
		}

		public void produce() {
			log(bufferAccess.incrementWithMax(), "producing items");
			bufferAccess.decrementWithMin();
		}

		public void finalize() {
			if (numberOfParallelChecks.getMax() == 1) {
				logException(new PointsException(10,
						"The checking process is not parellized"));
			}
		}
	}

	public static void main(String[] args) {
		try {
			Scanner s = new Scanner(System.in);
			int brKonzumeri = s.nextInt();
			int numIterations = s.nextInt();
			s.close();

			HashSet<Thread> threads = new HashSet<Thread>();

			for (int i = 0; i < brKonzumeri; i++) {
				Controller c = new Controller(numIterations);
				threads.add(c);
			}
			for (int i = 0; i < brKonzumeri; i++) {
				Producer p = new Producer(numIterations);
				threads.add(p);
			}

			state = new State(brKonzumeri);

			init();

			ProblemExecution.start(threads, state);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// </editor-fold>
}
