package com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand.MoveHarvestPaletteBetweenWarehousePalettesCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class MoveHarvestPaletteBetweenWarehousePalettesCommandHandler {
    private final WarehousePaletteFactory warehousePaletteFactory;

    public MoveHarvestPaletteBetweenWarehousePalettesCommandHandler(WarehousePaletteFactory warehousePaletteFactory) {
        this.warehousePaletteFactory = warehousePaletteFactory;
    }

    public CommandResult handle(MoveHarvestPaletteBetweenWarehousePalettesCommand command){
        warehousePaletteFactory.findWarehousePaletteByHarvestPaletteId(command.harvestPaletteId).unassignHarvestPalette(command.harvestPaletteId,false);
        warehousePaletteFactory.find(command.warehousePaletteId).assignHarvestPalette(command.harvestPaletteId,true);
        return new CommandResult(CommandResult.Status.OK,"HarvestPaletteMovedBetweenWarehousePalettes");
    }
}
