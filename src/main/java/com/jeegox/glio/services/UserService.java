package com.jeegox.glio.services;

import com.jeegox.glio.dao.admin.CompanyDAO;
import com.jeegox.glio.dao.admin.SessionDAO;
import com.jeegox.glio.dao.admin.TokenDAO;
import com.jeegox.glio.dao.admin.UserDAO;
import com.jeegox.glio.dao.admin.UserTypeDAO;
import com.jeegox.glio.dao.aim.TaskDAO;
import com.jeegox.glio.dto.GenericResponse;
import com.jeegox.glio.dto.GraphProductivityDTO;
import com.jeegox.glio.dto.TaskDTO;
import com.jeegox.glio.dto.admin.OptionMenuUserTypeDTO;
import com.jeegox.glio.dto.admin.UserResponse;
import com.jeegox.glio.entities.admin.Company;
import com.jeegox.glio.entities.admin.OptionMenu;
import com.jeegox.glio.entities.admin.Session;
import com.jeegox.glio.entities.admin.Token;
import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.entities.admin.UserType;
import com.jeegox.glio.enumerators.Status;
import com.jeegox.glio.enumerators.StatusResponse;
import com.jeegox.glio.util.Util;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    
    private final UserDAO userDAO;
    private final SessionDAO sessionDAO;
    private final CompanyDAO companyDAO;
    private final TaskDAO taskDAO;
    private final UserTypeDAO userTypeDAO;
    private final TokenDAO tokenDAO;

    @Autowired
    public UserService(UserDAO userDAO, SessionDAO sessionDAO, CompanyDAO companyDAO, TaskDAO taskDAO, UserTypeDAO userTypeDAO, TokenDAO tokenDAO) {
        this.userDAO = userDAO;
        this.sessionDAO = sessionDAO;
        this.companyDAO = companyDAO;
        this.taskDAO = taskDAO;
        this.userTypeDAO = userTypeDAO;
        this.tokenDAO = tokenDAO;
    }
    
    @Transactional(readOnly = true)
    public List<User> findAll(){
        return userDAO.findAll();
    }
    
    @Transactional
    public Session login(String username, String password) throws Exception{
        Session session = null;
        User user = userDAO.login(username, password);
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
    public List<User> findByCompany(Company company) {
        return userDAO.findByCompany(company);
    }

    @Transactional(readOnly = true)
    public Long count(Company company) {
        return userDAO.count(company);
    }

    @Transactional
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
    public User findById(Integer id) {
        return userDAO.findById(id);
    }

    @Transactional
    public void changeStatus(User user, Status status) throws Exception {
        user.setStatus(status);
        userDAO.save(user);
    }

    @Transactional
    public void saveOrUpdate(User user) throws Exception {
        userDAO.save(user);
    }

    @Transactional
    public UserResponse getToken(String username, String password) {
        UserResponse userResponse = null;
        try{
            Session session = login(username, password);
            User user = session.getFather();
            if(user != null){
                userResponse = new UserResponse(StatusResponse.OK,"OK",user.getId(), user.getUsername(), 
                    generateToken(user),user.getFather().getId(),user.getFather().getName(), 
                    user.getUserType().getId(), user.getUserType().getName(),session.getSession(), user.getUserType().getStatus().name());
            }
        }catch(Exception e){
            userResponse = new UserResponse(StatusResponse.FAILURE,
                e.getMessage(),0,"","",0,"",0,"","","");
        }
        return userResponse;
    }

    @Transactional(readOnly = true)
    public User findById(String username) {
        return userDAO.findByUsername(username);
    }

    @Transactional
    public void save(User user) throws Exception {
        Company c = this.companyDAO.findById(user.getFather().getId());
        int count = user.getId() == null ? 1 : 0;
        if(this.count(c) + count > c.getTotalUser()){
            throw new Exception("Haz llegado a tu límite de usuarios permitidos.");
        }else{
            this.saveOrUpdate(user);
        }
    }

    @Transactional(readOnly = true)
    public List<GraphProductivityDTO> findDataGraphProductivity(Company company, Date initDate, Date endDate, Integer idProject, Integer idAim) {
        List<GraphProductivityDTO> result = new ArrayList<>();
        List<User> users = userDAO.findByCompany(company);
        Status[] status = new Status[]{Status.IN_PROCESS, Status.PAUSED, Status.FINISHED, Status.ACCEPTED};
        
        List<TaskDTO> tasks = null;
        GraphProductivityDTO graphDTO = null;
        BigDecimal quantity = BigDecimal.ZERO;
        
        for(User user: users){
            graphDTO = new GraphProductivityDTO();
            tasks = taskDAO.findBy(user, status, initDate, endDate, idProject, idAim);
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
    public List<Map> findActivityData(Company company, Date initDate, Date endDate, Integer idProject, Integer idAim) {
        List<Map> result = new ArrayList<>();
        List<User> users = userDAO.findByCompany(company);
        Status[] status = new Status[]{Status.IN_PROCESS, Status.PAUSED, Status.FINISHED, Status.ACCEPTED};
        List<TaskDTO> tasks = null;
        Map<String,Object> map = null;
        for(User user: users){
            tasks = taskDAO.findBy(user, status, initDate, endDate, idProject, idAim);
            map = new HashMap<>();
            map.put("user", user);
            map.put("tasks", tasks);
            result.add(map);
        }
        return result;
    }
    
    @Transactional(readOnly = true)
    public List<UserType> findUserTypeAll() {
        return userTypeDAO.findAll();
    }

    @Transactional(readOnly = true)
    public List<UserType> findUserTypeByCompany(Company company) {
        return userTypeDAO.findByCompany(company);
    }

    @Transactional(readOnly = true)
    public UserType findUserTypeById(Integer id) {
        return userTypeDAO.findById(id);
    }

    @Transactional
    public void changeStatus(UserType userType, Status status) throws Exception {
        userType.setStatus(status);
        userTypeDAO.save(userType);
    }

    @Transactional
    public void save(Integer id, String name, Status status, Company company) throws Exception {
        UserType userType = this.findUserTypeById(id);
        if(userType == null){
            userType = new UserType(null, name, status, company);
        }else{
            userType.setName(name);
            userType.setStatus(status);
        }
        userTypeDAO.save(userType);
    }

    @Transactional(readOnly = true)
    public List<OptionMenuUserTypeDTO> findOptionsMenu(Integer idUserType) {
        UserType userType = userTypeDAO.findById(idUserType);
        List<OptionMenuUserTypeDTO> options = userTypeDAO.findOptionsMenu();
        Set<OptionMenu> optionUser = userType.getOptions();
        Iterator<OptionMenu> it = optionUser.iterator();
        while(it.hasNext()){
            OptionMenu om = it.next();
            for(OptionMenuUserTypeDTO item : options){
                if(item.getIdOptionMenu().equals(om.getId())){
                    item.setAssigned(true);
                    continue;
                }
            }
            it.remove();
        }
        return options;
    }

    @Transactional
    public void saveOptions(Integer idUserType, String[] optionsAdd, String[] optionsDel) throws Exception {
        if(optionsAdd.length > 0){
            for(String option: optionsAdd){
                if(userTypeDAO.findOption(idUserType, option).intValue() == 0)
                    userTypeDAO.addOption(idUserType, option);
            }
        }
        
        if(optionsDel.length > 0){
            userTypeDAO.deleteOptions(idUserType, optionsDel);
        }
    }
    
    @Transactional(readOnly = true)
    public List<Token> findTokenAll() {
        return tokenDAO.findAll();
    }

    @Transactional(readOnly = true)
    public Token find(User user) {
        return tokenDAO.getActive(user);
    }

    @Transactional(readOnly = true)
    public List<Token> findByUser(User user) {
        return tokenDAO.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Token find(Status status, String token) {
        return tokenDAO.find(status, token);
    }

    @Transactional(readOnly = true)
    public List<Token> findTokenByCompany(Company company) {
        return tokenDAO.findByCompany(company);
    }

    @Transactional
    public void changeStatus(Token token, Status status) throws Exception {
        token.setStatus(status);
        tokenDAO.save(token);
    }

    @Transactional(readOnly = true)
    public Token findTokenById(Integer id) {
        return tokenDAO.findById(id);
    }

    @Transactional(readOnly = true)
    public String generateToken(User user) {
        String sToken = "";
        Token token = tokenDAO.getActive(user);
        if(token == null){
            SecureRandom random = new SecureRandom();
            sToken = new BigInteger(130, random).toString(32);
            token = new Token();
            token.setStatus(Status.ACTIVE);
            token.setFather(user);
            token.setToken(sToken);
            tokenDAO.save(token);
        } else{
            sToken = token.getToken();
        }
        return sToken;
    }

    @Transactional(readOnly = true)
    public GenericResponse validateToken(String token) {
        GenericResponse response = new GenericResponse();
        Token tokenO = tokenDAO.find(Status.ACTIVE, token);
        if(tokenO != null){
            response.setMessage("OK");
            response.setStatusResponse(StatusResponse.OK);
        }else{
            response.setMessage("El token is inválido.");
            response.setStatusResponse(StatusResponse.FAILURE);
        }
        return response;
    }
    
    @Transactional(readOnly = true)
    public List<Session> findSessionAll() {
        return sessionDAO.findAll();
    }

    @Transactional
    public void changeStatus(Session session, Status status) throws Exception {
        session.setStatus(status);
        session.setEndDate(Util.getCurrentCompleteDate());
        sessionDAO.save(session);
    }

    @Transactional(readOnly = true)
    public List<Session> findSessionByUser(User user) {
        return sessionDAO.findByUser(user);
    }

    @Transactional(readOnly = true)
    public Session findSessionById(Integer id) {
        return sessionDAO.findById(id);
    }
    
    @Transactional(readOnly = true)
    public List<User> findByLike(Company company, String nameLike) {
        return userDAO.findByCompany(company, nameLike);
    }
}
