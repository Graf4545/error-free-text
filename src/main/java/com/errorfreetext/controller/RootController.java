package com.errorfreetext.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping
    public ResponseEntity<Map<String, String>> root() {
        Map<String, String> body = new HashMap<>();
        body.put("status", "UP");
        body.put("service", "error-free-text");
        return ResponseEntity.ok(body);
    }
}
