package mk.ukim.finki.os.synchronization.exam19.s1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Semaphore;

class Policeman {

  public void execute() throws InterruptedException {
    // waits until it is valid to enter the car
    System.out.println("Policeman enters in the car");

    // when the four passengers are inside, one policeman prints the starting command
    System.out.println("Start driving.");
    Thread.sleep(100);
    // one policeman prints the this command to notice that everyone can exit
    System.out.println("Arrived.");
    // the exit from the car is allowed after the "Arrived." message is printed
    System.out.println("Policeman exits from the car");
  }

}

class Criminal {

  public void execute() throws InterruptedException {
    // waits until it is valid to enter the car
    System.out.println("Criminal enters in the car");

    Thread.sleep(100);
    // the exit from the car is allowed after the "Arrived." message is printed
    System.out.println("Criminal exits from the car");
  }
}

public class CriminalTransport {


  public static void main(String[] args) throws InterruptedException {
    HashSet<Thread> threads = new HashSet<Thread>();
    for (int i = 0; i < 60; i++) {
      Policeman red = new Policeman();
      threads.add(red);
      Criminal green = new Criminal();
      threads.add(green);
    }
    // run all threads in background

    // after all of them are started, wait each of them to finish for maximum 1_000 ms

    // for each thread, terminate it if it is not finished

  }

}