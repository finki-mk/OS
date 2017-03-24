package mk.ukim.finki.os.synchronization.exam16.s2;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.Date;
import java.util.HashSet;

public class OxalicAcid {


  public static void init() {


  }


  public static class Carbon extends TemplateThread {

    public Carbon(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.bond();
      state.validate();
    }

  }

  public static class Hydrogen extends TemplateThread {

    public Hydrogen(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.bond();
      state.validate();
    }

  }

  public static class Oxygen extends TemplateThread {

    public Oxygen(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.bond();
      state.validate();
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
