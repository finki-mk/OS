package mk.ukim.finki.os.synchronization.exam16.k1.g2;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class CarnivalSolution {

	public static final int GROUP_SIZE = 10;
	public static final int TOTAL = 30;
	private static Semaphore seats = new Semaphore(10);
	private static Semaphore canPlay = new Semaphore(0);
	private static Semaphore newCycle = new Semaphore(0);
	private static Semaphore lock = new Semaphore(1);
	private static int groupNo = 0;
	private static int totalNo = 0;

	public static void init() {
	}

	public static class Participant extends TemplateThread {

		public Participant(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			seats.acquire();
			state.participantEnter();
			lock.acquire();
			groupNo++;
			if (groupNo == GROUP_SIZE) {
				canPlay.release(GROUP_SIZE);
			}
			lock.release();
			canPlay.acquire();
			state.present();
			lock.acquire();
			groupNo--;
			totalNo++;
			if (groupNo == 0) {
				state.endGroup();
				seats.release(GROUP_SIZE);
			}
			if (totalNo == TOTAL) {
				state.endCycle();
				newCycle.release(TOTAL);
				totalNo = 0;
			}
			lock.release();

			newCycle.acquire();
		}

	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			run();
		}
	}

	static CarnivalState state = new CarnivalState();

	public static void run() {
		try {
			int numRuns = 15;
			int numThreads = 30;

			HashSet<Thread> threads = new HashSet<Thread>();

			for (int i = 0; i < numThreads; i++) {
				Participant c = new Participant(numRuns);
				threads.add(c);
			}

			init();

			ProblemExecution.start(threads, state);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
