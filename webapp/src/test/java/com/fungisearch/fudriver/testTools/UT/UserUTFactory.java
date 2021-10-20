package com.fungisearch.fudriver.testTools.UT;

import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.user.command.repository.UserRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

/**
 * Created by marcin on 13.04.17.
 */
public class UserUTFactory {
private final UserRepository userRepository;
private final BeanValidator beanValidator;

    public UserUTFactory(UserRepository userRepository, BeanValidator beanValidator) {
        this.userRepository = userRepository;
        this.beanValidator = beanValidator;
    }

    public User create(){
        User user = new User.UserBuilder(userRepository,beanValidator)
                .login("test_login")
                .id(1L)
                .name("test_name")
                .surname("test_surname")
                .passwrod("test_password")
                .roleLeader(true)
                .build();
        user.create();
        return user;
    }
}
