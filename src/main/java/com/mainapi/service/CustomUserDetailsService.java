package com.mainapi.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mainapi.model.Role;
import com.mainapi.model.User;
import com.mainapi.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		List<String> privileges = getPrivileges(user.getRoles());
		List<GrantedAuthority> auth = new ArrayList<>();
		
		for (String privilege : privileges) {
			auth.add(new SimpleGrantedAuthority(privilege));
        }		
		String password = user.getPassword();
		return new org.springframework.security.core.userdetails.User(username, password,
				auth);
	}
	
	private List<String> getPrivileges(Collection<Role> roles) {	  
        List<String> privileges = new ArrayList<>();
        for (Role role : roles) {
        	privileges.add(role.getAuthority());
        }
        return privileges;
    }

}