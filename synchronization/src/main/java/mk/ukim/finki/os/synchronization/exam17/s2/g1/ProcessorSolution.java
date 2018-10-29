package mk.ukim.finki.os.synchronization.exam17.s2.g1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProcessorSolution extends Thread {

    public static Semaphore canGenerate = new Semaphore(5);
    public static Semaphore processEvent = new Semaphore(0);

    public static Random random = new Random();
    static List<EventGeneratorSolution> scheduled = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        // TODO: kreirajte Processor i startuvajte go negovoto pozadinsko izvrsuvanje
        Processor processor = new Processor();
        processor.start();


        for (int i = 0; i < 100; i++) {
            EventGeneratorSolution eventGenerator = new EventGeneratorSolution();
            register(eventGenerator);
            //TODO: startuvajte go eventGenerator-ot
            eventGenerator.start();
        }


        // TODO: Cekajte 20000ms za Processor-ot da zavrsi
        processor.join(20_000);

        // TODO: ispisete go statusot od izvrsuvanjeto
        if (processor.isAlive()) {
            processor.interrupt();
            System.out.println("Terminated scheduling");
        } else {
            System.out.println("Finished scheduling");
        }

    }

    public static void register(EventGeneratorSolution generator) {
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
            try {
                // TODO: cekanje  na nov generiran event
                processEvent.acquire();

                // TODO: povikajte go negoviot process() metod
                canGenerate.acquire(5);
                process();
                canGenerate.release(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Done scheduling!");
    }
}


class EventGeneratorSolution extends Thread {

    public Integer duration;

    public EventGeneratorSolution() throws InterruptedException {
        this.duration = Processor.random.nextInt(1000);
    }

    /**
     * Ne smee da bide povikan paralelno kaj poveke od 5 generatori
     */
    public static void generate() {
        System.out.println("Generating event: ");
    }

    @Override
    public void run() {
        try {
            Thread.sleep(this.duration);
            ProcessorSolution.canGenerate.acquire();
            generate();
            ProcessorSolution.processEvent.release();
            ProcessorSolution.canGenerate.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}