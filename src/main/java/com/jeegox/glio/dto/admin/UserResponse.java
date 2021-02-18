package com.jeegox.glio.dto.admin;

import com.jeegox.glio.dto.GenericResponse;
import com.jeegox.glio.enumerators.StatusResponse;
import java.io.Serializable;

public class UserResponse extends GenericResponse implements Serializable{
    private UserDTO userDTO;
    
    public UserResponse(){
        
    }
    
    public UserResponse(StatusResponse statusResponse, String message, Integer idUser, 
            String username, String token,Integer idCompany,String companyName, Integer idUserType, String nameUserType,
                String session, String userTypeStatus){
        this.statusResponse = statusResponse;
        this.message = message;
        userDTO = new UserDTO();
        userDTO.setId(idUser);
        userDTO.setUsername(username);
        userDTO.setToken(token);
        userDTO.setSession(session);
        CompanyDTO company = new CompanyDTO();
        company.setId(idCompany);
        company.setName(companyName);
        userDTO.setCompany(company);
        UserTypeDTO userType = new UserTypeDTO();
        userType.setId(idUserType);
        userType.setName(nameUserType);
        userType.setStatus(userTypeStatus);
        userDTO.setUserType(userType);
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
