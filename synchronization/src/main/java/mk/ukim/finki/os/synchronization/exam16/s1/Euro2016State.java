package mk.ukim.finki.os.synchronization.exam16.s1;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

import java.util.HashSet;

public class Euro2016State extends AbstractState {

  private static final int MAXIMUM_4_FANS_POINTS = 10;
  private static final int RACE_CONDITION_POINTS = 25;
  private static final int NOT_ALL_COMBINATIONS_INCLUDED_POINTS = 10;
  private static final int INVALID_COMBINATION_POINTS = 25;

  private static final String MAXIMUM_4_FANS_MESSAGE = "Vo taksito ima poveke od cetvorica.";
  private static final String MORE_THEN_ONE_CALL_OF_DEPARTURE = "Poveke od 1 patnik ja povikuva departure()";
  private static final String NOT_ALL_COMBINATIONS_INCLUDED_MESSAGE = "Ne gi dozvoluvate site kombinacii za kacuvanje.";
  private static final String CALL_OF_DEPARTURE = "Ja povikuvam departure()";

  private BoundCounterWithRaceConditionCheck Boat;

  private HashSet<String> combinations;

  private int fansA = 0;
  private int fansB = 0;
  private long checkRaceConditionInt;

  public Euro2016State() {
    combinations = new HashSet<String>();
    Boat = new BoundCounterWithRaceConditionCheck(0, 4,
      MAXIMUM_4_FANS_POINTS,
      MAXIMUM_4_FANS_MESSAGE, null, 0, null);
    checkRaceConditionInt = Long.MIN_VALUE;
  }

  public synchronized void board() {

    log(Boat.incrementWithMax(false), "se kacuvam vo taksi");
    Thread t = Thread.currentThread();
    if (t instanceof Euro2016.FanA) {
      fansA++;
    } else {
      fansB++;
    }
  }

  public void departure() {
    checkRaceConditionInt++;
    if (fansA + fansB == 4 && fansA % 2 == 0 && fansB % 2 == 0) {
      combinations.add(encode());

      RaceConditionMethod();

      log(null, CALL_OF_DEPARTURE);

      fansA = 0;
      fansB = 0;
      Boat.setValue(0);
    } else {
      logException(new PointsException(INVALID_COMBINATION_POINTS,
        InvalidCombinationMessage()));
    }
  }

  private String InvalidCombinationMessage() {
    return "Nevalidna kombinacija na fansA so fansB. FansA number : "
      + fansA + " FansB number : " + fansB;
  }

  private void RaceConditionMethod() {
    long check;
    synchronized (this) {
      check = checkRaceConditionInt;
    }
    Switcher.forceSwitch(3);

    if (check != checkRaceConditionInt) {
      logException(new PointsException(RACE_CONDITION_POINTS,
        MORE_THEN_ONE_CALL_OF_DEPARTURE));
    }
  }

  public String encode() {
    return fansA + " " + fansB;
  }

  @Override
  public void finalize() {

    if (combinations.size() != 3) {
      logException(new PointsException(
        NOT_ALL_COMBINATIONS_INCLUDED_POINTS,
        NOT_ALL_COMBINATIONS_INCLUDED_MESSAGE));
    }

  }
}
