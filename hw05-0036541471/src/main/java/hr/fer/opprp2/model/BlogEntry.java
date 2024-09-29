package hr.fer.opprp2.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

//@NamedQueries({
//        @NamedQuery(name = "BlogEntry.upit1", query = "select b from BlogComment as b where b.blogEntry=:be and b.postedOn>:when")
//})
@Entity
@Table(name = "blog_entries")
public class BlogEntry {

    private Long id;
    private Date createdAt;
    private Date lastModifiedAt;
    private String title;
    private String text;
    private BlogUser creator;
    private List<BlogComment> comments;

    public BlogEntry() {}

    public BlogEntry(String title, String text, BlogUser creator) {
        this.title = title;
        this.text = text;
        this.creator = creator;
        this.createdAt = new Date();
        this.lastModifiedAt = new Date();
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public BlogUser getCreator() {
        return this.creator;
    }

    public void setCreator(BlogUser creator) {
        this.creator = creator;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @Column(length = 200, nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(length = 4096, nullable = false)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @OneToMany(mappedBy = "blogEntry")
    public List<BlogComment> getComments() {
        return comments;
    }

    public void setComments(List<BlogComment> comments) {
        this.comments = comments;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlogEntry other = (BlogEntry) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}