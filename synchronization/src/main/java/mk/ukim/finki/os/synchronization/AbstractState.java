package mk.ukim.finki.os.synchronization;

import java.util.ArrayList;
import java.util.List;

/**
 * This class should be extended in order to preserve the state of the executing
 * scenario
 * 
 * @author ristes
 * 
 */
public abstract class AbstractState {

	/**
	 * Method called after threads ended their execution to validate the
	 * correctness of the scenario
	 */
	public abstract void finalize();

	/**
	 * List of logged actions
	 */
	private List<String> actions = new ArrayList<String>();

	/**
	 * 
	 * @return if the current thread is instance of TemplateThread it is
	 *         returned, and otherwise null is returned
	 */
	protected TemplateThread getThread() {
		Thread current = Thread.currentThread();
		if (current instanceof TemplateThread) {
			TemplateThread t = (TemplateThread) current;
			return t;
		} else {
			return null;
		}
	}

	/**
	 * Log this exception or action
	 * 
	 * @param e
	 *            occurred exception (null if no exception)
	 * @param action
	 *            Description of the occurring action
	 */
	public synchronized void log(PointsException e, String action) {
		TemplateThread t = (TemplateThread) Thread.currentThread();
		if (e != null) {
			t.setException(e);
			actions.add(t.toString() + "\t(e): " + e.getMessage());
			throw e;
		} else if (action != null) {
			actions.add(t.toString() + "\t(a): " + action);
		}
	}

	/**
	 * Logging exceptions
	 * 
	 * @param e
	 */
	protected synchronized void logException(PointsException e) {
		Thread t = Thread.currentThread();
		if (e != null) {
			if (t instanceof TemplateThread) {
				((TemplateThread) t).setException(e);
			}
			TemplateThread.hasException = true;
			actions.add("\t(e): " + e.getMessage());
			throw e;
		}
	}

	/**
	 * Printing of the actions and exceptions that has occurred
	 */
	public synchronized void printLog() {
		System.out
				.println("Poradi konkurentnosta za pristap za pecatenje, mozno e nekoja od porakite da ne e na soodvetnoto mesto.");
		System.out.println("Log na izvrsuvanje na akciite:");
		System.out.println("=========================");
		System.out.println("tip\tid\titer\takcija/error");
		System.out.println("=========================");
		for (String l : actions) {
			System.out.println(l);
		}
	}

	/**
	 * Prints the status of the execution, with the exceptions that has occur
	 */
	public void printStatus() {
		try {
			finalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!TemplateThread.hasException) {
			int poeni = 25;
			if (PointsException.getTotalPoints() == 0) {
				System.out
						.println("Procesot e uspesno sinhroniziran. Osvoeni 25 poeni.");
			} else {
				poeni -= PointsException.getTotalPoints();
				PointsException.printErrors();
				System.out.println("Maksimalni osvoeni poeni: " + poeni);
			}

		} else {
			System.out
					.println("Procesot ne e sinhroniziran spored uslovite na zadacata");
			printLog();
			System.out
					.println("====================================================");
			PointsException.printErrors();
			int total = (25 - PointsException.getTotalPoints());
			if (total < 0) {
				total = 0;
			}
			System.out.println("Maksimum Poeni: " + total);
		}

	}
}
