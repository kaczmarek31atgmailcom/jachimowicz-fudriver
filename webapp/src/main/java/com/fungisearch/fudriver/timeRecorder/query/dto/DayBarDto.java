package com.fungisearch.fudriver.timeRecorder.query.dto;

import com.fungisearch.fudriver.person.person.query.yearBar.model.BarPeriod;

import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 19.05.16.
 */
public class DayBarDto {
    public Date day;
    public List<BarPeriod> barPeriods;
}
