package mk.ukim.finki.os.synchronization.exam17.k1.g2;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.Date;
import java.util.HashSet;

public class Concert {


  public static void init() {


  }

  public static class Performer extends TemplateThread {

    public Performer(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.perform();
      state.vote();
    }

  }

  public static class Baritone extends TemplateThread {

    public Baritone(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.formBackingVocals();
      state.perform();
    }

  }

  public static class Tenor extends TemplateThread {

    public Tenor(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.formBackingVocals();
      state.perform();
    }

  }

  static ConcertState state = new ConcertState();

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
