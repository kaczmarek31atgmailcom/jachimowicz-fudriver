package com.fungisearch.fudriver.settings.command.chamberCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by marcin on 10.04.17.
 */
@Transactional
@Service
public class UpdateChamberNameCommandHandler {

    private final ChamberFactory chamberFactory;

    @Autowired
    public UpdateChamberNameCommandHandler(ChamberFactory chamberFactory) {
        this.chamberFactory = chamberFactory;
    }

    public CommandResult handle(UpdateChamberNameCommand command) {
        chamberFactory.
                find(command.id).
                edit(new Chamber.Edit().
                        name(command.name));
        return new CommandResult(command.id, CommandResult.Status.OK,"ChamberNameUpdated");
    }
}
