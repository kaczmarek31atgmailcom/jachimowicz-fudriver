package com.fungisearch.fudriver.timeRecorder.query.dto;

import java.util.Date;

/**
 * Created by marcin on 13.01.17.
 */
public class PersonDailyWorkTimeDto {
    public Long personId;
    public Long nr;
    public String name;
    public String surname;
    public Date day;
    public Long workMinutes;
}
