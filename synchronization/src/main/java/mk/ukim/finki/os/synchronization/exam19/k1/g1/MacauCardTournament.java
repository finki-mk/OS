package mk.ukim.finki.os.synchronization.exam19.k1.g1;

import java.util.HashSet;

class GreenPlayer  {


    public void execute() throws InterruptedException {
        System.out.println("Green player ready");
        Thread.sleep(50);
        System.out.println("Green player here");
            // TODO: the following code should be executed 3 times
            System.out.println("Game "+ num +" started");
            Thread.sleep(200);
            System.out.println("Green player finished game "+ num);
            // TODO: only one player calls the next line per game
            System.out.println("Game "+ num +" finished");
        // TODO: only one player calls the next line per match
        System.out.println("Match finished");
    }

}

class RedPlayer {



    public void execute() throws InterruptedException {
        System.out.println("Red player ready");
        Thread.sleep(50);
        System.out.println("Red player here");
            // TODO: the following code should be executed 3 times
            System.out.println("Game "+ num +" started");
            Thread.sleep(200);
            System.out.println("Red player finished game "+ num);
            // TODO: only one player calls the next line per game
            System.out.println("Game "+ num +" finished");
        // TODO: only one player calls the next line per match
        System.out.println("Match finished");
    }

}

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

        // after all of them are started, wait each of them to finish for 1_000 ms

        // after the waiting for each of the players is done, check the one that are not finished and terminate them
        System.err.println("Possible deadlock");
    }

}
