package mk.ukim.finki.os.synchronization.exam14.january.examproblem;

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

public class ExamSolution {

	public static Semaphore teacher;
	public static Semaphore studentEnter;
	public static Semaphore studentLeave;
	public static Semaphore studentHere;
	public static Semaphore canStudentLeave;

	public static void init() {
		teacher = new Semaphore(1);
		studentEnter = new Semaphore(0);
		studentLeave = new Semaphore(0);
		studentHere = new Semaphore(0);
		canStudentLeave = new Semaphore(0);
	}

	public static class Teacher extends TemplateThread {

		public Teacher(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {
			teacher.acquire();
			state.teacherEnter();
			
			studentEnter.release(50);
			studentHere.acquire(50);
			state.distributeTests();
			state.examEnd();
			canStudentLeave.release(50);
			studentLeave.acquire(50);
			state.teacherLeave();
			teacher.release();
		}
	}

	public static class Student extends TemplateThread {

		public Student(int numRuns) {
			super(numRuns);
		}

		@Override
		public void execute() throws InterruptedException {

			studentEnter.acquire();
			state.studentEnter();
			studentHere.release();

			canStudentLeave.acquire();
			state.studentLeave();

			studentLeave.release();

		}
	}

	static ExamState state = new ExamState();

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
					Teacher c = new Teacher(numRuns);
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
