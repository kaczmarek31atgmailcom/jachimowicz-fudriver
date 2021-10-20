package com.fungisearch.fudriver.warehouseEastMushrooms.command.wzCommand;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz.Shipment;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz.ShipmentFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateShipmentCommandHandler {
    private final ShipmentFactory shipmentFactory;

    public CreateShipmentCommandHandler(ShipmentFactory shipmentFactory) {
        this.shipmentFactory = shipmentFactory;
    }

    public CommandResult handle(CreateShipmentCommand command){
        Shipment shipment = shipmentFactory.getBuilder()
        .warehousePalettesIds(command.warehousePalettes)
        .customerId(command.customerId)
        .build();
        return new CommandResult(shipment.getId(), CommandResult.Status.OK,"ShipmentCreated");
    }
}
