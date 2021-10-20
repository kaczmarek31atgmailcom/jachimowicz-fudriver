package com.fungisearch.fudriver.settings.command.chamberCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Service
public class UpdateChamberAreaCommandHandler {

    private final ChamberFactory chamberFactory;

    @Autowired
    public UpdateChamberAreaCommandHandler(ChamberFactory chamberFactory) {
        this.chamberFactory = chamberFactory;
    }

    public CommandResult handle(UpdateChamberAreaCommand command) {
        chamberFactory.
                find(command.id)
                .edit(new Chamber.Edit()
                        .area(command.area));
        return new CommandResult(command.id, CommandResult.Status.OK,"ChamberAreaUpdated");
    }
}
