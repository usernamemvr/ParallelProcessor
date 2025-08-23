package com.feathercode.parallelprocessor.tasks;

import com.feathercode.parallelprocessor.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CsvBatchTask implements Callable<List<Product>> {

    private final List<String> lines;

    public CsvBatchTask(List<String> lines) {
        this.lines = lines;
    }
    @Override
    public List<Product> call() throws Exception {
        List<Product> products = new ArrayList<>();
        for(String line : lines) {
            String[] parts = line.split(",");
            Product p = new Product(
                    parts[0],
                    parts[1],
                    parts[2],
                    Integer.parseInt(parts[3]),
                    Double.parseDouble(parts[4]));
            products.add(p);
        }
        return products;
    }
}
