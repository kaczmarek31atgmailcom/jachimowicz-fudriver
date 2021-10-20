package com.fungisearch.fudriver.reclassification.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UnassignRodzajCommandHandler {
    private final TypeFactory typeFactory;

    public UnassignRodzajCommandHandler(TypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public CommandResult handle(UnassignRodzajCommand command){
        typeFactory.findById(command.localTypeId).unassignRemoteType();
        return new CommandResult(CommandResult.Status.OK);
    }
}
