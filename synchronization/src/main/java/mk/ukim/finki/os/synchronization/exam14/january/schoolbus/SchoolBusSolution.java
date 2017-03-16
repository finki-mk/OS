package mk.ukim.finki.os.synchronization.exam14.january.schoolbus;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

/**
 * 
 * @author ristes
 * 
 */

public class SchoolBusSolution {
	static int count = 0;
	static Semaphore lock;
	static Semaphore driver;
	static Semaphore student;
	static Semaphore departure;
	static Semaphore sleave;
	static Semaphore dleave;

	public static void init() {
		lock = new Semaphore(1);
		driver = new Semaphore(1);
		student = new Semaphore(0);
		departure = new Semaphore(0);
		sleave = new Semaphore(0);
		dleave = new Semaphore(0);

	}

	public static class Driver extends TemplateThread {

		public Driver(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			driver.acquire();
			lock.acquire();
			state.driverEnter();
			student.release(50);
			lock.release();


			departure.acquire();

			lock.acquire();
			state.busDeparture();
			state.busArrive();
			sleave.release(50);
			lock.release();


			dleave.acquire();
			lock.acquire();
			state.driverLeave();
			driver.release();
			lock.release();
		}
	}

	public static class Student extends TemplateThread {

		public Student(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			student.acquire();
			state.studentEnter();
			lock.acquire();
			count++;
			if(count == 50){
				count = 0;
				departure.release();
			}
			lock.release();

			sleave.acquire();
			lock.acquire();
			state.studentLeave();
			count++;
			if(count == 50){
				count = 0;
				dleave.release();
			}
			lock.release();
		}
	}
	static SchoolBusState state = new SchoolBusState();

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			run();
		}
	}

	public static void run() {
		try {
			int numRuns = 1;
			int numScenarios = 1000;
			HashSet<Thread> threads = new HashSet<Thread>();

			for (int i = 0; i < numScenarios; i++) {
				Student p = new Student(numRuns);
				threads.add(p);
				if (i % 50 == 0) {
					Driver c = new Driver(numRuns);
					threads.add(c);
				}
			}

			init();

			ProblemExecution.start(threads, state);
			System.out.println(new Date().getTime());
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
