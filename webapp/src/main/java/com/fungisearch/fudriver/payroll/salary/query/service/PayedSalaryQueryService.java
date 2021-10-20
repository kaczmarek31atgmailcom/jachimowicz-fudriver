package com.fungisearch.fudriver.payroll.salary.query.service;

import com.fungisearch.fudriver.payroll.salary.query.dao.SalaryDao;
import com.fungisearch.fudriver.payroll.salary.query.dto.payed.PayedPersonBonusHeaderDto;
import com.fungisearch.fudriver.payroll.salary.query.dto.payed.PayedPersonSalaryHeaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayedSalaryQueryService {

    private final SalaryDao salaryDao;

    @Autowired
    public PayedSalaryQueryService(SalaryDao salaryDao) {
        this.salaryDao = salaryDao;
    }

    public List<PayedPersonSalaryHeaderDto> getHeaders(long monthId) {
        List<PayedPersonSalaryHeaderDto> headers = salaryDao.getPayedHeaders(monthId);
        List<PayedPersonBonusHeaderDto> allBonuses = salaryDao.getPayedBonuses(monthId);
        for (PayedPersonSalaryHeaderDto salary : headers) {
            for (PayedPersonBonusHeaderDto bonus : allBonuses) {
                if (bonus.personId == salary.personId) {
                    salary.bonuses.add(bonus);
                }
            }
        }
        return headers;
    }
}
