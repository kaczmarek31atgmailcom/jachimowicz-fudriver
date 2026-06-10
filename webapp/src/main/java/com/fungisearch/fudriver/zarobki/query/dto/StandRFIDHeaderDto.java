package com.fungisearch.fudriver.zarobki.query.dto;

import java.util.Date;
import java.util.List;

public class StandRFIDHeaderDto {
    public long personId;
    public String personName;
    public String personSurname;
    public int lastDayExport;
    public int periodExport;
    public Date today;
    public Date yesterday;
    public Date dayBeforeYestarday;
    public Date startOfTheMonth;
    public List<StandDetailDto> lastDayDetails;
    public List<StandDetailDto> dayBeforeLastDayDetails;
    public List<StandDetailDto> periodDetails;
}
