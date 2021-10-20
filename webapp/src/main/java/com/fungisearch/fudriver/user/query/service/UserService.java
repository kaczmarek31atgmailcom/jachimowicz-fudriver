package com.fungisearch.fudriver.user.query.service;

import com.fungisearch.fudriver.user.query.dao.UserDao;
import com.fungisearch.fudriver.user.query.dto.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Long getCurrentUserId(){
        return userDao.getCurrentUserId();
    }
    public UserDto getCurrentUser(){
        return userDao.getCurrentUser();
    }
}
