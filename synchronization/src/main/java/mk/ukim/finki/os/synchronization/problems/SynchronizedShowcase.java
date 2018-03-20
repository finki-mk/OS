package mk.ukim.finki.os.synchronization.problems;

import mk.ukim.finki.os.synchronization.Switcher;

/**
 * @author Riste Stojanov
 */
public class SynchronizedShowcase {

  public static long staticField = 0;

  public static void main(String[] args) {
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < 10; i++) {
          SynchronizedShowcase.staticSynchronizedBlockIncrement();
        }
      }
    });

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        for (int i = 0; i < 10; i++) {
          SynchronizedShowcase.staticSynchronizedIncrement();
        }
      }
    });

    Thread t3 = new Thread(new Runnable() {
      @Override
      public void run() {
        SynchronizedShowcase instance = new SynchronizedShowcase();
        for (int i = 0; i < 10; i++) {
          instance.synchronizedBlockIncrement();
        }
      }
    });

    Thread t4 = new Thread(new Runnable() {
      @Override
      public void run() {
        SynchronizedShowcase instance = new SynchronizedShowcase();
        for (int i = 0; i < 10; i++) {
          instance.synchronizedIncrement();
        }
      }
    });

    t1.start();
    t2.start();
    t3.start();
    t4.start();
  }

  public static synchronized void staticSynchronizedIncrement() {
    System.out.println("staticSynchronizedIncrement start");
    Switcher.forceSwitch(10);
    staticField++;
    System.out.println("staticSynchronizedIncrement end");
  }


  public static void staticSynchronizedBlockIncrement() {
    synchronized (SynchronizedShowcase.class) {
      System.out.println("staticSynchronizedIncrement start");
      staticField++;
      Switcher.forceSwitch(10);
      System.out.println("staticSynchronizedIncrement end");
    }
  }


  public synchronized void synchronizedIncrement() {
    System.out.println("synchronizedIncrement start");
    staticField++;
    Switcher.forceSwitch(10);
    System.out.println("synchronizedIncrement end");
  }


  public void synchronizedBlockIncrement() {
    synchronized (this) {
      System.out.println("synchronizedBlocIncrement start");
      staticField++;
      Switcher.forceSwitch(10);
      System.out.println("synchronizedBlocIncrement end");
    }
  }

  public void synchronizedWithTheStaticBlocksIncrement() {
    synchronized (SynchronizedShowcase.class) {
      System.out.println("synchronizedBlocIncrement start");
      staticField++;
      Switcher.forceSwitch(10);
      System.out.println("synchronizedBlocIncrement end");
    }
  }

}
