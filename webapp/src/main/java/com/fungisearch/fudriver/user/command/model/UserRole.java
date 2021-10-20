package com.fungisearch.fudriver.user.command.model;

import com.fungisearch.fudriver.user.command.repository.UserRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by marcin on 26.03.16.
 */
@Entity
@Table(name="user_roles")
public class UserRole {

    private transient UserRepository userRepository;
    private transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    @Column(name = "user_role_id")
    private Long userRoleId;

    @Column(name="username")
    @NotNull
    private String username;

    @Column(name="user_id")
    @NotNull
    private Long userId;

    @Column(name="ROLE")
    @NotNull
    private String roleName;

    UserRole(UserRepository userRepository, BeanValidator beanValidator){
        this.userRepository = userRepository;
        this.beanValidator = beanValidator;
    }

    public Long getUserRoleId() {
        return userRoleId;
    }

    public void create(){
        UserRole userRole = userRepository.findByUserIdAndRoleName(this.userId, this.roleName);
        if (userRole == null) {
            beanValidator.validate(this);
            userRepository.saveUserRole(this);
        }
    }

    public void remove(){
        UserRole userRole = userRepository.findByUserIdAndRoleName(this.userId, this.roleName);
        if(userRole != null) {
            userRepository.delete(userRole);
        }
    }



    private UserRole(){}

    public static class UserRoleBuilder{
        private UserRepository userRepository;
        private BeanValidator beanValidator;
        private Long userRoleId;
        private String username;
        private Long userId;
        private String roleName;

        public UserRoleBuilder(UserRepository userRepository, BeanValidator beanValidator){
            this.userRepository = userRepository;
            this.beanValidator = beanValidator;
        }

        public UserRoleBuilder userRoleId(Long userRoleId){
            this.userRoleId = userRoleId;
            return this;
        }

        public UserRoleBuilder username(String username){
            this.username = username;
            return this;
        }

        public UserRoleBuilder userId(Long userId){
            this.userId = userId;
            return this;
        }
        public UserRoleBuilder roleName(String roleName){
            this.roleName = roleName;
            return this;
        }

        public UserRole build(){
            UserRole userRole = new UserRole(this.userRepository, this.beanValidator);
            userRole.userRoleId = this.userRoleId;
            userRole.username = this.username;
            userRole.userId = this.userId;
            userRole.roleName = this.roleName;
            return userRole;
        }
    }
}
