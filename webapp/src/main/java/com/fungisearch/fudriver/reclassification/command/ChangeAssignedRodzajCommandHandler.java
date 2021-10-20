package com.fungisearch.fudriver.reclassification.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupFactory;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 04.02.16.
 */
@Service
@Transactional
public class ChangeAssignedRodzajCommandHandler  {

    private final TypeFactory typeFactory;
    public ChangeAssignedRodzajCommandHandler(TypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    public CommandResult handle(ChangeAssignedRodzajCommand command) {
        typeFactory.findById(command.localTypeId).assignRemoteType(command.remoteTypeId);
        return CommandResult.OK;
    }
}
