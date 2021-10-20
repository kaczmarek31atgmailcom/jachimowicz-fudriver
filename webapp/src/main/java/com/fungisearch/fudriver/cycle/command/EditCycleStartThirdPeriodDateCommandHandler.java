package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EditCycleStartThirdPeriodDateCommandHandler {
    private final CycleFactory cycleFactory;

    @Autowired
    public EditCycleStartThirdPeriodDateCommandHandler(CycleFactory cycleFactory) {
        this.cycleFactory = cycleFactory;
    }

    public CommandResult handle(EditCycleStartThirdPeriodDateCommand command){
        cycleFactory.find(command.cycleId)
                .edit(new Cycle.Edit()
                .startThirdPeriod(command.startThirdPeriod)
                .version(command.version));
        return new CommandResult(command.cycleId, CommandResult.Status.OK,"CycleStartThirdPeriodDateUpdated");
    }
}
