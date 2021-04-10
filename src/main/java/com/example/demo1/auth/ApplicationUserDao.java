package com.example.demo1.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
public interface ApplicationUserDao {

     Optional<ApplicationUser>selectApplicationUserByUsername(String username);

}
