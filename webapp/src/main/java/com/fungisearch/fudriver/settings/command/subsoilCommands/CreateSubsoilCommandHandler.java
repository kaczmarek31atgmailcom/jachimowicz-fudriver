package com.fungisearch.fudriver.settings.command.subsoilCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Subsoil;
import com.fungisearch.fudriver.settings.command.model.SubsoilFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateSubsoilCommandHandler {

    private final SubsoilFactory subsoilFactory;

    @Autowired
    public CreateSubsoilCommandHandler(SubsoilFactory subsoilFactory) {
        this.subsoilFactory = subsoilFactory;
    }

    public CommandResult handle(CreateSubsoilCommand command){
        Subsoil subsoil = subsoilFactory.getBuilder().name(command.name).build();
        return new CommandResult(subsoil.getId(), CommandResult.Status.OK,"SubsoilCreated");
    }
}
