package mk.ukim.finki.os.synchronization.problems;

import java.util.HashSet;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

/**
 *
 * @author ristes
 */
public class ProducerConsumer {

  public static int NUM_RUNS = 10;

  // TODO: definirajte gi semaforite i ostanatite promenlivi. Mora da se
  // static

  // semafor koj kazuva deka celiot bafer e prazen t.e.
  // deka sekoja od stavkite vo baferot e prazna. Sluzi za komunikacija
  // pomegju prodcer-ot i consumer-ite (za posledniot consumer da mu kaze na
  // producer-ot deka baferot e ispraznet).
  // koga baferot ke bide prazen (togas ima 1 permit) i togas producer-ot nema
  // da blokira.
  static Semaphore bufferEmpty;
  // Za sekoja stavka imame poseben semafor koj kazuva dali taa e postavena
  // (popolneta). Sekoj consumer so ovoj semafor ke proveri dali negovata
  // stavka e popolneta. Ako ne e, treba da ceka dodeka da se popolni, za da
  // ne se obide da zeme prazna stavka
  static Semaphore[] itemFilled;
  // Monitor koj ke se koristi za pristap do baferot. Sluzi za kontrola na
  // iskluciv pristap kon baferot, t.e. samo eden da moze da pravi promeni kaj
  // nego. MORA DA E FINAL!!!
  static final Object bufferAccessMonitor = new Object();

  /**
   * Metod koj treba da gi inicijalizira vrednostite na semaforite i
   * ostanatite promenlivi za sinhronizacija.
   *
   * TODO: da se implementira
   *
   */
  public static void init() {
    int brKonzumeri = state.getBufferCapacity();
    bufferEmpty = new Semaphore(1);
    // Inicijalno site stavki se prazni
    itemFilled = new Semaphore[brKonzumeri];
    for (int i = 0; i < brKonzumeri; i++) {
      itemFilled[i] = new Semaphore(0);
    }
  }

  static class Producer extends TemplateThread {

    public Producer(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {
      // ako baferot e prazen (imame permit), nema da blokira i ke
      // prodolzi so negovo polnenje (so metodot fillBuffer). Ako baferot
      // ne e prazen, ovde ke bide blokiran producer-ot i ke ceka se
      // dodeka nekoj consumer ne napravi release(), so sto ke kaze deka
      // baferot e prazen i treba povtorno da se popolni.
      bufferEmpty.acquire();
      // stom producerot e ovde, znaci deka baferot e prazen

      // baferot e spodelen resurs i negovoto polnenje ne treba da se
      // preklopi so nitu edna proverka ili promenana brojot na stavkite
      // vo nego (povici na isBufferEmpty i decrementNumberOfItemsLeft)
      synchronized (bufferAccessMonitor) {
        state.fillBuffer();
        // signaliziraj na consumer-ite deka baferot e napolnet
        for (int i = 0; i < itemFilled.length; i++) {
          itemFilled[i].release();
        }
      }
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
      // ceka da mu bide postavena negovata stavka. Ako ne e postavena,
      // ke ceka dodeka da mu se postavi.
      itemFilled[cId].acquire();
      // zemanje na stavkata od baferot. Treba da e nadvor od kriticen
      // region za da moze paralelno da se zemaat stavkite od strana
      // razlicni consumer-i.
      state.getItem(cId);

      // proverkata i modifikacijata na brojot na stavki vo baferot
      // pristapuvaat do spodeleni resursi i mora da se vo kriticen region
      synchronized (bufferAccessMonitor) {
        // kazuva deka ja zemal stavkata
        state.decrementNumberOfItemsLeft();
        // ako e posledniot koj ja zema stavkata
        if (state.isBufferEmpty()) {
          // kazi na producer-ot da napolni buffer
          bufferEmpty.release();
        }
      }
    }
  }

  // <editor-fold defaultstate="collapsed" desc="This is the template code" >
  static State state;

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

    public int getBufferCapacity() {
      return bufferCapacity;
    }

    private int itemsLeft = 0;

    public State(int capacity) {
      bufferCapacity = capacity;
      items = new BoundCounterWithRaceConditionCheck[bufferCapacity];
      for (int i = 0; i < bufferCapacity; i++) {
        items[i] = new BoundCounterWithRaceConditionCheck(0, null, 0,
          null, 0, 10, "Ne moze da se zeme od prazen bafer.");
      }
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
