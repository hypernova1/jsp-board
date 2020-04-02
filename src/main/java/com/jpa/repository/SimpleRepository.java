package com.jpa.repository;

import java.util.List;
import java.util.Optional;

public class SimpleRepository<T, ID extends Number> implements JpaRepository<T, ID> {

    @Override
    public Optional<T> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return null;
    }

    @Override
    public T save(T t) {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(ID id) {

    }
}
