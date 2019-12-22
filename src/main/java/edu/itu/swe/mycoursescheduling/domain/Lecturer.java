package edu.itu.swe.mycoursescheduling.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Lecturer extends User {

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "lecturer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Course> courses;

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    private void addCourse(Course course){
        this.getCourses().add(course);
    }




}
