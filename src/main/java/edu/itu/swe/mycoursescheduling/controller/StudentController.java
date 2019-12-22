package edu.itu.swe.mycoursescheduling.controller;

import edu.itu.swe.mycoursescheduling.domain.Course;
import edu.itu.swe.mycoursescheduling.domain.Student;
import edu.itu.swe.mycoursescheduling.domain.User;
import edu.itu.swe.mycoursescheduling.model.CourseTimeConflictResolver;
import edu.itu.swe.mycoursescheduling.model.TimeOfScheduler;
import edu.itu.swe.mycoursescheduling.repository.CourseRepository;
import edu.itu.swe.mycoursescheduling.repository.StudentRepository;
import javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/students")
@CrossOrigin(origins = "*")
public class StudentController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public StudentController(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }


    @PostMapping
    public void createStudent(@RequestBody Student student) {
        student.setTerm(1);
        studentRepository.save(student);
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudents() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

    @PatchMapping("{id}/course")
    public ResponseEntity addCourseToStudent(@RequestBody Course course, @PathVariable Long id) throws NotFoundException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new NotFoundException("Student is not found"));

        if (student.getCourses().stream().anyMatch(course1 -> course1.equals(course))) {
            return ResponseEntity.badRequest().build();
        }

        student.getCourses().add(course);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public Student login(@RequestBody User loginInfo){
        Student student = studentRepository.findByEmail(loginInfo.getEmail());

        if (student!=null){
            if (student.getPassword().equals(loginInfo.getPassword())) {
                return student;
            }
        }
        return null;
    }


    @GetMapping("/{id}/scheduled")
    public List<TimeOfScheduler> getScheduledCourses(@PathVariable Long id) throws NotFoundException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new NotFoundException("Student is not found"));
        List<Course> courseByGradeAndTerm = courseRepository.findByGradeAndTerm(student.getGrade(), student.getTerm());


        List<Course> mandatoryCourses = courseByGradeAndTerm.stream().filter(Course::getIsMandatory).collect(Collectors.toList());
        List<Course> nonMandatoryCourses = courseByGradeAndTerm.stream().filter(course -> !course.getIsMandatory()).collect(Collectors.toList());

        List<CourseTimeConflictResolver> resolvers = new ArrayList<>();
        List<Course> scheduledCourses = new ArrayList<>();
        Set<Course> nonConflictMandatoryCourses = new HashSet<>();
        HashMap<CourseTimeConflictResolver, CourseTimeConflictResolver> conflictMandatoryCoursesMap = new HashMap<>();

        List<Course> sortedCourses = new ArrayList<>(mandatoryCourses);
        scheduledCourses.addAll(nonMandatoryCourses);

        HashMap<Long, Course> mandatoryCourseMap = new HashMap<>();
        sortedCourses.forEach(m -> {
            mandatoryCourseMap.put(m.getId(), m);
        });

        sortedCourses.forEach(course -> {
            course.getTimes().forEach(times -> {
                resolvers.add(new CourseTimeConflictResolver(course.getId(), times.getDay(), times.getStartTime(), times.getEndTime(), course.getCode(), course.getTitle()));
            });
        });



        resolvers.forEach(resolver -> {
            if (!nonConflictMandatoryCourses.contains(mandatoryCourseMap.get(resolver.getCourseId()))) {

                if (nonConflictMandatoryCourses.stream().noneMatch(co -> co.getCode().equals(resolver.getCode()))) {

                    List<CourseTimeConflictResolver> filteredResolver = resolvers.stream()
                            .filter(r -> !r.getCourseId().equals(resolver.getCourseId()))
                            .filter(r -> !r.getCode().equals(resolver.getCode()))
                            .collect(Collectors.toList());

                    filteredResolver.forEach(resolver2 -> {
                        if (resolver.hasConflictCourse(resolver2)) {
                            if (!conflictMandatoryCoursesMap.containsKey(resolver2)){
                                conflictMandatoryCoursesMap.put(resolver, resolver2);
                            }
                            List<CourseTimeConflictResolver> nonConflictOthers = filteredResolver.stream().filter(i -> i.getCourseId().equals(resolver2.getCourseId()) || i.getCode().equals(resolver2.getCode())).collect(Collectors.toList());
                            for (CourseTimeConflictResolver other : nonConflictOthers) {
                                if (!resolver.hasConflictCourse(other)) {
                                    nonConflictMandatoryCourses.add(mandatoryCourseMap.get(resolver.getCourseId()));
                                    nonConflictMandatoryCourses.add(mandatoryCourseMap.get(other.getCourseId()));
                                    break;
                                }
                            }
                        } else {
                            nonConflictMandatoryCourses.add(mandatoryCourseMap.get(resolver.getCourseId()));
                        }
                    });
                }
            }
        });

        return getTimeOfScheduler(new ArrayList<>(nonConflictMandatoryCourses));

    }

    private List<TimeOfScheduler> getTimeOfScheduler(List<Course> courses) {
        HashMap<Integer, TimeOfScheduler> timeOfSchedulerHashMap = new HashMap<>();

        for (int i = 8; i < 17; i++) {
            timeOfSchedulerHashMap.put(i, new TimeOfScheduler(i));
        }
        courses.forEach(course -> {
            course.getTimes().forEach(courseTime -> {
                int interval = courseTime.getEndTime() - courseTime.getStartTime();
                for (int i = 0; i < interval; i++) {
                    timeOfSchedulerHashMap.get(courseTime.getStartTime() + i).dayFactory(courseTime.getDay(), course);
                }
            });
        });
        return new ArrayList<>(timeOfSchedulerHashMap.values()).stream().sorted(Comparator.comparing(TimeOfScheduler::getStartTime)).collect(Collectors.toList());
    }

       /* for (Course course: mandatoryCourses) {
            for (Course course2: mandatoryCourses) {
                if (!course.equals(course2))
                {
                    for (CourseTime time :course2.getTimes()) {
                        if (course.hasConflictCourse(time)){

                        }
                    }
                }
            }
        }

         resolvers.forEach(resolver ->{
            if (!nonConflictMandatoryCourses.contains(mandatoryCourseMap.get(resolver.getCourseId()))) {
                resolvers.forEach( resolver2 -> {
                    if (!resolver.getCourseId().equals(resolver2.getCourseId()))
                    {
                        if (!resolver.hasConflictCourse(resolver2)) {
                            nonConflictMandatoryCourses.add(mandatoryCourseMap.get(resolver.getCourseId()));
                            nonConflictMandatoryCourses.add(mandatoryCourseMap.get(resolver2.getCourseId()));
                        } else {
                            nonConflictMandatoryCourses.add(mandatoryCourseMap.get(resolver.getCourseId()));
                        }
                    }
                });
            }
        });

        return courseByGradeAndTerm;


          Course a = new Course();
        a.setId(1L);
        a.setTitle("a");
        a.setTimes(new HashSet<>(Arrays.asList(new CourseTime(1L, DayOfWeek.MONDAY, 1, 3))));

        Course b = new Course();
        b.setId(1L);
        b.setTitle("b");
        b.setTimes(new HashSet<>(Arrays.asList(new CourseTime(2L, DayOfWeek.MONDAY, 3, 5),
                new CourseTime(3L, DayOfWeek.TUESDAY, 1, 3))));

        Course c = new Course();
        c.setId(1L);
        c.setTitle("c");
        c.setTimes(new HashSet<>(Arrays.asList(new CourseTime(2L, DayOfWeek.MONDAY, 3, 5),
                new CourseTime(3L, DayOfWeek.TUESDAY, 1, 3))));
    */

}
