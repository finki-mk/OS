package mk.ukim.finki.os.synchronization.problems;

import java.util.HashSet;
import java.util.Scanner;

/**
 * 
 * @author ristes
 */
public class CountThree {

	public static int NUM_RUNS = 100;
	/**
	 * Promenlivata koja treba da go sodrzi brojot na pojavuvanja na elementot 3
	 */
	int count = 0;
	/**
	 * TODO: definirajte gi potrebnite semafori ovde
	 */

	// Monitor za kriticniot region za pristap na count
	final Object lock = new Object();

	/*
	 * TODO: implementiraj spored baranjeto
	 */
	public void init() {
	}

	class Counter extends Thread {

		public void count(int[] data) throws InterruptedException {
			int localCount = 0;
			for (int i = 0; i < data.length; i++) {

				if (data[i] == 3) {
					// ako imame 100 3ki vo nizata data[], 100 pati ke zaklucime
					// i ke bide premnogu bavno. Ako celata niza e so 3ki, togas
					// ovoj kod e pobaven odkolku sekventnoto izvrsuvanje
					/*
					 * synchronized (lock) { count++; }
					 */

					// ova e localna promenliva i ne mora da se sinhronizira
					localCount++;
				}
			}
			// bez razlika kolku 3ki ima vo nizata data[] ke imame samo edno
			// zaklucuvanje po thread i znacitelno ke se namali vremeto na
			// cekanje i imame vistinska paralelizacija
			synchronized (lock) {
				count += localCount;
			}
		}

		private int[] data;

		public Counter(int[] data) {
			this.data = data;
		}

		@Override
		public void run() {
			try {
				count(data);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			CountThree environment = new CountThree();
			environment.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void start() throws Exception {

		init();

		HashSet<Thread> threads = new HashSet<Thread>();
		Scanner s = new Scanner(System.in);
		int total = s.nextInt();
		for (int i = 0; i < NUM_RUNS; i++) {
			int[] data = new int[total];
			for (int j = 0; j < total; j++) {
				data[j] = s.nextInt();
			}
			Counter prod = new Counter(data);
			threads.add(prod);
		}

		for (Thread t : threads) {
			t.start();
		}

		for (Thread t : threads) {
			t.join();
		}
		System.out.println(count);

	}
}
