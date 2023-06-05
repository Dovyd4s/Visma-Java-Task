package lt.vaitoska.vismatask.repositories;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lt.vaitoska.vismatask.models.Meeting;
import lt.vaitoska.vismatask.models.StorageFile;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Repository
public class MeetingRepository {
    private final String FILENAME = "MeetingsDataFile.json";

    public List<Meeting> getAllMeetings() {
        return readMeetingsFromFile(FILENAME).getMeetings();
    }
    public Meeting findById(Long id) {
        return readMeetingsFromFile(FILENAME).getMeetings().stream()
                .filter(s->s.getId()==id).findFirst().orElse(null);
    }
    private void writeMeetingsToFile(StorageFile storageFile, String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try (FileWriter writer = new FileWriter(filePath)) {
            objectMapper.writeValue(writer, storageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private StorageFile readMeetingsFromFile(String filePath) {
        File file = new File(filePath);
        if(!file.exists()){
            writeMeetingsToFile(new StorageFile(), filePath);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try (FileReader reader = new FileReader(filePath)) {
            return objectMapper.readValue(reader, StorageFile.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new StorageFile();
        }
    }

    public Meeting addMeeting(Meeting meeting) {
        StorageFile storageFile = readMeetingsFromFile(FILENAME);

        if(storageFile==null){
            storageFile = new StorageFile();
        }

        storageFile.addMeeting(meeting);
        writeMeetingsToFile(storageFile,FILENAME);
        return meeting;
    }

    public Meeting updateMeeting(Meeting meeting) {
        StorageFile storageFile = readMeetingsFromFile(FILENAME);

        List<Meeting> meetings = storageFile.getMeetings();
        Optional<Meeting> old = meetings.stream().filter(s->s.getId()==meeting.getId()).findFirst();

        if(old.isPresent()){
            meetings.set(meetings.indexOf(old.get()), meeting);
            writeMeetingsToFile(storageFile,FILENAME);
        }
        System.out.println("meeting: "+meeting);
        return meeting;
    }

    public void deleteById(long id) {
        StorageFile storageFile = readMeetingsFromFile(FILENAME);
        Iterator<Meeting> iterator = storageFile.getMeetings().iterator();
        while(iterator.hasNext()){
            if(iterator.next().getId()==id){
                iterator.remove();
                break;
            }
        }
        writeMeetingsToFile(storageFile, FILENAME);
    }
}
