package com.fungisearch.fudriver.type.command;

import com.fungisearch.fudriver.box.command.model.BoxFactory;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.type.command.model.ExportType;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateTypeCommandHandler {
    private final TypeFactory typeFactory;
    private final BoxFactory boxFactory;

    @Autowired
    public CreateTypeCommandHandler(TypeFactory typeFactory, BoxFactory boxFactory) {
        this.typeFactory = typeFactory;
        this.boxFactory = boxFactory;
    }

    public CommandResult handle(CreateTypeCommand command){
        Type type = typeFactory
                .getBuilder()
                .name(command.name)
                .weight(command.weight)
                .exportType(ExportType.valueOf(command.exportType))
                .typeGroup(typeFactory.findTypeGroup(command.groupId))
                .box(boxFactory.find(command.boxId))
                .typeSize(typeFactory.findTypeSize(command.sizeId))
                .build();
        return new CommandResult(type.getId(), CommandResult.Status.OK,"TypeCreated");
    }
}
