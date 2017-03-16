package mk.ukim.finki.os.synchronization.exam16.k1.g3;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Poker {

  private static Semaphore seats = new Semaphore(5);
  private static Semaphore canPlay = new Semaphore(0);
  private static Semaphore newCycle = new Semaphore(0);
  private static Semaphore lock = new Semaphore(1);
  private static int groupNo = 0;
  private static int totalNo = 0;

  public static void init() {
  }

  public static class Player extends TemplateThread {

    public Player(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      seats.acquire();
      state.playerSeat();
      lock.acquire();
      groupNo++;
      if (groupNo == 5) {
        canPlay.release(5);
      }
      lock.release();
      canPlay.acquire();
      state.play();
      lock.acquire();
      groupNo--;
      totalNo++;
      if (groupNo == 0) {
        state.endRound();
        seats.release(5);
      }
      if (totalNo == 15) {
        state.endCycle();
        newCycle.release(15);
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

  static PokerState state = new PokerState();

  public static void run() {
    try {
      int numRuns = 20;
      int numIterations = 15;

      HashSet<Thread> threads = new HashSet<Thread>();

      for (int i = 0; i < numIterations; i++) {
        Player c = new Player(numRuns);
        threads.add(c);
      }

      init();

      ProblemExecution.start(threads, state);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
