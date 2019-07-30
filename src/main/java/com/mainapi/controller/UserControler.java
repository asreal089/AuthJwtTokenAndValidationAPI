package com.mainapi.controller;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mainapi.model.Role;
import com.mainapi.model.User;
import com.mainapi.model.VerificationToken;
import com.mainapi.repository.RoleRepository;
import com.mainapi.repository.UserRepository;
import com.mainapi.repository.VerificationTokenRepository;
import com.mainapi.service.NotificationService;

@Controller
public class UserControler {
	
	private Logger logger = LoggerFactory.getLogger(UserControler.class);
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
	
	
	@Autowired
	private VerificationTokenRepository tokenRepo;
	
	@Autowired
	private NotificationService notificationService;
	
	
	@PostMapping(path="/register")
	public ResponseEntity<User> create(@RequestBody User user){
		User existingUser = userRepo.findByUsername(user.getUsername());
        if(existingUser != null)
        {
        	return ResponseEntity.notFound().build();
        }
        else
        {
        	user.setPassword(encoder().encode(user.getPassword()));
            this.userRepo.save(user);

            VerificationToken confirmationToken = new VerificationToken(user);

            tokenRepo.save(confirmationToken);
            
            try {
    			notificationService.sendNotification(user, confirmationToken);
    		} catch (MailException e) {
    			logger.info("Error sending email:" + e.getMessage());
    		}

          }

        return ResponseEntity.ok().build();

	}
	


	@GetMapping(path="/confirm-account")
    public ResponseEntity<Object> confirmUserAccount(@RequestParam("token")String confirmationToken)
    {
        VerificationToken token = tokenRepo.findByToken(confirmationToken);
        if(token != null)
        {
            User user = userRepo.findByUsername(token.getUser().getUsername());
            user.setEnable(true);
            Set<Role> Roles = new HashSet<Role>();
            Roles.add(roleRepo.findByName("user"));
            user.setRoles(Roles);
            userRepo.save(user);
        }
        else
        {
        	return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(true);
    }
    
        
}
