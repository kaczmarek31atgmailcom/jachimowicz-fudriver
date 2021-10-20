package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkup;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupFactory;
import com.fungisearch.fudriver.type.command.model.Type;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.command.model.User;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePalette;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.WzRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "east_mushrooms_shipment_palette")
public class ShipmentPalette extends BaseEntity {
    transient WzRepository wzRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;

    @OneToOne
    @JoinColumn(name = "warehouse_palette_id")
    @NotNull
    private WarehousePalette warehousePalette;

    @Column(name = "palette_type_id")
    private Integer paletteTypeId;

    @Column(name = "warehouse_palette_creation_date")
    private Date warehouseCreationDate;

    @Column(name = "warehouse_palette_creator_id")
    private Long warehousePaletteCreatorId;

    @Column(name = "warehouse_palette_creator_login")
    private String warehousePaletteCeatorLogin;

    @Column(name = "warehouse_palette_creator_name")
    private String warehousePaletteCreatorName;

    @Column(name = "warehouse_palette_creator_surname")
    private String warehousePalettecreatorSurname;

    @OneToMany(targetEntity = WzUnit.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "shipmentPalette")
    private Set<WzUnit> wzUnits;


    public ShipmentPalette(WzRepository wzRepository, BeanValidator beanValidator) {
        this.wzRepository = wzRepository;
        this.beanValidator = beanValidator;
    }

    private ShipmentPalette() {
    }

    private void save() {
        beanValidator.validate(this);
        wzRepository.save(this);
    }


    public static class ShipmentPaletteBuilder {
        private final WzRepository wzRepository;
        private final BeanValidator beanValidator;
        private final UserFactory userFactory;
        private final CycleFactory cycleFactory;
        private final PersonFactory personFactory;
        private final TypeFactory typeFactory;
        private final RodzajSkupFactory rodzajSkupFactory;
        private Shipment shipment;
        private WarehousePalette warehousePalette;
        private Integer paletteTypeId;
        private List<WzDocument> wzDocuments;

        private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());


        public ShipmentPaletteBuilder(WzRepository wzRepository, BeanValidator beanValidator, UserFactory userFactory, CycleFactory cycleFactory, PersonFactory personFactory, TypeFactory typeFactory, RodzajSkupFactory rodzajSkupFactory) {
            this.wzRepository = wzRepository;
            this.beanValidator = beanValidator;
            this.userFactory = userFactory;
            this.cycleFactory = cycleFactory;
            this.personFactory = personFactory;
            this.typeFactory = typeFactory;
            this.rodzajSkupFactory = rodzajSkupFactory;
        }

        public ShipmentPaletteBuilder shipment(Shipment shipment) {
            this.shipment = shipment;
            return this;
        }

        public ShipmentPaletteBuilder warehousePalette(WarehousePalette warehousePalette) {
            this.warehousePalette = warehousePalette;
            return this;
        }

        public ShipmentPaletteBuilder paletteTypeId(Integer paletteTypeId){
            this.paletteTypeId = paletteTypeId;
            return this;
        }

        public ShipmentPaletteBuilder wzDocuments(List<WzDocument> wzDocuments){
            this.wzDocuments = wzDocuments;
            return this;
}

        public ShipmentPalette build() {
            ShipmentPalette shipmentPalette = new ShipmentPalette(wzRepository, beanValidator);
            List<Type> localTypes = typeFactory.findAll();
            List<RodzajSkup> remoteTypes = rodzajSkupFactory.findAll();
            shipmentPalette.shipment = shipment;
            shipmentPalette.warehousePalette = warehousePalette;
            shipmentPalette.paletteTypeId = paletteTypeId;
            shipmentPalette.warehouseCreationDate = warehousePalette.getCreationTime();
            User warehousePaletteCreator = userFactory.find(warehousePalette.getCreatorId());
            shipmentPalette.warehousePaletteCreatorId = warehousePaletteCreator.getId();
            shipmentPalette.warehousePaletteCeatorLogin = warehousePaletteCreator.getLogin();
            shipmentPalette.warehousePaletteCreatorName = warehousePaletteCreator.getName();
            shipmentPalette.warehousePalettecreatorSurname = warehousePaletteCreator.getSurname();
            shipmentPalette.wzUnits = new HashSet<>();
            shipmentPalette.save();

            shipmentPalette.wzUnits = warehousePalette.getUnits().stream().map(o -> new WzUnit.WzUnitBuilder(wzRepository, beanValidator, cycleFactory, personFactory)
                    .cycleId(o.getCycleId())
                    .harvestDate(o.getHarvestTime())
                    .localTypeId(o.getLocalTypeId())
                    .localTypeName(findTypeById(localTypes, o.getLocalTypeId()).getName())
                    .localTypeWeight(findTypeById(localTypes, o.getLocalTypeId()).getWeight())
                    .pickerId(o.getPickerId())
                    .remoteTypeId(findRemoteTypeId(o.getLocalTypeId(),localTypes))
                    .remoteTypeName(findRemoteTypeName(o.getLocalTypeId(),localTypes,remoteTypes))
                    .remoteTypeWeight(findRemoteTypeWeight(o.getLocalTypeId(),localTypes,remoteTypes))
                    .uniqId(o.getUniqId())
                    .shipmentPalette(shipmentPalette)
                    .wzDocuments(wzDocuments)
                    .build()
            ).collect(Collectors.toSet());
            warehousePalette.release();
            return shipmentPalette;
        }

        private Long findRemoteTypeId(Long localTypeId,List<Type> localTypes){
            Type type = findTypeById(localTypes,localTypeId);
            if(type != null){
                return type.getRemoteTypeId();
            }
            return null;
        }

        private String findRemoteTypeName(Long localTypeId,List<Type> localTypes,List<RodzajSkup> remoteTypes){
            Type type = findTypeById(localTypes,localTypeId);
            if(type != null){
                if(type.getRemoteTypeId() != null){
                    RodzajSkup rodzajSkup = findRemoteTypeById(remoteTypes,type.getRemoteTypeId());
                    if(rodzajSkup != null){
                        return rodzajSkup.getName();
                    }
                }
            }
            return null;
        }

        private Double findRemoteTypeWeight(Long localTypeId,List<Type> localTypes,List<RodzajSkup> remoteTypes){
            Type type = findTypeById(localTypes,localTypeId);
            if(type != null){
                if(type.getRemoteTypeId() != null){
                    RodzajSkup rodzajSkup = findRemoteTypeById(remoteTypes,type.getRemoteTypeId());
                    if(rodzajSkup != null){
                        return rodzajSkup.getWeight();
                    }
                }
            }
            return null;
        }

        private Type findTypeById(List<Type> types, Long id) {
            return types.stream().filter(o -> o.getId().equals(id)).findFirst().orElse(null);

        }

        private RodzajSkup findRemoteTypeById(List<RodzajSkup> remoteTypes, Long remoteTypeId){
            for(RodzajSkup rodzajSkup: remoteTypes ){
                if(remoteTypeId.equals(rodzajSkup.getId())){
                    return rodzajSkup;
                }
            }
        return null;
        }
    }
}
