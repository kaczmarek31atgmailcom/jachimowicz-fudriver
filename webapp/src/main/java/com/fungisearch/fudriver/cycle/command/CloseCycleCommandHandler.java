package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CloseCycleCommandHandler {
    private final CycleFactory cycleFactory;

    @Autowired
    public CloseCycleCommandHandler(CycleFactory cycleFactory) {
        this.cycleFactory = cycleFactory;
    }

    public CommandResult handle(CloseCycleCommand command) {
       Cycle cycle =  cycleFactory.find(command.cycleId);
                cycle.close(new Cycle.Close()
                        .endDate(command.closeDate)
                        .version(command.version));
        return new CommandResult(command.cycleId, CommandResult.Status.OK, "CycleClosed");
    }
}
