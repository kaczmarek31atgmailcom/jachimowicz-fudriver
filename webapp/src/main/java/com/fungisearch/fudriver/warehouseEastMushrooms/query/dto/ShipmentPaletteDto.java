package com.fungisearch.fudriver.warehouseEastMushrooms.query.dto;

import java.util.List;

public class ShipmentPaletteDto {
    public long shipmentId;
    public long paletteId;
    public long warehousePaletteId;
    public int paletteTypeId;
    public String paletteTypeName;
    public List<ShipmentTypeDto> types;
}
