package com.fungisearch.fudriver.timeRecorder.command.service;

import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Component
@Transactional
public class ScheduledOpenedTimeWorkLogsRemover {

    @Autowired
    private TimeWorkLogFactory timeWorkLogFactory;

    @Scheduled(cron = "* 59 23 * * *")
    @Async
    public void closeOpenWorkPeriods() {
        for (TimeWorkLog timeWorkLog : timeWorkLogFactory.findAllOpened()) {
            if (timeWorkLog.getOpened()) {
                timeWorkLog.close();
            }
        }
    }
}

