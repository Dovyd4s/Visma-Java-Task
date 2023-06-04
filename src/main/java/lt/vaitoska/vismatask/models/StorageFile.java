package lt.vaitoska.vismatask.models;

import java.util.ArrayList;
import java.util.List;

public class StorageFile {
    private long nextIndex;
    private List<Meeting> meetings;

    public StorageFile() {
        this.nextIndex = 1;
        this.meetings = new ArrayList<>();
    }

    public StorageFile(long nextIndex, List<Meeting> meetings) {
        this.nextIndex = nextIndex;
        this.meetings = meetings;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void addMeeting(Meeting meeting){
        meeting.setId(nextIndex);
        this.meetings.add(meeting);
        nextIndex++;
    }

    public long getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(long nextIndex) {
        this.nextIndex = nextIndex;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }
}
