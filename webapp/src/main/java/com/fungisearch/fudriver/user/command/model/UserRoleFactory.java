package com.fungisearch.fudriver.user.command.model;

import com.fungisearch.fudriver.user.command.repository.UserRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 26.03.16.
 */
@Service
public class UserRoleFactory {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BeanValidator beanValidator;

    public UserRole.UserRoleBuilder userRoleBuilder() {
     UserRole.UserRoleBuilder builder  =new UserRole.UserRoleBuilder(userRepository, beanValidator);
        return builder;
    }
}
