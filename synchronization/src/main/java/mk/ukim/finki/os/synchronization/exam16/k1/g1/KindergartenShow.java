package mk.ukim.finki.os.synchronization.exam16.k1.g1;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class KindergartenShow {

  public static final int GROUP_SIZE = 6;
  public static final int TOTAL = 24;
  private static Semaphore seats = new Semaphore(6, false);
  private static Semaphore canPlay = new Semaphore(0, false);
  private static Semaphore newCycle = new Semaphore(0, false);
  private static Semaphore lock = new Semaphore(1, false);
  private static int groupNo = 0;
  private static int totalNo = 0;
  private static int sumPermits = 0;
  private static int numExecutions = 0;
  private static int sumQueue = 0;

  public static void init() {
  }

  public static class Child extends TemplateThread {

    public Child(int numRuns) {
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

  static KindergartenShowState state = new KindergartenShowState();

  public static void run() {
    try {
      int numRuns = 24;
      int numIterations = 24;
      numExecutions = 0;
      sumPermits = 0;
      sumQueue = 0;

      HashSet<Thread> threads = new HashSet<Thread>();

      for (int i = 0; i < numIterations; i++) {
        Child c = new Child(numRuns);
        threads.add(c);
      }

      init();

      ProblemExecution.start(threads, state);
      System.out.println(((double) sumPermits) / numExecutions);
      System.out.println(((double) sumQueue) / numExecutions);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
