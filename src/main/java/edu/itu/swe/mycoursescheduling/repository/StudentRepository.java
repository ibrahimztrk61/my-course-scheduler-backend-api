package edu.itu.swe.mycoursescheduling.repository;

import edu.itu.swe.mycoursescheduling.domain.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StudentRepository extends CrudRepository<Student, Long> {
    List<Student > findAll();

    Student findByEmail(String email);
}
