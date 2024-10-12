package message;

public enum MessageType {
    INCOMING("Входящие"),
    OUTGOING("Исходящие");
    private String type;

    MessageType(String type) {
        this.type = type;
    }
}