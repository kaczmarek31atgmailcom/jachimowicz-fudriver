package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.wz;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.customer.command.model.Customer;
import com.fungisearch.fudriver.customer.command.model.CustomerFactory;
import com.fungisearch.fudriver.cycle.command.model.CycleFactory;
import com.fungisearch.fudriver.person.person.command.model.PersonFactory;
import com.fungisearch.fudriver.reclassification.command.model.RodzajSkupFactory;
import com.fungisearch.fudriver.settings.command.model.Company;
import com.fungisearch.fudriver.settings.command.model.CompanyFactory;
import com.fungisearch.fudriver.type.command.model.TypeFactory;
import com.fungisearch.fudriver.user.command.model.UserFactory;
import com.fungisearch.fudriver.user.query.dto.UserDto;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehousePaletteFactory;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.WzRepository;
import com.fungisearch.fudriver.warehouseEastMushrooms.web.ProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "east_mushrooms_wz_document")
public class WzDocument extends BaseEntity {
    transient WzRepository wzRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "company_street")
    private String companyStreet;

    @Column(name = "company_city")
    private String companyCity;

    @Column(name = "company_nip")
    private String companyNip;

    @Column(name = "company_ggn")
    private String companyGGN;

    @Column(name = "date")
    @NotNull
    private Date date;

    @Column(name = "creator_id")
    @NotNull
    private Long creatorId;

    @Column(name = "creator_login")
    private String creatorLogin;

    @Column(name = "creator_name")
    private String creatorName;

    @Column(name = "creator_surname")
    private String creatorSurname;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(targetEntity = WzUnit.class, fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "wzDocument")
    private Set<WzUnit> wzUnits;

    @ManyToOne
    @JoinColumn(name = "shipment_id")
    private Shipment shipment;


    public WzDocument(WzRepository wzRepository, BeanValidator beanValidator) {
        this.wzRepository = wzRepository;
        this.beanValidator = beanValidator;
    }

    private WzDocument(){}

    public Long getId() {
        return id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyStreet() {
        return companyStreet;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public String getCompanyNip() {
        return companyNip;
    }

    public String getCompanyGGN() {
        return companyGGN;
    }

    public Date getDate() {
        return date;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public String getCreatorLogin() {
        return creatorLogin;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorSurname() {
        return creatorSurname;
    }

    public Company getCompany() {
        return company;
    }

    public Set<WzUnit> getWzUnits() {
        return wzUnits;
    }

    public Shipment getShipment() {
        return shipment;
    }

    private void save(){
        beanValidator.validate(this);
        wzRepository.save(this);
    }

    public void addWzUnit(WzUnit wzUnit) {
        this.wzUnits.add(wzUnit);
    }

    public static class WzDocumentBuilder{
        private final WzRepository wzRepository;
        private final BeanValidator beanValidator;
        private final UserService userService;

        private Customer customer;
        private Company company;
        private Shipment shipment;

        public WzDocumentBuilder(WzRepository wzRepository, BeanValidator beanValidator, UserService userService) {
            this.wzRepository = wzRepository;
            this.beanValidator = beanValidator;
            this.userService = userService;
        }

        public WzDocumentBuilder customer (Customer customer){
            this.customer = customer;
            return this;
        }

        public WzDocumentBuilder company(Company company){
            this.company = company;
            return this;
        }

        public WzDocumentBuilder shipment(Shipment shipment){
            this.shipment = shipment;
            return this;
        }

        public WzDocument buid(){

            WzDocument wzDocument = new WzDocument(wzRepository,beanValidator);
            wzDocument.date =  new Date();
            UserDto creator = userService.getCurrentUser();
            wzDocument.creatorId = creator.id;
            wzDocument.creatorLogin = creator.login;
            wzDocument.creatorName = creator.name;
            wzDocument.creatorSurname = creator.surname;
            wzDocument.customerId = customer.getId();
            wzDocument.customerName = customer.getName();
            wzDocument.company = company;
            wzDocument.companyName = company.getName();
            wzDocument.companyCity = company.getCity();
            wzDocument.companyStreet = company.getStreet();
            wzDocument.companyNip = company.getNip();
            wzDocument.companyGGN = company.getGgn();
            wzDocument.shipment = shipment;
            wzDocument.wzUnits = new HashSet<>();
            wzDocument.save();

            return wzDocument;

        }
    }
}
