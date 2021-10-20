package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChangeTypeTypeGroupCommandHandler {
    public final TypeFactory typeFactory;

    @Autowired
    public ChangeTypeTypeGroupCommandHandler(TypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public CommandResult handle(ChangeTypeTypeGroupCommand command){
        typeFactory
                .findById(command.typeId)
                .setTypeGroup(typeFactory.findTypeGroup(command.groupId));
        return new CommandResult(command.typeId, CommandResult.Status.OK,"TypeGroupUpdated");
    }
}
