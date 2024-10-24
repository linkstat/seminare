package ar.com.hmu.repository.dao;

import java.util.List;
import java.util.UUID;

public interface GenericDAO<T> {

    void create(T entity);
    T readByUUID(UUID id);
    List<T> readAll();
    void update(T entity);
    void delete(T entity);

}
