package com.fungisearch.fudriver.settings.command.depotCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.DepotFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class DeleteDepotCommandHandler {

    private DepotFactory depotFactory;

    @Autowired
    public DeleteDepotCommandHandler(DepotFactory depotFactory) {
        this.depotFactory = depotFactory;
    }

    public CommandResult handle(long depotId){
        depotFactory.find(depotId).deactivate();
        return new CommandResult(depotId, CommandResult.Status.OK,"DepotRemoved");
    }
}
