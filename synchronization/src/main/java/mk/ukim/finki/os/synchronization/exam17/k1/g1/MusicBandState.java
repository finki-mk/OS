package mk.ukim.finki.os.synchronization.exam17.k1.g1;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

public class MusicBandState extends AbstractState {

  private static final String PLAYING_NOT_PARALLEL = "The playing is not in parallel!";
  private static final String GROUP_NOT_FORMED = "The previous group is not formed";
  private static final String MAXIMUM_3_GUITAR_PLAYERS = "Maximum 3 Guitar Players playing is allowed.";
  private static final String MAXIMUM_2_SINGER = "Maximum 2 Singers playing is allowed.";
  private static final int MAXIMUM_2_SINGER_POINTS = 5;
  private static final int MAXIMUM_3_GUITAR_PLAYERS_POINTS = 5;
  private static final int GROUP_NOT_FORMED_POINTS = 5;
  private static final int PLAYING_NOT_PARALLEL_POINTS = 5;

  int groupMembersCount = 0;
  private BoundCounterWithRaceConditionCheck guitarPlayer;
  private BoundCounterWithRaceConditionCheck singer;

  public MusicBandState() {
    guitarPlayer = new BoundCounterWithRaceConditionCheck(0, 3,
      MAXIMUM_3_GUITAR_PLAYERS_POINTS, MAXIMUM_3_GUITAR_PLAYERS, null, 0, null);
    singer = new BoundCounterWithRaceConditionCheck(0, 2,
      MAXIMUM_2_SINGER_POINTS, MAXIMUM_2_SINGER, null, 0, null);
  }

  public void play() {
    synchronized (this) {
      groupMembersCount++;
    }
    if (getThread() instanceof MusicBand.GuitarPlayer) {
      log(guitarPlayer.incrementWithMax(false), "Guitar Player is playing");
    } else if (getThread() instanceof MusicBand.Singer) {
      log(singer.incrementWithMax(false), "Singer is playing");
    }
    Switcher.forceSwitch(3);
    if(guitarPlayer.getValue()!=3 || singer.getValue()!=2 ) {
//      log(new PointsException(5,"Ne se prisutni site"),"");
    }

  }

  public void evaluate() {
    synchronized (this) {
      if (groupMembersCount == 5) {
        if (guitarPlayer.getValue() == 3 && singer.getValue() == 2) {
          reset();
          log(null, "The group has performed.");
        } else {
          log(new PointsException(
            GROUP_NOT_FORMED_POINTS,
            GROUP_NOT_FORMED), null);

        }
      }
    }
  }

  private synchronized void reset() {
    guitarPlayer.setValue(0);
    singer.setValue(0);
    groupMembersCount = 0;
  }

  @Override
  public synchronized void finalize() {
    if (guitarPlayer.getMax() == 1 && singer.getMax() == 1) {
      logException(new PointsException(PLAYING_NOT_PARALLEL_POINTS,
        PLAYING_NOT_PARALLEL));
    }
  }

}
