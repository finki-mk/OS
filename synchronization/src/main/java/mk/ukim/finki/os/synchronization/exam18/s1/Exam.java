package mk.ukim.finki.os.synchronization.exam18.s1;

import java.util.ArrayList;
import java.util.List;

public class Exam extends Thread {


    Laboratory lab = new Laboratory(Exam.this);

    private int numStudents;

    public Exam(int numStudents) {
        this.numStudents = numStudents;
        init();
    }

    public void enterLaboratory() {
        lab.studentEnters();
        System.out.println("The student enters in laboratory.");
    }

    public void solveTheory() {
        System.out.println("The student solves theory.");
    }

    public void solvePractical() {
        System.out.println("The student solves practical.");
    }

    public void exitLaboratory() {
        lab.studentExits();
        System.out.println("The student exits practical.");
    }

    public void showReport() {
        System.out.println("The computer systems shows results.");
    }

    public void init() {
        //TODO: add initialization here
    }

    public void startExam() throws InterruptedException {
        //TODO: add synchronization in this block
        List<Student> studentList = new ArrayList<Student>();
        for (int i = 0; i < numStudents; i++) {
            Student s = new Student(Exam.this);
            studentList.add(s);
        }
        for (Student s : studentList) {
            s.start();
        }
        for (Student s : studentList) {
            s.join();
        }

        this.showReport();
    }

    @Override
    public void run() {
        try {
            startExam();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
