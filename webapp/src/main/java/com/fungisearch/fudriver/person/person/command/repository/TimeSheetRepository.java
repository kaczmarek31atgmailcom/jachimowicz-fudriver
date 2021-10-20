package com.fungisearch.fudriver.person.person.command.repository;

import com.fungisearch.fudriver.person.person.command.model.TimeSheet;

/**
 * Created by marcin on 03.03.16.
 */
public interface TimeSheetRepository {
    void addTimeSheet(TimeSheet timeSheet);
    TimeSheet findLatestOne(Long personId);
    TimeSheet findOne(Long id);
}
