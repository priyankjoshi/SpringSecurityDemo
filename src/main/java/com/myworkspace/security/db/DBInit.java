package com.myworkspace.security.db;

import com.myworkspace.security.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DBInit implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncode;

    public DBInit(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        //Delete the repo.
        this.userRepository.deleteAll();
        //Created users
        UserEntity user = new UserEntity("priyank", passwordEncode.encode("joshi"), "USER", "");
        UserEntity admin = new UserEntity("admin", passwordEncode.encode("password"),"ADMIN","ACCESS_TEST1,ACCESS_TEST2");
        UserEntity manager = new UserEntity("manager", passwordEncode.encode("manager123"),"MANAGER","");

        //save users to DB
        List<UserEntity> users = Arrays.asList(user,admin,manager);
        this.userRepository.saveAll(users);
    }
}
