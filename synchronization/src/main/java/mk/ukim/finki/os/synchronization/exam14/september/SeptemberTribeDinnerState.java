package mk.ukim.finki.os.synchronization.exam14.september;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

public class SeptemberTribeDinnerState extends AbstractState {

	private static final int EMPTY_POT = 0;

	private static final String _10_JADENJETO_NE_E_PARALELIZIRANO = "jadenjeto ne e paralelizirano. Site jadat eden po eden";

	private static final String _10_DVAJCA_ISTOVREMENO_PROVERUVAAT = "Dvajca istovremeno proveruvaat dali kazanot e prazen. Maksimum eden e dozvoleno.";
	private static final String _7_DVAJCA_ISTOVREMENO_POLNAT = "Dvajca istovremeno zemaat hrana od kazanot. Maksimum eden e dozvoleno.";
	private static final String _5_NE_MOZE_DA_POLNI_OD_PRAZEN_KAZAN = "Ne moze da se polni od kazan. Treba da se povika 'state.cook()'";
	private static final String _5_NE_MOZE_DA_SE_GOTVI_VO_KAZAN_KOJ_NE_E_PRAZEN = "Ne moze da se gotvi vo kazan koj ne e prazen";
	private static final String _7_NEMA_MESTO_NA_TRPEZATA = "Trpezata e polna. Nema mesto na trpezata za poveke od cetvorica. ";
	private static final String _7_NEMA_ZEMENO_HRANA = "NEMA ZEMENO HRANA";
	private static final String _7_POLNI_OD_PRAZEN_KAZAN = "POLNI OD PRAZEN KAZAN";

	private static final int POT_CAPACITY = 15;

	private BoundCounterWithRaceConditionCheck platesLeft = new BoundCounterWithRaceConditionCheck(
			0, null, 0, null, 0, 10, _7_POLNI_OD_PRAZEN_KAZAN);

	private BoundCounterWithRaceConditionCheck checks = new BoundCounterWithRaceConditionCheck(
			0, 1, 10, _10_DVAJCA_ISTOVREMENO_PROVERUVAAT, null, 0, null);

	private BoundCounterWithRaceConditionCheck fills = new BoundCounterWithRaceConditionCheck(
			0, 1, 7, _7_DVAJCA_ISTOVREMENO_POLNAT, 0, 5,
			_5_NE_MOZE_DA_POLNI_OD_PRAZEN_KAZAN);

	private BoundCounterWithRaceConditionCheck ready = new BoundCounterWithRaceConditionCheck(
			0, null, 0, null, 0, 7, _7_NEMA_ZEMENO_HRANA);

	private BoundCounterWithRaceConditionCheck eat = new BoundCounterWithRaceConditionCheck(
			0, 4, 7, _7_NEMA_MESTO_NA_TRPEZATA, null, 0, null);

	public SeptemberTribeDinnerState() {

	}

	/**
	 * Maksimum 1 proveruva.
	 * 
	 * @return
	 * @throws RuntimeException
	 */
	public boolean isPotEmpty() throws RuntimeException {
		log(checks.incrementWithMax(), "proverka dali ima hrana vo kazanot");
		boolean res = platesLeft.getValue() == 0;
		log(checks.decrementWithMin(), null);
		return res;
	}

	/**
	 * Maksimum 1 zema paralelno. Ne smee da se povika od prazen kazan.
	 * 
	 * @throws RuntimeException
	 */
	public void fillPlate() throws RuntimeException {
		log(fills.incrementWithMax(), "zemanje na hrana");
		log(platesLeft.decrementWithMin(false), null);
		log(fills.decrementWithMin(), null);
		ready.incrementWithMax(false);
	}

	/**
	 * Maksimum 4 jadat paralelno. Ne smeat da jadat eden po eden.
	 * 
	 * @throws RuntimeException
	 */
	public void eat() throws RuntimeException {
		log(ready.decrementWithMin(false), null);
		log(eat.incrementWithMax(false), "jadenje");
		Switcher.forceSwitch(15);
		log(eat.decrementWithMin(false), null);

	}

	/**
	 * Se povikuva od gotvacot. Ne smee da se povika koga kazanot ne e prazen.
	 * 
	 * @throws RuntimeException
	 */
	public void cook() throws RuntimeException {
		log(platesLeft.assertEquals(EMPTY_POT, 5,
				_5_NE_MOZE_DA_SE_GOTVI_VO_KAZAN_KOJ_NE_E_PRAZEN), null);
		Switcher.forceSwitch(10);
		platesLeft.setValue(POT_CAPACITY);
	}

	@Override
	public void finalize() {
		if (eat.getMax() == 1) {
			logException(new PointsException(10,
					_10_JADENJETO_NE_E_PARALELIZIRANO));
		}
	}

}
