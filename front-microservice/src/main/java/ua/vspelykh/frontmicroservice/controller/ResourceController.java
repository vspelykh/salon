package ua.vspelykh.frontmicroservice.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1")
public class ResourceController {

    @GetMapping(value = "/images/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> getImage(@PathVariable String imageName) {
        Resource resource = new ClassPathResource("static/images/" + imageName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }

    @GetMapping("/styles/{styleName}")
    public ResponseEntity<Resource> getStyle(@PathVariable String styleName) {
        Resource resource = new ClassPathResource("static/css/" + styleName);
        return ResponseEntity.ok()
                .body(resource);
    }

    @GetMapping("/scripts/{scripts}")
    public ResponseEntity<Resource> getScripts(@PathVariable String scripts) {

        Resource resource = new ClassPathResource("static/js/" + scripts);
        return ResponseEntity.ok()
                .body(resource);
    }
}
