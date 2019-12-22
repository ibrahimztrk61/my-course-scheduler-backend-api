package edu.itu.swe.mycoursescheduling.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Student extends User{

    private int grade;
    private int term;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "student_course",
            joinColumns = { @JoinColumn(name = "student_id") },
            inverseJoinColumns = { @JoinColumn(name = "course_id") })
    private List<Course> courses;

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }


    public void addCourse(Course course){
        this.getCourses().add(course);
    }
}
