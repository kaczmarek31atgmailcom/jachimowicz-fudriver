package com.fungisearch.fudriver.settings.command.chamberCommands;


import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DeleteChamberCommandHandler {

    private final ChamberFactory chamberFactory;

    @Autowired
    public DeleteChamberCommandHandler(ChamberFactory chamberFactory) {
        this.chamberFactory = chamberFactory;
    }

    public CommandResult handle(long id){
        chamberFactory.find(id).inactivate();
        return new CommandResult(id, CommandResult.Status.OK,"ChamberDeleted");
    }
}
