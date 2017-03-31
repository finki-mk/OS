package mk.ukim.finki.os.synchronization.exam17.k1.g3;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class Concert2 {

  public static Semaphore ready=new Semaphore(0);
  public static Semaphore done=new Semaphore(0);

  public static Semaphore performerHere=new Semaphore(0);

  public static Semaphore tenorHere=new Semaphore(0);
  public static Semaphore groupForm=new Semaphore(0);
  public static Semaphore groupDone=new Semaphore(0);

  public static Semaphore groupHere=new Semaphore(0);

  public static Semaphore performer=new Semaphore(1);
  public static Semaphore tenor=new Semaphore(3);
  public static Semaphore baritone=new Semaphore(3);


  public static void init() {


  }

  public static class Performer extends TemplateThread {

    public Performer(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      performer.acquire();
      state.performerHere();
      performerHere.release(6);
      groupHere.acquire(3);

      ready.release(6);
      state.perform();
      done.acquire(6);

      state.vote();
      performer.release();
      tenor.release(3);
      baritone.release(3);
    }

  }

  public static class Baritone extends TemplateThread {

    public Baritone(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      baritone.acquire();

      performerHere.acquire();
      tenorHere.acquire();

      groupForm.release();
      state.formBackingVocals();
      groupDone.acquire();

      groupHere.release();

      ready.acquire();
      state.perform();
      done.release();
    }

  }

  public static class Tenor extends TemplateThread {

    public Tenor(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      tenor.acquire();
      performerHere.acquire();
      tenorHere.release();
      groupForm.acquire();
      state.formBackingVocals();
      groupDone.release();
      ready.acquire();
      state.perform();
      done.release();
    }

  }

  static Concert2State state = new Concert2State();

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
        Tenor t = new Tenor(numRuns);
        Baritone b = new Baritone(numRuns);
        threads.add(t);
        if (i % 3 == 0) {
          Performer p = new Performer(numRuns);
          threads.add(p);
        }
        threads.add(b);
      }

      init();

      ProblemExecution.start(threads, state);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
