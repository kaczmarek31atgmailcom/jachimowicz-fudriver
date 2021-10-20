package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz;

import com.fungisearch.fudriver.customer.command.model.CustomerFactory;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupFactory;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.WzRepository;
import com.fungisearch.fudriver.warehouseEastMushrooms.web.ProxyService;
import org.springframework.stereotype.Service;

@Service
public class ShipmentFactory {

    private final WzRepository wzRepository;
    private final UserService userService;
    private final WarehousePaletteFactory  warehousePaletteFactory;
    private final BeanValidator beanValidator;
    private final UserFactory userFactory;
    private final CycleFactory cycleFactory;
    private final PersonFactory personFactory;
    private final TypeFactory typeFactory;
    private final RodzajSkupFactory rodzajSkupFactory;
    private final CustomerFactory customerFactory;
    private final ProxyService proxyService;

    public ShipmentFactory(WzRepository wzRepository, UserService userService, WarehousePaletteFactory warehousePaletteFactory, BeanValidator beanValidator, UserFactory userFactory, CycleFactory cycleFactory, PersonFactory personFactory, TypeFactory typeFactory, RodzajSkupFactory rodzajSkupFactory, CustomerFactory customerFactory, ProxyService proxyService) {
        this.wzRepository = wzRepository;
        this.userService = userService;
        this.warehousePaletteFactory = warehousePaletteFactory;
        this.beanValidator = beanValidator;
        this.userFactory = userFactory;
        this.cycleFactory = cycleFactory;
        this.personFactory = personFactory;
        this.typeFactory = typeFactory;
        this.rodzajSkupFactory = rodzajSkupFactory;
        this.customerFactory = customerFactory;
        this.proxyService = proxyService;
    }


    public Shipment.ShipmentBuilder getBuilder(){
        return new Shipment.ShipmentBuilder(wzRepository,userService,warehousePaletteFactory,beanValidator,userFactory,cycleFactory,personFactory,typeFactory,rodzajSkupFactory,customerFactory, proxyService);
    }


}
