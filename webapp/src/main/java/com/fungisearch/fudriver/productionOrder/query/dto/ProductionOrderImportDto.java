package com.fungisearch.fudriver.productionOrder.query.dto;

import java.util.Date;

public class ProductionOrderImportDto {
    public int warehouseOrderId;
    public Date creationDate;
    public Date dueDate;
    public int warehouseTypeId;
    public int dueAmount;
    public int deliveredAmount;
    public int status;
    public String description;
    public int version;
}
