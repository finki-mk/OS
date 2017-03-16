package mk.ukim.finki.os.synchronization.exam14.june.gym;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;

public class GymSynchronization {

  public static void init() {

  }

  public static class Player extends TemplateThread {

    public Player(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.presobleci();
      state.sportuvaj();
      state.slobodnaSala();
    }

  }

  static GymState state = new GymState();

  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      run();
    }
  }

  public static void run() {
    try {
      Scanner s = new Scanner(System.in);
      int numRuns = 1;
      int numIterations = 1200;
      s.close();

      HashSet<Thread> threads = new HashSet<Thread>();

      for (int i = 0; i < numIterations; i++) {
        Player h = new Player(numRuns);
        threads.add(h);
      }

      init();

      ProblemExecution.start(threads, state);
      System.out.println(new Date().getTime());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
