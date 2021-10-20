package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.settings.command.model.MyceliumFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class EditCycleMyceliumCommandHandler {
    private final CycleFactory cycleFactory;
    private final MyceliumFactory myceliumFactory;

    @Autowired
    public EditCycleMyceliumCommandHandler(CycleFactory cycleFactory, MyceliumFactory myceliumFactory) {
        this.cycleFactory = cycleFactory;
        this.myceliumFactory = myceliumFactory;
    }

    public CommandResult handle(EditCycleMyceliumCommand command) {
        cycleFactory.find(command.cycleId)
                .edit(new Cycle.Edit()
                        .mycelium(myceliumFactory.find(command.myceliumId))
                        .version(command.version));
        return new CommandResult(command.cycleId, CommandResult.Status.OK, "CycleMyceliumUpdated");
    }
}
