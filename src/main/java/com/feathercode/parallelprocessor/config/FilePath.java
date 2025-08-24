package com.feathercode.parallelprocessor.config;

public enum FilePath {
    CSV_INPUT("src/main/resources/csv/MOCK_DATA_huge.csv"),
    CSV_OUTPUT("src/main/resources/csv/summary.csv");

    private final String path;

    FilePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
