package com.fungisearch.fudriver.timeRecorder.command.model;

import com.fungisearch.fudriver.timeRecorder.command.repository.TimeWorkLogRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.joda.time.DateTime;
import org.springframework.context.ApplicationEventPublisher;
import java.util.Date;

/** Repairs missed daily closing inside the caller's employee-locked transaction. */
public final class PastWorkPeriodCloser {
    private PastWorkPeriodCloser() {}

    public static void close(Long personId, TimeWorkLogRepository repository,
                             ApplicationEventPublisher events, BeanValidator validator) {
        close(personId, repository, events, validator, null, null);
    }

    public static void close(Long personId, TimeWorkLogRepository repository,
                             ApplicationEventPublisher events, BeanValidator validator,
                             Long editedId, Date proposedStart) {
        repository.lockPerson(personId);
        for (TimeWorkLog log : repository.findOpenedBefore(personId, new DateTime().withTimeAtStartOfDay().toDate())) {
            log.setTimeWorkLogRepository(repository);
            log.setBeanValidator(validator);
            log.applicationEventPublisher = events;
            log.closePastPeriod(log.getId().equals(editedId) ? null : proposedStart);
        }
    }
}
