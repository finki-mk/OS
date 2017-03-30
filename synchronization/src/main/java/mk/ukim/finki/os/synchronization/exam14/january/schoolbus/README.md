School Bus
====

Од ФИНКИ ве ангажираат за синхронизација на процесот на пренос на студенти на екскурзија, каде повеќе возачи ќе пренесуваат студенти со ист автобус. Екскурзијата се изведува во повеќе термини, каде во секој термин мора да има присутно еден возач и 50 студенти. По завршувањето на терминот, од автобусот прво излегуваат студентите и возачот, а потоа влегува нов возач и нови 50 студенти. 

Притоа важат следните услови:

 - Во автобусот може да има **само еден возач** и **точно 50 студенти**. 
 - Студентите не смеат да влезат ако во автобусот нема возач
 - Студентите не смеат да излезат додека возачот не каже дека автобусот пристигнал
 - Возачот не може да излезе додека има студенти во автобусот
 - Автобусот иницијално е празен

Вашата задача е да го синхронизирате претходното сценарио. 

Во почетниот код кој е даден, дефинирани се класите `Driver` и `Student`, кои го симболизираат однесувањето на возачите и студентите, соодветно. Има повеќе инстанци од двете класи кај кои методот `execute()` се повикува само еднаш.

Во имплементацијата, можете да ги користите следните методи од веќе дефинираната променлива `state`:

- `state.driverEnter()`
    - Означува дека возачот влегува во автобусот. 
    - Се повикува од сите возачи.
    - Доколку автобусот не е празен во моментот на повикувањето, ќе се јави исклучок.
 - `state.studentEnter()`
    - Означува дека студентот влегува во автобусот. 
    - Се повикува од сите студенти.
    - Доколку нема возач во автобусот (претходно не е повикан `state.driverEnter()`), или има повеќе од 50 студенти внатре, ќе се јави исклучок.
    - Доколку студентите не влегуваат паралелно (повеќе истовремено), ќе јави исклучок.
 - `state.busDeparture()`
    - Го симболизира тргнувањето на автобусот.  
    - Се повикува од сите возачи по влегувањето на сите 50 студенти.
    - Доколку нема 50 присутни студенти во автобусот, ќе се јави исклучок. 
 - `state.busArrive()`
    - Го симболизира пристигнувањето на автобусот. 
    - Се повикува од сите возачи. 
    - Доколку претходно не е повикан `state.busDeparture()`, ќе јави исклучок. 
 - `state.studentLeave()`
    - Го симболизира излегувањето на студентот од автобусот.  
    - Се повикува од сите студенти. 
    - Доколку се повика пред `state.busArrive()`, или ако претходно излегол возачот, ќе се јави исклучок. 
- `state.driverLeave()`
    - Го симболизира излегувањето на возачот од автобусот.  
    - Се повикува од сите возачи.
    - Доколку методот се повика, а сеуште има студенти во автобусот, ќе добиете порака за грешка.


За решавање на задачата, преземете го проектот со клик на копчето `Starter file`, отпакувајте го и отворете го со Eclipse или Netbeans.

Претходно назначените методи служат за проверка на точноста на сценариото и не смеат да бидат променети и мораат да бидат повикани.  

Вашата задача е да го имплементирате методот `execute()` од класите `Driver` и `Student`, кои се наоѓаат во датотеката `SchoolBusSynchronization.java`. При решавањето можете да користите семафори и монитори по ваша желба и нивната иницијализација треба да ја направите во `init()` методот.

При стартувањето на класата, сценариото ќе се повика 10 пати, со креирање на голем број инстанци од класата `Driver` и една инстанца од класата `Student`, кај кои паралелно само еднаш ќе се повика нивниот execute() метод.

**Напомена:** Поради конкурентниот пристап за логирањето, можно е некои од пораките да не се на позицијата каде што треба да се. Токму затоа, овие пораки користете ги само како информација.