package edu.itu.swe.mycoursescheduling.controller;

import edu.itu.swe.mycoursescheduling.domain.Course;
import edu.itu.swe.mycoursescheduling.domain.Lecturer;
import edu.itu.swe.mycoursescheduling.domain.User;
import edu.itu.swe.mycoursescheduling.model.TimeOfScheduler;
import edu.itu.swe.mycoursescheduling.repository.CourseRepository;
import edu.itu.swe.mycoursescheduling.repository.LecturerRepository;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/lecturers")
@CrossOrigin(origins = "*")
public class LecturerController {

    private final LecturerRepository lecturerRepository;
    private final CourseRepository courseRepository;

    public LecturerController(LecturerRepository lecturerRepository, CourseRepository courseRepository) {
        this.lecturerRepository = lecturerRepository;
        this.courseRepository = courseRepository;
    }


    @PostMapping
    public ResponseEntity createLecturer(@RequestBody Lecturer request) {
        Lecturer lecturer = lecturerRepository.findByNameAndSurname(request.getName(),request.getSurname());
        lecturer.setEmail(request.getEmail());
        lecturer.setPassword(request.getPassword());
        lecturerRepository.save(lecturer);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Lecturer>> getStudents(){
        List<Lecturer> lecturers = lecturerRepository.findAll();
        return ResponseEntity.ok(lecturers);
    }

    @PatchMapping("{id}/course")
    public ResponseEntity addCourseToLecturer(@RequestBody Course course, @PathVariable Long id) throws NotFoundException {
        Lecturer lecturer = lecturerRepository.findById(id).orElseThrow(()-> new NotFoundException("Lecturer is not found"));

        if (lecturer.getCourses().stream().anyMatch(course1 -> course1.equals(course))) {
            return ResponseEntity.badRequest().build();
        }

        lecturer.getCourses().add(course);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public Lecturer login(@RequestBody User loginInfo){
        Lecturer lecturer = lecturerRepository.findByEmail(loginInfo.getEmail());

        if (lecturer!=null){
            if (lecturer.getPassword().equals(loginInfo.getPassword())) {
                return lecturer;
            }
        }
        return null;
    }


    @GetMapping("/{id}/scheduled")
    public List<TimeOfScheduler> getScheduledCourses(@PathVariable Long id) throws NotFoundException {
        Lecturer lecturer = lecturerRepository.findById(id).orElseThrow(() -> new NotFoundException("Lecturer is not found"));
        List<Course> courses = courseRepository.findByLecturer(lecturer);

        HashMap<Integer,TimeOfScheduler> hashMap = new HashMap<>();

        for(int i = 8; i < 17;i++){
            hashMap.put(i,new TimeOfScheduler(i));
        }
        courses.forEach(course -> {
            course.getTimes().forEach(courseTime -> {
                int interval = courseTime.getEndTime() - courseTime.getStartTime();
                for (int i = 0; i < interval; i++){
                    hashMap.get(courseTime.getStartTime()+i).dayFactory(courseTime.getDay(),course);
                }
            });
        });
        return new ArrayList<>(hashMap.values()).stream().sorted(Comparator.comparing(TimeOfScheduler::getStartTime)).collect(Collectors.toList());
    }
}
