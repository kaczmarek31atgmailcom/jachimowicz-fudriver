package com.fungisearch.fudriver.payroll.salary.query.dao;


import com.fungisearch.fudriver.payroll.salary.query.dto.PersonSalaryAccountHistoryDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.PersonSalaryAccountStatusDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.notPayed.*;
import com.fungisearch.fudriver.payroll.salary.query.dto.payed.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SalaryDao {
    List<HarvestByPersonAndWageDto> getHarvestByPersonAndWage(String timeshort);
    List<WorkTimeByPersonAndDayTypeDto> getWorkTimeByPersonAndDayType(String timeshort);
    List<PersonSalaryHeaderDto> getPersonNames(String timeshort);
    List<PayrollMonthDto> getPayrollMonths();
    List<PayrollBonusDto> getActiveBonuses();
    List<BonusPersonAssignementDto> getAssignedBonuses(long payrollMonthId);

    List<PayedPersonSalaryHeaderDto> getPayedHeaders(long monthId);
    List<PayedPersonBonusHeaderDto> getPayedBonuses(long monthId);
    List<PersonSalaryAccountStatusDto> getSalaryAccountStatus();
    List<PersonSalaryAccountHistoryDto> getPersonSalaryAccountHistory(long personId);
    Map<Long, Long> getExportRate(String timeshort);
    List<PayedPersonSalaryHarvestDetailDto> getPayedHarvestDetails(long personId,long payoffDetailId);
    List<PayedPersonSalaryBonusDetailDto> getPayedBonusesDetails(long personId, long payoffDetailId);
    PayedPersonSalaryTimeDetailDto getPayedTimeDetails(long personId, long payoffDetailId);
    String getPayrollMonthNameById(long id);
    List<PersonWorkTimeDto> getMonthlyWorkTime(Date startDate, Date endDate);
    Map<Long,Integer> getWorkingDaysAmount(String timeshort);
}
