package com.fungisearch.fudriver.settings.command.chamberCommands;

import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import com.fungisearch.fudriver.settings.command.model.Depot;
import com.fungisearch.fudriver.settings.command.model.DepotFactory;
import com.fungisearch.fudriver.settings.command.repository.ChamberRepository;
import com.fungisearch.fudriver.settings.command.repository.DepotRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UpdateChamberDepotCommandHandlerTest {

    @Mock
    private ChamberRepository chamberRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private DepotRepository depotRepository;
    @Mock
    private DepotFactory depotFactory;
    @Mock
    private ChamberFactory chamberFactory;

    @InjectMocks
    private UpdateChamberDepotCommandHandler handler;

    @Test
    public void shouldUpdateChamberDepot(){
        //Given
        Depot depot1 = new Depot.DepotBuilder(depotRepository,beanValidator).name("depot_1").build();
        Depot depot2 = new Depot.DepotBuilder(depotRepository,beanValidator).name("depot_2").build();
        Chamber chamber = new Chamber.ChamberBuilder(chamberRepository,beanValidator)
                .area(100)
                .name("some_name")
                .depot(depot1)
                .build();
        when(chamberFactory.find(any(Long.class))).thenReturn(chamber);
        when(depotFactory.find(1L)).thenReturn(depot1);
        when(depotFactory.find(2L)).thenReturn(depot2);
        UpdateChamberDepotCommand command = new UpdateChamberDepotCommand();
        command.id = 1L;
        command.depotId = 2L;
        //When
        handler.handle(command);
        //Then
        assertEquals(depot2,chamber.getDepot());
    }
}