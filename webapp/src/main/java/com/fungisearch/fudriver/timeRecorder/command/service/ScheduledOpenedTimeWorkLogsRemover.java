package com.fungisearch.fudriver.timeRecorder.command.service;

import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;



@Component
@Transactional
public class ScheduledOpenedTimeWorkLogsRemover {

    @Autowired
    private TimeWorkLogFactory timeWorkLogFactory;

    @Scheduled(cron = "0 59 23 * * *")
    public void closeOpenWorkPeriods() {
        for (TimeWorkLog timeWorkLog : timeWorkLogFactory.findAllOpened()) {
            // A badge scan may have closed this period since the initial query.
            // Recheck under the same employee lock used by the badge endpoints.
            TimeWorkLog current = timeWorkLogFactory.findOpen(timeWorkLog.getPerson().getId());
            if (current != null) {
                current.close();
            }
        }
    }
}

