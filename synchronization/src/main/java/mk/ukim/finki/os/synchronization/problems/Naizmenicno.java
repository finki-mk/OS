package mk.ukim.finki.os.synchronization.problems;

import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;

/**
 * 
 * @author ristes
 */
public class Naizmenicno {

	public static int NUM_RUNS = 1000;
	// TODO: definirajte gi semaforite i ostanatite promenlivi ovde
	Semaphore s1;
	Semaphore s2;
	final Object lock = new Object();
	int f1count;
	int f2count;
	int maxDifference = 0;

	/**
	 * Metod koj treba da gi inicijalizira vrednostite na semaforite i
	 * ostanatite promenlivi za sinhronizacija.
	 * 
	 * 
	 * TODO: da se implementira
	 * 
	 */
	public void init(int count) {
		s1 = new Semaphore(count);
		s2 = new Semaphore(0);
	}

	class F1Thread extends Thread {

		/**
		 * Da se implementira odnesuvanjeto na domakinot spored baranjeto na
		 * zadacata.
		 * 
		 * 
		 * TODO: da se implementira
		 * 
		 */
		public void executeF1() throws InterruptedException {
			s1.acquire();
			synchronized (lock) {
				f1();
			}
			s2.release();
		}

		@Override
		public void run() {
			try {
				executeF1();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class F2Thread extends Thread {

		/**
		 * Da se implementira odnesuvanjeto na ucesnikot spored uslovite na
		 * zadacata.
		 */
		public void executeF2() throws InterruptedException {
			s2.acquire();
			synchronized (lock) {
				f2();
			}
			s1.release();

		}

		@Override
		public void run() {
			try {
				executeF2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void f1() {
		System.out.println("f1()");
		counter.incrementWithMax();
		f1count++;
		if (f1count - f2count > maxDifference) {
			maxDifference = f1count - f2count;
		}
	}

	public void f2() {
		System.out.println("f2()");
		f2count++;
		counter.decrementWithMin();
		if (f1count - f2count > maxDifference) {
			maxDifference = f1count - f2count;
		}
	}

	public static void main(String[] args) {
		try {
			Naizmenicno environment = new Naizmenicno();
			environment.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private BoundCounterWithRaceConditionCheck counter;

	public void start() throws Exception {

		System.out.println("Vnesete za kolku poveke sakate da se izvrsi f1()");
		Scanner s = new Scanner(System.in);
		int n = s.nextInt();
		init(n);
		counter = new BoundCounterWithRaceConditionCheck(0, n, 10,
				"maksimumot e nadminat", null, 0, null);

		HashSet<Thread> threads = new HashSet<Thread>();
		for (int i = 0; i < NUM_RUNS; i++) {
			F1Thread f1 = new F1Thread();
			F2Thread f2 = new F2Thread();
			threads.add(f1);
			threads.add(f2);
		}

		for (Thread t : threads) {
			t.start();
		}

		for (Thread t : threads) {
			t.join();
		}
		PointsException.printErrors();
		System.out.println("F1count: " + f1count);
		System.out.println("F2count: " + f2count);
		System.out.println("maxDifference: " + maxDifference);
		System.out.println("Status: " + (maxDifference <= n));
	}
}
