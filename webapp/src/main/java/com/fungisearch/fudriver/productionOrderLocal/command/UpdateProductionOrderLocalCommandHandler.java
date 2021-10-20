package com.fungisearch.fudriver.productionOrderLocal.command;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.productionOrderLocal.command.model.ProductionOrderLocalFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateProductionOrderLocalCommandHandler {
    private final ProductionOrderLocalFactory factory;

    public UpdateProductionOrderLocalCommandHandler(ProductionOrderLocalFactory factory) {
        this.factory = factory;
    }

    public CommandResult handle(UpdateProductionOrderLocalCommand command){
        factory.getBuilder()
                .dueDate(command.day)
                .typeId(command.typeId)
                .amount(command.amount)
                .build();
        return new CommandResult(CommandResult.Status.OK);
    }
}
