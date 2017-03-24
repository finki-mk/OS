package mk.ukim.finki.os.synchronization.exam16.s3;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

/**
 * H4 P2 O6
 */
public class HypophosphoricAcidState extends AbstractState {

  public static final int H_ATOMS = 4;
  public static final int P_ATOMS = 2;
  public static final int O_ATOMS = 6;

  private static final String DONE_SHOULD_CALLED_ONCE = "The validate() method should be called only once per molecule.";
  private static final String BONDING_NOT_PARALLEL = "The bonding is not executed in parallel!";
  private static final String MOLECULE_NOT_BOUNDED_COMPLETELY = "The previous molecule is not bonded completely.";
  private static final String MAXIMUM_OXYGEN = "Maximum " + O_ATOMS + " Oxygen atoms for bonding are allowed.";
  private static final String MAXIMUM_HYDROGEN = "Maximum " + H_ATOMS + "Hydrogen atoms for bonding are allowed.";
  private static final String MAXIMUM_PHOSPHORUS = "Maximum " + P_ATOMS + " Phosphorus atoms for bonding is allowed.";
  private static final int MAXIMUM_2_PHOSPHORUS_POINTS = 5;
  private static final int MAXIMUM_2_HYDROGEN_POINTS = 5;
  private static final int MAXIMUM_4_OXYGEN_POINTS = 5;
  private static final int MOLECULE_NOT_BOUNDED_COMPLETELY_POINTS = 10;
  private static final int BONDING_NOT_PARALLEL_POINTS = 5;
  private static final int DONE_SHOULD_CALLED_ONCE_POINTS = 5;


  int numAtoms = 0;
  private BoundCounterWithRaceConditionCheck O;
  private BoundCounterWithRaceConditionCheck H;
  private BoundCounterWithRaceConditionCheck P;

  public HypophosphoricAcidState() {
    O = new BoundCounterWithRaceConditionCheck(0, O_ATOMS,
      MAXIMUM_4_OXYGEN_POINTS, MAXIMUM_OXYGEN, null, 0, null);
    H = new BoundCounterWithRaceConditionCheck(0, H_ATOMS,
      MAXIMUM_2_HYDROGEN_POINTS, MAXIMUM_HYDROGEN, null, 0, null);
    P = new BoundCounterWithRaceConditionCheck(0, P_ATOMS,
      MAXIMUM_2_PHOSPHORUS_POINTS, MAXIMUM_PHOSPHORUS, null, 0, null);
  }

  public void bond() {
    synchronized (this) {
      numAtoms++;
    }

    Switcher.forceSwitch(3);
    if (getThread() instanceof HypophosphoricAcid.Oxygen) {
      log(O.incrementWithMax(false), "Oxygen here");
    } else if (getThread() instanceof HypophosphoricAcid.Hydrogen) {
      log(H.incrementWithMax(false), "Hydrogen here");
    } else if (getThread() instanceof HypophosphoricAcid.Phosphorus) {
      log(P.incrementWithMax(false), "Phosphorus here");
    }

  }

  public void validate() {
    synchronized (this) {
      if (O.getValue() == O_ATOMS && P.getValue() == P_ATOMS && H.getValue() == H_ATOMS) {
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
    P.setValue(0);
    numAtoms = 0;
  }

  @Override
  public synchronized void finalize() {
    if (O.getMax() == 1 && H.getMax() == 1 && P.getMax() == 1) {
      logException(new PointsException(BONDING_NOT_PARALLEL_POINTS,
        BONDING_NOT_PARALLEL));
    }
  }

}
