package com.fungisearch.fudriver.payroll.salary.query.dto.notPayed;

import java.util.Date;

public class PersonWorkTimeDto {
    public long personId;
    public Date startTime;
    public Date endTime;
    public boolean isClosed;

    public long getPersonId() {
        return personId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public String toString() {
        return "PersonWorkTimeDto{" +
                "personId=" + personId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", isClosed=" + isClosed +
                '}';
    }
}
