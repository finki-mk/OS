package mk.ukim.finki.os.synchronization.exam14.march.aloh3;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;
import mk.ukim.finki.os.synchronization.exam14.march.aloh3.AluminiumHydroxide.Hydrogen;
import mk.ukim.finki.os.synchronization.exam14.march.aloh3.AluminiumHydroxide.Oxygen;

public class AluminiumHydroxideState extends AbstractState {

	private static final String DONE_SHOULD_CALLED_ONCE = "The validate() method should be called only once per molecule.";
	private static final String OH_BONDING_NOT_PARALLEL = "The OH bonding is not in parallel!";
	private static final String MOLECULE_NOT_BOUNDED_COMPLITELY = "The previous molecule is not bonded completely.";
	private static final String OH_3_GROUP_IS_NOT_PRESENT = "(OH)3 group is not present.";
	private static final String MAXIMUM_3_OXYGEN = "Maximum 3 Oxygen atoms for bonding are allowed.";
	private static final String MAXIMUM_3_HYDROGEN = "Maximum 3 TribeMember atoms for bonding are allowed.";
	private static final String MAXIMUM_1_ALUMINIUM = "Maximum 1 Aluminium atom for bonding is allowed.";
	private static final int MAXIMUM_1_ALUMINIUM_POINTS = 5;
	private static final int MAXIMUM_3_HYDROGEN_POINTS = 5;
	private static final int MAXIMUM_3_OXYGEN_POINTS = 5;
	private static final int OH_3_GROUP_IS_NOT_PRESENT_PONTS = 5;
	private static final int MOLECULE_NOT_BOUNDED_COMPLITELY_POINTS = 10;
	private static final int OH_BONDING_NOT_PARALLEL_POINTS = 5;
	private static final int DONE_SHOULD_CALLED_ONCE_POINTS = 5;

	int numAtoms = 0;
	private BoundCounterWithRaceConditionCheck O;
	private BoundCounterWithRaceConditionCheck H;
	private BoundCounterWithRaceConditionCheck Al;

	public AluminiumHydroxideState() {
		O = new BoundCounterWithRaceConditionCheck(0, 3,
				MAXIMUM_3_OXYGEN_POINTS, MAXIMUM_3_OXYGEN, null, 0, null);
		H = new BoundCounterWithRaceConditionCheck(0, 3,
				MAXIMUM_3_HYDROGEN_POINTS, MAXIMUM_3_HYDROGEN, null, 0, null);
		Al = new BoundCounterWithRaceConditionCheck(0, 1,
				MAXIMUM_1_ALUMINIUM_POINTS, MAXIMUM_1_ALUMINIUM, null, 0, null);
	}

	public void bondOH() {

		Switcher.forceSwitch(3);
		if (getThread() instanceof Oxygen) {
			log(O.incrementWithMax(false), "Oxygen for OH group");
		} else if (getThread() instanceof Hydrogen) {
			log(H.incrementWithMax(false), "TribeMember for OH group");
		}
	}

	public void bondAlOH3() {
		synchronized (this) {
			// first check
			if (numAtoms == 0) {
				if (O.getValue() == 3 && H.getValue() == 3) {
					O.setValue(0);
					H.setValue(0);
				} else {
					log(new PointsException(OH_3_GROUP_IS_NOT_PRESENT_PONTS,
							OH_3_GROUP_IS_NOT_PRESENT), null);
				}
			}
			numAtoms++;
		}
		Switcher.forceSwitch(3);
		if (getThread() instanceof Oxygen) {
			log(O.incrementWithMax(false), "Oxygen for Al(OH)3");
		} else if (getThread() instanceof Hydrogen) {
			log(H.incrementWithMax(false), "TribeMember for Al(OH)3");
		} else {
			log(Al.incrementWithMax(false), "Aluminium for Al(OH)3");
		}
	}

	public void validate	() {
		synchronized (this) {
			if (numAtoms == 7) {
				reset();
				log(null, "Al(OH)3 molecule is formed.");
			} else if (numAtoms != 0) {
				log(new PointsException(MOLECULE_NOT_BOUNDED_COMPLITELY_POINTS,
						MOLECULE_NOT_BOUNDED_COMPLITELY), null);
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
		Al.setValue(0);
		numAtoms = 0;
	}

	@Override
	public synchronized void finalize() {
		if (O.getMax() == 1 && H.getMax() == 1) {
			logException(new PointsException(OH_BONDING_NOT_PARALLEL_POINTS,
					OH_BONDING_NOT_PARALLEL));
		}
	}

}
