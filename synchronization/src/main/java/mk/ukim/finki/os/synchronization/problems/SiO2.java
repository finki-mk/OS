package mk.ukim.finki.os.synchronization.problems;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

/**
 * @author Riste Stojanov
 */
public class SiO2 {

    public static Semaphore si = new Semaphore(1);
    public static Semaphore o = new Semaphore(2);

    public static Semaphore oHere = new Semaphore(0);
    public static Semaphore readyBarrier = new Semaphore(0);

    public static Semaphore doneBarrier = new Semaphore(0);


    static class Silicium extends Thread {

        public Silicium() {
            this.setName("Si");
        }

        public void run() {
            try {
                processSi(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    static class Oxygen extends Thread {


        public Oxygen() {
            this.setName("O");
        }

        public void run() {
            try {
                processO(this);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static void processSi(Object t) throws InterruptedException {
        si.acquire();
        oHere.acquire(2);
        readyBarrier.release(2);
        bond(t);
        doneBarrier.acquire(2);
        o.release(2);
        si.release();

    }

    public static void processO(Object t) throws InterruptedException {
        o.acquire();
        oHere.release();
        readyBarrier.acquire();
        bond(t);
        doneBarrier.release();

    }

    public static void main(String[] args) throws InterruptedException {

        HashSet<Thread> atoms = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            // kreirajte eden Si atom i 2 O atomi vo pozadina i dodajte gi vo atoms mnozestvoto
            Silicium si = new Silicium();

            Oxygen o1 = new Oxygen();
            Oxygen o2 = new Oxygen();

            atoms.add(si);
            atoms.add(o1);
            atoms.add(o2);
        }
        // startuvajte gi site atomi vo pozadina
        for (Thread t : atoms) {
            t.start();
        }

        // sekoj od atomite pocekajte go maksimum 2s da zavrsi
        for (Thread t : atoms) {
            t.join(2_000);
        }
        // ako nekoj od atomite ne zavrsil, terminirajte go i ispecatete "possible deadlock"
        for (Thread t : atoms) {
            if (t.isAlive()) {
                t.interrupt();
                System.out.println("possible deadlock");
            }


        }


    }

    public static void bond(Object t) {
        System.out.println("Bonding: " + ((Thread) t).getName());
    }
}
