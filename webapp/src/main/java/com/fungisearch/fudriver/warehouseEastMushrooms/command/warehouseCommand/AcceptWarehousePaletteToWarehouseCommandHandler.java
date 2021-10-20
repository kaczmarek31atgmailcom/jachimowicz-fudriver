package com.fungisearch.fudriver.warehouseEastMushrooms.command.warehouseCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.common.event.CustomApplicationEvent;
import com.fungisearch.fudriver.common.event.EventTypeEnum;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePalette;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AcceptWarehousePaletteToWarehouseCommandHandler {
    private final WarehousePaletteFactory warehousePaletteFactory;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AcceptWarehousePaletteToWarehouseCommandHandler(WarehousePaletteFactory warehousePaletteFactory, ApplicationEventPublisher applicationEventPublisher) {
        this.warehousePaletteFactory = warehousePaletteFactory;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public CommandResult handle(AcceptWarehousePaletteToWarehouseCommand command){
        warehousePaletteFactory.findWarehousePalette(command.warehousePaletteId).acceptToWarehouse();
//        CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EAST_WAREHOUSE_PALETTE_ACCEPTED_TO_WAREHOUSE, command.warehousePaletteId);
//        applicationEventPublisher.publishEvent(event);
        return new CommandResult(command.warehousePaletteId,CommandResult.Status.OK,"WarehousePaletteAcceptedToWarehouse");
    }
}
