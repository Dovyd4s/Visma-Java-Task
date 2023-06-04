package lt.vaitoska.vismatask.models;

import java.time.LocalDateTime;

public class Person {
    private String name;
    private String lastName;
    private LocalDateTime attendingTimeBeginning;
    private LocalDateTime attendingTimeEnd;

    public Person() {
    }

    public boolean isSamePerson(Person other){
        return this.name.equalsIgnoreCase(other.getName()) && this.lastName.equalsIgnoreCase(other.getLastName());
    }
    public Person(String name, String lastName, LocalDateTime attendingTimeBeginning, LocalDateTime attendingTimeEnd) {
        this.name = name;
        this.lastName = lastName;
        this.attendingTimeBeginning = attendingTimeBeginning;
        this.attendingTimeEnd = attendingTimeEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDateTime getAttendingTimeBeginning() {
        return attendingTimeBeginning;
    }

    public void setAttendingTimeBeginning(LocalDateTime attendingTimeBeginning) {
        this.attendingTimeBeginning = attendingTimeBeginning;
    }

    public LocalDateTime getAttendingTimeEnd() {
        return attendingTimeEnd;
    }

    public void setAttendingTimeEnd(LocalDateTime attendingTimeEnd) {
        this.attendingTimeEnd = attendingTimeEnd;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", attendingTimeBeginning=" + attendingTimeBeginning +
                ", attendingTimeEnd=" + attendingTimeEnd +
                '}';
    }
}
