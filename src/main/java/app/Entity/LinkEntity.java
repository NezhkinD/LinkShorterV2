package app.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "links")
public class LinkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String original;

    @Column(nullable = false, length = 30, unique = true, name = "short")
    private String shortLink;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "expired_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime expiredAt;

    @Column(nullable = false, unique = true, length = 36)
    private String user;

    @Column(name = "transition_limit", nullable = false, columnDefinition = "int default 0")
    private int transitionLimit;

    @Column(name = "transition_current", nullable = false, columnDefinition = "int default 0")
    private int transitionCurrent;

    public static LinkEntity create(String original, String shortLink, LocalDateTime expiredAt, String user, int transitionLimit)
    {
        LinkEntity link = new LinkEntity();
        link.original = original;
        link.shortLink = shortLink;
        link.expiredAt = expiredAt;
        link.user = user;
        link.transitionLimit = transitionLimit;
        link.transitionCurrent = 0;
        link.createdAt = LocalDateTime.now();
        return link;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getTransitionLimit() {
        return transitionLimit;
    }

    public void setTransitionLimit(int transitionLimit) {
        this.transitionLimit = transitionLimit;
    }

    public int getTransitionCurrent() {
        return transitionCurrent;
    }

    public void setTransitionCurrent(int transitionCurrent) {
        this.transitionCurrent = transitionCurrent;
    }
}
