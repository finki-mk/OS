Message Processing
=====

Потребно е да направите сценарио за синхронизација на процесирање на пораки. Притоа треба да имате еден процесор (`Processor`) и 50 
извори на пораките (`MessageSource`).  

Процесорот е паузиран се додека не се насоберат 5 пораки од изворите. Кога ќе се насоберат 5 пораки, петтиот извор на порака 
го активира процесорот и тој започнува да обработува пораките. При активирањето, процесорот печати `Activated processing`. 
Штом процесорот ќе ги обработи пораките од сите извори, 
тој повторно паузира. Кога процесорот е активен, тој најпрво бара порака од еден од изворите со печатење на `Request message`. 
Кога пораката ќе биде спремна, изворот печати `Provide message`, по што почнува нејзината обработка. 
Откако пораката е обработена, изворот печати `Message delivered. Leaving`, по што завршува неговата работа во позадина. 
Доколку нема наредена порака од некој од изворите, процесорот паузира со печатење на `Processing pause`. 
Доколку процесорот е паузиран, изворите треба да чекаат додека не се соберат 5 пораки, и петтиот треба да го активира процесорот.
                                       
Во почетниот код кој е даден, дефинирани се класите `Processor` и `MessageSource`, кои ги симболизираат процесорот и 
изворите на пораките. Има само една инстанца од класата `Processor` кај која методот execute() се повикува 50 пати 
и 50 инстанци од класата `MessageSource` во кои методот execute() се повикува само еднаш.
                                       
Вашата задача е да ги имплементирате методите execute() од класите `Processor` и `MessageSource` според претходно 
опшаното сценарио.

Потребно е да имплементирате сценарио во кое во `main` методот ќе се стартуваат 50 инстанци од класите `MessageSource` 
и една инстанца од `Processor` кои ќе се однесуваат како Threads и во позадина секоја ќе се однесува според претходно
опишаното сценарио. Откако ќе се стартуваат процесорот и изворите на пораки, треба за секој од нив да се почека да заврши 
за максимум 1 секунда (1000 ms). Потоа треба да се провери дали сите инстанци се завршени со позадинското извршување. 
Доколку некоја инстанца не е завршена, потребно е истата да се терминира и да се испечати пораката: `Possible deadlock`.  

Имплементацијата на сценариото треба да се направи во `execute` методите, кои треба да се стартуваат во позадина. 


**Стартен код (MessageProcessing.java):**

```

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

``` 