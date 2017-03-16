package mk.ukim.finki.os.synchronization.exam14.june.gym;

import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

public class GymSolution {

	public static void init() {

	}

	static Semaphore sala = new Semaphore(12);
	static Semaphore soblekuvalna = new Semaphore(4);
	static Semaphore start = new Semaphore(0);
	static int i = 0;

	public static class Player extends TemplateThread {

		public Player(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			sala.acquire();
			soblekuvalna.acquire();
			state.presobleci();
			soblekuvalna.release();
			synchronized (state) {
				i++;
				if (i == 12) {
					start.release(12);
				}
			}
			start.acquire();
			state.sportuvaj();
			synchronized (state) {
				i--;
				if (i == 0) {
					state.slobodnaSala();
					sala.release(12);
				}
			}
		}

	}

	static GymState state = new GymState();

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			run();
		}
	}

	public static void run() {
		try {
			Scanner s = new Scanner(System.in);
			int numRuns = 1;
			int numIterations = 1200;
			s.close();

			HashSet<Thread> threads = new HashSet<Thread>();

			for (int i = 0; i < numIterations; i++) {
				Player h = new Player(numRuns);
				threads.add(h);
			}

			init();

			ProblemExecution.start(threads, state);
			System.out.println(new Date().getTime());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
