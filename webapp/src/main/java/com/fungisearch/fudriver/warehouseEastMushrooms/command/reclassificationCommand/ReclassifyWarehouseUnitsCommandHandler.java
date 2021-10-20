package com.fungisearch.fudriver.warehouseEastMushrooms.command.reclassificationCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.reclassification.ReclassificationFactory;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.reclassification.ReclassificationHeader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReclassifyWarehouseUnitsCommandHandler {
    private final ReclassificationFactory reclassificationFactory;

    public ReclassifyWarehouseUnitsCommandHandler(ReclassificationFactory reclassificationFactory) {
        this.reclassificationFactory = reclassificationFactory;
    }

    public CommandResult handle(ReclassifyWarehouseUnitsCommand command){
        ReclassificationHeader header = reclassificationFactory.getHeaderBuilder()
                .idsToBeReclassified(command.sourceTypeIds)
                .targetTypeId(command.targetTypeId)
                .build();
        return new CommandResult(header.getId(), CommandResult.Status.OK,"ReclassificationCreated");
    }
}
