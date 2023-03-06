package Chat.Message;

import java.io.Serializable;

public class Message implements Serializable{
    protected static final long serialVersionUID = 1L;
    private String content;

    public Message() {}

    public Message(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}