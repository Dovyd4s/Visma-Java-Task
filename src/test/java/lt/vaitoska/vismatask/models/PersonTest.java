package lt.vaitoska.vismatask.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    void shouldBeTrueWhenNameAndLastNameMatch() {
        Person person = new Person("John", "Doe", null, null);
        Person person2 = new Person("John", "Doe", null, null);

        Assertions.assertTrue(person.isSamePerson(person2));
    }

    @Test
    void shouldBeTrueWhenNameAndLastNameAreTheSameIgnoringCapitalLetters() {
        Person person = new Person("JOHN", "DOE", null, null);
        Person person2 = new Person("john", "doe", null, null);

        Assertions.assertTrue(person.isSamePerson(person2));
    }
    @Test
    void shouldBeFalseWhenNameAndLastNameAreDifferent() {
        Person person = new Person("JOHN", "DOE", null, null);
        Person person2 = new Person("johnie", "does", null, null);

        Assertions.assertFalse(person.isSamePerson(person2));
    }
    @Test
    void shouldBeFalseWhenNameAndLastNameValuesAreNull() {
        Person person = new Person("JOHN", "DOE", null, null);
        Person person2 = new Person(null, null, null, null);

        Assertions.assertFalse(person.isSamePerson(person2));
    }
}