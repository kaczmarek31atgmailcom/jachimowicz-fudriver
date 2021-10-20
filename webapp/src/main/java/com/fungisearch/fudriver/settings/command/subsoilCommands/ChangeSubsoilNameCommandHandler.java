package com.fungisearch.fudriver.settings.command.subsoilCommands;


import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Subsoil;
import com.fungisearch.fudriver.settings.command.model.SubsoilFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ChangeSubsoilNameCommandHandler {

    private final SubsoilFactory subsoilFactory;

    @Autowired
    public ChangeSubsoilNameCommandHandler(SubsoilFactory subsoilFactory) {
        this.subsoilFactory = subsoilFactory;
    }

    public CommandResult handle(ChangeSubsoilNameCommand command) {
        subsoilFactory
                .find(command.id)
                .changeName(new Subsoil.ChangeName()
                        .name(command.name));
        return new CommandResult(command.id, CommandResult.Status.OK,"SubsoilNameChanged");
    }
}
