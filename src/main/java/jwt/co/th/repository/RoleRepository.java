package jwt.co.th.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jwt.co.th.model.Role;

@Repository("RoleRepository")
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Role findByName(String name);

}