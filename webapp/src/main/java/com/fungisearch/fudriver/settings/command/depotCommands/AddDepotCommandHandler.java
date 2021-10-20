package com.fungisearch.fudriver.settings.command.depotCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Depot;
import com.fungisearch.fudriver.settings.command.model.DepotFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class AddDepotCommandHandler {

    private final DepotFactory depotFactory;

    @Autowired
    public AddDepotCommandHandler(DepotFactory depotFactory) {
        this.depotFactory = depotFactory;
    }

    public CommandResult handle(AddDepotCommand command){
        Depot depot = depotFactory.getBuilder().name(command.name).build();
        return new CommandResult(depot.getId(), CommandResult.Status.OK,"DepotCreated");
    }
}
