package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChangeTypeTypeSizeCommandHandler {
    private final TypeFactory typeFactory;

    @Autowired
    public ChangeTypeTypeSizeCommandHandler(TypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public CommandResult handle(ChangeTypeTypeSizeCommand command){
        typeFactory.findById(command.typeId)
                .setTypeSize(typeFactory.findTypeSize(command.sizeId));
        return new CommandResult(command.typeId, CommandResult.Status.OK,"TypeSizeChanged");
    }
}
