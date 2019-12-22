package edu.itu.swe.mycoursescheduling.repository;

import edu.itu.swe.mycoursescheduling.domain.Lecturer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LecturerRepository extends CrudRepository<Lecturer, Long> {

    List<Lecturer> findAll();
    Lecturer findByNameAndSurname(String name, String surname);

    Lecturer findByEmail(String email);
}
