package com.fungisearch.fudriver.wozek.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.wozek.command.model.TrolleyManFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CreateTrolleyManCommandHandler {

    private final TrolleyManFactory trolleyManFactory;

    public CreateTrolleyManCommandHandler(TrolleyManFactory trolleyManFactory) {
        this.trolleyManFactory = trolleyManFactory;
    }

    public CommandResult handle(CreateTrolleyManCommand command){
        trolleyManFactory.getBuilder()
                .name(command.name)
                .surname(command.surname)
                .build();
        return new CommandResult(CommandResult.Status.OK);
    }
}
