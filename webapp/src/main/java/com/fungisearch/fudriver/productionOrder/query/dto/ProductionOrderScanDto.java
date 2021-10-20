package com.fungisearch.fudriver.productionOrder.query.dto;

import java.util.Date;

public class ProductionOrderScanDto {
    public int warehouseTypeId;
    public String localTypeName;
    public int localTypeWeight;
    public int preparedAmount;
    public int dueAmount;
    public Date dueDate;
}
