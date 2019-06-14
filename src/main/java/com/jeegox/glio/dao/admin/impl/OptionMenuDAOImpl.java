package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.OptionMenuDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.enumerators.EntityType;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author j2esus
 */
@Repository
public class OptionMenuDAOImpl extends GenericDAOImpl<OptionMenu,Integer> implements OptionMenuDAO {

    @Override
    public List<OptionMenu> findBy(EntityType entityType) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select o ");
        sb.append(" from OptionMenu o ");
        sb.append(" where o.entityType = :entityType ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        
        q.setParameter("entityType", entityType);
        
        return q.list();
    }
    
}
