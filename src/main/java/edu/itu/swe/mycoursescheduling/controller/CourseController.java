package edu.itu.swe.mycoursescheduling.controller;

import edu.itu.swe.mycoursescheduling.domain.Course;
import edu.itu.swe.mycoursescheduling.domain.CourseTime;
import edu.itu.swe.mycoursescheduling.domain.Lecturer;
import edu.itu.swe.mycoursescheduling.repository.CourseRepository;
import edu.itu.swe.mycoursescheduling.repository.LecturerRepository;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/courses")
@CrossOrigin(origins = "*")

public class CourseController {

    private final CourseRepository courseRepository;
    private final LecturerRepository lecturerRepository;

    public CourseController(CourseRepository courseRepository, LecturerRepository lecturerRepository) {
        this.courseRepository = courseRepository;
        this.lecturerRepository = lecturerRepository;
    }

    @PostMapping("/{lecturerId}")
    public ResponseEntity createCourse(@RequestBody  Course course, @PathVariable Long lecturerId) throws NotFoundException {
        Lecturer lecturer = lecturerRepository.findById(lecturerId).orElseThrow(()-> new NotFoundException("Lecturer is not found with this id"));
        course.setLecturer(lecturer);
        courseRepository.save(course);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAll() {
        List<Course> courses = courseRepository.findAll();
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/crn/{crn}")
    public ResponseEntity<Course> getByCrn(@PathVariable int crn) {
        Course course = courseRepository.findCourseByCrn(crn);
        return ResponseEntity.ok(course);
    }

    @PostMapping("/{id}/lecturer")
    public ResponseEntity assignLecturer(Long lecturerId, @PathVariable Long id) throws NotFoundException {
        Lecturer lecturer = lecturerRepository.findById(lecturerId).orElseThrow(()-> new NotFoundException("Lecturer is not found with this id"));
        Course course = courseRepository.findById(id).orElseThrow(()-> new NotFoundException("Course is not found with this id"));

        if (lecturer.getCourses().stream().anyMatch(course1 -> course1.equals(course))) {
            return ResponseEntity.badRequest().build();
        }

        lecturer.getCourses().add(course);
        return ResponseEntity.ok().build();
    }


    //@PostConstruct
    void initialize() {

        if (courseRepository.count() == 0) {
            Course course = new Course();
            course.setId(1L);
            course.setCode("BLG 101E");
            course.setCrn(11988);
            course.setTitle("Intr. to Information Systems");
            course.setRoom("BIL.LAB1 D202");
            course.setCapacity(60);

            CourseTime courseTime = new CourseTime();
            courseTime.setDay(DayOfWeek.MONDAY);
            courseTime.setStartTime(8);
            courseTime.setEndTime(11);

            course.setTimes(Collections.singleton(courseTime));
            courseRepository.save(course);
        }

    }
}
