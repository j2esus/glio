package com.jeegox.glio.dao;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.dao.admin.CategoryMenuDAO;
import com.jeegox.glio.entities.admin.CategoryMenu;
import com.jeegox.glio.enumerators.Status;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CategoryMenuDAOTest {
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private CategoryMenuDAO categoryMenuDAO;
    private static CategoryMenu expectedCategoryMenu = new CategoryMenu(1, "Administración", 1,
            Status.ACTIVE, "fa-building", "bg-primary");

    @Before
    public void setUp() throws SQLException{
        insertInitialData();
    }

    @Test
    public void findById_idExists_categoryMenu(){
        assertThat(categoryMenuDAO.findById(1)).isEqualTo(expectedCategoryMenu);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(categoryMenuDAO.findById(10)).isNull();
    }

    @Test
    public void findAll_notRequired_listWithThreeElements(){
        assertThat(categoryMenuDAO.findAll()).isEqualTo(allExpectedCategories());
    }

    @Test
    public void count_notRequired_three(){
        assertThat(categoryMenuDAO.count()).isEqualTo(3);
    }

    @Test
    public void exists_idExists_true(){
        assertThat(categoryMenuDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(categoryMenuDAO.exists(100)).isFalse();
    }

    private List<CategoryMenu> allExpectedCategories(){
        List<CategoryMenu> categoryMenuList = new ArrayList<>();
        categoryMenuList.add(new CategoryMenu(1, "Administración", 1, Status.ACTIVE, "fa-building", "bg-primary"));
        categoryMenuList.add(new CategoryMenu(2, "Tareas", 2, Status.ACTIVE, "fa-list", "bg-warning"));
        categoryMenuList.add(new CategoryMenu(3, "Gastos", 3, Status.ACTIVE, "fa-usd", "bg-success"));
        return categoryMenuList;
    }

    private void insertInitialData() throws SQLException {
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();

        connection.createStatement().execute("insert into category_menu"+
                "(id_category_menu, name, order_category_menu, status, icon, class) "+
                "values "+
                "(1, 'Administración', '1', 'ACTIVE', 'fa-building', 'bg-primary')");

        connection.createStatement().execute("insert into category_menu"+
                "(id_category_menu, name, order_category_menu, status, icon, class) "+
                "values "+
                "(2, 'Tareas', '2', 'ACTIVE', 'fa-list', 'bg-warning')");

        connection.createStatement().execute("insert into category_menu"+
                "(id_category_menu, name, order_category_menu, status, icon, class) "+
                "values "+
                "(3, 'Gastos', '3', 'ACTIVE', 'fa-usd', 'bg-success')");
    }
}
