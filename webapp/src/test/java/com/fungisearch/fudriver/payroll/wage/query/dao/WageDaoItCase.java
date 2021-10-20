package com.fungisearch.fudriver.payroll.wage.query.dao;

import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.payroll.wage.query.dto.PersonWageDto;
import com.fungisearch.fudriver.person.person.command.model.PayrollTypeEnum;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.testTools.CreatePerson;
import com.fungisearch.fudriver.testTools.CreateType;
import com.fungisearch.fudriver.testTools.FlushDao;
import com.fungisearch.fudriver.type.command.model.Type;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration(locations = {"/test-spring.xml"})
public class WageDaoItCase extends AbstractTransactionalJUnit4SpringContextTests{

    @Autowired
    private CreatePerson createPerson;
    @Autowired
    private WageFactory wageFactory;
    @Autowired
    private CreateType createType;
    @Autowired
    private FlushDao flushDao;
    @Autowired
    private WageDao wageDao;

    @Test
    public void shouldReturnActivePeopleWages(){
        //Given
        Person person0 = createPerson.create();
        Person person1 = createPerson.create();
        Type type0 = createType.create();
        Type type1 = createType.create();
        WageHeader wageHeader0 = wageFactory.headerBuilder().name("test_header_0").build();
        WageHeader wageHeader1 = wageFactory.headerBuilder().name("test_header_1").build();
        person0.setWageHeader(wageHeader0);
        person1.setWageHeader(wageHeader1);
        person0.switchToAccordAccordPayed();
        person1.switchToHourlyPayed();
        person1.setRegularWage(100);
        person1.setSundayWage(200);
        person1.setBonusWage(300);
        flushDao.flush();
        //When
        List<PersonWageDto> tested = wageDao.getActivePeopleWages();
        //Then
        assertEquals(2,tested.size());
        assertEquals("test_header_0",tested.get(0).wageHeaderName);
        assertEquals(PayrollTypeEnum.ACCORD,tested.get(0).payrollType);
        assertEquals("test_header_1",tested.get(1).wageHeaderName);
        assertEquals(PayrollTypeEnum.HOURLY,tested.get(1).payrollType);
        assertEquals(100,tested.get(1).hourlyRegularWage);
        assertEquals(200,tested.get(1).hourlySundayWage);
        assertEquals(300,tested.get(1).hourlyBonusWage);
        assertEquals(person0.getName(),tested.get(0).name);
        assertEquals(person0.getSurname(),tested.get(0).surname);
        assertEquals(person1.getName(),tested.get(1).name);
        assertEquals(person1.getSurname(),tested.get(1).surname);
    }

}