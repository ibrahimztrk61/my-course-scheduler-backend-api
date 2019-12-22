package edu.itu.swe.mycoursescheduling.model;

import edu.itu.swe.mycoursescheduling.domain.CourseTime;

import java.time.DayOfWeek;

public class CourseTimeConflictResolver {

    private Long courseId;
    private DayOfWeek day;
    private int startTime;
    private int endTime;
    private String code;
    private String title;

    public CourseTimeConflictResolver(Long courseId, DayOfWeek day, int startTime, int endTime, String code, String title) {
        this.courseId = courseId;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.code = code;
        this.title = title;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean hasConflictCourse(CourseTimeConflictResolver courseTime){
        return (this.getDay().equals(courseTime.getDay()) &&
                        (this.getStartTime()==courseTime.getStartTime() ||
                                this.getEndTime()==(courseTime.getEndTime()) ||
                                this.getStartTime()<(courseTime.getStartTime()) && this.getEndTime()>(courseTime.getStartTime()) ||
                                this.getEndTime()>(courseTime.getEndTime()) && this.getStartTime()<(courseTime.getEndTime()) ||
                                this.getStartTime()>(courseTime.getStartTime()) && this.getEndTime()<(courseTime.getEndTime())));
    }
}
