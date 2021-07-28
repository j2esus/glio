package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.OptionMenuDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dto.admin.OptionMenuDTO;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.enumerators.EntityType;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class OptionMenuDAOImpl extends GenericDAOImpl<OptionMenu,Integer> 
        implements OptionMenuDAO {

    @Override
    public List<OptionMenu> findBy(EntityType entityType) {
        String query = " select o from OptionMenu o "+
                " where o.entityType = :entityType ";
        return sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("entityType", entityType)
                .getResultList();
    }

    @Override
    public List<OptionMenuDTO> getPublicOptions() {
        String query = " select new com.jeegox.glio.dto.admin.OptionMenuDTO(o.id, o.name, "
                + " o.order, o.url, o.status, c.id, c.name ) "
                + " from OptionMenu o "
                + " join o.father c "
                + " where o.entityType = :entityType "
                + " order by c.id, o.order ";
        return sessionFactory.getCurrentSession().createQuery(query)
                .setParameter("entityType", EntityType.PUBLIC)
                .getResultList();
    }

    @Override
    public List<OptionMenu> findByIds(Integer[] ids) {
        String query = " select o "+
                " from OptionMenu o "+
                " where o.id in ( :ids )";
        return sessionFactory.getCurrentSession().createQuery(query)
                .setParameterList("ids", ids)
                .getResultList();
    }
    
}
