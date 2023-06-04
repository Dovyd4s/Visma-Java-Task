package lt.vaitoska.vismatask.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorageFileTest {

    @Test
    void indexShouldIncreaseBy100WhenAdded100NewMeetings() {
        StorageFile storageFile = new StorageFile();
        long first = storageFile.getNextIndex();
        for (int i = 0; i < 100; i++){
            storageFile.addMeeting(new Meeting());
        }
        long result = storageFile.getNextIndex();
        Assertions.assertEquals(100, result-first);
    }
}