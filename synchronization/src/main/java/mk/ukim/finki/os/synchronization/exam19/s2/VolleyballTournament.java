package mk.ukim.finki.os.synchronization.exam19.s2;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class VolleyballTournament {

    public static void main(String[] args) {
        HashSet<Player> threads = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            Player p = new Player();
            threads.add(p);
        }
        // run all threads in background

        // after all of them are started, wait each of them to finish for maximum 2_000 ms

        // for each thread, terminate it if it is not finished
        System.out.println("Possible deadlock!");
        System.out.println("Tournament finished.");

    }
}

class Player {

    public void execute() throws InterruptedException {
        // at most 12 players should print this in parallel
        System.out.println("Player inside.");
        // at most 4 players may enter in the dressing room in parallel
        System.out.println("In dressing room.");
        Thread.sleep(10);// this represent the dressing time
        // after all players are ready, they should start with the game together
        System.out.println("Game started.");
        Thread.sleep(100);// this represent the game duration
        System.out.println("Player done.");
        // only one player should print the next line, representing that the game has finished
        System.out.println("Game finished.");
    }
}
