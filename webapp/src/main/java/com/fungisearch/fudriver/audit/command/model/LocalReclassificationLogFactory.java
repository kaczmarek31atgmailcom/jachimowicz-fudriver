package com.fungisearch.fudriver.audit.command.model;

import com.fungisearch.fudriver.audit.command.repository.AuditRepository;
import com.fungisearch.fudriver.validation.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by marcin on 03.08.16.
 */
@Repository
public class LocalReclassificationLogFactory {

    @Autowired
    private AuditRepository auditRepository;

    @Autowired
    private BeanValidator beanValidator;

    public LocalReclassificationLog.LocalReclassificationLogBuilder builder(){
        return new LocalReclassificationLog.LocalReclassificationLogBuilder(auditRepository,beanValidator);
    }
}
