package mk.ukim.finki.os.synchronization.exam18.s3;

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