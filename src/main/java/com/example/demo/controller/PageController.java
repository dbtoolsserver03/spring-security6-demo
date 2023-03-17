package com.example.demo.controller;

import com.example.demo.domain.user.User;
import com.example.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class PageController {
    private final UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/sign-in";
    }

    @GetMapping("/sign-in")
    public String login() {
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String join() {
        return "sign-up";
    }

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void signUp(User user, HttpServletResponse response) throws IOException {
        userService.signUp(user);
        response.sendRedirect("/sign-in");
    }
}
