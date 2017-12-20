package mk.ukim.finki.os.synchronization.exam17.s1.g1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author Riste Stojanov
 */
public class Scheduler extends Thread {
  public static Random random = new Random();
  static List<Process> scheduled = new ArrayList<>();

  public static void main(String[] args) throws InterruptedException {
    Process processes[] = new Process[100];
    for (int i = 0; i < 100; i++) {
      processes[i] = new Process();
      register(processes[i]);
    }
    Scheduler scheduler = new Scheduler();
    scheduler.start();

    scheduler.join(20000);
    if (scheduler.isAlive()) {
      scheduler.interrupt();
      System.out.println("Terminated scheduling");
    } else {
      System.out.println();
    }
  }

  public static synchronized void register(Process process) {
    scheduled.add(process);
  }

  public Process next() {
    synchronized (Scheduler.class) {
      if (!scheduled.isEmpty()) {
        return scheduled.remove(0);
      }
    }
    return null;

  }


  public void run() {
    try {
      while (!scheduled.isEmpty()) {
        Thread.sleep(100);
        System.out.print(".");

        Process process = next();
        if (process != null) {
          process.execute();
          process.join();
        }

      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Done scheduling!");
  }
}


class Process extends Thread {

  public Integer duration;

  public Process() throws InterruptedException {
    this.duration = Scheduler.random.nextInt(1000);
  }

  public void execute() {
    System.out.println("Executing[" + this + "]: " + duration);
    this.start();
  }

  public void run() {
    try {
      Thread.sleep(duration);
    } catch (Exception e) {
    }
  }


}
