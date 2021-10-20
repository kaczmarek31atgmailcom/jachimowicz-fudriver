package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUser {

    private final UserFactory userFactory;

    @Autowired
    public CreateUser(UserFactory userFactory) {
        this.userFactory = userFactory;
    }

    public User create() {
        User user = userFactory.builder()
                .login("test_login")
                .name("test_name")
                .surname("test_surname")
                .passwrod("test_password")
                .removed(false)
                .roleDostepy(true)
                .roleLeader(true)
                .rolePalety(true)
                .rolePanel(true)
                .roleRozliczenia(true)
                .roleWaga(true)
                .build();
        user.create();
        return user;
    }
}
