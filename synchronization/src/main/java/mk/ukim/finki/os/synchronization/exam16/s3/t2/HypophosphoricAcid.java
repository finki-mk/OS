package mk.ukim.finki.os.synchronization.exam16.s3;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.Date;
import java.util.HashSet;

public class HypophosphoricAcid {


  public static void init() {


  }


  public static class Phosphorus extends TemplateThread {

    public Phosphorus(int numRuns) {
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


  static HypophosphoricAcidState state = new HypophosphoricAcidState();

  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      run();
    }
  }

  public static void run() {
    try {
      int numRuns = 1;
      int numScenarios = 100;

      HashSet<Thread> threads = new HashSet<Thread>();

      for (int i = 0; i < numScenarios; i++) {
        for (int j = 0; j < state.O_ATOMS; j++) {
          Oxygen o = new Oxygen(numRuns);
          threads.add(o);
        }
        for (int j = 0; j < state.H_ATOMS; j++) {
          Hydrogen h = new Hydrogen(numRuns);
          threads.add(h);
        }

        for (int j = 0; j < state.P_ATOMS; j++) {
          Phosphorus p = new Phosphorus(numRuns);
          threads.add(p);
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
