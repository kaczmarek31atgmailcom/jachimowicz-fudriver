package com.fungisearch.fudriver.user.query.dao;

import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.user.query.dto.UserDto;

import java.util.List;

/**
 * Created by kaczmarm on 2015-01-22.
 */
public interface UserDao {

    Long getCurrentUserId();
    List<UserDto> getActiveUsers();
    List<String> getAllLogins();
    UserDto getCurrentUser();
}
