package mk.ukim.finki.os.synchronization.exam19.k1.g2;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

class Processor  {


    public void execute() throws InterruptedException {
        int processedMessages=0;

        while(processedMessages < 50) {
            // wait fotr 5 ready messages in order to activate the processing
            System.out.println("Activate processing");


            System.out.println("Request message");
            // when the messasge is provided, process it
            System.out.println("Process message");
            Thread.sleep(200);
            // if there are no more ready messages, pause the processing
            System.out.println("Processing pause");
        }
    }

}

class MessageSource {



    public void execute() throws InterruptedException {
        Thread.sleep(50);
        System.out.println("Message ready");
        // wait until the processor requests the message
        System.out.println("Provide message");
        // wait until the processor is done with the processing of the message
        System.out.println("Message delivered. Leaving.");
    }

}

public class MessageProcessing {


    public static void main(String[] args) throws InterruptedException {
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 50; i++) {
            MessageSource ms = new MessageSource();
            threads.add(ms);
        }
        threads.add(new Processor());
        // start all threads in background

        // after all of them are started, wait each of them to finish for 1_000 ms

        // after the waiting for each of the players is done, check the one that are not finished and terminate them
        System.err.println("Possible deadlock");
    }

}