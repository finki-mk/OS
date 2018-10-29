package mk.ukim.finki.os.synchronization.exam18.s1;

public class Laboratory {

    private final static int CAPACITY = 20;

    private int numActiveStudents;

    public Laboratory(Exam state) {
        this.numActiveStudents = 0;
    }

    public synchronized int checkNumActiveStudents() {
        //TODO: add synchronization in this block
        return numActiveStudents;
    }

    public synchronized int getFreeComputers() {
        //TODO: add synchronization in this block
        return CAPACITY - numActiveStudents;
    }


    public synchronized void studentEnters() {
        //TODO: implement this method
        numActiveStudents++;
    }

    public synchronized void studentExits() {
        //TODO: implement this method
        numActiveStudents--;
    }
}
