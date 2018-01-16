package mk.ukim.finki.os.synchronization.exam17.s2.g1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Processor extends Thread {

  public static Random random = new Random();
  public static Semaphore event = new Semaphore(1);
  public static Semaphore inGenerate = new Semaphore(5);
  static List<EventGenerator> scheduled = new ArrayList<>();

  public static void main(String[] args) throws InterruptedException {
    // TODO: create the Processor and start it in the background
    Processor processor = new Processor();
    processor.start();

    for (int i = 0; i < 100; i++) {
      EventGenerator eventGenerator = new EventGenerator();
      register(eventGenerator);
      // TODO: start the eventGenerator
      eventGenerator.start();
    }

    // TODO: wait for 20.000 ms for the Processor to finish
    processor.join(20000);


    // TODO: write out the execution status
    if (processor.isAlive()) {
      processor.interrupt();
    } else {
    }
  }

  public static void register(EventGenerator generator) {
    scheduled.add(generator);
    try {
      Thread.sleep(random.nextInt(100));
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    scheduled.remove(0);
  }

  /**
   * Cannot be executed in parallel with the generate() method
   */
  public static void process() {
    System.out.println("processing event");
  }

  public void run() {

    while (!scheduled.isEmpty()) {
      try {
        // TODO: wait for a new event
        event.acquire();
        inGenerate.acquire(5);
        // TODO: invoke its process() method
        process();
        inGenerate.release(5);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    System.out.println("Done scheduling!");
  }
}


class EventGenerator extends Thread {

  public Integer duration;

  public EventGenerator() throws InterruptedException {
    this.duration = Processor.random.nextInt(1000);
  }

  /**
   * Cannot be invoked in parallel by more than 5 generators
   */
  public static void generate() throws InterruptedException {
    Processor.inGenerate.acquire();
    System.out.println("Generating event: ");
    Processor.event.release();
    Processor.inGenerate.release();
  }


  @Override
  public void run() {
    try {
      Thread.sleep(this.duration);
      generate();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    super.run();
  }


}