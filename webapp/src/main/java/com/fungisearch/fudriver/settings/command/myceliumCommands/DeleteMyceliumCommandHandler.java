package com.fungisearch.fudriver.settings.command.myceliumCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.MyceliumFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DeleteMyceliumCommandHandler {

    private final MyceliumFactory myceliumFactory;

    @Autowired
    public DeleteMyceliumCommandHandler(MyceliumFactory myceliumFactory) {
        this.myceliumFactory = myceliumFactory;
    }

    public CommandResult handle(long id){
        myceliumFactory
                .find(id)
                .remove();
        return new CommandResult(id, CommandResult.Status.OK,"MyceliumDeleted");
    }
}
