package lt.vaitoska.vismatask.services;

import lt.vaitoska.vismatask.models.Meeting;
import lt.vaitoska.vismatask.models.MeetingCategory;
import lt.vaitoska.vismatask.models.MeetingType;
import lt.vaitoska.vismatask.models.Person;
import lt.vaitoska.vismatask.repositories.MeetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;
    private MeetingService meetingServiceTest;

    @BeforeEach
    void setUp(){
        meetingServiceTest = new MeetingService(meetingRepository);
        Person person1 = new Person("John", "Doe", LocalDateTime.of(2023, 6, 5, 10,0), LocalDateTime.of(2023, 6, 5, 13,0));
        Person person2 = new Person("Tom", "Dough", LocalDateTime.of(2023, 6, 6, 10,0), LocalDateTime.of(2023, 6, 5, 13,0));

        Meeting meeting1 = new Meeting(1,"NameOne", null, null, "description", MeetingCategory.HUB_SHORT, MeetingType.LIVE, LocalDateTime.of(2023, 6, 5, 10,0), LocalDateTime.of(2023, 6, 5, 13,0));
        Meeting meeting2 = new Meeting(2,"NameTwo", null, null, "java meeting", MeetingCategory.CODE_MONKEY, MeetingType.IN_PERSON, LocalDateTime.of(2023, 6, 6, 10,0), LocalDateTime.of(2023, 6, 6, 13,0));

        meeting1.setResponsiblePerson(person1);
        meeting1.setPersons(new ArrayList<>(List.of(person2)));

        meeting2.setResponsiblePerson(person2);
        meeting2.setPersons(new ArrayList<>(List.of(person1)));

        List<Meeting> meetings = new ArrayList<>(List.of(meeting1, meeting2));
        lenient().when(meetingRepository.getAllMeetings()).thenReturn(meetings);
        lenient().when(meetingRepository.findById(1L)).thenReturn(meeting1);
    }
    @Test
    void shouldReturnListOf2Meetings() {
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, null, null, null, null, null);
        verify(meetingRepository).getAllMeetings();

        assertEquals(2, meetings.size());
    }

    @Test
    void shouldReturnMeetingFilteredByDescription() {
        String description = "jAva";
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(description, null, null, null, null, null, null, null, null);
        verify(meetingRepository).getAllMeetings();

        for(Meeting meeting : meetings){
            assertTrue(meeting.getDescription().toUpperCase().contains(description.toUpperCase()));
        }
    }
    @Test
    void shouldReturnFalseFilteredByDescription() {
        String description = "jAvasctipt";
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(description, null, null, null, null, null, null, null, null);
        verify(meetingRepository).getAllMeetings();

        for(Meeting meeting : meetings){
            assertFalse(meeting.getDescription().toUpperCase().contains(description.toUpperCase()));
        }
    }
    @Test
    void shouldReturnMeetingFilteredByResponsiblePrsnName() {
        String responsiblePName = "JohN";
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, responsiblePName, null, null, null, null, null, null, null);
        verify(meetingRepository).getAllMeetings();

        for(Meeting meeting : meetings){
            assertTrue(meeting.getResponsiblePerson().getName().toUpperCase().contains(responsiblePName.toUpperCase()));
        }
    }
    @Test
    void shouldReturnMeetingFilteredByResponsiblePrsnLastName() {
        String responsiblePLastName = "Dough";
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, responsiblePLastName, null, null, null, null, null, null);
        verify(meetingRepository).getAllMeetings();

        for(Meeting meeting : meetings){
            assertTrue(meeting.getResponsiblePerson().getLastName().toUpperCase().contains(responsiblePLastName.toUpperCase()));
        }
        assertEquals(1, meetings.size());
    }
    @Test
    void shouldReturnMeetingFilteredByCategory() {
        String meetingCategory = String.valueOf(MeetingCategory.CODE_MONKEY);
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, meetingCategory, null, null, null, null, null);
        verify(meetingRepository).getAllMeetings();

        for(Meeting meeting : meetings){
            assertEquals(meetingCategory, meeting.getMeetingCategory().toString());
        }
        assertEquals(1, meetings.size());
    }
    @Test
    void shouldReturnMeetingFilteredByType() {
        String meetingType = String.valueOf(MeetingType.IN_PERSON);
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, meetingType, null, null, null, null);
        verify(meetingRepository).getAllMeetings();

        for(Meeting meeting : meetings){
            assertEquals(meetingType, meeting.getMeetingType().toString());
        }
        assertEquals(1, meetings.size());
    }
    @Test
    void shouldReturn2MeetingsFilteredByDateFrom() {
        LocalDate starDate = LocalDate.of(2020, 6, 5);
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, null, starDate, null, null, null);
        verify(meetingRepository).getAllMeetings();

        for(Meeting meeting : meetings){
            assertTrue(meeting.getStartDate().isAfter(starDate.atStartOfDay().minusNanos(1)));
        }
        assertEquals(2, meetings.size());
    }
    @Test
    void shouldReturn1MeetingFilteredByDateFrom() {
        LocalDate starDate = LocalDate.of(2023, 6, 6);
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, null, starDate, null, null, null);
        verify(meetingRepository).getAllMeetings();

        for(Meeting meeting : meetings){
            assertTrue(meeting.getEndDate().isAfter(starDate.atStartOfDay().minusNanos(1)));
        }
        assertEquals(1, meetings.size());
    }
    @Test
    void shouldReturn0MeetingsFilteredByDateFrom() {
        LocalDate starDate = LocalDate.of(2023, 6, 7);
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, null, starDate, null, null, null);
        verify(meetingRepository).getAllMeetings();

        assertEquals(0, meetings.size());
    }
    @Test
    void shouldReturn2MeetingsFilteredByDateTo() {
        LocalDate endDate = LocalDate.of(2023, 6, 6);
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, null, null, endDate, null, null);
        verify(meetingRepository).getAllMeetings();

        for(Meeting meeting : meetings){
            assertTrue(meeting.getStartDate().isBefore(endDate.plusDays(1).atStartOfDay()));
        }
        assertEquals(2, meetings.size());
    }
    @Test
    void shouldReturn1MeetingFilteredByDateTo() {
        LocalDate endDate = LocalDate.of(2023, 6, 5);
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, null, null, endDate, null, null);
        verify(meetingRepository).getAllMeetings();

        for(Meeting meeting : meetings){
            assertTrue(meeting.getStartDate().isBefore(endDate.plusDays(1).atStartOfDay()));
        }
        assertEquals(1, meetings.size());
    }
    @Test
    void shouldReturn0MeetingsFilteredByDateTo() {
        LocalDate endDate = LocalDate.of(2023, 6, 4);
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, null, null, endDate, null, null);
        verify(meetingRepository).getAllMeetings();

        assertEquals(0, meetings.size());
    }
    @Test
    void shouldReturn2MeetingsFilteredByMinAttendees() {
        int minAttendees = 0;
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, null, null, null, minAttendees, null);
        verify(meetingRepository).getAllMeetings();

        assertEquals(2, meetings.size());
    }
    @Test
    void shouldReturnNoMeetingsFilteredByMinAttendees() {
        int minAttendees = 3;
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, null, null, null, minAttendees, null);
        verify(meetingRepository).getAllMeetings();

        assertEquals(0, meetings.size());
    }
    @Test
    void shouldReturn2MeetingsFilteredByMaxAttendees() {
        int maxAttendees = 3;
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, null, null, null, null, maxAttendees);
        verify(meetingRepository).getAllMeetings();

        assertEquals(2, meetings.size());
    }
    @Test
    void shouldReturnNoMeetingsFilteredByMaxAttendees() {
        int maxAttendees = 0;
        List<Meeting> meetings = meetingServiceTest.getFilteredMeetings(null, null, null, null, null, null, null, null, maxAttendees);
        verify(meetingRepository).getAllMeetings();

        assertEquals(0, meetings.size());
    }

    //Other methods

    @Test
    void shouldCallMeetingRepositoryFindById() {
        assertEquals(1, meetingServiceTest.getMeetingById(1).getId());
        verify(meetingRepository).findById(1L);
    }

    @Test
    void shouldReturnBadRequestWhenDeletingNullPerson() {
        HttpStatus status = meetingServiceTest.deletePerson(1L,null);

        verifyNoInteractions(meetingRepository);
        assertEquals(HttpStatus.BAD_REQUEST, status);
    }
    @Test
    void shouldNotDeleteResponsiblePersonFromMeeting() {
        Person person = meetingServiceTest.getMeetingById(1L).getResponsiblePerson();
        HttpStatus status = meetingServiceTest.deletePerson(1L, person);

        verify(meetingRepository, never()).updateMeeting(any(Meeting.class));
        assertEquals(HttpStatus.FORBIDDEN, status);
    }
    @Test
    void shouldDeletePersonFromMeeting() {
        Person person = meetingServiceTest.getMeetingById(1L).getPersons().get(0);
        HttpStatus status = meetingServiceTest.deletePerson(1L, person);

        ArgumentCaptor<Meeting> captor = ArgumentCaptor.forClass(Meeting.class);

        verify(meetingRepository).updateMeeting(captor.capture());
        assertFalse(captor.getValue().getPersons().contains(person));
        assertEquals(HttpStatus.NO_CONTENT, status);
    }
    //should be continued...
}