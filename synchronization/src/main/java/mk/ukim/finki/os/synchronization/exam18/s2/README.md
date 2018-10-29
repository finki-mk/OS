Tennis tournament
=====

Потребно е да направите сценарио за синхронизација на турнир во тенис за двојки. 

На турнирот се натпреваруваат зелена со црвена група, каде во секоја од групите има по 30 учесници. 
 
Потребно е да го синхронизирате турнирот на следниот начин:

 - По стартувањето на играчите во позадина, тие најпрво треба да испечатат `{Red|Green} player ready`
 - На теренот може да влезат по 2 играчи од двете групи. По влегувањето треба да испечатат `{Red|Geen} player enters field`
    - Доколку влезат повеќе од 2 играчи од некоја од групте, сценариото е невалидно
 - Откако ќе влезат четирите играчи на теренот, треба истовремено да започнат со играта со печатење на `Match started` и повикување на `Thread.sleep(200)`
    - Треба да се повика истовремено и паралелно да се повика кај сите играчи.
 - Откако ќе заврши `Thread.sleep` кај сите играчи сите печатат `{Red|Green} player finished playing`
 - Потоа само еден играч печати `Match finished` и сигнализира дека теренот е слободен.  

   

Потребно е да имплементирате сценарио во кое во `main` методот ќе се стартуваат по 30 инстанци од класите `Greenplayer` 
и `Redplayer` кои ќе се однесуваат како Threads и во позадина секоја ќе знае да изигра една партија според претходно
опишаното сценарио. Откако сите играчи ќе се стартуваат, треба за секој од нив да се почека да заврши за максимум 1 секунда 
(1000 ms). Потоа треба да се провери дали сите инстанци се завршени со позадинското извршување. Доколку некоја инстанца
не е завршена, потребно е истата да се терминира и да се испечати пораката: `Possible deadlock`.  

Имплементацијата на сценариото треба да се направи во `execute` методите, кои треба да се стартуваат во позадина. 


**Стартен код (TennisTournament.java):**

```

import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class TennisTournament {

    

    public static class GreenPlayer  {
    
    
        public void execute() throws InterruptedException {
            System.out.println("Green player ready");
            System.out.println("Green player enters field");
            System.out.println("Match started");
            System.out.println("Green player finished playing");
            // TODO: only one player calls the next line per match
            System.out.println("Match finished");
        }

    }

    public static class RedPlayer {


        
        public void execute() throws InterruptedException {
            System.out.println("Red player ready");
            System.out.println("Red player enters field");
            System.out.println("Match started");
            System.out.println("Red player finished playing");
            // TODO: only one player calls the next line per match
            System.out.println("Match finished");
        }

    }


    public static void main(String[] args) throws InterruptedException {
        HashSet<Thread> threads = new HashSet<Thread>();
        for (int i = 0; i < 30; i++) {
            RedPlayer red = new RedPlayer();
            threads.add(red);
            GreenPlayer green = new GreenPlayer();
            threads.add(green);
        }
        // start 30 red and 30 green players in background
        
        // after all of them are started, wait each of them to finish for 1_000 ms
        
        // after the waiting for each of the players is done, check the one that are not finished and terminate them 
        System.err.println("Possible deadlock");
    }

}

``` 