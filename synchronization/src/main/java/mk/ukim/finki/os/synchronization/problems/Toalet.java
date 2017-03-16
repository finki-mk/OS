package mk.ukim.finki.os.synchronization.problems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 
 * @author ristes
 */
public class Toalet {

	// Konstanti
	public static int BROJ_LUGJE = 100;
	public static final Random RANDOM = new Random();
	// Instanca od toaletot
	public WC wc;
	// TODO: Definicija na globalni promenlivi i semafori
	int maziVnatre;
	int zeniVnatre;
	public final Object mLock = new Object();
	public final Object zLock = new Object();
	public Semaphore toalet;

	public void init() {
		// TODO: da se implementira
		// inicijalno toaletot e sloboden
		toalet = new Semaphore(1);

	}

	class Maski extends Thread {
		// TODO: Definicija na promenlivi za sostojbata

		public void mazVleguva() throws InterruptedException {
			// poceten kod:
			// wc.vlezi();
			// TODO: da se implementira
			synchronized (mLock) {
				if (maziVnatre == 0) {
					toalet.acquire();
				}
				maziVnatre++;
				wc.vlezi();
			}
		}

		public void mazIzleguva() throws InterruptedException {
			// poceten kod:
			// wc.izlezi();
			// TODO: da se implementira
			synchronized (mLock) {
				wc.izlezi();
				maziVnatre--;
				if (maziVnatre == 0) {
					toalet.release();
				}
			}
		}

		public Maski() {
		}

		@Override
		public void run() {
			try {
				mazVleguva();
				try {
					int r;
					synchronized (RANDOM) {
						r = RANDOM.nextInt(7);
					}
					Thread.sleep(r);
				} catch (Exception e) {
					// do nothing
				}
				mazIzleguva();
			} catch (InterruptedException e) {
				// Do nothing
			} catch (Exception e) {
				exception = e;
			}
		}

		@Override
		public String toString() {
			return String.format("m\t%d", getId());
		}

		public Exception exception = null;
	}

	class Zenski extends Thread {
		// TODO: Definicija na promenlivi za sostojbata

		public Zenski() {
		}

		public void zenaVleguva() throws InterruptedException {
			// poceten kod:
			// wc.vlezi();
			// TODO: da se implementira
			synchronized (zLock) {
				if (zeniVnatre == 0) {
					toalet.acquire();
				}
				zeniVnatre++;
				wc.vlezi();
			}
		}

		public void zenaIzleguva() throws InterruptedException {
			// poceten kod:
			// wc.izlezi();
			// TODO: da se implementira
			synchronized (zLock) {
				wc.izlezi();
				zeniVnatre--;
				if (zeniVnatre == 0) {
					toalet.release();
				}
			}
		}

		@Override
		public void run() {
			try {
				zenaVleguva();
				try {
					int r;
					synchronized (RANDOM) {
						r = RANDOM.nextInt(7);
					}
					Thread.sleep(r);
				} catch (Exception e) {
					// do nothing
				}
				zenaIzleguva();
			} catch (InterruptedException e) {
				// Do nothing
			} catch (Exception e) {
				exception = e;
			}
		}

		@Override
		public String toString() {
			return String.format("z\t%d", getId());
		}

		public Exception exception = null;
	}

	public class WC {

		public WC() {
		}

		private int brojVnatre = 0;
		private boolean imaMaz = false;
		private boolean imaZena = false;

		public void vlezi() throws RuntimeException {
			proveriMoze(true);
			if (Thread.currentThread() instanceof Zenski) {
				imaZena = true;
			} else {
				imaMaz = true;
			}
			brojVnatre++;
			log(null, "vleguva " + brojVnatre);
		}

		public void izlezi() throws RuntimeException {
			proveriMoze(false);
			if (proveriPrazno()) {
				RuntimeException e = new RuntimeException(
						"Toaletot e prazen. Nema koj da izleze.");
				log(e, null);
				throw e;
			}
			brojVnatre--;
			if (proveriPrazno()) {
				imaMaz = false;
				imaZena = false;
			}
			log(null, "izleguva " + brojVnatre);
		}

		public void proveriMoze(boolean in) {
			if (!proveriPrazno()) {
				if (imaMaz && (Thread.currentThread() instanceof Zenski)) {
					RuntimeException e = new RuntimeException(
							"Vo toaletot ima maski, a se obiduva da "
									+ (in ? "vleze" : "izleze") + " zensko.");
					log(e, null);
					throw e;
				}
				if (imaZena && (Thread.currentThread() instanceof Maski)) {
					RuntimeException e = new RuntimeException(
							"Vo toaletot ima zenski, a se obiduva da "
									+ (in ? "vleze" : "izleze") + " masko.");
					log(e, null);
					throw e;
				}
			}
		}

		public boolean proveriPrazno() {
			return brojVnatre == 0;
		}

		private List<String> actions = new ArrayList<String>();

		private synchronized void log(RuntimeException e, String action) {
			Thread t = Thread.currentThread();
			if (e == null) {
				actions.add(t.toString() + "\t(a): " + action);
			} else {
				actions.add(t.toString() + "\t(e): " + e.getMessage());
			}
		}

		public synchronized void printLog() {
			System.out
					.println("Poradi konkurentnosta za pristap za pecatenje, mozno e nekoja od porakite da ne e na soodvetnoto mesto.");
			System.out.println("Log na izvrsuvanje na akciite:");
			System.out.println("=========================");
			System.out.println("(tip m<=>Masko, tip z<=>Zensko)");
			System.out.println("tip\tid\titer\takcija/error");
			System.out.println("=========================");
			for (String l : actions) {
				System.out.println(l);
			}
		}
	}

	public static void main(String[] args) {
		try {
			Toalet environment = new Toalet();
			environment.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void start() throws Exception {
		wc = new WC();
		init();
		HashSet<Thread> threads = new HashSet<Thread>();
		for (int i = 0; i < BROJ_LUGJE; i++) {
			Zenski z = new Zenski();
			Maski m = new Maski();
			threads.add(z);
			threads.add(m);
		}

		for (Thread t : threads) {
			t.start();
		}

		boolean valid = true;
		for (Thread t : threads) {
			t.join();
			if (t instanceof Maski) {
				Maski d = (Maski) t;
				if (d.exception != null) {
					valid = false;
				}
			}
			if (t instanceof Zenski) {
				Zenski r = (Zenski) t;
				if (r.exception != null) {
					valid = false;
				}
			}
		}
		if (valid) {
			System.out.println("Procesot e uspesno sinhroniziran");
		} else {
			System.out
					.println("Procesot ne e sinhroniziran spored uslovite na zadacata");
			wc.printLog();
		}

	}
}
