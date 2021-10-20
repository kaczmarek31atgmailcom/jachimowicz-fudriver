package com.fungisearch.fudriver.user.query.dto;

import java.util.List;

/**
 * Created by marcin on 24.03.16.
 */
public class UserDto {
    public Long id;
    public String login;
    public String name;
    public String surname;
    public Boolean active;
    public List<UserRoleDto> roles;
    public Long version;
}
