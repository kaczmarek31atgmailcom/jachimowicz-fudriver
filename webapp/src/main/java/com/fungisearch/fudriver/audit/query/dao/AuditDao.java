package com.fungisearch.fudriver.audit.query.dao;

import com.fungisearch.fudriver.audit.query.dto.LocalReclassificationAuditDto;

import java.util.Date;
import java.util.List;

/**
 * Created by marcin on 03.08.16.
 */
public interface AuditDao {
    List<LocalReclassificationAuditDto> findReclassifications(Date startDate, Date endDate);
}
