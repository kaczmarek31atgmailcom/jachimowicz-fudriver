package com.fungisearch.fudriver.payroll.wage.query.dao;

import com.fungisearch.fudriver.payroll.calendar.command.model.SalaryDayTypeEnum;
import com.fungisearch.fudriver.payroll.wage.command.model.WageFactory;
import com.fungisearch.fudriver.payroll.wage.command.model.WageHeader;
import com.fungisearch.fudriver.payroll.wage.query.dto.WageDto;
import com.fungisearch.fudriver.payroll.wage.query.dto.WageHeaderDto;
import com.fungisearch.fudriver.testTools.CreateType;
import com.fungisearch.fudriver.testTools.FlushDao;
import com.fungisearch.fudriver.type.command.model.Type;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(locations = "/test-spring.xml")
public class WageDaoITCase extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private WageFactory wageFactory;
    @Autowired
    private WageDao wageDao;
    @Autowired
    private FlushDao flushDao;
    @Autowired
    private CreateType createType;


    @Test
    public void shouldGetListOf4Headers(){
        //Given
        WageHeader header0 = createHeader("header_0");
        WageHeader header1 = createHeader("header_1");
        WageHeader header2 = createHeader("header_2");
        WageHeader header3 = createHeader("header_3");
        //When
        List<WageHeaderDto> tested = wageDao.getWageHeaders();
        //Then
        assertEquals(4,tested.size());
    }

    @Test
    public void wageHeaderDtoShouldContainValidNameAndIds(){
        //Given
        WageHeader header0 = createHeader("header_0");
        WageHeader header1 = createHeader("header_1");
        WageHeader header2 = createHeader("header_2");
        WageHeader header3 = createHeader("header_3");
        //When
        flushDao.flush();
        List<WageHeaderDto> tested = wageDao.getWageHeaders();
        //Then
        assertEquals(header0.getId(),(Long)tested.get(0).id);
        assertEquals(header0.getName(),tested.get(0).name);
        assertEquals(header1.getId(),(Long)tested.get(1).id);
        assertEquals(header1.getName(),tested.get(1).name);
        assertEquals(header2.getId(),(Long)tested.get(2).id);
        assertEquals(header2.getName(),tested.get(2).name);
        assertEquals(header3.getId(),(Long)tested.get(3).id);
        assertEquals(header3.getName(),tested.get(3).name);
    }

    @Test
    public void shouldReturnProperWagesForHeader(){
        //Given
        Type type = createType.create();
        WageHeader header = createHeader("header_0");
        flushDao.flush();
        //When
        List<WageDto> tested = wageDao.getWages(header.getId());
        //Then
        assertEquals(3,tested.size());
        assertEquals(0,tested.get(0).value);
        assertEquals(SalaryDayTypeEnum.REGULAR_DAY,tested.get(0).dayType);
        assertEquals(type.getName(),tested.get(0).typeName);
        assertEquals(type.getWeight(),(Double)tested.get(0).typeWeight);
        assertEquals(type.getId(),(Long)tested.get(0).typeId);

        assertEquals(0,tested.get(1).value);
        assertEquals(SalaryDayTypeEnum.SUNDAY,tested.get(1).dayType);
        assertEquals(type.getName(),tested.get(1).typeName);
        assertEquals(type.getWeight(),(Double)tested.get(1).typeWeight);
        assertEquals(type.getId(),(Long)tested.get(1).typeId);

        assertEquals(0,tested.get(2).value);
        assertEquals(SalaryDayTypeEnum.BONUS_DAY,tested.get(2).dayType);
        assertEquals(type.getName(),tested.get(2).typeName);
        assertEquals(type.getWeight(),(Double)tested.get(2).typeWeight);
        assertEquals(type.getId(),(Long)tested.get(2).typeId);
    }

    private WageHeader createHeader(String name){
        return wageFactory.headerBuilder().name(name).build();
    }

}
