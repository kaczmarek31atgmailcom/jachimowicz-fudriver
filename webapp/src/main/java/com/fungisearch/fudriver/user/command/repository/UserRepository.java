package com.fungisearch.fudriver.user.command.repository;

import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.user.command.model.UserRole;

import java.util.List;

/**
 * Created by marcin on 25.03.16.
 */
public interface UserRepository {
    void save(User user);
    void saveUserRole(UserRole userRole);
    void update(User user);
    void delete(UserRole userRole);
    UserRole findByUserIdAndRoleName(Long userId, String roleName);
    User getReference(Long userId);
    User find(Long userId);
    List<UserRole> findUserRoles(Long userId);
}
