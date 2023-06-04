package lt.vaitoska.vismatask.controllers;

import lt.vaitoska.vismatask.models.Meeting;
import lt.vaitoska.vismatask.models.Person;
import lt.vaitoska.vismatask.models.ResourseAndMessage;
import lt.vaitoska.vismatask.services.MeetingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/meetings")
public class MeetingController {
    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping
    public List<Meeting> getMeetings(
            @RequestParam(required = false, name = "description") String description,
            @RequestParam(required = false, name = "responsiblePersonName") String responsiblePersonName,
            @RequestParam(required = false, name = "responsiblePersonLastName") String responsiblePersonLastName,
            @RequestParam(required = false, name = "category") String category,
            @RequestParam(required = false, name = "type") String type,
            @RequestParam(required = false, name = "fromDate")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
            @RequestParam(required = false, name = "toDate")
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
            @RequestParam(required = false, name = "numberOfAttendeesMin") Integer minAttendees,
            @RequestParam(required = false, name = "numberOfAttendeesMax") Integer maxAttendees) {

        return meetingService.getFilteredMeetings(description, responsiblePersonName, responsiblePersonLastName, category, type, fromDate, toDate, minAttendees, maxAttendees);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Meeting> getMeetingById(@PathVariable long id){
        Meeting meeting = meetingService.getMeetingById(id);
        if(meeting == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(meeting);
    }

    @PostMapping
    public ResponseEntity<Meeting> addMeeting(@RequestBody Meeting meeting){
        meetingService.saveMeeting(meeting);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(meeting.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{id}/persons")
    public ResponseEntity<String> addPerson(@PathVariable long id, @RequestBody Person person){
        ResourseAndMessage<Meeting> resourseAndMessage = meetingService.addPerson(id, person);

        if (resourseAndMessage.getMessage() != null) {
            return ResponseEntity.status(resourseAndMessage.getHttpStatus()).body(resourseAndMessage.getMessage());
        }
        return ResponseEntity.status(resourseAndMessage.getHttpStatus()).build();
    }

    @DeleteMapping("/{id}/persons")
    public ResponseEntity<Void> deletePerson(@PathVariable long id, @RequestBody Person person){
        return ResponseEntity.status(meetingService.deletePerson(id, person)).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable long id, @RequestBody Person person){
        return ResponseEntity.status(meetingService.deleteMeeting(id, person)).build();
    }
}
