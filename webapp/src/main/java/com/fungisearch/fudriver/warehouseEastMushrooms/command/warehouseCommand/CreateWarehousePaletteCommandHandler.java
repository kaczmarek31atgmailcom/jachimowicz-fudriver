package com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateWarehousePaletteCommandHandler {
    private final WarehousePaletteFactory warehousePaletteFactory;

    public CreateWarehousePaletteCommandHandler(WarehousePaletteFactory warehousePaletteFactory) {
        this.warehousePaletteFactory = warehousePaletteFactory;
    }

    public CommandResult handle(CreateWarehousePaletteCommand command){
        warehousePaletteFactory.getBuilder().depotId(command.depotId).paletteTypeId(command.paletteTypeId).build();
        return new CommandResult(CommandResult.Status.OK,"WarehousePaletteCreated");
    }
}
