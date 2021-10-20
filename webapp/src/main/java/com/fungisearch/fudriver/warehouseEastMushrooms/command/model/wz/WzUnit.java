package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.person.person.command.model.Person;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.WzRepository;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "east_mushrooms_wz_unit")
public class WzUnit extends BaseEntity {
    transient WzRepository wzRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    public Long id;

    @ManyToOne
    @JoinColumn(name = "shipment_palette_id")
    private ShipmentPalette shipmentPalette;

    @Column(name = "uniq_id")
    @NotNull
    private long uniqId;

    @Column(name = "local_type_id")
    @NotNull
    private long localTypeId;

    @Column(name = "local_type_name")
    @NotNull
    private String localTypeName;

    @Column(name = "local_type_weight")
    @NotNull
    private double localTypeWeight;

    @Column(name = "remote_type_id")
    private Long remoteTypeId;

    @Column(name = "remote_type_name")
    private String remoteTypeName;

    @Column(name = "remote_type_weight")
    private Double remoteTypeWeight;

    @Column(name = "harvest_date")
    private Date harvestDate;

    @ManyToOne
    @JoinColumn(name = "cycle_id")
    private Cycle cycle;

    @Column(name = "chamber_name")
    private String chamberName;

    @Column(name = "picker_id")
    @NotNull
    private long pickerId;

    @Column(name = "picker_name")
    private String pickerName;

    @Column(name = "picker_surname")
    private String pickerSurname;

    @Column(name = "company_id")
    private Long companyId;

    @ManyToOne
    @JoinColumn(name = "wz_id")
    private WzDocument wzDocument;

    public WzUnit(WzRepository wzRepository, BeanValidator beanValidator) {
        this.wzRepository = wzRepository;
        this.beanValidator = beanValidator;
    }

    private WzUnit(){}

    private void save(){
        beanValidator.validate(this);
        wzRepository.save(this);
    }


    public static class WzUnitBuilder{
        private final WzRepository wzRepository;
        private final BeanValidator beanValidator;
        private final CycleFactory cycleFactory;
        private final PersonFactory personFactory;

        private ShipmentPalette shipmentPalette;
        private long uniqId;
        private long localTypeId;
        private String localTypeName;
        private double localTypeWeight;
        private Long remoteTypeId;
        private String remoteTypeName;
        private Double remoteTypeWeight;
        private Date harvestDate;
        private long cycleId;
        private long pickerId;
        private List<WzDocument> wzDocuments;

        public WzUnitBuilder(WzRepository wzRepository, BeanValidator beanValidator, CycleFactory cycleFactory, PersonFactory personFactory) {
            this.wzRepository = wzRepository;
            this.beanValidator = beanValidator;
            this.cycleFactory = cycleFactory;
            this.personFactory = personFactory;
        }

        public WzUnitBuilder shipmentPalette(ShipmentPalette shipmentPalette){
            this.shipmentPalette = shipmentPalette;
            return this;
        }

        public WzUnitBuilder uniqId(long uniqId){
            this.uniqId = uniqId;
            return this;
        }

        public WzUnitBuilder localTypeId(long localTypeId){
            this.localTypeId = localTypeId;
            return this;
        }

        public WzUnitBuilder localTypeName(String localTypeName){
            this.localTypeName = localTypeName;
            return this;
        }

        public WzUnitBuilder localTypeWeight(double localTypeWeight){
            this.localTypeWeight = localTypeWeight;
            return this;
        }

        public WzUnitBuilder remoteTypeId(Long remoteTypeId){
            this.remoteTypeId = remoteTypeId;
            return this;
        }

        public WzUnitBuilder remoteTypeName(String remoteTypeName){
            this.remoteTypeName = remoteTypeName;
            return this;
        }

        public WzUnitBuilder remoteTypeWeight(Double remoteTypeWeight){
            this.remoteTypeWeight = remoteTypeWeight;
            return this;
        }

        public WzUnitBuilder harvestDate(Date harvestDate){
            this.harvestDate = harvestDate;
            return this;
        }

        public WzUnitBuilder cycleId(long cycleId){
            this.cycleId = cycleId;
            return this;
        }


        public WzUnitBuilder pickerId(long pickerId){
            this.pickerId = pickerId;
            return this;
        }

        public WzUnitBuilder wzDocuments(List<WzDocument> wzDocuments){
            this.wzDocuments = wzDocuments;
            return this;
        }


        public WzUnit build(){
            WzUnit wzUnit = new WzUnit(wzRepository,beanValidator);
            wzUnit.localTypeId = localTypeId;
            wzUnit.localTypeName = localTypeName;
            wzUnit.localTypeWeight = localTypeWeight;
            wzUnit.remoteTypeId = remoteTypeId;
            wzUnit.remoteTypeName = remoteTypeName;
            wzUnit.remoteTypeWeight = remoteTypeWeight;
            wzUnit.harvestDate = harvestDate;
            wzUnit.cycle = cycleFactory.find(cycleId);
            wzUnit.chamberName = wzUnit.cycle.getChamber().getName();
            wzUnit.pickerId = pickerId;
            Person person = personFactory.find(pickerId);
            wzUnit.pickerName = person.getName();
            wzUnit.pickerSurname = person.getSurname();
            wzUnit.shipmentPalette = shipmentPalette;
            wzUnit.uniqId = uniqId;
            wzUnit.companyId = wzUnit.cycle.getChamber().getCompany().getId();
            WzDocument wzDocument = wzDocuments.stream().filter(doc -> doc.getCompany().getId().equals(wzUnit.companyId)).findAny().orElse(null);
            wzUnit.wzDocument = wzDocument;
            wzUnit.save();
            wzDocument.addWzUnit(wzUnit);
            return wzUnit;
        }
    }
}
