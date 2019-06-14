package com.jeegox.glio.dao.expenses.impl;

import com.jeegox.glio.dao.expenses.ExpenseDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dto.GeneralCategoryDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Expense;
import com.jeegox.glio.enumerators.Status;
import java.util.Date;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author j2esus
 */
@Repository
public class ExpenseDAOImpl extends GenericDAOImpl<Expense, Integer> implements ExpenseDAO{

    @Override
    public List<Expense> findBy(Company company, Status[] estatus, Integer idCategory, 
            Integer idSubcategory, Date initDate, Date endDate) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select e ");
        sb.append(" from Expense e ");
        sb.append(" join e.father f ");
        sb.append(" join e.subcategory s ");
        sb.append(" join s.father c ");
        sb.append(" where f = :company ");
        sb.append(" and e.status in ( :status )");
        if(!idSubcategory.equals(0))
            sb.append(" and s.id = :idSubcategory ");
        if(!idCategory.equals(0))
        sb.append(" and c.id = :idCategory ");
        sb.append(" and e.date between :init and :end ");
        
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        q.setParameter("init", initDate);
        q.setParameter("end", endDate);
        q.setParameterList("status", estatus);
        if(!idSubcategory.equals(0))
            q.setParameter("idSubcategory", idSubcategory);
        if(!idCategory.equals(0))
            q.setParameter("idCategory", idCategory);
        return q.list();
    }

    @Override
    public List<GeneralCategoryDTO> findDataCategory(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select new com.jeegox.glio.dto.GeneralCategoryDTO(c.id, c.name, sum(e.amount) ) ");
        sb.append(" from Expense e ");
        sb.append(" join e.father f ");
        sb.append(" join e.subcategory s ");
        sb.append(" join s.father c ");
        sb.append(" where f = :company ");
        sb.append(" and e.status =  :status ");
        sb.append(" group by c.id, c.name ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString(), GeneralCategoryDTO.class);
        q.setParameter("company", company);
        q.setParameter("status", Status.ACTIVE);
        return q.list();
    }

    @Override
    public List<GeneralCategoryDTO> findDataSubcategory(Category category) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select new com.jeegox.glio.dto.GeneralCategoryDTO(s.id, s.name, sum(e.amount) ) ");
        sb.append(" from Expense e ");
        sb.append(" join e.father f ");
        sb.append(" join e.subcategory s ");
        sb.append(" join s.father c ");
        sb.append(" where c = :category ");
        sb.append(" and e.status =  :status ");
        sb.append(" group by s.id, s.name ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString(), GeneralCategoryDTO.class);
        q.setParameter("category", category);
        q.setParameter("status", Status.ACTIVE);
        return q.list();
    }
    
}
