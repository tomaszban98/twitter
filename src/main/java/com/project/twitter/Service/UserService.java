package com.project.twitter.Service;

import com.project.twitter.Model.User;
import com.project.twitter.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveUser(User user) {

            if(user.getId()==null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            userRepository.save(user);



    }

    public void deleteUser(User user) {

        userRepository.delete(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getOne(long id) {

        return userRepository.getOne(id);
    }

    public User getUserByLogin(String login) {
        return userRepository.getUserByLogin(login)
                .orElseThrow(() -> new RuntimeException("Brak usera o loginie: " + login));
    }

    public boolean existsByLogin(String login) {

        return userRepository.existsByLogin(login);
    }



}
