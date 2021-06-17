package com.jeegox.glio.dao.expenses;

import static com.google.common.truth.Truth.assertThat;
import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.expenses.Category;
import com.jeegox.glio.entities.expenses.Subcategory;
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
public class SubcategoryDAOTest {
    @Autowired
    private SubcategoryDAO subcategoryDAO;

    @Autowired
    private SessionFactory sessionFactory;
    
    private final Company mcdonals = new Company(1, "Mcdonals", "burgers",
            Status.ACTIVE, 3);
    private final Category advertising = new Category(1, "advertising", 
            Status.ACTIVE, mcdonals);
    private final Category suplies = new Category(2, "suplies",
            Status.ACTIVE, mcdonals);
    
    @Before
    public void setUp() throws SQLException {
        insertInitialData();
    }
    
    @Test
    public void findById_idExists_subcategory() {
        assertThat(subcategoryDAO.findById(1)).isEqualTo(expectedSubcategory());
    }
    
    private Subcategory expectedSubcategory(){
        return new Subcategory(1, "google", Status.ACTIVE, advertising);
    }
    
    @Test
    public void findById_idNotExists_null(){
        assertThat(subcategoryDAO.findById(100)).isNull();
    }
    
    @Test
    public void exists_idExists_true() {
        assertThat(subcategoryDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false() {
        assertThat(subcategoryDAO.exists(100)).isFalse();
    }
    
    @Test
    public void count_return6() {
        assertThat(subcategoryDAO.count()).isEqualTo(6);
    }

    @Test
    public void findAll_returnListWithSixElements() {
        assertThat(subcategoryDAO.findAll()).isEqualTo(expectedSubcategoryList());
    }
    
    private List<Subcategory> expectedSubcategoryList() {
        List<Subcategory> subcategories = new ArrayList<>();
        subcategories.add(new Subcategory(1, "google", Status.ACTIVE, advertising));
        subcategories.add(new Subcategory(2, "facebook", Status.INACTIVE, advertising));
        subcategories.add(new Subcategory(3, "facebook", Status.DELETED, advertising));
        subcategories.add(new Subcategory(4, "meat", Status.ACTIVE, suplies));
        subcategories.add(new Subcategory(5, "bread", Status.INACTIVE, suplies));
        subcategories.add(new Subcategory(6, "pinaple", Status.DELETED, suplies));
        return subcategories;
    }
    
    @Test
    public void findBy_categoryExistsThreeStatus_listWithThreeElements() {
        Status[] statusFilter = new Status[]{
            Status.ACTIVE,
            Status.INACTIVE,
            Status.DELETED};
        String nameFilter = "";
        List<Subcategory> subcategoryListResponse = subcategoryDAO.findBy(advertising,
                statusFilter, nameFilter);
        assertThat(subcategoryListResponse).isEqualTo(expectedSubcategoryListByCategory());
    }
    
    private List<Subcategory> expectedSubcategoryListByCategory() {
        List<Subcategory> subcategories = new ArrayList<>();
        subcategories.add(new Subcategory(1, "google", Status.ACTIVE, advertising));
        subcategories.add(new Subcategory(2, "facebook", Status.INACTIVE, advertising));
        subcategories.add(new Subcategory(3, "facebook", Status.DELETED, advertising));
        return subcategories;
    }
    
    @Test
    public void findBy_categoryExistsOneStatus_listWithOneElement() {
        Status[] statusFilter = new Status[]{Status.ACTIVE};
        String nameFilter = "";
        List<Subcategory> subcategoryListResponse = subcategoryDAO.findBy(advertising, 
                statusFilter, nameFilter);
        assertThat(subcategoryListResponse)
                .isEqualTo(expectedActiveSubcategoryListByCategory());
    }
    
    private List<Subcategory> expectedActiveSubcategoryListByCategory() {
        List<Subcategory> subcategories = new ArrayList<>();
        subcategories.add(new Subcategory(1, "google", Status.ACTIVE, advertising));
        return subcategories;
    }
    
    @Test
    public void findBy_categoryNotExistsThreeStatus_emptyList() {
        Category categoryNotExistsFilter = new Category(100, "notExists",
                Status.ACTIVE, mcdonals);
        Status[] statusFilter = new Status[]{
            Status.ACTIVE,
            Status.INACTIVE,
            Status.DELETED};
        String nameFilter = "";
        List<Subcategory> subcategoryListResponse
                = subcategoryDAO.findBy(categoryNotExistsFilter, statusFilter, nameFilter);
        assertThat(subcategoryListResponse).isEmpty();
    }
    
    @Test
    public void findBy_categoryExistsThreeStatusAndName_listWithTwoElements() {
        Status[] statusFilter = new Status[]{
            Status.ACTIVE,
            Status.INACTIVE,
            Status.DELETED
        };
        String nameFilter = "facebook";
        List<Subcategory> subcategoryListResponse
                = subcategoryDAO.findBy(advertising, statusFilter, nameFilter);
        assertThat(subcategoryListResponse).isEqualTo(
                expectedSubcategoryListByCategoryWithSameName());
    }
    
    private List<Subcategory> expectedSubcategoryListByCategoryWithSameName() {
        List<Subcategory> subcategories = new ArrayList<>();
        subcategories.add(new Subcategory(2, "facebook", Status.INACTIVE, advertising));
        subcategories.add(new Subcategory(3, "facebook", Status.DELETED, advertising));
        return subcategories;
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
                .execute(" INSERT INTO subcategory(id_subcategory, id_category, name, status)"
                        + " VALUES (1, 1, 'google', 'ACTIVE') ");
        
        connection.createStatement()
                .execute(" INSERT INTO subcategory(id_subcategory, id_category, name, status)"
                        + " VALUES (2, 1, 'facebook', 'INACTIVE') ");
        
        connection.createStatement()
                .execute(" INSERT INTO subcategory(id_subcategory, id_category, name, status)"
                        + " VALUES (3, 1, 'facebook', 'DELETED') ");
        
        connection.createStatement()
                .execute(" INSERT INTO category(id_category, id_company, name, status)"
                        + " VALUES (2, 1, 'suplies', 'ACTIVE') ");
        
        connection.createStatement()
                .execute(" INSERT INTO subcategory(id_subcategory, id_category, name, status)"
                        + " VALUES (4, 2, 'meat', 'ACTIVE') ");

        connection.createStatement()
                .execute(" INSERT INTO subcategory(id_subcategory, id_category, name, status)"
                        + " VALUES (5, 2, 'bread', 'INACTIVE') ");

        connection.createStatement()
                .execute(" INSERT INTO subcategory(id_subcategory, id_category, name, status)"
                        + " VALUES (6, 2, 'pinaple', 'DELETED') ");
    }
}
