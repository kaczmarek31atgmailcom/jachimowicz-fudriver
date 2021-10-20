package com.fungisearch.fudriver.settings.command.chamberCommands;

import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import com.fungisearch.fudriver.settings.command.model.Depot;
import com.fungisearch.fudriver.settings.command.model.DepotFactory;
import com.fungisearch.fudriver.settings.command.repository.ChamberRepository;
import com.fungisearch.fudriver.settings.command.repository.DepotRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DeleteChamberCommandHandlerTest {

    @Mock
    private ChamberRepository chamberRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private ChamberFactory chamberFactory;
    @Mock
    private DepotFactory depotFactory;
    @Mock
    private DepotRepository depotRepository;

    @InjectMocks
    private DeleteChamberCommandHandler handler;


    @Test
    public void shouldInactivateChamber(){
        //Given
        Depot depot = new Depot.DepotBuilder(depotRepository,beanValidator).name("test_depot").build();
        Chamber chamber = new Chamber.ChamberBuilder(chamberRepository,beanValidator).depot(depot).name("test_chamber").area(123).build();
        //When
        when(depotFactory.find(any(Long.class))).thenReturn(depot);
        when(chamberFactory.find(any(Long.class))).thenReturn(chamber);
        //When
        handler.handle(1L);
        //Then
        Assert.assertFalse(chamber.isActive());
    }
}