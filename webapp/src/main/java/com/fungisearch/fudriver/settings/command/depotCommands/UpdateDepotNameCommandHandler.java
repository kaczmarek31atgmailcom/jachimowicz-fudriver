package com.fungisearch.fudriver.settings.command.depotCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Depot;
import com.fungisearch.fudriver.settings.command.model.DepotFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class UpdateDepotNameCommandHandler {

    private DepotFactory depotFactory;

    @Autowired
    public UpdateDepotNameCommandHandler(DepotFactory depotFactory) {
        this.depotFactory = depotFactory;
    }

    public CommandResult handle(UpdateDepotNameCommand command){
        depotFactory.find(command.id).edit(new Depot.Edit().name(command.name));
        return new CommandResult(command.id, CommandResult.Status.OK,"DepotNameUpdated");
    }
}
