package mk.ukim.finki.os.synchronization.exam15.march.poker;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

public class PokerSolution {

	private static Semaphore seats = new Semaphore(6);
	private static Semaphore canPlay = new Semaphore(0);
	private static Semaphore lock = new Semaphore(1);
	private static int customerNo = 0;

	public static void init() {
	}

	public static class Player extends TemplateThread {

		public Player(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			seats.acquire();
			state.playerSeat();
			lock.acquire();
			customerNo++;
			if (customerNo == 6) {
				state.dealCards();
				canPlay.release(6);
			}
			lock.release();

			canPlay.acquire();
			state.play();

			lock.acquire();
			customerNo--;
			if (customerNo == 0) {
				state.endRound();
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

	static PokerState state = new PokerState();

	public static void run() {
		try {
			int numRuns = 1;
			int numIterations = 1200;

			HashSet<Thread> threads = new HashSet<Thread>();

			for (int i = 0; i < numIterations; i++) {
				Player c = new Player(numRuns);
				threads.add(c);
			}

			init();

			ProblemExecution.start(threads, state);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
