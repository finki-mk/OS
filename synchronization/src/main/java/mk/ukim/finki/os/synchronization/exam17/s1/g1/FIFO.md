Shortest Job First Scheduling
============================

Потребно е да го имплементирате алгоритмот за распределување на процеси First In First Out, 
со дефининираните класи `Scheduler` и `Process` кои треба да функционираат како thread-ови.  

Главниот метод `Scheduler.main`  треба да стартува 100 нови `Process` thread-ови , да ги регистрира со 
`Scheduler.register(Process p)` методот и потоа да го стартува `Scheduler` thread-от. Потоа треба да го чека 
`Scheduler` thread-от да заврши најмногу 20000ms. Доколку не заврши за ова време, треба да 
се прекине и да се испише порака `Terminated scheduling`, а во спротивен случај да се испише 
`Finished scheduling`. 


Секој од `Process` thread-овите веднаш по стартувањето во позадина треба да заспие рандом време со
`Thread.sleep(this.duration)`. Во методот `Process.execute` треба да се стартува позадинското 
извршување на `Process` thread-от. 

Класата `Scheduler` е Thread, кој во позадина извршува циклус се додека има елемент ви `scheduled` листата. 
Во овој циклус прво заспива 100ms, па го зема и го извршува наредниот процес `next().execute()`. Извршувањето 
на циклусот не продолжува додека `Process` thread-от не заврши со своето извршување. 

Вашата задача е да го дополните дадениот код според барањата на задачата, при што треба да внимавате 
не настане Race Condition и Deadlock. 

```
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Scheduler {
  public static Random random = new Random();
  static List<Process> scheduled = new ArrayList<>();

  public static void main(String[] args) throws InterruptedException {
    // TODO: kreirajte 100 Process thread-ovi i registrirajte gi
    
    // TODO: kreirajte Scheduler i startuvajte go negovoto pozadinsko izvrsuvanje
    
    // TODO: Cekajte 20000ms za Scheduler-ot da zavrsi
    
    // TODO: ispisete go statusot od izvrsuvanjeto
  }

  public static void register(Process process) {
    scheduled.add(process);
  }

  public Process next() {
    if (!scheduled.isEmpty()) {
      return scheduled.remove(0);
    }
    return null;
  }

  public void run() {
    try {
          while (!scheduled.isEmpty()) {
            Thread.sleep(100);
            System.out.print(".");
    
            // TODO: zemete go naredniot proces
            
            // TODO: povikajte go negoviot execute() metod
            
            // TODO: cekajte dodeka ne zavrsi negovoto pozadinsko izvrsuvanje            
    
          }
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println("Done scheduling!");
      }
  }
  
}


class Process {

  public Integer duration;

  public Process() throws InterruptedException {
    this.duration = Scheduler.random.nextInt(1000);
  }


  public void execute() {
    System.out.println("Executing[" + this + "]: " + duration);
    // TODO: startuvajte go pozadinskoto izvrsuvanje
  }
}

```

