package com.jeegox.glio.services.admin.impl;

import com.jeegox.glio.dao.admin.SessionDAO;
import com.jeegox.glio.dao.admin.UserDAO;
import com.jeegox.glio.dao.aim.TaskDAO;
import com.jeegox.glio.dto.GraphProductivityDTO;
import com.jeegox.glio.dto.TaskDTO;
import com.jeegox.glio.dto.admin.UserResponse;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.enumerators.StatusResponse;
import com.jeegox.glio.services.admin.CompanyService;
import com.jeegox.glio.services.admin.TokenService;
import com.jeegox.glio.services.admin.UserService;
import com.jeegox.glio.util.Util;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author j2esus
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private SessionDAO sessionDAO;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private TaskDAO taskDAO;
    
    @Transactional(readOnly = true)
    @Override
    public List<User> findAll(){
        return userDAO.findAll();
    }
    
    @Transactional
    @Override
    public Session login(String username, String password, String token) throws Exception{
        Session session = null;
        User user = userDAO.login(username, password, token);
        if(user != null){
            //buscar sesion abierta
            session = sessionDAO.findOpenSession(user);
            if(session == null){
                session = new Session();
                session.setStatus(Status.ACTIVE);
                session.setFather(user);
                session.setInitDate(Util.getCurrentCompleteDate());
                session.setSession(Util.getRandom(50));
                sessionDAO.save(session);
            }else{
                if(user.getOnlyOneAccess()){
                    //throw new Exception("There is an open session with this user in another device.");
                    throw new Exception("La sesión de este usuario está siendo utilizada en otro dispositivo");
                }
            }
        }else{
            //throw new Exception("The username or password you entered is incorrect.");
            throw new Exception("El nombre de usuario y/o contraseña son incorrectos.");
        }
        return session;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findByCompany(Company company) {
        return userDAO.findByCompany(company);
    }

    @Transactional(readOnly = true)
    @Override
    public Long count(Company company) {
        return userDAO.count(company);
    }

    @Transactional
    @Override
    public User changePassword(User userConfirm, Integer idUser, String password, String newPassword, String confirmPassword) throws Exception {
        try{
            User user = this.userDAO.findById(idUser);
            if(userConfirm != null){
                if(!user.getId().equals(userConfirm.getId())){
                    throw new Exception("El usuario no corresponde con el de la sesión.");
                }
            }else{
                throw new Exception("Usuario incorrecto");
            }
            
            if(user.getPassword().equals(password)){
                if(newPassword.equals(confirmPassword)){
                    user.setPassword(newPassword);
                    userDAO.save(user);
                }else{
                    throw new Exception("El nuevo password y la confirmación son diferentes");
                }
            }else{
                throw new Exception("Password actual incorrecto");
            }
            return user;
        }catch(Exception e){
            throw e;
        }
    }

    @Transactional
    @Override
    public User changeUserData(Integer idUser, String username, String name, String email) throws Exception {
        try{
            User user = this.userDAO.findById(idUser);
            if(user != null){
                user.setName(name);
                user.setUsername(username);
                user.setEmail(email);
                userDAO.save(user);
            }else{
                throw new Exception("Usuario incorrecto");
            }
            return user;
        }catch(Exception e){
            throw e;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Integer id) {
        return userDAO.findById(id);
    }

    @Transactional
    @Override
    public void changeStatus(User user, Status status) throws Exception {
        user.setStatus(status);
        userDAO.save(user);
    }

    @Transactional
    @Override
    public void saveOrUpdate(User user) throws Exception {
        userDAO.save(user);
    }

    @Transactional
    @Override
    public UserResponse getToken(String username, String password) {
        UserResponse userResponse = null;
        try{
            Session session = login(username, password,"");
            User user = session.getFather();
            if(user != null){
                userResponse = new UserResponse(StatusResponse.OK,"OK",user.getId(), user.getUsername(), 
                    tokenService.generateToken(user),user.getFather().getId(),user.getFather().getName(), 
                    user.getUserType().getId(), user.getUserType().getName(),session.getSession(), user.getUserType().getStatus().name());
            }
        }catch(Exception e){
            userResponse = new UserResponse(StatusResponse.FAILURE,
                e.getMessage(),0,"","",0,"",0,"","","");
        }
        return userResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(String username) {
        return userDAO.findById(username);
    }

    @Transactional
    @Override
    public void save(User user) throws Exception {
        Company c = this.companyService.findBydId(user.getFather().getId());
        int count = user.getId() == null ? 1 : 0;
        if(this.count(c) + count > c.getTotalUser()){
            throw new Exception("Haz llegado a tu límite de usuarios permitidos.");
        }else{
            this.saveOrUpdate(user);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<GraphProductivityDTO> findDataGraphProductivity(Company company, Date initDate, Date endDate, Integer idProject) {
        List<GraphProductivityDTO> result = new ArrayList<>();
        List<User> users = userDAO.findByCompany(company);
        Status[] status = new Status[]{Status.IN_PROCESS, Status.PAUSED, Status.FINISHED, Status.ACCEPTED};
        
        List<TaskDTO> tasks = null;
        GraphProductivityDTO graphDTO = null;
        BigDecimal quantity = BigDecimal.ZERO;
        
        for(User user: users){
            graphDTO = new GraphProductivityDTO();
            tasks = taskDAO.findBy(user, status, initDate, endDate, idProject);
            float total = 0;
            for(TaskDTO item: tasks){
                double multiplo = 0;
                float real = item.getRealTime() != null? item.getRealTime().floatValue() : 0F;
                float dif = item.getEstimatedTime().floatValue() - real;
                float prom = (item.getEstimatedTime().floatValue() + real);
                if(item.getPriority().equals(0)){
                    multiplo = 0.3;
                }else if(item.getPriority().equals(2)){
                    multiplo = 0.1;
                }else{
                    multiplo = 0.2;
                }
                total += (prom*multiplo)-(dif*multiplo);
            }
            quantity = new BigDecimal(total).setScale(2, RoundingMode.HALF_UP);
            graphDTO.setIdUser(user.getId());
            graphDTO.setUsername(user.getUsername());
            graphDTO.setQuantity(quantity);
            result.add(graphDTO);
        }
        
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Map> findActivityData(Company company, Date initDate, Date endDate, Integer idProject) {
        List<Map> result = new ArrayList<>();
        List<User> users = userDAO.findByCompany(company);
        Status[] status = new Status[]{Status.IN_PROCESS, Status.PAUSED, Status.FINISHED, Status.ACCEPTED};
        List<TaskDTO> tasks = null;
        Map<String,Object> map = null;
        for(User user: users){
            tasks = taskDAO.findBy(user, status, initDate, endDate, idProject);
            map = new HashMap<>();
            map.put("user", user);
            map.put("tasks", tasks);
            result.add(map);
        }
        return result;
    }
    
}
