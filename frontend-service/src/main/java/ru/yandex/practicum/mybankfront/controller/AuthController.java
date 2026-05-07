package ru.yandex.practicum.mybankfront.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {
    @GetMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }
}
