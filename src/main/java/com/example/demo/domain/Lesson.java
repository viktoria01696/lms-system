package com.example.demo.domain;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column
    private String text;

    @ManyToOne(optional = false)
    private Course course;

    public Lesson() {
    }

    public Lesson(Long id, String title, String text, Course course) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }


}


