package com.mainapi.repository;
import com.mainapi.model.Role;


import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long>{
	
	 Role findByName(String name);
}

