package com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand.UnAssignHarvestPaletteCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UnAssignHarvestPaletteCommandHandler {
    private final WarehousePaletteFactory warehousePaletteFactory;

    public UnAssignHarvestPaletteCommandHandler(WarehousePaletteFactory warehousePaletteFactory) {
        this.warehousePaletteFactory = warehousePaletteFactory;
    }

    public CommandResult handle(UnAssignHarvestPaletteCommand command){
        warehousePaletteFactory.findWarehousePaletteByHarvestPaletteId(command.harvestPaletteId).unassignHarvestPalette(command.harvestPaletteId,true);
        return new CommandResult(CommandResult.Status.OK,"HarvestPaletteUnassigned");
    }
}
