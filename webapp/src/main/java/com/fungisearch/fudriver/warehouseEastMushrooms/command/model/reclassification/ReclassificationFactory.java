package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.reclassification;

import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehouseUnitFactory;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.EastMushroomsWarehouseRepository;
import org.springframework.stereotype.Service;

@Service
public class ReclassificationFactory {

    private final EastMushroomsWarehouseRepository warehouseRepository;
    private final BeanValidator beanValidator;
    private final UserService userService;
    private final TypeFactory typeFactory;
    private final WarehouseUnitFactory warehouseUnitFactory;

    public ReclassificationFactory(EastMushroomsWarehouseRepository warehouseRepository, BeanValidator beanValidator, UserService userService, TypeFactory typeFactory, WarehouseUnitFactory warehouseUnitFactory) {
        this.warehouseRepository = warehouseRepository;
        this.beanValidator = beanValidator;
        this.userService = userService;
        this.typeFactory = typeFactory;
        this.warehouseUnitFactory = warehouseUnitFactory;
    }

    public ReclassificationHeader.ReclassificationHeaderBuilder getHeaderBuilder(){
        return new ReclassificationHeader.ReclassificationHeaderBuilder(warehouseRepository,beanValidator,userService,warehouseUnitFactory);
    }
}
