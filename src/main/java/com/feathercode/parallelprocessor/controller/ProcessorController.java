package com.feathercode.parallelprocessor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.feathercode.parallelprocessor.model.Product;
import com.feathercode.parallelprocessor.service.DataProcessor;
import com.feathercode.parallelprocessor.serviceimpl.CsvProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@RestController()
@RequestMapping("/api")
public class ProcessorController {

    DataProcessor csvProcessor = new CsvProcessor();
    private File uploadFile;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        if(file.isEmpty()) return new ResponseEntity<>("File is Empty", HttpStatus.NO_CONTENT);
        try {
            File uploadDir = new File("E:/Code/PROJECTS/ParallelProcessor/uploaded_csv");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File tempFile = File.createTempFile("upload-",".csv", uploadDir);
            file.transferTo(tempFile);
            uploadFile = tempFile;
            return new ResponseEntity<>("File uploaded successfully at:" + tempFile.getAbsolutePath(),
                                        HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }

    }
    @GetMapping("/")
    public ResponseEntity<String> home() {
        return new ResponseEntity<>("Welcome to ParallelProcessing", HttpStatus.OK);
    }

    @GetMapping("/process")
    public ResponseEntity<Map<String, Product>> summary(@RequestParam("max") boolean max) {
        if (uploadFile == null || !uploadFile.exists()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Map<String, Product> result = Product.getSoldProducts(
                                        csvProcessor.readProducts(uploadFile.getAbsolutePath()),
                                        max);
        boolean deleted = uploadFile.delete();
        if (deleted) {
            System.out.println("Deleted uploaded file: " + uploadFile.getAbsolutePath());
        } else {
            System.err.println("Failed to delete uploaded file: " + uploadFile.getAbsolutePath());
        }
        uploadFile = null;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
