package com.fungisearch.fudriver.wozek.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.wozek.command.model.TrolleyMan;
import com.fungisearch.fudriver.wozek.command.model.TrolleyManFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class EditTrolleyManCommandHandler {
    private final TrolleyManFactory trolleyManFactory;

    public EditTrolleyManCommandHandler(TrolleyManFactory trolleyManFactory) {
        this.trolleyManFactory = trolleyManFactory;
    }

    public CommandResult handle(EditTrolleyManCommand command){
        trolleyManFactory.find(command.id)
                .edit(new TrolleyMan.Edit()
                        .name(command.name)
                        .surname(command.surname)
                        .active(command.active));
        return new CommandResult(CommandResult.Status.OK);
    }
}
