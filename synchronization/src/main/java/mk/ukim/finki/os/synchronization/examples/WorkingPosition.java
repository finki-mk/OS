package mk.ukim.finki.os.synchronization.examples;

/**
 * @author Riste Stojanov
 */
public class WorkingPosition {

    private String name;

    private Double salary;

    public WorkingPosition(String name, Double salary) {
        this.name = name;
        this.salary = salary;
    }

    public Double getSalary() {
        return salary;
    }    public String getName() {
        return name;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }    public void setName(String name) {
        this.name = name;
    }




}
