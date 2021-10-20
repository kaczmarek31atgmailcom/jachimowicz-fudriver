package com.fungisearch.fudriver.testTools;

import com.fungisearch.fudriver.payroll.wage.command.model.Wage;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.type.command.model.Type;
import org.springframework.stereotype.Service;

@Service
public class CreateWage {

    public Wage createRegularWage(WageHeader wageHeader, Type type, long value) {
        Wage wage = wageHeader.getRegularWage(type);
        wage.updateValue(value);
        return wage;
    }


    public Wage createSundayWage(WageHeader wageHeader, Type type, long value) {
        Wage wage = wageHeader.getSundayWage(type);
        wage.updateValue(value);
        return wage;
    }

    public Wage createBonusWage(WageHeader wageHeader, Type type, long value) {
        Wage wage = wageHeader.getBonusWage(type);
        wage.updateValue(value);
        return wage;
    }
}
