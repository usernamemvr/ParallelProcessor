package com.feathercode.parallelprocessor.model;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Product {

    private final String id;
    private final String name;
    private final String category;
    private final int quantitySold;
    private final double price;

    public Product(String id, String name, String category, int quantitySold, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantitySold = quantitySold;
        this.price = price;
    }

    public static Map<String, Product> getSoldProducts(List<Product> products, boolean getMax){
        Comparator<Product> comparator = Comparator.comparingInt(Product::getQuantitySold);
        return products.stream()
                .collect(Collectors.toMap(
                        Product::getCategory, // sets category (String) as key
                        Function.identity(), // product -> product
                        getMax ? BinaryOperator.maxBy(comparator)
                                : BinaryOperator.minBy(comparator)));
    }


    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", quantitySold=" + quantitySold +
                ", price=" + price +
                '}';
    }
}
