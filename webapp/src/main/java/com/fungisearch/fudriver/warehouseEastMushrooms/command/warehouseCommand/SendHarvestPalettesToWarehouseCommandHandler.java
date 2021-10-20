package com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SendHarvestPalettesToWarehouseCommandHandler {

    private final WarehousePaletteFactory warehousePaletteFactory;

    public SendHarvestPalettesToWarehouseCommandHandler(WarehousePaletteFactory warehousePaletteFactory) {
        this.warehousePaletteFactory = warehousePaletteFactory;
    }

    public CommandResult handle(SendHaverstPalettesToWarehouseCommand command){
        warehousePaletteFactory
                .getBuilder()
                .depotId(command.depotId)
                .paletteTypeId(command.paletteTypeId)
                .build().assignHarvestPalettesAndAcceptToWarehouse(command.harvestPalettes);
        return new CommandResult(CommandResult.Status.OK,"WarehousePaletteCreated");
    }
}
