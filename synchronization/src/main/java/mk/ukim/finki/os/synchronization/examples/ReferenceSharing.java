package mk.ukim.finki.os.synchronization.examples;

import mk.ukim.finki.os.synchronization.Switcher;

/**
 * @author Riste Stojanov
 */
public class ReferenceSharing {

    public static void main(String[] args) {
        WorkingPosition worker = new WorkingPosition("worker", 12_000D);
        WorkingPosition manager = new WorkingPosition("MANAGER", 60_000D);
        Employee riste = new Employee("Riste", worker);
        Employee trajko = new Employee("Trajko", worker);
        Employee petko = new Employee("Petko", manager);

        riste.start();

        trajko.start();

        petko.start();

        Switcher.forceSwitch(6 * 2_000);

        worker.setSalary(13_000D);


        Switcher.forceSwitch(2_000);
        riste.setSamePositionTo(petko);


        System.out.println("MAIN DONE!");


    }


}
