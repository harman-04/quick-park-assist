package com.quickparkassist.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quickparkassist.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
	Optional<User> findIdByEmail(String email);  // Return Optional<User> to use map

Optional<User> findById(Long userId);

}
