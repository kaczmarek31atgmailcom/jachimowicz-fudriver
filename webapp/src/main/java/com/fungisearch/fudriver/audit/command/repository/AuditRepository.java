package com.fungisearch.fudriver.audit.command.repository;

import com.fungisearch.fudriver.audit.command.model.LocalReclassificationLog;

/**
 * Created by marcin on 03.08.16.
 */
public interface AuditRepository {
    void saveLocalReclassificationLog(LocalReclassificationLog log);
}
