package com.fungisearch.fudriver.person.person.command.model;

import com.fungisearch.fudriver.person.person.command.repository.TimeSheetRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by idea on 08.03.16.
 */
@Repository
public class TimeSheetFactory {

    @Autowired
    private TimeSheetRepository timeSheetRepository;

    @Autowired
    private BeanValidator beanValidator;

    public TimeSheet findLatestOne(Long personId) {
        TimeSheet timeSheet = timeSheetRepository.findLatestOne(personId);
        timeSheet.setTimeSheetRepository(timeSheetRepository);
        timeSheet.setBeanValidator(beanValidator);
        return timeSheet;
    }

    public TimeSheet.TimeSheetBuilder builder() {
        TimeSheet.TimeSheetBuilder builder = new TimeSheet.TimeSheetBuilder(timeSheetRepository, beanValidator);
        return builder;
    }
}
