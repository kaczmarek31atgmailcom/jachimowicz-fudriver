package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz;

import com.fungisearch.fudriver.customer.command.model.Customer;
import com.fungisearch.fudriver.customer.command.model.CustomerFactory;
import com.fungisearch.fudriver.cycle.command.model.Cycle;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupFactory;
import com.fungisearch.fudriver.settings.command.model.Chamber;
import com.fungisearch.fudriver.settings.command.model.Company;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.dto.UserDto;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePalette;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehouseUnit;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.WzRepository;
import com.fungisearch.fudriver.warehouseEastMushrooms.web.ProxyService;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "east_mushrooms_shipment")
public class Shipment {

    transient WzRepository wzRepository;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "monthly_no")
    private Integer monthlyNo;

    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "creator_id")
    private Long creatorId;

    @Column(name = "creator_login")
    private String creatorLogin;

    @Column(name = "creator_name")
    private String creatorName;

    @Column(name = "creator_surname")
    private String creatorSurname;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_address")
    private String customerAddress;


    @OneToMany(targetEntity = ShipmentPalette.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "shipment")
    private Set<ShipmentPalette> palettes;

    public Shipment(WzRepository wzRepository) {
        this.wzRepository = wzRepository;
    }

    private Shipment() {
    }


    public Long getId() {
        return id;
    }

    private void create() {
        wzRepository.save(this);
    }

    public static class ShipmentBuilder {
        private final WzRepository wzRepository;
        private final UserService userService;
        private final WarehousePaletteFactory warehousePaletteFactory;
        private final BeanValidator beanValidator;
        private final UserFactory userFactory;
        private final CycleFactory cycleFactory;
        private final PersonFactory personFactory;
        private final TypeFactory typeFactory;
        private final RodzajSkupFactory rodzajSkupFactory;
        private final CustomerFactory customerFactory;
        private final ProxyService proxyService;

        private List<Long> warehousePalettesIds;
        private Customer customer;


        public ShipmentBuilder(WzRepository wzRepository, UserService userService, WarehousePaletteFactory warehousePaletteFactory, BeanValidator beanValidator, UserFactory userFactory, CycleFactory cycleFactory, PersonFactory personFactory, TypeFactory typeFactory, RodzajSkupFactory rodzajSkupFactory, CustomerFactory customerFactory, ProxyService proxyService) {
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

        public ShipmentBuilder warehousePalettesIds(List<Long> warehousePalettesIds) {
            this.warehousePalettesIds = warehousePalettesIds;
            return this;
        }

        public ShipmentBuilder customerId(Long customerId) {
            this.customer = customerFactory.find(customerId);
            return this;
        }

        public Shipment build() {
            UserDto creator = userService.getCurrentUser();
            Shipment shipment = new Shipment(wzRepository);
            shipment.creatorId = creator.id;
            shipment.creatorLogin = creator.login;
            shipment.creatorName = creator.name;
            shipment.creatorSurname = creator.surname;
            shipment.customer = customer;
            shipment.customerName = customer.getName();
            shipment.customerAddress = customer.getAddress();
            shipment.creationDate = new Date();
            shipment.palettes = new HashSet<>();
            shipment.create();


            List<WarehousePalette> warehousePalettes = warehousePalettesIds
                    .stream()
                    .map(wpId -> warehousePaletteFactory.find(wpId))
                    .collect(Collectors.toList());

            List<WzDocument> wzDocuments = createWzDocuments(warehousePalettes, customer, shipment);

            shipment.palettes = warehousePalettes
                    .stream()
                    .map(wp -> new ShipmentPalette.ShipmentPaletteBuilder(wzRepository, beanValidator, userFactory, cycleFactory, personFactory, typeFactory, rodzajSkupFactory)
                            .shipment(shipment)
                            .warehousePalette(wp)
                            .paletteTypeId(wp.getPaletteTypeId())
                            .wzDocuments(wzDocuments)
                            .build())
                    .collect(Collectors.toSet());
            return shipment;
        }


        private List<WzDocument> createWzDocuments(List<WarehousePalette> warehousePalettes, Customer customer, Shipment shipment) {
            List<WzDocument> wzDocuments = findUniqueCompanies(warehousePalettes)
                    .stream()
                    .map(c -> new WzDocument.WzDocumentBuilder(wzRepository, beanValidator, userService)
                            .company(c)
                            .customer(customer)
                            .shipment(shipment)
                            .buid())
                    .collect(Collectors.toList());
            return wzDocuments;
        }

        public List<Company> findUniqueCompanies(List<WarehousePalette> warehousePalettes) {
            Set<WarehouseUnit> warehouseUnits = new HashSet<>();
            warehousePalettes.forEach(w -> warehouseUnits.addAll(w.getUnits()));
            Set<Long> cycleIds = warehouseUnits
                    .stream()
                    .filter(Objects::nonNull)
                    .map(u -> u.getCycleId())
                    .collect(Collectors.toSet());

            List<Cycle> cycles = cycleIds
                    .stream()
                    .filter(Objects::nonNull)
                    .map(c -> cycleFactory.find(c))
                    .collect(Collectors.toList());

            Set<Chamber> chambers = new HashSet<>();
            chambers = cycles
                    .stream()
                    .filter(Objects::nonNull)
                    .map(c -> c.getChamber())
                    .collect(Collectors.toSet());

            Set<Company> companies = chambers
                    .stream()
                    .filter(Objects::nonNull)
                    .map(c -> c.getCompany())
                    .collect(Collectors.toSet());

            return new ArrayList<>(companies);
        }


    }
}

