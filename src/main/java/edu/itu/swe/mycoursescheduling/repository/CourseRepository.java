package edu.itu.swe.mycoursescheduling.repository;

import edu.itu.swe.mycoursescheduling.domain.Course;
import edu.itu.swe.mycoursescheduling.domain.Lecturer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course,Long> {

    List<Course> findAll();
    List<Course> findByGradeAndTerm(int grade, int term);
    List<Course> findByLecturer(Lecturer lecturer);

    Course findCourseByCrn(int crn);
}
