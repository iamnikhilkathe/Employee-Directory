package com.SymphonyAI.employeedirectory.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {
    private final String name;
    private final String version;

    public InfoController(@Value("${app.name}") String name, @Value("${app.version}") String version) {
        this.name = name;
        this.version = version;
    }

    @GetMapping("/api/info")
    public ResponseEntity<Info> info() {
        return ResponseEntity.ok(new Info(name, version));
    }

    public record Info(String name, String version) {
    }
}
