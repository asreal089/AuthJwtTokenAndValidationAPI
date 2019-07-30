package com.mainapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.mainapi.model.User;
import com.mainapi.model.VerificationToken;

@Service
public class NotificationService {
	private JavaMailSender javaMail;
	
	@Autowired
	public NotificationService(JavaMailSender javaMail){
		this.javaMail = javaMail;
	}
	
	public void sendNotification(User user, VerificationToken token) throws MailException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getUsername());
		mail.setSubject("Complete Registration!");
		mail.setFrom("lexx_89@hotmail.com");
		mail.setText("To confirm your account, please click here : "
        +"http://localhost:8080/confirm-account?token="+token.getToken());
        
		
		javaMail.send(mail);
	}
}
