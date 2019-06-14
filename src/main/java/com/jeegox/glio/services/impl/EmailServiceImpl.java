package com.jeegox.glio.services.impl;

import com.jeegox.glio.entities.admin.User;
import com.jeegox.glio.services.EmailService;
import com.jeegox.glio.util.Constants;
import java.util.List;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

/**
 *
 * @author j2esus
 */
@Service
public final class EmailServiceImpl implements EmailService{
    @Autowired
    private JavaMailSender mailSender;
    
    @Override
    public void send(List<User> users, String subject, String text, boolean html) {
        mailSender.send(getMessagePreparator(users, subject, text, html));
    }
    
    private MimeMessagePreparator getMessagePreparator(List<User> users, String subject, String text, boolean html) {
 
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                
                mimeMessage.setFrom(Constants.Mail.EMAIL);
                mimeMessage.setSubject(subject);
                for(User user : users){
                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
                }
                if(html)
                    mimeMessage.setContent(text, "text/html");
                else
                    mimeMessage.setText(text);
            }
        };
        return preparator;
    }
    
}
