package edu.itu.swe.mycoursescheduling.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginInfo {
    private String email;
    private String password;
}
