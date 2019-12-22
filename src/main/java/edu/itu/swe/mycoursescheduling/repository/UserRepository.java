package edu.itu.swe.mycoursescheduling.repository;

import edu.itu.swe.mycoursescheduling.domain.Student;
import edu.itu.swe.mycoursescheduling.domain.User;
import edu.itu.swe.mycoursescheduling.domain.UserType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    List<User> findAll();
    List<User> findByUserType(UserType userType);

    User findByEmail(String email);
}
