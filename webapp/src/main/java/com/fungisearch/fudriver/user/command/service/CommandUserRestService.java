package com.fungisearch.fudriver.user.command.service;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.user.command.AddUserCommand;
import com.fungisearch.fudriver.user.command.DeleteUserCommand;
import com.fungisearch.fudriver.user.command.EditUserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Created by marcin on 26.03.16.
 */
@RestController
public class CommandUserRestService {

    @Autowired
    private CommandHandler<AddUserCommand> addUserHander;

    @Autowired
    private CommandHandler<EditUserCommand> editUserHandler;

    @Autowired
    private CommandHandler<DeleteUserCommand> deleteUserHandler;

    @RequestMapping(value="/rest/user", method = RequestMethod.POST, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @Transactional
    public CommandResult addUser(@RequestBody AddUserCommand command){
        CommandResult result = addUserHander.handle(command);
        return result;
    }

    @RequestMapping(value="/rest/user", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @Transactional
    public CommandResult editUser(@RequestBody EditUserCommand command){
        CommandResult result = editUserHandler.handle(command);
        return result;
    }

    @RequestMapping(value="/rest/user", method = RequestMethod.DELETE, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    @Transactional
    public CommandResult deleteUser(@RequestBody DeleteUserCommand command){
        CommandResult result = deleteUserHandler.handle(command);
        return result;
    }
}
