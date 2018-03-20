package mk.ukim.finki.os.synchronization.exam17.s3.g1;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Reader extends Thread {

  public static Random random = new Random();
  public static Semaphore lock = new Semaphore(1);
  public static Semaphore event = new Semaphore(0);

  public static void main(String[] args) throws InterruptedException {
    // TODO: kreirajte Reader i startuvajte go negovoto pozadinsko izvrsuvanje
    Reader reader = new Reader();
    reader.start();

    for (int i = 0; i < 100; i++) {
      Writer writer = new Writer();
      //TODO: startuvajte go writer-ot
      writer.start();

    }


    // TODO: Cekajte 10000ms za Reader-ot da zavrsi
    reader.join(100);

    // TODO: ispisete go statusot od izvrsuvanjeto
    if (reader.isAlive()) {
      System.out.println("reader will be interrupted");
      reader.interrupt();
    } else {
      System.out.println("reader is done");
    }

  }

  /**
   * Ne smee da bide izvrsuva paralelno so write() metodot
   */
  public static void read() {
    System.out.println("reading");
  }


  public void run() {
    int pendingReading = 100;
    while (pendingReading > 0) {
      pendingReading--;
      try {
        // TODO: cekanje na nov zapisan podatok
        event.acquire();

        lock.acquire();
        // TODO: read() metodot ne smee da se izvrsuva paralelno so write() od Writer klasata
        read();
        lock.release();
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }

    System.out.println("Done scheduling!");
  }
}


class Writer extends Thread {

  public Integer duration;

  public Writer() throws InterruptedException {
    this.duration = Reader.random.nextInt(1000);
  }


  /**
   * Ne smee da bide povikan paralelno
   */
  public static void write() {
    System.out.println("writting");
  }

  @Override
  public void run() {
    try {
      Thread.sleep(this.duration);

      Reader.lock.acquire();
      write();
      Reader.lock.release();
      Reader.event.release();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}