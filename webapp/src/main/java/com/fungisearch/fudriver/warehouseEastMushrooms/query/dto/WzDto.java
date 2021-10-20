package com.fungisearch.fudriver.warehouseEastMushrooms.query.dto;

import java.util.Date;
import java.util.List;

public class WzDto {
    public Long wzId;
    public Integer wzNr;
    public String companyName;
    public String companyCity;
    public String companyStreet;
    public String companyNip;
    public String companyGGN;
    public String customerName;
    public Date date;
    public String creatorLogin;
    public String creatorName;
    public String creatorSurname;
    public List<WzTypeDto> types;
}
