package mk.ukim.finki.os.synchronization.examples;

import mk.ukim.finki.os.synchronization.Switcher;

/**
 * @author Riste Stojanov
 */
public class Employee extends Thread {

    private String firstName;


    private WorkingPosition position;

    public Employee(String firstName, WorkingPosition position) {
        this.firstName = firstName;
        this.position = position;
    }


    public void run() {
        for (int i = 0; i < 12; i++) {
            System.out.println(i + "[" + this.firstName + "] salary: " + this.position.getSalary() + "@" + this.position.getName());
            Switcher.forceSwitch(2_000);
        }

    }

    public WorkingPosition getPosition() {
        return position;
    }

    public void setPosition(WorkingPosition position) {
        this.position = position;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSamePositionTo(Employee other) {
        other.setPosition(this.position);
    }
}
