package com.fungisearch.fudriver.chamber.query.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 01.03.17.
 */
public class ChamberTypesDto {
    public long cycleId;
    public String chamberName;
    public Date startDate;
    public double inneTotal = 0.0;
    public double krajTotal = 0.0;
    public double exportTotal = 0.0;
    public double exportRatio = 0.0;
    public List<ChamberTypeDto> types;

}
