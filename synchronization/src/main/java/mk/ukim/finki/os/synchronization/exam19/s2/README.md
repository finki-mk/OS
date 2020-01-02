Volleyball tournament
===

Потребно е да направите систем за синхронизација на турнир во одбојка, кој се одржува според следните правила:

На турнирот учествуваат 60 одбојкари, кои произволно се групираат во тимови. Во салата истовремено може да влезат 
најмногу 12 играчи. По влегувањето во салата, секој одбојкар треба да испечати `Player inside.`. Потоа одбојкарите треба
да се пресоблечат за што имаат на располагање кабина со капацитет 4, односно може да се пресоблекуваат 4 одбојкари во 
исто време. При влегувањето во соблекувалната, треба да се испечати `In dressing room.`. По пресоблекувањето, играчите 
се чекаат меѓусебно. Откако сите ќе завршат со пресоблекувањето, започнуваат со натпреварот, при што сите печатат 
`Game started.`. Откако ќе заврши натпреварот, сите печатат `Player done.`, а последниот го повикува печати `Game finished.`,
со што означува дека салата е слободна. Потоа, во салата може да влезат нови 12 играчи и да започне нов натпревар.

Во почетниот код кој е даден, дефинирани се класите `VolleyballTournament` и `Player`. Во `main` методот од класата `VolleyballTournament` потребно
е да стартувате `60` играчи, кои се репрезентирани преку класата `Player`. Потоа секој од играчите треба да започне да го 
извршува претходно дефинираното сценарио во позадина. Однесувањето на играчите треба да го дефинрате во `execute` методот 
од `Payer` класата, кој треба да се извршува паралелно кај сите играчи. По стартувањето на сите играчи, во `main` треба да се
чека секој од играчите да заврши за 2 секунди (2000 ms). Доколку некој од играчите не заврши за 2 секунди, треба да се 
испечати `Possible deadlock!` и да се терминира, а доколку сите играчи завршиле во предвиденото време, да се испечати 
`Tournament finished.`.  


Вашата задача е да го дополните дадениот код според барањата на задачата, при што треба да внимавате 
не настане Race Condition и Deadlock.

```java
import java.util.HashSet;
import java.util.concurrent.Semaphore;

public class VolleyballTournament {

    public static void main(String[] args) {
        HashSet<Player> threads = new HashSet<>();
        for (int i = 0; i < 60; i++) {
            Player p = new Player();
            threads.add(p);
        }
        // run all threads in background

        // after all of them are started, wait each of them to finish for maximum 2_000 ms

        // for each thread, terminate it if it is not finished
        System.out.println("Possible deadlock!");
        System.out.println("Tournament finished.");

    }
}

class Player {
    
    public void execute() throws InterruptedException {
        // at most 12 players should print this in parallel
        System.out.println("Player inside.");
        // at most 4 players may enter in the dressing room in parallel
        System.out.println("In dressing room.");
        Thread.sleep(10);// this represent the dressing time
        // after all players are ready, they should start with the game together
        System.out.println("Game started.");
        Thread.sleep(100);// this represent the game duration
        System.out.println("Player done.");
        // only one player should print the next line, representing that the game has finished
        System.out.println("Game finished.");
    }
}
```  


