package mk.ukim.finki.os.synchronization.problems;

import mk.ukim.finki.os.synchronization.Switcher;

/**
 * @author Riste Stojanov
 */
public class RaceConditionThreadNotLocalShowcase {

  public static void main(String[] args) {

    Thread t = new Thread() {

      private long valLocal = 0;


      public void run() {

        Switcher.forceSwitch(100);
        for (int i = 0; i < 10; i++) {
          Switcher.forceSwitch(100);
          System.out.println(Thread.currentThread().getName() + " i: " + i + " val: " + valLocal);
          Switcher.forceSwitch(100);
          valLocal++;
          Switcher.forceSwitch(100);
          System.out.println(Thread.currentThread().getName() + "i: " + i + " val: " + valLocal);
          Switcher.forceSwitch(100);
        }
        Switcher.forceSwitch(100);
      }
    };
    t.setName("[t] ");
    t.start();

    Thread badThread = new Thread() {

      public void run() {
        t.run();
      }
    };
    badThread.setName("[badT] ");
    badThread.start();
  }
}
