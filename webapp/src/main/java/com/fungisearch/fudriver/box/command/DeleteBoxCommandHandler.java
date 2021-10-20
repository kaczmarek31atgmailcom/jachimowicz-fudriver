package com.fungisearch.fudriver.box.command;

import com.fungisearch.fudriver.box.command.model.BoxFactory;
import com.fungisearch.fudriver.common.command.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteBoxCommandHandler {
    private final BoxFactory boxFactory;

    @Autowired
    public DeleteBoxCommandHandler(BoxFactory boxFactory) {
        this.boxFactory = boxFactory;
    }

    public CommandResult handle(long id){
        boxFactory
                .find(id)
                .delete();
        return new CommandResult(id, CommandResult.Status.OK,"BoxDeleted");
    }
}
