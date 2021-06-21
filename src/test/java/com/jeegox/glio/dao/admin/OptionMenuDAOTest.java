package com.jeegox.glio.dao.admin;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.CategoryMenu;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.enumerators.EntityType;
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
import com.jeegox.glio.dto.admin.OptionMenuDTO;

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class OptionMenuDAOTest {
    @Autowired
    private OptionMenuDAO optionMenuDAO;
    @Autowired
    private SessionFactory sessionFactory;
    private static final OptionMenu expectedOptionMenu = new OptionMenu(1, "Empresa", 1, "/company/init",
            Status.ACTIVE, "fa-server", EntityType.PRIVATE, new CategoryMenu(1, "Administración" , 1,
            Status.ACTIVE, "fa-building","bg-primary" ));

    @Before
    public void setUp() throws SQLException{
        insertInitialData();
    }

    @Test
    public void count_notRequired_five(){
        assertThat(optionMenuDAO.count()).isEqualTo(5);
    }

    @Test
    public void exists_idExists_true(){
        assertThat(optionMenuDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(optionMenuDAO.exists(100)).isFalse();
    }

    @Test
    public void findById_idExists_optionMenu(){
        assertThat(optionMenuDAO.findById(1)).isEqualTo(expectedOptionMenu);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(optionMenuDAO.findById(100)).isNull();
    }

    @Test
    public void findAll_notRequired_listWithFiveElements(){
        assertThat(optionMenuDAO.findAll()).isEqualTo(expectedAllOptionsMenusList());
    }
    
    private List<OptionMenu> expectedAllOptionsMenusList() {
        CategoryMenu categoryMenuAdmin = new CategoryMenu(1, "Administración", 1, Status.ACTIVE, "fa-building", "bg-primary");
        CategoryMenu categoryMenuTask = new CategoryMenu(2, "Tareas", 2, Status.ACTIVE, "fa-list", "bg-warning");
        List<OptionMenu> optionMenuList = new ArrayList<>();
        optionMenuList.add(new OptionMenu(1, "Empresa", 1, "/company/init", Status.ACTIVE, "fa-server",
                EntityType.PRIVATE, categoryMenuAdmin));
        optionMenuList.add(new OptionMenu(2, "Usuario", 2, "/user/init", Status.ACTIVE, "fa-user",
                EntityType.PUBLIC, categoryMenuAdmin));
        optionMenuList.add(new OptionMenu(6, "Proyecto", 1, "/project/init", Status.ACTIVE, "fa-cubes",
                EntityType.PUBLIC, categoryMenuTask));
        optionMenuList.add(new OptionMenu(7, "Tareas", 2, "/task/init", Status.ACTIVE, "fa-tasks",
                EntityType.PUBLIC, categoryMenuTask));
        optionMenuList.add(new OptionMenu(8, "Avances", 3, "/advance/init", Status.ACTIVE, "fa-pie-chart",
                EntityType.PUBLIC, categoryMenuTask));
        return optionMenuList;
    }

    @Test
    public void findByEntityType_private_listWithOnlyOneElement(){
        assertThat(optionMenuDAO.findBy(EntityType.PRIVATE)).containsExactly(expectedOptionMenu);
    }

    @Test
    public void findByEntityType_public_listWithFourElement(){
        assertThat(optionMenuDAO.findBy(EntityType.PUBLIC)).isEqualTo(expectedPublicOptionsMenusList());
    }
    
    private List<OptionMenu> expectedPublicOptionsMenusList() {
        CategoryMenu categoryMenuAdmin = new CategoryMenu(1, "Administración", 1, Status.ACTIVE, "fa-building", "bg-primary");
        CategoryMenu categoryMenuTask = new CategoryMenu(2, "Tareas", 2, Status.ACTIVE, "fa-list", "bg-warning");
        List<OptionMenu> optionMenuList = new ArrayList<>();
        optionMenuList.add(new OptionMenu(2, "Usuario", 2, "/user/init", Status.ACTIVE, "fa-user",
                EntityType.PUBLIC, categoryMenuAdmin));
        optionMenuList.add(new OptionMenu(6, "Proyecto", 1, "/project/init", Status.ACTIVE, "fa-cubes",
                EntityType.PUBLIC, categoryMenuTask));
        optionMenuList.add(new OptionMenu(7, "Tareas", 2, "/task/init", Status.ACTIVE, "fa-tasks",
                EntityType.PUBLIC, categoryMenuTask));
        optionMenuList.add(new OptionMenu(8, "Avances", 3, "/advance/init", Status.ACTIVE, "fa-pie-chart",
                EntityType.PUBLIC, categoryMenuTask));
        return optionMenuList;
    }
    
    @Test
    public void getPublicOptions_returnListWithFourElements(){
        List<OptionMenuDTO> listPublicOptions = optionMenuDAO.getPublicOptions();
        assertThat(listPublicOptions).isEqualTo(getExpectedPublicOptions());
    }
    
    private List<OptionMenuDTO> getExpectedPublicOptions(){
        List<OptionMenuDTO> listPublicOptions = new ArrayList<>();
        listPublicOptions.add(new OptionMenuDTO(2, "Usuario", 2, "/user/init", Status.ACTIVE, 1, "Administración"));
        listPublicOptions.add(new OptionMenuDTO(6, "Proyecto", 1, "/project/init", Status.ACTIVE, 2, "Tareas"));
        listPublicOptions.add(new OptionMenuDTO(7, "Tareas", 2, "/task/init", Status.ACTIVE, 2, "Tareas"));
        listPublicOptions.add(new OptionMenuDTO(8, "Avances", 3, "/advance/init", Status.ACTIVE, 2, "Tareas"));
        return listPublicOptions;
    }
    
    @Test
    public void findByIds_oneId_listWithOneElement(){
        List<OptionMenu> options = optionMenuDAO.findByIds(new Integer[]{2});
        assertThat(options).containsExactly(expectedOptionMenu());
    }
    
    private OptionMenu expectedOptionMenu(){
        CategoryMenu categoryMenuAdmin = new CategoryMenu(1, "Administración", 1, 
                Status.ACTIVE, "fa-building", "bg-primary");
        OptionMenu optionMenu = new OptionMenu(2, "Usuario", 2, "/user/init", 
                Status.ACTIVE, "fa-user", EntityType.PUBLIC, categoryMenuAdmin);
        return optionMenu;
    }
    
    @Test
    public void findByIds_twoIds_listWithTwoElements() {
        List<OptionMenu> options = optionMenuDAO.findByIds(new Integer[]{2, 6});
        assertThat(options).isEqualTo(expectedOptionsMenu());
    }
    
    private List<OptionMenu> expectedOptionsMenu() {
        List<OptionMenu> listOptions = new ArrayList<>();
        
        CategoryMenu categoryMenuAdmin = new CategoryMenu(1, "Administración", 1,
                Status.ACTIVE, "fa-building", "bg-primary");
        CategoryMenu categoryMenuTask = new CategoryMenu(2, "Tareas", 2, 
                Status.ACTIVE, "fa-list", "bg-warning");
        
        listOptions.add(new OptionMenu(2, "Usuario", 2, "/user/init",
                Status.ACTIVE, "fa-user", EntityType.PUBLIC, categoryMenuAdmin));
        listOptions.add(new OptionMenu(6, "Proyecto", 1, "/project/init", Status.ACTIVE, "fa-cubes",
                EntityType.PUBLIC, categoryMenuTask));
        return listOptions;
    }

    private void insertInitialData() throws SQLException{
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();
        connection.createStatement().execute("insert into category_menu"+
                "(id_category_menu, name, order_category_menu, status, icon, class) "+
                "values (1, 'Administración', 1, 'ACTIVE', 'fa-building', 'bg-primary')");

        connection.createStatement().execute("insert into category_menu"+
                "(id_category_menu, name, order_category_menu, status, icon, class) "+
                "values (2, 'Tareas', 2, 'ACTIVE', 'fa-list', 'bg-warning')");

        connection.createStatement().execute("insert into option_menu "+
                "(id_option_menu, name, order_option_menu, url, status, id_category_menu, entity_type, icon) "+
                "values (1, 'Empresa', 1, '/company/init', 'ACTIVE', 1, 'PRIVATE', 'fa-server')");

        connection.createStatement().execute("insert into option_menu "+
                "(id_option_menu, name, order_option_menu, url, status, id_category_menu, entity_type, icon) "+
                "values (2, 'Usuario', 2, '/user/init', 'ACTIVE', 1, 'PUBLIC', 'fa-user')");

        connection.createStatement().execute("insert into option_menu "+
                "(id_option_menu, name, order_option_menu, url, status, id_category_menu, entity_type, icon) "+
                "values (6, 'Proyecto', 1, '/project/init', 'ACTIVE', 2, 'PUBLIC', 'fa-cubes')");

        connection.createStatement().execute("insert into option_menu "+
                "(id_option_menu, name, order_option_menu, url, status, id_category_menu, entity_type, icon) "+
                "values (7, 'Tareas', 2, '/task/init', 'ACTIVE', 2, 'PUBLIC', 'fa-tasks')");

        connection.createStatement().execute("insert into option_menu "+
                "(id_option_menu, name, order_option_menu, url, status, id_category_menu, entity_type, icon) "+
                "values (8, 'Avances', 3, '/advance/init', 'ACTIVE', 2, 'PUBLIC', 'fa-pie-chart')");
    }
}
