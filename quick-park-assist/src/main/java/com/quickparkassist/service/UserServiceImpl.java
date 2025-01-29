package com.quickparkassist.service;


import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.quickparkassist.dto.UserRegistrationDto;
import com.quickparkassist.model.User;
import com.quickparkassist.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        // Now the role is a String instead of Role entity
        User user = new User(
                registrationDto.getFullName(),
                registrationDto.getEmail(),
                passwordEncoder.encode(registrationDto.getPassword()),
                registrationDto.getAvailability(),
                registrationDto.getPhoneNumber(),
                registrationDto.getAddress(),
                registrationDto.getRole()
        );

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve user from the repository by email
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        // Instead of roles being a collection of Role objects, it's now a collection of
        // Strings
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRole()) // Mapping the String role to authorities
        );
    }

    // Since the role is now a String, mapRolesToAuthorities only needs to handle a
    // single String role
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role)); // Wrap the String in a
        // SimpleGrantedAuthority
    }
    @Override
    public Long findUserIdByUsername(String email) {
        return userRepository.findIdByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User with email not found: " + email));
    }
    // view all user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        return user.orElse(null); // Return the user if found, else return null
    }

    // Save or update a user
    public void saveUser(User user) {
        userRepository.save(user); // If user exists, it will update, otherwise it will create a new user
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
        // Return the user if found, else return null
    }


    public void deleteUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        userRepository.delete(user);  // Delete user from the repository by email
    }

    @Override
    public String findMobileNumberByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return user.getPhoneNumber();  // Access mobileNumber from the User entity
    }
    @Override
    public User findByUsername(String username) {
        return userRepository.findByEmail(username);  // Assuming username is email
    }

    @Override
    public User findUserById(Long id) {
        // Find and return the user by ID from the repository
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username);
    }
}
