package mk.ukim.finki.os.synchronization.exam18.s1;

import java.util.ArrayList;
import java.util.List;

public class ExamSchedule {

    private int numExams;

    public ExamSchedule(int numExams, int numLabs) {
        this.numExams = numExams;
        init();
    }

    public static void main(String[] args) {
        ExamSchedule examSchedule = new ExamSchedule(10, 3);
        examSchedule.startExams();
    }

    public void init() {
        //TODO: add initialization here
    }

    public void startExams() {
        //TODO: add synchronization in this block
        List<Exam> examList = new ArrayList<Exam>();
        for (int i = 0; i < numExams; i++) {
            Exam exam = new Exam((int) (Math.random() * 100));
            examList.add(exam);
        }
        for (Exam exam : examList) {
            exam.start();
        }

    }
}
