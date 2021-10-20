package com.fungisearch.fudriver.zarobki.query.dto;

import java.util.List;

public class StandHeaderDto {
    public long personId;
    public String personName;
    public String personSurname;
    public int lastDayExport;
    public int periodExport;
    public List<StandDetailDto> lastDayDetails;
    public List<StandDetailDto> periodDetails;
}
