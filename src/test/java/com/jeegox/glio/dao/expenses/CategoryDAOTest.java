package com.jeegox.glio.dao.expenses;

import static com.google.common.truth.Truth.assertThat;
import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.enumerators.Status;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CategoryDAOTest {
    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private SessionFactory sessionFactory;
    
    private final Company mcdonals = new Company(1, "Mcdonals", "burgers", 
            Status.ACTIVE, 3);
    private final Company burgerKing = new Company(2, "BurgerKing", "burgers",
            Status.ACTIVE, 3);
    
    @Before
    public void setUp() throws SQLException {
        insertInitialData();
    }
    
    @Test
    public void findById_idExists_category() {
        assertThat(categoryDAO.findById(1)).isEqualTo(expectedCategory());
    }
    
    private Category expectedCategory(){
        return new Category(1, "advertising", Status.ACTIVE, mcdonals);
    }
    
    @Test
    public void findById_idNotExists_null() {
        assertThat(categoryDAO.findById(100)).isNull();
    }
    
    @Test
    public void exists_idExists_true() {
        assertThat(categoryDAO.exists(1)).isTrue();
    }
    
    @Test
    public void exists_idNotExists_false() {
        assertThat(categoryDAO.exists(100)).isFalse();
    }
    
    @Test
    public void count_return6(){
        assertThat(categoryDAO.count()).isEqualTo(6);
    }
    
    @Test
    public void findAll_returnListWithSixElements() {
        assertThat(categoryDAO.findAll()).isEqualTo(expectedCategoryList());
    }
    
    private List<Category> expectedCategoryList(){
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "advertising", Status.ACTIVE, mcdonals));
        categories.add(new Category(2, "suplies", Status.INACTIVE, mcdonals));
        categories.add(new Category(3, "advertising", Status.DELETED, mcdonals));
        categories.add(new Category(4, "advertising", Status.ACTIVE, burgerKing));
        categories.add(new Category(5, "suplies", Status.INACTIVE, burgerKing));
        categories.add(new Category(6, "advertising", Status.DELETED, burgerKing));
        return categories;
    }
    
    @Test
    public void findBy_companyExistsThreeStatus_listWithThreeElements(){
        Status[] statusFilter = new Status[]{
            Status.ACTIVE, 
            Status.INACTIVE, 
            Status.DELETED};
        String nameFilter = "";
        List<Category> categoryListResponse = categoryDAO.findBy(mcdonals, statusFilter, nameFilter);
        assertThat(categoryListResponse).isEqualTo(expectedCategoryListByCompany());
    }
    
    private List<Category> expectedCategoryListByCompany() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "advertising", Status.ACTIVE, mcdonals));
        categories.add(new Category(2, "suplies", Status.INACTIVE, mcdonals));
        categories.add(new Category(3, "advertising", Status.DELETED, mcdonals));
        return categories;
    }
    
    @Test
    public void findBy_companyExistsOneStatus_listWithOneElement() {
        Status[] statusFilter = new Status[]{Status.ACTIVE};
        String nameFilter = "";
        List<Category> categoryListResponse = categoryDAO.findBy(mcdonals, statusFilter, nameFilter);
        assertThat(categoryListResponse)
                .isEqualTo(expectedActiveCategoryListByCompany());
    }

    private List<Category> expectedActiveCategoryListByCompany() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "advertising", Status.ACTIVE, mcdonals));
        return categories;
    }
    
    @Test
    public void findBy_companyNotExistsThreeStatus_emptyList() {
        Company companyNotExistsFilter = new Company(100, "NoExists", "nothing",
                Status.ACTIVE, 3);
        Status[] statusFilter = new Status[]{
            Status.ACTIVE, 
            Status.INACTIVE,
            Status.DELETED};
        String nameFilter = "";
        List<Category> categoryListResponse =
            categoryDAO.findBy(companyNotExistsFilter, statusFilter, nameFilter);
        assertThat(categoryListResponse).isEmpty();
    }
    
    @Test
    public void findBy_companyNotExistsThreeStatusAndName_listWithTwoElements() {
        Status[] statusFilter = new Status[]{
            Status.ACTIVE, 
            Status.INACTIVE,
            Status.DELETED
        };
        String nameFilter = "advertising";
        List<Category> categoryListResponse
                = categoryDAO.findBy(mcdonals, statusFilter, nameFilter);
        assertThat(categoryListResponse).isEqualTo(
                    expectedCategoryListByCompanyWithSameName());
    }
    
    private List<Category> expectedCategoryListByCompanyWithSameName() {
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(1, "advertising", Status.ACTIVE, mcdonals));
        categories.add(new Category(3, "advertising", Status.DELETED, mcdonals));
        return categories;
    }
    
    private void insertInitialData() throws SQLException {
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl) session.getSession()).connection();

        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'Mcdonals','burgers', 'ACTIVE', 3)");
        
        connection.createStatement()
                .execute(" INSERT INTO category(id_category, id_company, name, status)"
                        + " VALUES (1, 1, 'advertising', 'ACTIVE') ");
        
        connection.createStatement()
                .execute(" INSERT INTO category(id_category, id_company, name, status)"
                        + " VALUES (2, 1, 'suplies', 'INACTIVE') ");
        
        connection.createStatement()
                .execute(" INSERT INTO category(id_category, id_company, name, status)"
                        + " VALUES (3, 1, 'advertising', 'DELETED') ");
        
        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (2,'BurgerKing','burgers', 'ACTIVE', 3)");
        
        connection.createStatement()
                .execute(" INSERT INTO category(id_category, id_company, name, status)"
                        + " VALUES (4, 2, 'advertising', 'ACTIVE') ");
        
        connection.createStatement()
                .execute(" INSERT INTO category(id_category, id_company, name, status)"
                        + " VALUES (5, 2, 'suplies', 'INACTIVE') ");
        
        connection.createStatement()
                .execute(" INSERT INTO category(id_category, id_company, name, status)"
                        + " VALUES (6, 2, 'advertising', 'DELETED') ");
    }
}
