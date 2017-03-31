package mk.ukim.finki.os.synchronization.exam17.k1.g1;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.Date;
import java.util.HashSet;
import java.util.Scanner;

public class MusicBand {


  static MusicBandState state = new MusicBandState();

  public static class GuitarPlayer extends TemplateThread {

    public GuitarPlayer(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.play();
      state.evaluate();
    }

  }

  public static class Singer extends TemplateThread {

    public Singer(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      state.play();

    }

  }

  public static void init() {

  }

  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      run();
    }
  }

  public static void run() {
    try {
      Scanner s = new Scanner(System.in);
      int numRuns = 1;
      int numIterations = 100;
      s.close();

      HashSet<Thread> threads = new HashSet<Thread>();

      for (int i = 0; i < numIterations; i++) {
        Singer singer = new Singer(numRuns);
        threads.add(singer);
        GuitarPlayer gp = new GuitarPlayer(numRuns);
        threads.add(gp);
        gp = new GuitarPlayer(numRuns);
        threads.add(gp);
        singer = new Singer(numRuns);
        threads.add(singer);
        gp = new GuitarPlayer(numRuns);
        threads.add(gp);
      }

      init();

      ProblemExecution.start(threads, state);
      System.out.println(new Date().getTime());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}
