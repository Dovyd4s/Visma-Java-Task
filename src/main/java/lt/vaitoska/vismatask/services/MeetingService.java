package lt.vaitoska.vismatask.services;

import lt.vaitoska.vismatask.models.Meeting;
import lt.vaitoska.vismatask.models.Person;
import lt.vaitoska.vismatask.models.ResourseAndMessage;
import lt.vaitoska.vismatask.repositories.MeetingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

@Service
public class MeetingService {
    private final MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public List<Meeting> getMeetings(String description, String responsiblePersonName, String responsiblePersonLastName, String category, String type, LocalDate fromDate, LocalDate toDate, Integer minAttendees, Integer maxAttendees) {
        List<Meeting> meetings = meetingRepository.getAllMeetings();

        if(description != null){
            meetings.removeIf(s->!s.getDescription().toUpperCase().contains(description.toUpperCase()));
        }
        if(responsiblePersonName != null){
            meetings.removeIf(s->!s.getResponsiblePerson().getName().equalsIgnoreCase(responsiblePersonName));
        }
        if(responsiblePersonLastName != null){
            meetings.removeIf(s->!s.getResponsiblePerson().getLastName().equalsIgnoreCase(responsiblePersonLastName));
        }
        if(category != null){
            meetings.removeIf(s->!s.getMeetingCategory().toString().equalsIgnoreCase(category));
        }
        if(type != null){
            meetings.removeIf(s->!s.getMeetingType().toString().equalsIgnoreCase(type));
        }
        if(fromDate != null){
            meetings.removeIf(s->s.getEndDate().isBefore(fromDate.atStartOfDay()));
        }
        if(toDate != null){
            meetings.removeIf(s->s.getStartDate().isAfter(toDate.atTime(23,59,59)));
        }
        if(minAttendees != null){
            meetings.removeIf(s->s.getPersons().size()<minAttendees);
        }
        if(maxAttendees != null){
            meetings.removeIf(s->s.getPersons().size()>maxAttendees);
        }

        return meetings;
    }

    public Meeting getMeetingById(long id) {
        return meetingRepository.finById(id);
    }
    public HttpStatus deletePerson(long meetingId, Person person) {
        boolean found = false;
        Meeting meeting = meetingRepository.finById(meetingId);
        if(meeting == null){
            return HttpStatus.NOT_FOUND;
        }
        if(meeting.getResponsiblePerson().isSamePerson(person)){
            return HttpStatus.FORBIDDEN;
        }else {
            Iterator<Person> iterator = meeting.getPersons().iterator();
            while (iterator.hasNext()){
                if(iterator.next().isSamePerson(person)){
                    iterator.remove();
                    found = true;
                    break;
                }
            }
            meetingRepository.updateMeeting(meeting);
        }
        if(found){
            return HttpStatus.NO_CONTENT;
        }else{
            return HttpStatus.NOT_FOUND;
        }
    }
    public Meeting saveMeeting(Meeting meeting) {
        return meetingRepository.addMeeting(meeting);
    }

    public ResourseAndMessage<Meeting> addPerson(long meetingId, Person person) {
        ResourseAndMessage<Meeting> resourseAndMessage = new ResourseAndMessage<>();
        List<Meeting> allMeetings = new ArrayList<>(meetingRepository.getAllMeetings());
        Meeting meetingById = allMeetings.stream().filter(s->s.getId()==meetingId).findFirst().orElse(null);
        if(meetingById==null){
            resourseAndMessage.setHttpStatus(HttpStatus.NOT_FOUND);
            return resourseAndMessage;
        }

        List<Meeting> intersectingMeetings = getIntersectingMeetings(person, allMeetings);

        if(meetingById.personExists(person)){
            resourseAndMessage.setHttpStatus(HttpStatus.CONFLICT);
            return resourseAndMessage;
        }

        if(meetingById.getPersons()==null){
            meetingById.setPersons(new ArrayList<>());
        }
        meetingById.getPersons().add(person);

        resourseAndMessage.setHttpStatus(HttpStatus.CREATED);
        resourseAndMessage.setResource(meetingRepository.updateMeeting(meetingById));

        if(intersectingMeetings.size()>0){
            StringBuilder stringBuilder = new StringBuilder("Intersecting meetings:");
            intersectingMeetings.forEach(s-> stringBuilder.append(" ").append(s.toString()));
            resourseAndMessage.setMessage(stringBuilder.toString());
        }
        return resourseAndMessage;
    }

    private List<Meeting> getIntersectingMeetings(Person person, List<Meeting> allMeetings) {
        List<Meeting> intersectingMeetings = new ArrayList<>();
        for (Meeting meeting : allMeetings) {
            if(meeting.getResponsiblePerson().isSamePerson(person)){
                if(timesIntersects(meeting.getStartDate(), meeting.getEndDate(), person.getAttendingTimeBeginning(), person.getAttendingTimeEnd())){
                    intersectingMeetings.add(meeting);
                }
            }
            if (meeting.getPersons() != null) {
                for (Person p : meeting.getPersons()) {
                    if (person.isSamePerson(p)) {
                        if (timesIntersects(meeting.getStartDate(), meeting.getEndDate(), person.getAttendingTimeBeginning(), person.getAttendingTimeEnd())) {
                            intersectingMeetings.add(meeting);
                        }
                    }
                }
            }
        }
        return intersectingMeetings;
    }

    private boolean timesIntersects(LocalDateTime aStart, LocalDateTime aEnd, LocalDateTime bStart, LocalDateTime bEnd) {
        if(aStart != null && aEnd != null && bStart != null && bEnd != null){
            boolean dontIntersect = aStart.isAfter(bEnd) || aEnd.isBefore(bStart);
            return !dontIntersect;
        }
        return false;
    }


    public HttpStatus deleteMeeting(long id, Person person) {
        Meeting meeting = meetingRepository.finById(id);
        if(meeting == null){
            return HttpStatus.NOT_FOUND;
        }
        if(meeting.getResponsiblePerson().isSamePerson(person)){
            meetingRepository.deleteById(id);
        }else{
            return HttpStatus.FORBIDDEN;
        }
        return HttpStatus.NO_CONTENT;
    }
}
