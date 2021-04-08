import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private Date sendAt;
    private String author;
    private String message;
    private String[] command;

    public static Message of(String author, String message, String[] command) {
        Message m = new Message();
        m.setAuthor(author);
        m.setMessage(message);
        m.setSendAt(new Date());
        m.setCommand(command);
        return m;
    }

    public Message() {
    }

    public Date getSendAt() {
        return sendAt;
    }

    public void setSendAt(Date sendAt) {
        this.sendAt = sendAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setCommand(String[] command) {
        this.command = command;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String[] getCommand() {
        return command;
    }

    @Override
    public String toString() {
        return "[" + sendAt +
                "](" + author + "):" + message;
    }
}