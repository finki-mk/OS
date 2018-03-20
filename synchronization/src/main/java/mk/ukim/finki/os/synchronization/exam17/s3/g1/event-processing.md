Read-Write Simulation
============================

Потребно е да извршите симулација за читање и запишување на податоци со дефининираните 
класи `Reader` и `Writer` кои треба да функционираат како thread-ови.  

Главниот метод `Reader.main` да го стартува `Reader` thread-от и потоа треба да стартува 100 нови `Writer` 
thread-ови. Потоа `main` методот треба да го чека `Reader` thread-от да заврши најмногу 10000ms. 
Доколку не заврши за ова време, треба да се прекине позадинското извршвање на `Reader` и да се испише порака `Terminated reading`, а во спротивен 
случај да се испише `Finished reading`. 


Секој од `Writer` thread-овите веднаш по стартувањето во позадина треба да заспие рандом време со
`Thread.sleep(this.duration)`, по што треба да запише со методот `write()` кој **не смее да се извршува
паралелно**. Потоа треба да се извести `Reader` thread-от дека има нови податоци за читање. 

Класата `Reader` е Thread, кој во позадина извршува бесконечен циклус, во кој чека да биде известен за новиот настан,
по што го процесира со користење на методот `read()`, кој **не смее да се повика паралелно со извршување на `write()` 
методот**.

Вашата задача е да го дополните дадениот код според барањата на задачата, при што треба да внимавате 
не настане Race Condition и Deadlock. 

```

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Reader {

  public static Random random = new Random();

  public static void main(String[] args) throws InterruptedException {
    // TODO: kreirajte Reader i startuvajte go negovoto pozadinsko izvrsuvanje


    for (int i = 0; i < 100; i++) {
      Writer writer = new Writer();
      //TODO: startuvajte go writer-ot
      
    }


    // TODO: Cekajte 10000ms za Reader-ot da zavrsi

    // TODO: ispisete go statusot od izvrsuvanjeto
  }

  /**
   * Ne smee da bide izvrsuva paralelno so write() metodot
   */
  public static void read() {
    System.out.println("reading");
  }


  public void run() {
    int pendingReading=100;
    while (pendingReading>0) {
      pendingReading--;
      try {
        // TODO: cekanje na nov zapisan podatok


        // TODO: read() metodot ne smee da se izvrsuva paralelno so write() od Writer klasata
        read();
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }

    System.out.println("Done reading!");
  }
}


class Writer {

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
}

```

