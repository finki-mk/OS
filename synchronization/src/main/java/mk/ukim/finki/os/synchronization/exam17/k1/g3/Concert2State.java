package mk.ukim.finki.os.synchronization.exam17.k1.g3;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

public class Concert2State extends AbstractState {

  private static final String VOTE_SHOULD_CALLED_ONCE = "The vote() method should be called only once per performance.";
  private static final String GROUP_FORMING_NOT_PARALLEL = "The group forming is not in parallel!";
  private static final String INCOMPLETE_PERFORMANCE = "The previous performance is incomplete.";
  private static final String GROUPS_ARE_NOT_PRESENT = "Not all backing groups are present.";
  private static final String MAXIMUM_3_BARITONES = "Maximum 3 Baritones for performance are allowed.";
  private static final String MAXIMUM_3_TENORS = "Maximum 3 Tenors for performance are allowed.";
  private static final String MAXIMUM_1_PERFORMER = "Maximum 1 Performer for performance is allowed.";
  private static final int MAXIMUM_1_PERFORMER_POINTS = 5;
  private static final int MAXIMUM_3_TENORS_POINTS = 5;
  private static final int MAXIMUM_3_BARITONES_POINTS = 5;
  private static final int GROUPS_ARE_NOT_PRESENT_POINTS = 5;
  private static final int INCOMPLETE_PERFORMANCE_POINTS = 5;
  private static final int GROUP_FORMING_NOT_PARALLEL_POINTS = 5;
  private static final int VOTE_SHOULD_CALLED_ONCE_POINTS = 5;

  int numParticipants = 0;
  private BoundCounterWithRaceConditionCheck baritone;
  private BoundCounterWithRaceConditionCheck tenor;
  private BoundCounterWithRaceConditionCheck performer;
  private BoundCounterWithRaceConditionCheck performerHere;

  public Concert2State() {
    baritone = new BoundCounterWithRaceConditionCheck(0, 3,
      MAXIMUM_3_BARITONES_POINTS, MAXIMUM_3_BARITONES, null, 0, null);
    tenor = new BoundCounterWithRaceConditionCheck(0, 3,
      MAXIMUM_3_TENORS_POINTS, MAXIMUM_3_TENORS, null, 0, null);
    performer = new BoundCounterWithRaceConditionCheck(0, 1,
      MAXIMUM_1_PERFORMER_POINTS, MAXIMUM_1_PERFORMER, null, 0, null);
    performerHere = new BoundCounterWithRaceConditionCheck(0, 1,
      MAXIMUM_1_PERFORMER_POINTS, MAXIMUM_1_PERFORMER, null, 0, null);
  }

  public void performerHere() {

    Switcher.forceSwitch(3);
    if (!(getThread() instanceof Concert2.Performer)) {
      logException(new PointsException(5, "Not invoked by performer"));
    }
    performerHere.incrementWithMax();

  }

  public void formBackingVocals() {
    logException(performerHere.assertEquals(1, 5, "The performer is not here"));
    Switcher.forceSwitch(30);
    if (getThread() instanceof Concert2.Baritone) {
      log(baritone.incrementWithMax(false), "Baritone for backing group");
    } else if (getThread() instanceof Concert2.Tenor) {
      log(tenor.incrementWithMax(false), "Tenor for backing group");
    }
  }

  public void perform() {
    synchronized (this) {
      // first check
      if (numParticipants == 0) {
        if (baritone.getValue() == 3 && tenor.getValue() == 3) {
          baritone.setValue(0);
          tenor.setValue(0);
        } else {
          log(new PointsException(GROUPS_ARE_NOT_PRESENT_POINTS,
            GROUPS_ARE_NOT_PRESENT), null);
        }
      }
      numParticipants++;
    }
    Switcher.forceSwitch(30);
    if (getThread() instanceof Concert2.Baritone) {
      log(baritone.incrementWithMax(false), "Baritone performed");
    } else if (getThread() instanceof Concert2.Tenor) {
      log(tenor.incrementWithMax(false), "Tenor performed");
    } else {
      log(performer.incrementWithMax(false), "Performer performed");
    }
  }

  public void vote() {
    synchronized (this) {
      if (numParticipants == 7) {
        reset();
        log(null, "Voting started.");
      } else if (numParticipants != 0) {
        log(new PointsException(INCOMPLETE_PERFORMANCE_POINTS,
          INCOMPLETE_PERFORMANCE), null);
        reset();
      } else {
        log(new PointsException(VOTE_SHOULD_CALLED_ONCE_POINTS,
          VOTE_SHOULD_CALLED_ONCE), null);
      }
    }
  }

  private synchronized void reset() {
    baritone.setValue(0);
    tenor.setValue(0);
    performer.setValue(0);
    performerHere.setValue(0);
    numParticipants = 0;
  }

  @Override
  public synchronized void finalize() {
    if (baritone.getMax() == 1 && tenor.getMax() == 1) {
      logException(new PointsException(GROUP_FORMING_NOT_PARALLEL_POINTS,
        GROUP_FORMING_NOT_PARALLEL));
    }
  }

}
