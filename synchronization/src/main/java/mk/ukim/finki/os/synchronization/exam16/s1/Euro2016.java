package mk.ukim.finki.os.synchronization.exam16.s1;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.Date;
import java.util.HashSet;

public class Euro2016 {

  public static void init() {

  }


  static class FanA extends TemplateThread {

    public FanA(int numRuns) {
      super(numRuns);
    }

    public void execute() throws InterruptedException {
      state.board();
      state.departure();
    }
  }

  static class FanB extends TemplateThread {

    public FanB(int numRuns) {
      super(numRuns);
    }

    public void execute() throws InterruptedException {
      state.board();
      state.departure();
    }

  }


  static Euro2016State state = new Euro2016State();

  public static void main(String[] args) {
    for (int i = 0; i < 15; i++)
      run();
  }

  public static void run() {
    try {
      int numRuns = 1;
      int numIterations = 120;

      HashSet<Thread> threads = new HashSet<Thread>();

      for (int i = 0; i < numIterations; i++) {
        FanA h = new FanA(numRuns);
        FanB s = new FanB(numRuns);
        threads.add(h);
        threads.add(s);
      }

      init();

      ProblemExecution.start(threads, state);
      System.out.println(new Date().getTime());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
