package mk.ukim.finki.os.synchronization.exam14.september;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

public class SeptemberTribeDinnerSynchronization {
	public static Semaphore mut, cek, zabr, fill, jade;
	public static int br = 0;

	public static void init() {
		mut = new Semaphore(1);
		cek = new Semaphore(0);
		// mut =new Semaphore(1);
		fill = new Semaphore(1);
		jade = new Semaphore(4);
	}

	public static class TribeMember extends TemplateThread {

		public TribeMember(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {

			// vrakja dali e prazen kazanot
			mut.acquire();
			if (state.isPotEmpty()) {
				br++;
				mut.release();
				cek.acquire();
			} else {
				//prekin
				fill.acquire();
				state.fillPlate();
				fill.release();
				mut.release();
			}

			// metod za polnenje na cinijata

			jade.acquire();
			// dokolku ima mesto na trpezata, treba da se povika
			state.eat();
			jade.release();

		}

	}

	public static class Chef extends TemplateThread {

		public Chef(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			// go polni kazanot
			mut.acquire();
			if (state.isPotEmpty()) {
				cek.release(br);
				br = 0;
				mut.release();
				state.cook();

			}

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
