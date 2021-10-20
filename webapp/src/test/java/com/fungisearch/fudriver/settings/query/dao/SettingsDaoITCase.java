package com.fungisearch.fudriver.settings.query.dao;

import com.fungisearch.fudriver.settings.command.model.*;
import com.fungisearch.fudriver.settings.query.dto.ChamberDto;
import com.fungisearch.fudriver.settings.query.dto.DepotDto;
import com.fungisearch.fudriver.settings.query.dto.MyceliumDto;
import com.fungisearch.fudriver.settings.query.dto.SubsoilDto;
import com.fungisearch.fudriver.testTools.DepotITestFactory;
import com.fungisearch.fudriver.testTools.FlushDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;


@ContextConfiguration(locations = {"/test-spring.xml"})
public class SettingsDaoITCase extends AbstractTransactionalJUnit4SpringContextTests {


    @Autowired
    private FlushDao flushDao;
    @Autowired
    private SettingsDao settingsDao;
    @Autowired
    private DepotITestFactory depotITestFactory;
    @Autowired
    private ChamberFactory chamberFactory;
    @Autowired
    private DepotFactory depotFactory;
    @Autowired
    private SubsoilFactory subsoilFactory;
    @Autowired
    private MyceliumFactory myceliumFactory;

    @Test
    public void shouldReturnActiveDepots() {
        //Given
        Depot depot1 = depotITestFactory.create("depot1");
        depotITestFactory.create("inactiveDepot").deactivate();
        Depot depot2 = depotITestFactory.create("depot2");
        flushDao.flush();
        //When
        List<DepotDto> list = new ArrayList(settingsDao.getActiveDepots());
        Collections.sort(list, (o1, o2) -> o1.name.compareTo(o2.name));
        //Then
        assertEquals(list.size(), 2);
        assertEquals((Long) list.get(0).id, depot1.getId());
        assertEquals(list.get(0).name, depot1.getName());
        assertEquals((Long) list.get(1).id, depot2.getId());
        assertEquals(list.get(1).name, depot2.getName());
    }

    @Test
    public void shouldRetrieveActiveChambers() {
        //Given
        Depot depot1 = depotFactory.getBuilder().name("depot1").build();
        Depot depot2 = depotFactory.getBuilder().name("depot2").build();
        Chamber chamber1 = chamberFactory.getBuilder().name("chamber1").depot(depot1).area(101).build();
        Chamber chamber2 = chamberFactory.getBuilder().name("chamber2").depot(depot1).area(102).build();
        Chamber chamber3 = chamberFactory.getBuilder().name("chamber3").depot(depot2).area(103).build();
        Chamber chamber4 = chamberFactory.getBuilder().name("chamber4").depot(depot2).area(104).build();
        chamber3.inactivate();
        flushDao.flush();
        //When
        Set<ChamberDto> set = settingsDao.getActiveChambers();
        List<ChamberDto> tested = new ArrayList<>(settingsDao.getActiveChambers());
        Collections.sort(tested, (o1, o2) -> o1.name.compareTo(o2.name));
        //Then
        assertEquals(tested.size(), 3);
        assertEquals(tested.get(0).id,Math.toIntExact(chamber1.getId()));
        assertEquals(tested.get(0).name,chamber1.getName());
        assertEquals(tested.get(0).area, 101);
        assertEquals(tested.get(0).depotId, Math.toIntExact(depot1.getId()));
        assertEquals(tested.get(0).depotName, depot1.getName());

        assertEquals(tested.get(1).id,Math.toIntExact(chamber2.getId()));
        assertEquals(tested.get(1).name,chamber2.getName());
        assertEquals(tested.get(1).area, 102);
        assertEquals(tested.get(1).depotId, Math.toIntExact(depot1.getId()));
        assertEquals(tested.get(1).depotName, depot1.getName());

        assertEquals(tested.get(2).id,Math.toIntExact(chamber4.getId()));
        assertEquals(tested.get(2).name,chamber4.getName());
        assertEquals(tested.get(2).area, 104);
        assertEquals(tested.get(2).depotId, Math.toIntExact(depot2.getId()));
        assertEquals(tested.get(2).depotName, depot2.getName());
    }

    @Test
    public void shouldGetActiveSubsoils(){
        //Given
        Subsoil subsoil1 = subsoilFactory.getBuilder().name("subsoil1").build();
        Subsoil subsoil2 = subsoilFactory.getBuilder().name("subsoil2").build();
        Subsoil subsoil3 = subsoilFactory.getBuilder().name("subsoil3").build();
        Subsoil subsoil4 = subsoilFactory.getBuilder().name("subsoil4").build();
        subsoil3.inactivate();
        flushDao.flush();
        //When
        List<SubsoilDto> tested = new ArrayList<>(settingsDao.getActiveSubsoils());
        Collections.sort(tested, (o1,o2) -> o1.name.compareTo(o2.name));
        //Then
        assertEquals(tested.size(),3);
        assertEquals((Long)tested.get(0).id, subsoil1.getId());
        assertEquals(tested.get(0).name, subsoil1.getName());

        assertEquals((Long)tested.get(1).id, subsoil2.getId());
        assertEquals(tested.get(1).name, subsoil2.getName());

        assertEquals((Long)tested.get(2).id, subsoil4.getId());
        assertEquals(tested.get(2).name, subsoil4.getName());

    }

    @Test
    public void shouldGetActiveMyceliums(){
        //Given
        Mycelium mycelium0 = myceliumFactory.getBuilder().name("mycelium0").build();
        Mycelium mycelium1 = myceliumFactory.getBuilder().name("mycelium1").build();
        Mycelium mycelium2 = myceliumFactory.getBuilder().name("mycelium2").build();
        Mycelium mycelium3 = myceliumFactory.getBuilder().name("mycelium3").build();
        mycelium2.remove();
        flushDao.flush();
        //When
        List<MyceliumDto> tested = new ArrayList(settingsDao.getActiveMyceliums());
        tested.sort((m1,m2) -> m1.name.compareTo(m2.name));
        //Then
        assertEquals(tested.size(),3);
        assertEquals((Long)tested.get(0).id,mycelium0.getId());
        assertEquals(tested.get(0).name,mycelium0.getName());
        assertEquals((Long)tested.get(1).id,mycelium1.getId());
        assertEquals(tested.get(1).name,mycelium1.getName());
        assertEquals((Long)tested.get(2).id,mycelium3.getId());
        assertEquals(tested.get(2).name,mycelium3.getName());
    }
}
