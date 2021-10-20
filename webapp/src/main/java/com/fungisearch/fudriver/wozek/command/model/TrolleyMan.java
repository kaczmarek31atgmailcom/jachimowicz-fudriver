package com.fungisearch.fudriver.wozek.command.model;

import com.fungisearch.fudriver.wozek.command.repository.TrolleyManRepository;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "trolley_man")
public class TrolleyMan {

    protected transient TrolleyManRepository trolleyManRepository;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Integer id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "surname")
    @NotNull
    private String surname;

    @Column(name = "active")
    private boolean active = false;


    public TrolleyMan(TrolleyManRepository trolleyManRepository) {
        this.trolleyManRepository = trolleyManRepository;
    }

    private TrolleyMan() {
    }

    public void save() {
        trolleyManRepository.save(this);
    }

    public static class Edit {
        private String name;
        private String surname;
        private Boolean active;

        public Edit name(String name) {
            this.name = name;
            return this;
        }

        public Edit surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Edit active(Boolean active) {
            this.active = active;
            return this;
        }
    }

    public void edit(Edit edit) {
        if (edit.name != null) {
            this.name = edit.name;
        }
        if (edit.surname != null) {
            this.surname = edit.surname;
        }
        if (edit.active != null) {
            this.active = active;
        }
    }

    public static class TrolleyManBuilder {
        private final TrolleyManRepository trolleyManRepository;
        private String name;
        private String surname;

        public TrolleyManBuilder(TrolleyManRepository trolleyManRepository) {
            this.trolleyManRepository = trolleyManRepository;
        }

        public TrolleyManBuilder name(String name) {
            this.name = name;
            return this;
        }

        public TrolleyManBuilder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public TrolleyMan build() {
            TrolleyMan trolleyMan = new TrolleyMan(trolleyManRepository);
            trolleyMan.name = name;
            trolleyMan.surname = surname;
            trolleyMan.active = true;
            trolleyMan.save();
            return trolleyMan;
        }
    }
}
