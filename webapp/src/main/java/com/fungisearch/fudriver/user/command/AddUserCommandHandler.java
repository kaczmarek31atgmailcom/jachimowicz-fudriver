package com.fungisearch.fudriver.user.command;

import com.fungisearch.fudriver.common.command.CommandHandler;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.command.model.UserRole;
import com.fungisearch.fudriver.user.command.model.UserRoleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by marcin on 25.03.16.
 */
@Service
public class AddUserCommandHandler implements CommandHandler<AddUserCommand> {

    @Autowired
    UserFactory userFactory;

    @Autowired
    UserRoleFactory userRoleFactory;

    @Override
    public CommandResult handle(AddUserCommand command) {
        User user = userFactory.builder().
                login(command.login).
                name(command.name).
                surname(command.surname).
                passwrod(command.password).
                roleDostepy(command.admin).
                roleLeader(command.leader).
                rolePalety(command.palety).
                rolePanel(isPanelActive(command)).
                roleRozliczenia(command.place).
                roleWaga(command.waga).
                build();
        Long id = user.create();

        if(command.admin){
            userRoleFactory.userRoleBuilder().roleName("ROLE_DOSTEPY").userId(id).username(command.login).build().create();
        }

        if(command.leader){
            userRoleFactory.userRoleBuilder().roleName("ROLE_LEADER").userId(id).username(command.login).build().create();
        }

        if(command.palety){
            userRoleFactory.userRoleBuilder().roleName("ROLE_PALETY").userId(id).username(command.login).build().create();
        }

        if(command.panel){
            userRoleFactory.userRoleBuilder().roleName("ROLE_PANEL").userId(id).username(command.login).build().create();
        }

        if(command.place){
            userRoleFactory.userRoleBuilder().roleName("ROLE_ROZLICZENIA").userId(id).username(command.login).build().create();
        }

        if(command.waga){
            userRoleFactory.userRoleBuilder().roleName("ROLE_WAGA").userId(id).username(command.login).build().create();
        }

        if(command.hotel){
            userRoleFactory.userRoleBuilder().roleName("ROLE_HOTEL").userId(id).username(command.login).build().create();
        }

        if(command.handlowiec){
            userRoleFactory.userRoleBuilder().roleName("ROLE_HANDLOWIEC").userId(id).username(command.login).build().create();
        }


        return new CommandResult(id, CommandResult.Status.OK,"User created");
    }

    private boolean isPanelActive(AddUserCommand command){
        return command.admin || command.leader || command.place;
    }
}
