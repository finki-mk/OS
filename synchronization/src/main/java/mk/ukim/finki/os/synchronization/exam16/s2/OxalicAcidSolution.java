package mk.ukim.finki.os.synchronization.exam16.s2;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class OxalicAcidSolution {

  public static Semaphore o = new Semaphore(4);
  public static Semaphore h = new Semaphore(2);
  public static Semaphore c = new Semaphore(2);

  public static Semaphore hHere = new Semaphore(0);
  public static Semaphore cHere = new Semaphore(0);

  public static Semaphore ready = new Semaphore(0);
  public static Semaphore done = new Semaphore(0);

  public static int oNum = 0;


  public static void init() {


  }


  public static class Carbon extends TemplateThread {

    public Carbon(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      c.acquire();
      cHere.release();
      ready.acquire();
      state.bond();
      done.release();
      //state.validate();
    }

  }

  public static class Hydrogen extends TemplateThread {

    public Hydrogen(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      h.acquire();
      hHere.release();
      ready.acquire();
      state.bond();
      done.release();
      //state.validate();

    }

  }

  public static class Oxygen extends TemplateThread {

    public Oxygen(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      o.acquire();
      synchronized (Oxygen.class) {
        oNum++;
        if (oNum == 4) {
          hHere.acquire(2);
          cHere.acquire(2);
          oNum = 0;
          ready.release(8);
        }
      }
      ready.acquire();
      state.bond();
      done.release();
      synchronized (Oxygen.class) {
        oNum++;
        if (oNum == 4) {
          done.acquire(8);
          state.validate();
          o.release(4);
          c.release(2);
          h.release(2);
          oNum = 0;
        }
      }

    }

  }


  static OxalicAcidState state = new OxalicAcidState();

  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      run();
    }
  }

  public static void run() {
    try {
      int numRuns = 1;
      int numScenarios = 300;

      HashSet<Thread> threads = new HashSet<Thread>();

      for (int i = 0; i < numScenarios; i++) {
        Oxygen o = new Oxygen(numRuns);

        threads.add(o);
        if (i % 2 == 0) {
          Hydrogen h = new Hydrogen(numRuns);
          Carbon c = new Carbon(numRuns);
          threads.add(c);
          threads.add(h);
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
