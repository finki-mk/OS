package mk.ukim.finki.os.synchronization.problems;

import mk.ukim.finki.os.synchronization.*;

import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

/**
 * @author ristes
 */
public class ProducerConsumerAlternative {

  public static int NUM_RUNS = 10;

  // TODO: definirajte gi semaforite i ostanatite promenlivi. Mora da se
  // static
  public static Semaphore bufferAccess;
  public static Semaphore emptyTable;
  public static Semaphore bufferSizeLock = new Semaphore(1);
  public static Semaphore consumerContinue = new Semaphore(0);

  static State state;

  static class Producer extends TemplateThread {

    public Producer(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      emptyTable.acquire();
      System.out.println("fill buffer");
      bufferSizeLock.acquire();
      state.fillBuffer();
      bufferSizeLock.release();
      bufferAccess.release(state.getBufferCapacity());//

    }
  }

  static class Consumer extends TemplateThread {
    private int cId;

    public Consumer(int numRuns, int id) {
      super(numRuns);
      cId = id;
    }

    @Override
    public void execute() throws InterruptedException {
      bufferAccess.acquire();// 
      System.out.println("Consume item: " + cId);
      state.getItem(cId);

      bufferSizeLock.acquire();// 1
      state.decrementNumberOfItemsLeft();
      // pause
      if (state.isBufferEmpty()) {
        // pause
        emptyTable.release();
        consumerContinue.release(state.getBufferCapacity());
      }
      bufferSizeLock.release();
      System.out.println("Done: " + cId);

      consumerContinue.acquire();


    }
  }

  // <editor-fold defaultstate="collapsed" desc="This is the template code" >

  static class State extends AbstractState {

    private static final String _10_DVAJCA_ISTOVREMENO_PROVERUVAAT = "Dvajca istovremeno proveruvaat dali baferot e prazen. Maksimum eden e dozvoleno.";
    private static final String _10_KONZUMIRANJETO_NE_E_PARALELIZIRANO = "Konzumiranjeto ne e paralelizirano.";
    private int bufferCapacity = 15;

    private BoundCounterWithRaceConditionCheck[] items;
    private BoundCounterWithRaceConditionCheck counter = new BoundCounterWithRaceConditionCheck(
      0);
    private BoundCounterWithRaceConditionCheck raceConditionTester = new BoundCounterWithRaceConditionCheck(
      0);
    private BoundCounterWithRaceConditionCheck bufferFillCheck = new BoundCounterWithRaceConditionCheck(
      0, 1, 10, "", null, 0, null);
    private int itemsLeft = 0;

    public State(int capacity) {
      bufferCapacity = capacity;
      items = new BoundCounterWithRaceConditionCheck[bufferCapacity];
      for (int i = 0; i < bufferCapacity; i++) {
        items[i] = new BoundCounterWithRaceConditionCheck(0, null, 0,
          null, 0, 10, "Ne moze da se zeme od prazen bafer.");
      }
    }

    public int getBufferCapacity() {
      return bufferCapacity;
    }

    public boolean isBufferEmpty() throws RuntimeException {
      log(raceConditionTester.incrementWithMax(), "checking buffer state");
      boolean empty = false;
      synchronized (this) {
        empty = (itemsLeft == 0);
      }
      log(raceConditionTester.decrementWithMin(), null);
      return empty;
    }

    public void getItem(int index) {
      counter.incrementWithMax(false);
      log(items[index].decrementWithMin(), "geting item");
      counter.decrementWithMin(false);
    }

    public void decrementNumberOfItemsLeft() {
      counter.incrementWithMax(false);
      synchronized (this) {
        itemsLeft--;
      }
      counter.decrementWithMin(false);
    }

    public void fillBuffer() {
      log(bufferFillCheck.incrementWithMax(), "filling buffer");
      if (isBufferEmpty()) {
        for (int i = 0; i < bufferCapacity; i++) {
          items[i].incrementWithMax();

        }
      } else {
        logException(new PointsException(10, "Filling non-empty buffer"));
      }
      synchronized (this) {
        itemsLeft = bufferCapacity;
      }
      log(bufferFillCheck.decrementWithMin(), null);
    }

    public void finalize() {
      if (counter.getMax() == 1) {
        logException(new PointsException(10,
          _10_KONZUMIRANJETO_NE_E_PARALELIZIRANO));
      }
    }
  }

  /**
   * Metod koj treba da gi inicijalizira vrednostite na semaforite i
   * ostanatite promenlivi za sinhronizacija.
   * <p>
   * TODO: da se implementira
   */
  public static void init() {
    bufferAccess = new Semaphore(0);
    emptyTable = new Semaphore(1);

  }

  public static void main(String[] args) {
    try {
      Scanner s = new Scanner(System.in);
      int brKonzumeri = s.nextInt();
      int numIterations = s.nextInt();
      s.close();

      HashSet<Thread> threads = new HashSet<Thread>();

      for (int i = 0; i < brKonzumeri; i++) {
        Consumer c = new Consumer(numIterations, i);
        threads.add(c);
      }
      Producer p = new Producer(numIterations);
      threads.add(p);

      state = new State(brKonzumeri);

      init();

      ProblemExecution.start(threads, state);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  // </editor-fold>
}
