package mk.ukim.finki.os.synchronization.exam18.s2;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class TennisTournament {


    static int greenNum = 0;
    static Semaphore lock = new Semaphore(1);

    static Semaphore red = new Semaphore(2);
    static Semaphore green = new Semaphore(2);

    static Semaphore redHere = new Semaphore(0);
    static Semaphore ready = new Semaphore(0);

    static Semaphore done = new Semaphore(0);


    public static class GreenPlayer extends Thread {

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            System.out.println("Green player ready");
            green.acquire();
            System.out.println("Green player enters field");
            lock.acquire();
            greenNum++;
            if (greenNum == 2) {
                lock.release();
                redHere.acquire(2);
                ready.release(4);
            } else {
                lock.release();
            }
            ready.acquire();
            System.out.println("Match started");
            Thread.sleep(200);
            System.out.println("Green player finished playing");
            done.release();

            lock.acquire();
            greenNum--;
            if (greenNum == 0) {

                done.acquire(4);
                // TODO: only one player calls the next line per match
                System.out.println("Match finished");
                green.release(2);
                red.release(2);
            }
            lock.release();
        }

    }

    public static class RedPlayer extends Thread {

        @Override
        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {
            System.out.println("Red player ready");
            red.acquire();
            System.out.println("Red player enters field");
            redHere.release();
            ready.acquire();
            System.out.println("Match started");
            Thread.sleep(200);
            System.out.println("Red player finished playing");
            done.release();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        // start 30 red and 30 green players in background
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 30; i++) {
            RedPlayer red = new RedPlayer();
            threads.add(red);
            GreenPlayer green = new GreenPlayer();
            threads.add(green);
        }
        for (Thread t : threads) {
            t.start();
        }
        // after all of them are started, wait each of them to finish for 1_000 ms
        for (Thread t : threads) {
            t.join(2_000);
        }
        // after the waiting for each of the players is done, check the one that are not finished and terminate them
        for (Thread t : threads) {
            if (t.isAlive()) {
                t.interrupt();
                System.err.println("POSSIBLE DEADLOCK!");
            }
        }

    }

}
