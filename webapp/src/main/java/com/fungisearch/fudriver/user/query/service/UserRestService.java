package com.fungisearch.fudriver.user.query.service;

import com.fungisearch.fudriver.user.query.dao.UserDao;
import com.fungisearch.fudriver.user.query.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marcin on 24.03.16.
 */
@RestController
public class UserRestService {

    @Autowired
    UserDao userDao;


    @RequestMapping(value = "/rest/user", params = "active", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    List<UserDto> getUsers(@RequestParam(value = "active") Boolean active) {
        List<UserDto> users = new ArrayList<UserDto>();
        if (active) {
            users = userDao.getActiveUsers();
        }
        return users;
    }

    @RequestMapping(value = "/rest/user/login", method = RequestMethod.GET, produces="application/json; charset=UTF-8")
    List<String> getLogins(){
        return userDao.getAllLogins();
    }

}
