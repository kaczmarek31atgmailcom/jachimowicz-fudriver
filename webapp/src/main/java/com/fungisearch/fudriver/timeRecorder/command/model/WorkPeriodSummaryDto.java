package com.fungisearch.fudriver.timeRecorder.command.model;

import java.util.Date;

/**
 * Created by marcin on 06.04.16.
 */
public class WorkPeriodSummaryDto {
    public Long personId;
    public Long nr;
    public String name;
    public String surname;
    public Boolean active;
    public Date startDate;
    public Date endDate;
    public String rfid;
    public boolean isStateUpdated = false;
}
