package com.fungisearch.fudriver.user.command.model;

import com.fungisearch.fudriver.user.command.repository.UserRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 26.03.16.
 */
@Service
public class UserFactory {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BeanValidator beanValidator;

    public User.UserBuilder builder(){
        User.UserBuilder userBuilder = new User.UserBuilder(userRepository,beanValidator);
        return userBuilder;
    }

    public User find(Long id){
        User user = userRepository.find(id);
        if(user != null){
            user.beanValidator = beanValidator;
            user.userRepository = userRepository;
        }
    return user;
    }
    
}
