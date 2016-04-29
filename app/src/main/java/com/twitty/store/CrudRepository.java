package com.twitty.store;

import java.io.Serializable;

public interface CrudRepository<T, PK extends Serializable> {

    long save(T entity);

    T findOne(PK id);

    void delete(T entity);

    Iterable<T> findAll();

    void refresh();

    boolean exists(PK id);
}
