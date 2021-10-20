package com.fungisearch.fudriver.warehouseEastMushrooms.query.dto;

import java.util.Date;
import java.util.List;

public class ProxyShipmentDto {
    public long shipmentId;
    public int supplierId;
    public int shipmentMonthlyNo;
    public Date shipmentCreationDate;
    public int shipmentCreatorId;
    public String shipmentCreatorLogin;
    public String shipmentCreatorName;
    public String shipmentCreatorSurname;
    public String shipmentCustomerName;
    public String shipmentCustomerAddress;
    public int shipmentCustomerGroupId;
    public List<ProxyWzDto> wzDtoList;
}
