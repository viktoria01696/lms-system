package com.example.demo.domain;

import javax.persistence.*;

@Entity
@Table(name = "avatar_images")
public class AvatarImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String contentType;

    @Column
    private String filename;

    @Column
    private String nativeName;

    @OneToOne
    private User user;

    @OneToOne
    private Course course;

    public AvatarImage(Long id, String contentType, String filename, String nativeName, User user) {
        this.id = id;
        this.contentType = contentType;
        this.filename = filename;
        this.nativeName = nativeName;
        this.user = user;
    }

    public AvatarImage(Long id, String contentType, String filename, String nativeName, Course course) {
        this.id = id;
        this.contentType = contentType;
        this.filename = filename;
        this.nativeName = nativeName;
        this.course = course;
    }

    public AvatarImage() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }
}
