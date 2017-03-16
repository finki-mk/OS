package mk.ukim.finki.os.synchronization.exam15.march.sushibar;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class SushiBarSolution {

	private static Semaphore seats = new Semaphore(6);
	private static Semaphore canEat = new Semaphore(0);
	private static Semaphore lock = new Semaphore(1);
	private static int customerNo = 0;

	public static void init() {
	}

	public static class Customer extends TemplateThread {

		public Customer(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			seats.acquire();
			state.customerSeat();
			lock.acquire();
			customerNo++;
			if (customerNo == 6) {
				state.callWaiter();
				canEat.release(6);
			}
			lock.release();

			canEat.acquire();
			state.customerEat();

			lock.acquire();
			customerNo--;
			if (customerNo == 0) {
				state.eatingDone();
				seats.release(6);
			}
			lock.release();

		}

	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			run();
		}
	}

	static SushiBarState state = new SushiBarState();

	public static void run() {
		try {
			int numRuns = 1;
			int numIterations = 1200;

			HashSet<Thread> threads = new HashSet<Thread>();

			for (int i = 0; i < numIterations; i++) {
				Customer c = new Customer(numRuns);
				threads.add(c);
			}

			init();

			ProblemExecution.start(threads, state);
			// System.out.println(new Date().getTime());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
