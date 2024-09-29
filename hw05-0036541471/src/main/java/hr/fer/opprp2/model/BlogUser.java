package hr.fer.opprp2.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "blog_users")
public class BlogUser {

    private Long id;
    private String firstName;
    private String lastName;
    private String nick;
    private String email;
    private String passwordHash;
    private List<BlogEntry> blogEntries;



    private List<ProfileMessage> receivedProfileMessages;
    private List<ProfileMessage> writtenProfileMessages;

    @Id @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false, length = 100)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @OneToMany(mappedBy = "creator")
    public List<BlogEntry> getBlogEntries() {
        return this.blogEntries;
    }

    public void setBlogEntries(List<BlogEntry> blogEntries) {
        this.blogEntries = blogEntries;
    }

    @Column(nullable = false, length = 100)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(nullable = false, length = 100, unique = true)
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Column(nullable = false, length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(nullable = false)
    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @OneToMany(mappedBy = "target")
    public List<ProfileMessage> getReceivedProfileMessages() {
        return receivedProfileMessages;
    }

    public void setReceivedProfileMessages(List<ProfileMessage> receivedProfileMessages) {
        this.receivedProfileMessages = receivedProfileMessages;
    }

    @OneToMany(mappedBy = "creator")
    public List<ProfileMessage> getWrittenProfileMessages() {
        return writtenProfileMessages;
    }

    public void setWrittenProfileMessages(List<ProfileMessage> writtenProfileMessages) {
        this.writtenProfileMessages = writtenProfileMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlogUser blogUser = (BlogUser) o;
        return Objects.equals(id, blogUser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
