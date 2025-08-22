package com.feathercode.parallelprocessor.serviceimpl;

import com.feathercode.parallelprocessor.model.Product;
import com.feathercode.parallelprocessor.service.DataProcessor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CsvProcessor implements DataProcessor {
    @Override
    public List<Product> readProducts(String path) {
        List<Product> products = new ArrayList<>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(path));
            bf.readLine(); // to skip the header
            String line;
            while((line = bf.readLine()) != null) {
                String[] parts = line.split(",");
                Product p = new Product(
                        parts[0],
                        parts[1],
                        parts[2],
                        Integer.parseInt(parts[3]),
                        Double.parseDouble(parts[4]));
                products.add(p);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return products;
    }

    @Override
    public boolean writeSummary(Map<String, Product> summary, String path) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("Category -- topProduct\n");
            for (Map.Entry<String, Product> entry : summary.entrySet()) {
                bw.write(entry.getKey() + " -- " + entry.getValue().getName() + "\n");
            }
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
