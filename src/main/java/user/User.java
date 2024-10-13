package user;

import message.Message;
import message.MessageType;

import java.util.*;

public class User {
    private final String name;
    private final String password;
    private HashMap<MessageType, ArrayList<Message>> messages;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        messages = new HashMap<>();
        messages.put(MessageType.INCOMING, new ArrayList<>());
        messages.put(MessageType.OUTGOING, new ArrayList<>());

    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Map<MessageType, ArrayList<Message>> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", messages=" + messages +
                '}';
    }
}
