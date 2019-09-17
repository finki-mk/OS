package mk.ukim.finki.os.synchronization.exam19.k1.g1.solution;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class MacauCardTournament {

    public static void main(String[] args) throws InterruptedException {
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 30; i++) {
            RedPlayer red = new RedPlayer();
            threads.add(red);
            GreenPlayer green = new GreenPlayer();
            threads.add(green);
        }
        // start 30 red and 30 green players in background
        for (Thread t : threads) {
            t.start();
        }
        // after all of them are started, wait each of them to finish for 1_000
        // ms
        for (Thread t : threads) {
            t.join(1_000);
        }
        // after the waiting for each of the players is done, check the one that
        // are not finished and terminate them
        for (Thread t : threads) {
            if (t.isAlive()) {
                t.interrupt();
                System.err.println("Possible deadlock");
            }
        }
    }

    public static Semaphore redT = new Semaphore(2);
    public static Semaphore greenT = new Semaphore(2);

    public static Semaphore greenHere = new Semaphore(0);
    public static Semaphore ready = new Semaphore(0);
    public static Semaphore done = new Semaphore(0);

    public static Semaphore lock = new Semaphore(1);
    public static int brR = 0;

    public static class RedPlayer extends Thread {

        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {

            redT.acquire();
            System.out.println("Red player ready");

            Thread.sleep(50);
            System.out.println("Red player here");

            lock.acquire();
            brR++;
            boolean komandant = (brR == 2);
            lock.release();
            for (int num = 1; num <= 3; num++) {
                if (komandant) {
                    // komandant
                    greenHere.acquire(2);
                    ready.release(4);
                }
                ready.acquire();
                System.out.println("Game red " + num + " started");
                Thread.sleep(200);
                System.out.println("Red player finished game " + num);
                done.release();

                if (komandant) {
                    done.acquire(4);
                }
            }

            if (komandant) {
                brR = 0;
                redT.release(2);
                greenT.release(2);
                System.out.println("Match finished");
            }


        }

    }

    public static class GreenPlayer extends Thread {

        public void run() {
            try {
                execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void execute() throws InterruptedException {

            greenT.acquire();
            System.out.println("Green player ready");
            Thread.sleep(50);
            System.out.println("Green player here");

            for (int num = 1; num <= 3; num++) {
                greenHere.release();
                ready.acquire();
                System.out.println("Game green " + num + " started");
                Thread.sleep(200);
                System.out.println("Green player finished game " + num);
                done.release();
            }

        }
    }

}
