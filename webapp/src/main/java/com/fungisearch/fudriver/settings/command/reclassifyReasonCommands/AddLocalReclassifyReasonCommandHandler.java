package com.fungisearch.fudriver.settings.command.reclassifyReasonCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.LocalReclassifyReason;
import com.fungisearch.fudriver.settings.command.model.LocalReclassifyReasonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by marcin on 02.08.16.
 */
@Service
@Transactional
public class AddLocalReclassifyReasonCommandHandler {

    @Autowired
    public LocalReclassifyReasonFactory factory;

    public CommandResult handle(AddLocalReclassifyReasonCommand command) {
        LocalReclassifyReason localReclassifyReason = factory.getBuilder()
                .description(command.description)
                .build();
        Long entityId = localReclassifyReason.create();
        return new CommandResult(entityId, CommandResult.Status.OK,"New ReclassifyReason created");
    }
}
