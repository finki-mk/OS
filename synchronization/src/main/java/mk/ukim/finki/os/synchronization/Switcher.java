package mk.ukim.finki.os.synchronization;
import java.util.Random;

public class Switcher {
	private static final Random RANDOM = new Random();

	/*
	 * This method pauses the current thread i.e. changes its state to be
	 * Blocked. This should force thread switch if there are threads waiting
	 */
	public static void forceSwitch(int range) {
		try {
			Thread.sleep(RANDOM.nextInt(range));
		} catch (InterruptedException e) {
		}
	}
}
