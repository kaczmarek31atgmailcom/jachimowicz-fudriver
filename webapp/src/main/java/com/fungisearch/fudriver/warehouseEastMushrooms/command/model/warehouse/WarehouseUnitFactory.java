package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse;

import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.EastMushroomsWarehouseRepository;
import org.springframework.stereotype.Service;

@Service
public class WarehouseUnitFactory {
    private final EastMushroomsWarehouseRepository warehouseRepository;
    private final BeanValidator beanValidator;
    private final TypeFactory typeFactory;


    public WarehouseUnitFactory(EastMushroomsWarehouseRepository warehouseRepository, BeanValidator beanValidator, TypeFactory typeFactory) {
        this.warehouseRepository = warehouseRepository;
        this.beanValidator = beanValidator;
        this.typeFactory = typeFactory;

    }

    public WarehouseUnit find(long id){
        WarehouseUnit warehouseUnit = warehouseRepository.findWarehouseUnit(id);
        if(warehouseUnit != null){
            warehouseUnit.warehouseRepository = warehouseRepository;
            warehouseUnit.beanValidator = beanValidator;
            warehouseUnit.typeFactory = typeFactory;

        }
        return warehouseUnit;
    }

    public WarehouseUnit findByUniqAndPicker(long uniqId, long pickerId) {
        WarehouseUnit warehouseUnit = warehouseRepository.findByUniqAndPicker(uniqId,pickerId);
        if(warehouseUnit != null){
            warehouseUnit.warehouseRepository = warehouseRepository;
            warehouseUnit.beanValidator = beanValidator;
            warehouseUnit.typeFactory = typeFactory;
        }
        return warehouseUnit;
    }
}
