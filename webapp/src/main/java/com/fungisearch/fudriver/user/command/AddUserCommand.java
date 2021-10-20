package com.fungisearch.fudriver.user.command;

import com.fungisearch.fudriver.common.command.Command;

/**
 * Created by marcin on 25.03.16.
 */
public class AddUserCommand implements Command {
    public String login;
    public String name;
    public String surname;
    public String password;
    public Boolean panel;
    public Boolean admin;
    public Boolean place;
    public Boolean waga;
    public Boolean leader;
    public Boolean palety;
    public Boolean hotel;
}
