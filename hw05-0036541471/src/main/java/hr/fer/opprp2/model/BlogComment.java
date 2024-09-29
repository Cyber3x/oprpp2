package hr.fer.opprp2.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "blog_comments")
public class BlogComment {
    private Long id;
    private Date createdAt;
    private String content;
    private BlogUser creator;
    private BlogEntry blogEntry;

    public BlogComment() {}

    public BlogComment(String content, BlogUser creator, BlogEntry blogEntry) {
        this.content = content;
        this.creator = creator;
        this.blogEntry = blogEntry;
        this.createdAt = new Date();
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

    @Column(length = 1024, nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @ManyToOne
    public BlogUser getCreator() {
        return creator;
    }

    public void setCreator(BlogUser creator) {
        this.creator = creator;
    }

    @ManyToOne
    public BlogEntry getBlogEntry() {
        return blogEntry;
    }

    public void setBlogEntry(BlogEntry blogEntry) {
        this.blogEntry = blogEntry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlogComment comment = (BlogComment) o;
        return Objects.equals(id, comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
