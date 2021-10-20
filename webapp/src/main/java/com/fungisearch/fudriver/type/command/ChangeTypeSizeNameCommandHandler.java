package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChangeTypeSizeNameCommandHandler {
    private final TypeFactory typeFactory;

    @Autowired
    public ChangeTypeSizeNameCommandHandler(TypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public CommandResult handle(ChangeTypeSizeNameCommand command){
        typeFactory.findTypeSize(command.id).setName(command.name);
        return new CommandResult(command.id, CommandResult.Status.OK, "TypeSizeNameUpdated");
    }
}
