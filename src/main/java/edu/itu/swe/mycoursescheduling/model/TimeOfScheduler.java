package edu.itu.swe.mycoursescheduling.model;
import edu.itu.swe.mycoursescheduling.domain.Course;

import java.time.DayOfWeek;

public class TimeOfScheduler {
    private int startTime;
    private Course monday;
    private Course tuesday;
    private Course wednesday;
    private Course thursday;
    private Course friday;

    public TimeOfScheduler(int i) {
        this.startTime = i;
    }


    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public Course getMonday() {
        return monday;
    }

    public void setMonday(Course monday) {
        this.monday = monday;
    }

    public Course getTuesday() {
        return tuesday;
    }

    public void setTuesday(Course tuesday) {
        this.tuesday = tuesday;
    }

    public Course getWednesday() {
        return wednesday;
    }

    public void setWednesday(Course wednesday) {
        this.wednesday = wednesday;
    }

    public Course getThursday() {
        return thursday;
    }

    public void setThursday(Course thursday) {
        this.thursday = thursday;
    }

    public Course getFriday() {
        return friday;
    }

    public void setFriday(Course friday) {
        this.friday = friday;
    }

    public void dayFactory(DayOfWeek dayOfWeek, Course course){
        if (dayOfWeek.equals(DayOfWeek.MONDAY)){
            this.monday = course;
        }
        else if (dayOfWeek.equals(DayOfWeek.TUESDAY)){
            this.tuesday = course;
        }
        else if (dayOfWeek.equals(DayOfWeek.WEDNESDAY)){
            this.wednesday = course;
        }
        else if (dayOfWeek.equals(DayOfWeek.THURSDAY)){
            this.thursday = course;
        }
        else if (dayOfWeek.equals(DayOfWeek.FRIDAY)){
            this.friday = course;
        }
    }
}
