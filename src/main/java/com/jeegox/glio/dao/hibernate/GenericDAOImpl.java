package com.jeegox.glio.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericDAOImpl <T, ID extends Serializable> implements GenericDAO<T, ID>{
    @Autowired
    protected SessionFactory sessionFactory;
    private Class<T> persistentClass;
    
    public GenericDAOImpl(){
        persistentClass = findEntityClassOrInterface();
    }
    
    private final Class<T> findEntityClassOrInterface() {
        ParameterizedType genericSuperClass = (ParameterizedType) getClass().getGenericSuperclass();
        for (Type type : genericSuperClass.getActualTypeArguments()) {
            if (type instanceof ParameterizedType) {
                return (Class<T>) ((ParameterizedType) type).getRawType();
            } else if (type instanceof Class<?>) {
                return (Class<T>) type;
            }
        }
        return null;
    }
    
    public Class<? extends T> getPersistentClass() {
        return persistentClass;
    }

    @Override
    public void save(T entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public T findById(ID id) {
        return (T)sessionFactory.getCurrentSession().get(getPersistentClass(), id);
    }

    @Override
    public List<T> findAll() {
        String qry = " from "+getPersistentClass().getSimpleName();
        return sessionFactory.getCurrentSession().createQuery(qry).getResultList();
    }

    @Override
    public Long count() {
        String qry = " select count(e) from "+getPersistentClass().getSimpleName() +" e ";
        return (Long)sessionFactory.getCurrentSession().createQuery(qry).getResultList().stream().findFirst().orElse(0L);
    }

    @Override
    public Boolean exists(ID id) {
        if(findById(id) != null)
            return true;
        return false;
    }

    @Override
    public void flush() {
        sessionFactory.getCurrentSession().flush();
    }

    @Override
    public void clear() {
        sessionFactory.getCurrentSession().clear();
    }
}
