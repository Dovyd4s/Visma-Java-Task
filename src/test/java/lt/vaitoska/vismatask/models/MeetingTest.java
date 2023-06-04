package lt.vaitoska.vismatask.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MeetingTest {

    @Test
    void shouldBeFalseWhenPersonDoesNotExist() {
        Meeting meeting = new Meeting();
        Person person = new Person("John", "Doe", null, null);
        Person person2 = new Person("Johnie", "Doe", null, null);
        meeting.setPersons(new ArrayList<>());
        meeting.getPersons().add(person);
        Assertions.assertFalse(meeting.personExists(person2));
    }

    @Test
    void shouldBeTrueWhenPersonExistsInPersons() {
        Meeting meeting = new Meeting();
        Person person = new Person("John", "Doe", null, null);
        Person person2 = new Person("John", "Doe", null, null);
        meeting.setPersons(new ArrayList<>());
        meeting.getPersons().add(person);
        Assertions.assertTrue(meeting.personExists(person2));
    }

    @Test
    void shouldBeTrueWhenPersonExistsAsResponsiblePerson() {
        Meeting meeting = new Meeting();
        Person person = new Person("John", "Doe", null, null);
        Person person2 = new Person("John", "Doe", null, null);
        meeting.setResponsiblePerson(person);
        Assertions.assertTrue(meeting.personExists(person2));
    }
}