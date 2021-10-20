package com.fungisearch.fudriver.timeRecorder.command.service;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.person.person.query.dao.PersonDao;
import com.fungisearch.fudriver.person.person.query.dto.WorkTimePersonHeaderDto;
import com.fungisearch.fudriver.timeRecorder.command.*;
import com.fungisearch.fudriver.timeRecorder.command.model.WorkPeriodSummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by marcin on 06.04.16.
 */
@RestController
public class TimeRecorderRestController {


    private final PersonDao personDao;
    private final UpdateTimeRecordCommandHandler updateTimeRecordCommandHandler;
    private final DeleteTimeRecordCommandHandler deleteTimeRecordCommandHandler;
    private final CloseTimeWorkCommandHandler closeTimeWorkCommandHandler;
    private final AddTimeRecordCommandHandler addTimeRecordCommandHandler;
    private final CloseTimeRecordCommandHandler closeTimeRecordCommandHandler;
    private final OpenTimeRecordCommandHandler openTimeRecordCommandHandler;
    private final CreateClosedTimeRecordCommandHandler createClosedTimeRecordCommandHandler;

    @Autowired
    public TimeRecorderRestController(PersonDao personDao, UpdateTimeRecordCommandHandler updateTimeRecordCommandHandler, DeleteTimeRecordCommandHandler deleteTimeRecordCommandHandler, CloseTimeWorkCommandHandler closeTimeWorkCommandHandler, AddTimeRecordCommandHandler addTimeRecordCommandHandler, CloseTimeRecordCommandHandler closeTimeRecordCommandHandler, OpenTimeRecordCommandHandler openTimeRecordCommandHandler, CreateClosedTimeRecordCommandHandler createClosedTimeRecordCommandHandler) {
        this.personDao = personDao;
        this.updateTimeRecordCommandHandler = updateTimeRecordCommandHandler;
        this.deleteTimeRecordCommandHandler = deleteTimeRecordCommandHandler;
        this.closeTimeWorkCommandHandler = closeTimeWorkCommandHandler;
        this.addTimeRecordCommandHandler = addTimeRecordCommandHandler;
        this.closeTimeRecordCommandHandler = closeTimeRecordCommandHandler;
        this.openTimeRecordCommandHandler = openTimeRecordCommandHandler;
        this.createClosedTimeRecordCommandHandler = createClosedTimeRecordCommandHandler;
    }

    @RequestMapping(value = "/rest/workTimeRecorder/person/{id}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    WorkTimePersonHeaderDto getPeople(@PathVariable Long id) {
        WorkTimePersonHeaderDto person = personDao.findWorkTimePerson(id);
        return person;
    }

    @RequestMapping(value = "/rest/person/workPeriod", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8", produces="application/json; charset-UTF-8")
    CommandResult updateWorkPeriod(@RequestBody UpdateTimeRecordCommand command){
        return updateTimeRecordCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/person/workPeriod", method = RequestMethod.DELETE, consumes = "application/json; charset=UTF-8", produces="application/json; charset-UTF-8")
    CommandResult deleteWorkPeriod(@RequestBody DeleteTimeRecordCommand command){
        return deleteTimeRecordCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/person/workPeriod/close", method = RequestMethod.PUT, consumes = "application/json; charset=UTF-8", produces = "application/json; charset=UTF-8")
    CommandResult closeWorkPeriod(@RequestBody Long periodId){
        return closeTimeWorkCommandHandler.handle(periodId);
    }

    @RequestMapping(value = "/rest/workTimeRecorder/badge-event", method=RequestMethod.POST, produces = "application/json; charset=UTF-8", consumes ="application/json; charset=UTF-8")
    WorkPeriodSummaryDto registerBadgeEvent(@RequestBody AddTimeRecordCommand command){
        return addTimeRecordCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/workTimeRecorder/close-only-badge-event", method=RequestMethod.POST, produces = "application/json; charset=UTF-8", consumes ="application/json; charset=UTF-8")
    WorkPeriodSummaryDto closeTimeRecordBadgeEvent(@RequestBody AddTimeRecordCommand command){
        return closeTimeRecordCommandHandler.handle(command);
    }

    @RequestMapping(value = "/rest/workTimeRecorder/open-only-badge-event", method=RequestMethod.POST, produces = "application/json; charset=UTF-8", consumes ="application/json; charset=UTF-8")
    WorkPeriodSummaryDto openTimeRecordBadgeEvent(@RequestBody AddTimeRecordCommand command){
        return openTimeRecordCommandHandler.handle(command);
    }

    @RequestMapping(value ="/rest/workTimeRecorder/closed", method = RequestMethod.POST, produces = "application/json; charset=UTF-8", consumes = "application/json; charset=UTF-8")
    CommandResult createClosedTimePeriod(@RequestBody CreateClosedTimeRecordCommand command){
        return createClosedTimeRecordCommandHandler.handle(command);
    }
}
