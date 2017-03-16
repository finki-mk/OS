package mk.ukim.finki.os.synchronization.exam15.march.sushibar;

import mk.ukim.finki.os.synchronization.ProblemExecution;
import mk.ukim.finki.os.synchronization.TemplateThread;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class SushiBar {
  public static int jadat;
  public static Semaphore p;
  public static Semaphore posetiteli;
  public static int br;
  public static Semaphore val;
  public static Semaphore done;
  public static Semaphore done1;
  public static int calWalt;
  public static Semaphore eat;
  public static Semaphore prodolzi;


  public static void init() {

    p = new Semaphore(1);
    posetiteli = new Semaphore(6);
    jadat = 0;
    br = 0;
    val = new Semaphore(0);
    done = new Semaphore(0);
    done1 = new Semaphore(1);
    calWalt = 0;
    eat = new Semaphore(0);
    prodolzi = new Semaphore(0);

  }


  public static class Customer extends TemplateThread {

    public Customer(int numRuns) {
      super(numRuns);
    }

    @Override
    public void execute() throws InterruptedException {

      posetiteli.acquire();
      p.acquire();
      br++;
      p.release();
      //прекин
      if (br == 6) {
        val.release(6);
      }

      val.acquire();
      state.customerSeat();
      if (val.availablePermits() == 0) {
        //прекин
        prodolzi.release(6);
        calWalt = 1;
      }
      prodolzi.acquire();
      if (calWalt == 1) {
        state.callWaiter();
        eat.release(6);
      }

      eat.acquire();
      state.customerEat();

      done1.acquire();
      br--;
      if (br == 0) {
        done.release();
      } else {
        done1.release();
      }

      done.acquire();
      state.eatingDone();
      posetiteli.release(6);
      p.release(1);
      done1.release(1);
      br = 0;
      calWalt = 0;
    }

  }

  public static void main(String[] args) {
    for (int i = 0; i < 10; i++) {
      run();
    }
  }

  static SushiBarState state = new SushiBarState();

  public static void run() {
    try {
      int numRuns = 1;
      int numIterations = 1200;

      HashSet<Thread> threads = new HashSet<Thread>();

      for (int i = 0; i < numIterations; i++) {
        Customer c = new Customer(numRuns);
        threads.add(c);
      }

      init();

      ProblemExecution.start(threads, state);
      // System.out.println(new Date().getTime());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
