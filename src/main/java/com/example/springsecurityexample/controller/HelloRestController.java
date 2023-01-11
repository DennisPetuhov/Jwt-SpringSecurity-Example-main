package com.example.springsecurityexample.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@SecurityRequirement(name = "javainuseapi")

@RestController
@RequestMapping("hello")

public class HelloRestController {
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))

    @GetMapping("user")
    public String helloUser() {
        return "Hello User";
    }

    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("admin")
    public String helloAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // те ли за кого выдаем себя(прошли -идем дальше)

        UserDetails userPrincipal = (UserDetails)authentication.getPrincipal();
        System.out.println("User principal name =" + userPrincipal.getUsername());
        return "Hello Admin " + userPrincipal.getUsername();
    }

}