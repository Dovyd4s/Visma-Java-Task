package lt.vaitoska.vismatask.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Meeting {
    private long id;
    private String name;
    private Person responsiblePerson;
    private List<Person> persons;
    private String description;
    private MeetingCategory meetingCategory;
    private MeetingType meetingType;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public Meeting() {
    }

    public boolean personExists (Person person){
        if(responsiblePerson!=null){
            if(person.isSamePerson(responsiblePerson))
                return true;
        }
        if (persons != null) {
            for (Person p : persons) {
                if (person.isSamePerson(p)) {
                    return true;
                }
            }
        }
        return false;
    }
    public Meeting(long id, String name, Person responsiblePerson, List<Person> persons, String description, MeetingCategory meetingCategory, MeetingType meetingType, LocalDateTime startDate, LocalDateTime endDate) {
        this.id = id;
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.persons = persons;
        this.description = description;
        this.meetingCategory = meetingCategory;
        this.meetingType = meetingType;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Meeting(String name, Person responsiblePerson, List<Person> persons, String description, MeetingCategory meetingCategory, MeetingType meetingType, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.responsiblePerson = responsiblePerson;
        this.persons = persons;
        this.description = description;
        this.meetingCategory = meetingCategory;
        this.meetingType = meetingType;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Person getResponsiblePerson() {
        return responsiblePerson;
    }

    public void setResponsiblePerson(Person responsiblePerson) {
        this.responsiblePerson = responsiblePerson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MeetingCategory getMeetingCategory() {
        return meetingCategory;
    }

    public void setMeetingCategory(MeetingCategory meetingCategory) {
        this.meetingCategory = meetingCategory;
    }

    public MeetingType getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(MeetingType meetingType) {
        this.meetingType = meetingType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", responsiblePerson=" + responsiblePerson +
                ", description='" + description + '\'' +
                ", meetingCategory=" + meetingCategory +
                ", meetingType=" + meetingType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
