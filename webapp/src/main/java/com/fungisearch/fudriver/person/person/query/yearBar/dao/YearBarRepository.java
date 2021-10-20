package com.fungisearch.fudriver.person.person.query.yearBar.dao;

import com.fungisearch.fudriver.person.person.query.yearBar.dto.SimpleWorkPeriodDto;
import com.fungisearch.fudriver.person.person.query.yearBar.dto.WorkPeriodDto;

import java.util.List;

/**
 * Created by idea on 05.03.16.
 */
public interface YearBarRepository {
    WorkPeriodDto findActive(Long personId);
    List<WorkPeriodDto> findAllForPerson(Long personId);
    List<SimpleWorkPeriodDto> findSimpleWorkPeriodsForPerson(Long personId);
}
