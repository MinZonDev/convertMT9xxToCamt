package com.example.convertMT9xxToCamt.controller;

import com.example.convertMT9xxToCamt.service.SwiftMTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SwiftMTController {

    @Autowired
    private SwiftMTService swiftMTService;

    @PostMapping("/parse")
    public List<String> parseSwiftMTFile(@RequestParam("filePath") String filePath) {
        return swiftMTService.parseSwiftMTFile(filePath);
    }
}

