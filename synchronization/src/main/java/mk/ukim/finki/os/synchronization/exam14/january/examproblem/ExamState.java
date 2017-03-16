package mk.ukim.finki.os.synchronization.exam14.january.examproblem;

import mk.ukim.finki.os.synchronization.AbstractState;
import mk.ukim.finki.os.synchronization.BoundCounterWithRaceConditionCheck;
import mk.ukim.finki.os.synchronization.PointsException;
import mk.ukim.finki.os.synchronization.Switcher;

/**
 * 
 * @author ristes
 * 
 */

public class ExamState extends AbstractState {

	private static final String STUDENTS_PRESENT_WHEN_TEACHER_LEAVING = "There are students in the room in the moment when the teacher is leaving.";
	private static final String EXAM_NOT_ENDED_WHEN_STUDENT_LEAVING = "The exam is not ended when the student leaves the classroom.";
	private static final String STUDENT_LEAVING_CLASSROOM = "Student is leaving the classroom. ";
	private static final String EXAM_NOT_STARTED = "The exam is not started, and thus can not end.";
	private static final String STARTING_THE_EXAM_DISTRIBUTING_THE_TESTS = "Starting the exam. Distributing the tests.";
	private static final String TEACHER_ENTERING_IN_CLASSROOM = "Teacher entering in classroom";
	private static final String STUDENT_BEFORE_TEACHER = "There is a student in the classroom before the teacher.";
	private static final String STUDENT_ENTERING_CLASSROOM = "Student entering classroom";
	private static final String MAXIMUM_50_STUDENTS = "Maximum 50 students in the classroom are allowed";
	private static final String MAXIMUM_1_TEACHER = "Maximum 1 teacher in the classroom is allowed.";
	private static final String STUDENT_BEFORE_TEACHER_ENTER = "The student can not enter before the teacher.";
	private static final String SECOND_TEACHER_IN_CLASSROOM = "The teacher can not enter. There is other teacher in the classroom.";
	private static final String EXAM_START_WITH_LESS_THAN_50_STUDENTS = "The exam can not start with less than 50 students in the classroom.";
	private static final String STUDENT_LEAVE_AFTER_TEACHER = "There is no exactly 1 profesor in the classroom when the student is leaving.";
	private static final String CLASSROOM_ENTERING_NOT_PARALEL = "The students are entering one by one, but they should do it together. ";

	private static final int STUDENTS_PRESENT_WHEN_TEACHER_LEAVING_POINTS = 5;
	private static final int EXAM_NOT_ENDED_WHEN_STUDENT_LEAVING_POINTS = 5;
	private static final int EXAM_NOT_STARTED_POINTS = 5;
	private static final int STUDENT_BEFORE_TEACHER_POINTS = 5;
	private static final int MAXIMUM_50_STUDENTS_POINTS = 5;
	private static final int MAXIMUM_1_TEACHER_POINTS = 5;
	private static final int STUDENT_ENTER_POINTS = 5;
	private static final int SECOND_TEACHER_IN_CLASSROOM_POINTS = 5;
	private static final int EXAM_START_WITH_LESS_THAN_50_STUDENTS_POINTS = 5;
	private static final int STUDENT_LEAVE_AFTER_PROFESOR_POINTS = 5;
	private static final int CLASSROOM_ENTERING_NOT_PARALEL_POINTS = 5;

	private BoundCounterWithRaceConditionCheck teachers;
	private BoundCounterWithRaceConditionCheck students;

	private boolean examStarted = false;
	private boolean examEnded = false;

	public ExamState() {
		teachers = new BoundCounterWithRaceConditionCheck(0, 1,
				MAXIMUM_1_TEACHER_POINTS, MAXIMUM_1_TEACHER, null, 0, null);
		students = new BoundCounterWithRaceConditionCheck(0, 50,
				MAXIMUM_50_STUDENTS_POINTS, MAXIMUM_50_STUDENTS, null, 0, null);

	}

	public void teacherEnter() {
		log(teachers.assertEquals(0, SECOND_TEACHER_IN_CLASSROOM_POINTS,
				SECOND_TEACHER_IN_CLASSROOM), null);
		log(students.assertEquals(0, STUDENT_BEFORE_TEACHER_POINTS,
				STUDENT_BEFORE_TEACHER), null);
		log(teachers.incrementWithMax(), TEACHER_ENTERING_IN_CLASSROOM);
	}

	public void studentEnter() {
		log(teachers.assertEquals(1, STUDENT_ENTER_POINTS,
				STUDENT_BEFORE_TEACHER_ENTER), null);
		log(students.incrementWithMax(false), STUDENT_ENTERING_CLASSROOM);
		Switcher.forceSwitch(5);
	}

	public void distributeTests() {
		log(students.assertEquals(50,
				EXAM_START_WITH_LESS_THAN_50_STUDENTS_POINTS,
				EXAM_START_WITH_LESS_THAN_50_STUDENTS), null);
		log(teachers.assertEquals(1, MAXIMUM_1_TEACHER_POINTS,
				MAXIMUM_1_TEACHER), STARTING_THE_EXAM_DISTRIBUTING_THE_TESTS);

		examStarted = true;
		Switcher.forceSwitch(5);
	}

	public void examEnd() {
		synchronized (this) {
			if (!examStarted) {
				log(new PointsException(EXAM_NOT_STARTED_POINTS,
						EXAM_NOT_STARTED), null);
			}
			examStarted = false;
			examEnded = true;
		}
		Switcher.forceSwitch(5);

	}

	public void studentLeave() {
		synchronized (this) {
			if (!examEnded) {
				log(new PointsException(
						EXAM_NOT_ENDED_WHEN_STUDENT_LEAVING_POINTS,
						EXAM_NOT_ENDED_WHEN_STUDENT_LEAVING), null);
			}
		}
		log(teachers.assertEquals(1, STUDENT_LEAVE_AFTER_PROFESOR_POINTS,
				STUDENT_LEAVE_AFTER_TEACHER), null);
		log(students.decrementWithMin(false), STUDENT_LEAVING_CLASSROOM);
		Switcher.forceSwitch(5);
	}

	public void teacherLeave() {
		students.assertEquals(0, STUDENTS_PRESENT_WHEN_TEACHER_LEAVING_POINTS,
				STUDENTS_PRESENT_WHEN_TEACHER_LEAVING);
		synchronized (this) {
			examEnded = false;
		}
		teachers.decrementWithMin();
	}

	@Override
	public void finalize() {
		if (students.getMax() == 1) {
			logException(new PointsException(
					CLASSROOM_ENTERING_NOT_PARALEL_POINTS,
					CLASSROOM_ENTERING_NOT_PARALEL));
		}

	}

}
