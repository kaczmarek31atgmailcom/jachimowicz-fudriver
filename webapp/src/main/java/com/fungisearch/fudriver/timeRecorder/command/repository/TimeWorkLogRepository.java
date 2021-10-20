package com.fungisearch.fudriver.timeRecorder.command.repository;

import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 06.04.16.
 */
public interface TimeWorkLogRepository {
    TimeWorkLog findOpen(Long personId);
    void save(TimeWorkLog timeWorkLog);
    TimeWorkLog find(Long id);
    void delete(TimeWorkLog timeWorkLog);

    List<TimeWorkLog> findAllOpened();

    List<TimeWorkLog> findForPeriod(Date firstDay, Date lastDay, Person person);

    List<Person> findPeopleInPeriod(Date firstDay, Date lastDay);

    List<TimeWorkLog> findAllInPeriod(Date startOfDay, Date endOfDay);
}
