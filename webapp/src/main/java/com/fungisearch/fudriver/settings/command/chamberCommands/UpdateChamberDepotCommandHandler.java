package com.fungisearch.fudriver.settings.command.chamberCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import com.fungisearch.fudriver.settings.command.model.DepotFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class UpdateChamberDepotCommandHandler {

    private final DepotFactory depotFactory;
    private final ChamberFactory chamberFactory;

    @Autowired
    public UpdateChamberDepotCommandHandler(DepotFactory depotFactory, ChamberFactory chamberFactory) {
        this.depotFactory = depotFactory;
        this.chamberFactory = chamberFactory;
    }

    public CommandResult handle(UpdateChamberDepotCommand command) {
        chamberFactory
                .find(command.id)
                .edit(new Chamber.Edit()
                        .depot(depotFactory.find(command.depotId)));
    return new CommandResult(command.id, CommandResult.Status.OK,"ChamberDepotUpdated");
    }
}
