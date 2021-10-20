package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.repository.TimeWorkLogRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by marcin on 15.01.17.
 */
@Service
public class CreateTimeWorkLog {

    private final TimeWorkLogRepository timeWorkLogRepository;
    private final BeanValidator beanValidator;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PersonFactory personFactory;

    @Autowired
    public CreateTimeWorkLog(TimeWorkLogRepository timeWorkLogRepository,
                             ApplicationEventPublisher applicationEventPublisher,
                             BeanValidator beanValidator,
                             PersonFactory personFactory){
        this.timeWorkLogRepository = timeWorkLogRepository;
        this.applicationEventPublisher = applicationEventPublisher;
        this.beanValidator = beanValidator;
        this.personFactory = personFactory;
    }

    public TimeWorkLog create(Date startDate, Date endDate, Long personId){
        TimeWorkLog timeWorkLog = new TimeWorkLog.TimeWorkLogBuilder(timeWorkLogRepository,applicationEventPublisher,beanValidator)
                .person(personFactory.find(personId))
                .build();
        timeWorkLog.register();
        timeWorkLog.close();
        timeWorkLog.edit(new TimeWorkLog.Edit()
        .startDate(startDate)
        .endDate(endDate));
        return timeWorkLog;
    }
}
