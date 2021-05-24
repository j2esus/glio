package com.jeegox.glio.dao.aim;

import com.jeegox.glio.config.spring.ApplicationContextConfigTest;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.entities.aim.Aim;
import com.jeegox.glio.entities.aim.Project;
import com.jeegox.glio.entities.aim.Task;
import com.jeegox.glio.enumerators.Priority;
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
import com.jeegox.glio.dto.TaskDTO;
import java.math.BigDecimal;

@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfigTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TaskDAOTest {
    @Autowired
    private TaskDAO taskDAO;
    @Autowired
    private SessionFactory sessionFactory;
    private final static Company company = new Company(1, "company", "description", Status.ACTIVE, 3);
    private final static User admin = new User(1, "admin@company", "password", "admin", Status.ACTIVE,
            new UserType(1, "Admin",Status.ACTIVE, company),
            false, company, "admin@company.com");
    private static final User worker = new User(2, "worker@company", "password", "worker", Status.ACTIVE,
            new UserType(2, "Worker",Status.ACTIVE, new Company(1, "company", "description",Status.ACTIVE, 3)),
            false, company, "worker@company.com");
    private final static Project project = new Project(1, "Project", "This is a really big project", Status.ACTIVE,
            java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2020-12-31"), admin);
    private final static Aim loginScreen = new Aim(1, "Login dummy", "Create a login dummy for demo presentation",
            Status.ACTIVE, java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2010-01-05"), admin, project);
    private final static Aim welcomeScreen = new Aim(2, "Welcome screen", "Create a welcome screen",
            Status.ACTIVE, java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2010-01-05"), admin, project);

    private static Task screenTask = new Task(1, "screen", "create screen", Status.IN_PROCESS, Priority.MEDIA, 8, admin, worker, loginScreen);

    private final static Project smallProject = new Project(2, "Small project", "This is a really small project", Status.ACTIVE,
            java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2020-12-31"), admin);
    private final static Aim smallAim = new Aim(3, "Do workout", "Go outside and do exercise.",
            Status.ACTIVE, java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2010-01-05"), admin, smallProject);
    private static Task openDoorTask = new Task(6, "Open the door", "wake up and open the door", Status.PENDING, Priority.MEDIA, 8, admin, worker, smallAim);
    
    private final static Aim welcomeScreenDeleted = new Aim(4, "Welcome deleted", "Create a welcome screen",
            Status.DELETED, java.sql.Date.valueOf("2010-01-01"), java.sql.Date.valueOf("2010-01-05"), admin, project);

    @Before
    public void setUp() throws SQLException {
        insertInitialData();
    }

    @Test
    public void findById_idExists_task(){
        Task task = new Task(1, "screen", "create screen", Status.IN_PROCESS, Priority.MEDIA, 8, admin, worker, loginScreen);
        assertThat(taskDAO.findById(1)).isEqualTo(task);
    }

    @Test
    public void findById_idNotExists_null(){
        assertThat(taskDAO.findById(100)).isNull();
    }

    @Test
    public void exists_idExists_true(){
        assertThat(taskDAO.exists(1)).isTrue();
    }

    @Test
    public void exists_idNotExists_false(){
        assertThat(taskDAO.exists(100)).isFalse();
    }

    @Test
    public void count_notRequired_eight(){
        assertThat(taskDAO.count()).isEqualTo(8);
    }

    @Test
    public void findByProject_projectExists_listWithFourElements(){
        assertThat(taskDAO.findByProject(project)).isEqualTo(getExpectedTasksByProject());
    }
    
    private List<Task> getExpectedTasksByProject() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, "screen", "create screen", Status.IN_PROCESS, Priority.MEDIA, 8, admin, worker, loginScreen));
        tasks.add(new Task(3, "service", "create service", Status.PENDING, Priority.BAJA, 8, admin, worker, loginScreen));
        tasks.add(new Task(4, "dao", "create dao", Status.PENDING, Priority.ALTA, 8, admin, worker, loginScreen));
        tasks.add(new Task(5, "screen", "create screen for welcome", Status.PENDING, Priority.ALTA, 8, admin, worker, welcomeScreen));
        tasks.add(new Task(8, "screen", "create screen for welcome", Status.ACCEPTED, Priority.ALTA, 8, admin, worker, welcomeScreen));
        return tasks;
    }

    @Test
    public void findByProject_projectNotExists_emptyList(){
        Project project = new Project();
        project.setId(100);
        assertThat(taskDAO.findByProject(project)).isEmpty();
    }

    @Test
    public void findByAim_aimExists_listWithThreeElements(){
        assertThat(taskDAO.findByAim(loginScreen)).isEqualTo(getExpectedTasksByAim());
    }
    
    private List<Task> getExpectedTasksByAim() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, "screen", "create screen", Status.IN_PROCESS, Priority.MEDIA, 8, admin, worker, loginScreen));
        tasks.add(new Task(3, "service", "create service", Status.PENDING, Priority.BAJA, 8, admin, worker, loginScreen));
        tasks.add(new Task(4, "dao", "create dao", Status.PENDING, Priority.ALTA, 8, admin, worker, loginScreen));
        return tasks;
    }

    @Test
    public void findByAim_aimNotExists_emptyList(){
        Aim aim = new Aim();
        aim.setId(100);
        assertThat(taskDAO.findByAim(aim)).isEmpty();
    }

    @Test
    public void countTasksInProcessByUser_userExists_one(){
        assertThat(taskDAO.countInProcess(worker)).isEqualTo(1);
    }

    @Test
    public void countTasksInProcessByUser_userWithoutTasks_zero(){
        assertThat(taskDAO.countInProcess(admin)).isEqualTo(0);
    }

    @Test
    public void findTasksByUser_finishedStatus_emptyList(){
        List<Task> tasks = taskDAO.findByUser(worker, new Status[]{Status.FINISHED}, "screen", Priority.values());
        assertThat(tasks).isEmpty();
    }

    @Test
    public void findTasksByUser_inProcessStatus_listWithOnlyOneElement(){
        List<Task> tasks = taskDAO.findByUser(worker, new Status[]{Status.IN_PROCESS}, "screen", Priority.values());
        assertThat(tasks).containsExactly(screenTask);
    }

    @Test
    public void findTasksByUser_unnexistsValue_emptyList(){
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "unnecessary task", Priority.values());
        assertThat(tasks).isEmpty();
    }

    @Test
    public void findTasksByUser_commonValue_listWithThreeElements(){
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "screen", Priority.values());
        assertThat(tasks).isEqualTo(getExpectedTasksByCommonName());
    }
    
    @Test
    public void findTasksByUser_overloadMethodWithProjectParam_commonValue_listWithThreeElements() {
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "screen", Priority.values(), project);
        assertThat(tasks).isEqualTo(getExpectedTasksByCommonName());
    }
    
    private List<Task> getExpectedTasksByCommonName() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, "screen", "create screen", Status.IN_PROCESS, Priority.MEDIA, 8, admin, worker, loginScreen));
        tasks.add(new Task(2, "screen", "create screen", Status.DELETED, Priority.MEDIA, 8, admin, worker, loginScreen));
        tasks.add(new Task(5, "screen", "create screen for welcome", Status.PENDING, Priority.ALTA, 8, admin, worker, welcomeScreen));
        tasks.add(new Task(8, "screen", "create screen for welcome", Status.ACCEPTED, Priority.ALTA, 8, admin, worker, welcomeScreen));
        return tasks;
    }

    @Test
    public void findTasksByUser_bajaPriority_emptyList(){
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "screen", new Priority[]{Priority.BAJA});
        assertThat(tasks).isEmpty();
    }

    @Test
    public void findTasksByUser_mediaPriority_listWithTwoElements(){
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "screen", new Priority[]{Priority.MEDIA});
        assertThat(tasks).isEqualTo(getExpectedMediaPriorityTasks());
    }
    
    @Test
    public void findByUser_overloadMethodWithProjectParam_mediaPriority_listWithTwoElements() {
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "screen", new Priority[]{Priority.MEDIA}, project);
        assertThat(tasks).isEqualTo(getExpectedMediaPriorityTasks());
    }

    private List<Task> getExpectedMediaPriorityTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, "screen", "create screen", Status.IN_PROCESS, Priority.MEDIA, 8, admin, worker, loginScreen));
        tasks.add(new Task(2, "screen", "create screen", Status.DELETED, Priority.MEDIA, 8, admin, worker, loginScreen));
        return tasks;
    }

    @Test
    public void findTasksByUser_companyNotExists_emptyList(){
        User unnexistUser = new User(100, "unnexists@none", "none", "none", Status.ACTIVE,
                new UserType(2, "None", Status.ACTIVE, new Company(1, "company", "description", Status.ACTIVE, 3)),
                false, company, "unnexists@none.com");
        List<Task> tasks = taskDAO.findByUser(unnexistUser, Status.values(), "", Priority.values());
        assertThat(tasks).isEmpty();
    }

    @Test
    public void findByUser_onlyCompanyParameter_listWithSixElements(){
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "", Priority.values());
        assertThat(tasks).isEqualTo(getAllTasksByCompany());
    }
    
    private List<Task> getAllTasksByCompany() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1, "screen", "create screen", Status.IN_PROCESS, Priority.MEDIA, 8, admin, worker, loginScreen));
        tasks.add(new Task(2, "screen", "create screen", Status.DELETED, Priority.MEDIA, 8, admin, worker, loginScreen));
        tasks.add(new Task(3, "service", "create service", Status.PENDING, Priority.BAJA, 8, admin, worker, loginScreen));
        tasks.add(new Task(4, "dao", "create dao", Status.PENDING, Priority.ALTA, 8, admin, worker, loginScreen));
        tasks.add(new Task(5, "screen", "create screen for welcome", Status.PENDING, Priority.ALTA, 8, admin, worker, welcomeScreen));
        tasks.add(new Task(8, "screen", "create screen for welcome", Status.ACCEPTED, Priority.ALTA, 8, admin, worker, welcomeScreen));
        tasks.add(openDoorTask);
        return tasks;
    }

    @Test
    public void findTasksByUser_overloadMethodWithProjectParam_finishedStatus_emptyList(){
        List<Task> tasks = taskDAO.findByUser(worker, new Status[]{Status.FINISHED}, "screen", Priority.values(), project);
        assertThat(tasks).isEmpty();
    }

    @Test
    public void findTasksByUser_overloadMethodWithProjectParam_inProcessStatus_listWithOnlyOneElement(){
        List<Task> tasks = taskDAO.findByUser(worker, new Status[]{Status.IN_PROCESS}, "screen", Priority.values(), project);
        assertThat(tasks).containsExactly(screenTask);
    }

    @Test
    public void findTasksByUser_overloadMethodWithProjectParam_unnexistsValue_emptyList(){
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "unnecessary task", Priority.values(), project);
        assertThat(tasks).isEmpty();
    }

    @Test
    public void findByUser_overloadMethodWithProjectParam_bajaPriority_emptyList(){
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "screen", new Priority[]{Priority.BAJA}, project);
        assertThat(tasks).isEmpty();
    }

    @Test
    public void findBy_overloadMethodWithProjectParam_companyNotExists_emptyList(){
        User unnexistUser = new User(100, "unnexists@none", "none", "none", Status.ACTIVE,
                new UserType(2, "None", Status.ACTIVE, new Company(1, "company", "description", Status.ACTIVE, 3)),
                false, company, "unnexists@none.com");
        List<Task> tasks = taskDAO.findByUser(unnexistUser, Status.values(), "", Priority.values(), project);
        assertThat(tasks).isEmpty();
    }

    @Test
    public void findByUser_overloadMethodWithProjectParam_projectWithOneTask_listWithOnlyOneElement(){
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "", Priority.values(), smallProject);
        assertThat(tasks).containsExactly(openDoorTask);
    }
    
    @Test
    public void findByUser_overloadMethodWithAimParam_aimWithOneTask_listWithOnlyOneElement(){
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "", Priority.values(), smallAim);
        assertThat(tasks).containsExactly(openDoorTask);
    }
    
    @Test
    public void findByUser_overloadMethodWithAimParam_nameNotExists_emptyList() {
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "NotExists", Priority.values(), smallAim);
        assertThat(tasks).isEmpty();
    }

    @Test
    public void findByUser_overloadMethodWithAimParam_deletedAim_emptyList() {
        List<Task> tasks = taskDAO.findByUser(worker, Status.values(), "NotExists", Priority.values(), welcomeScreenDeleted);
        assertThat(tasks).isEmpty();
    }
    
    @Test
    public void countActiveByUserOwner_userExists_five() {
        assertThat(taskDAO.countActiveByUserOwner(worker)).isEqualTo(5);
    }
    
    @Test
    public void countActiveByUserOwner_userNotExists_zero() {
        assertThat(taskDAO.countActiveByUserOwner(admin)).isEqualTo(0);
    }
    
    @Test
    public void findSummaryTime_taskExists_taskDtoId1WithZeroRealTime(){
        assertThat(taskDAO.findSummaryTime(1)).isEqualTo(getExpectedTaskDTO_id1_zeroRealTime());
    }
    
    private TaskDTO getExpectedTaskDTO_id1_zeroRealTime(){
        return new TaskDTO(1, "screen", 1, 2, 1, 8, BigDecimal.valueOf(0));
    }
    
    @Test
    public void findSummaryTime_taskExists_taskDtoId1WithFourRealTime() throws SQLException {
        insert4secondsTimeForTask1();
        assertThat(taskDAO.findSummaryTime(1)).isEqualTo(getExpectedTaskDTO_id1_fourRealTime());
    }

    private TaskDTO getExpectedTaskDTO_id1_fourRealTime() throws SQLException{
        return new TaskDTO(1, "screen", 1, 2, 1, 8, BigDecimal.valueOf(240));
    }
    
    @Test
    public void findSummaryTime_taskNotExists_null() {
        assertThat(taskDAO.findSummaryTime(100)).isNull();
    }
    
    @Test
    public void countFinishedByUserOwner_userExists_one() {
        assertThat(taskDAO.countFinishedByUserOwner(worker)).isEqualTo(1);
    }

    @Test
    public void countFinishedByUserOwner_userNotExists_zero() {
        assertThat(taskDAO.countFinishedByUserOwner(admin)).isEqualTo(0);
    }
    
    @Test
    public void countActiveByAim_aimWithOneDeletedAndThreeActive_three() {
        assertThat(taskDAO.countActiveByAim(loginScreen)).isEqualTo(3);
    }
    
    @Test
    public void countActiveByAim_aimWithOneActiveAndOneFinished_one() {
        assertThat(taskDAO.countActiveByAim(welcomeScreen)).isEqualTo(1);
    }
    
    @Test
    public void countActiveByAim_deletedAim_zero() {
        assertThat(taskDAO.countActiveByAim(welcomeScreenDeleted)).isEqualTo(0);
    }
    
    @Test
    public void countFinishByAim_aimWithOneDeletedAndThreeActive_zero() {
        assertThat(taskDAO.countFinishByAim(loginScreen)).isEqualTo(0);
    }

    @Test
    public void countFinishByAim_aimWithOneActiveAndOneFinished_one() {
        assertThat(taskDAO.countFinishByAim(welcomeScreen)).isEqualTo(1);
    }
    
    @Test
    public void countFinishByAim_deletedAim_zero() {
        assertThat(taskDAO.countFinishByAim(welcomeScreenDeleted)).isEqualTo(0);
    }

    private void insertInitialData() throws SQLException {
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl)session.getSession()).connection();
        connection.createStatement()
                .execute("INSERT INTO company(id_company, name, description, status, total_user)"
                        + " VALUES (1,'company','description', 'ACTIVE', 3)");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(1, 'Admin', 1, 'ACTIVE')");

        connection.createStatement().execute("insert into user_type (id_user_type, name, id_company, status)"+
                " values(2, 'Worker', 1, 'ACTIVE')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(1, 'password','admin@company', 1, 1, 'ACTIVE', false, 'admin', 'admin@company.com')");

        connection.createStatement().execute("insert into user (id_user, password, username, id_company, id_user_type," +
                " status, only_one_access, name, email)"+
                " values(2, 'password','worker@company', 1, 2, 'ACTIVE', false, 'worker', 'worker@company.com')");


        connection.createStatement().execute("insert into project(id_project, name, description, init_date, end_date, status, id_user) "+
                " values (1, 'Project', 'This is a really big project', '2010-01-01', '2020-12-31', 'ACTIVE', 1)");

        connection.createStatement().execute("insert into aim(id_aim, name, description, init_date, end_date, status, id_project, id_user)"+
                " values (1, 'Login dummy', 'Create a login dummy for demo presentation', '2010-01-01', '2010-01-05', 'ACTIVE', 1, 1)");

        connection.createStatement().execute("insert into aim(id_aim, name, description, init_date, end_date, status, id_project, id_user)"+
                " values (2, 'Welcome screen', 'Create a welcome screen', '2010-01-01', '2010-01-05', 'ACTIVE', 1, 1)");

        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"+
                " values(1, 'screen', 'create screen', 'IN_PROCESS', 1, 8, 1, 1, 2)");

        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"+
                " values(2, 'screen', 'create screen', 'DELETED', 1, 8, 1, 1, 2)");

        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"+
                " values(3, 'service', 'create service', 'PENDING', 2, 8, 1, 1, 2)");

        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"+
                " values(4, 'dao', 'create dao', 'PENDING', 0, 8, 1, 1, 2)");

        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"+
                " values(5, 'screen', 'create screen for welcome', 'PENDING', 0, 8, 2, 1, 2)");
        
        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"
                + " values(8, 'screen', 'create screen for welcome', 'ACCEPTED', 0, 8, 2, 1, 2)");

        connection.createStatement().execute("insert into project(id_project, name, description, init_date, end_date, status, id_user) "+
                " values (2, 'Small project', 'This is a really small project', '2010-01-01', '2020-12-31', 'ACTIVE', 1)");

        connection.createStatement().execute("insert into aim(id_aim, name, description, init_date, end_date, status, id_project, id_user)"+
                " values (3, 'Do workout', 'Go outside and do exercise.', '2010-01-01', '2010-01-05', 'ACTIVE', 2, 1)");

        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"+
                " values(6, 'Open the door', 'wake up and open the door', 'PENDING', 1, 8, 3, 1, 2)");
        
        connection.createStatement().execute("insert into aim(id_aim, name, description, init_date, end_date, status, id_project, id_user)"
                + " values (4, 'Welcome deleted', 'Create a welcome screen', '2010-01-01', '2010-01-05', 'DELETED', 1, 1)");
        
        connection.createStatement().execute("insert into task(id_task, name, description, status, priority, estimated_time, id_aim, id_user_requester, id_user_owner)"
                + " values(7, 'Open the door', 'wake up and open the door', 'PENDING', 1, 8, 4, 1, 2)");
    }
    
    private void insert4secondsTimeForTask1() throws SQLException{
        Session session = sessionFactory.getCurrentSession();
        Connection connection = ((SessionImpl) session.getSession()).connection();
        connection.createStatement().execute("insert into time(id_time, init_date, end_date, id_task)"+
                " values(1, '2020-06-24 20:53:00', '2020-06-24 20:57:00', 1)");
    }
}
