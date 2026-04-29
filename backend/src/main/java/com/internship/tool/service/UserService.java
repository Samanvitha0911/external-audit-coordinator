package com.internship.tool.service;

import com.internship.tool.entity.Role;
import com.internship.tool.entity.User;
import com.internship.tool.exception.DuplicateResourceException;
import com.internship.tool.exception.ResourceNotFoundException;
import com.internship.tool.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @org.springframework.cache.annotation.Cacheable(value = "users", key = "#id")
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Transactional(readOnly = true)
    @org.springframework.cache.annotation.Cacheable(value = "usersByEmail", key = "#email")
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<User> getAllUsers(org.springframework.data.domain.Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"users", "usersByEmail", "allUsers"}, allEntries = true)
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + user.getEmail());
        }
        // In a real scenario, password should be encoded here using PasswordEncoder
        return userRepository.save(user);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"users", "usersByEmail", "allUsers"}, allEntries = true)
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        if (!user.getEmail().equals(userDetails.getEmail()) && userRepository.existsByEmail(userDetails.getEmail())) {
             throw new DuplicateResourceException("Email already exists: " + userDetails.getEmail());
        }

        user.setFullName(userDetails.getFullName());
        user.setEmail(userDetails.getEmail());
        user.setRole(userDetails.getRole());
        user.setDepartment(userDetails.getDepartment());
        user.setPhoneNumber(userDetails.getPhoneNumber());
        user.setIsActive(userDetails.getIsActive());

        return userRepository.save(user);
    }

    @Transactional
    @org.springframework.cache.annotation.CacheEvict(value = {"users", "usersByEmail", "allUsers"}, allEntries = true)
    public void deleteUser(Long id) {
        User user = getUserById(id);
        // Instead of hard delete, perform a soft delete to maintain foreign key integrity
        user.setIsActive(false);
        userRepository.save(user);
    }
}
