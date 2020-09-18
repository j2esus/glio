package com.jeegox.glio.dao.hibernate;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, ID extends Serializable> {

    void save(T entity);

    T findById(ID id);

    List<T> findAll();

    Long count();

    Boolean exists(ID id);

    void flush();

    void clear();
}
