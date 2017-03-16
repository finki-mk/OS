package mk.ukim.finki.os.synchronization.exam16.k1.g4;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

import java.util.HashSet;

public class TablanetState extends AbstractState {

  private static final int ALL_GROUP_HASENT_FINISH_POINTS = 7;
  private static final String ALL_GROUP_HASENT_FINISH = "Ne se zavrseni site grupi so igranje";

  private static final int THREADS_IN_PROCESS_POINTS = 7;
  private static final String THREADS_IN_PROCESS = "Site igraci ne zavrsile so igranje";

  private static final int PROCESS_NOT_PARALLEL_POINTS = 7;
  private static final String PROCESS_NOT_PARALLEL = "Procesot na igranje ne e paralelen";

  private static final int MAXIMUM_GROUP_THREADS_POINTS = 7;
  private static final String MAXIMUM_GROUP_THREADS = "Nema mesto na masata vo tekovnata grupa";

  private static final int NOT_ENOUGH_GROUP_THREADS_POINTS = 7;
  private static final String NOT_ENOUGH_GROUP_THREADS = "Nema dovolno igraci za da se sostavi grupa";

  public static final int DUPLICATE_THREAD_IN_CYCLE_POINTS = 7;
  public static final String DUPLICATE_THREAD_IN_CYCLE = "Igracot sednuva po vtor pat vo ist ciklus";

  public static final int THREADS_HASNT_FINISHED_THE_CYCLE_POINTS = 7;
  public static final String THREADS_HASNT_FINISHED_THE_CYCLE = "Ima igraci koi ne igrale vo ovoj ciklus";

  public static final int NO_CALL_TO_PRODUCER_POINTS = 7;
  public static final String NO_CALL_TO_PRODUCER = "Ne se podeleni kartite.";

  public static final String THREAD_READY = "Igrac sednuva";
  public static final String THREAD_IN_PROCESS = "Igracot zapocnuva so igra";
  public static final String GROUP_THREADS_FINISHED_PROCESS = "Tekovnata grupa zavrsi so igranje.";

  public static final String FINISHED_CYCLE = "Site grupi zavrsija so igranje vo tekovniot ciklus.";

  public static final int GROUP_SIZE = 4;
  public static final int TOTAL_THREADS = 20;

  private BoundCounterWithRaceConditionCheck threadsPrepared;
  private BoundCounterWithRaceConditionCheck threadsInProcess;
  private BoundCounterWithRaceConditionCheck threadsFinishedProcess;
  private BoundCounterWithRaceConditionCheck threadsFinishedRound;

  private HashSet preparedThreads = new HashSet();
  private boolean dealedCards = false;

  public TablanetState() {
    threadsPrepared = new BoundCounterWithRaceConditionCheck(
      0,
      GROUP_SIZE,
      MAXIMUM_GROUP_THREADS_POINTS,
      MAXIMUM_GROUP_THREADS,
      null,// NO MINIMUM CHECK
      0,
      null
    );

    threadsInProcess = new BoundCounterWithRaceConditionCheck(0);
    threadsFinishedProcess = new BoundCounterWithRaceConditionCheck(0);
    threadsFinishedRound = new BoundCounterWithRaceConditionCheck(0);
  }

  public void playerSeat() {
    synchronized (TablanetState.class) {
      Thread current = getThread();
      if (preparedThreads.contains(current.getId())) {
        throw new PointsException(
          DUPLICATE_THREAD_IN_CYCLE_POINTS,
          DUPLICATE_THREAD_IN_CYCLE
        );
      }
      preparedThreads.add(current.getId());
    }
    PointsException e = threadsPrepared.incrementWithMax(false);
    logException(e);
    log(e, THREAD_READY);
    Switcher.forceSwitch(5);
  }


  public void dealCards() {
    logException(
      threadsPrepared.assertEquals(
        GROUP_SIZE,
        NOT_ENOUGH_GROUP_THREADS_POINTS,
        NOT_ENOUGH_GROUP_THREADS
      )
    );
    Switcher.forceSwitch(3);
    synchronized (TablanetState.class) {
      dealedCards = true;
    }

  }

  public void play() {
    synchronized (TablanetState.class) {
      if (dealedCards == false) {
        logException(new PointsException(NO_CALL_TO_PRODUCER_POINTS, NO_CALL_TO_PRODUCER));
      }

    }
    log(threadsInProcess.incrementWithMax(false), THREAD_IN_PROCESS);
    Switcher.forceSwitch(10);
    log(threadsInProcess.decrementWithMin(false), null);
    threadsFinishedProcess.incrementWithMax(false);
    threadsFinishedRound.incrementWithMax(false);
  }


  public void nextGroup() {
    logException(
      threadsFinishedProcess.assertEquals(
        GROUP_SIZE,
        ALL_GROUP_HASENT_FINISH_POINTS,
        ALL_GROUP_HASENT_FINISH
      )
    );
    logException(
      threadsInProcess.assertEquals(
        0,
        THREADS_IN_PROCESS_POINTS,
        THREADS_IN_PROCESS
      )
    );
    log(null, GROUP_THREADS_FINISHED_PROCESS);
    synchronized (TablanetState.class) {
      // reset round
      threadsPrepared.setValue(0);
      threadsFinishedProcess.setValue(0);
      dealedCards = false;
    }
    Switcher.forceSwitch(3);
  }

  public void endCycle() {
    logException(
      threadsFinishedRound.assertEquals(
        TOTAL_THREADS,
        THREADS_HASNT_FINISHED_THE_CYCLE_POINTS,
        THREADS_HASNT_FINISHED_THE_CYCLE
      )
    );
    log(null, FINISHED_CYCLE);
    synchronized (TablanetState.class) {
      threadsFinishedRound.setValue(0);
      preparedThreads.clear();
    }
  }

  @Override
  public void finalize() {
    if (threadsInProcess.getMax() == 1) {
      logException(
        new PointsException(
          PROCESS_NOT_PARALLEL_POINTS,
          PROCESS_NOT_PARALLEL
        )
      );
    }

  }


}