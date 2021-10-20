package com.fungisearch.fudriver.timeRecorder.command;

import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLog;
import com.fungisearch.fudriver.timeRecorder.command.model.TimeWorkLogFactory;
import com.fungisearch.fudriver.timeRecorder.command.model.WorkPeriodSummaryDto;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OpenTimeRecordCommandHandler {


    private final TimeWorkLogFactory timeWorkLogFactory;
    private final ZarobkiFactory zarobkiFactory;
    private final PersonFactory personFactory;

    @Autowired
    public OpenTimeRecordCommandHandler(TimeWorkLogFactory timeWorkLogFactory, ZarobkiFactory zarobkiFactory, PersonFactory personFactory) {
        this.timeWorkLogFactory = timeWorkLogFactory;
        this.zarobkiFactory = zarobkiFactory;
        this.personFactory = personFactory;
    }

    public WorkPeriodSummaryDto handle(AddTimeRecordCommand command) {
        Person person = personFactory.createByRfid(command.rfid);
        WorkPeriodSummaryDto dto = new WorkPeriodSummaryDto();
        if (person != null) {
            TimeWorkLog timeWorkLog = timeWorkLogFactory.findOpen(person.getId());
            if (timeWorkLog == null) {
                timeWorkLog = timeWorkLogFactory.create(person);
                timeWorkLog.register();
                dto.isStateUpdated = true;
            }
            dto.personId = timeWorkLog.getPerson().getId();
            dto.active = timeWorkLog.getOpened();
            dto.nr = person.getNr();
            dto.name = person.getName();
            dto.surname = person.getSurname();
            dto.startDate = timeWorkLog.getStartTime();
            dto.endDate = timeWorkLog.getEndTime();
            dto.rfid = command.rfid;
        } else {
            dto.personId = 0L;
            dto.rfid = command.rfid;
        }
        return dto;
    }
}
