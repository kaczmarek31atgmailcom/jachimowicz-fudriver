package com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand.AssignHarvestPaletteToWarehousePaletteCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AssignHarvestPaletteToWarehousePaletteCommandHandler {
    private final WarehousePaletteFactory warehousePaletteFactory;

    public AssignHarvestPaletteToWarehousePaletteCommandHandler(WarehousePaletteFactory warehousePaletteFactory) {
        this.warehousePaletteFactory = warehousePaletteFactory;
    }

    public CommandResult handle(AssignHarvestPaletteToWarehousePaletteCommand command){
        warehousePaletteFactory.find(command.warehousePaletteId).assignHarvestPalette(command.harvestPaletteId,true);
        return new CommandResult(command.harvestPaletteId, CommandResult.Status.OK, "Trolley Commited");
    }
}
