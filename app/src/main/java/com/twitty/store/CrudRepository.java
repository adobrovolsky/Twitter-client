package com.twitty.store;

import java.io.Serializable;

public interface CrudRepository<T, PK extends Serializable> {

    PK save(T entity);

    T findOne(PK id);

    void update(T entity);

    void delete(T entity);

    Iterable<T> findAll();

    void refresh();

    boolean exists(PK id);
}
