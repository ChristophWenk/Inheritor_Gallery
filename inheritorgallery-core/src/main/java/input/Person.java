package input;

public class Person {
    private String firstName;
    private String lastName;

    public Person() {
        this.firstName = "Chris";
        this.lastName = "Wenk";
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String sayGreeting() {
        return "Hi, I am " + firstName + " " + lastName;
    }

    public String toString() {
        return firstName + " " + lastName;
    }
}
