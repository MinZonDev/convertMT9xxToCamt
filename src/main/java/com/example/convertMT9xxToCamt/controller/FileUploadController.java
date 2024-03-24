package com.example.convertMT9xxToCamt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class FileUploadController {

    private final String uploadDir = "./uploads";

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Create the directory if it doesn't exist
            Path directory = Paths.get(uploadDir);
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // Save the file to the upload directory
            Path filePath = directory.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath);

            // Build download URL
            String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/download/")
                    .path(file.getOriginalFilename())
                    .toUriString();

            return ResponseEntity.ok().body("File uploaded successfully. Download URL: " + downloadURL);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error uploading file.");
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        // Load file as Resource
        Path filePath = Paths.get(uploadDir).resolve(fileName);
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }

        // Try to determine file's content type
        String contentType;
        try {
            contentType = Files.probeContentType(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}

