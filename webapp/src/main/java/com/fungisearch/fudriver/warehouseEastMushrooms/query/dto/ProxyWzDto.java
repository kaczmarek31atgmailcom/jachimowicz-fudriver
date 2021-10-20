package com.fungisearch.fudriver.warehouseEastMushrooms.query.dto;

import java.util.Date;
import java.util.List;

public class ProxyWzDto {
    public Integer wzId;
    public Integer supplierId;
    public Integer wzNumber;
    public Integer companyId;
    public String companyName;
    public String companyNip;
    public String companyGGN;
    public String companyCity;
    public String companyStreet;
    public Date creationDate;
    public String creatorLogin;
    public String creatorName;
    public String creatorSurname;
    public String customerName;
    public List<ProxyUnitDto> units;
}
