Event Processing Simulation
============================

Потребно е да извршите симулација за процесирање на настани со дефининираните 
класи `EventGenerator` и `Processor` кои треба да функционираат како thread-ови.  

Главниот метод `Processor.main` да стартува 20 `Processor` thread-ови и потоа треба да стартува 100 нови `EventGenerator` 
thread-ови во позадина. Потоа `main` методот треба да ги чека `Processor` thread-овите да завршат за најмногу 20000ms. 
Доколку не завршат за ова време, треба да се прекинат и да се испише порака `Terminated processing`, а во спротивен 
случај да се испише `Finished processing`. 


Секој од `EventGenerator` thread-овите веднаш по стартувањето во позадина да заспие рандом време со
`Thread.sleep(this.duration)`, по што треба да го генерира настанот со методот `generate()` кој **не смее** да се извршува
паралелно кај **повеќе од 5 генератори**. Потоа треба да се извести `Processor` thread-от дека има нов настан за процесирање. 
Додека не заврши ова процесирање, не смеат да се генерираат нови настани. 

Секоја од `Processor` класите е Thread, кој во позадина чека да биде известен за **5 нови настани**,
по што ги процесира со користење на методот `process()`. Дури откако ќе заврши овој метод, може да се продолжи со 
генерирање на нови настани, а во меѓувреме сите `EventGenerator`-и го чекаат овој метод да заврши.

Вашата задача е да го дополните дадениот код според барањата на задачата, при што треба да внимавате 
не настане Race Condition и Deadlock. 

```

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventProcessor {

    public static Random random = new Random();
    static List<EventGenerator> scheduled = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        List<EventProcessor> processors = new ArrayList<>();
        // TODO: kreirajte 20 Processor i startuvajte gi vo pozadina
        for (int i = 0; i < 20; i++) {
            EventProcessor p = new EventProcessor();
            processors.add(p);
            //TODO: startuvajte go vo pozadina
        }

        for (int i = 0; i < 100; i++) {
            EventGenerator eventGenerator = new EventGenerator();
            //TODO: startuvajte go eventGenerator-ot

        }


        for (int i = 0; i < 20; i++) {
            EventProcessor p = processors.get(i);
            // TODO: Cekajte 20000ms za Processor-ot p da zavrsi

            // TODO: ispisete go statusot od izvrsuvanjeto
        }
    }


    public static void process() {
        // TODO: pocekajte 5 novi nastani

        System.out.println("processing event");
    }

}


class EventGenerator {

    public Integer duration;

    public EventGenerator() throws InterruptedException {
        this.duration = EventProcessor.random.nextInt(1000);
    }


    /**
     * Ne smee da bide povikan paralelno kaj poveke od 5 generatori
     */
    public static void generate() {
        System.out.println("Generating event: ");
    }
}
```

