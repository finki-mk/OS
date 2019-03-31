package mk.ukim.finki.os.synchronization.exam18.backup;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * @author Riste Stojanov
 */
public class MainG2 {

  /**
   * Don't change this method
   *
   * @return
   */
  private static final List<DistanceVectorRouter> initNetwork() {
    List<DistanceVectorRouter> routers = new ArrayList<>();

    DistanceVectorRouter first = new DistanceVectorRouter("X1");
    DistanceVectorRouter second = new DistanceVectorRouter("X2", first);
    DistanceVectorRouter third = new DistanceVectorRouter("X3", first);
    routers.add(first);
    routers.add(second);
    routers.add(third);
    for (int i = 3; i < 10; i++) {
      DistanceVectorRouter next = new DistanceVectorRouter("X" + i, first, second, third);
      first = second;
      second = third;
      third = next;
      routers.add(next);
    }
    for (DistanceVectorRouter router : routers) {
      System.out.println(router + "\n\t" + router.neighbors + "\n\t" + router.routingTable);
    }
    return routers;
  }

  /**
   * Finish the todo requirements
   *
   * @param args
   * @throws InterruptedException
   */
  public static void main(String[] args) throws InterruptedException {

    List<DistanceVectorRouter> routers = initNetwork();
    List<Thread> threads = new ArrayList<>();

    for (DistanceVectorRouter router : routers) {
      // todo: run this in background
      Thread t = new Thread(() -> router.keepAliveHartBeat());
      threads.add(t);
      t.start();

      // todo: run this in other thread
      Thread t1 = new Thread(() -> router.handleChangesInBackground());
      threads.add(t1);
      t1.start();
    }

    // todo: wait for 10_000 ms and terminate all routers
    Thread.sleep(10_000);
    for (Thread thread : threads) {
      thread.interrupt();
    }
    System.out.println("DONE!");

  }
}

class DistanceVectorRouter {

  final Set<DistanceVectorRouter> neighbors = new HashSet<>();
  final Map<String, Integer> routingTable = new HashMap<>();
  private final Queue<Map<String, Integer>> advertisements = new LinkedList<>();
  private final String network;
  boolean routingTableChanged = false;
  private Semaphore hasMessage = new Semaphore(0);

  /**
   * Don't change this method
   *
   * @param neighbors
   */
  public DistanceVectorRouter(String network, DistanceVectorRouter... neighbors) {
    this.network = network;
    this.routingTable.put(network, 1);
    for (DistanceVectorRouter neighbor : neighbors) {
      neighbor.neighbors.add(this);
      this.neighbors.add(neighbor);
      this.routingTable.put(neighbor.network, 1);
    }
  }

  /**
   * Don't change this method
   *
   * @param invoker
   */
  public void notifyNeighbors(String invoker) {
    for (DistanceVectorRouter neighbor : neighbors) {
      System.out.println(invoker + " " + network + " notifying " + neighbor.network);
      neighbor.notifyAdvertisement(routingTable);
    }
  }

  public void notifyAdvertisement(Map<String, Integer> routingTable) {
    synchronized (advertisements) {
      advertisements.add(routingTable);
      hasMessage.release();
    }
  }

  /**
   * Don't change this method
   */
  public void keepAliveHartBeat() {
    Thread.currentThread().setName("keep alive [" + network + "]: ");
    try {
      notifyNeighbors("keep alive");
      Thread.sleep(5000);
      notifyNeighbors("keep alive");
    } catch (InterruptedException e) {
      System.out.println(Thread.currentThread().getName() + " interrupted");
    }
  }

  /**
   * TODO: finish this method
   */
  public void handleChangesInBackground() {
    try {
      Thread.currentThread().setName("handle changes [" + network + "]: ");
      while (true) {
        routingTableChanged = false;
        // todo: wait for message from the neighbors
        hasMessage.acquire();
        synchronized (advertisements) {
          // todo: only one call at a given time is allowed for the nextAdvertisement() method
          Map<String, Integer> advertisement = advertisements.poll();

          calculateRoutingTable(advertisement);
        }

        if (routingTableChanged) {
          System.out.println("new routing table: " + routingTable);
          notifyNeighbors("\t\thandle changes");
        }
      }
    } catch (InterruptedException e) {
      System.out.println(Thread.currentThread().getName() + " interrupted");
    }
  }

  /**
   * Don't change this method
   *
   * @param advertisement
   */
  private final void calculateRoutingTable(Map<String, Integer> advertisement) {
    System.out.println(Thread.currentThread().getName() + " calculating routing table");
    synchronized (advertisement) {
      advertisement.forEach((net, price) -> {
        Integer oldPrice = routingTable.get(net);
        if (oldPrice == null || (price + 1) < oldPrice) {
          synchronized (routingTable) {
            routingTable.put(net, price + 1);
          }
          routingTableChanged = true;
        }
      });
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof DistanceVectorRouter)
      return network.equals(((DistanceVectorRouter) obj).network);
    return false;
  }

  @Override
  public String toString() {
    return network;
  }
}
