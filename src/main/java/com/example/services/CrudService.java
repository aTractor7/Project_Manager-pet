package com.example.services;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {
    List<T> findAll();
    T findOne(int id);
    void save(T toSave);
    void update(int id,T toUpdate);
    void delete(int id);
}
