package com.jeegox.glio.dao.admin.impl;

import com.jeegox.glio.dao.admin.UserTypeDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dto.admin.OptionMenuUserTypeDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.enumerators.EntityType;
import com.jeegox.glio.enumerators.Status;
import java.math.BigInteger;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UserTypeDAOImpl extends GenericDAOImpl<UserType,Integer> implements UserTypeDAO{

    @Override
    public List<UserType> findByCompany(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select u ");
        sb.append(" from UserType u ");
        sb.append(" where u.father = :father ");
        sb.append(" and u.status != :status ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("father", company);
        q.setParameter("status", Status.DELETED);
        return q.list();
    }

    @Override
    public List<OptionMenuUserTypeDTO> findOptionsMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append(" select new com.jeegox.glio.dto.admin.OptionMenuUserTypeDTO(o.id, o.name,cat.id, cat.name, false ) ");
        sb.append(" from OptionMenu o ");
        sb.append(" join o.father cat ");
        sb.append(" where o.entityType = :entityType ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString(), OptionMenuUserTypeDTO.class);
        
        q.setParameter("entityType", EntityType.PUBLIC);
        return q.list();
    }

    @Override
    public void deleteOptions(Integer idUserType, String[] idsOptions) {
        StringBuilder sb = new StringBuilder();
        sb.append(" delete from user_type_option ");
        sb.append(" where id_user_type = :idUserType ");
        sb.append(" and id_option_menu in ( :idsOptions ) ");
        
        Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
        query.setParameter("idUserType", idUserType);
        query.setParameterList("idsOptions", idsOptions);
        query.executeUpdate();
    }

    @Override
    public void addOption(Integer idUserType, String idOption) {
        StringBuilder sb = new StringBuilder();
        sb.append(" insert into user_type_option ");
        sb.append(" values ( :idUserType , :idOption ) ");
        
        Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
        query.setParameter("idUserType", idUserType);
        query.setParameter("idOption", idOption);
        query.executeUpdate();
    }

    @Override
    public Integer findOption(Integer idUserType, String idOption) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select count(*) from user_type_option ");
        sb.append(" where id_user_type =  :idUserType and id_option_menu = :idOption ");
        
        Query query = sessionFactory.getCurrentSession().createNativeQuery(sb.toString());
        query.setParameter("idUserType", idUserType);
        query.setParameter("idOption", idOption);
        
        return ((BigInteger) query.uniqueResult()).intValue();
    }
    
}
