package com.fungisearch.fudriver.box.command;

import com.fungisearch.fudriver.box.command.model.Box;
import com.fungisearch.fudriver.box.command.model.BoxFactory;
import com.fungisearch.fudriver.common.command.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateBoxCommandHandler {
    private final BoxFactory boxFactory;

    @Autowired
    public CreateBoxCommandHandler(BoxFactory boxFactory) {
        this.boxFactory = boxFactory;
    }

    public CommandResult handle(CreateBoxCommand command) {
        Box box = boxFactory
                .getBuilder()
                .name(command.name)
                .build();
        return new CommandResult(box.getId(), CommandResult.Status.OK,"BoxCreated");
    }
}
