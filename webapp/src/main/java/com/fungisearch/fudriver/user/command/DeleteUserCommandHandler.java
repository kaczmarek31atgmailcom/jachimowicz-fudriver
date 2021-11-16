package com.fungisearch.fudriver.user.command;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.command.model.UserRoleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 30.03.16.
 */
@Service
public class DeleteUserCommandHandler implements CommandHandler<DeleteUserCommand> {

    @Autowired
    UserFactory userFactory;

    @Autowired
    UserRoleFactory userRoleFactory;

    @Override
    public CommandResult handle(DeleteUserCommand command) {
        User user = userFactory.find(command.userId);
        user.remove();
        userRoleFactory.userRoleBuilder().roleName("ROLE_DOSTEPY").userId(command.userId).build().remove();
        userRoleFactory.userRoleBuilder().roleName("ROLE_LEADER").userId(command.userId).build().remove();
        userRoleFactory.userRoleBuilder().roleName("ROLE_PALETY").userId(command.userId).build().remove();
        userRoleFactory.userRoleBuilder().roleName("ROLE_PANEL").userId(command.userId).build().remove();
        userRoleFactory.userRoleBuilder().roleName("ROLE_ROZLICZENIA").userId(command.userId).build().remove();
        userRoleFactory.userRoleBuilder().roleName("ROLE_WAGA").userId(command.userId).build().remove();
        userRoleFactory.userRoleBuilder().roleName("ROLE_HOTEL").userId(command.userId).build().remove();
        userRoleFactory.userRoleBuilder().roleName("ROLE_HANDLOWIEC").userId(command.userId).build().remove();
        return CommandResult.OK;
    }
}
