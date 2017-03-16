package mk.ukim.finki.os.synchronization.exam14.september;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

public class SeptemberTribeDinnerSolution {

	static Semaphore table = new Semaphore(4);
	static Semaphore lock = new Semaphore(1);
	static Semaphore empty = new Semaphore(0);
	static Semaphore filled = new Semaphore(0);

	public static void init() {

	}

	public static class TribeMember extends TemplateThread {

		public TribeMember(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {

			lock.acquire();
			// vrakja dali e prazen kazanot
			if (state.isPotEmpty()) {
				empty.release();
				filled.acquire();
			}
			// metod za polnenje na cinijata
			state.fillPlate();
			lock.release();

			table.acquire();
			// dokolku ima mesto na trpezata, treba da se povika
			state.eat();
			table.release();

		}

	}

	public static class Chef extends TemplateThread {

		public Chef(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			empty.acquire();
			state.cook();
//			System.out.println("filled");
			filled.release();
		}

	}

	static SeptemberTribeDinnerState state = new SeptemberTribeDinnerState();

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			run();
		}
	}

	public static void run() {
		try {
			int numRuns = 1;
			int numIterations = 150;

			HashSet<Thread> threads = new HashSet<Thread>();

			for (int i = 0; i < numIterations; i++) {
				TribeMember h = new TribeMember(numRuns);
				threads.add(h);
			}

			Chef chef = new Chef(10);
			threads.add(chef);

			init();

			ProblemExecution.start(threads, state);
			System.out.println(new Date().getTime());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
