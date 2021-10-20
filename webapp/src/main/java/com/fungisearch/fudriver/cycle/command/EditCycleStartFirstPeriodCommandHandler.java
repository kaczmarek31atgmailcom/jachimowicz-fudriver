package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EditCycleStartFirstPeriodCommandHandler {
    private final CycleFactory cycleFactory;

    public EditCycleStartFirstPeriodCommandHandler(CycleFactory cycleFactory) {
        this.cycleFactory = cycleFactory;
    }

    public CommandResult handle(EditCycleStartFirstPeriodCommand command) {
        cycleFactory.find(command.cycleId)
                .edit(new Cycle.Edit()
                        .startFirstPeriod(command.startFirstPeriod)
                        .version(command.version));
        return new CommandResult(command.cycleId, CommandResult.Status.OK, "StartFirstPeriodDateUpdated");
    }
}
