package mk.ukim.finki.os.synchronization.exam14.march.ca3n2;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;
import mk.ukim.finki.os.synchronization.exam14.march.ca3n2.CalciumNitride.Calcium;
import mk.ukim.finki.os.synchronization.exam14.march.ca3n2.CalciumNitride.Nitrogen;

public class CalciumNitrideState extends AbstractState {

	private static final String BONDING_NOT_PARALLEL = "The bonding is not in parallel!";
	private static final String MOLECULE_NOT_BOUNDED_COMPLITELY = "The previous molecule is not bounded complitely";
	private static final String MAXIMUM_3_CALCIUM = "Maximum 3 Calcium atoms for bonding are allowed.";
	private static final String MAXIMUM_2_NITROGEN = "Maximum 2 Nitrogen atoms for bonding are allowed.";
	private static final int MAXIMUM_2_NITROGEN_POINTS = 5;
	private static final int MAXIMUM_3_CALCIUM_POINTS = 5;
	private static final int MOLECULE_NOT_BOUNDED_COMPLITELY_POINTS = 10;
	private static final int BONDING_NOT_PARALLEL_POINTS = 5;

	int numAtoms = 0;
	private BoundCounterWithRaceConditionCheck Ca;
	private BoundCounterWithRaceConditionCheck N;

	public CalciumNitrideState() {
		Ca = new BoundCounterWithRaceConditionCheck(0, 3,
				MAXIMUM_3_CALCIUM_POINTS, MAXIMUM_3_CALCIUM, null, 0, null);
		N = new BoundCounterWithRaceConditionCheck(0, 3,
				MAXIMUM_2_NITROGEN_POINTS, MAXIMUM_2_NITROGEN, null, 0, null);
	}

	public void bond() {
		synchronized (this) {
			numAtoms++;
		}
		Switcher.forceSwitch(3);
		if (getThread() instanceof Calcium) {
			log(Ca.incrementWithMax(false), "Calcium bonding");
		} else if (getThread() instanceof Nitrogen) {
			log(N.incrementWithMax(false), "Nitrogen bonding");
		}
	}

	public void validate() {
		synchronized (this) {
			if (numAtoms == 5) {
				if (Ca.getValue() == 3 && N.getValue() == 2) {
					reset();
					log(null, "Ca3N3 molecule is formed.");
				} else {
					log(new PointsException(
							MOLECULE_NOT_BOUNDED_COMPLITELY_POINTS,
							MOLECULE_NOT_BOUNDED_COMPLITELY), null);

				}
			}
		}
	}

	private synchronized void reset() {
		Ca.setValue(0);
		N.setValue(0);
		numAtoms = 0;
	}

	@Override
	public synchronized void finalize() {
		if (Ca.getMax() == 1 && N.getMax() == 1) {
			logException(new PointsException(BONDING_NOT_PARALLEL_POINTS,
					BONDING_NOT_PARALLEL));
		}
	}

}
