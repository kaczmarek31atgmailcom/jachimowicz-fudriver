package com.fungisearch.fudriver.payroll.salary;

import com.fungisearch.fudriver.common.command.CommandResult;
import com.fungisearch.fudriver.payroll.salary.command.CreatePayrollIncreaseCommandHandler;
import com.fungisearch.fudriver.payroll.salary.command.model.CloseMonthCommand;
import com.fungisearch.fudriver.payroll.salary.command.model.CloseMonthCommandHandler;
import com.fungisearch.fudriver.payroll.salary.command.CreatePayrollAccountUpdateCommand;
import com.fungisearch.fudriver.payroll.salary.command.CreatePayrollDecreaseCommandHandler;
import com.fungisearch.fudriver.payroll.salary.command.model.bonus.*;
import com.fungisearch.fudriver.payroll.salary.query.dao.SalaryDao;
import com.fungisearch.fudriver.payroll.salary.query.dto.PersonSalaryAccountHistoryDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.PersonSalaryAccountStatusDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.BonusPersonAssignementDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.PayrollBonusDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.PayrollMonthDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.PersonSalaryHeaderDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.payed.PayedPersonSalaryBonusDetailDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.payed.PayedPersonSalaryHarvestDetailDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.payed.PayedPersonSalaryHeaderDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.payed.PayedPersonSalaryTimeDetailDto;
import com.fungisearch.fudriver.payroll.salary.query.service.NotPayedSalaryQueryService;
import com.fungisearch.fudriver.payroll.salary.query.service.PayedSalaryQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/salary", produces = "application/json; charset=UTF-8")
public class SalaryRestController {
    private final String JSON = "application/json; charset=UTF-8";

    private final NotPayedSalaryQueryService salaryQueryService;
    private final SalaryDao salaryDao;
    private final CreateFixedBonusCommandHandler createFixedBonusCommandHandler;
    private final CreatePercentageBonusCommandHandler createPercentageBonusCommandHandler;
    private final DeleteBonusCommandHandler deleteBonusCommandHandler;
    private final UpdatePersonBonusMonthlyAssignmentCommandHandler updatePersonBonusMonthlyAssignmentCommandHandler;
    private final CloseMonthCommandHandler closeMonthCommandHandler;
    private final PayedSalaryQueryService payedSalaryQueryService;
    private final CreatePayrollDecreaseCommandHandler createPayrollDecreaseCommandHandler;
    private final CreatePayrollIncreaseCommandHandler createPayrollIncreaseCommandHandler;

    @Autowired
    public SalaryRestController(NotPayedSalaryQueryService salaryQueryService, SalaryDao salaryDao, CreateFixedBonusCommandHandler createFixedBonusCommandHandler, CreatePercentageBonusCommandHandler createPercentageBonusCommandHandler, DeleteBonusCommandHandler deleteBonusCommandHandler, UpdatePersonBonusMonthlyAssignmentCommandHandler updatePersonBonusMonthlyAssignmentCommandHandler, CloseMonthCommandHandler closeMonthCommandHandler, PayedSalaryQueryService payedSalaryQueryService, CreatePayrollDecreaseCommandHandler createPayrollDecreaseCommandHandler, CreatePayrollIncreaseCommandHandler createPayrollIncreaseCommandHandler) {
        this.salaryQueryService = salaryQueryService;
        this.salaryDao = salaryDao;
        this.createFixedBonusCommandHandler = createFixedBonusCommandHandler;
        this.createPercentageBonusCommandHandler = createPercentageBonusCommandHandler;
        this.deleteBonusCommandHandler = deleteBonusCommandHandler;
        this.updatePersonBonusMonthlyAssignmentCommandHandler = updatePersonBonusMonthlyAssignmentCommandHandler;
        this.closeMonthCommandHandler = closeMonthCommandHandler;
        this.payedSalaryQueryService = payedSalaryQueryService;
        this.createPayrollDecreaseCommandHandler = createPayrollDecreaseCommandHandler;
        this.createPayrollIncreaseCommandHandler = createPayrollIncreaseCommandHandler;
    }

    @RequestMapping(value = "/{date}", method = RequestMethod.GET)
    public List<PersonSalaryHeaderDto> getSalaryHeaders(@PathVariable(name = "date")
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return salaryQueryService.getHeaders(date);
    }

    @RequestMapping(value = "/payroll-month", method = RequestMethod.GET)
    public List<PayrollMonthDto> getPayrollMonths() {
        return salaryDao.getPayrollMonths();
    }

    @RequestMapping(value = "/payroll-month/name/{id}", method = RequestMethod.GET)
    public String getPayrollMonthName(@PathVariable(value = "id") long id) {
        return salaryDao.getPayrollMonthNameById(id);
    }

    @RequestMapping(value = "/payroll-month", method = RequestMethod.POST)
        public CommandResult closeMonth(@RequestBody  CloseMonthCommand command){
            return closeMonthCommandHandler.handle(command);
        }

    @RequestMapping(value = "/fixed-bonus", method = RequestMethod.POST, consumes = JSON)
    public CommandResult addFixedBonus(@RequestBody CreateBonusCommand command) {
        return createFixedBonusCommandHandler.handle(command);
    }

    @RequestMapping(value = "/percentage-bonus", method = RequestMethod.POST, consumes = JSON)
    public CommandResult addPercentageBonus(@RequestBody CreateBonusCommand command) {
        return createPercentageBonusCommandHandler.handle(command);
    }

    @RequestMapping(value = "/bonus/active", method = RequestMethod.GET)
    public List<PayrollBonusDto> getActiveBonuses() {
        return salaryDao.getActiveBonuses();
    }

    @RequestMapping(value = "/bonus", method = RequestMethod.DELETE, consumes = JSON)
    public CommandResult deleteBonus(@RequestBody Long id) {
        return deleteBonusCommandHandler.handle(id);
    }

    @RequestMapping(value = "/bonus/assignment/{monthId}", method = RequestMethod.GET)
    public List<BonusPersonAssignementDto> getAssignedBonuses(@PathVariable(name = "monthId") long monthId) {
        return salaryDao.getAssignedBonuses(monthId);
    }

    @RequestMapping(value = "/bonus/assignment", method = RequestMethod.POST, consumes = JSON)
    public CommandResult updatePersonBonusMonthlyAssignment(@RequestBody UpdatePersonBonusMonthlyAssignmentCommand command){
        return updatePersonBonusMonthlyAssignmentCommandHandler.handle(command);
    }

    @RequestMapping(value = "/payed/{monthId}", method = RequestMethod.GET)
        public List<PayedPersonSalaryHeaderDto> getPayedHeaders(@PathVariable(name = "monthId") long monthId){
        return payedSalaryQueryService.getHeaders(monthId);
    }

    @RequestMapping(value = "/payed/details/harvest/{personId}/{payoffDetailId}", method = RequestMethod.GET)
    public List<PayedPersonSalaryHarvestDetailDto> getPersonHarvestSalaryDetails(@PathVariable(name = "personId") long personId,
                                                                                 @PathVariable(name = "payoffDetailId") long payoffDetailId){
            return salaryDao.getPayedHarvestDetails(personId,payoffDetailId);
    }

    @RequestMapping(value = "/payed/details/bonus/{personId}/{payoffDetailId}", method = RequestMethod.GET)
    public List<PayedPersonSalaryBonusDetailDto> getPersonSalaryBonusDetails(@PathVariable(name = "personId") long personId,
                                                                               @PathVariable(name = "payoffDetailId") long payoffDetailId){
        return salaryDao.getPayedBonusesDetails(personId,payoffDetailId);
    }

    @RequestMapping(value = "/payed/details/time/{personId}/{payoffDetailId}", method = RequestMethod.GET)
    public PayedPersonSalaryTimeDetailDto getPersonSalaryTimeDetails(@PathVariable(name = "personId") long personId,
                                                                       @PathVariable(name = "payoffDetailId") long payoffDetailId){
        return salaryDao.getPayedTimeDetails(personId,payoffDetailId);
    }


    @RequestMapping(value = "/payment/payoff", method = RequestMethod.POST)
    public CommandResult createSalaryPayoff(@RequestBody CreatePayrollAccountUpdateCommand command){
            return createPayrollDecreaseCommandHandler.handle(command);
    }

    @RequestMapping(value = "/payment/payment", method = RequestMethod.POST)
    public CommandResult createSalaryPayment(@RequestBody CreatePayrollAccountUpdateCommand command){
        return createPayrollIncreaseCommandHandler.handle(command);
    }

    @RequestMapping(value = "/account/status", method = RequestMethod.GET)
    public List<PersonSalaryAccountStatusDto> getCurrentAccountStatus(){
        return salaryDao.getSalaryAccountStatus();
    }

    @RequestMapping(value = "/account/history/{personId}", method = RequestMethod.GET)
    public List<PersonSalaryAccountHistoryDto> getAccountHistory(@PathVariable(name = "personId") long personId){
        return salaryDao.getPersonSalaryAccountHistory(personId);
    }
}
