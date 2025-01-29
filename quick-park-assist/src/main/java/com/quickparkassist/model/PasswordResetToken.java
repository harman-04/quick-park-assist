package com.quickparkassist.model;



import java.time.LocalDateTime;

import javax.persistence.*;

@Entity
@Table(name = "passwordresettoken", uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;
    private LocalDateTime expiryDateTime;
    @OneToOne(fetch = FetchType.EAGER , cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiryDateTime() {
        return expiryDateTime;
    }

    public void setExpiryDateTime(LocalDateTime expiryDateTime) {
        this.expiryDateTime = expiryDateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}