package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz;

import com.fungisearch.fudriver.customer.command.model.CustomerFactory;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.cycle.command.repository.CycleRepository;
import com.fungisearch.fudriver.fileGenerator.eastMushrooms.EastMushroomsPaletteLabelService;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupFactory;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.ChamberFactory;
import com.fungisearch.fudriver.settings.command.model.Company;
import com.fungisearch.fudriver.settings.command.model.Depot;
import com.fungisearch.fudriver.settings.command.repository.ChamberRepository;
import com.fungisearch.fudriver.settings.command.repository.DepotRepository;
import com.fungisearch.fudriver.settings.command.repository.SettingsRepository;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePalette;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehouseUnit;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.EastMushroomsWarehouseRepository;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.WzRepository;
import com.fungisearch.fudriver.warehouseEastMushrooms.web.ProxyService;
import com.fungisearch.fudriver.wozek.command.AddZarobki;
import com.fungisearch.fudriver.wozek.command.model.WozekEntryFactory;
import com.fungisearch.fudriver.zarobki.command.model.ZarobkiFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ShipmentTest {

    @Mock
    private EastMushroomsWarehouseRepository warehouseRepository;
    @Mock
    private BeanValidator beanValidator;
    @Mock
    private UserService userService;
    @Mock
    private WozekEntryFactory wozekEntryFactory;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private EastMushroomsPaletteLabelService eastMushroomsPaletteLabelService;
    @Mock
    private TypeFactory typeFactory;
    @Mock
    private AddZarobki addZarobki;
    @Mock
    private SettingsRepository settingsRepository;
    @Mock
    private ChamberRepository chamberRepository;
    @Mock
    private CycleRepository cycleRepository;
    @Mock
    private ZarobkiFactory zarobkiFactory;
    @Mock
    private DepotRepository depotRepository;
    @Mock
    private CycleFactory cycleFactory;
    @Mock
    private ChamberFactory chamberFactory;
    @Mock
    private WzRepository wzRepository;
    @Mock
    private WarehousePaletteFactory warehousePaletteFactory;
    @Mock
    private UserFactory userFactory;
    @Mock
    private PersonFactory personFactory;
    @Mock
    private RodzajSkupFactory rodzajSkupFactory;
    @Mock
    private CustomerFactory customerFactory;
    @Mock
    private ProxyService proxyService;

    //@InjectMocks
    //private Shipment.ShipmentBuilder shipmentBuilder;


    @Test
    public void shouldFindUniqueCompanies(){
        //Given
        List<WarehousePalette> palettes = createPalettes();
        List<Chamber> chambers = getChambers();
        List<Cycle> cycles = getCycles(chambers);

        when(cycleFactory.find(1L)).thenReturn(cycles.get(0));
        when(cycleFactory.find(2L)).thenReturn(cycles.get(1));
        Shipment.ShipmentBuilder shipmentBuilder = new Shipment.ShipmentBuilder(wzRepository,userService,warehousePaletteFactory,beanValidator,userFactory,cycleFactory,personFactory,typeFactory,rodzajSkupFactory,customerFactory, proxyService);

        //When
        List<Company> companies = shipmentBuilder.findUniqueCompanies(palettes);
        //Then
        Collections.sort(companies, (o1, o2) -> o1.getId().compareTo(o2.getId()));
        Assert.assertEquals(companies.get(0).getName(),"company1");
        Assert.assertEquals(companies.get(1).getName(),"company2");

    }


    private List<WarehousePalette> createPalettes(){
        List<WarehousePalette> palettes = new ArrayList<>();
        WarehousePalette warehousePalette1 = new WarehousePalette.WarehousePaletteBuilder(warehouseRepository,beanValidator,userService,applicationEventPublisher,wozekEntryFactory,eastMushroomsPaletteLabelService,typeFactory,addZarobki)
                .depotId(1L)
                .build();
        WarehouseUnit unit1 = new WarehouseUnit.WarehouseUnitBuilder(warehouseRepository,beanValidator,typeFactory)
                .cycleId(1)
                .warehousePalette(warehousePalette1)
                .build();
        WarehouseUnit unit2 = new WarehouseUnit.WarehouseUnitBuilder(warehouseRepository,beanValidator,typeFactory)
                .cycleId(2)
                .warehousePalette(warehousePalette1)
                .build();
        WarehouseUnit unit3 = new WarehouseUnit.WarehouseUnitBuilder(warehouseRepository,beanValidator,typeFactory)
                .cycleId(3)
                .warehousePalette(warehousePalette1)
                .build();
        warehousePalette1.getUnits().add(unit1);
        warehousePalette1.getUnits().add(unit2);
        warehousePalette1.getUnits().add(unit3);
        palettes.add(warehousePalette1);
        return palettes;
    }

    private List<Chamber> getChambers() {
        Depot depot = new Depot.DepotBuilder(depotRepository,beanValidator)
        .name("depot1")
        .build();
        Company company1 = new Company.CompanyBuilder(settingsRepository, beanValidator)
                .name("company1")
                .build();
        company1.setId(new Long(1));
        Company company2 = new Company.CompanyBuilder(settingsRepository, beanValidator)
                .name("company2")
                .build();
        company2.setId(new Long(2));
        Chamber chamber1 = new Chamber.ChamberBuilder(chamberRepository, beanValidator)
                .name("Chamber1")
                .depot(depot)
                .company(company1)
                .build();
        chamber1.setId(1L);
        Chamber chamber2 = new Chamber.ChamberBuilder(chamberRepository, beanValidator)
                .name("Chamber2")
                .depot(depot)
                .company(company2)
                .build();
        chamber2.setId(2L);
        Chamber chamber3 = new Chamber.ChamberBuilder(chamberRepository, beanValidator)
                .name("Chamber3")
                .depot(depot)
                .company(company1)
                .build();
        chamber3.setId(3L);
        List<Chamber> chambers = new ArrayList<>();
        chambers.add(chamber1);
        chambers.add(chamber2);
        chambers.add(chamber3);
        return chambers;
    }


        private List<Cycle> getCycles(List<Chamber> chambers){
        Cycle cycle1 = new Cycle.CycleBuilder(cycleRepository,beanValidator,zarobkiFactory)
                .chamber(chambers.get(0))
                .build();
        cycle1.setId(1L);
        Cycle cycle2 = new Cycle.CycleBuilder(cycleRepository,beanValidator,zarobkiFactory)
                .chamber(chambers.get(1))
                .build();
        cycle2.setId(2L);
        Cycle cycle3 = new Cycle.CycleBuilder(cycleRepository,beanValidator,zarobkiFactory)
                .chamber(chambers.get(2))
                .build();
        cycle3.setId(3L);
         List<Cycle> cycles = new ArrayList<>();
         cycles.add(cycle1);
         cycles.add(cycle2);
         cycles.add(cycle3);
         return cycles;
    }
}
