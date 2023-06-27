package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.reclassification.ReclassificationDetail;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.reclassification.ReclassificationHeader;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.EastMushroomsWarehouseRepository;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "east_mushrooms_warehouse_unit")
public class WarehouseUnit extends BaseEntity {

    transient EastMushroomsWarehouseRepository warehouseRepository;
    transient BeanValidator beanValidator;
    transient TypeFactory typeFactory;
    transient Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "palette_id")
    private WarehousePalette warehousePalette;

    @Column(name = "picker_id")
    @NotNull
    private Long pickerId;

    @Column(name = "local_type_id")
    @NotNull
    private Long localTypeId;

    @Column(name = "remote_type_id")
    private Long remoteTypeId;

    @Column(name = "uniq_id")
    @NotNull
    private Long uniqId;

    @Column(name = "harvest_time")
    @NotNull
    private Date harvestTime;

    @Column(name = "scanner_man_id")
    @NotNull
    private Long scannerManId;

    @Column(name = "trolley_man_id")
    @NotNull
    private Integer trolleyManId;

    @Column(name = "harvest_palette_id")
    @NotNull
    private long harvestPaletteId;

    @Column(name = "cycle_id")
    @NotNull
    private Long cycleId;

    @Column(name = "status")
    @Enumerated(EnumType.ORDINAL)
    @NotNull
    private WarehouseUnitStatus status;

    @Version
    private long version;

    private WarehouseUnit() {
    }

    public WarehouseUnit(EastMushroomsWarehouseRepository warehouseRepository, BeanValidator beanValidator, TypeFactory typeFactory) {
        this.warehouseRepository = warehouseRepository;
        this.beanValidator = beanValidator;
        this.typeFactory = typeFactory;
    }

    public Long getId() {
        return id;
    }

    public WarehousePalette getWarehousePalette() {
        return warehousePalette;
    }

    public Long getPickerId() {
        return pickerId;
    }

    public Long getLocalTypeId() {
        return localTypeId;
    }

    public Long getRemoteTypeId() {
        return remoteTypeId;
    }

    public Long getUniqId() {
        return uniqId;
    }

    public Date getHarvestTime() {
        return harvestTime;
    }

    public Long getScannerManId() {
        return scannerManId;
    }

    public Long getCycleId() {
        return cycleId;
    }

    public WarehouseUnitStatus getStatus() {
        return status;
    }

    public Integer getTrolleyManId() {
        return trolleyManId;
    }

    public long getHarvestPaletteId() {
        return harvestPaletteId;
    }

    public void remove() {
        warehouseRepository.removeUnit(this);
    }

    private void save() {
        beanValidator.validate(this);
        warehouseRepository.save(this);
    }

    public void acceptToWarehouse() {
        this.status = WarehouseUnitStatus.ON_STOCK;
    }

    public void release() {
        if (this.status.equals(WarehouseUnitStatus.RELEASED)) {
            throw new IllegalStateException("Release warehouse unit that is not on stock");
        }
        this.status = WarehouseUnitStatus.RELEASED;
    }

    public void reclassify(long newTypeId, ReclassificationHeader reclassificationHeader) {
        if (!this.status.equals(WarehouseUnitStatus.ON_STOCK)) {
            throw new IllegalStateException("Reclassify warehouse unit that is not on stock");
        }

        Type targetType = typeFactory.findById(newTypeId);
        Type localType = typeFactory.findById(this.getLocalTypeId());
        new ReclassificationDetail.ReclassificationDetailBuilder(warehouseRepository, beanValidator)
                .harvestDate(this.harvestTime)
                .pickerId(this.pickerId)
                .uniqId(this.uniqId)
                .header(reclassificationHeader)
                .sourceTypeId(this.localTypeId)
                .sourceTypeName(localType.getName())
                .sourceTypeWeight(localType.getWeight())
                .targetTypeId(targetType.getId())
                .targetTypeName(targetType.getName())
                .targetTypeWeight(targetType.getWeight())
                .build();
        this.localTypeId = targetType.getId();
        this.remoteTypeId = null;
    }

    public void reclassify(long newTypeId) {
        if (!this.status.equals(WarehouseUnitStatus.ON_STOCK)) {
            throw new IllegalStateException("Reclassify warehouse unit that is not on stock");
        }

        this.localTypeId = newTypeId;
    }


    public void moveToAnotherWarehousePalette(long paletteId){
        if(this.status != WarehouseUnitStatus.ON_STOCK){
            throw new IllegalStateException("Moving to other palette warehouseUnit that is not on the stock.");
        }
        WarehousePalette warehousePalette = warehouseRepository.findWarehousePalette(paletteId);
        if(warehousePalette == null){
            throw new IllegalStateException("Moving to warehouseUnit to palette that does not exist.");
        }
        if(warehousePalette.getStatus() != WarehousePaletteStatus.ON_STOCK){
            throw new IllegalStateException("Moving to warehouseUnit to palette that is not on stock.");
        }
        //this.warehousePalette.removeUnit(this);
        this.warehousePalette = warehousePalette;
        this.warehousePalette.getUnits().add(this);
        beanValidator.validate(this);
    }

    public static class WarehouseUnitBuilder {
        private final EastMushroomsWarehouseRepository warehouseRepository;
        private final BeanValidator beanValidator;
        private final TypeFactory typeFactory;
        private WarehousePalette warehousePalette;
        private long pickerId;
        private long typeId;
        private long uniqId;
        private Date harvestTime;
        private long scannerManId;
        private Integer trolleyManId;
        private long cycleId;
        private long harvestPaletteId;

        private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

        public WarehouseUnitBuilder(EastMushroomsWarehouseRepository warehouseRepository, BeanValidator beanValidator, TypeFactory typeFactory) {
            this.warehouseRepository = warehouseRepository;
            this.beanValidator = beanValidator;
            this.typeFactory = typeFactory;
        }

        public WarehouseUnitBuilder warehousePalette(WarehousePalette warehousePalette) {
            this.warehousePalette = warehousePalette;
            return this;
        }

        public WarehouseUnitBuilder pickerId(long pickerId) {
            this.pickerId = pickerId;
            return this;
        }

        public WarehouseUnitBuilder typeId(long typeId) {
            this.typeId = typeId;
            return this;
        }

        public WarehouseUnitBuilder uniqId(long uniqId) {
            this.uniqId = uniqId;
            return this;
        }

        public WarehouseUnitBuilder harvestDate(Date harvestTime) {
            this.harvestTime = harvestTime;
            return this;
        }

        public WarehouseUnitBuilder scannerManId(long scannerManId) {
            this.scannerManId = scannerManId;
            return this;
        }

        public WarehouseUnitBuilder trolleyManId(Integer trolleyManId){
            this.trolleyManId = trolleyManId;
            return this;
        }

        public WarehouseUnitBuilder cycleId(long cycleId) {
            this.cycleId = cycleId;
            return this;
        }

        public WarehouseUnitBuilder harvestPaletteId(long harvestPaletteId) {
            this.harvestPaletteId = harvestPaletteId;
            return this;
        }

        public WarehouseUnit build() {
            WarehouseUnit warehouseUnit = new WarehouseUnit(warehouseRepository, beanValidator, typeFactory);
            warehouseUnit.warehousePalette = warehousePalette;
            warehouseUnit.pickerId = pickerId;
            warehouseUnit.localTypeId = typeId;
            warehouseUnit.uniqId = uniqId;
            warehouseUnit.harvestTime = harvestTime;
            warehouseUnit.scannerManId = scannerManId;
            warehouseUnit.trolleyManId = trolleyManId;
            warehouseUnit.cycleId = cycleId;
            warehouseUnit.status = WarehouseUnitStatus.CREATED;
            warehouseUnit.harvestPaletteId = harvestPaletteId;
            warehouseUnit.save();
            warehousePalette.getUnits().add(warehouseUnit);
            return warehouseUnit;
        }
    }
}
