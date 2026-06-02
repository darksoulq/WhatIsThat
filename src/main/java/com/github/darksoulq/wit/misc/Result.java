package com.github.darksoulq.wit.misc;

public record Result<T>(boolean isSuccess, T value) {
    public static <T> Result<T> success(T value) {
        return new Result<>(true, value);
    }

    public static <T> Result<T> failure(T value) {
        return new Result<>(false, value);
    }
}