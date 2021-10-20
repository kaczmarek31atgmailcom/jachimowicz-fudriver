package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class EditCycleAreaCommandHandler {
    private final CycleFactory cycleFactory;

    @Autowired
    public EditCycleAreaCommandHandler(CycleFactory cycleFactory) {
        this.cycleFactory = cycleFactory;
    }

    public CommandResult handle(EditCycleAreaCommand command) {
        cycleFactory.find(command.cycleId)
                .edit(new Cycle.Edit()
                        .area(command.area)
                        .version(command.version));
        return new CommandResult(command.cycleId, CommandResult.Status.OK, "CycleAreaUpdated");
    }
}
