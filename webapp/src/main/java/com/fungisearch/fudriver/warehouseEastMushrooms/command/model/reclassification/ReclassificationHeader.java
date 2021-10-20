package com.fungisearch.fudriver.warehouseEastMushrooms.command.model.reclassification;

import com.fungisearch.fudriver.common.command.BaseEntity;
import com.fungisearch.fudriver.user.query.dto.UserDto;
import com.fungisearch.fudriver.user.query.service.UserService;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.model.warehouse.WarehouseUnitFactory;
import com.fungisearch.fudriver.warehouseEastMushrooms.command.repository.EastMushroomsWarehouseRepository;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "east_mushrooms_reclassification_header")
public class ReclassificationHeader extends BaseEntity {

    transient EastMushroomsWarehouseRepository warehouseRepository;
    transient BeanValidator beanValidator;
    transient WarehouseUnitFactory warehouseUnitFactory;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "login")
    private String login;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_surname")
    private String userSurname;

    @OneToMany(targetEntity = ReclassificationDetail.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "reclassificationHeader")
    private Set<ReclassificationDetail> details;

    private ReclassificationHeader() {
    }

    public ReclassificationHeader(EastMushroomsWarehouseRepository warehouseRepository, BeanValidator beanValidator, WarehouseUnitFactory warehouseUnitFactory) {
        this.warehouseRepository = warehouseRepository;
        this.beanValidator = beanValidator;
        this.warehouseUnitFactory = warehouseUnitFactory;
    }


    public Set<ReclassificationDetail> getDetails() {
        return details;
    }

    private void save() {
        beanValidator.validate(this);
        warehouseRepository.save(this);
    }

    public Long getId() {
        return this.id;
    }

    public static class ReclassificationHeaderBuilder {
        private final EastMushroomsWarehouseRepository warehouseRepository;
        private final BeanValidator beanValidator;
        private final UserService userService;
        private final WarehouseUnitFactory warehouseUnitFactory;
        private List<Long> idsToBeReclassified;
        private Long targetTypeId;

        public ReclassificationHeaderBuilder(EastMushroomsWarehouseRepository warehouseRepository, BeanValidator beanValidator, UserService userService, WarehouseUnitFactory warehouseUnitFactory) {
            this.warehouseRepository = warehouseRepository;
            this.beanValidator = beanValidator;
            this.userService = userService;
            this.warehouseUnitFactory = warehouseUnitFactory;
        }

        public ReclassificationHeaderBuilder idsToBeReclassified(List<Long> idsToBeReclassified) {
            this.idsToBeReclassified = idsToBeReclassified;
            return this;
        }

        public ReclassificationHeaderBuilder targetTypeId(Long targetTypeId) {
            this.targetTypeId = targetTypeId;
            return this;
        }

        public ReclassificationHeader build() {
            ReclassificationHeader header = new ReclassificationHeader(warehouseRepository, beanValidator, warehouseUnitFactory);
            UserDto creator = userService.getCurrentUser();
            header.date = new Date();
            header.userId = creator.id;
            header.login = creator.login;
            header.userName = creator.name;
            header.userSurname = creator.surname;
            header.details = new HashSet<>();
            header.save();
            idsToBeReclassified.forEach(o -> warehouseUnitFactory.find(o).reclassify(targetTypeId,header));
            return header;
        }
    }

}
