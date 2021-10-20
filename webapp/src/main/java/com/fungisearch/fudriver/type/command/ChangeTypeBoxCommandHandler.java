package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.box.command.model.BoxFactory;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChangeTypeBoxCommandHandler {
    private final TypeFactory typeFactory;
    private final BoxFactory boxFactory;

    @Autowired
    public ChangeTypeBoxCommandHandler(TypeFactory typeFactory, BoxFactory boxFactory) {
        this.typeFactory = typeFactory;
        this.boxFactory = boxFactory;
    }

    public CommandResult handle(ChangeTypeBoxCommand command){
        typeFactory.findById(command.typeId)
                .setBox(boxFactory.find(command.boxId));
        return new CommandResult(command.typeId, CommandResult.Status.OK,"TypeBoxUpdated");
    }
}
