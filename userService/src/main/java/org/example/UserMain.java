package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/api")
@SpringBootApplication
public class UserMain {
    public static void main(String[] args) {
        SpringApplication.run(UserMain.class, args);
    }

    @GetMapping("/info")
    public String getApi(@RequestHeader("client-id")String clientId){
        System.out.println(clientId);
        return "user";
    }


}