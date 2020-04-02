package com.jpa.repository;

import java.util.List;
import java.util.Optional;

public interface JpaRepository<T, ID extends Number> {

    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T t);
    void deleteAll();
    void deleteById(ID id);
}
