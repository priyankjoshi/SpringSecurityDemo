package com.myworkspace.security.controllers;

import com.myworkspace.security.db.UserRepository;
import com.myworkspace.security.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicRestApiController {

	@Autowired
    private UserRepository userRepository;

	/*public PublicRestApiController(UserRepository userRepository) {
	    this.userRepository = userRepository;
    }*/

	@GetMapping("/test1")
	public String test1() { return "API test1"; }

	@GetMapping("/test2")
	public String test2() { return "API test2"; }

	@GetMapping("/users")
    public List<UserEntity> getUsers(){
        return (List<UserEntity>)userRepository.findAll();
    }
}
