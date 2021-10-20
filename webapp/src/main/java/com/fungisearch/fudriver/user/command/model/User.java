package com.fungisearch.fudriver.user.command.model;

import com.fungisearch.fudriver.user.command.repository.UserRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
public class User {

    public transient UserRepository userRepository;

    public transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "user_name")
    @NotNull
    private String login;

    @Column(name = "imie")
    private String name;

    @Column(name = "nazwisko")
    private String surname;

    @Column(name = "passwd")
    private String password;

    @Column(name= "active")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @NotNull
    private Boolean active;


    @Column(name = "removed")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    @NotNull
    private Boolean removed;

    @Column(name = "waga")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean roleWaga;

    @Column(name = "palety")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean rolePalety;

    @Column(name = "panel")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean rolePanel;

    @Column(name = "brygadzista")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean roleLeader;

    @Column(name = "dostepy")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean roleDostepy;

    @Column(name = "rozliczenia")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean roleRozliczenia;

    @Version
    @Column(name = "version")
    private Long version;

    public User(UserRepository userRepository, BeanValidator beanValidator) {
        this.userRepository = userRepository;
        this.beanValidator = beanValidator;
    }

    @SuppressWarnings("unused")
    private User() {
    }

    public Long getId() {
        return this.id;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public Long create() {
        this.removed = false;
        this.active = true;
        this.password = DigestUtils.md5Hex(this.password);
        beanValidator.validate(this);
        userRepository.save(this);
        return this.id;
    }

    public void edit(Edit edit){
        this.login = edit.login;
        this.name = edit.name;
        this.surname = edit.surname;
        if(edit.password.length() > 0){
            this.password = DigestUtils.md5Hex(edit.password);
        }
        this.roleDostepy = edit.roleDostepy;
        this.roleWaga = edit.roleWaga;
        this.rolePalety = edit.rolePalety;
        this.roleLeader = edit.roleLeader;
        this.roleRozliczenia = edit.roleRozliczenia;
        this.rolePanel = edit.rolePanel;
        this.version = edit.version;
        this.active = true;
        this.removed = false;
        beanValidator.validate(this);
    }

public void remove(){
    this.removed = true;
    this.active = false;
    beanValidator.validate(this);
}

    public static class Edit{
        private Long id;
        private String login;
        private String name;
        private String surname;
        private String password;
        private Boolean roleWaga;
        private Boolean rolePalety;
        private Boolean rolePanel;
        private Boolean roleLeader;
        private Boolean roleDostepy;
        private Boolean roleRozliczenia;
        private Long version;

        public Edit id(Long id){
            this.id = id;
            return this;
        }

        public Edit login(String login){
            this.login = login;
            return this;
        }
        public Edit name(String name){
            this.name = name;
            return this;
        }
        public Edit surname(String surname){
            this.surname = surname;
            return this;
        }
        public Edit password(String password){
            this.password = password;
            return this;
        }
        public Edit roleWaga(Boolean roleWaga){
            this.roleWaga = roleWaga;
            return this;
        }
        public Edit rolePalety(Boolean rolePalety){
            this.rolePalety = rolePalety;
            return this;
        }
        public Edit rolePanel(Boolean rolePanel){
            this.rolePanel = rolePanel;
            return this;
        }
        public Edit roleLeader(Boolean roleLeader){
            this.roleLeader = roleLeader;
            return this;
        }
        public Edit roleDostepy(Boolean roleDostepy){
            this.roleDostepy = roleDostepy;
            return this;
        }
        public Edit roleRozliczenia(Boolean roleRozliczenia){
            this.roleRozliczenia = roleRozliczenia;
            return this;
        }
        public Edit version (Long version){
            this.version = version;
            return this;
        }
    }


    public static class UserBuilder {

        private UserRepository userRepository;
        private BeanValidator beanValidator;
        private Long id;
        private String login;
        private String name;
        private String surname;
        private String password;
        private Boolean removed;
        private Boolean roleWaga;
        private Boolean rolePalety;
        private Boolean rolePanel;
        private Boolean roleLeader;
        private Boolean roleDostepy;
        private Boolean roleRozliczenia;
        private Long version;

        public UserBuilder(UserRepository userRepository, BeanValidator beanValidator) {
            this.userRepository = userRepository;
            this.beanValidator = beanValidator;
        }

        public UserBuilder id(Long id){
            this.id = id;
            return this;
        }

        public UserBuilder login(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public UserBuilder passwrod(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder removed(Boolean removed) {
            this.removed = removed;
            return this;
        }

        public UserBuilder roleWaga(Boolean roleWaga) {
            this.roleWaga = roleWaga;
            return this;
        }

        public UserBuilder rolePalety(Boolean rolePalety) {
            this.rolePalety = rolePalety;
            return this;
        }

        public UserBuilder rolePanel(Boolean rolePanel) {
            this.rolePanel = rolePanel;
            return this;
        }

        public UserBuilder roleLeader(Boolean roleLeader) {
            this.roleLeader = roleLeader;
            return this;
        }

        public UserBuilder roleDostepy(Boolean roleDostepy) {
            this.roleDostepy = roleDostepy;
            return this;
        }

        public UserBuilder roleRozliczenia(Boolean roleRozliczenia) {
            this.roleRozliczenia = roleRozliczenia;
            return this;
        }

        public UserBuilder version(Long version) {
            this.version = version;
            return this;
        }

        public User build() {
            User user = new User(this.userRepository, this.beanValidator);
            user.id = this.id;
            user.login = this.login;
            user.name = this.name;
            user.surname = this.surname;
            user.password = this.password;
            user.removed = this.removed;
            user.roleWaga = this.roleWaga;
            user.rolePalety = this.rolePalety;
            user.rolePanel = this.rolePanel;
            user.roleLeader = this.roleLeader;
            user.roleDostepy = this.roleDostepy;
            user.roleRozliczenia = this.roleRozliczenia;
            user.version = this.version;
            return user;
        }
    }
}
