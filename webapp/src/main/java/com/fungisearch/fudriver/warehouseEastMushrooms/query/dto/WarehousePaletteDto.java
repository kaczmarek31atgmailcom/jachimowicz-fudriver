package com.fungisearch.fudriver.warehouseEastMushrooms.query.dto;

import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteStatus;

public class WarehousePaletteDto {
    public long paletteId;
    public int paletteTypeId;
    public long localTypeId;
    public String localTypeName;
    public double localTypeWeight;
    public Long remoteTypeId;
    public String remoteTypeName;
    public Double remoteTypeWeight;
    public int amount;
    public WarehousePaletteStatus warehousePaletteStatus;
}
