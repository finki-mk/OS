Macau card tournament
=====

Потребно е да направите сценарио за синхронизација на турнир во макао за двојки, каде победникот се избира по 3 одиграни партии. 

На турнирот се натпреваруваат зелена со црвена група, каде во секоја од групите има по 30 учесници. 
 
Потребно е да го синхронизирате турнирот на следниот начин:

 1. По стартувањето на играчите во позадина, тие најпрво треба да испечатат `{Red|Green} player ready`
 2. На масата може да седнат по 2 играчи од двете групи. По влегувањето треба да испечатат `{Red|Geen} player here`
    - Доколку влезат повеќе од 2 играчи од некоја од групте, сценариото е невалидно
 3. Откако ќе седнат четирите играчи на масата, треба истовремено да започнат со играта со печатење на `Game {num} started` и повикување на `Thread.sleep(200)`
    - Треба да се повика истовремено и паралелно да се повика кај сите играчи.
 4. Откако ќе заврши `Thread.sleep` кај сите играчи сите печатат `{Red|Green} player finished game {num}`.
 5. Потоа само еден играч печати `Game {num} finished` и сигнализира дека може да започне наредната партија од игра.
 6. По завршувањето на трите партии (игри), само еден играч сигнализира `Match finished`.

   

Потребно е да имплементирате сценарио во кое во `main` методот ќе се стартуваат по 30 инстанци од класите `Greenplayer` 
и `Redplayer` кои ќе се однесуваат како Threads и во позадина секоја ќе знае да изигра три партии од играта според претходно
опишаното сценарио. Откако сите играчи ќе се стартуваат, треба за секој од нив да се почека да заврши за максимум 1 секунда 
(1000 ms). Потоа треба да се провери дали сите инстанци се завршени со позадинското извршување. Доколку некоја инстанца
не е завршена, потребно е истата да се терминира и да се испечати пораката: `Possible deadlock`.  

Имплементацијата на сценариото треба да се направи во `execute` методите, кои треба да се стартуваат во позадина. 


**Стартен код (MacauCardTournament.java):**

```

import java.util.HashSet;
import java.util.concurrent.Semaphore;

class GreenPlayer  {


    public void execute() throws InterruptedException {
        System.out.println("Green player ready");
        Thread.sleep(50);
        System.out.println("Green player here");
            // TODO: the following code should be executed 3 times
            System.out.println("Game "+ num +" started");
            Thread.sleep(200);
            System.out.println("Green player finished game "+ num);
            // TODO: only one player calls the next line per game
            System.out.println("Game "+ num +" finished");
        // TODO: only one player calls the next line per match
        System.out.println("Match finished");
    }

}

class RedPlayer {



    public void execute() throws InterruptedException {
        System.out.println("Red player ready");
        Thread.sleep(50);
        System.out.println("Red player here");
            // TODO: the following code should be executed 3 times
            System.out.println("Game "+ num +" started");
            Thread.sleep(200);
            System.out.println("Red player finished game "+ num);
            // TODO: only one player calls the next line per game
            System.out.println("Game "+ num +" finished");
        // TODO: only one player calls the next line per match
        System.out.println("Match finished");
    }

}

public class MacauCardTournament {


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