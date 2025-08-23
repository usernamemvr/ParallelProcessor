package com.feathercode.parallelprocessor.serviceimpl;

import com.feathercode.parallelprocessor.model.Product;
import com.feathercode.parallelprocessor.service.DataProcessor;
import com.feathercode.parallelprocessor.tasks.CsvBatchTask;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class CsvProcessor implements DataProcessor {

    private static final int BATCH_SIZE = 500;
    @Override
    public List<Product> readProducts(String path) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        List<Product> products = new ArrayList<>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(path));
            bf.readLine(); // to skip the header
            List<String> batch = new ArrayList<>();
            List<Future<List<Product>>> futures = new ArrayList<>();
            String line;
            while((line = bf.readLine()) != null) {
                batch.add(line);
                if(batch.size() == BATCH_SIZE) {
                    futures.add(executor.submit(new CsvBatchTask(new ArrayList<>(batch))));
                    batch.clear();
                }
            }
            if(!batch.isEmpty()) {
                futures.add(executor.submit(new CsvBatchTask(new ArrayList<>(batch))));
            }
            for(Future<List<Product>> f : futures) products.addAll(f.get());
        } catch (Exception e){
            System.out.println("Error while reading csv -> " + e.getMessage());
        } finally {
            executor.shutdown();
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
