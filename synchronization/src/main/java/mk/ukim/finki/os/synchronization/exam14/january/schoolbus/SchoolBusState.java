package mk.ukim.finki.os.synchronization.exam14.january.schoolbus;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

/**
 * 
 * @author ristes
 * 
 */

public class SchoolBusState extends AbstractState {

	private static final String BUS_ARRIVED = "Bus arrived.";
	private static final String STUDENTS_PRESENT_WHEN_DRIVER_LEAVING = "There are students in the bus in the moment when the driver is leaving.";
	private static final String BUS_NOT_ARRIVED_WHEN_STUDENT_LEAVING = "The exam is not ended when the student leaves the classroom.";
	private static final String STUDENT_LEAVING_CLASSROOM = "Student is leaving the classroom. ";
	private static final String BUS_NOT_DEPARTURED = "The bus haven't departured, and thus can not arrive.";
	private static final String BUS_IS_DEPARTURING = "The bus is departuring.";
	private static final String DRIVER_ENTERING_IN_BUS = "Driver entering in the bus.";
	private static final String STUDENT_BEFORE_DRIVER = "There is a student in the bas before the driver.";
	private static final String STUDENT_ENTERING_IN_BUS = "Student entering in the bus";
	private static final String MAXIMUM_50_STUDENTS = "Maximum 50 students in the bus are allowed";
	private static final String MAXIMUM_1_DRIVER = "Maximum 1 driver in the bus is allowed.";
	private static final String STUDENT_BEFORE_DRIVER_ENTER = "The student can not enter before the driver.";
	private static final String SECOND_DRIVER_IN_BUS = "The driver can not enter. There is other driver in the bus.";
	private static final String BUS_DEPARTURED_WITH_LESS_THAN_50_STUDENTS = "The bus can not departure with less than 50 students.";
	private static final String STUDENT_LEAVE_AFTER_DRIVER = "There is no exactly 1 driver in the bus when the student is leaving.";
	private static final String BUS_ENTERING_NOT_PARALEL = "The students are entering one by one, but they should do it together. ";

	private static final int STUDENTS_PRESENT_WHEN_DRIVER_LEAVING_POINTS = 5;
	private static final int BUS_NOT_DEPARTURED_POINTS = 5;
	private static final int STUDENT_BEFORE_DRIVER_POINTS = 5;
	private static final int MAXIMUM_50_STUDENTS_POINTS = 5;
	private static final int MAXIMUM_1_DRIVER_POINTS = 5;
	private static final int STUDENT_ENTER_POINTS = 5;
	private static final int SECOND_DRIVER_IN_BUS_POINTS = 5;
	private static final int EXAM_START_WITH_LESS_THAN_50_STUDENTS_POINTS = 5;
	private static final int BUS_NOT_ARRIVED_WHEN_STUDENT_LEAVING_POINTS = 5;
	private static final int STUDENT_LEAVE_AFTER_PROFESOR_POINTS = 5;
	private static final int CLASSROOM_ENTERING_NOT_PARALEL_POINTS = 5;

	private BoundCounterWithRaceConditionCheck drivers;
	private BoundCounterWithRaceConditionCheck students;

	private boolean busDepartured = false;
	private boolean busArrived = false;

	public SchoolBusState() {
		drivers = new BoundCounterWithRaceConditionCheck(0, 1,
				MAXIMUM_1_DRIVER_POINTS, MAXIMUM_1_DRIVER, null, 0, null);
		students = new BoundCounterWithRaceConditionCheck(0, 50,
				MAXIMUM_50_STUDENTS_POINTS, MAXIMUM_50_STUDENTS, null, 0, null);

	}

	public void driverEnter() {
		log(drivers.assertEquals(0, SECOND_DRIVER_IN_BUS_POINTS,
				SECOND_DRIVER_IN_BUS), null);
		log(students.assertEquals(0, STUDENT_BEFORE_DRIVER_POINTS,
				STUDENT_BEFORE_DRIVER), null);
		log(drivers.incrementWithMax(), DRIVER_ENTERING_IN_BUS);
	}

	public void studentEnter() {
		log(drivers.assertEquals(1, STUDENT_ENTER_POINTS,
				STUDENT_BEFORE_DRIVER_ENTER), null);
		log(students.incrementWithMax(false), STUDENT_ENTERING_IN_BUS);
		Switcher.forceSwitch(5);
	}

	public void busDeparture() {
		log(students.assertEquals(50,
				EXAM_START_WITH_LESS_THAN_50_STUDENTS_POINTS,
				BUS_DEPARTURED_WITH_LESS_THAN_50_STUDENTS), null);
		log(drivers.assertEquals(1, MAXIMUM_1_DRIVER_POINTS, MAXIMUM_1_DRIVER),
				BUS_IS_DEPARTURING);

		busDepartured = true;
		Switcher.forceSwitch(5);
	}

	public void busArrive() {
		synchronized (this) {
			if (!busDepartured) {
				log(new PointsException(BUS_NOT_DEPARTURED_POINTS,
						BUS_NOT_DEPARTURED), null);
			}
			busDepartured = false;
			busArrived = true;
		}
		log(null, BUS_ARRIVED);
		Switcher.forceSwitch(5);

	}

	public void studentLeave() {
		synchronized (this) {
			if (!busArrived) {
				log(new PointsException(
						BUS_NOT_ARRIVED_WHEN_STUDENT_LEAVING_POINTS,
						BUS_NOT_ARRIVED_WHEN_STUDENT_LEAVING), null);
			}
		}
		log(drivers.assertEquals(1, STUDENT_LEAVE_AFTER_PROFESOR_POINTS,
				STUDENT_LEAVE_AFTER_DRIVER), null);
		log(students.decrementWithMin(false), STUDENT_LEAVING_CLASSROOM);
		Switcher.forceSwitch(5);
	}

	public void driverLeave() {
		students.assertEquals(0, STUDENTS_PRESENT_WHEN_DRIVER_LEAVING_POINTS,
				STUDENTS_PRESENT_WHEN_DRIVER_LEAVING);
		synchronized (this) {
			busArrived = false;
		}
		drivers.decrementWithMin();
	}

	@Override
	public void finalize() {
		if (students.getMax() == 1) {
			logException(new PointsException(
					CLASSROOM_ENTERING_NOT_PARALEL_POINTS,
					BUS_ENTERING_NOT_PARALEL));
		}

	}

}
