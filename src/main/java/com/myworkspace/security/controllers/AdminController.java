package com.myworkspace.security.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

        @GetMapping("/page")
        public String getAdminDetails(){
            System.out.print("the controller lands here");
            return "This is admin Page";
        }

}
