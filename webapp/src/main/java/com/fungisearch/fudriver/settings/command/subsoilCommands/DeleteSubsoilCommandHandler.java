package com.fungisearch.fudriver.settings.command.subsoilCommands;


import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.SubsoilFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class DeleteSubsoilCommandHandler {

    private final SubsoilFactory subsoilFactory;

    @Autowired
    public DeleteSubsoilCommandHandler(SubsoilFactory subsoilFactory) {
        this.subsoilFactory = subsoilFactory;
    }

    public CommandResult handle(long id){
        subsoilFactory.find(id).inactivate();
        return new CommandResult(id, CommandResult.Status.OK,"SubsoilDeleted");
    }
}
