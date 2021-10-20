package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse;

import com.fungisearch.fudriver.fileGenerator.eastMushrooms.EastMushroomsPaletteLabelService;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.EastMushroomsWarehouseRepository;
import com.fungisearch.fudriver.wozek.command.AddZarobki;
import com.fungisearch.fudriver.wozek.command.model.WozekEntryFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class WarehousePaletteFactory {
    private final EastMushroomsWarehouseRepository warehouseRepository;
    private final UserService userService;
    private final BeanValidator beanValidator;
    private final WozekEntryFactory wozekEntryFactory;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EastMushroomsPaletteLabelService eastMushroomsPaletteLabelService;
    private final TypeFactory typeFactory;
    private final AddZarobki addZarobki;


    public WarehousePaletteFactory(EastMushroomsWarehouseRepository warehouseRepository, UserService userService, BeanValidator beanValidator, WozekEntryFactory wozekEntryFactory, ApplicationEventPublisher applicationEventPublisher, EastMushroomsPaletteLabelService eastMushroomsPaletteLabelService, TypeFactory typeFactory, AddZarobki addZarobki) {
        this.warehouseRepository = warehouseRepository;
        this.userService = userService;
        this.beanValidator = beanValidator;
        this.wozekEntryFactory = wozekEntryFactory;
        this.applicationEventPublisher = applicationEventPublisher;
        this.eastMushroomsPaletteLabelService = eastMushroomsPaletteLabelService;
        this.typeFactory = typeFactory;
        this.addZarobki = addZarobki;
    }

    public WarehousePalette.WarehousePaletteBuilder getBuilder() {
        return new WarehousePalette.WarehousePaletteBuilder(warehouseRepository, beanValidator, userService, applicationEventPublisher, wozekEntryFactory, eastMushroomsPaletteLabelService, typeFactory, addZarobki);
    }

    public WarehousePalette find(Long paletteId) {
        WarehousePalette warehousePalette = warehouseRepository.findWarehousePalette(paletteId);
        if (warehousePalette != null) {
            warehousePalette.eastMushroomsWarehouseRepository = warehouseRepository;
            warehousePalette.beanValidator = beanValidator;
            warehousePalette.applicationEventPublisher = applicationEventPublisher;
            warehousePalette.userService = userService;
            warehousePalette.wozekEntryFactory = wozekEntryFactory;
            warehousePalette.eastMushroomsPaletteLabelService = eastMushroomsPaletteLabelService;
            warehousePalette.typeFactory = typeFactory;
            warehousePalette.addZarobki = addZarobki;
        }
        return warehousePalette;
    }

    public WarehousePalette findWarehousePaletteByHarvestPaletteId(long harvestPaletteId) {
        WarehousePalette warehousePalette = warehouseRepository.findWarehousePaletteByHarvestPaletteId(harvestPaletteId);
        if (warehousePalette != null) {
            warehousePalette.eastMushroomsWarehouseRepository = warehouseRepository;
            warehousePalette.beanValidator = beanValidator;
            warehousePalette.applicationEventPublisher = applicationEventPublisher;
            warehousePalette.userService = userService;
            warehousePalette.wozekEntryFactory = wozekEntryFactory;
            warehousePalette.eastMushroomsPaletteLabelService = eastMushroomsPaletteLabelService;
            warehousePalette.typeFactory = typeFactory;
            warehousePalette.addZarobki = addZarobki;
        }
        return warehousePalette;
    }

    public WarehousePalette findWarehousePalette(long warehousePaletteId) {
        WarehousePalette warehousePalette = warehouseRepository.findWarehousePalette(warehousePaletteId);
        if (warehousePalette != null) {
            warehousePalette.eastMushroomsWarehouseRepository = warehouseRepository;
            warehousePalette.wozekEntryFactory = wozekEntryFactory;
            warehousePalette.userService = userService;
            warehousePalette.applicationEventPublisher = applicationEventPublisher;
            warehousePalette.beanValidator = beanValidator;
            warehousePalette.eastMushroomsPaletteLabelService = eastMushroomsPaletteLabelService;
            warehousePalette.typeFactory = typeFactory;
            warehousePalette.addZarobki = addZarobki;
        }
        return warehousePalette;
    }
}
