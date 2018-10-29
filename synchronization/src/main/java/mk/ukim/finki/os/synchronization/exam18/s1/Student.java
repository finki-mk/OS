package mk.ukim.finki.os.synchronization.exam18.s1;

import java.util.concurrent.Semaphore;

public class Student extends Thread {

    Semaphore labaratory = new Semaphore(20);
    private Exam state;

    public Student(Exam state) {
        this.state = state;
    }

    @Override
    public void run() {
        //TODO: add synchronization in this block
        //theoretical part execution
        try {
            labaratory.acquire();
            this.state.enterLaboratory();
            this.state.solveTheory();
            this.state.exitLaboratory();
            labaratory.release();

            labaratory.acquire();
            //practical part execution
            this.state.enterLaboratory();
            this.state.solvePractical();
            this.state.exitLaboratory();
            labaratory.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
