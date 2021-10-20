package com.fungisearch.fudriver.reclassification.command.model;

import com.fungisearch.fudriver.reclassification.command.repository.RodzajSkupRepository;
import com.fungisearch.fudriver.validation.BeanValidator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "skup_rodzaj_grupa")
public class RodzajSkupGrupa {

    transient RodzajSkupRepository rodzajSkupRepository;
    transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "group_id")
    @NotNull
    private Long groupId;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "active")
    private boolean active = true;

    private RodzajSkupGrupa() {
    }

    public RodzajSkupGrupa(RodzajSkupRepository rodzajSkupRepository, BeanValidator beanValidator) {
        this.rodzajSkupRepository = rodzajSkupRepository;
        this.beanValidator = beanValidator;
    }

    private void save() {
        beanValidator.validate(this);
        rodzajSkupRepository.save(this);
    }

    public static class RodzajSkupGrupaBuilder {
        private final RodzajSkupRepository rodzajSkupRepository;
        private final BeanValidator beanValidator;
        private long groupId;
        private String groupName;


        public RodzajSkupGrupaBuilder(RodzajSkupRepository rodzajSkupRepository, BeanValidator beanValidator) {
            this.rodzajSkupRepository = rodzajSkupRepository;
            this.beanValidator = beanValidator;
        }


        public RodzajSkupGrupaBuilder groupId(long groupId) {
            this.groupId = groupId;
            return this;
        }

        public RodzajSkupGrupaBuilder groupName(String groupName) {
            this.groupName = groupName;
            return this;
        }

        public void build() {
            RodzajSkupGrupa existingOne = rodzajSkupRepository.findByGroupIdRodzajSkupGrupa(groupId);
            if (existingOne == null) {
                RodzajSkupGrupa rodzajSkupGrupa = new RodzajSkupGrupa(rodzajSkupRepository, beanValidator);
                rodzajSkupGrupa.groupId = groupId;
                rodzajSkupGrupa.groupName = groupName;
                rodzajSkupGrupa.save();
            }
        }
    }
}
