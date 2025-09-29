package com.omnibase.quiz.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
public class OpenApiController {

    @GetMapping(value = "/openapi/openapi.yml", produces = "application/x-yaml")
    public ResponseEntity<String> getOpenApiSpec() {
        try {
            ClassPathResource resource = new ClassPathResource("static/openapi/openapi.yml");
            String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("application/x-yaml"))
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
