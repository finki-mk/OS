Event Processing Simulation
============================

Потребно е да извршите симулација за процесирање на настани со дефининираните 
класи `EventGenerator` и `Processor` кои треба да функционираат како thread-ови.  

Главниот метод `Processor.main` да го стартува `Processor` thread-от и потоа треба да стартува 100 нови `EventGenerator` 
thread-ови, да ги регистрира со `Processor.register(EventGenerator p)` методот. Потоа `main` методот треба да го чека 
`Processor` thread-от да заврши најмногу 20000ms. Доколку не заврши за ова време, треба да се прекине и да се испише 
порака `Terminated scheduling`, а во спротивен случај да се испише `Finished scheduling`. 


Секој од `EventGenerator` thread-овите веднаш по стартувањето во позадина треба да заспие рандом време со
`Thread.sleep(this.duration)`, по што треба да го генерира настанот со методот `generate()` кој **не смее** да се извршува
паралелно кај **повеќе од 5 генератори**. Потоа треба да се извести `Processor` thread-от дека има нов настан за процесирање. 

Класата `Processor` е Thread, кој во позадина извршува бесконечен циклус, во кој чека да биде известен за новиот настан,
по што го процесира со користење на методот `process()`, кој **не смее да се повика паралелно со извршување на `generate()` 
методот**.

Вашата задача е да го дополните дадениот код според барањата на задачата, при што треба да внимавате 
не настане Race Condition и Deadlock. 

```

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Processor {

  public static Random random = new Random();
  static List<EventGenerator> scheduled = new ArrayList<>();

  public static void main(String[] args) throws InterruptedException {
    // TODO: kreirajte Processor i startuvajte go negovoto pozadinsko izvrsuvanje


    for (int i = 0; i < 100; i++) {
      EventGenerator eventGenerator = new EventGenerator();
      register(eventGenerator);
      //TODO: startuvajte go eventGenerator-ot
      
    }


    // TODO: Cekajte 20000ms za Processor-ot da zavrsi

    // TODO: ispisete go statusot od izvrsuvanjeto
  }

  public static void register(EventGenerator generator) {
    scheduled.add(generator);
  }

  /**
   * Ne smee da bide izvrsuva paralelno so generate() metodot
   */
  public static void process() {
    System.out.println("processing event");
  }


  public void run() {

    while (!scheduled.isEmpty()) {
      // TODO: cekanje  na nov generiran event

      // TODO: povikajte go negoviot process() metod
      process();
    }

    System.out.println("Done scheduling!");
  }
}


class EventGenerator {

  public Integer duration;

  public EventGenerator() throws InterruptedException {
    this.duration = Processor.random.nextInt(1000);
  }


  /**
   * Ne smee da bide povikan paralelno kaj poveke od 5 generatori
   */
  public static void generate() {
    System.out.println("Generating event: ");
  }
}

```

