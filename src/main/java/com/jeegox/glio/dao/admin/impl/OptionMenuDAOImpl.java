package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.OptionMenuDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.enumerators.EntityType;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class OptionMenuDAOImpl extends GenericDAOImpl<OptionMenu,Integer> implements OptionMenuDAO {

    @Override
    public List<OptionMenu> findBy(EntityType entityType) {
        String query = " select o from OptionMenu o "+
                " where o.entityType = :entityType ";
        return sessionFactory.getCurrentSession().createQuery(query).setParameter("entityType", entityType).getResultList();
    }
    
}
