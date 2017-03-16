package mk.ukim.finki.os.synchronization.exam15.march.poker;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

public class Poker {


  static Semaphore masa = new Semaphore(1);
  static Semaphore igrac = new Semaphore(0);
  static Semaphore igraci = new Semaphore(6);
  static Semaphore lock = new Semaphore(0);
  static int i = 0;
  static boolean polna = true;


  public static void init() {
  }

  public static class Player extends TemplateThread {

    public Player(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {

      masa.acquire();

      synchronized (state) {
        i++;
        if (i == 6) {

          igraci.acquire();

          state.playerSeat();

          state.dealCards();
          state.play();

        }
      }
    // prekin
      synchronized (state) {
        i--;
        if (i == 0) {
          state.endRound();
          igraci.release(6);
          masa.release();

        }

      }
      lock.release();

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
      int numRuns = 1;
      int numIterations = 1200;

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
