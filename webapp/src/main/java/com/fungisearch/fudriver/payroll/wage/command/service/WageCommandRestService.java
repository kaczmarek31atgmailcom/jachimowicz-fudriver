package com.fungisearch.fudriver.payroll.wage.command.service;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.wage.command.*;
import com.fungisearch.fudriver.payroll.wage.query.dao.WageDao;
import com.fungisearch.fudriver.payroll.wage.query.dto.PersonWageDto;
import com.fungisearch.fudriver.payroll.wage.query.dto.WageDto;
import com.fungisearch.fudriver.payroll.wage.query.dto.WageHeaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/rest/wage", produces = "application/json; charset=UTF-8")
public class WageCommandRestService {

    private final AddWageHeaderCommandHandler addWageHeaderCommandHandler;
    private final UpdateWageValueCommandHandler updateWageValueCommandHandler;
    private final WageDao wageDao;
    private final UpdateEmployeeSalaryTypeCommandHandler updateEmployeeSalaryTypeCommandHandler;
    private final UpdateEmployeeAccordHeaderCommandHandler updateEmployeeAccordHeaderCommandHandler;
    private final UpdateEmployeeHourlyRegularWageCommandHandler updateEmployeeHourlyRegularWageCommandHandler;
    private final UpdateEmployeeHourlySundayWageCommandHandler updateEmployeeHourlySundayWageCommandHandler;
    private final UpdateEmployeeHourlyBonusWageCommandHandler updateEmployeeHourlyBonusWageCommandHandler;

    private final String JSON = "application/json; charset=UTF-8";

    @Autowired
    public WageCommandRestService(AddWageHeaderCommandHandler addWageHeaderCommandHandler, UpdateWageValueCommandHandler updateWageValueCommandHandler, WageDao wageDao, UpdateEmployeeSalaryTypeCommandHandler updateEmployeeSalaryTypeCommandHandler, UpdateEmployeeAccordHeaderCommandHandler updateEmployeeAccordHeaderCommandHandler, UpdateEmployeeHourlyRegularWageCommandHandler updateEmployeeHourlyRegularWageCommandHandler, UpdateEmployeeHourlySundayWageCommandHandler updateEmployeeHourlySundayWageCommandHandler, UpdateEmployeeHourlyBonusWageCommandHandler updateEmployeeHourlyBonusWageCommandHandler) {
        this.addWageHeaderCommandHandler = addWageHeaderCommandHandler;
        this.updateWageValueCommandHandler = updateWageValueCommandHandler;
        this.wageDao = wageDao;
        this.updateEmployeeSalaryTypeCommandHandler = updateEmployeeSalaryTypeCommandHandler;
        this.updateEmployeeAccordHeaderCommandHandler = updateEmployeeAccordHeaderCommandHandler;
        this.updateEmployeeHourlyRegularWageCommandHandler = updateEmployeeHourlyRegularWageCommandHandler;
        this.updateEmployeeHourlySundayWageCommandHandler = updateEmployeeHourlySundayWageCommandHandler;
        this.updateEmployeeHourlyBonusWageCommandHandler = updateEmployeeHourlyBonusWageCommandHandler;
    }

    @RequestMapping(value = "/wageHeader", method = RequestMethod.POST, consumes = JSON)
    CommandResult addWageHeader(@RequestBody AddWageHeaderCommand command) {
        return addWageHeaderCommandHandler.handle(command);
    }

    @RequestMapping(value = "/wageHeader", method = RequestMethod.GET)
    public List<WageHeaderDto> getWageHeaders() {
        return wageDao.getWageHeaders();
    }

    @RequestMapping(value = "/{headerId}", method = RequestMethod.GET)
    public List<WageDto> getWages(@PathVariable(name = "headerId") long headerId) {
        return wageDao.getWages(headerId);
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = JSON)
    CommandResult updateWage(@RequestBody UpdateWageValueCommand command) {
        return updateWageValueCommandHandler.handle(command);
    }

    @RequestMapping(value = "/activePeople", method = RequestMethod.GET)
    public List<PersonWageDto> getActivePeopleWages() {
        return wageDao.getActivePeopleWages();
    }

    @RequestMapping(value = "/employee/payrollType", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult changeEmployeePayrollType(@RequestBody UpdateEmployeeSalaryTypeCommand command) {
        return updateEmployeeSalaryTypeCommandHandler.handle(command);
    }

    @RequestMapping(value = "/employee/accordHeader", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult changeEmployeeAccordHeader(@RequestBody UpdateEmployeeAccordHeaderCommand command) {
        return updateEmployeeAccordHeaderCommandHandler.handle(command);
    }

    @RequestMapping(value = "/employee/hourlyRegularWage", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult changeEmployeeHourlyRegularWage(@RequestBody UpdateEmployeeHourlyWageCommand command) {
        return updateEmployeeHourlyRegularWageCommandHandler.handle(command);
    }

    @RequestMapping(value = "/employee/hourlySundayWage", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult changeEmployeeHourlySundayWage(@RequestBody UpdateEmployeeHourlyWageCommand command) {
        return updateEmployeeHourlySundayWageCommandHandler.handle(command);
    }

    @RequestMapping(value = "/employee/hourlyBonusWage", method = RequestMethod.PUT, consumes = JSON)
    public CommandResult changeEmployeeHourlyBonusWage(@RequestBody UpdateEmployeeHourlyWageCommand command) {
        return updateEmployeeHourlyBonusWageCommandHandler.handle(command);
    }
}

