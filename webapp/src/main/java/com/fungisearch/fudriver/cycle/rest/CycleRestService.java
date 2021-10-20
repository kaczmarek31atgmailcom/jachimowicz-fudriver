package com.fungisearch.fudriver.cycle.rest;

import com.fungisearch.fudriver.common.DateUtils;
import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.cycle.command.*;
import com.fungisearch.fudriver.cycle.query.dao.CycleDao;
import com.fungisearch.fudriver.cycle.query.dto.*;
import com.fungisearch.fudriver.cycle.query.service.CycleDailyReportService;
import com.fungisearch.fudriver.cycle.query.service.CycleTechnologistReport.CycleTechnologistReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = "/rest/cycle", produces = "application/json; charset=UTF-8")
public class CycleRestService {
    private final String JSON = "application/json; charset=UTF-8";

    private final CreateCycleCommandHandler createCycleCommandHandler;
    private final EditCycleStartDateCommandHandler editCycleStartDateCommandHandler;
    private final EditCycleStartFirstPeriodCommandHandler editCycleStartFirstPeriodCommandHandler;
    private final EditCycleStartSecondPeriodCommandHandler editCycleStartSecondPeriodCommandHandler;
    private final EditCycleStartThirdPeriodDateCommandHandler editCycleStartThirdPeriodDateCommandHandler;
    private final EditCycleAreaCommandHandler editCycleAreaCommandHandler;
    private final EditCycleWeightCommandHandler editCycleWeightCommandHandler;
    private final EditCycleHumidityCommandHandler editCycleHumidityCommandHandler;
    private final EditCycleMyceliumCommandHandler editCycleMyceliumCommandHandler;
    private final EditCycleSubsoilCommandHandler editCycleSubsoilCommandHandler;
    private final EditCycleTechnologistCommandHandler editCycleTechnologistCommandHandler;
    private final CloseCycleCommandHandler closeCycleCommandHandler;
    private final CycleDao cycleDao;
    private final CycleDailyReportService cycleDailyReportService;
    private final CycleTechnologistReport cycleTechnologistReport;

    @Autowired
    public CycleRestService(CreateCycleCommandHandler createCycleCommandHandler, EditCycleStartDateCommandHandler editCycleStartDateCommandHandler, EditCycleStartFirstPeriodCommandHandler editCycleStarFirstPeriodCommandHandler, EditCycleStartSecondPeriodCommandHandler editCycleStartSecondPeriodCommandHandler, EditCycleStartThirdPeriodDateCommandHandler editCycleStartThirdPeriodDateCommandHandler, EditCycleAreaCommandHandler editCycleAreaCommandHandler, EditCycleWeightCommandHandler editCycleWeightCommandHandler, EditCycleHumidityCommandHandler editCycleHumidityCommandHandler, EditCycleMyceliumCommandHandler editCycleMyceliumCommandHandler, EditCycleSubsoilCommandHandler editCycleSubsoilCommandHandler, EditCycleTechnologistCommandHandler editCycleTechnologistCommandHandler, CloseCycleCommandHandler closeCycleCommandHandler, CycleDao cycleDao, CycleDailyReportService cycleDailyReportService, CycleTechnologistReport cycleTechnologistReport) {
        this.createCycleCommandHandler = createCycleCommandHandler;
        this.editCycleStartDateCommandHandler = editCycleStartDateCommandHandler;
        this.editCycleStartFirstPeriodCommandHandler = editCycleStarFirstPeriodCommandHandler;
        this.editCycleStartSecondPeriodCommandHandler = editCycleStartSecondPeriodCommandHandler;
        this.editCycleStartThirdPeriodDateCommandHandler = editCycleStartThirdPeriodDateCommandHandler;
        this.editCycleAreaCommandHandler = editCycleAreaCommandHandler;
        this.editCycleWeightCommandHandler = editCycleWeightCommandHandler;
        this.editCycleHumidityCommandHandler = editCycleHumidityCommandHandler;
        this.editCycleMyceliumCommandHandler = editCycleMyceliumCommandHandler;
        this.editCycleSubsoilCommandHandler = editCycleSubsoilCommandHandler;
        this.editCycleTechnologistCommandHandler = editCycleTechnologistCommandHandler;
        this.closeCycleCommandHandler = closeCycleCommandHandler;
        this.cycleDao = cycleDao;
        this.cycleDailyReportService = cycleDailyReportService;
        this.cycleTechnologistReport = cycleTechnologistReport;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = JSON)
    public CommandResult createCycle(@RequestBody CreateCycleCommand command) {
        return createCycleCommandHandler.handle(command);
    }

    @RequestMapping(value = "/startDate", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult editCycleStartDate(@RequestBody EditCycleStartDateCommand command) {
        return editCycleStartDateCommandHandler.handle(command);
    }


    @RequestMapping(value = "/startFirstPeriod", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult editCycleStartFirstPeriodDate(@RequestBody EditCycleStartFirstPeriodCommand command) {
        return editCycleStartFirstPeriodCommandHandler.handle(command);
    }

    @RequestMapping(value = "/startSecondPeriod", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult editCycleStartSecondPeriodDate(@RequestBody EditCycleStartSecondPeriodCommand command) {
        return editCycleStartSecondPeriodCommandHandler.handle(command);
    }

    @RequestMapping(value = "/startThirdPeriod", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult editCycleStartThirdPeriodDate(@RequestBody EditCycleStartThirdPeriodDateCommand command) {
        return editCycleStartThirdPeriodDateCommandHandler.handle(command);
    }

    @RequestMapping(value = "/area", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult editCycleArea(@RequestBody EditCycleAreaCommand command) {
        return editCycleAreaCommandHandler.handle(command);
    }

    @RequestMapping(value = "/weight", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult editCycleWeight(@RequestBody EditCycleWeightCommand command) {
        return editCycleWeightCommandHandler.handle(command);
    }

    @RequestMapping(value = "/humidity", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult editCycleHumidity(@RequestBody EditCycleHumidityCommand command) {
        return editCycleHumidityCommandHandler.handle(command);
    }

    @RequestMapping(value = "/mycelium", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult editCycleMycelium(@RequestBody EditCycleMyceliumCommand command) {
        return editCycleMyceliumCommandHandler.handle(command);
    }

    @RequestMapping(value = "/subsoil", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult editCycleSubsoil(@RequestBody EditCycleSubsoilCommand command) {
        return editCycleSubsoilCommandHandler.handle(command);
    }

    @RequestMapping(value = "/technologist", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult editCycleTechnologist(@RequestBody EditCycleTechnologistCommand command) {
        return editCycleTechnologistCommandHandler.handle(command);
    }

    @RequestMapping(value = "/close", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult closeCycle(@RequestBody CloseCycleCommand command) {
        return closeCycleCommandHandler.handle(command);
    }

    @RequestMapping(value = "/{startDay}/{endDay}", method = RequestMethod.GET)
    public Set<CycleHeaderDto> getCycleHeaders(@PathVariable(name = "startDay") int startDay,
                                               @PathVariable(name = "endDay") int endDay) {
        return cycleDao.findAllCycles(startDay, endDay);
    }

    @RequestMapping(value = "/header/{cycleId}", method = RequestMethod.GET)
    public CycleHeaderDto findCycleHeader(@PathVariable(name = "cycleId") long cycleId) {
        return cycleDao.findHeader(cycleId);
    }

    @RequestMapping(value = "/dates", method = RequestMethod.GET)
    private Set<CycleDatesDto> getCycleDates() {
        return cycleDao.findCurrentCycleDates();
    }

    @RequestMapping(value = "/{id}/dates", method = RequestMethod.GET)
    private CycleDatesDto getCycleDatesByCycleId(@PathVariable(name = "id") long id) {
        return cycleDao.findCycleDatesById(id);
    }

    @RequestMapping(value = "/chamber/{id}/dates", method = RequestMethod.GET)
    private CycleDatesDto getCycleDatesByChamberId(@PathVariable(name = "id") long id) {
        return cycleDao.findCycleDatesByChamberId(id);
    }

    @RequestMapping(value = "/{cycleId}/details", method = RequestMethod.GET)
    private List<CyclePeriodDto> getDaysOfTheCycle(@PathVariable(value = "cycleId") long cycleId) {
        return cycleDailyReportService.getCycleDetails(cycleId);
    }

    @RequestMapping(value = "/brigades/{startDate}/{endDate}", method = RequestMethod.GET)
    private List<CycleByBrigadeDto> getCycleByBrigades(@PathVariable(name = "startDate") @DateTimeFormat(pattern = "yyyyMMdd") Date startDate,
                                                       @PathVariable(name = "endDate") @DateTimeFormat(pattern = "yyyyMMdd") Date endDate) {
        return cycleDao.findCycleByBrigades(DateUtils.getStartOfDay(startDate), DateUtils.getEndOfDay(endDate));
    }

    @RequestMapping(value = "/technologists/header/{startDate}/{endDate}", method = RequestMethod.GET)
    private CycleTechnologistDto getTechnologistHeaders(@PathVariable(name = "startDate") int startDate,
                                                        @PathVariable(name = "endDate") int endDate) {
        return cycleTechnologistReport.getReport(startDate, endDate);
    }
}
