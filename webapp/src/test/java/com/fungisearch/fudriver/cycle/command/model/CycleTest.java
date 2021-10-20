package com.fungisearch.fudriver.cycle.command.model;

import com.fungisearch.fudriver.cycle.Exception.CycleStartDayAfterFirstHarvestDayException;
import com.fungisearch.fudriver.cycle.command.repository.CycleRepository;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.Mycelium;
import com.fungisearch.fudriver.settings.command.model.Subsoil;
import com.fungisearch.fudriver.settings.command.repository.ChamberRepository;
import com.fungisearch.fudriver.settings.command.repository.DepotRepository;
import com.fungisearch.fudriver.settings.command.repository.MyceliumRepository;
import com.fungisearch.fudriver.settings.command.repository.SubsoilRepository;
import com.fungisearch.fudriver.testTools.UT.ChamberUTFactory;
import com.fungisearch.fudriver.testTools.UT.MyceliumUTFactory;
import com.fungisearch.fudriver.testTools.UT.SubsoilUTFactory;
import com.fungisearch.fudriver.testTools.UT.UserUTFactory;
import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.user.command.repository.UserRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.validation.HibernateBeanValidator;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CycleTest {

    @Mock
    private CycleRepository cycleRepository;

    @Mock
    //private BeanValidator beanValidator = new HibernateBeanValidator();
    private BeanValidator beanValidator;

    @Mock
    private DepotRepository depotRepository;
    @Mock
    private ChamberRepository chamberRepository;
    @Mock
    private MyceliumRepository myceliumRepository;
    @Mock
    private SubsoilRepository subsoilRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ZarobkiFactory zarobkiFactory;


    @Test
    public void shouldCreateCycle() {
        //Given
        User technolgist = new UserUTFactory(userRepository, beanValidator).create();


        Chamber chamber = new ChamberUTFactory(chamberRepository, depotRepository, beanValidator).create();
        Mycelium mycelium = new MyceliumUTFactory(myceliumRepository, beanValidator).create();
        Subsoil subsoil = new SubsoilUTFactory(subsoilRepository, beanValidator).create();
        //When
        Cycle cycle = new Cycle.CycleBuilder(cycleRepository, beanValidator, zarobkiFactory)
                .chamber(chamber)
                .mycelium(mycelium)
                .subsoil(subsoil)
                .area(10)
                .weight(123)
                .initDate(20170101)
                .humidity(20)
                .description("Description")
                .technologist(technolgist)
                .build();
        //Then
        assertEquals(cycle.getCycleStatus(), CycleStatusEnum.ACTIVE);
        assertEquals(cycle.getChamber(), chamber);
        assertEquals(cycle.getSubsoil(), subsoil);
        assertEquals(cycle.getMycelium(), mycelium);
        assertEquals(cycle.getWeight(), 123);
        assertEquals(cycle.getInitDate(), new Integer(20170101));
        assertEquals(cycle.getHumidity(), 20);
        assertEquals(cycle.getDescription(), "Description");
        assertEquals(cycle.getArea(), 10);
    }

    @Test
    public void shouldSetDefaultChamberAreaWhenNotSetInTheCommand() {
        //Given
        User technolgist = new UserUTFactory(userRepository, beanValidator).create();
        Chamber chamber = new ChamberUTFactory(chamberRepository, depotRepository, beanValidator).create(450);
        Mycelium mycelium = new MyceliumUTFactory(myceliumRepository, beanValidator).create();
        Subsoil subsoil = new SubsoilUTFactory(subsoilRepository, beanValidator).create();
        //When
        Cycle cycle = new Cycle.CycleBuilder(cycleRepository, beanValidator, zarobkiFactory)
                .chamber(chamber)
                .mycelium(mycelium)
                .subsoil(subsoil)
                .weight(123)
                .initDate(20170221)
                .humidity(21)
                .description("Description")
                .technologist(technolgist)
                .build();
        //Then
        assertEquals(cycle.getArea(), 450);
    }

    @Test
    public void shouldSetDefaultWeightIfNotSetInTheCommand() {
        //Given
        User technolgist = new UserUTFactory(userRepository, beanValidator).create();
        Chamber chamber = new ChamberUTFactory(chamberRepository, depotRepository, beanValidator).create(450);
        Mycelium mycelium = new MyceliumUTFactory(myceliumRepository, beanValidator).create();
        Subsoil subsoil = new SubsoilUTFactory(subsoilRepository, beanValidator).create();
        //When
        Cycle cycle = new Cycle.CycleBuilder(cycleRepository, beanValidator, zarobkiFactory)
                .chamber(chamber)
                .mycelium(mycelium)
                .subsoil(subsoil)
                .weight(123)
                .initDate(20130301)
                .description("Description")
                .technologist(technolgist)
                .build();
        //Then
        assertEquals(cycle.getHumidity(), 20);
    }

    @Test
    public void humidityShouldNotBeBelowZero() {
        //Given
        Cycle cycle = createCycle();
        //When & Then
        try {
            cycle.edit(new Cycle.Edit().humidity(-1));
            fail();
        } catch (IllegalStateException ex) {
            assertEquals("Humidity should be an integer between 0 and 100", ex.getMessage());
        }
    }

    @Test
    public void humidityShouldNotBeGreaterThan100() {
        //Given
        Cycle cycle = createCycle();
        //When & Then
        try {
            cycle.edit(new Cycle.Edit().humidity(101));
            fail();
        } catch (IllegalStateException ex) {
            assertEquals("Humidity should be an integer between 0 and 100", ex.getMessage());
        }
    }

    @Test
    public void shouldNotCloseCycleWhenNotStartThirdPeriodDate() {
        //Given
        Cycle cycle = createCycle();
        cycle.edit(new Cycle.Edit().startFirstPeriod(20170101));
        cycle.edit(new Cycle.Edit().startSecondPeriod(20170102));
        //When & Then
        try {
            cycle.close(new Cycle.Close().endDate(20170110).version(cycle.getVersion()));
            fail();
        } catch (IllegalStateException ex) {
            assertEquals("Specify start of third period date before closing the cycle.", ex.getMessage());
        }
    }

    @Test
    public void endCycleDateShouldBeEqualOrAfterStartThirdCycle() {
        //Given
        Cycle cycle = createCycle();
        cycle.edit(new Cycle.Edit().startFirstPeriod(20161231));
        cycle.edit(new Cycle.Edit().startSecondPeriod(20170101));
        cycle.edit(new Cycle.Edit().startThirdPeriod(20170102));
        //When & Then
        try {
            cycle.close(new Cycle.Close().endDate(20170101).version(cycle.getVersion()));
            fail();
        } catch (IllegalStateException ex) {
            assertEquals("Cycle end date should be equal or grater then start of third period date.", ex.getMessage());
        }
    }

    @Test
    public void shouldSetEndDateWhenClosingCycle() {
        //Given
        Cycle cycle = createCycle();
        cycle.edit(new Cycle.Edit().startFirstPeriod(20161231));
        cycle.edit(new Cycle.Edit().startSecondPeriod(20170101));
        cycle.edit(new Cycle.Edit().startThirdPeriod(20170102));
        cycle.setId(1L);
        when(zarobkiFactory.findMaxDateForCycle(any(Long.class))).thenReturn(20170102);
        //When
        cycle.close(new Cycle.Close().endDate(20170103).version(cycle.getVersion()));
        //then
        assertEquals(new Integer(20170103), cycle.getEnd());
    }

    @Test
    public void shouldSetCycleClosedStatus() {
        //Given
        Cycle cycle = createCycle();
        cycle.edit(new Cycle.Edit().startFirstPeriod(20161231));
        cycle.edit(new Cycle.Edit().startSecondPeriod(20170101));
        cycle.edit(new Cycle.Edit().startThirdPeriod(20170102));
        cycle.setId(1L);
        when(zarobkiFactory.findMaxDateForCycle(any(Long.class))).thenReturn(20170102);
        //When
        cycle.close(new Cycle.Close().endDate(20170103).version(cycle.getVersion()));
        //then
        assertEquals(cycle.getCycleStatus(), CycleStatusEnum.CLOSED);
    }

    @Test
    public void shouldNotSetStartDateBeforeFirstHarvest(){
        //Given
        Cycle cycle = createCycle();
        cycle.setId(1L);
        when(zarobkiFactory.findMinDateForCycle(any(Long.class))).thenReturn(20170102);
        //When && Then
        try {
            cycle.edit(new Cycle.Edit().initDate(20170103).version(cycle.getVersion()));
            fail();
        } catch (CycleStartDayAfterFirstHarvestDayException ex){
            assertEquals(20170102,ex.getFirstDate());
        }
    }


    private Cycle createCycle() {
        User technolgist = new UserUTFactory(userRepository, beanValidator).create();
        Chamber chamber = new ChamberUTFactory(chamberRepository, depotRepository, beanValidator).create(450);
        return new Cycle.CycleBuilder(cycleRepository, beanValidator, zarobkiFactory)
                .chamber(chamber)
                .weight(123)
                .initDate(20130301)
                .description("Description")
                .technologist(technolgist)
                .build();
    }


}
