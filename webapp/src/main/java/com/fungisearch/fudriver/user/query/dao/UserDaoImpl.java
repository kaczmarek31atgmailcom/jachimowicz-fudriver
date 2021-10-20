package com.fungisearch.fudriver.user.query.dao;

import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.user.query.dto.UserDto;
import com.fungisearch.fudriver.user.query.dto.UserRoleDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Long getCurrentUserId() {
            org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String name = user.getUsername();
            String sql = "select id from users where user_name = ? and removed = 0";
            Long userId = jdbcTemplate.queryForObject(sql, Long.class, new Object[]{name});
        return userId;
    }



    @Override
    public List<UserDto> getActiveUsers() {
        List<UserDto> users = jdbcTemplate.query("select u.id as id, " +
                "u.user_name as login, " +
                "u.imie as name, " +
                "u.nazwisko as surname," +
                "r.user_role_id as roleId, " +
                "r.role as roleName, " +
                "u.version as version from " +
                "users u left join user_roles r on r.username = u.user_name where u.removed = 0 and u.id > 1", new UsersResultSetExtractor());
        return users;
    }

    @Override
    public List<String> getAllLogins() {
        List<String> logins = (List<String>)jdbcTemplate.queryForList("select distinct user_name from users", String.class);
        return logins;
    }

    class UsersResultSetExtractor implements ResultSetExtractor<List<UserDto>> {

        @Override
        public List<UserDto> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Long, UserDto> map = new LinkedHashMap<Long, UserDto>();
            UserDto user = null;
            while(rs.next()){
                long userId = rs.getLong("id");
                if(map.containsKey(userId)){
                    user = map.get(userId);
                } else {
                    user = new UserDto();
                    user.id = (userId);
                    user.login = rs.getString("login");
                    user.name = rs.getString("name");
                    user.surname = rs.getString("surname");
                    user.active = true;
                    user.version = rs.getLong("version");
                    List<UserRoleDto> roles = new ArrayList<UserRoleDto>();
                    user.roles = roles;
                }
                UserRoleDto role = new UserRoleDto();
                role.id = rs.getLong("roleId");
                role.name = rs.getString("roleName");
                user.roles.add(role);
                map.put(userId,user);
            }

            return new ArrayList<UserDto>(map.values());
        }
    }
    @Override
    public UserDto getCurrentUser() {
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = user.getUsername();
        return jdbcTemplate.query("select id as id, " +
                "user_name as login, " +
                "nazwisko as surname, " +
                "imie as name " +
                " from users where user_name = ? ", new Object[]{name}, new UserDtoResultSetExtractor());
    }

    private static class UserDtoResultSetExtractor implements ResultSetExtractor<UserDto>{

        @Override
        public UserDto extractData(ResultSet rs) throws SQLException {
            UserDto dto = new UserDto();
            while(rs.next()){
                dto.id = rs.getLong("id");
                dto.login = rs.getString("login");
                dto.name = rs.getString("name");
                dto.surname = rs.getString("surname");
            }
            return dto;
        }
    }
}