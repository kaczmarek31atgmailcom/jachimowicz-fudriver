package com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePalette;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateAcceptedWarehousePaletteCommandHandler {
    private final WarehousePaletteFactory warehousePaletteFactory;

    public CreateAcceptedWarehousePaletteCommandHandler(WarehousePaletteFactory warehousePaletteFactory) {
        this.warehousePaletteFactory = warehousePaletteFactory;
    }

    public CommandResult handle(CreateAcceptedWarehousePaletteCommand command){
        WarehousePalette warehousePalette = warehousePaletteFactory.getBuilder().depotId(command.depotId).paletteTypeId(command.paletteTypeId).build();
        warehousePalette.acceptToWarehouse();
        return new CommandResult(warehousePalette.getId(), CommandResult.Status.OK,"WarehousePaletteCreated");
    }
}
