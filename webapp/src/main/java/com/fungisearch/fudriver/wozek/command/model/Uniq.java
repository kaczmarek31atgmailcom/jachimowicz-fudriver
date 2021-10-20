package com.fungisearch.fudriver.wozek.command.model;

import com.fungisearch.fudriver.exception.BarcodeAlreadyUsedException;
import com.fungisearch.fudriver.exception.BarcodeNotReservedException;
import com.fungisearch.fudriver.exception.BarcodeNotUtilisedException;
import com.fungisearch.fudriver.validation.BeanValidator;
import com.fungisearch.fudriver.wozek.command.repository.UniqRepository;
import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

/**
 * Created by marcin on 23.02.16.
 */
@Entity
@Table(name = "uniq")
public class Uniq {


    @Autowired
    private transient UniqRepository uniqRepository;

    @Autowired
    private transient BeanValidator beanValidator;

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "ludzie_id")
    private Long pickerId;

    @Column(name = "uniq_id")
    private Long uniqId;

    @Column(name = "used", nullable = false)
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean used;

    @SuppressWarnings("unused")
    public Uniq() {
    }

    public Uniq(UniqRepository uniqRepository, BeanValidator beanValidator) {
        this.uniqRepository = uniqRepository;
        this.beanValidator = beanValidator;
    }

    public Long getId() {
        return id;
    }

    public Long getPickerId() {
        return pickerId;
    }

    public Long getUniqId() {
        return uniqId;
    }

    public Boolean isUsed() {
        return used;
    }

    public void setUniqRepository(UniqRepository uniqRepository) {
        this.uniqRepository = uniqRepository;
    }

    public void setBeanValidator(BeanValidator beanValidator) {
        this.beanValidator = beanValidator;
    }

    @Transactional
    public void utilise() throws BarcodeAlreadyUsedException, BarcodeNotReservedException {
        if (this.isUsed()) {
            throw new BarcodeAlreadyUsedException(this.pickerId, this.uniqId);
        }
        this.used = true;
        beanValidator.validate(this);
    }

    public void reclaim() throws BarcodeNotUtilisedException, BarcodeNotReservedException {
        if (!this.used) {
            throw new BarcodeNotUtilisedException(this.pickerId, this.uniqId);
        }
        if (this.used == null) {
            throw new BarcodeNotReservedException(this.pickerId, this.uniqId);
        }
        this.used = false;
        beanValidator.validate(this);
    }

    public void deleteIfNotUsed() {
        if (!this.used) {
            uniqRepository.delete(this);
        }
    }

    public void create() {
        beanValidator.validate(this);
        uniqRepository.save(this);
    }

    public static class UniqBuilder {
        private UniqRepository uniqRepository;
        private BeanValidator beanValidator;
        private Long pickerId;
        private Long uniqId;
        private Boolean used;

        public UniqBuilder(UniqRepository uniqRepository, BeanValidator beanValidator) {
            this.uniqRepository = uniqRepository;
            this.beanValidator = beanValidator;
        }

        public UniqBuilder pickerId(Long pickerId) {
            this.pickerId = pickerId;
            return this;
        }

        public UniqBuilder uniqId(Long uniqId) {
            this.uniqId = uniqId;
            return this;
        }

        public UniqBuilder used(Boolean used) {
            this.used = used;
            return this;
        }

        public Uniq build() {
            Uniq uniq = new Uniq(this.uniqRepository, this.beanValidator);
            uniq.pickerId = this.pickerId;
            uniq.uniqId = this.uniqId;
            uniq.used = false;
            return uniq;
        }
    }
}