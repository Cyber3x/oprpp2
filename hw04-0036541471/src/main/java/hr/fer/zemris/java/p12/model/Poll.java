package hr.fer.zemris.java.p12.model;

public class Poll {
    private final int id;
    private final String title;
    private final String message;

    public Poll(int id, String title, String message) {
        this.id = id;
        this.title = title;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
