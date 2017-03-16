package mk.ukim.finki.os.synchronization.exam16.k1.g4;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Tablanet {

  private static Semaphore seats = new Semaphore(4);
  private static Semaphore playerHere = new Semaphore(0);
  private static Semaphore canPlay = new Semaphore(0);
  private static Semaphore playerFinished = new Semaphore(0);
  private static Semaphore newCycle = new Semaphore(0);
  private static Semaphore lock = new Semaphore(1);
  private static int totalNo = 0;

  public static void init() {
  }

  public static class Dealer extends TemplateThread {

    public Dealer(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      playerHere.acquire(4);
      state.dealCards();
      canPlay.release(4);
      playerFinished.acquire(4);
      state.nextGroup();
      seats.release(4);
    }
  }

  public static class Player extends TemplateThread {

    public Player(int numRuns) {
      super(numRuns);
    }


    @Override
    public void execute() throws InterruptedException {
      seats.acquire();
      state.playerSeat();
      playerHere.release();
      canPlay.acquire();
      state.play();
      playerFinished.release();
      lock.acquire();
      totalNo++;
      if (totalNo == 20) {
        state.endCycle();
        newCycle.release(20);
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

  static TablanetState state = new TablanetState();

  public static void run() {
    try {
      int numCycles = 10;
      int numIterations = 20;

      HashSet<Thread> threads = new HashSet<Thread>();

      Dealer d = new Dealer(50);
      threads.add(d);
      for (int i = 0; i < numIterations; i++) {
        Player c = new Player(numCycles);
        threads.add(c);
      }

      init();

      ProblemExecution.start(threads, state);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
