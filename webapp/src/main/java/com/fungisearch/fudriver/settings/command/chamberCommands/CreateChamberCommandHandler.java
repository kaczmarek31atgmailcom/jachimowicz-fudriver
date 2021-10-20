package com.fungisearch.fudriver.settings.command.chamberCommands;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import com.fungisearch.fudriver.settings.command.model.CompanyFactory;
import com.fungisearch.fudriver.settings.command.model.DepotFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateChamberCommandHandler {

    private final ChamberFactory chamberFactory;
    private final DepotFactory depotFactory;
    private final CompanyFactory companyFactory;

    @Autowired
    public CreateChamberCommandHandler(ChamberFactory chamberFactory, DepotFactory depotFactory,CompanyFactory companyFactory) {
        this.chamberFactory = chamberFactory;
        this.depotFactory = depotFactory;
        this.companyFactory = companyFactory;
    }

    public CommandResult handle(CreateChamberCommand command) {
        Chamber chamber = chamberFactory
                .getBuilder()
                .name(command.name)
                .area(command.area)
                .depot(depotFactory.find(command.depotId))
                .company(companyFactory.getCompany(command.companyId))
                .build();
        return new CommandResult(chamber.getId(), CommandResult.Status.OK, "ChamberCreated");
    }
}
