package com.fungisearch.fudriver.user.command;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.command.model.UserRoleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 28.03.16.
 */
@Service
public class EditUserCommandHandler implements CommandHandler<EditUserCommand> {

    @Autowired
    UserFactory userFactory;

    @Autowired
    UserRoleFactory userRoleFactory;


    @Override
    public CommandResult handle(EditUserCommand command) {
        User user = userFactory.find(command.id);
        user.edit(new User.Edit().
                id(command.id).
                login(command.login).
                name(command.name).
                surname(command.surname).
                password(command.password).
                roleDostepy(command.admin).
                roleLeader(command.leader).
                rolePalety(command.palety).
                rolePanel(isPanelActive(command)).
                roleRozliczenia(command.place).
                roleWaga(command.waga));

        if(command.admin){
            userRoleFactory.userRoleBuilder().roleName("ROLE_DOSTEPY").userId(command.id).username(command.login).build().create();
        } else {
            userRoleFactory.userRoleBuilder().roleName("ROLE_DOSTEPY").userId(command.id).username(command.login).build().remove();
        }

        if(command.leader){
            userRoleFactory.userRoleBuilder().roleName("ROLE_LEADER").userId(command.id).username(command.login).build().create();
        } else {
            userRoleFactory.userRoleBuilder().roleName("ROLE_LEADER").userId(command.id).username(command.login).build().remove();
        }

        if(command.palety){
            userRoleFactory.userRoleBuilder().roleName("ROLE_PALETY").userId(command.id).username(command.login).build().create();
        } else {
            userRoleFactory.userRoleBuilder().roleName("ROLE_PALETY").userId(command.id).username(command.login).build().remove();
        }

        if(command.panel){
            userRoleFactory.userRoleBuilder().roleName("ROLE_PANEL").userId(command.id).username(command.login).build().create();
        } else {
            userRoleFactory.userRoleBuilder().roleName("ROLE_PANEL").userId(command.id).username(command.login).build().remove();
        }

        if(command.place){
            userRoleFactory.userRoleBuilder().roleName("ROLE_ROZLICZENIA").userId(command.id).username(command.login).build().create();
        } else {
            userRoleFactory.userRoleBuilder().roleName("ROLE_ROZLICZENIA").userId(command.id).username(command.login).build().remove();
        }

        if(command.waga){
            userRoleFactory.userRoleBuilder().roleName("ROLE_WAGA").userId(command.id).username(command.login).build().create();
        } else {
            userRoleFactory.userRoleBuilder().roleName("ROLE_WAGA").userId(command.id).username(command.login).build().remove();
        }

        if(command.hotel){
            userRoleFactory.userRoleBuilder().roleName("ROLE_HOTEL").userId(command.id).username(command.login).build().create();
        } else {
            userRoleFactory.userRoleBuilder().roleName("ROLE_HOTEL").userId(command.id).username(command.login).build().remove();
        }

        if(command.handlowiec){
            userRoleFactory.userRoleBuilder().roleName("ROLE_HANDLOWIEC").userId(command.id).username(command.login).build().create();
        } else {
            userRoleFactory.userRoleBuilder().roleName("ROLE_HANDLOWIEC").userId(command.id).username(command.login).build().remove();
        }


        return CommandResult.OK;
    }

    private boolean isPanelActive(EditUserCommand command){
        return command.admin || command.leader || command.place || command.panel;
    }
}
