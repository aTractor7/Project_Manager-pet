package com.example.services.impl;

import com.example.entity.User;
import com.example.repositories.UserRepository;
import com.example.services.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService implements CrudService<User> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findOne(int id){
        return userRepository.findById(id).orElseThrow();
    }

    public Optional<User> findByUniqueParam(String username) {
        return userRepository.findByUsername(username);}

    @Transactional
    public void save(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
    }

    @Transactional
    public void update(int id, User updatedUser){
        userRepository.findById(id).ifPresentOrElse(u -> {
            u.setUsername(updatedUser.getUsername());
            u.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }, ()->{throw new IllegalArgumentException("update user exception");});
        userRepository.save(updatedUser);
    }

    @Transactional
    public void delete(int id){
        userRepository.deleteById(id);
    }
}