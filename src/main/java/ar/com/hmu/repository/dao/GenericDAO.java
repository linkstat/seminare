package ar.com.hmu.repository.dao;

import java.util.List;

public interface GenericDAO<T> {

    void create(T entity);
    T readById(long id);
    List<T> readAll();
    void update(T entity);
    void delete(T entity);

}
