# Criminal Transport

Потребно е да извршите симулација за пренесување на затвореници со автомобил. Автомобилите вршат превоз на точно 4 патници, 
при што не е дозволено заедно да се возат еден полицаец и 3 затвореници. Сите други комбинации се дозволени.  
Превозот се се одвива според следните правила: 

 - Не е дозволено да влезат еден полицаец и 3 затвореници во автомобилот 
 - Автомобилот врши превоз кога ќе се соберат точно 4 патници
 - Кога ќе се соберат 4 патници, еден од полицајците треба да издаде команда за тргнување преку печатење на пораката  
 `Start driving.`
 - Слегувањето од автомобилот е дозволено откако еден од полицајците ќе ја издаде соодветната команда преку печатење на 
 пораката `Arrived.`
 - Секој се вози само еднаш
 - Потоа сценариото може да започне од почеток


Во почетниот код кој е даден, дефинирани се класите `Policeman` и `Criminal`, кои го симболизираат однесувањето на 
полицајците и затворениците.  


Главниот метод `CriminalTransport.main` треба да го стартува по 60 `Policeman` и `Criminal` thread-ови. Потоа `main` методот 
треба да чека најмногу 1000ms за секој `Policeman` и `Criminal` thread да заврши. Доколку некој не заврши за ова време, 
треба да се прекине и да се испише порака `Terminated transport`, а во спротивен случај да се испише `Finished transport`. 


Класите `Policeman` и `Criminal` треба да се Thread, кои во позадина го извршуваат `execute` методот само еднаш.

Вашата задача е да го дополните дадениот код според барањата на задачата, при што треба да внимавате 
не настане Race Condition и Deadlock.

```java
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Semaphore;

class Policeman {

  public void execute() throws InterruptedException {
    // waits until it is valid to enter the car
    System.out.println("Policeman enters in the car");

    // when the four passengers are inside, one policeman prints the starting command
    System.out.println("Start driving.");
    Thread.sleep(100);
    // one policeman prints the this command to notice that everyone can exit
    System.out.println("Arrived.");
    // the exit from the car is allowed after the "Arrived." message is printed
    System.out.println("Policeman exits from the car");
  }

}

class Criminal {

  public void execute() throws InterruptedException {
    // waits until it is valid to enter the car
    System.out.println("Criminal enters in the car");

    Thread.sleep(100);
    // the exit from the car is allowed after the "Arrived." message is printed
    System.out.println("Criminal exits from the car");
  }
}

public class CriminalTransport {


  public static void main(String[] args) throws InterruptedException {
    HashSet<Thread> threads = new HashSet<Thread>();
    for (int i = 0; i < 60; i++) {
      Policeman red = new Policeman();
      threads.add(red);
      Criminal green = new Criminal();
      threads.add(green);
    }
    // run all threads in background

    // after all of them are started, wait each of them to finish for maximum 1_000 ms

    // for each thread, terminate it if it is not finished

  }

}
``` 