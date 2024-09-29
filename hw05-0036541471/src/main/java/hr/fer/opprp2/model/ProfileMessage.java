package hr.fer.opprp2.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "profile_message")
public class ProfileMessage {
    private Long id;
    private Date createdAt;
    private String messageText;
    private BlogUser creator;
    private BlogUser target;

    public ProfileMessage() {}

    public ProfileMessage(String messageText, BlogUser creator, BlogUser target) {
        this.messageText = messageText;
        this.createdAt = new Date();
        this.creator = creator;
        this.target = target;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Column(nullable = false, length = 1024)
    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @ManyToOne
    public BlogUser getCreator() {
        return creator;
    }

    public void setCreator(BlogUser creator) {
        this.creator = creator;
    }

    @ManyToOne
    public BlogUser getTarget() {
        return target;
    }

    public void setTarget(BlogUser target) {
        this.target = target;
    }
}
