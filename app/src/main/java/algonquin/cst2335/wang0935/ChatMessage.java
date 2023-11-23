package algonquin.cst2335.wang0935;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity

public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    long id;

    @ColumnInfo(name="MessageColumn")
    protected String message;
    @ColumnInfo(name="TimeSentColumn")
    protected String timeSent;
    @ColumnInfo(name="SendRecieveColumn")
    protected boolean isSentButton;

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}