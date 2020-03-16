package com.jeegox.glio.dao.expenses.impl;

import com.jeegox.glio.dao.expenses.ExpenseDAO;
import com.jeegox.glio.dao.hibernate.GenericDAOImpl;
import com.jeegox.glio.dto.GeneralCategoryDTO;
import com.jeegox.glio.dto.MonthDTO;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Expense;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.util.Util;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author j2esus
 */
@Repository
public class ExpenseDAOImpl extends GenericDAOImpl<Expense, Integer> implements ExpenseDAO {

    @Override
    public List<Expense> findBy(Company company, Status[] estatus, Integer idCategory,
            Integer idSubcategory, Date initDate, Date endDate, String description) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select e ");
        sb.append(" from Expense e ");
        sb.append(" join e.father f ");
        sb.append(" join e.subcategory s ");
        sb.append(" join s.father c ");
        sb.append(" where f = :company ");
        sb.append(" and e.description like :description ");
        sb.append(" and e.status in ( :status )");
        if (!idSubcategory.equals(0)) {
            sb.append(" and s.id = :idSubcategory ");
        }
        if (!idCategory.equals(0)) {
            sb.append(" and c.id = :idCategory ");
        }
        sb.append(" and e.date between :init and :end ");
        sb.append(" order by e.date ");

        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        q.setParameter("init", initDate);
        q.setParameter("end", endDate);
        q.setParameter("description", "%" + description + "%");
        q.setParameterList("status", estatus);
        if (!idSubcategory.equals(0)) {
            q.setParameter("idSubcategory", idSubcategory);
        }
        if (!idCategory.equals(0)) {
            q.setParameter("idCategory", idCategory);
        }
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

    @Override
    public List<String> yearsExpenses(Company company) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select year(date) ");
        sb.append(" from Expense e ");
        sb.append(" where e.father = :company ");
        sb.append(" group by year(date) ");
        sb.append(" order by year(date) desc ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString());
        q.setParameter("company", company);
        return q.list();
    }

    @Override
    public List<MonthDTO> getMonthAmounts(Company company, Integer year) {
        List<MonthDTO> result = Util.getMonths();
        StringBuilder sb = new StringBuilder();
        sb.append(" select month(expense_date), sum(amount) ");
        sb.append(" from expense ");
        sb.append(" where status = '").append(Status.ACTIVE).append("'");
        sb.append(" and year(expense_date) = ").append(year).append(" ");
        sb.append(" and id_company = ").append(company.getId());
        sb.append(" group by month(expense_date) ");

        List<Object[]> data = sessionFactory.getCurrentSession().createNativeQuery(sb.toString()).list();

        for (Object[] objects : data) {
            result.get(((Integer) objects[0])-1).setAmount((Double) objects[1]);
        }

        return result;
    }

    @Override
    public List<GeneralCategoryDTO> findDataCategory(Company company, Integer year) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select new com.jeegox.glio.dto.GeneralCategoryDTO(c.id, c.name, sum(e.amount) ) ");
        sb.append(" from Expense e ");
        sb.append(" join e.father f ");
        sb.append(" join e.subcategory s ");
        sb.append(" join s.father c ");
        sb.append(" where f = :company ");
        sb.append(" and e.status =  :status ");
        sb.append(" and year(e.date) =  :year ");
        sb.append(" group by c.id, c.name ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString(), GeneralCategoryDTO.class);
        q.setParameter("company", company);
        q.setParameter("status", Status.ACTIVE);
        q.setParameter("year", year);
        return q.list();
    }

    @Override
    public List<GeneralCategoryDTO> findDataCategory(Company company, Integer year, Integer month) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select new com.jeegox.glio.dto.GeneralCategoryDTO(c.id, c.name, sum(e.amount) ) ");
        sb.append(" from Expense e ");
        sb.append(" join e.father f ");
        sb.append(" join e.subcategory s ");
        sb.append(" join s.father c ");
        sb.append(" where f = :company ");
        sb.append(" and e.status =  :status ");
        sb.append(" and year(e.date) =  :year ");
        sb.append(" and month(e.date) =  :month ");
        sb.append(" group by c.id, c.name ");
        Query q = sessionFactory.getCurrentSession().createQuery(sb.toString(), GeneralCategoryDTO.class);
        q.setParameter("company", company);
        q.setParameter("status", Status.ACTIVE);
        q.setParameter("year", year);
        q.setParameter("month", month);
        return q.list();
    }

}
