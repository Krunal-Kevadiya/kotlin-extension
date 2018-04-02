package com.kotlinextension.data.db;

import java.util.List;

import io.reactivex.Flowable;

public interface DatabaseSource<T> {
    Flowable<T> get(T t);
    Flowable<List<T>> getAll();
    void insert(T t);
    void delete(T t);
    void update(T t);
}
