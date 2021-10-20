package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.type.command.model.TypeGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateTypeGroupCommandHandler {

    private final TypeFactory typeFactory;

    @Autowired
    public CreateTypeGroupCommandHandler(TypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public CommandResult handle(CreateTypeGroupCommand command) {
        TypeGroup typeGroup = typeFactory.getTypeGroupBuilder()
                .name(command.name)
                .build();
        return new CommandResult(typeGroup.getId(), CommandResult.Status.OK,"TypeGroupCreated");
    }
}
