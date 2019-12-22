package edu.itu.swe.mycoursescheduling.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.DayOfWeek;


@Data
@Table
@Entity
public class CourseTime {

   @Id
   @GeneratedValue
   private Long id;
   @Enumerated(EnumType.STRING)
   private DayOfWeek day;
   private int startTime;
   private int endTime;

   public CourseTime(Long id, DayOfWeek day, int startTime, int endTime) {
      this.id = id;
      this.day = day;
      this.startTime = startTime;
      this.endTime = endTime;
   }

   public CourseTime() {
   }
   // @ManyToMany(mappedBy = "times")
   //private List<Course> courses;



}
