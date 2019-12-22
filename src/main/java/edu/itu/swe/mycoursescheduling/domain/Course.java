package edu.itu.swe.mycoursescheduling.domain;


import javax.persistence.*;
import java.util.*;

@Entity
@Table
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    private String code;
    private int crn;
    private String title;
    private String building;
    private String room;
    private int capacity;
    private int grade;
    private int term;
    private boolean isMandatory;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "course_course_time",
            joinColumns = { @JoinColumn(name = "course_id") },
            inverseJoinColumns = { @JoinColumn(name = "course_time_id") })
    private Set<CourseTime> times = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCrn() {
        return crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


    public Lecturer getLecturer() {
        return lecturer;
    }

    public void setLecturer(Lecturer lecturer) {
        this.lecturer = lecturer;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

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

    public boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(boolean mandatory) {
        isMandatory = mandatory;
    }

    public Set<CourseTime> getTimes() {
        return times;
    }

    public void setTimes(Set<CourseTime> times) {
        this.times = times;
    }

    public boolean hasConflictCourse(CourseTime courseTime){
        return this.getTimes()
                .stream()
                .anyMatch(c -> (courseTime.getDay().toString().equals(courseTime.getDay().toString()) &&
                        (c.getStartTime()==courseTime.getStartTime() ||
                        c.getEndTime()==(courseTime.getEndTime()) ||
                        c.getStartTime()<(courseTime.getStartTime()) && c.getEndTime()>(courseTime.getStartTime()) ||
                        c.getEndTime()>(courseTime.getEndTime()) && c.getStartTime()<(courseTime.getEndTime()) ||
                        c.getStartTime()>(courseTime.getStartTime()) && c.getEndTime()<(courseTime.getEndTime()))));
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Course course = (Course) object;
        return id.equals(course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

