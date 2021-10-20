package com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehouseUnitFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MoveBoxesBetweenHarvestPalettesCommandHandler {
    public final WarehouseUnitFactory warehouseUnitFactory;

    public MoveBoxesBetweenHarvestPalettesCommandHandler(WarehouseUnitFactory warehouseUnitFactory) {
        this.warehouseUnitFactory = warehouseUnitFactory;
    }

    public CommandResult handle(MoveBoxesBetweenWarehousePalettesCommand command){
        command.boxes.forEach(o -> warehouseUnitFactory.find(o).moveToAnotherWarehousePalette(command.targetPaletteId));
        return new CommandResult(CommandResult.Status.OK,"BoxesMovedToAnotherWarehousePalette");
    }
}
