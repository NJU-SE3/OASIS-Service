package com.example.oasispaper.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
    @GetMapping("/")
    public String hello() {
        return "hello world";
    }

    @GetMapping("/{name}")
    public String h(@PathVariable String name) {
        return name;
    }

    @GetMapping("/hek")
    public String h1() {
        return "name";
    }
}
