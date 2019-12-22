package edu.itu.swe.mycoursescheduling.controller;

import com.sun.xml.bind.v2.model.core.EnumLeafInfo;
import edu.itu.swe.mycoursescheduling.domain.User;
import edu.itu.swe.mycoursescheduling.domain.UserType;
import edu.itu.swe.mycoursescheduling.model.LoginInfo;
import edu.itu.swe.mycoursescheduling.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {


    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody User user){
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<User> findStudents(@PathVariable Long id){
        return userRepository.findById(id);
    }


    //@PostConstruct
    void initUser(){

        if (userRepository.count() == 0){
            User user = new User();
            user.setEmail("admin@itu.com");
            user.setPassword("123456");
            user.setName("admin");
            user.setSurname("admin");
            user.setUserType(UserType.ADMIN);
            userRepository.save(user);
        }
    }

}
