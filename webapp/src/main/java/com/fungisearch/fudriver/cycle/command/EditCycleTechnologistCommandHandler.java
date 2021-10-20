package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EditCycleTechnologistCommandHandler {
    private final CycleFactory cycleFactory;
    private final UserFactory userFactory;

    @Autowired
    public EditCycleTechnologistCommandHandler(CycleFactory cycleFactory, UserFactory userFactory) {
        this.cycleFactory = cycleFactory;
        this.userFactory = userFactory;
    }

    public CommandResult handle(EditCycleTechnologistCommand command) {
        cycleFactory.find(command.cycleId)
                .edit(new Cycle.Edit()
                        .technologis(userFactory.find(command.technologistId))
                        .version(command.version));
        return new CommandResult(command.cycleId, CommandResult.Status.OK, "CycleTechnologistUpdated");
    }
}
