package hr.fer.zemris.java.p12.model;

import java.util.Objects;

public final class PollOption {
    private final int id;
    private final String title;
    private final String link;
    private final int pollId;
    private final int likeCount;
    private final int dislikeCount;

    public PollOption(int id, String title, String link, int pollId, int likeCount, int dislikeCount) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.pollId = pollId;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public int getPollId() {
        return pollId;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }
}
