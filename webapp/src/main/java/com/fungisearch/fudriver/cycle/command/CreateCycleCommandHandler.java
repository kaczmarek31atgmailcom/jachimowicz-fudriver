package com.fungisearch.fudriver.cycle.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateCycleCommandHandler {
    private final CycleFactory cycleFactory;
    private final UserService userService;
    private final ChamberFactory chamberFactory;
    private final UserFactory userFactory;

    @Autowired
    public CreateCycleCommandHandler(CycleFactory cycleFactory, UserService userService, ChamberFactory chamberFactory, UserFactory userFactory) {
        this.cycleFactory = cycleFactory;
        this.userService = userService;
        this.chamberFactory = chamberFactory;
        this.userFactory = userFactory;
    }

    public CommandResult handle(CreateCycleCommand command){
        Cycle cycle = cycleFactory.findByChamberId(command.chamberId,command.startDate);
        return new CommandResult(cycle.getId(), CommandResult.Status.OK,"CycleCreated");
    }
}
