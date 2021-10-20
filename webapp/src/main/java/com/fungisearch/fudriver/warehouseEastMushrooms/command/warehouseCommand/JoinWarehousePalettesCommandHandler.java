package com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JoinWarehousePalettesCommandHandler {
    public final WarehousePaletteFactory warehousePaletteFactory;

    public JoinWarehousePalettesCommandHandler(WarehousePaletteFactory warehousePaletteFactory) {
        this.warehousePaletteFactory = warehousePaletteFactory;
    }

    public CommandResult handle(JoinWarehousePalettesCommand command) {
        warehousePaletteFactory .findWarehousePalette(command.sourcePaletteId).moveUnitsToOtherPalette(command.targetPaletteId);
        return new CommandResult(CommandResult.Status.OK,"WarehousePalettesJoined");
    }
}
