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
public class RemoveLocalReclassifyReasonCommandHandler {

    @Autowired
    LocalReclassifyReasonFactory factory;

    public CommandResult handle(RemoveLocalReclassifyReasonCommand command) {
        LocalReclassifyReason reason = factory.findReason(command.id);
        reason.remove();
        return new CommandResult(command.id, CommandResult.Status.OK,"Reason removed");
    }
}
