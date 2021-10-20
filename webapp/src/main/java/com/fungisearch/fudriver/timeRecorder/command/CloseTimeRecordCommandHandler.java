package com.fungisearch.fudriver.timeRecorder.command;


import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.WorkPeriodSummaryDto;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiEntry;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class CloseTimeRecordCommandHandler {

    private final TimeWorkLogFactory timeWorkLogFactory;
    private final ZarobkiFactory zarobkiFactory;
    private final PersonFactory personFactory;

    @Autowired
    public CloseTimeRecordCommandHandler(TimeWorkLogFactory timeWorkLogFactory, ZarobkiFactory zarobkiFactory, PersonFactory personFactory) {
        this.timeWorkLogFactory = timeWorkLogFactory;
        this.zarobkiFactory = zarobkiFactory;
        this.personFactory = personFactory;
    }


    public WorkPeriodSummaryDto handle(AddTimeRecordCommand command) {
        Person person = personFactory.createByRfid(command.rfid);
        WorkPeriodSummaryDto dto = new WorkPeriodSummaryDto();
        if (person != null) {
            TimeWorkLog timeWorkLog = timeWorkLogFactory.findOpen(person.getId());
            if (timeWorkLog != null) {
                timeWorkLog.register();
                dto.isStateUpdated = true;
                dto.startDate = timeWorkLog.getStartTime();
                dto.endDate = timeWorkLog.getEndTime();
                Long workMinutes = findTimeDifference(timeWorkLog.getStartTime(), timeWorkLog.getEndTime());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
                Long timeshort = Long.parseLong(sdf.format(new Date()));
                ZarobkiEntry zarobkiEntry = zarobkiFactory.builder().
                        pickerId(timeWorkLog.getPerson().getId()).
                        payed(false).
                        workingMinutes(workMinutes).
                        timeshort(timeshort).
                        harvestTime(new Date()).build();
                zarobkiEntry.create();
            }
            dto.personId = person.getId();
            //dto.active = timeWorkLog.getOpened();
            dto.nr = person.getNr();
            dto.name = person.getName();
            dto.surname = person.getSurname();

            dto.rfid = command.rfid;

        } else {
            dto.personId = 0L;
            dto.rfid = command.rfid;
        }
        return dto;
    }

    private long findTimeDifference(Date startTime, Date endTime) {
        long duration = endTime.getTime() - startTime.getTime();
        return TimeUnit.MILLISECONDS.toMinutes(duration);
    }

}
