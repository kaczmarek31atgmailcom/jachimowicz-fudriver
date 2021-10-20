package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteTypeSizeCommandHandler {
    private final TypeFactory typeFactory;

    @Autowired
    public DeleteTypeSizeCommandHandler(TypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public CommandResult handle(long id){
        typeFactory.findTypeSize(id).remove();
        return new CommandResult(id, CommandResult.Status.OK,"TypeSizeRemoved");
    }
}
