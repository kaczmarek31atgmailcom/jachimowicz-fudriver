package com.fungisearch.fudriver.warehouseEastMushrooms.query.dto;

import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehouseUnitStatus;

import java.util.Date;

public class WarehouseUnitDto {
    public long id;
    public long uniqId;
    public Long warehousePaletteId;
    public Long harvestPaletteId;
    public long pickerId;
    public String pickerName;
    public String pickerSurname;
    public long cycleId;
    public String chamberName;
    public long typeId;
    public String typeName;
    public double typeWeight;
    public Date harvestTime;
    public WarehouseUnitStatus warehouseUnitStatus;
}
