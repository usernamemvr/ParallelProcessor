package com.feathercode.parallelprocessor.service;

import com.feathercode.parallelprocessor.model.Product;

import java.util.List;
import java.util.Map;

public interface DataProcessor {
    List<Product> readProducts(String path);

    boolean writeSummary(Map<String, Product> summary, String path);
}
