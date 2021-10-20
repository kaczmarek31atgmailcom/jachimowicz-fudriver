package com.fungisearch.fudriver.settings.command.myceliumCommands;


import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Mycelium;
import com.fungisearch.fudriver.settings.command.model.MyceliumFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CreateMyceliumCommandHandler {

    private final MyceliumFactory myceliumFactory;

    @Autowired
    public CreateMyceliumCommandHandler(MyceliumFactory myceliumFactory) {
        this.myceliumFactory = myceliumFactory;
    }

    public CommandResult handle(CreateMyceliumCommand command) {
        Mycelium mycelium = myceliumFactory
                .getBuilder()
                .name(command.name)
                .build();
        return new CommandResult(mycelium.getId(), CommandResult.Status.OK,"MyceliumCreated");
    }
}
