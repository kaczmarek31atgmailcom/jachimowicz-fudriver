package com.fungisearch.fudriver.warehouseEastMushrooms.query.dto;

import java.util.Date;
import java.util.List;

public class ShipmentHeaderDto {
    public long id;
    public int nr;
    public Date date;
    public String customerName;
    public List<WzHeaderDto> wzHeaders;
}
