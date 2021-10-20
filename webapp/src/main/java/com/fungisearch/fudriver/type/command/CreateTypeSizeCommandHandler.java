package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.type.command.model.TypeSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateTypeSizeCommandHandler {
    private final TypeFactory typeFactory;

    @Autowired
    public CreateTypeSizeCommandHandler(TypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public CommandResult handle(CreateTypeSizeCommand command){
        TypeSize typeSize = typeFactory.getTypeSizeBuilder().name(command.name).build();
        return new CommandResult(typeSize.getId(), CommandResult.Status.OK,"TypeSizeCreated");
    }
}
