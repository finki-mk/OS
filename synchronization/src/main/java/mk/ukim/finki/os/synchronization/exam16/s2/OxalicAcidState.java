package mk.ukim.finki.os.synchronization.exam16.s2;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

public class OxalicAcidState extends AbstractState {

  private static final String DONE_SHOULD_CALLED_ONCE = "The validate() method should be called only once per molecule.";
  private static final String BONDING_NOT_PARALLEL = "The bonding is not executed in parallel!";
  private static final String MOLECULE_NOT_BOUNDED_COMPLETELY = "The previous molecule is not bonded completely.";
  private static final String MAXIMUM_4_OXYGEN = "Maximum 4 Oxygen atoms for bonding are allowed.";
  private static final String MAXIMUM_2_HYDROGEN = "Maximum 2 Hydrogen atoms for bonding are allowed.";
  private static final String MAXIMUM_2_CARBON = "Maximum 2 Carbon atom for bonding is allowed.";
  private static final int MAXIMUM_2_CARBON_POINTS = 5;
  private static final int MAXIMUM_2_HYDROGEN_POINTS = 5;
  private static final int MAXIMUM_4_OXYGEN_POINTS = 5;
  private static final int MOLECULE_NOT_BOUNDED_COMPLETELY_POINTS = 10;
  private static final int OH_BONDING_NOT_PARALLEL_POINTS = 5;
  private static final int DONE_SHOULD_CALLED_ONCE_POINTS = 5;

  int numAtoms = 0;
  private BoundCounterWithRaceConditionCheck O;
  private BoundCounterWithRaceConditionCheck H;
  private BoundCounterWithRaceConditionCheck C;

  public OxalicAcidState() {
    O = new BoundCounterWithRaceConditionCheck(0, 4,
      MAXIMUM_4_OXYGEN_POINTS, MAXIMUM_4_OXYGEN, null, 0, null);
    H = new BoundCounterWithRaceConditionCheck(0, 2,
      MAXIMUM_2_HYDROGEN_POINTS, MAXIMUM_2_HYDROGEN, null, 0, null);
    C = new BoundCounterWithRaceConditionCheck(0, 2,
      MAXIMUM_2_CARBON_POINTS, MAXIMUM_2_CARBON, null, 0, null);
  }

  public void bond() {
    synchronized (this) {
      numAtoms++;
    }

    Switcher.forceSwitch(3);
    if (getThread() instanceof OxalicAcid.Oxygen) {
      log(O.incrementWithMax(false), "Oxygen here");
    } else if (getThread() instanceof OxalicAcid.Hydrogen) {
      log(H.incrementWithMax(false), "Hydrogen here");
    } else if (getThread() instanceof OxalicAcid.Carbon) {
      log(C.incrementWithMax(false), "Carbon here");
    }

  }

  public void validate() {
    synchronized (this) {
      if (O.getValue() == 4 && C.getValue() == 2 && H.getValue() == 2) {
        reset();
        log(null, "Molecule is formed.");
      } else if (numAtoms != 0) {
        log(new PointsException(MOLECULE_NOT_BOUNDED_COMPLETELY_POINTS,
          MOLECULE_NOT_BOUNDED_COMPLETELY), null);
        reset();
      } else {
        log(new PointsException(DONE_SHOULD_CALLED_ONCE_POINTS,
          DONE_SHOULD_CALLED_ONCE), null);
      }
    }
  }

  private synchronized void reset() {
    O.setValue(0);
    H.setValue(0);
    C.setValue(0);
    numAtoms = 0;
  }

  @Override
  public synchronized void finalize() {
    if (O.getMax() == 1 && H.getMax() == 1) {
      logException(new PointsException(OH_BONDING_NOT_PARALLEL_POINTS,
        BONDING_NOT_PARALLEL));
    }
  }

}
