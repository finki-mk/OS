package mk.ukim.finki.os.synchronization;

import java.util.HashSet;

public abstract class ProblemExecution {

	public static void start(HashSet<Thread> threads, AbstractState state)
			throws Exception {

		startWithoutDeadlock(threads, state);

		checkDeadlock(threads, state);
	}

	public static void startWithoutDeadlock(HashSet<Thread> threads,
			AbstractState state) throws Exception {

		// start the threads
		for (Thread t : threads) {
			t.start();
		}

		// wait threads to finish
		for (Thread t : threads) {
			t.join(1000);
		}

	}

	private static void checkDeadlock(HashSet<Thread> threads,
			AbstractState state) {
		// check for deadlock
		for (Thread t : threads) {
			if (t.isAlive()) {
				t.interrupt();
				if (t instanceof TemplateThread) {
					TemplateThread tt = (TemplateThread) t;
					tt.setException(new PointsException(25, "DEADLOCK"));
				}
			}
		}

		// print the status
		state.printStatus();
	}

}
