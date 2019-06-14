package com.jeegox.glio.services;

import com.jeegox.glio.entities.admin.User;
import java.util.List;

/**
 *
 * @author j2esus
 */
public interface EmailService {
    
    void send(List<User> users, String subject, String text, boolean html);
}
