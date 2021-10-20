package com.fungisearch.fudriver.box.command;

import com.fungisearch.fudriver.box.command.model.BoxFactory;
import com.fungisearch.fudriver.common.command.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateBoxNameCommandHandler {
    private final BoxFactory boxFactory;

    @Autowired
    public UpdateBoxNameCommandHandler(BoxFactory boxFactory) {
        this.boxFactory = boxFactory;
    }

    public CommandResult handle(ChangeBoxNameCommand command){
        boxFactory
                .find(command.id)
                .setName(command.name);
        return new CommandResult(command.id, CommandResult.Status.OK,"BoxNameUpdated");
    }
}
