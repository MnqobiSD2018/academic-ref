package com.hit.academic_ref.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping(value = {
        "/login",
        "/years",
        "/years/**",
        "/projects/**",
        "/search",
        "/admin"
    })
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}
