package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.settings.command.model.SubsoilFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EditCycleSubsoilCommandHandler {
    private final CycleFactory cycleFactory;
    private final SubsoilFactory subsoilFactory;

    @Autowired
    public EditCycleSubsoilCommandHandler(CycleFactory cycleFactory, SubsoilFactory subsoilFactory) {
        this.cycleFactory = cycleFactory;
        this.subsoilFactory = subsoilFactory;
    }

    public CommandResult handle(EditCycleSubsoilCommand command) {
        cycleFactory.find(command.cycleId)
                .edit(new Cycle.Edit()
                        .subsoil(subsoilFactory.find(command.subsoilId))
                        .version(command.version));
        return new CommandResult(command.cycleId, CommandResult.Status.OK, "CycleSubsoilUpdated");
    }
}
