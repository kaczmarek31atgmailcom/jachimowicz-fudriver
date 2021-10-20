package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.common.event.CustomApplicationEvent;
import com.fungisearch.fudriver.common.event.EventTypeEnum;
import com.fungisearch.fudriver.fileGenerator.eastMushrooms.EastMushroomsPaletteLabelService;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.EastMushroomsWarehouseRepository;
import com.fungisearch.fudriver.wozek.command.AddZarobki;
import com.fungisearch.fudriver.wozek.command.model.WozekEntry;
import com.fungisearch.fudriver.wozek.command.model.WozekEntryFactory;
import org.simpleframework.xml.Version;
import org.springframework.context.ApplicationEventPublisher;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "east_mushrooms_warehouse_palette")
public class WarehousePalette extends BaseEntity {

    transient EastMushroomsWarehouseRepository eastMushroomsWarehouseRepository;
    transient BeanValidator beanValidator;
    transient UserService userService;
    transient WozekEntryFactory wozekEntryFactory;
    transient ApplicationEventPublisher applicationEventPublisher;
    transient EastMushroomsPaletteLabelService eastMushroomsPaletteLabelService;
    transient TypeFactory typeFactory;
    transient AddZarobki addZarobki;
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "creator_id")
    @NotNull
    private Long creatorId;

    @Column(name = "creation_time")
    private Date creationTime;

    @Column(name = "depot_id")
    @NotNull
    private Long depotId;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private WarehousePaletteStatus status;

    @OneToMany
    @JoinColumn(name = "palette_id")
    private Set<WarehouseUnit> units;

    @Column(name = "palette_type_id")
    private Integer paletteTypeId;

    @Version
    private long version;

    public WarehousePalette(EastMushroomsWarehouseRepository warehouseRepository, BeanValidator beanValidator, UserService userService, WozekEntryFactory wozekEntryFactory, ApplicationEventPublisher applicationEventPublisher, EastMushroomsPaletteLabelService eastMushroomsPaletteLabelService, TypeFactory typeFactory, AddZarobki addZarobki) {
        this.eastMushroomsWarehouseRepository = warehouseRepository;
        this.beanValidator = beanValidator;
        this.userService = userService;
        this.wozekEntryFactory = wozekEntryFactory;
        this.applicationEventPublisher = applicationEventPublisher;
        this.eastMushroomsPaletteLabelService = eastMushroomsPaletteLabelService;
        this.typeFactory = typeFactory;
        this.addZarobki = addZarobki;
    }

    private WarehousePalette() {
    }

    public Long getId() {
        return id;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Long getDepotId() {
        return depotId;
    }

    public WarehousePaletteStatus getStatus() {
        return status;
    }

    public Set<WarehouseUnit> getUnits() {
        return units;
    }

    public Integer getPaletteTypeId() {
        return paletteTypeId;
    }

    private void save() {
        beanValidator.validate(this);
        eastMushroomsWarehouseRepository.save(this);
        CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EAST_WAREHOUSE_PALETTE_CREATED, this.id);
        applicationEventPublisher.publishEvent(event);
    }

    private void remove() {
        eastMushroomsWarehouseRepository.remove(this);
        CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EAST_WAREHOUSE_PALETTE_REMOVED, this.id);
        applicationEventPublisher.publishEvent(event);
    }

    public void assignHarvestPalettesAndAcceptToWarehouse(List<Long> harvestPaletteIds) {
        harvestPaletteIds.forEach(o -> wozekEntryFactory.getWozekEntriesOfPalleteId(o).stream().forEach(WozekEntry::askForAcceptance));
        harvestPaletteIds.forEach(o -> assignHarvestPalette(o, false));
        this.acceptToWarehouse();
    }


    public void assignHarvestPalette(long harvestPaletteId, boolean sentEvent) {
        List<WozekEntry> wozekEntries = wozekEntryFactory.getWozekEntriesOfPalleteId(harvestPaletteId);
        wozekEntries.forEach(WozekEntry::assignToWarehousePalette);
        wozekEntries.forEach(o -> new WarehouseUnit.WarehouseUnitBuilder(eastMushroomsWarehouseRepository, beanValidator, typeFactory)
                .cycleId(o.getCykleId())
                .harvestDate(o.getTime())
                .warehousePalette(this)
                .pickerId(o.getPickerId())
                .scannerManId(o.getWagowyId())
                .trolleyManId(o.getWozkowyId())
                .typeId(o.getRodzajId())
                .uniqId(o.getUniqId())
                .harvestPaletteId(o.getWozekNr())
                .build());
        if (sentEvent) {
            CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EAST_WAREHOUSE_PALETTE_ASSIGNED, this.id);
            applicationEventPublisher.publishEvent(event);
        }
    }

    public void unassignHarvestPalette(long harvestPaletteId, boolean sentEvent) {
        List<WozekEntry> wozekEntries = wozekEntryFactory.getWozekEntriesOfPalleteId(harvestPaletteId);
        wozekEntries.forEach(WozekEntry::askForAcceptance);
        List<WarehouseUnit> toRemove = this.units.stream().filter(o -> o.getHarvestPaletteId() == harvestPaletteId).collect(Collectors.toList());
        toRemove.forEach(o -> {
            o.warehouseRepository = eastMushroomsWarehouseRepository;
            o.remove();
        });
        if (sentEvent) {
            CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EAST_WAREHOUSE_PALETTE_ASSIGNED, this.id);
            applicationEventPublisher.publishEvent(event);
        }
    }

    public void acceptToWarehouse() {
        this.units.forEach(WarehouseUnit::acceptToWarehouse);
        this.units.forEach(addZarobki::addZarobkiWarehouseUnit);
        this.units.forEach(o -> wozekEntryFactory.findByPickerAndUniq(o.getPickerId(),o.getUniqId()).delete());
        this.status = WarehousePaletteStatus.ON_STOCK;
        CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EAST_WAREHOUSE_PALETTE_ACCEPTED_TO_WAREHOUSE, this.id);
        applicationEventPublisher.publishEvent(event);

        eastMushroomsPaletteLabelService.createLabel(this.getId());
    }

    public void release() {
        if (!this.status.equals(WarehousePaletteStatus.ON_STOCK)) {
            throw new IllegalStateException("Release palette that is not on stock");
        }
        this.units.forEach(WarehouseUnit::release);
        this.status = WarehousePaletteStatus.RELEASED;
    }

    public void changePaletteType(int paletteTypeId){
        if (this.status.equals(WarehousePaletteStatus.RELEASED)) {
            throw new IllegalStateException("Change palete type that is already released");
        }
        this.paletteTypeId = paletteTypeId;
        CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EAST_WAREHOUSE_PALETTE_TYPE_CHANGED, id);
        applicationEventPublisher.publishEvent(event);
    }

    public void removeUnit(WarehouseUnit unit) {
        this.units.remove(unit);
    }

    public void moveUnitsToOtherPalette(long targetPaletteId) {
        if (this.status != WarehousePaletteStatus.ON_STOCK) {
            throw new IllegalStateException("Join warehouse palettes that are not on stock");
        }
        for(WarehouseUnit warehouseUnit: this.units){
            addServicesAndMoveUnitToAnotherPalette(warehouseUnit, targetPaletteId);
        }
        long id = this.id;
        this.remove();
        eastMushroomsPaletteLabelService.createLabel(targetPaletteId);
        CustomApplicationEvent event = new CustomApplicationEvent(EventTypeEnum.EAST_WAREHOUSE_PALETTE_REMOVED, id);
        applicationEventPublisher.publishEvent(event);
    }

    private void addServicesAndMoveUnitToAnotherPalette(WarehouseUnit warehouseUnit, long targetPaletteId) {
        warehouseUnit.warehouseRepository = this.eastMushroomsWarehouseRepository;
        warehouseUnit.typeFactory = this.typeFactory;
        warehouseUnit.beanValidator = this.beanValidator;
        warehouseUnit.moveToAnotherWarehousePalette(targetPaletteId);
    }

    public static class WarehousePaletteBuilder {
        private final EastMushroomsWarehouseRepository warehouseRepository;
        private final BeanValidator beanValidator;
        private final UserService userService;
        private final ApplicationEventPublisher applicationEventPublisher;
        private final WozekEntryFactory wozekEntryFactory;
        private final EastMushroomsPaletteLabelService eastMushroomsPaletteLabelService;
        private final TypeFactory typeFactory;
        private final AddZarobki addZarobki;
        private long depotId;
        private int paletteTypeId;

        public WarehousePaletteBuilder(EastMushroomsWarehouseRepository warehouseRepository, BeanValidator beanValidator, UserService userService, ApplicationEventPublisher applicationEventPublisher, WozekEntryFactory wozekEntryFactory, EastMushroomsPaletteLabelService eastMushroomsPaletteLabelService, TypeFactory typeFactory, AddZarobki addZarobki) {
            this.warehouseRepository = warehouseRepository;
            this.beanValidator = beanValidator;
            this.userService = userService;
            this.applicationEventPublisher = applicationEventPublisher;
            this.wozekEntryFactory = wozekEntryFactory;
            this.eastMushroomsPaletteLabelService = eastMushroomsPaletteLabelService;
            this.typeFactory = typeFactory;
            this.addZarobki = addZarobki;
        }

        public WarehousePaletteBuilder depotId(long depotId) {
            this.depotId = depotId;
            return this;
        }

        public WarehousePaletteBuilder paletteTypeId(int paletteTypeId){
            this.paletteTypeId = paletteTypeId;
            return this;
        }

        public WarehousePalette build() {
            WarehousePalette warehousePalette = new WarehousePalette(warehouseRepository, beanValidator, userService, wozekEntryFactory, applicationEventPublisher, eastMushroomsPaletteLabelService, typeFactory, addZarobki);
            warehousePalette.creatorId = userService.getCurrentUserId();
            warehousePalette.status = WarehousePaletteStatus.CREATED;
            warehousePalette.depotId = depotId;
            warehousePalette.paletteTypeId = paletteTypeId;
            warehousePalette.creationTime = new Date();
            warehousePalette.units = new HashSet<>();
            warehousePalette.save();
            return warehousePalette;
        }
    }
}
