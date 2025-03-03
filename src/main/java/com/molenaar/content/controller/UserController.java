package com.molenaar.content.controller;

import com.molenaar.content.module.user.UserDomain;
import com.molenaar.content.module.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    Mono<Optional<UserDomain.UserRecord>> index(){
        return userService.getByUsername("Something");
    }
}
