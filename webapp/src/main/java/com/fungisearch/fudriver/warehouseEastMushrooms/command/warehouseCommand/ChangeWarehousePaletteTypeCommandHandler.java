package com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChangeWarehousePaletteTypeCommandHandler {
    private final WarehousePaletteFactory warehousePaletteFactory;

    public ChangeWarehousePaletteTypeCommandHandler(WarehousePaletteFactory warehousePaletteFactory) {
        this.warehousePaletteFactory = warehousePaletteFactory;
    }

    public CommandResult handle(ChangeWarehousePaletteTypeCommand command){
        warehousePaletteFactory
                .find(command.paletteId)
                .changePaletteType(command.paletteTypeId);
        return new CommandResult(CommandResult.Status.OK, "PaletteTypeChanged");
    }
}
