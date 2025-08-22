package com.feathercode.parallelprocessor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.feathercode.parallelprocessor.config.FilePath;
import com.feathercode.parallelprocessor.model.Product;
import com.feathercode.parallelprocessor.service.DataProcessor;
import com.feathercode.parallelprocessor.serviceimpl.CsvProcessor;

import java.util.Map;

@RestController()
@RequestMapping("/api")
public class ProcessorController {

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return new ResponseEntity<>("Welcome to ParallelProcessing", HttpStatus.OK);
    }

    @GetMapping("/csv")
    public ResponseEntity<Map<String, Product>> summary(@RequestParam("max") boolean max) {
        DataProcessor csvProcessor = new CsvProcessor();
        Map<String, Product> result = Product.getSoldProducts(
                                        csvProcessor.readProducts(FilePath.CSV_INPUT.getPath()),
                                        max);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
