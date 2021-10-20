package com.fungisearch.fudriver.productionOrder.query.dto;

import java.util.Date;

public class ProductionOrderDto {
    public int warehouseOrderId;
    public Date creationDate;
    public Date dueDate;
    public int warehouseTypeId;
    public String warehouseTypeName;
    public int warehouseTypeWeight;
    public int localTypeId;
    public String localTypeName;
    public int localTypeWeight;
    public int dueAmount;
    public int deliveredAmount;
    public String description;
}
