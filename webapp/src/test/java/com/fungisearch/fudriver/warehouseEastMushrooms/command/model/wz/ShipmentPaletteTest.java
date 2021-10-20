package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz;

import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkup;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupFactory;
import com.fungisearch.fudriver.reclassification.command.repository.RodzajSkupRepository;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.WzRepository;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ShipmentPaletteTest {
    public ShipmentPaletteTest() {
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private WzRepository wzRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private UserFactory userFactory;
    @Mock
    private CycleFactory cycleFactory;
    @Mock
    private PersonFactory personFactory;
    @Mock
    private TypeFactory typeFactory;
    @Mock
    private RodzajSkupFactory rodzajSkupFactory;
    @Mock
    private RodzajSkupRepository rodzajSkupRepository;

    @InjectMocks
    ShipmentPalette.ShipmentPaletteBuilder builder;




    private RodzajSkup createRodzajSkup(long localId,long remoteId,String name, double weight){
        RodzajSkup rodzajSkup = new RodzajSkup(rodzajSkupRepository);
        rodzajSkup.setActive(true);
        rodzajSkup.setRemoteId(remoteId);
        rodzajSkup.setName(name);
        rodzajSkup.setWeight(weight);
        return rodzajSkup;
    }

}
