package com.fungisearch.fudriver.settings.command.myceliumCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Mycelium;
import com.fungisearch.fudriver.settings.command.model.MyceliumFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChangeMyceliumNameCommandHandler {

    private final MyceliumFactory myceliumFactory;

    @Autowired
    public ChangeMyceliumNameCommandHandler(MyceliumFactory myceliumFactory) {
        this.myceliumFactory = myceliumFactory;
    }

    public CommandResult handle(ChangeMyceliumNameCommand command) {
        myceliumFactory
                .find(command.id)
                .edit(new Mycelium.Edit()
                        .name(command.name));
        return new CommandResult(command.id, CommandResult.Status.OK,"MyceliumNameChanged");
    }
}
